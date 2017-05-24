package rest;
import java.util.List;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
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
import vos.CompaniaTeatro;
import vos.ConsultaCompania;
import vos.Usuario;
import vos.UsuarioSimple;

/**
 * Clase que expone servicios REST con ruta base: http://"ip o nombre de host":8080/FestivAndes/rest/...
 * @author Tomas F. Venegas
 */
@Path("/companias")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class FestivAndesCompaniaServicios {

	/**
	 * Atributo que usa la anotación @Context para tener el ServletContext de la conexión actual.
	 */
	@Context
	private ServletContext context;
	
	public FestivAndesCompaniaServicios() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Método que retorna el path de la carpeta WEB-INF/ConnectionData en el deploy actual dentro del servidor.
	 * @return path de la carpeta WEB-INF/ConnectionData en el deploy actual.
	 */
	private String getPath() {
		return context.getRealPath("WEB-INF/ConnectionData");
	}
	
	/**
	 * M�todo que contruye el error para una transacci�n
	 * @param e Excepci�n base
	 * @return Mensaje de error
	 */
	private String doErrorMessage(Exception e){
		return "{ \"ERROR\": \""+ e.getMessage() + "\"}" ;
	}
	
	/**
     * Método que expone servicio REST usando POST que actualiza el video que recibe en Json
     * <b>URL: </b> http://"ip o nombre de host":8080/VideoAndes/rest/videos/video
     * @param video - video a actualizar. 
     * @return Json con el video que actualizo o Json con el error que se produjo
     */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addCompaniaTeatro(CompaniaTeatro compania, @QueryParam("acces_token")String token) {
		FestivAndesMaster tm = new FestivAndesMaster(getPath());
		try {
			tm.addCompaniaTeatro(compania, token);
			
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(compania).build();
	}
	
	/**
     * Método que expone servicio REST usando POST que actualiza el video que recibe en Json
     * <b>URL: </b> http://"ip o nombre de host":8080/VideoAndes/rest/videos/video
     * @param video - video a actualizar. 
     * @return Json con el video que actualizo o Json con el error que se produjo
     */
	@GET
	@Path("/{id}/consulta")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response consultaCompaniaTeatro(@PathParam("id")Long id, @QueryParam("acces_token")String token) {
		FestivAndesMaster tm = new FestivAndesMaster(getPath());
		ConsultaCompania consul = null;
		try {
			Usuario usuario=tm.getUser(token);
			if (usuario.getRol().equals("OPERARIO")) consul = tm.consultaCompaniaTeatro(id,usuario.getId(),false);
			else if(usuario.getRol().equals("ADMINISTRADOR"))consul = tm.consultaCompaniaTeatro(id,usuario.getId(),true);
			else throw new Exception("El usuario usado no tiene los permisos suficientes.");
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(consul).build();
	}
	

	/**
	 * M�todo que consulta la asistencia de los usuarios a FestivAndes
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
	@GET
	@Path("/{id}/asistencia")
	public Response getAsistenciaFestivAndes(@PathParam("id") Long id, @QueryParam("access_token") String token,
			@QueryParam("fechaInicio")String fechaInicio,@QueryParam("fechaFin")String fechaFin,@QueryParam("edad")Integer edad,
			@QueryParam("alfa")Integer alfa, @QueryParam("desc")Integer desc, @QueryParam("group")Integer group){
		//operario
		FestivAndesMaster tm= new FestivAndesMaster(getPath());
		List<UsuarioSimple> usuarios = null;
		try {
			Usuario usuario=tm.getUser(token);
			boolean existe = tm.existsCompany(id);
			if(!existe) throw new Exception("La compa��a de teatro seleccionada no existe");
			if (usuario==null) throw new Exception("El access_token especificado es incorrecto");
			if (!usuario.getRol().equals("ADMINISTRADOR")) throw new Exception("El usuario usado no tiene los permisos suficientes.");
			usuarios = tm.getAsistenciaFestivAndes(id,fechaInicio,fechaFin,edad,alfa,desc,group);
		} catch (Exception e) {

			e.printStackTrace();
			 return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(usuarios).build();
		
	} 
	
	/**
	 * M�todo que consulta la inasistencia de los usuarios a FestivAndes
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
	@GET
	@Path("/{id}/inasistencia")
	public Response getInasistenciaFestivAndes(@PathParam("id") Long id, @QueryParam("access_token") String token,
			@QueryParam("fechaInicio")String fechaInicio,@QueryParam("fechaFin")String fechaFin,@QueryParam("edad")Integer edad,
			@QueryParam("alfa")Integer alfa, @QueryParam("desc")Integer desc, @QueryParam("group")Integer group){
		//operario
		FestivAndesMaster tm= new FestivAndesMaster(getPath());
		List<UsuarioSimple> usuarios = null;
		try {
			Usuario usuario=tm.getUser(token);
			boolean existe = tm.existsCompany(id);
			if(!existe) throw new Exception("La compa��a de teatro seleccionada no existe");
			if (usuario==null) throw new Exception("El access_token especificado es incorrecto");
			if (!usuario.getRol().equals("ADMINISTRADOR")) throw new Exception("El usuario usado no tiene los permisos suficientes.");
			usuarios = tm.getInasistenciaFestivAndes(id,fechaInicio,fechaFin,edad,alfa,desc);
		} catch (Exception e) {

			e.printStackTrace();
			 return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(usuarios).build();
		
	} 
	
	
	@DELETE
	@Path("/{id}/multi")
	public Response retirarCompaniaDistribuida(@PathParam("id") Long id){
		FestivAndesMaster tm= new FestivAndesMaster(getPath());
		try{
			tm.retirarCompania(id);
		}
		catch(Exception e){
			e.printStackTrace();
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(new String ("OK")).build();
	}
}
