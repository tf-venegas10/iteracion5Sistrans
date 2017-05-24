package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import vos.Categoria;
import vos.CompaniaTeatro;
import vos.Espectaculo;
import vos.EspectaculoPopular;
import vos.ReporteEspectaculo;
import vos.RequerimientosTecnicos;

public class DAOTablaEspectaculos {


	/**
	 * Arraylits de recursos que se usan para la ejecuciÃ³n de sentencias SQL
	 */
	private ArrayList<Object> recursos;

	/**
	 * Atributo que genera la conexiÃ³n a la base de datos
	 */
	private Connection conn;

	/**
	 * MÃ©todo constructor que crea DAOVideo
	 * <b>post: </b> Crea la instancia del DAO e inicializa el Arraylist de recursos
	 */
	public DAOTablaEspectaculos() {
		recursos = new ArrayList<Object>();
	}

	/**
	 * MÃ©todo que cierra todos los recursos que estan enel arreglo de recursos
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

	public void setConn(Connection conn) {
		this.conn = conn;
	}

	/**
	 * Método que agrega un espectáculo a la base de datos
	 * @param espectaculo
	 */
	public void addEspectaculo(Espectaculo espectaculo)throws SQLException,Exception {

		conn.setAutoCommit(false);
		conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
		try{
			if(espectaculo.getCompanias()==null || espectaculo.getCompanias().isEmpty())throw new Exception("El espectáculo debe estar afiliado a por lo menos una compañía.");
			else if(espectaculo.getCategorias()==null || espectaculo.getCategorias().isEmpty())throw new Exception("El espectáculo debe tener por lo menos una categoría.");

			String sql = "INSERT INTO ESPECTACULOS VALUES (IDESPECTACULOS.NEXTVAL,'";
			sql+= espectaculo.getNombre()+"',"+espectaculo.getDuracion()+",'"+ espectaculo.getIdioma()+"',"
					+espectaculo.getCosto()+",'"+espectaculo.getDescripcion()+"')";

			PreparedStatement prepStmt = conn.prepareStatement(sql);
			System.out.println(sql);
			recursos.add(prepStmt);
			prepStmt.executeQuery();
			
			String sql2="SELECT IDESPECTACULOS.CURRVAL FROM DUAL";
			prepStmt = conn.prepareStatement(sql2);
			recursos.add(prepStmt);
			System.out.println(sql2);
			ResultSet result = prepStmt.executeQuery();
			ResultSetMetaData meta = result.getMetaData();
			Long id=new Long(0);
			if(result.next()){
				id=Long.parseUnsignedLong(result.getString(meta.getColumnName(1)));
			}

			for (CompaniaTeatro compania : espectaculo.getCompanias()) {
				sql = "INSERT INTO PRESENTAN VALUES ("+compania.getId()+","+id+")";
				System.out.println(sql);
				prepStmt = conn.prepareStatement(sql);
				recursos.add(prepStmt);
				prepStmt.executeQuery();
			}

			for (Categoria categoria : espectaculo.getCategorias()) {
				sql = "INSERT INTO HACEPARTE VALUES ("+id+","+categoria.getId()+")";
				prepStmt = conn.prepareStatement(sql);
				System.out.println(sql);
				recursos.add(prepStmt);
				prepStmt.executeQuery();
			}

			if(espectaculo.getRequerimientosTecnicos()!=null){
				for (RequerimientosTecnicos req : espectaculo.getRequerimientosTecnicos()) {
					sql = "INSERT INTO REQUIERE VALUES ("+id+","+req.getId()+")";
					prepStmt = conn.prepareStatement(sql);
					System.out.println(sql);
					recursos.add(prepStmt);
					prepStmt.executeQuery();
				}
			}
			conn.commit();

		}catch(Exception e){
			conn.rollback();
			throw e;
		}finally{
			conn.setAutoCommit(true);
		}
	}


	/**
	 * Método que retorna los espectáculos existentes en la bd
	 * @return
	 * @throws SQLException
	 * @throws Exception
	 */
	public List<Espectaculo> getEspectaculos() throws SQLException, Exception{

		conn.setAutoCommit(false);
		conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
		List<Espectaculo> espec = new ArrayList<Espectaculo>();
		try{
			String sql = "SELECT * FROM ESPECTACULOS ORDER BY ID";
			PreparedStatement prepStmt = conn.prepareStatement(sql);
			recursos.add(prepStmt);
			ResultSet result = prepStmt.executeQuery();

			while(result.next()){
				espec.add(new Espectaculo(result.getLong("ID"),result.getString("NOMBRE"),result.getInt("DURACION"), result.getString("IDIOMA"),
						result.getDouble("COSTO"),result.getString("DESCRIPCION"),null,null,null));
			}
			conn.commit();

		}catch(Exception e){
			conn.rollback();
			throw e;
		}finally{
			conn.setAutoCommit(true);
		}

		return espec;
	}


	public ReporteEspectaculo getReporteEspectaculo(Long id){


		return null;
	}

	/**
	 * Método que extrae el conjunto de espectáculos más populares de la base de datos
	 * @param cantidad de espectaculos a mostrar
	 * @return Lista de espectaculos más populares
	 */
	public List<EspectaculoPopular> getEspectaculosMasPopulares(int cantidad,String fechaInicio, String fechaFin) throws SQLException, Exception{

		conn.setAutoCommit(false);
		conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
		List<EspectaculoPopular> espectaculos = new ArrayList<EspectaculoPopular>();
		try{
			java.util.Date d = new SimpleDateFormat("yyyy-MM-dd").parse(fechaInicio);
			Date inicio = new Date(d.getTime());
			java.util.Date d1 = new SimpleDateFormat("yyyy-MM-dd").parse(fechaFin);
			Date fin = new Date(d1.getTime());
			if(inicio.after(fin))throw new Exception("El intervalo de fechas es incorrecto");

			String sql = "SELECT * FROM (SELECT E.ID AS IDE, COUNT (CASE WHEN B.VENDIDO='Y' THEN 1 END) AS CANTIDAD"
					+" FROM (ESPECTACULOS E LEFT JOIN FUNCIONES F ON E.ID = F.IDESPECTACULO) LEFT JOIN BOLETAS B ON F.ID = B.IDFUNCION "
					+ "WHERE TO_TIMESTAMP("+inicio+",'RRRR-mm-dd')<=F.FECHA AND TO_TIMESTAMP("+fin+",'RRRR-mm-dd')>=F.FECHA "
					+ "GROUP BY E.ID ORDER BY COUNT (CASE WHEN B.VENDIDO='Y' THEN 1 END) DESC) REPORTE JOIN ESPECTACULOS ES ON REPORTE.IDE=ES.ID";

			PreparedStatement prepStmt = conn.prepareStatement(sql);
			recursos.add(prepStmt);
			ResultSet result = prepStmt.executeQuery();

			while(result.next()&&cantidad-->0){
				EspectaculoPopular esp = new EspectaculoPopular();
				esp.setNombre(result.getString("NOMBRE"));
				esp.setId(result.getLong("ID"));
				esp.setDuracion(result.getInt("DURACION"));
				esp.setIdioma(result.getString("IDIOMA"));
				esp.setCosto(result.getDouble("COSTO"));
				esp.setDescripcion(result.getString("DESCRIPCION"));
				espectaculos.add(esp);
			}

			conn.commit();

		}catch(Exception e){
			conn.rollback();
			throw e;
		}finally{
			conn.setAutoCommit(true);
		}
		
		return espectaculos;
	}

}
