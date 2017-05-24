package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import vos.CompaniaEspectaculo;
import vos.CompaniaSitio;
import vos.CompaniaTeatro;
import vos.ConsultaCompania;
import vos.UsuarioSimple;

public class DAOTablaCompania {

	/**
	 * Arraylits de recursos que se usan para la ejecución de sentencias SQL
	 */
	private ArrayList<Object> recursos;

	/**
	 * Atributo que genera la conexión a la base de datos
	 */
	private Connection conn;

	/**
	 * Método constructor que crea DAOVideo <b>post: </b> Crea la instancia del
	 * DAO e inicializa el Arraylist de recursos
	 */
	public DAOTablaCompania() {
		recursos = new ArrayList<Object>();
	}

	/**
	 * Método que cierra todos los recursos que estan enel arreglo de recursos
	 * <b>post: </b> Todos los recurso del arreglo de recursos han sido cerrados
	 */
	public void cerrarRecursos() {
		for (Object ob : recursos) {
			if (ob instanceof PreparedStatement)
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
	 * M�todo que consulta en la base de datos si existe una compa��a de teatro
	 * 
	 * @param id
	 *            de la compa��a a buscar
	 * @return si existe o no
	 */
	public boolean existsCompany(Long id) throws SQLException, Exception {

		conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
		String sql = "SELECT ID FROM COMPANIASTEATRO WHERE ID=" + id.toString();
		System.out.println(sql);
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet res = prepStmt.executeQuery();
		return res.next();

	}

	/**
	 * M�todo que a�ade una compa��a de teatro
	 * 
	 * @param compania
	 * @throws SQLException
	 * @throws Exception
	 */
	public void addCompaniaTeatro(CompaniaTeatro compania, String token) throws SQLException, Exception {

		try {
			conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);

			String sql = "SELECT R.NOMBRE FROM USUARIOS U INNER JOIN ROLES R ON R.ID = U.IDROL WHERE U.ACCES_TOKEN='"
					+ token + "'";
			System.out.println(sql);
			PreparedStatement prepStmt = conn.prepareStatement(sql);
			recursos.add(prepStmt);
			ResultSet res = prepStmt.executeQuery();
			if (res.next()) {
				if (!res.getString("NOMBRE").equals("OPERARIO"))
					throw new Exception("El usuario especificado no esta registrado como operario");
			} else
				throw new Exception("El usuario especificado no existe");

			conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			conn.setAutoCommit(false);

			sql = "INSERT INTO COMPANIASTEATRO VALUES (IDCOMPANIASTEATRO.NEXTVAL,'";
			sql += compania.getNombre() + "','" + compania.getPaisOrigen() + "','" + compania.getUrlPagina()
					+ "',TO_DATE('" + compania.getFechaLlegada() + "','RRRR-mm-dd'),TO_DATE('"
					+ compania.getFechaSalida() + "','RRRR-mm-dd'),'" + compania.getRepresentante() + "')";

			prepStmt = conn.prepareStatement(sql);
			recursos.add(prepStmt);
			prepStmt.executeQuery();

			conn.commit();

		} catch (Exception e) {
			conn.rollback();
			throw e;
		} finally {
			conn.setAutoCommit(true);
		}

	}

	/**
	 * M�todo que hace una consulta para una compa��a de teatro
	 * 
	 * @param id
	 * @return
	 */
	public ConsultaCompania consultaCompaniaTeatro(Long id, Long idRepresentante, boolean admin)
			throws SQLException, Exception {
		conn.setAutoCommit(false);
		conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
		ConsultaCompania consul = new ConsultaCompania();
		try {
			String sql = "";
			if (!admin) {
				sql = "SELECT IDREPRESENTANTE FROM COMPANIASTEATRO WHERE ID=" + id.toString();
				PreparedStatement prepStmt = conn.prepareStatement(sql);
				System.out.println(sql);
				recursos.add(prepStmt);
				ResultSet res = prepStmt.executeQuery();
				if (!res.next())
					throw new Exception("La compa��a especificada no existe");
				if (res.getLong("IDREPRESENTATNE") != idRepresentante)
					throw new Exception("El operario especificado no tiene los permisos sobre esta compa��a");
			}

			sql = "SELECT ID, NOMBRE FROM COMPANIASTEATRO WHERE ID=" + id;
			PreparedStatement prepStmt = conn.prepareStatement(sql);
			System.out.println(sql);
			recursos.add(prepStmt);
			ResultSet res = prepStmt.executeQuery();
			if (!res.next())
				throw new Exception("La compa��a especificada no existe");
			consul.setId(res.getLong("ID"));
			consul.setNombre(res.getString("NOMBRE"));

			sql = "SELECT E.ID, E.NOMBRE, COUNT(VENDIDO) AS ASISTOT "
					+ "FROM (((COMPANIASTEATRO C INNER JOIN PRESENTAN P ON C.ID=P.IDCOMPANIA)"
					+ "INNER JOIN ESPECTACULOS E ON P.IDESPECTACULO=E.ID) "
					+ "INNER JOIN FUNCIONES F ON E.ID=F.IDESPECTACULO) "
					+ "INNER JOIN BOLETAS B ON B.IDFUNCION = F.ID WHERE VENDIDO='Y' AND C.ID= " + id.toString()
					+ " GROUP BY (E.ID,E.NOMBRE) ORDER BY E.ID";
			prepStmt = conn.prepareStatement(sql);
			System.out.println(sql);
			recursos.add(prepStmt);
			ResultSet asisTotal = prepStmt.executeQuery();

			sql = "SELECT E.ID, E.NOMBRE, COUNT(VENDIDO) AS REGISTRADOS "
					+ "FROM (((COMPANIASTEATRO C INNER JOIN PRESENTAN P ON C.ID=P.IDCOMPANIA)"
					+ "INNER JOIN ESPECTACULOS E ON P.IDESPECTACULO=E.ID) "
					+ "INNER JOIN FUNCIONES F ON E.ID=F.IDESPECTACULO) "
					+ "INNER JOIN BOLETAS B ON B.IDFUNCION = F.ID WHERE VENDIDO='Y' AND B.IDUSUARIO IS NOT NULL AND C.ID = "
					+ id.toString() + " GROUP BY (E.ID,E.NOMBRE)ORDER BY E.ID";
			prepStmt = conn.prepareStatement(sql);
			System.out.println(sql);
			recursos.add(prepStmt);
			ResultSet asisRegistrados = prepStmt.executeQuery();

			sql = "SELECT E.ID, E.NOMBRE, SUM(PRECIO) AS GENERADO "
					+ "FROM ((((COMPANIASTEATRO C INNER JOIN PRESENTAN P ON C.ID=P.IDCOMPANIA)"
					+ "INNER JOIN ESPECTACULOS E ON P.IDESPECTACULO=E.ID)"
					+ "INNER JOIN FUNCIONES F ON E.ID=F.IDESPECTACULO)" + "INNER JOIN BOLETAS B ON B.IDFUNCION = F.ID)"
					+ "INNER JOIN LOCALIDADES L ON L.ID=B.IDLOCALIDAD WHERE VENDIDO='Y' AND C.ID = " + id.toString()
					+ " GROUP BY (E.ID, E.NOMBRE)ORDER BY E.ID";
			prepStmt = conn.prepareStatement(sql);
			System.out.println(sql);
			recursos.add(prepStmt);
			ResultSet generado = prepStmt.executeQuery();

			sql = "CREATE VIEW TOTAL AS (SELECT E.ID, E.NOMBRE AS NOMESP, S.ID AS IDSITIO, S.NOMBRE, B.VENDIDO "
					+ "FROM (((((COMPANIASTEATRO C INNER JOIN PRESENTAN P ON C.ID=P.IDCOMPANIA)"
					+ "INNER JOIN ESPECTACULOS E ON P.IDESPECTACULO=E.ID))"
					+ "INNER JOIN FUNCIONES F ON E.ID=F.IDESPECTACULO)" + "INNER JOIN BOLETAS B ON B.IDFUNCION = F.ID) "
					+ "INNER JOIN SITIOS S ON S.ID=F.IDSITIO WHERE C.ID = " + id.toString() + ")";
			prepStmt = conn.prepareStatement(sql);
			System.out.println(sql);
			recursos.add(prepStmt);
			prepStmt.executeQuery();

			sql = "SELECT TOTAL.ID, TOTAL.IDSITIO,TOTAL.NOMESP, TOTAL.NOMBRE, "
					+ "(COUNT(VENDIDO)/(CASE WHEN (SELECT COUNT(*) FROM TOTAL)=0 THEN 1 "
					+ "ELSE (SELECT COUNT(*) FROM TOTAL) END)) AS PORCENTAJE FROM TOTAL WHERE VENDIDO='Y' "
					+ "GROUP BY (TOTAL.ID, TOTAL.IDSITIO,TOTAL.NOMESP, TOTAL.NOMBRE)ORDER BY TOTAL.ID ";
			prepStmt = conn.prepareStatement(sql);
			System.out.println(sql);
			recursos.add(prepStmt);
			ResultSet porcentaje = prepStmt.executeQuery();

			sql = "DROP VIEW TOTAL";
			prepStmt = conn.prepareStatement(sql);
			System.out.println(sql);
			recursos.add(prepStmt);
			prepStmt.executeQuery();

			while (asisTotal.next()) {
				CompaniaEspectaculo espectaculo = new CompaniaEspectaculo();
				espectaculo.setId(asisTotal.getLong("ID"));
				espectaculo.setNombre(asisTotal.getString("NOMBRE"));
				espectaculo.setAsistTotal(asisTotal.getInt("ASISTOT"));

				if (!asisRegistrados.next())
					throw new Exception("Something went wrong");
				espectaculo.setAsistRegistrados(asisRegistrados.getInt("REGISTRADOS"));

				if (!generado.next())
					throw new Exception("Something went wrong");
				espectaculo.setDineroRecaudado(generado.getDouble("GENERADO"));

				consul.addEspectaculo(espectaculo);
			}

			List<CompaniaEspectaculo> esp = consul.getEspectaculos();
			int i = 0;
			while (porcentaje.next()) {
				if (esp.get(i).getNombre().equals(porcentaje.getString("NOMESP")))
					esp.get(i).addSitio(new CompaniaSitio(porcentaje.getLong("IDSITIO"), porcentaje.getString("NOMBRE"),
							porcentaje.getDouble("PORCENTAJE")));
				else if (esp.get(++i).getNombre().equals(porcentaje.getString("NOMESP")))
					esp.get(i).addSitio(new CompaniaSitio(porcentaje.getLong("IDSITIO"), porcentaje.getString("NOMBRE"),
							porcentaje.getDouble("PORCENTAJE")));
				else
					throw new Exception("Something went wrong");
			}

			conn.commit();

		} catch (Exception e) {
			conn.rollback();
			throw e;
		} finally {
			conn.setAutoCommit(true);
		}
		return consul;
	}

	/**
	 * M�todo que consulta la asistencia al festival de los usuarios seg�n
	 * criterios establecidos por el administrador
	 * 
	 * @param id
	 * @param fechaInicio
	 * @param fechaFin
	 * @param edad
	 * @param alfa
	 * @param desc
	 * @param group
	 * @return
	 */
	public List<UsuarioSimple> getAsistenciaFestivAndes(Long id, String fechaInicio, String fechaFin, Integer edad,
			Integer alfa, Integer desc, Integer group) throws SQLException, Exception {

		conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
		List<UsuarioSimple> usuarios = new ArrayList<>();

		String sql = "SELECT U.ID, U.NOMBRE, U.CORREOELECTRONICO, U.FECHANACIMIENTO, R.NOMBRE AS ROL FROM (((((USUARIOS U "
				+ "INNER JOIN BOLETAS B ON U.ID=B.IDUSUARIO) " + "INNER JOIN FUNCIONES F ON F.ID = B.IDFUNCION) "
				+ "INNER JOIN ESPECTACULOS E ON E.ID=F.IDESPECTACULO) "
				+ "INNER JOIN PRESENTAN P ON P.IDESPECTACULO=E.ID)"
				+ "INNER JOIN COMPANIASTEATRO C ON C.ID=P.IDCOMPANIA)"
				+ "INNER JOIN ROLES R ON R.ID = U.IDROL WHERE C.ID = " + id
				+ " AND ( TO_DATE(to_char(F.FECHA, 'RRRR-MM-dd'),'RRRR-MM-dd') BETWEEN " + "TO_DATE('" + fechaInicio
				+ "','RRRR-MM-dd') AND TO_DATE('" + fechaFin + "','RRRR-MM-dd'))";

		if (group != null && group == 1) {
			sql += " GROUP BY U.ID, U.NOMBRE, U.CORREOELECTRONICO, U.FECHANACIMIENTO, R.NOMBRE";
		}

		String order = " ORDER BY ";
		if (edad != null && edad == 1) {
			order += "U.FECHANACIMIENTO";
			if (alfa != null && alfa == 1) {
				order += ",U.NOMBRE";
			}
			if (desc != null && desc == 1) {
				order += " DESC ";
			}
			sql += order;
		} else if (alfa != null && alfa == 1) {
			order += "U.NOMBRE";
			if (desc != null && desc == 1) {
				order += " DESC ";
			}
			sql += order;
		}

		System.out.println(sql);
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet res = prepStmt.executeQuery();
		while (res.next()) {
			usuarios.add(new UsuarioSimple(res.getLong("ID"), res.getString("NOMBRE"),
					res.getString("CORREOELECTRONICO"), res.getString("FECHANACIMIENTO"), res.getString("ROL")));
		}

		return usuarios;
	}

	/**
	 * M�todo que consulta la inasistencia de los usuarios a una funci�n de una
	 * compa��a de teatro
	 * 
	 * @param id
	 * @param fechaInicio
	 * @param fechaFin
	 * @param edad
	 * @param alfa
	 * @param desc
	 * @return
	 * @throws SQLException
	 * @throws Exception
	 */
	public List<UsuarioSimple> getInasistenciaFestivAndes(Long id, String fechaInicio, String fechaFin, Integer edad,
			Integer alfa, Integer desc) throws SQLException, Exception {

		conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
		List<UsuarioSimple> usuarios = new ArrayList<>();

		String sql = "SELECT * FROM ((SELECT U.ID, U.NOMBRE, U.CORREOELECTRONICO, U.FECHANACIMIENTO, R.NOMBRE AS ROL "
				+ "FROM USUARIOS U INNER JOIN ROLES R ON U.IDROL=R.ID) "
				+ "MINUS (SELECT U.ID, U.NOMBRE, U.CORREOELECTRONICO, U.FECHANACIMIENTO, R.NOMBRE AS ROL "
				+ "FROM (((((USUARIOS U INNER JOIN BOLETAS B ON U.ID=B.IDUSUARIO) "
				+ "INNER JOIN FUNCIONES F ON F.ID = B.IDFUNCION) "
				+ "INNER JOIN ESPECTACULOS E ON E.ID=F.IDESPECTACULO) "
				+ "INNER JOIN PRESENTAN P ON P.IDESPECTACULO=E.ID) "
				+ "INNER JOIN COMPANIASTEATRO C ON C.ID=P.IDCOMPANIA) "
				+ "INNER JOIN ROLES R ON R.ID = U.IDROL WHERE C.ID = " + id.toString()
				+ " AND ( TO_DATE(to_char(F.FECHA, 'RRRR-MM-dd'),'RRRR-MM-dd') BETWEEN " + "TO_DATE('" + fechaInicio
				+ "','RRRR-MM-dd') AND TO_DATE('" + fechaFin + "','RRRR-MM-dd')) "
				+ "GROUP BY U.ID, U.NOMBRE, U.CORREOELECTRONICO, U.FECHANACIMIENTO, R.NOMBRE)) INAS";

		String order = " ORDER BY ";
		if (edad != null && edad == 1) {
			order += "FECHANACIMIENTO";
			if (alfa != null && alfa == 1) {
				order += ",NOMBRE";
			}
			if (desc != null && desc == 1) {
				order += " DESC ";
			}
			sql += order;
		} else if (alfa != null && alfa == 1) {
			order += "NOMBRE";
			if (desc != null && desc == 1) {
				order += " DESC ";
			}
			sql += order;
		}

		System.out.println(sql);
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet res = prepStmt.executeQuery();
		while (res.next()) {
			usuarios.add(new UsuarioSimple(res.getLong("ID"), res.getString("NOMBRE"),
					res.getString("CORREOELECTRONICO"), res.getString("FECHANACIMIENTO"), res.getString("ROL")));
		}

		return usuarios;
	}

	public void retirarCompania(Long idCompania) throws Exception {
		String sql = "SELECT F.ID FROM COMPANIASTEATRO JOIN PRESENTAN P ON (C.ID=P.IDCOMPANIA) "
				+ "JOIN ESPECTACULOS E ON (E.ID=P.IDESPECTACULO) " + "JOIN FUNCIONES F ON F.IDESPECTACULO=E.ID "
				+ "WHERE C.ID=" + idCompania;
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		ResultSet rs = prepStmt.executeQuery();
		while (rs.next()) {
			Long id = rs.getLong("ID");

			sql = "SELECT B.ID AS ID, B.IDUSUARIO, B.IDABONAMIENTO, L.PRECIO FROM BOLETAS B JOIN LOCALIDADES L ON B.IDLOCALIDAD=L.ID WHERE idFuncion="
					+ id;
			prepStmt = conn.prepareStatement(sql);
			rs = prepStmt.executeQuery();

			boolean exist = false;
			while (rs.next()) {
				exist = true;
				if (rs.getString("IDUSUARIO") != null && rs.getString("IDUSUARIO") != "NULL") {
					if (rs.getString("IDABONAMIENTO") != "NULL") {
						sql = "INSERT INTO CERTIFICADOSDEDEVOLUCION (ID,IDUSUARIO,IDBOLETA,VALOR) VALUES (IDCERTIFICADOSDEDEVOLUCION.NEXTVAL,"
								+ rs.getString("IDUSUARIO") + ",NULL," + (rs.getDouble("PRECIO") * 0.8) + " )";

					} else {
						sql = "INSERT INTO CERTIFICADOSDEDEVOLUCION (ID,IDUSUARIO,IDBOLETA,VALOR) VALUES (IDCERTIFICADOSDEDEVOLUCION.NEXTVAL,"
								+ rs.getString("IDUSUARIO") + ",NULL," + (rs.getString("PRECIO")) + " )";

					}
					System.out.println(sql);
					prepStmt = conn.prepareStatement(sql);
					rs = prepStmt.executeQuery();
				}

			}
			if (!exist)
				throw new Exception("La funci�n no existe o ya fue realizada.");
			sql = "DELETE FROM BOLETAS WHERE idFuncion=" + id;
			System.out.println(sql);
			prepStmt = conn.prepareStatement(sql);
			rs = prepStmt.executeQuery();

			sql = "DELETE FROM FUNCIONES WHERE id=" + id;
			System.out.println(sql);

			prepStmt = conn.prepareStatement(sql);
			rs = prepStmt.executeQuery();

		}

		sql = "DELETE FROM COMPANIASTEATRO WHERE ID=" + idCompania;
		System.out.println(sql);
		prepStmt = conn.prepareStatement(sql);
		rs = prepStmt.executeQuery();

	}

}
