package rest;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import tm.FestivAndesMaster;
import vos.CertificadoDeDevolucion;
import vos.Funcion;
import vos.Localidad;
import vos.Tupla;
import vos.VOAbonamiento;
import vos.VOBoleta;

@Path("/abonamientos")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)

public class FestivAndesAbonamientoServicios {
	/**
	 * Atributo que usa la anotación @Context para tener el ServletContext de la conexión actual.
	 */
	@Context
	private ServletContext context;
	
	public FestivAndesAbonamientoServicios() {
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
	 * M�todo que contruye el error para una transacci�n
	 * @param e Excepci�n base
	 * @return Mensaje de error
	 */
	private String doSuccessMessage(String mensaje){
		return "{ \"SUCCESS\": \""+ mensaje + "\"}" ;
	}
	
	
	
	/**
	 * M�todo que regista la compra efectiva de de un abonamineto de boletas
	 * @param idUsuario al que se le asocia la boleta. No Puede ser null seg�n la l�gica de negocio. 
	 * @param funciones: lista de funciones
	 * @param localidades: lista de localidades asociada por orden a cada funcion.
	 * @return el precio del abonamineto total teniendo en cuenta el decuento de 20%
	 */
	@POST
	@Produces({ MediaType.APPLICATION_JSON })
	public Response registroCompraAbonamiento(@QueryParam("usuario")Long idUsuario,Tupla tuple){
		Funcion[] funciones = tuple.getFunciones();
		Localidad[] localidades = tuple.getLocalidades();
		FestivAndesMaster tm = new FestivAndesMaster(getPath());
		Double precio;
		try{
			if (idUsuario==null) throw new Exception("El id del usuario no puede ser null");
			if (funciones.length!=localidades.length) throw new Exception("La lista de funciones y de localidades debe tener el mismo tama�o.");
			List<Funcion> func= new ArrayList<Funcion>();
			List<Localidad> loc= new ArrayList<Localidad>();
			for (int i = 0; i < localidades.length; i++) {
				func.add(funciones[i]);
				loc.add(localidades[i]);
			}
			precio = tm.compraAbono(idUsuario,func,loc);
			
		}catch(Exception e){
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(precio).build();
	}
	
	@DELETE
	@Path("/{idAbonamiento: \\d+}")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response devolucionAbonamiento(@PathParam("idAbonamiento") Long idAbonamiento,@QueryParam("usuario")Long idUsuario){
		FestivAndesMaster tm = new FestivAndesMaster(getPath());
		CertificadoDeDevolucion certificado;
		try{
			if (idUsuario==null) throw new Exception("El id del usuario no puede ser null");
			if (idAbonamiento==null) throw new Exception("El id del abonamiento  no puede ser null");
			certificado = tm.devolverAbono(idUsuario,idAbonamiento);
			
		}catch(Exception e){
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(certificado).build();
	}

	
	@POST
	@Path("/abonarGlobal/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response crearAbonamientoGlobal(@PathParam("id")Long idUsuario, VOAbonamiento abonamiento)
	{
		ArrayList<VOBoleta> listBoletas = null;
		FestivAndesMaster tm = new FestivAndesMaster(getPath());
		try{
		listBoletas = tm.crearAbonamientoGlobal(idUsuario, abonamiento);
		}
		catch(Exception e)
		{
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).type("text/plain").entity(listBoletas.toString()).build();
	}
	
}
