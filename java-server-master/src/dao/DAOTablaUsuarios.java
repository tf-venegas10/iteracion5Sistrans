
package dao;


import java.math.BigInteger;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import vos.AsistenciaFuncion;
import vos.AsistenciaUsuario;
import vos.Usuario;
import vos.UsuarioBuenCliente;

/**
 * Clase DAO que se conecta la base de datos usando JDBC para resolver los requerimientos de la aplicaci贸n
 * @author Tomas Venegas
 */
public class DAOTablaUsuarios {


	/**
	 * Arraylits de recursos que se usan para la ejecuci贸n de sentencias SQL
	 */
	private ArrayList<Object> recursos;

	/**
	 * Atributo que genera la conexi贸n a la base de datos
	 */
	private Connection conn;

	/**
	 * M茅todo constructor que crea DAOVideo
	 * <b>post: </b> Crea la instancia del DAO e inicializa el Arraylist de recursos
	 */
	public  DAOTablaUsuarios() {
		// TODO Auto-generated constructor stub
		recursos = new ArrayList<Object>();
	}

	/**
	 * M茅todo que cierra todos los recursos que estan enel arreglo de recursos
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
	 * M茅todo que inicializa la connection del DAO a la base de datos con la conexi贸n que entra como par谩metro.
	 * @param con  - connection a la base de datos
	 */
	public void setConn(Connection con){
		this.conn = con;
	}

	/**
	 * M閠odo que a馻de un usuario a la base de datos
	 * @param usuario
	 * @param rol
	 * @throws SQLException
	 * @throws Exception
	 */
	public void addUsuario(Usuario usuario, String rol) throws SQLException, Exception {


		conn.setAutoCommit(false);
		conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
		try{
			String sql ="INSERT INTO USUARIOS (ID,IDROL,NOMBRE,CORREOELECTRONICO,FECHANACIMIENTO,CONTRASENA,ACCES_TOKEN) VALUES (IDUSUARIOS.NEXTVAL";

			String sql1="SELECT id FROM ROLES WHERE NOMBRE LIKE ";
			switch (rol) {
			case "administrador":
				sql1+="'ADMINISTRADOR'";
				break;
			case "operario":
				sql1+="'OPERARIO'";
				break;

			default:
				sql1+="'CLIENTE'";
				break;
			}
			PreparedStatement prepStmt = conn.prepareStatement(sql1);
			recursos.add(prepStmt);
			ResultSet result = prepStmt.executeQuery();
			long idRol = 0;
			if(result.next()){
				idRol = Integer.parseInt(result.getString("ID"));
			}
			else throw new Exception("Problema al encontrar el rol en la base de datos");
			SecureRandom random = new SecureRandom();
			String r = new BigInteger(256, random).toString(32);

			sql += ","+idRol+",'" +usuario.getNombre()+"','" +usuario.getCorreoElectronico()+"',TO_DATE('"+ usuario.getFechaNacimiento()
			+"','yyyy-mm-dd'),'"+ usuario.getContrasena()+"','"+r+"')";

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
	 * M閠odo que retorna un usuario dado su access token
	 * @param acces_token
	 * @return
	 * @throws SQLException
	 */
	public Usuario getUserId(String acces_token) throws SQLException{

		conn.setAutoCommit(false);
		conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
		Usuario usr = null;

		try{
			String sql="SELECT U.ID,R.NOMBRE FROM USUARIOS U JOIN ROLES R ON (U.IDROL=R.ID) WHERE U.ACCES_TOKEN='"+acces_token+"'";
			System.out.println("SQL stmt:" + sql);

			PreparedStatement prepStmt = conn.prepareStatement(sql);
			recursos.add(prepStmt);
			ResultSet result = prepStmt.executeQuery();

			if(result.next()){
				usr= new Usuario();
				usr.setId(Long.parseLong(result.getString("ID")));
				usr.setRol(result.getString("NOMBRE"));
			}

			conn.commit();

		}catch(Exception e){
			conn.rollback();
			throw e;
		}finally{
			conn.setAutoCommit(true);
		}

		return usr;
	}

	public void addSitio(Long idUsuario, Long idSitio) throws SQLException {

		conn.setAutoCommit(false);
		conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
		try{
			String sql= "INSERT INTO PREFERENCIASITIO (IDUSUARIO,IDSITIO) VALUES("+idUsuario+","+idSitio+") ";
			System.out.println("SQL stmt:" + sql);
			PreparedStatement prepStmt = conn.prepareStatement(sql);
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
	 * M閠odo que a馻de una categor韆 preferida a un usuario
	 * @param idUsuario
	 * @param idCategoria
	 * @throws SQLException
	 */
	public void addCategoria(Long idUsuario, Long idCategoria) throws SQLException {

		conn.setAutoCommit(false);
		conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
		try{
			String sql="INSERT INTO PREFERENCIACATEGORIA (IDCATEGORIA,IDUSUARIO) VALUES("+idCategoria+","+idUsuario+")";
			System.out.println("SQL stmt:" + sql);
			PreparedStatement prepStmt = conn.prepareStatement(sql);
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
	 * M閠odo que retorna la asistencia de un determinado usuario
	 * @param idUsuario
	 * @return
	 * @throws SQLException
	 * @throws Exception
	 */
	public AsistenciaUsuario getAsistenciaFestival(Long idUsuario)throws SQLException, Exception {

		conn.setAutoCommit(false);
		conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
		AsistenciaUsuario asis = new AsistenciaUsuario();
		try{
			String sql = "SELECT * FROM USUARIOS WHERE ID="+idUsuario.toString();
			PreparedStatement prepStmt = conn.prepareStatement(sql);
			System.out.println(sql);
			recursos.add(prepStmt);
			ResultSet res = prepStmt.executeQuery();

			if(!res.next())throw new Exception("Something went wrong");
			asis.setNombre(res.getString("NOMBRE"));
			asis.setId(res.getLong("ID"));
			asis.setCorreoElectronico(res.getString("CORREOELECTRONICO"));

			sql = "SELECT F.ID , E.NOMBRE AS NOMESP,E.DURACION, F.FECHA, COUNT(F.ID) AS NUMBOLETAS FROM "
					+"(((USUARIOS U INNER JOIN BOLETAS B ON U.ID=B.IDUSUARIO)"
					+"INNER JOIN FUNCIONES F ON F.ID=B.IDFUNCION)"
					+"INNER JOIN ESPECTACULOS E ON E.ID=F.IDESPECTACULO) INNER JOIN (SELECT FUN.ID, "
					+"TO_TIMESTAMP(to_char(FUN.FECHA + (.000694444444444 * ESP.DURACION), 'RRRR-MM-dd HH24:MI:SS'), 'RRRR-MM-dd HH24:MI:SS') AS FECHAFIN " 
					+"FROM FUNCIONES FUN INNER JOIN ESPECTACULOS ESP ON ESP.ID=FUN.IDESPECTACULO) OFFS ON OFFS.ID = F.ID "
					+"WHERE FECHAFIN<TO_TIMESTAMP(to_char(CURRENT_TIMESTAMP, 'RRRR-MM-dd HH24:MI:SS'),'RRRR-MM-dd HH24:MI:SS') AND U.ID = "
					+idUsuario.toString()+" GROUP BY (F.ID,E.NOMBRE,E.DURACION,F.FECHA)";
			prepStmt = conn.prepareStatement(sql);
			System.out.println(sql);
			recursos.add(prepStmt);
			res = prepStmt.executeQuery();

			while(res.next()){
				AsistenciaFuncion fun = new AsistenciaFuncion();
				fun.setId(res.getLong("ID"));
				fun.setEspectaculo(res.getString("NOMESP"));
				fun.setDuracion(res.getInt("DURACION"));
				fun.setFecha(res.getString("FECHA"));
				fun.setCantidadBoletas(res.getInt("NUMBOLETAS"));
				asis.addRealizadas(fun);
			}

			sql = "SELECT F.ID, E.NOMBRE AS NOMESP, E.DURACION,F.FECHA, COUNT(F.ID) AS NUMBOLETAS FROM "
					+"(((USUARIOS U INNER JOIN BOLETAS B ON U.ID=B.IDUSUARIO)"
					+"INNER JOIN FUNCIONES F ON F.ID=B.IDFUNCION)"
					+"INNER JOIN ESPECTACULOS E ON E.ID=F.IDESPECTACULO)"
					+"INNER JOIN (SELECT FUN.ID, "
					+"TO_TIMESTAMP(to_char(FUN.FECHA + (.000694444444444 * ESP.DURACION), 'RRRR-MM-dd HH24:MI:SS'), 'RRRR-MM-dd HH24:MI:SS') AS FECHAFIN " 
					+"FROM FUNCIONES FUN INNER JOIN ESPECTACULOS ESP ON ESP.ID=FUN.IDESPECTACULO) OFFS ON OFFS.ID = F.ID "
					+"WHERE (TO_TIMESTAMP(to_char(CURRENT_TIMESTAMP, 'RRRR-MM-dd HH24:MI:SS'),'RRRR-MM-dd HH24:MI:SS') "
					+"BETWEEN F.FECHA AND FECHAFIN) AND U.ID = "+idUsuario.toString()+" GROUP BY (F.ID,E.NOMBRE,E.DURACION,F.FECHA)";
			prepStmt = conn.prepareStatement(sql);
			System.out.println(sql);
			recursos.add(prepStmt);
			res = prepStmt.executeQuery();
			while(res.next()){
				AsistenciaFuncion fun = new AsistenciaFuncion();
				fun.setId(res.getLong("ID"));
				fun.setEspectaculo(res.getString("NOMESP"));
				fun.setDuracion(res.getInt("DURACION"));
				fun.setFecha(res.getString("FECHA"));
				fun.setCantidadBoletas(res.getInt("NUMBOLETAS"));
				asis.addEnCurso(fun);
			}

			sql = "SELECT F.ID ,E.NOMBRE AS NOMESP, E.DURACION,F.FECHA, COUNT(F.ID) AS NUMBOLETAS FROM "
					+"((USUARIOS U INNER JOIN BOLETAS B ON U.ID=B.IDUSUARIO)"
					+"INNER JOIN FUNCIONES F ON F.ID=B.IDFUNCION)"
					+"INNER JOIN ESPECTACULOS E ON E.ID=F.IDESPECTACULO "
					+"WHERE F.FECHA>TO_TIMESTAMP(to_char(CURRENT_TIMESTAMP, 'RRRR-MM-dd HH24:MI:SS'),'RRRR-MM-dd HH24:MI:SS') AND U.ID = "+idUsuario.toString()
					+" GROUP BY (F.ID,E.NOMBRE,E.DURACION,F.FECHA)";
			prepStmt = conn.prepareStatement(sql);
			System.out.println(sql);
			recursos.add(prepStmt);
			res = prepStmt.executeQuery();
			while(res.next()){
				AsistenciaFuncion fun = new AsistenciaFuncion();
				fun.setId(res.getLong("ID"));
				fun.setEspectaculo(res.getString("NOMESP"));
				fun.setDuracion(res.getInt("DURACION"));
				fun.setFecha(res.getString("FECHA"));
				fun.setCantidadBoletas(res.getInt("NUMBOLETAS"));
				asis.addPrevistas(fun);
			}

			sql = "SELECT F.ID, E.NOMBRE, E.DURACION,F.FECHA, COUNT(F.ID) AS NUMDEVUELTAS FROM (((USUARIOS U "
					+"INNER JOIN CERTIFICADOSDEDEVOLUCION C ON U.ID=C.IDUSUARIO)"
					+"INNER JOIN BOLETAS B ON B.ID=C.IDBOLETA)"
					+"INNER JOIN FUNCIONES F ON F.ID=B.IDFUNCION)"
					+"INNER JOIN ESPECTACULOS E ON E.ID=F.IDESPECTACULO "
					+"WHERE U.ID = "+idUsuario.toString()+" GROUP BY (F.ID,E.NOMBRE, E.DURACION,F.FECHA)";
			prepStmt = conn.prepareStatement(sql);
			System.out.println(sql);
			recursos.add(prepStmt);
			res = prepStmt.executeQuery();
			while(res.next()){
				AsistenciaFuncion fun = new AsistenciaFuncion();
				fun.setId(res.getLong("ID"));
				fun.setEspectaculo(res.getString("NOMBRE"));
				fun.setDuracion(res.getInt("DURACION"));
				fun.setFecha(res.getString("FECHA"));
				fun.setCantidadBoletas(res.getInt("NUMDEVUELTAS"));
				asis.addDevueltas(fun);
			}
			conn.commit();

		}catch(Exception e){
			conn.rollback();
			throw e;
		}finally{
			conn.setAutoCommit(true);
		}
		return asis;
	}

	/**
	 * M閠odo que retorna la consulta de los buenos clientes seg鷑 critero del administrador
	 * @param cantidad
	 * @return
	 */
	public List<UsuarioBuenCliente> getBuenosClientes(Integer cantidad)throws SQLException, Exception {
		
		conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
		List<UsuarioBuenCliente> buenos = new ArrayList<UsuarioBuenCliente>();
		
		String sql = "SELECT * FROM (SELECT BU.ID, BU.NOMBRE, BU.CORREOELECTRONICO, BU.FECHANACIMIENTO, BU.ROL, "
				+ "COUNT(BU.NOMBRE) AS CANTIDADBOLETAS FROM "
				+ "((SELECT U.ID, U.NOMBRE, U.CORREOELECTRONICO, U.FECHANACIMIENTO, R.NOMBRE AS ROL "
				+ "FROM (USUARIOS U INNER JOIN BOLETAS B ON B.IDUSUARIO=U.ID) "
				+ "INNER JOIN ROLES R ON R.ID=U.IDROL WHERE B.IDLOCALIDAD=1 "
				+ "GROUP BY U.ID, U.NOMBRE, U.CORREOELECTRONICO, U.FECHANACIMIENTO, R.NOMBRE) "
				+ "MINUS (SELECT U.ID, U.NOMBRE, U.CORREOELECTRONICO, U.FECHANACIMIENTO, R.NOMBRE AS ROL "
				+ "FROM (USUARIOS U INNER JOIN BOLETAS B ON B.IDUSUARIO=U.ID) "
				+ "INNER JOIN ROLES R ON R.ID=U.IDROL WHERE B.IDLOCALIDAD!=1 "
				+ "GROUP BY U.ID, U.NOMBRE, U.CORREOELECTRONICO, U.FECHANACIMIENTO, R.NOMBRE)) BU "
				+ "INNER JOIN BOLETAS B ON B.IDUSUARIO=BU.ID "
				+ "GROUP BY BU.ID, BU.NOMBRE, BU.CORREOELECTRONICO, BU.FECHANACIMIENTO, BU.ROL) "
				+ "WHERE CANTIDADBOLETAS>="+cantidad;
		
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		System.out.println(sql);
		recursos.add(prepStmt);
		ResultSet res = prepStmt.executeQuery();
		
		while(res.next())
		{
			buenos.add(new UsuarioBuenCliente(res.getLong("ID"), res.getString("NOMBRE"), res.getString("CORREOELECTRONICO"), 
					res.getString("FECHANACIMIENTO"), res.getString("ROL"), res.getInt("CANTIDADBOLETAS")));
		}
		
		return buenos;
	}


}
