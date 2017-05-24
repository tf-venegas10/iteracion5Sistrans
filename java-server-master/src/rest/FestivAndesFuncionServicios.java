package rest;
import java.sql.Date;
import java.util.List;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import tm.FestivAndesMaster;
import vos.BoletasCompra;
import vos.CertificadoDeDevolucion;
import vos.Funcion;
import vos.ReporteFuncion;
import vos.Usuario;
import vos.UsuarioSimple;
import vos.VOFuncion;


/**
 * Clase que expone servicios REST con ruta base: http://"ip o nombre de host":8080/FestivAndes/rest/...
 * @author Tomas F. Venegas
 */
@Path("/funciones")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class FestivAndesFuncionServicios {

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
	
	@GET
	public Response getFunciones(@QueryParam("fechaInicio") Date fechaInicio, @QueryParam("fechaFin") Date fechaFin, @QueryParam("compania") String compania,@QueryParam("categoria") String categoria, @QueryParam("idioma")String  idioma, @QueryParam("traduccion") String traduccion,@QueryParam("aptos") String aptos){
		FestivAndesMaster tm= new FestivAndesMaster(getPath());

		boolean raduccion = false;
		if(traduccion!=null) raduccion = traduccion.equals("true");

		List<Funcion> res = null;
		try {
			res = tm.getFunciones(fechaInicio, fechaFin, compania, categoria, idioma, raduccion, aptos);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		
		
		return Response.status(200).entity(res).build();
	}
	
	@GET
	@Path("/{id}")
	public Response getReporteFuncion(@PathParam("id") Long id){
		FestivAndesMaster tm= new FestivAndesMaster(getPath());
		ReporteFuncion res=null;
		try {
			res = tm.getReporteFuncion(id);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Response.status(500).entity(doErrorMessage(e)).build();
		}
		
		
		return Response.status(200).entity(res).build();
	}
	
	
	@PUT
	@Path("/{id}")
	public void registrarRealizacionFuncion(@PathParam("id") Long id, @QueryParam("acces_token") String token){
		//operario
		FestivAndesMaster tm= new FestivAndesMaster(getPath());
		try {
			Usuario usuario=tm.getUser(token);
			if (!usuario.getRol().equals("OPERARIO")) throw new Exception("El usuario usado no tiene los permisos suficientes.");
			tm.registrarRealizacionFuncion(id);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			 Response.status(500).entity(doErrorMessage(e)).build();
		}
		
	}
	
	@POST 
	public void registrarFuncion(Funcion funcion, @QueryParam("acces_token") String token){
		//administrador
		FestivAndesMaster tm= new FestivAndesMaster(getPath());
		try {
			Usuario usuario=tm.getUser(token);
			if (!usuario.getRol().equals("ADMINISTRADOR")) throw new Exception("El usuario usado no tiene los permisos suficientes.");
			tm.registrarFuncion(funcion);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Response.status(500).entity(doErrorMessage(e)).build();
		}
	}
	
	@DELETE
	@Path("/{id}")
	public Response eliminarFuncion(@PathParam("id") Long id, @QueryParam("acces_token") String token){
		//operario
		FestivAndesMaster tm= new FestivAndesMaster(getPath());
		try {
			Usuario usuario=tm.getUser(token);
			if (!usuario.getRol().equals("OPERARIO")) throw new Exception("El usuario usado no tiene los permisos suficientes.");
			tm.eliminarFuncion(id);
		} catch (Exception e) {

			e.printStackTrace();
			 return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(new String("Funcion cancelada")).build();
		
	}
	
	/**
	 * Método que registra la devolución de una boleta
	 * @param idBoleta id de la boleta a devolver
	 * @param idUsuario id del cliente que devuelve la boleta
	 * @return La respuesta a la solicitud de compra de una boleta
	 */
	@GET
	@Path("/compras")
	public Response comprasBoletas(@QueryParam("access_token")String token, @QueryParam("fechaInicio")String fechaInicio,
			@QueryParam("fechaFin")String fechaFin,@QueryParam("elemento")Integer elemento,@QueryParam("tipoLocalidad")Integer tipoLocalidad,
			@QueryParam("dia")String dia){
		
		FestivAndesMaster tm = new FestivAndesMaster(getPath());
		List<BoletasCompra> bols = null;
		try{
			Usuario usr = tm.getUser(token);
			if(usr==null) throw new Exception("El access_token especificado es erróneo");
			if(!usr.getRol().equals("ADMINISTRADOR")) throw new Exception ("No posee los permisos sobre este usuario.");
			bols = tm.comprasBoletas(fechaInicio,fechaFin,elemento,tipoLocalidad,dia);
		}catch(Exception e){
			e.printStackTrace();
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(bols).build();
	}


	@GET
	@Path("/multi")
	public Response getFunciones(){
		
		FestivAndesMaster tm = new FestivAndesMaster(getPath());
		List<VOFuncion> funciones = null;
		try{
			
			funciones = tm.getFuncionesDistribuidas();
		}catch(Exception e){
			e.printStackTrace();
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(funciones).build();
	}
}
