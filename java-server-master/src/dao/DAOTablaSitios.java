package dao;


import java.sql.Connection; 
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;


import oracle.sql.TIMESTAMP;

import java.security.SecureRandom;
import java.util.List;
import java.math.BigInteger;

import vos.*;

/**
 * Clase DAO que se conecta la base de datos usando JDBC para resolver los requerimientos de la aplicación
 * @author Tomas Venegas
 */
public class DAOTablaSitios {

	/**
	 * Arraylits de recursos que se usan para la ejecución de sentencias SQL
	 */
	private ArrayList<Object> recursos;

	/**
	 * Atributo que genera la conexión a la base de datos
	 */
	private Connection conn;

	/**
	 * Método constructor que crea DAOVideo
	 * <b>post: </b> Crea la instancia del DAO e inicializa el Arraylist de recursos
	 */
	public  DAOTablaSitios() {
	
		recursos = new ArrayList<Object>();
	}

	/**
	 * Método que cierra todos los recursos que estan enel arreglo de recursos
	 * <b>post: </b> Todos los recurso del arreglo de recursos han sido cerrados
	 */
	public void cerrarRecursos() {
		for(Object ob : recursos){
			if(ob instanceof PreparedStatement)
				try {
					((PreparedStatement) ob).close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
		}
	}

	/**
	 * Método que inicializa la connection del DAO a la base de datos con la conexión que entra como parámetro.
	 * @param con  - connection a la base de datos
	 */
	public void setConn(Connection con){
		this.conn = con;
	}

	/**
	 * M�todo que crea un sitio en la base de datos
	 * @param sitio
	 * @throws Exception
	 */
	public void crearSitio(Sitio sitio) throws Exception{

		conn.setAutoCommit(false);
		conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
		try{
			Long idSilleteria=null;
			String sql1="SELECT ID FROM SILLETERIA WHERE ID="+sitio.getTipoSilleteria().getId();
			PreparedStatement prepStmt = conn.prepareStatement(sql1);
			recursos.add(prepStmt);
			ResultSet result = prepStmt.executeQuery();
			if (result.next()){
				idSilleteria=Long.parseLong(result.getString("ID"));
			}else throw new Exception("Silleteria no encontrada.");

			String sql ="INSERT INTO SITIOS (ID,NOMBRE,ABIERTO,PROTECCIONLLUVIA,HORAAPERTURA,HORACIERRE,IDSILLETERIA) VALUES (IDSITIOS.NEXTVAL";

			sql += ",'"+sitio.getNombre()+"',";
			if (sitio.getAbierto()) sql+="'Y',";
			else sql+="'N',";
			if (sitio.getProteccionLluvia()) sql+="'Y',";
			else sql+="'N',";

			sql+="TO_TIMESTAMP('"+sitio.getHoraApertura()+"','RRRR-MM-dd HH24:MI:SS.FF' ),TO_TIMESTAMP('"+ sitio.getHoraCierre()+"','RRRR-MM-dd HH24:MI:SS.FF' )"
			+",";


			sql+=idSilleteria+")";

			System.out.println("SQL stmt:" + sql);

			prepStmt = conn.prepareStatement(sql);
			recursos.add(prepStmt);
			prepStmt.executeQuery();

			conn.commit();


		}catch(Exception e){
			conn.rollback();
			throw e;
		}finally{
			conn.setAutoCommit(true);
		}
	}

	/**
	 * M�todo que busca un sitio dado su id
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public Sitio buscarSitio(Long id) throws Exception {

		conn.setAutoCommit(false);
		conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
		Sitio sitio = new Sitio();
		try{
			String sql1="SELECT S.ID AS SID, S.ABIERTO AS SABIERTO, S.PROTECCIONLLUVIA AS SPROTECCIONLLUVIA, S.HORAAPERTURA AS SHORAAPERTURA,"
					+ "S.HORACIERRE AS SHORACIERRE, S.NOMBRE AS SNOMBRE,"
					+ "A.ID AS AID, A.NOMBRE AS ANOMBRE, E.COSTO AS ECOSTO, E.DESCRIPCION AS EDESCRIPCION, E.DURACION AS EDURACION, "
					+ "E.ID AS EID, E.NOMBRE AS ENOMBRE, E.IDIOMA AS EIDIOMA,"
					+ "F.ID AS FID, F.FECHA AS FFECHA, F.REALIZADO AS FREALIZADO "
					+ "FROM SITIOS S "
					+ "LEFT JOIN SILLETERIA SI "
					+ "ON S.IDSILLETERIA=SI.ID  "
					+ "LEFT JOIN ESPARA ON S.ID=ESPARA.IDSITIO "
					+ "LEFT JOIN APTOS A ON ESPARA.IDAPTOS=A.ID "
					+ "LEFT JOIN FUNCIONES F ON S.ID=F.IDSITIO "
					+ "LEFT JOIN ESPECTACULOS E ON F.IDESPECTACULO=E.ID "
					+ "WHERE S.ID="+id;
			PreparedStatement prepStmt = conn.prepareStatement(sql1);
			recursos.add(prepStmt);
			ResultSet result = prepStmt.executeQuery();
			List<Apto> aptos= new ArrayList<Apto>();
			List<Funcion> funciones = new ArrayList<Funcion>();
			Apto apto= new Apto();
			Funcion funcion = new Funcion();
			Espectaculo espectaculo = new Espectaculo();
			Apto lastApto=null;
			Funcion lastFuncion=null;
			if (result.next()){
				sitio.setId(Long.parseLong(result.getString("SID")));
				sitio.setAbierto(result.getString("SABIERTO").equals("V"));
				sitio.setProteccionLluvia(result.getString("SPROTECCIONLLUVIA").equals("V"));
				sitio.setHoraApertura(Timestamp.valueOf(result.getString("SHORAAPERTURA")));
				sitio.setHoraCierre(Timestamp.valueOf(result.getString("SHORACIERRE")));
				sitio.setNombre(result.getString("SNOMBRE"));

				//agregar atributos a las listas
				apto.setId(Long.parseLong(result.getString("AID")));
				apto.setNombre(result.getString("ANOMBRE"));
				aptos.add(apto);
				lastApto=apto;
				espectaculo.setCosto(Double.parseDouble(result.getString("ECOSTO")));
				espectaculo.setDescripcion(result.getString("EDESCRIPCION"));
				espectaculo.setDuracion(Integer.parseInt(result.getString("EDURACION")));
				espectaculo.setId(Long.parseLong(result.getString("EID")));
				espectaculo.setNombre(result.getString("ENOMBRE"));
				espectaculo.setIdioma(result.getString("EIDIOMA"));

				funcion.setEspectaculo(espectaculo);
				//funcion.setFecha(Timestamp.valueOf(result.getString("FFECHA")));
				funcion.setId(Long.parseLong(result.getString("FID")));
				funcion.setRealizado(result.getString("FREALIZADO").equals("V"));
				lastFuncion=funcion;
				funciones.add(funcion);

			}else throw new Exception("Sitio no encontrado.");

			while (result.next()){
				//agregar atributos a las listas
				espectaculo = new Espectaculo();
				apto= new Apto();
				funcion = new Funcion();

				apto.setId(Long.parseLong(result.getString("AID")));
				apto.setNombre(result.getString("ANOMBRE"));
				if (lastApto==null || lastApto.getId()!=apto.getId()){
					aptos.add(apto);
					lastApto=apto;
				}

				espectaculo.setCosto(Double.parseDouble(result.getString("ECOSTO")));
				espectaculo.setDescripcion(result.getString("EDESCRIPCION"));
				espectaculo.setDuracion(Integer.parseInt(result.getString("EDURACION")));
				espectaculo.setId(Long.parseLong(result.getString("EID")));
				espectaculo.setNombre(result.getString("ENOMBRE"));
				espectaculo.setIdioma(result.getString("EIDIOMA"));

				funcion.setEspectaculo(espectaculo);
				//funcion.setFecha( Timestamp.valueOf(result.getString("FFECHA")));
				funcion.setId(Long.parseLong(result.getString("FID")));
				funcion.setRealizado(result.getString("FREALIZADO").equals("V"));
				if(lastFuncion==null || lastFuncion.getId()!=funcion.getId()){
					funciones.add(funcion);
					lastFuncion=funcion;
				}

			}
			sitio.setAptos(aptos);
			sitio.setFunciones(funciones);

			conn.commit();

		}catch(Exception e){
			conn.rollback();
			throw e;
		}finally{
			conn.setAutoCommit(true);
		}
		return sitio;
	}

	public Sitio buscarSitioPreferencias(Long id, Long idUsuario) {
		// TODO Auto-generated method stub
		return null;
	}

}
