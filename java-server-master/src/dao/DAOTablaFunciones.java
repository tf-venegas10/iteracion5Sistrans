package dao;


import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import vos.BoletasCompra;
import vos.Espectaculo;
import vos.EspectaculoPopular;
import vos.Funcion;
import vos.LocalidadReporte;
import vos.ReporteFuncion;
import vos.Sitio;
import vos.UsuarioSimple;

/**
 * Clase DAO que se conecta la base de datos usando JDBC para resolver los requerimientos de la aplicación
 * @author Tomas Venegas
 */
public class DAOTablaFunciones {

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
	public  DAOTablaFunciones() {
		
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
	 * M�todo que registra una funci�n en la base de datos
	 * @param funcion
	 * @throws Exception 
	 */
	public void registrarFuncion(Funcion funcion) throws Exception {

		conn.setAutoCommit(false);
		conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
		try{
			String sql="INSERT INTO FUNCIONES (ID,FECHA,REALIZADO,IDESPECTACULO,IDSITIO) VALUES(IDFUNCIONES.NEXTVAL,TO_TIMESTAMP('"
					+ funcion.getFecha().toString()+"','RRRR-MM-dd HH24:MI:SS.FF'),'N',"
					+ funcion.getEspectaculo().getId()+","+funcion.getSitio().getId()+")";

			System.out.println("SQL stmt:" + sql);

			PreparedStatement prepStmt = conn.prepareStatement(sql);
			recursos.add(prepStmt);
			prepStmt.executeQuery();

			String sql2="SELECT IDFUNCIONES.CURRVAL FROM DUAL";
			System.out.println("SQL stmt:" + sql2);
			prepStmt = conn.prepareStatement(sql2);
			recursos.add(prepStmt);
			ResultSet result = prepStmt.executeQuery();
			Long id=new Long(0);
			if(result.next()){
				id=Long.parseUnsignedLong(result.getString(1));
			}

			String sql3= "SELECT L.ID AS ID , L.NUMERODEPUESTOS, L.ESNUMERADA, L.FILAS, L.SILLASPORFILA "
					+ "FROM SITIOS S LEFT JOIN SECOMPONEDE CO ON S.ID=CO.IDSITIO "
					+ "LEFT JOIN LOCALIDADES L ON L.ID=CO.IDLOCALIDAD "
					+ "WHERE S.ID="+funcion.getSitio().getId();
			System.out.println("SQL stmt:" + sql3);
			prepStmt = conn.prepareStatement(sql3);
			recursos.add(prepStmt);
			result = prepStmt.executeQuery();

			int numeroPuestos=0;
			String sql4;
			int idLocalidad=0;
			int numeroFil=0;
			int numeroSillas=0;

			List<String> queries=new ArrayList<String>();

			while(result.next()){
				numeroPuestos=Integer.parseInt(result.getString("NUMERODEPUESTOS"));
				idLocalidad=Integer.parseInt(result.getString("ID"));
				if(!result.getString("ESNUMERADA").equals("Y")){
					for (int i = 0; i < numeroPuestos; i++) {
						sql4="INSERT INTO BOLETAS (ID, VENDIDO,FILA,SILLA,IDLOCALIDAD,IDFUNCION,IDUSUARIO) VALUES (IDBOLETAS.NEXTVAL,'N',NULL,NULL,"

								+idLocalidad+","+id+",NULL)";
						queries.add(sql4);

					}
				}
				else{
					numeroFil=Integer.parseInt(result.getString("FILAS"));
					numeroSillas=Integer.parseInt(result.getString("SILLASPORFILA"));
					for (int i = 1; i <= numeroFil; i++) {
						for (int j = 1; j <= numeroSillas; j++) {
							sql4="INSERT INTO BOLETAS (ID, VENDIDO,FILA,SILLA,IDLOCALIDAD,IDFUNCION,IDUSUARIO) VALUES (IDBOLETAS.NEXTVAL,'N',"+i+","+j+","
									+idLocalidad+","+id+",NULL)";
							prepStmt = conn.prepareStatement(sql4);
							recursos.add(prepStmt);
							prepStmt.executeQuery();
							//queries.add(sql4);

						}
					}
				}
			}

			/*for (Iterator iterator = queries.iterator(); iterator.hasNext();) {
				String string = (String) iterator.next();
				prepStmt = conn.prepareStatement(string);
				recursos.add(prepStmt);
				prepStmt.executeQuery();
				cerrarRecursos();
			}*/
			if (queries.isEmpty()) throw new Exception("Las boletas no se generaron");
			conn.commit();

		}catch(Exception e){
			conn.rollback();
			throw e;
		}finally{
			conn.setAutoCommit(true);
		}

	}

	/**
	 * Cambia el estado de la funci�n a realizado
	 * @param id
	 * @throws SQLException
	 */
	public void registrarRealizacionFuncion(Long id) throws SQLException {

		conn.setAutoCommit(false);
		conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
		try{
			String sql="UPDATE FUNCIONES SET REALIZADO='Y' WHERE ID="+id;
			PreparedStatement prepStmt = conn.prepareStatement(sql);

			recursos.add(prepStmt);
			System.out.println(sql);
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
	 * M�todo que retorna el reporte de una funci�n determinada
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public ReporteFuncion getReporteFuncion(Long id) throws Exception {

		conn.setAutoCommit(false);
		conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
		ReporteFuncion reporte= new ReporteFuncion();
		try{
			String sql="SELECT F.ID AS ID,L.TIPO as TIPO, L.PRECIO AS PRECIO, COUNT (CASE WHEN B.VENDIDO='Y' THEN 1 END) AS VENDIDAS,"
					+ " COUNT (CASE WHEN B.VENDIDO='Y' AND B.IDUSUARIO IS NOT NULL THEN 1 END) AS VENDIDASREGISTRADOS, "
					+ "COUNT (CASE WHEN B.VENDIDO='Y'  AND B.IDUSUARIO IS NULL THEN 1 END) AS VENDIDASNOREGISTRADOS "
					+ "FROM FUNCIONES F JOIN BOLETAS B ON (F.ID=B.IDFUNCION)"
					+ "JOIN LOCALIDADES L ON (L.ID=B.IDLOCALIDAD)"
					+ "WHERE F.ID="+id
					+"GROUP BY F.ID, L.TIPO, L.precio";
			PreparedStatement prepStmt = conn.prepareStatement(sql);
			recursos.add(prepStmt);
			ResultSet result = prepStmt.executeQuery();
			LocalidadReporte localidad;
			List<LocalidadReporte> localidades= new ArrayList<LocalidadReporte>();
			while(result.next()){
				localidad = new LocalidadReporte();
				localidad.setBoletas_vendidas_localidad(result.getInt("VENDIDAS"));
				localidad.setBoletas_vendidas_no_registrados(result.getInt("VENDIDASNOREGISTRADOS"));
				localidad.setBoletas_vendidas_registrados(result.getInt("VENDIDASREGISTRADOS"));
				localidad.setProducido_localidad(result.getInt("VENDIDAS")*Double.parseDouble(result.getString("PRECIO")));
				localidad.setProducido_no_registrados(result.getInt("VENDIDASNOREGISTRADOS")*Double.parseDouble(result.getString("PRECIO")));
				localidad.setProducido_registrados(result.getInt("VENDIDASREGISTRADOS")*Double.parseDouble(result.getString("PRECIO")));
				localidad.setTipo(result.getString("TIPO"));

				localidades.add(localidad);
			}
			int total=0;
			double venta=0.0;
			for (Iterator iterator = localidades.iterator(); iterator.hasNext();) {
				LocalidadReporte localidadReporte = (LocalidadReporte) iterator.next();
				total+=localidadReporte.getBoletas_vendidas_localidad();
				venta+=localidadReporte.getProducido_localidad();
			}

			reporte.setId(id);
			reporte.setBoletas_vendidas(total);
			reporte.setProducido(venta);
			reporte.setLocalidadReportes(localidades);

			conn.commit();

		}catch(Exception e){
			conn.rollback();
			throw e;
		}finally{
			conn.setAutoCommit(true);
		}

		return reporte;
	}

	/**
	 * M�todo que retorna las funciones de la base de datos
	 * @param fechaInicio
	 * @param fechaFin
	 * @param compania
	 * @param categoria
	 * @param idioma
	 * @param traduccion
	 * @param aptos
	 * @return
	 * @throws SQLException
	 */
	public List<Funcion> getFunciones(Date fechaInicio, Date fechaFin, String compania, String categoria, String idioma, Boolean traduccion, String aptos ) throws SQLException{

		conn.setAutoCommit(false);
		conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
		List<Funcion> list = new ArrayList<Funcion>();
		try{
			String sql="";
			String pFechaIncio, pFechaFin, pCompania, pCategoria, pIdioma, pRequerimientos, pAptos;
			if (fechaInicio==null){
				pFechaIncio="1800-01-01";
			}
			else{
				pFechaIncio=fechaInicio.toString();
			}

			if(fechaFin==null){
				pFechaFin="2200-01-01";
			}else{
				pFechaFin=fechaFin.toString();
			}

			if(compania==null){
				pCompania="%";
			}else{

				pCompania="%"+compania;
			}

			if(categoria==null){
				pCategoria="%";
			}else{
				pCategoria="%"+categoria;
			}

			if(idioma==null ){
				pIdioma="%";
			}else{
				pIdioma="%"+idioma;
			}
			if (traduccion==null || !traduccion){

				pRequerimientos="%";
			}
			else{
				pRequerimientos="%traduccion%";

			}
			if(aptos==null){
				pAptos="%";
			}else{
				pAptos="%"+aptos;
			}


			sql="SELECT F.ID AS FID, E.ID AS EID, E.NOMBRE AS ENOMBRE, E.DURACION AS EDURACION,E.IDIOMA AS EIDIOMA, E.COSTO AS ECOSTO, E.DESCRIPCION AS EDESCRIPCION, "
					+ "F.FECHA AS FFECHA, F.REALIZADO AS FREALIZADO, S.ID AS SID, S.NOMBRE AS SNOMBRE, S.ABIERTO AS SABIERTO "
					+ " FROM (FUNCIONES F LEFT JOIN ESPECTACULOS E ON (F.IDESPECTACULO=E.ID) "
					+ "LEFT JOIN PRESENTAN PRE ON PRE.IDESPECTACULO=E.ID "
					+ "LEFT JOIN COMPANIASTEATRO C ON PRE.IDCOMPANIA=C.ID "
					+ "LEFT JOIN REQUIERE REQ ON REQ.IDESPECTACULO=E.ID "
					+ "LEFT JOIN REQUERIMIENTOSTECNICOS R ON REQ.IDREQUERIMIENTO=R.ID "
					+ "LEFT JOIN HACEPARTE H ON H.IDESPECTACULO= E.ID "
					+ "LEFT JOIN CATEGORIAs CAT ON CAT.ID=H.IDCATEGORIA "
					+ "LEFT JOIN SITIOS S ON S.ID=F.IDSITIO "
					+ "LEFT JOIN ESPARA ON ESPARA.IDSITiO=S.ID "
					+ "LEFT JOIN APTOS ON APTOS.ID=ESPARA.IDAPTOS) "
					+" WHERE ("
					//	+ "WHERE (F.FECHA> TO_DATE('"+pFechaIncio+"') "
					//	+ " AND F.FECHA< TO_DATE('"+pFechaFin+"') "
					//	+ "AND"
					+ "(C.NOMBRE IS NULL OR C.NOMBRE LIKE '"+pCompania+ "'  )"
					+ "AND (CAT.NOMBRE IS NULL OR CAT.NOMBRE LIKE '"+ pCategoria+ "' )"
					+ "AND (E.IDIOMA IS NULL OR E.IDIOMA LIKE '"+ pIdioma+ "' )"
					+ "AND (R.NOMBRE IS NULL OR R.NOMBRE LIKE '"+ pRequerimientos+ "') "
					+ "AND (APTOS.NOMBRE IS NULL OR APTOS.NOMBRE LIKE '"+ pAptos+ "') "
					+ ")";


			PreparedStatement prepStmt = conn.prepareStatement(sql);
			recursos.add(prepStmt);
			System.out.println(sql);
			ResultSet result = prepStmt.executeQuery();

			while (result.next()){
				Funcion funcion = new Funcion();
				funcion.setId(Long.parseLong(result.getString("FID")));
				funcion.setEspectaculo(new Espectaculo(Long.parseLong(result.getString("EID")),result.getString("ENOMBRE"), Integer.parseInt(result.getString("EDURACION")),  result.getString("EIDIOMA"),  Double.parseDouble(result.getString("ECOSTO")),  result.getString("EDESCRIPCION"), null,  null,  null));
				funcion.setFecha((result.getString("FFECHA")));
				funcion.setRealizado(result.getString("FREALIZADO")=="V");
				funcion.setSitio(new Sitio( Long.parseLong(result.getString("SID")), result.getString("SNOMBRE"), result.getString("SABIERTO")=="V", null, null, null, null, null, null, null, null));

				list.add(funcion);
			}

			conn.commit();

		}catch(Exception e){
			conn.rollback();
			throw e;
		}finally{
			conn.setAutoCommit(true);
		}
		return list;
	}

	/**
	 * M�todo que elimina una funci�n dado su id
	 * @param id
	 * @throws Exception
	 */
	public void eliminarFuncion(Long id) throws Exception,SQLException {
		conn.setAutoCommit(false);
		conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
		try{

			String sql="SELECT B.ID AS ID, B.IDUSUARIO, B.IDABONAMIENTO, L.PRECIO FROM BOLETAS B JOIN LOCALIDADES L ON B.IDLOCALIDAD=L.ID WHERE idFuncion="+id;
			PreparedStatement prepStmt = conn.prepareStatement(sql);
			recursos.add(prepStmt);
			ResultSet rs = prepStmt.executeQuery();
			boolean exist=false;
			while(rs.next()){
				exist=true;
				if(rs.getString("IDUSUARIO")!=null &&  rs.getString("IDUSUARIO")!="NULL"){
					if(rs.getString("IDABONAMIENTO")!="NULL"){
						sql="INSERT INTO CERTIFICADOSDEDEVOLUCION (ID,IDUSUARIO,IDBOLETA,VALOR) VALUES (IDCERTIFICADOSDEDEVOLUCION.NEXTVAL,"+rs.getString("IDUSUARIO")+  ",NULL,"+(rs.getDouble("PRECIO")*0.8)+" )";

					}
					else{
						sql="INSERT INTO CERTIFICADOSDEDEVOLUCION (ID,IDUSUARIO,IDBOLETA,VALOR) VALUES (IDCERTIFICADOSDEDEVOLUCION.NEXTVAL,"+rs.getString("IDUSUARIO")+  ",NULL,"+(rs.getString("PRECIO"))+" )";

					}
					System.out.println(sql);
					prepStmt = conn.prepareStatement(sql);
					recursos.add(prepStmt);
					prepStmt.executeQuery();
				}

			}
			if (!exist) throw new Exception("La funci�n no existe o ya fue realizada.");
			sql="DELETE FROM BOLETAS WHERE idFuncion="+id;
			System.out.println(sql);
			prepStmt = conn.prepareStatement(sql);
			recursos.add(prepStmt);
			prepStmt.executeQuery();

			sql="DELETE FROM FUNCIONES WHERE id="+id;
			System.out.println(sql);
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
	 * M�todo que retorna un reporte sobre las funciones acorde a las boletas vendidas
	 * @param fechaInicio
	 * @param fechaFin
	 * @param elemento
	 * @param tipoLocalidad
	 * @param dia
	 * @return
	 */
	public List<BoletasCompra> comprasBoletas(String fechaInicio, String fechaFin, Integer elemento,
			Integer tipoLocalidad, String dia)throws SQLException,Exception {

		conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
		List<BoletasCompra> bols = new ArrayList<BoletasCompra>();

		String sql = "SELECT S.ID AS IDSITIO, E.NOMBRE AS NOMESP, F.ID AS IDFUNCION, F.FECHA, S.NOMBRE AS NOMSITIO, COUNT(*) AS VENDIDAS,"
				+ "SUM(CASE WHEN B.IDUSUARIO IS NOT NULL THEN 1 ELSE 0 END) AS REGISTRADOS, to_char(F.FECHA,'DAY') AS DIA FROM (((BOLETAS B "
				+ "INNER JOIN FUNCIONES F ON F.ID = B.IDFUNCION) "
				+ "INNER JOIN ESPECTACULOS E ON E.ID = F.IDESPECTACULO) "
				+ "INNER JOIN SITIOS S ON F.IDSITIO = S.ID) "
				+ "WHERE B.VENDIDO = 'Y' ";

		if(fechaInicio!=null && fechaFin!=null)
		{
			sql+=" AND F.FECHA BETWEEN TO_DATE('"+fechaInicio+"','RRRR-MM-dd') AND TO_DATE('"+fechaFin+"','RRRR-MM-dd')";
		}
		if(tipoLocalidad != null)
		{
			sql+=" AND IDLOCALIDAD = "+tipoLocalidad;
		}

		sql+=" GROUP BY S.ID, E.NOMBRE, F.ID, F.FECHA, S.NOMBRE";

		if(elemento !=null)
		{
			sql= "SELECT VEN.IDFUNCION, VEN.NOMESP, VEN.FECHA, VEN.NOMSITIO, VEN.VENDIDAS, VEN.REGISTRADOS, VEN.DIA FROM"
					+ "("+sql+")VEN INNER JOIN PRESTA P ON P.IDSITIO=VEN.IDSITIO WHERE P.IDREQUERIMIENTO = "+elemento.toString();
		}
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		System.out.println(sql);
		recursos.add(prepStmt);
		ResultSet res = prepStmt.executeQuery();

		while(res.next())
		{
			BoletasCompra bol = new BoletasCompra(res.getLong("IDFUNCION"), res.getString("NOMESP"), res.getString("FECHA"), res.getString("NOMSITIO"),
					res.getInt("VENDIDAS"), res.getInt("REGISTRADOS"));
			bols.add(bol);
		}

		return bols;
	}

}
