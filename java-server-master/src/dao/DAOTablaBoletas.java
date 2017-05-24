package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import vos.BoletaDetail;
import vos.CertificadoDeDevolucion;
import vos.Funcion;
import vos.Localidad;

public class DAOTablaBoletas {

	/**
	 * Arraylits de recursos que se usan para la ejecuciÃ³n de sentencias SQL
	 */
	private ArrayList<Object> recursos;

	/**
	 * Atributo que genera la conexiÃ³n a la base de datos
	 */
	private Connection conn;

	/**
	 * MÃ©todo constructor que crea DAOTablaBoletas
	 * <b>post: </b> Crea la instancia del DAO e inicializa el Arraylist de recursos
	 */
	public DAOTablaBoletas() {
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
	 * Método que hace el registro para la compra de una boleta
	 * @param idBoleta 
	 * @param idUsuario
	 * @return la boleta detallada
	 * @throws SQLException Si algo falla en la bd
	 * @throws Exception si hay algún otro fallo
	 */
	public BoletaDetail registroCompraBoleta(Long idBoleta, Long idUsuario, String accesToken) throws SQLException,Exception{

		conn.setAutoCommit(false);
		conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

		try{
			String sql = "SELECT B.VENDIDO, F.REALIZADO FROM BOLETAS B INNER JOIN FUNCIONES F ON F.ID=B.IDFUNCION "
					+ "WHERE B.ID = "+idBoleta.toString();
			String sql1 = "UPDATE BOLETAS SET VENDIDO = 'Y'"
					+", IDUSUARIO = "+(idUsuario==null?"NULL":idUsuario.toString())
					+" WHERE ID = "+idBoleta.toString();

			System.out.println(sql);
			System.out.println(sql1);

			//Verificar la existencia de la boleta en el sistema
			PreparedStatement prepStmt = conn.prepareStatement(sql);
			recursos.add(prepStmt);
			ResultSet rs = prepStmt.executeQuery();

			if(!rs.next())throw new Exception("La boleta no existe");
			if(rs.getString("REALIZADO").equals("Y"))throw new Exception("No se puede comprar una boleta de una función ya realizada");
			boolean vendido = rs.getString("VENDIDO").equals("Y");

			//Verificar la existencia de un cliente especificado como query param
			if(idUsuario!=null){
				String sql2= "SELECT * FROM USUARIOS WHERE ID = "+idUsuario.toString();
				prepStmt = conn.prepareStatement(sql2);
				recursos.add(prepStmt);
				rs = prepStmt.executeQuery();
				if(!rs.next())throw new Exception("El usuario especificado no existe");
				if(!rs.getString("ACCES_TOKEN").equals(accesToken))throw new Exception("Acceso denegado");
			}

			if(vendido)throw new Exception("La boleta no se puede comprar porque ya ha sido vendida");
			else{
				prepStmt = conn.prepareStatement(sql1);
				recursos.add(prepStmt);
				rs = prepStmt.executeQuery();
			}

			conn.commit();

			BoletaDetail bol = null;

			//Retornar la boleta con la información de interés para el usuario
			sql = "SELECT BOLETAS.ID, BOLETAS.FILA, BOLETAS.SILLA, FUNCIONES.FECHA,"
					+ "LOCALIDADES.TIPO, LOCALIDADES.PRECIO, USUARIOS.NOMBRE AS NOMUSUARIO, SITIOS.NOMBRE AS NOMSITIO,"
					+"ESPECTACULOS.NOMBRE AS NOMESPECTACULO, ESPECTACULOS.DURACION, ESPECTACULOS.IDIOMA FROM "
					+"((((BOLETAS INNER JOIN FUNCIONES ON BOLETAS.IDFUNCION = FUNCIONES.ID) "
					+"INNER JOIN LOCALIDADES ON BOLETAS.IDLOCALIDAD = LOCALIDADES.ID) "
					+"LEFT JOIN USUARIOS ON BOLETAS.IDUSUARIO = USUARIOS.ID) "
					+"INNER JOIN SITIOS ON SITIOS.ID = FUNCIONES.IDSITIO) "
					+"INNER JOIN ESPECTACULOS ON ESPECTACULOS.ID = FUNCIONES.IDESPECTACULO "
					+"WHERE BOLETAS.ID = "+idBoleta.toString();

			System.out.println(sql);
			prepStmt = conn.prepareStatement(sql);
			recursos.add(prepStmt);
			rs = prepStmt.executeQuery();
			if(!rs.next())throw new Exception("La boleta no existe");

			bol = new BoletaDetail(rs.getLong("ID"),null,null,rs.getTimestamp("FECHA").toString(),
					rs.getString("TIPO"),rs.getDouble("PRECIO"),rs.getString("NOMUSUARIO"),rs.getString("NOMSITIO"),
					rs.getString("NOMESPECTACULO"),rs.getInt("DURACION"),rs.getString("IDIOMA"));

			Integer val = rs.getInt("FILA");
			bol.setFila(rs.wasNull()?null:val);
			val = rs.getInt("SILLA");
			bol.setSilla(rs.wasNull()?null:val);

			return bol;

		}catch(Exception e){
			conn.rollback();
			throw e;
		}finally{
			conn.setAutoCommit(true);
		}

	}

	/**
	 * Método que compra varias boletas en simultáneo
	 * @param idUsuario
	 * @param idFuncion
	 * @param cantidad
	 * @return
	 * @throws Exception
	 * @throws SQLException
	 */
	public List<BoletaDetail> registroCompraBoletas(Long idUsuario, String accessToken,Long idFuncion, Integer cantidad) throws Exception, SQLException{

		conn.setAutoCommit(false);
		conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

		try{
			List<BoletaDetail> boletas = new ArrayList<BoletaDetail>();
			String sql = "SELECT REALIZADO FROM FUNCIONES WHERE ID="+idFuncion.toString();
			PreparedStatement prepStmt = conn.prepareStatement(sql);
			recursos.add(prepStmt);
			ResultSet rs = prepStmt.executeQuery();
			if(!rs.next())throw new Exception("La función especificada no existe");
			if(rs.getString("REALIZADO").equals("Y"))throw new Exception("No se puede comprar boletas de una función ya realizada");
			
			sql = "SELECT * FROM (SELECT BOL.ID, BOL.VENDIDO, BOL.SILLA, BOL.FILA, SEQ.TIPO, " 
					+"ROW_NUMBER() OVER (PARTITION BY BOL.FILA, BOL.VENDIDO, SEQ.TIPO, SEQ.PART ORDER BY BOL.SILLA, BOL.FILA) AS SEGUIDAS "
					+"FROM (SELECT B.ID AS IDBOL, LOC.TIPO ,B.SILLA - ROW_NUMBER() OVER (PARTITION BY B.FILA, B.VENDIDO, LOC.TIPO ORDER BY B.SILLA, B.FILA) AS PART " 
					+"FROM (BOLETAS B INNER JOIN FUNCIONES FUN ON IDFUNCION = FUN.ID) "
					+"INNER JOIN LOCALIDADES LOC ON LOC.ID = B.IDLOCALIDAD "
					+"WHERE FUN.ID = "+idFuncion.toString()+") SEQ INNER JOIN BOLETAS BOL ON BOL.ID = SEQ.IDBOL WHERE VENDIDO = 'N') WHERE SEGUIDAS = "+cantidad.toString();

			System.out.println(sql);

			//Verificar la disponibilidad de la boletas continuas en el sistema
			prepStmt = conn.prepareStatement(sql);
			recursos.add(prepStmt);
			rs = prepStmt.executeQuery();
			if(!rs.next())throw new Exception("No hay "+cantidad.toString()+" cupos seguidos para esta función");
			int id = rs.getInt("ID");

			//Verificar la existencia de un cliente especificado como query param
			String sql2= "SELECT * FROM USUARIOS WHERE ID = "+idUsuario.toString();
			prepStmt = conn.prepareStatement(sql2);
			recursos.add(prepStmt);
			rs = prepStmt.executeQuery();
			if(!rs.next())throw new Exception("El usuario especificado no existe");
			if(rs.getLong("IDROL")!= 1) throw new Exception("El usuario no tiene los persmisos para comprar una boleta");
			if(!rs.getString("ACCES_TOKEN").equals(accessToken))throw new Exception("Acceso denegado");

			//El sistema hace la compra efectiva de las boletas consecutivas
			String ids = "(";
			for (int i = id; i > id-cantidad; i--) {
				sql ="UPDATE BOLETAS SET VENDIDO = 'Y'"
						+", IDUSUARIO = "+(idUsuario==null?"NULL":idUsuario.toString())
						+" WHERE ID = "+i;

				System.out.println(sql);
				prepStmt = conn.prepareStatement(sql);
				recursos.add(prepStmt);
				rs = prepStmt.executeQuery();

				ids += i+(i!=(id-cantidad+1)?", ":")");
			}

			conn.commit();

			//Retornar la boleta con la información de interés para el usuario
			sql = "SELECT BOLETAS.ID, BOLETAS.FILA, BOLETAS.SILLA, FUNCIONES.FECHA,"
					+ "LOCALIDADES.TIPO, LOCALIDADES.PRECIO, USUARIOS.NOMBRE AS NOMUSUARIO, SITIOS.NOMBRE AS NOMSITIO,"
					+"ESPECTACULOS.NOMBRE AS NOMESPECTACULO, ESPECTACULOS.DURACION, ESPECTACULOS.IDIOMA FROM "
					+"((((BOLETAS INNER JOIN FUNCIONES ON BOLETAS.IDFUNCION = FUNCIONES.ID) "
					+"INNER JOIN LOCALIDADES ON BOLETAS.IDLOCALIDAD = LOCALIDADES.ID) "
					+"LEFT JOIN USUARIOS ON BOLETAS.IDUSUARIO = USUARIOS.ID) "
					+"INNER JOIN SITIOS ON SITIOS.ID = FUNCIONES.IDSITIO) "
					+"INNER JOIN ESPECTACULOS ON ESPECTACULOS.ID = FUNCIONES.IDESPECTACULO "
					+"WHERE BOLETAS.ID IN "+ids+" ORDER BY BOLETAS.ID";

			System.out.println(sql);
			prepStmt = conn.prepareStatement(sql);
			recursos.add(prepStmt);
			rs = prepStmt.executeQuery();


			while(rs.next()){
				BoletaDetail bol = new BoletaDetail(rs.getLong("ID"),null,null,rs.getTimestamp("FECHA").toString(),
						rs.getString("TIPO"),rs.getDouble("PRECIO"),rs.getString("NOMUSUARIO"),rs.getString("NOMSITIO"),
						rs.getString("NOMESPECTACULO"),rs.getInt("DURACION"),rs.getString("IDIOMA"));

				Integer val = rs.getInt("FILA");
				bol.setFila(rs.wasNull()?null:val);
				val = rs.getInt("SILLA");
				bol.setSilla(rs.wasNull()?null:val);

				boletas.add(bol);
			}

			return boletas;

		}catch(Exception e){
			conn.rollback();
			throw e;
		}finally{
			conn.setAutoCommit(true);
		}
	}

	/**
	 * Método que devuelve la boleta de un usuario registrado o no registrado
	 * @param idBoleta
	 * @return
	 * @throws SQLException
	 * @throws Exception
	 */
	public CertificadoDeDevolucion registroDevolucionBoleta(Long idBoleta, Long idUsuario, String accessToken) throws SQLException,Exception{
		conn.setAutoCommit(false);
		conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
		CertificadoDeDevolucion certificado=null;
		try{
			String sql = "SELECT * FROM BOLETAS WHERE ID = "+idBoleta.toString();
			String sql1 = "UPDATE BOLETAS SET VENDIDO = 'N', IDUSUARIO = NULL WHERE ID = "+idBoleta;
					
			System.out.println(sql1);

			//Verificar la existencia de un cliente especificado como query param
			String sql2= "SELECT * FROM USUARIOS WHERE ID = "+idUsuario.toString();
			System.out.println(sql2);
			PreparedStatement prepStmt = conn.prepareStatement(sql2);
			recursos.add(prepStmt);
			ResultSet rs = prepStmt.executeQuery();
			if(!rs.next())throw new Exception("El usuario especificado no existe");
			if(!rs.getString("ACCES_TOKEN").equals(accessToken))throw new Exception("Acceso denegado");
			
			prepStmt = conn.prepareStatement(sql);
			recursos.add(prepStmt);
			rs = prepStmt.executeQuery();

			if(!rs.next())throw new Exception("La boleta no existe");
			boolean vendido = rs.getString("VENDIDO").equals("Y");
			if(!vendido)throw new Exception("La boleta no se puede DEVOLVER porque NO ha sido vendida");
			if(rs.getLong("IDUSUARIO") != idUsuario)throw new Exception("La boleta no le pertenece al usuario especificado");
			System.out.println(sql1);
			prepStmt = conn.prepareStatement(sql1);
			recursos.add(prepStmt);
			rs = prepStmt.executeQuery();
			
			sql1 = " INSERT INTO CERTIFICADOSDEDEVOLUCION (ID,IDUSUARIO,IDBOLETA,VALOR) VALUES (IDCERTIFICADOSDEDEVOLUCION.NEXTVAL, "
					+idUsuario+" , "+idBoleta+" , (SELECT L.PRECIO FROM BOLETAS B JOIN LOCALIDADES L ON (B.IDLOCALIDAD=L.ID) WHERE B.ID="+idBoleta+"))";
			System.out.println(sql1);
			prepStmt = conn.prepareStatement(sql1);
			recursos.add(prepStmt);
			rs = prepStmt.executeQuery();

			sql1="SELECT CER.ID, CER.VALOR, CLI.NOMBRE, CLI.CORREOELECTRONICO FROM CERTIFICADOSDEDEVOLUCION CER "
					+ "INNER JOIN USUARIOS CLI ON CER.IDUSUARIO = CLI.ID WHERE CLI.ID = "+idUsuario+" ORDER BY CER.ID DESC";
			System.out.println(sql1);
			prepStmt = conn.prepareStatement(sql1);
			recursos.add(prepStmt);
			rs = prepStmt.executeQuery();

			if (!rs.next()) throw new Exception ("Something failed");
			certificado= new CertificadoDeDevolucion(rs.getLong("ID"), rs.getDouble("VALOR"),rs.getString("NOMBRE"),rs.getString("CORREOELECTRONICO"));
			conn.commit();

		}catch(Exception e){
			conn.rollback();
			throw e;
		}finally{
			conn.setAutoCommit(true);
		}
		return certificado;
	}

	public double registroCompraAbono( Long idUsuario, List<Funcion> funciones, List<Localidad> localidades) throws SQLException,Exception{
		if (funciones.size()!= localidades.size()) throw new Exception("La lista de localidades debe contener un elemento por cada funcion.");
		if (idUsuario ==null) throw new Exception("El id del usuario no puede ser null, el usuario debe estar registrado para comprar un abono");
		conn.setAutoCommit(false);
		conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
		double precioTotal=0;
		long idBoleta=0;
		try{

			PreparedStatement prepStmt;
			ResultSet rs;
			Iterator iterator2= funciones.iterator();
			String sqlpre;
			String sql;
			sql = "INSERT INTO ABONAMIENTOS (ID, IDUSUARIO) VALUES (IDABONAMIENTOS.NEXTVAL,"+idUsuario+") ";
			prepStmt = conn.prepareStatement(sql);
			recursos.add(prepStmt);
			rs = prepStmt.executeQuery();
			sql ="SELECT * FROM ABONAMIENTOS WHERE IDUSUARIO="+idUsuario+" ORDER BY ID DESC ";
			prepStmt = conn.prepareStatement(sql);
			recursos.add(prepStmt);
			System.out.println(sql);
			rs = prepStmt.executeQuery();
			if (!rs.next()) throw new Exception ("No se creo el abonamineto");
			long idAbonamiento = rs.getLong("ID");


			for (Iterator iterator = localidades.iterator(); iterator.hasNext();) {
				//try catch to save point, check local variables
				conn.setSavepoint("forSavepoint");
				//
				Localidad localidad = (Localidad) iterator.next();
				Funcion funcion = (Funcion) iterator2.next();

				sqlpre="SELECT B.ID as id, L.PRECIO as precio FROM BOLETAS B JOIN LOCALIDADES L ON (B.IDLOCALIDAD=L.ID)"

						+ "WHERE B.IDFUNCION="+funcion.getId()+" AND B.IDLOCALIDAD="+localidad.getId()+" ";

				prepStmt = conn.prepareStatement(sqlpre);
				recursos.add(prepStmt);
				rs = prepStmt.executeQuery();

				if (rs.next()){
					idBoleta=rs.getLong("id");
					precioTotal+=rs.getDouble("precio");
				}else throw new Exception("No quedan boletas disponibles para la funcion "+funcion.getId()+ " en la localidad pedida");

				sql="UPDATE BOLETAS SET VENDIDO = 'Y', IDABONAMIENTO="+idAbonamiento+" "
						+", IDUSUARIO = "+idUsuario
						+" WHERE ID = "+idBoleta;
				prepStmt = conn.prepareStatement(sql);
				recursos.add(prepStmt);
				rs = prepStmt.executeQuery();


			}

			conn.commit();
			precioTotal=precioTotal*0.8;
			return precioTotal;
		}catch(Exception e){
			conn.rollback();
			throw e;
		}finally{
			conn.setAutoCommit(true);
		}

	}

	public CertificadoDeDevolucion devolverAbonamiento( Long idUsuario, Long idAbonamiento) throws SQLException,Exception{
		if (idAbonamiento==null) throw new Exception("El id abonamiento no puede ser null");
		if (idUsuario ==null) throw new Exception("El id del usuario no puede ser null, el usuario debe estar registrado para comprar un abono");
		conn.setAutoCommit(false);
		conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
		double precioTotal=0;
		long idBoleta=0;
		try{

			PreparedStatement prepStmt;
			ResultSet rs;

			String sql;

			sql= "SELECT B.ID AS ID, B.IDUSUARIO AS IDUSUARIO, L.PRECIO AS PRECIO FROM BOLETAS B JOIN LOCALIDADES L ON (B.IDLOCALIDAD=L.ID) WHERE IDABONAMIENTO="+idAbonamiento;
			System.out.println(sql);
			prepStmt = conn.prepareStatement(sql);
			recursos.add(prepStmt);
			rs = prepStmt.executeQuery();

			

			boolean check=false;
			
			while(rs.next()) {
				if (rs.getLong("IDUSUARIO")!=idUsuario) throw new Exception("El usuario no tiene el abonamiento con ese id.");
				check=true;
				//try catch to save point, check local variables
				conn.setSavepoint("forSavepoint");

				idBoleta=rs.getLong("ID");
				precioTotal+=rs.getDouble("PRECIO");

				sql="UPDATE BOLETAS SET VENDIDO = 'N', IDABONAMIENTO=NULL "
						+", IDUSUARIO = NULL"
						+" WHERE ID = "+idBoleta;
				
				prepStmt = conn.prepareStatement(sql);
				recursos.add(prepStmt);
				prepStmt.executeQuery();
				
				sql="INSERT INTO CERTIFICADOSDEDEVOLUCION (ID,IDUSUARIO,IDBOLETA,VALOR) VALUES (IDCERTIFICADOSDEDEVOLUCION.NEXTVAL,"+idUsuario+","+idBoleta +","+(rs.getDouble("PRECIO")*0.8)+" )";
				prepStmt = conn.prepareStatement(sql);
				recursos.add(prepStmt);
				prepStmt.executeQuery();

			}
			
			sql="DELETE FROM ABONAMIENTOS WHERE ID="+idAbonamiento;
			prepStmt = conn.prepareStatement(sql);
			recursos.add(prepStmt);
			prepStmt.executeQuery();
			/*
			sql="SELECT C.ID AS ID, C.VALOR AS VALOR, U.NOMBRE AS NOMBRE, U.CORREOELECTRONICO AS CORREOELECTRONICO FROM CERTIFICADOSDEVOLUCION C JOIN USUARIOS U ON C.IDUSUARIO=U.ID ORDER BY ID DESC LIMIT 1,1";
			prepStmt = conn.prepareStatement(sql);
			recursos.add(prepStmt);
			rs = prepStmt.executeQuery();
			*/

			if (!check) throw new Exception ("Something failed");
			
			CertificadoDeDevolucion certificado = new CertificadoDeDevolucion(null, precioTotal, null,null);

			conn.commit();

			return certificado;
		}catch(Exception e){
			conn.rollback();
			throw e;
		}finally{
			conn.setAutoCommit(true);
		}

	}

}
