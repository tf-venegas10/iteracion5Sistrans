package rest;
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
import vos.Espectaculo;
import vos.EspectaculoPopular;
import vos.ReporteEspectaculo;
import vos.Usuario;

/**
 * Clase que expone servicios REST con ruta base: http://"ip o nombre de host":8080/FestivAndes/rest/...
 * @author Tomas F. Venegas
 */
@Path("/espectaculos")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class FestivAndesEspectaculoServicios {

	/**
	 * Atributo que usa la anotación @Context para tener el ServletContext de la conexión actual.
	 */
	@Context
	private ServletContext context;
	
	public FestivAndesEspectaculoServicios() {
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
	public Response addEspectaculo(Espectaculo espectaculo, @QueryParam("acces_token")String token) {
		FestivAndesMaster tm = new FestivAndesMaster(getPath());
		try {
			Usuario usuario=tm.getUser(token);
			if (!usuario.getRol().equals("ADMINISTRADOR")) throw new Exception("El usuario usado no tiene los permisos suficientes.");
			tm.addEspectaculo(espectaculo);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(espectaculo).build();
	}
	
	/**
     * Método que expone servicio REST usando POST que actualiza el video que recibe en Json
     * <b>URL: </b> http://"ip o nombre de host":8080/VideoAndes/rest/videos/video
     * @param video - video a actualizar. 
     * @return Json con el video que actualizo o Json con el error que se produjo
     */
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getEspectaculos() {
		FestivAndesMaster tm = new FestivAndesMaster(getPath());
		List<Espectaculo> res = null;
		try {
			res = tm.getEspectaculos();
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(res).build();
	}
	
	/**
     * Método que expone servicio REST usando POST que actualiza el video que recibe en Json
     * <b>URL: </b> http://"ip o nombre de host":8080/VideoAndes/rest/videos/video
     * @param video - video a actualizar. 
     * @return Json con el video que actualizo o Json con el error que se produjo
     */
	@GET
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)

	public Response getEspectaculoMasOcupacion(@PathParam("id") Long id) {

		FestivAndesMaster tm = new FestivAndesMaster(getPath());
		ReporteEspectaculo res = null;
		try {
			res = tm.getReporteEspectaculo(id);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(res).build();
	}
	
	/**
     * Método que expone servicio REST usando POST que actualiza el video que recibe en Json
     * <b>URL: </b> http://"ip o nombre de host":8080/VideoAndes/rest/videos/video
     * @param video - video a actualizar. 
     * @return Json con el video que actualizo o Json con el error que se produjo
     */
	@GET
	@Path("/populares")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getEspectaculosMasPopulares(@QueryParam("cantidad") int cantidad, @QueryParam("fechaInicio")String fechaInicio,@QueryParam("fechaFin")String fechaFin) {
		FestivAndesMaster tm = new FestivAndesMaster(getPath());
		List<EspectaculoPopular> res = null;
		try {
			res = tm.getEspectaculosMasPopulares(cantidad,fechaInicio,fechaFin);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(res).build();
	}
}
