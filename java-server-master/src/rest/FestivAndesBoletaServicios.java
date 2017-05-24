package rest;

import java.util.ArrayList;
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
import vos.Boleta;
import vos.BoletaDetail;
import vos.CertificadoDeDevolucion;
import vos.Funcion;
import vos.Localidad;
import vos.Usuario;

@Path("/boletas")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class FestivAndesBoletaServicios {

	/**
	 * Atributo que usa la anotaciÃ³n @Context para tener el ServletContext de la conexiÃ³n actual.
	 */
	@Context
	private ServletContext context;
	
	public FestivAndesBoletaServicios() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * MÃ©todo que retorna el path de la carpeta WEB-INF/ConnectionData en el deploy actual dentro del servidor.
	 * @return path de la carpeta WEB-INF/ConnectionData en el deploy actual.
	 */
	private String getPath() {
		return context.getRealPath("WEB-INF/ConnectionData");
	}
	
	/**
	 * Método que contruye el error para una transacción
	 * @param e Excepción base
	 * @return Mensaje de error
	 */
	private String doErrorMessage(Exception e){
		return "{ \"ERROR\": \""+ e.getMessage() + "\"}" ;
	}
	
	/**
	 * Método que registra la compra de una boleta
	 * @param idBoleta id de la boleta a comprar
	 * @param idUsuario id del cliente que compra la boleta
	 * @return La respuesta a la solicitud de compra de una boleta
	 */
	@PUT
	@Path("/{id: \\d+}")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response registroCompraBoleta(@PathParam("id") Long idBoleta, @QueryParam("usuario") Long idUsuario, @QueryParam("access_token")String accessToken){
		FestivAndesMaster tm = new FestivAndesMaster(getPath());
		BoletaDetail bol = null;
		try{
			if(idBoleta == null )throw new Exception("El id de la boleta no es válido");
			bol = tm.registroCompraBoleta(idBoleta,idUsuario, accessToken);
		}catch(Exception e){
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(bol).build();
	}
	
	/**
	 * Método que hace la compra efectiva de boletas para una cantidad mayor o igual a 2
	 * @param idUsuario al que se le asocia la boleta. Puede ser null al ser un usuario no registrado
	 * @param cantidad >=2 de boletas que se van a comprar
	 * @return La respuesta a la solicitud de compra de una cantidad determinada de boletas
	 */
	@PUT
	@Produces({ MediaType.APPLICATION_JSON })
	public Response registroCompraBoletas(@QueryParam("usuario")Long idUsuario, @QueryParam("access_token")String accessToken,@QueryParam("funcion")Long idFuncion, @QueryParam("cantidad")Integer cantidad){
		FestivAndesMaster tm = new FestivAndesMaster(getPath());
		List<BoletaDetail> boletas = new ArrayList<BoletaDetail>();
		try{
			if(cantidad == null || cantidad<2 )throw new Exception("La cantidad de boletas debe existir y ser mayor igual a 2");
			else if(idFuncion == null)throw new Exception("La función a la cual se quiere asistir debe ser especificada");
			else if(idUsuario == null|| accessToken == null) throw new Exception("El usuario especificado es incorrecto o no tiene el acceso permitido");
			boletas = tm.registroCompraBoletas(idUsuario,accessToken,idFuncion,cantidad);
		}catch(Exception e){
			if(e.getMessage().contains("ORA-08177"))e = new Exception("La transacción no se pudo completar porque alguien más ya compró la boleta.");
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(boletas).build();
	}
	
	/**
	 * Método que registra la devolución de una boleta
	 * @param idBoleta id de la boleta a devolver
	 * @param idUsuario id del cliente que devuelve la boleta
	 * @return La respuesta a la solicitud de compra de una boleta
	 */
	@DELETE
	@Path("/{id: \\d+}")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response registroDevolucionBoleta(@PathParam("id") Long idBoleta, @QueryParam("usuario") Long idUsuario, @QueryParam("acces_token")String accessToken){
		FestivAndesMaster tm = new FestivAndesMaster(getPath());
		CertificadoDeDevolucion cert = null;
		try{
			if(idBoleta == null )throw new Exception("El id de la boleta no es válido");
			if(idUsuario == null || accessToken == null)throw new Exception("El usuario especificado es incorrecto o no tiene el acceso permitido");
			cert = tm.registroDevolucionBoleta(idBoleta,idUsuario, accessToken);
		}catch(Exception e){
			e.printStackTrace();
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(cert).build();
	}
	
}
