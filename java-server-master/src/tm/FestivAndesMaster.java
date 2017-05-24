package tm;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import dao.DAOTablaBoletas;
import dao.DAOTablaCompania;
import dao.DAOTablaEspectaculos;
import dao.DAOTablaFunciones;
import dao.DAOTablaSitios;
import dao.DAOTablaUsuarios;
import dtm.FestivAndesDistributed;
import jms.NonReplyException;
import vos.AsistenciaUsuario;
import vos.BoletaDetail;
import vos.BoletasCompra;
import vos.CertificadoDeDevolucion;
import vos.CompaniaTeatro;
import vos.ConsultaCompania;
import vos.Espectaculo;
import vos.EspectaculoPopular;
import vos.Funcion;
import vos.Localidad;
import vos.ReporteEspectaculo;
import vos.ReporteFuncion;
import vos.Sitio;
import vos.Usuario;
import vos.UsuarioBuenCliente;
import vos.UsuarioSimple;
import vos.VOAbonamiento;
import vos.VOBoleta;
import vos.VOFuncion;

/**
 * Fachada en patron singleton de la aplicación
 */
public class FestivAndesMaster {

	/**
	 * Atributo estático que contiene el path relativo del archivo que tiene los
	 * datos de la conexión
	 */
	private static final String CONNECTION_DATA_FILE_NAME_REMOTE = "/conexion.properties";

	/**
	 * Atributo estático que contiene el path absoluto del archivo que tiene los
	 * datos de la conexión
	 */
	private String connectionDataPath;

	/**
	 * Atributo que guarda el usuario que se va a usar para conectarse a la base
	 * de datos.
	 */
	private String user;

	/**
	 * Atributo que guarda la clave que se va a usar para conectarse a la base
	 * de datos.
	 */
	private String password;

	/**
	 * Atributo que guarda el URL que se va a usar para conectarse a la base de
	 * datos.
	 */
	private String url;

	/**
	 * Atributo que guarda el driver que se va a usar para conectarse a la base
	 * de datos.
	 */
	private String driver;

	/**
	 * Conexión a la base de datos
	 */
	private Connection conn;

	/**
	 * Método constructor de la clase FestivAndesMaster, esta clase modela y
	 * contiene cada una de las transacciones y la logica de negocios que estas
	 * conllevan. <b>post: </b> Se crea el objeto FestivAndesMaster, se
	 * inicializa el path absoluto de el archivo de conexión y se inicializa los
	 * atributos que se usan par la conexión a la base de datos.
	 * 
	 * @param contextPathP
	 *            - path absoluto en el servidor del contexto del deploy actual
	 */
	public FestivAndesMaster(String contextPathP) {
		connectionDataPath = contextPathP + CONNECTION_DATA_FILE_NAME_REMOTE;
		initConnectionData();
	}

	/*
	 * Método que inicializa los atributos que se usan para la conexion a la
	 * base de datos. <b>post: </b> Se han inicializado los atributos que se
	 * usan par la conexión a la base de datos.
	 */
	private void initConnectionData() {
		try {
			File arch = new File(this.connectionDataPath);
			Properties prop = new Properties();
			FileInputStream in = new FileInputStream(arch);
			prop.load(in);
			in.close();
			this.url = prop.getProperty("url");
			this.user = prop.getProperty("usuario");
			this.password = prop.getProperty("clave");
			this.driver = prop.getProperty("driver");
			Class.forName(driver);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Método que retorna la conexión a la base de datos
	 * 
	 * @return Connection - la conexión a la base de datos
	 * @throws SQLException
	 *             - Cualquier error que se genere durante la conexión a la base
	 *             de datos
	 */
	private Connection darConexion() throws SQLException {
		System.out.println("Connecting to: " + url + " With user: " + user);
		return DriverManager.getConnection(url, user, password);
	}

	////////////////////////////////////////
	/////// Transacciones////////////////////
	////////////////////////////////////////

	/**
	 * Método que modela la transacción que retorna todos los videos de la base
	 * de datos.
	 * 
	 * @return ListaVideos - objeto que modela un arreglo de videos. este
	 *         arreglo contiene el resultado de la búsqueda
	 * @throws Exception
	 *             - cualquier error que se genere durante la transacción
	 */
	public void registrarUsuario(Usuario usuario, String rol) throws Exception {

		DAOTablaUsuarios daoUsuario = new DAOTablaUsuarios();

		try {
			////// Transacción
			this.conn = darConexion();
			daoUsuario.setConn(conn);
			daoUsuario.addUsuario(usuario, rol);
		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoUsuario.cerrarRecursos();
				if (this.conn != null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	/**
	 * Método que obtiene un usuario dado su access token
	 * 
	 * @param acces_token
	 * @return
	 * @throws SQLException
	 */
	public Usuario getUser(String acces_token) throws SQLException {
		DAOTablaUsuarios daoUsuario = new DAOTablaUsuarios();
		Usuario usr = null;
		try {
			////// Transacción
			this.conn = darConexion();
			daoUsuario.setConn(conn);
			usr = daoUsuario.getUserId(acces_token);
		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoUsuario.cerrarRecursos();
				if (this.conn != null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return usr;
	}

	public void registrarSitioUsuario(Long idUsuario, Long idSitio) throws Exception {
		DAOTablaUsuarios daoUsuario = new DAOTablaUsuarios();

		try {
			////// Transacción
			this.conn = darConexion();
			daoUsuario.setConn(conn);
			daoUsuario.addSitio(idUsuario, idSitio);
		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoUsuario.cerrarRecursos();
				if (this.conn != null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}

	}

	public void registrarCategoriaUsuario(Long idUsuario, Long idCategoria) throws Exception {
		DAOTablaUsuarios daoUsuario = new DAOTablaUsuarios();

		try {
			////// Transacción
			this.conn = darConexion();
			daoUsuario.setConn(conn);
			daoUsuario.addCategoria(idUsuario, idCategoria);
		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoUsuario.cerrarRecursos();
				if (this.conn != null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}

	}

	/**
	 * Método que hace la consulta de asistencia de un cliente
	 * 
	 * @param idUsuario
	 * @param token
	 * @return
	 * @throws SQLException
	 * @throws Exception
	 */
	public AsistenciaUsuario getAsistenciaFestival(Long idUsuario) throws SQLException, Exception {
		DAOTablaUsuarios daoUsuarios = new DAOTablaUsuarios();
		AsistenciaUsuario as = null;
		try {
			this.conn = darConexion();
			daoUsuarios.setConn(conn);
			as = daoUsuarios.getAsistenciaFestival(idUsuario);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoUsuarios.cerrarRecursos();
				if (this.conn != null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return as;

	}

	/**
	 * Método que retorna la consulta de los buenos clientes
	 * 
	 * @param cantidad
	 * @return
	 */
	public List<UsuarioBuenCliente> getBuenosClientes(Integer cantidad) throws SQLException, Exception {
		DAOTablaUsuarios daoUsuarios = new DAOTablaUsuarios();
		List<UsuarioBuenCliente> buenos = null;
		try {
			this.conn = darConexion();
			daoUsuarios.setConn(conn);
			buenos = daoUsuarios.getBuenosClientes(cantidad);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoUsuarios.cerrarRecursos();
				if (this.conn != null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return buenos;
	}

	/**
	 * Método que hace el registro de compra de una boleta
	 * 
	 * @param idBoleta
	 * @param idUsuario
	 * @param accessToken
	 * @return
	 * @throws Exception
	 */
	public BoletaDetail registroCompraBoleta(Long idBoleta, Long idUsuario, String accessToken) throws Exception {
		DAOTablaBoletas daoBoletas = new DAOTablaBoletas();
		BoletaDetail bol = null;
		try {
			this.conn = darConexion();
			daoBoletas.setConn(conn);
			bol = daoBoletas.registroCompraBoleta(idBoleta, idUsuario, accessToken);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoBoletas.cerrarRecursos();
				if (this.conn != null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return bol;
	}

	/**
	 * Método que hace registro de compra de varias boletas
	 * 
	 * @param idUsuario
	 * @param accessToken
	 * @param idFuncion
	 * @param cantidad
	 * @return
	 * @throws Exception
	 */
	public List<BoletaDetail> registroCompraBoletas(Long idUsuario, String accessToken, Long idFuncion,
			Integer cantidad) throws Exception {
		DAOTablaBoletas daoBoletas = new DAOTablaBoletas();
		List<BoletaDetail> bol = null;
		try {
			this.conn = darConexion();
			daoBoletas.setConn(conn);
			bol = daoBoletas.registroCompraBoletas(idUsuario, accessToken, idFuncion, cantidad);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoBoletas.cerrarRecursos();
				if (this.conn != null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return bol;
	}

	/**
	 * Método que devuelve una boleta comprada por un cliente
	 * 
	 * @param idBoleta
	 * @param idUsuario
	 * @param accessToken
	 * @return Certificado de devolución
	 */
	public CertificadoDeDevolucion registroDevolucionBoleta(Long idBoleta, Long idUsuario, String accessToken)
			throws SQLException, Exception {
		DAOTablaBoletas daoBoletas = new DAOTablaBoletas();
		CertificadoDeDevolucion cert = null;
		try {
			this.conn = darConexion();
			daoBoletas.setConn(conn);
			cert = daoBoletas.registroDevolucionBoleta(idBoleta, idUsuario, accessToken);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoBoletas.cerrarRecursos();
				if (this.conn != null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return cert;
	}

	/**
	 * Método que obtiene una compañía dado su id
	 * 
	 * @param id
	 *            de la companía
	 * @return si existe o no
	 */
	public boolean existsCompany(Long id) throws SQLException, Exception {
		DAOTablaCompania daoCompania = new DAOTablaCompania();
		boolean consul = false;
		try {
			////// Transacción
			this.conn = darConexion();
			daoCompania.setConn(conn);
			consul = daoCompania.existsCompany(id);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoCompania.cerrarRecursos();
				if (this.conn != null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return consul;
	}

	/**
	 * Método que agrega una compañía de teatro a la base de datos
	 * 
	 * @param compania
	 * @throws Exception
	 */
	public void addCompaniaTeatro(CompaniaTeatro compania, String token) throws Exception {
		DAOTablaCompania daoCompania = new DAOTablaCompania();
		try {
			////// Transacción
			this.conn = darConexion();
			daoCompania.setConn(conn);
			daoCompania.addCompaniaTeatro(compania, token);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoCompania.cerrarRecursos();
				if (this.conn != null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	/**
	 * Método que devuelve una consulta para una compañía de teatro
	 * 
	 * @param id
	 * @return
	 */
	public ConsultaCompania consultaCompaniaTeatro(Long id, Long idRepresentante, boolean admin)
			throws SQLException, Exception {
		DAOTablaCompania daoCompania = new DAOTablaCompania();
		ConsultaCompania consul = null;
		try {
			////// Transacción
			this.conn = darConexion();
			daoCompania.setConn(conn);
			consul = daoCompania.consultaCompaniaTeatro(id, idRepresentante, admin);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoCompania.cerrarRecursos();
				if (this.conn != null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return consul;
	}

	/**
	 * Método que consulta la asistencia al festival de los usuarios según
	 * criterios establecidos por el administrador
	 * 
	 * @param id
	 * @param token
	 * @param fechaInicio
	 * @param fechaFin
	 * @param edad
	 * @param alfa
	 * @param desc
	 * @param group
	 * @return
	 */
	public List<UsuarioSimple> getAsistenciaFestivAndes(Long id, String fechaInicio, String fechaFin, Integer edad,
			Integer alfa, Integer desc, Integer group) throws Exception, SQLException {

		DAOTablaCompania daocompania = new DAOTablaCompania();
		List<UsuarioSimple> list = null;
		try {
			////// Transacción
			this.conn = darConexion();
			daocompania.setConn(conn);
			list = daocompania.getAsistenciaFestivAndes(id, fechaInicio, fechaFin, edad, alfa, desc, group);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daocompania.cerrarRecursos();
				if (this.conn != null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}

		return list;
	}

	/**
	 * Método que obtiene la inasistencia de usuarios para una función de una
	 * compañía de teatro
	 * 
	 * @param id
	 * @param fechaInicio
	 * @param fechaFin
	 * @param edad
	 * @param alfa
	 * @param desc
	 * @return
	 */
	public List<UsuarioSimple> getInasistenciaFestivAndes(Long id, String fechaInicio, String fechaFin, Integer edad,
			Integer alfa, Integer desc) throws SQLException, Exception {
		DAOTablaCompania daocompania = new DAOTablaCompania();
		List<UsuarioSimple> list = null;
		try {
			////// Transacción
			this.conn = darConexion();
			daocompania.setConn(conn);
			list = daocompania.getInasistenciaFestivAndes(id, fechaInicio, fechaFin, edad, alfa, desc);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daocompania.cerrarRecursos();
				if (this.conn != null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}

		return list;
	}

	/**
	 * Método que agrega un espectaculo
	 */
	public void addEspectaculo(Espectaculo espectaculo) throws Exception {
		DAOTablaEspectaculos daoEspectaculos = new DAOTablaEspectaculos();
		try {
			////// Transacción
			this.conn = darConexion();
			daoEspectaculos.setConn(conn);
			daoEspectaculos.addEspectaculo(espectaculo);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoEspectaculos.cerrarRecursos();
				if (this.conn != null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	public List<Espectaculo> getEspectaculos() throws SQLException, Exception {
		DAOTablaEspectaculos daoEspectaculos = new DAOTablaEspectaculos();
		List<Espectaculo> espec = new ArrayList<Espectaculo>();
		try {
			////// Transacción
			this.conn = darConexion();
			daoEspectaculos.setConn(conn);
			espec = daoEspectaculos.getEspectaculos();

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoEspectaculos.cerrarRecursos();
				if (this.conn != null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return espec;
	}

	/**
	 * Método que genera el reporte para un espectáculo
	 * 
	 * @param id
	 *            del espéctaculo
	 * @throws SQLException
	 */

	public ReporteEspectaculo getReporteEspectaculo(Long id) throws Exception {

		DAOTablaEspectaculos daoEspectaculo = new DAOTablaEspectaculos();
		ReporteEspectaculo espectaculoReporte = null;
		try {
			////// Transacción
			this.conn = darConexion();
			daoEspectaculo.setConn(conn);
			espectaculoReporte = daoEspectaculo.getReporteEspectaculo(id);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoEspectaculo.cerrarRecursos();
				if (this.conn != null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return espectaculoReporte;
	}

	/**
	 * Método que retorna los espectáculos más populares
	 * 
	 * @param cantidad
	 *            cantidad para recuperar
	 * @return espectáculos más populares
	 */
	public List<EspectaculoPopular> getEspectaculosMasPopulares(int cantidad, String fechaInicio, String fechaFin)
			throws Exception {
		DAOTablaEspectaculos daoEspectaculo = new DAOTablaEspectaculos();
		List<EspectaculoPopular> espectaculoPopular = null;
		try {
			////// Transacción
			this.conn = darConexion();
			daoEspectaculo.setConn(conn);
			espectaculoPopular = daoEspectaculo.getEspectaculosMasPopulares(cantidad, fechaInicio, fechaFin);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoEspectaculo.cerrarRecursos();
				if (this.conn != null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return espectaculoPopular;
	}

	public void crearSitio(Sitio sitio) throws Exception {
		DAOTablaSitios daoSitios = new DAOTablaSitios();
		try {
			////// Transacción
			this.conn = darConexion();
			daoSitios.setConn(conn);
			daoSitios.crearSitio(sitio);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoSitios.cerrarRecursos();
				if (this.conn != null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}

	}

	public Sitio buscarSitio(Long id) throws Exception {
		DAOTablaSitios daoSitios = new DAOTablaSitios();
		Sitio ret;
		try {
			////// Transacción
			this.conn = darConexion();
			daoSitios.setConn(conn);
			ret = daoSitios.buscarSitio(id);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoSitios.cerrarRecursos();
				if (this.conn != null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return ret;

	}

	public Sitio buscarSitio(Long id, Long idUsuario) throws Exception {
		DAOTablaSitios daoSitios = new DAOTablaSitios();
		Sitio ret;
		try {
			////// Transacción
			this.conn = darConexion();
			daoSitios.setConn(conn);
			ret = daoSitios.buscarSitioPreferencias(id, idUsuario);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoSitios.cerrarRecursos();
				if (this.conn != null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return ret;
	}

	/**
	 * Método que retorna todas las funciones de la base de datos
	 * 
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
	public List<Funcion> getFunciones(Date fechaInicio, Date fechaFin, String compania, String categoria, String idioma,
			Boolean traduccion, String aptos) throws SQLException {
		DAOTablaFunciones daofunciones = new DAOTablaFunciones();
		List<Funcion> list;
		try {
			////// Transacción
			this.conn = darConexion();
			daofunciones.setConn(conn);
			list = daofunciones.getFunciones(fechaInicio, fechaFin, compania, categoria, idioma, traduccion, aptos);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daofunciones.cerrarRecursos();
				if (this.conn != null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}

		return list;

	}

	/**
	 * Método que retorna un reporte de una función dada
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public ReporteFuncion getReporteFuncion(Long id) throws Exception {
		DAOTablaFunciones daofunciones = new DAOTablaFunciones();
		ReporteFuncion funcion = null;
		try {
			////// Transacción
			this.conn = darConexion();
			daofunciones.setConn(conn);
			funcion = daofunciones.getReporteFuncion(id);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daofunciones.cerrarRecursos();
				if (this.conn != null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return funcion;
	}

	/**
	 * Método que registra la realización de una función
	 * 
	 * @param id
	 * @throws Exception
	 */
	public void registrarRealizacionFuncion(Long id) throws Exception {
		DAOTablaFunciones daofunciones = new DAOTablaFunciones();
		try {
			////// Transacción
			this.conn = darConexion();
			daofunciones.setConn(conn);
			daofunciones.registrarRealizacionFuncion(id);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daofunciones.cerrarRecursos();
				if (this.conn != null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}

	}

	/**
	 * Método que registra una función en la base de datos
	 * 
	 * @param funcion
	 * @throws Exception
	 */
	public void registrarFuncion(Funcion funcion) throws Exception {
		DAOTablaFunciones daofunciones = new DAOTablaFunciones();
		try {
			////// Transacción
			this.conn = darConexion();
			daofunciones.setConn(conn);
			daofunciones.registrarFuncion(funcion);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daofunciones.cerrarRecursos();
				if (this.conn != null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}

	}

	/**
	 * Método que retorna información relevante de las funciones a partir de las
	 * boletas compradas
	 * 
	 * @param fechaInicio
	 * @param fechaFin
	 * @param elemento
	 * @param tipoLocalidad
	 * @param dia
	 * @return
	 */
	public List<BoletasCompra> comprasBoletas(String fechaInicio, String fechaFin, Integer elemento,
			Integer tipoLocalidad, String dia) throws SQLException, Exception {

		DAOTablaFunciones daofunciones = new DAOTablaFunciones();
		List<BoletasCompra> bols = null;
		try {
			////// Transacción
			this.conn = darConexion();
			daofunciones.setConn(conn);
			bols = daofunciones.comprasBoletas(fechaInicio, fechaFin, elemento, tipoLocalidad, dia);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daofunciones.cerrarRecursos();
				if (this.conn != null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return bols;
	}

	/**
	 * Método que compra un abono para un usuario
	 * 
	 * @param idUsuario
	 * @param func
	 * @param loc
	 * @return
	 * @throws Exception
	 */
	public double compraAbono(Long idUsuario, List<Funcion> func, List<Localidad> loc) throws Exception {
		DAOTablaBoletas dao = new DAOTablaBoletas();
		double total = 0;
		try {
			////// Transacción
			this.conn = darConexion();
			dao.setConn(conn);
			total = dao.registroCompraAbono(idUsuario, func, loc);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				dao.cerrarRecursos();
				if (this.conn != null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}

		return total;

	}

	public CertificadoDeDevolucion devolverAbono(Long idUsuario, Long idAbonamiento) throws Exception {
		DAOTablaBoletas dao = new DAOTablaBoletas();
		CertificadoDeDevolucion certificado = null;
		try {
			////// Transacción
			this.conn = darConexion();
			dao.setConn(conn);
			certificado = dao.devolverAbonamiento(idUsuario, idAbonamiento);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				dao.cerrarRecursos();
				if (this.conn != null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}

		return certificado;
	}

	public void eliminarFuncion(Long id) throws Exception {
		DAOTablaFunciones daofunciones = new DAOTablaFunciones();
		try {
			////// Transacción
			this.conn = darConexion();
			daofunciones.setConn(conn);
			daofunciones.eliminarFuncion(id);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daofunciones.cerrarRecursos();
				if (this.conn != null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}

	}

	public void retirarCompania(Long idCompania) throws Exception {
		DAOTablaCompania daocomp = new DAOTablaCompania();
		try {
			////// Transacción local
			this.conn = darConexion();
			daocomp.setConn(conn);
			daocomp.retirarCompania(idCompania);
			
			//transacción global
			FestivAndesDistributed dtm =  FestivAndesDistributed.getInstance();
			dtm.retirarCompaniaGlobal(idCompania);
			

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daocomp.cerrarRecursos();
				if (this.conn != null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}

	}
	
	public List<VOFuncion> getFuncionesDistribuidas() throws Exception {
		DAOTablaCompania daocomp = new DAOTablaCompania();
		try {
			////// Transacción local
			
			List<Funcion> funciones = getFunciones(null, null, null, null, null, null, null);
			List<VOFuncion> ret= new ArrayList<VOFuncion>();
			for (Iterator iterator = funciones.iterator(); iterator.hasNext();) {
				Funcion funcion = (Funcion) iterator.next();
				VOFuncion Vfuncion = new VOFuncion();
				Vfuncion.setFecha(funcion.getFecha());
				Vfuncion.setId(funcion.getId());
				Vfuncion.setNombreEspectaculo(funcion.getEspectaculo().getNombre());
				Vfuncion.setNombreSitio(funcion.getSitio().getNombre());
				ret.add(Vfuncion);
			}
			
			
			//transacción global
			FestivAndesDistributed dtm =  FestivAndesDistributed.getInstance();
			ret.addAll(dtm.getFuncionesGlobal());
			return ret;
			

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daocomp.cerrarRecursos();
				if (this.conn != null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}

	}

	public ArrayList<VOBoleta> crearAbonamientoGlobal(Long idUsuario, VOAbonamiento abonamiento) throws NonReplyException {
		
		// TODO Auto-generated method stub
		//LOCAL
		ArrayList<VOBoleta> abonamientos = compraAbono(idUsuario, abonamiento.getIdsFunciones(), abonamiento.getIdsLocalidades()) ;
		abonamiento.setIdUsuario(idUsuario);
		
		//GLOBAL
		FestivAndesDistributed dtm =  FestivAndesDistributed.getInstance();
		ArrayList<VOBoleta> abonamientosGlobal = dtm.getAbonamientosGlobal(abonamiento);
		abonamientos.addAll(abonamientosGlobal);
		return abonamientos;
	}

	private ArrayList<VOBoleta> compraAbono(Long idUsuario, ArrayList<Long> idsFunciones,
			ArrayList<String> idsLocalidades) {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}
