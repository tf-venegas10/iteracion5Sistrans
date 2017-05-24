package rest;
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
import vos.Sitio;
import vos.Usuario;


/**
 * Clase que expone servicios REST con ruta base: http://"ip o nombre de host":8080/FestivAndes/rest/...
 * @author Tomas F. Venegas
 */
@Path("/sitios")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class FestivAndesSitioServicios {
	/**
	 * Atributo que usa la anotación @Context para tener el ServletContext de la conexión actual.
	 */
	@Context
	private ServletContext context;

	/**
	 * Método que retorna el path de la carpeta WEB-INF/ConnectionData en el deploy actual dentro del servidor.
	 * @return path de la carpeta WEB-INF/ConnectionData en el deploy actual.
	 */
	private String getPath() {
		return context.getRealPath("WEB-INF/ConnectionData");
	}
	
	
	private String doErrorMessage(Exception e){
		return "{ \"ERROR\": \""+ e.getMessage() + "\"}" ;
	}
	
	@POST
	public void crarSitio(Sitio sitio, @QueryParam("acces_token") String token){
		FestivAndesMaster tm= new FestivAndesMaster(getPath());
		try {
			Usuario usuario=tm.getUser(token);
			if (!usuario.getRol().equals("ADMINISTRADOR")) throw new Exception("El usuario usado no tiene los permisos suficientes.");
			tm.crearSitio(sitio);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Response.status(500).entity(doErrorMessage(e)).build();
		}
	}
	
	@GET
	@Path("/{id}")
	public Response buscarSitio(@PathParam("id") Long id, @QueryParam("acces_token") String token, @QueryParam("intereses") int intereses){
		FestivAndesMaster tm= new FestivAndesMaster(getPath());
		Sitio s = null;
		try {
			if (intereses==1){
			Usuario usuario=tm.getUser(token);
				s=tm.buscarSitio(id, usuario.getId());
			}
			else{
				s=tm.buscarSitio(id);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(s).build();
	}
}
