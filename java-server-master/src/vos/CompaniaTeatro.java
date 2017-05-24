/**-------------------------------------------------------------------
 * $Id$
 * Universidad de los Andes (Bogotá - Colombia)
 * Departamento de Ingeniería de Sistemas y Computación
 *
 * Materia: Sistemas Transaccionales
 * Ejercicio: VideoAndes
 * Autor: Juan Felipe García - jf.garcia268@uniandes.edu.co
 * -------------------------------------------------------------------
 */
package vos;

import java.sql.Date;
import java.text.SimpleDateFormat;

import org.codehaus.jackson.annotate.JsonProperty;

import oracle.sql.DATE;

/**
 * Clase que representa un Video
 * @author Juan
 */
public class CompaniaTeatro {

	//// Atributos

	/**
	 * Id 
	 */
	@JsonProperty(value="id")
	private Long id;

	/**
	 * Nombre 
	 */
	@JsonProperty(value="nombre")
	private String nombre;
	
	/**
	 * paisOrigen 
	 */
	@JsonProperty(value="paisOrigen")
	private String paisOrigen;
	
	/**
	 * urlPagina 
	 */
	@JsonProperty(value="urlPagina")
	private String urlPagina;
	
	/**
	 * representante 
	 */
	@JsonProperty(value="idRepresentante")
	private Long idRepresentante;
	
	/**
	 * fechaLlegada
	 */
	@JsonProperty(value="fechaLlegada")
	private Date fechaLlegada;
	
	/**
	 * fechaSalida
	 */
	@JsonProperty(value="fechaSalida")
	private Date fechaSalida;
	
	public CompaniaTeatro() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Método constructor de la clase 
	 * <b>post: </b> Crea el video con los valores que entran como parámetro
	 */
	public CompaniaTeatro(@JsonProperty(value="id")long id, @JsonProperty(value="nombre") String nombre,@JsonProperty(value="paisOrigen")
	 String paisOrigen,@JsonProperty(value="urlPagina")
	 String urlPagina,@JsonProperty(value="idRepresentante")
	 Long idRepresentante,@JsonProperty(value="fechaLlegada")
	 String fechaLlegada,@JsonProperty(value="fechaSalida")
	 String fechaSalida) throws Exception{
		super();
		java.util.Date d = new SimpleDateFormat("yyyy-MM-dd").parse(fechaLlegada);
		this.fechaLlegada=new Date(d.getTime());
		d = new SimpleDateFormat("yyyy-MM-dd").parse(fechaSalida);
		this.fechaSalida=new Date(d.getTime());;
		this.nombre=nombre;
		this.paisOrigen=paisOrigen;
		this.id=id;
		this.paisOrigen=paisOrigen;
		this.idRepresentante=idRepresentante;
		this.urlPagina=urlPagina;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getPaisOrigen() {
		return paisOrigen;
	}

	public void setPaisOrigen(String paisOrigen) {
		this.paisOrigen = paisOrigen;
	}

	public String getUrlPagina() {
		return urlPagina;
	}

	public void setUrlPagina(String urlPagina) {
		this.urlPagina = urlPagina;
	}

	public Long getRepresentante() {
		return idRepresentante;
	}

	public void setRepresentante(Long idRepresentante) {
		this.idRepresentante = idRepresentante;
	}

	public Date getFechaLlegada() {
		return fechaLlegada;
	}

	public void setFechaLlegada(Date fechaLlegada) {
		this.fechaLlegada = fechaLlegada;
	}

	public Date getFechaSalida() {
		return fechaSalida;
	}

	public void setFechaSalida(Date fechaSalida) {
		this.fechaSalida = fechaSalida;
	}
	
}
