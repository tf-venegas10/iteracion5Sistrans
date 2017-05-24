package rest;

import java.sql.Date;
import java.util.List;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import tm.FestivAndesMaster;
import vos.AsistenciaUsuario;
import vos.Usuario;
import vos.UsuarioBuenCliente;

/**
 * Clase que expone servicios REST con ruta base: http://"ip o nombre de host":8080/FestivAndes/rest/...
 * @author Tomas F. Venegas
 */
@Path("/usuarios")

public class FestivAndesUsuariosServicios {
	/**
	 * Atributo que usa la anotaciÃ³n @Context para tener el ServletContext de la conexiÃ³n actual.
	 */
	@Context
	private ServletContext context;

	public FestivAndesUsuariosServicios() 
	{
	}
	/**
	 * MÃ©todo que retorna el path de la carpeta WEB-INF/ConnectionData en el deploy actual dentro del servidor.
	 * @return path de la carpeta WEB-INF/ConnectionData en el deploy actual.
	 */
	private String getPath() {
		return context.getRealPath("WEB-INF/ConnectionData");
	}

	/**
	 * Método que construye el mensaje de error
	 * @param e
	 * @return
	 */
	private String doErrorMessage(Exception e){
		return "{ \"ERROR\": \""+ e.getMessage() + "\"}" ;
	}

	/**
	 * Método que registra un nuevo usuario en la base de datos
	 * @param usuario
	 * @param usr
	 * @param token
	 * @return
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response registrarUsuario(Usuario usuario, @QueryParam("usuario") String usr, @QueryParam("acces_token") String token) {

		FestivAndesMaster tm= new FestivAndesMaster(getPath());
		if (usuario.getContrasena()==null|| usuario.getCorreoElectronico()==null || usuario.getFechaNacimiento().after(new Date(System.currentTimeMillis())) ||
				usuario.getNombre()==null ){

			Response.status(500).entity(doErrorMessage(new Exception("Los atributos.. deben tener valores no nulos."))).build();
		}
		try {
			if(usr!="administrador" && usr!="operario"){
				Usuario usrr = tm.getUser(token);
				if (!usrr.getRol().equals("ADMINISTRADOR")) throw new Exception("El usuario usado no tiene los permisos suficientes.");
			}

			tm.registrarUsuario(usuario, usr);
		} catch (Exception e) {

			e.printStackTrace();

			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return  Response.status(200).entity(usuario).build();

	}

	/**
	 * Método que añade preferencias de sitio a un usuario determinado
	 * @param token
	 * @param id
	 * @param idSitio
	 * @return
	 */
	@POST
	@Path("/{id}/preferenciaSitio/{idSitio}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)

	public Response agregarPreferenciaSitio( @QueryParam("acces_token") String token, @PathParam("id") Long id, @PathParam("idSitio") Long idSitio) {
		FestivAndesMaster tm= new FestivAndesMaster(getPath());
		try{
			if (idSitio<0 || id<0) throw new Exception ("Los id deben ser positivos.");
			Usuario usr = tm.getUser(token);
			if(!usr.getId().equals(id)) throw new Exception ("No posee los permisos sobre este usuario.");
			tm.registrarSitioUsuario(id, idSitio);
		}
		catch(Exception e){
			return Response.status(500).entity(doErrorMessage(e)).build();

		}
		return  Response.status(200).build();

	}

	/**
	 * Método que agrega preferencias de categoría a un usuario
	 * @param token
	 * @param id
	 * @param idCategoria
	 * @return
	 */
	@POST
	@Path("/{id}/preferenciaCategoria/{idCategoria}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response agregarPreferenciaCategoria( @QueryParam("acces_token") String token, @PathParam("id") long id, @PathParam("idCategoria") long idCategoria) {
		FestivAndesMaster tm= new FestivAndesMaster(getPath());
		try{
			if (idCategoria<0 || id<0) throw new Exception ("Los id deben ser positivos.");
			Usuario usr = tm.getUser(token);
			if(!usr.getId().equals(id)) throw new Exception ("No posee los permisos sobre este usuario.");
			tm.registrarCategoriaUsuario(id, idCategoria);
		}catch(Exception e){
			return Response.status(500).entity(doErrorMessage(e)).build();

		}
		return  Response.status(200).build();
	}

	/**
	 * Método que consulta la asistencia de un cliente en el festival
	 * @param idUsuario id del usuario
	 * @param token access token del usuario
	 * @return Entidad de respuesta
	 */
	@GET
	@Path("/{id}/asistencia")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAsistenciaFestival(@PathParam("id") Long idUsuario, @QueryParam("acces_token") String token) {
		FestivAndesMaster tm= new FestivAndesMaster(getPath());
		AsistenciaUsuario as = null;
		try
		{
			if (idUsuario ==null) throw new Exception ("El id especificado no es correcto");
			Usuario usr = tm.getUser(token);
			if(!usr.getId().equals(idUsuario))
				if(!usr.getRol().equals("ADMINISTRADOR")) throw new Exception ("No posee los permisos sobre este usuario.");
			as = tm.getAsistenciaFestival(idUsuario);
		}
		catch(Exception e)
		{
			return Response.status(500).entity(doErrorMessage(e)).build();

		}
		return Response.status(200).entity(as).build();
	}
	
	/**
	 * Método que consulta los buenos clientes
	 * @param cantidad cantidad mínima
	 * @param access_token access token del admin
	 * @return Entidad de respuesta
	 */
	@GET
	@Path("/buenosclientes")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getBuenosClientes(@QueryParam("access_token") String token, @QueryParam("cantidad")Integer cantidad) {
		FestivAndesMaster tm= new FestivAndesMaster(getPath());
		List<UsuarioBuenCliente> buenos = null;
		try
		{
			Usuario usr = tm.getUser(token);
			if(usr==null) throw new Exception("El access_token especificado es erróneo");
			if(!usr.getRol().equals("ADMINISTRADOR")) throw new Exception ("No posee los permisos sobre este usuario.");
			buenos = tm.getBuenosClientes(cantidad);
		}
		catch(Exception e)
		{
			return Response.status(500).entity(doErrorMessage(e)).build();

		}
		return Response.status(200).entity(buenos).build();
	}
	
}
