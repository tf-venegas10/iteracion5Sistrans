/**-------------------------------------------------------------------
 * $Id$
 * Universidad de los Andes (Bogot√° - Colombia)
 * Departamento de Ingenier√≠a de Sistemas y Computaci√≥n
 *
 * Materia: Sistemas Transaccionales
 * Ejercicio: VideoAndes
 * Autor: Juan Felipe Garc√≠a - jf.garcia268@uniandes.edu.co
 * -------------------------------------------------------------------
 */
package vos;

import java.sql.Timestamp;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Clase que representa un Video
 * @author Juan
 */
public class BoletaDetail {

	//// Atributos
	
	/**
	 * Id de la boleta
	 */
	@JsonProperty(value="id")
	private Long id;
	
	/**
	 * Fila de la boleta
	 */
	@JsonProperty(value="fila")
	private Integer fila;
	
	/**
	 * Silla de la boleta
	 */
	@JsonProperty(value="silla")
	private Integer silla;
	
	/**
	 * Fecha de realizaciÛn de la funciÛn
	 */
	@JsonProperty(value="fecha")
	private String fecha;
	
	/**
	 * Tipo de la localidad de la boleta
	 */
	@JsonProperty(value="tipoLocalidad")
	private String tipoLocalidad;
	
	/**
	 * Precio de la boleta
	 */
	@JsonProperty(value="precio")
	private Double precio;
	
	/**
	 * Nombre del usuario que compra la boleta
	 */
	@JsonProperty(value="nomUsuario")
	private String nomUsuario;
	
	/**
	 * Nombre del sitio del espectaculo
	 */
	@JsonProperty(value="nomSitio")
	private String nomSitio;
	
	/**
	 * Nombre del espect·culo
	 */
	@JsonProperty(value="nomEspectaculo")
	private String nomEspectaculo;
	
	/**
	 * DuraciÛn del espect·culo
	 */
	@JsonProperty(value="duracion")
	private Integer duracion;
	
	/**
	 * Idioma del espect·culo
	 */
	@JsonProperty(value="idioma")
	private String idioma;

	/**
	 * 
	 * @param id
	 * @param fila
	 * @param silla
	 * @param fecha
	 * @param tipoLocalidad
	 * @param precio
	 * @param nomUsuario
	 * @param nomSitio
	 * @param nomEspectaculo
	 * @param duracion
	 * @param idioma
	 */
	public BoletaDetail(@JsonProperty(value="id")Long id, @JsonProperty(value="fila")Integer fila,
			@JsonProperty(value="silla")Integer silla,@JsonProperty(value="fecha")String fecha,
			@JsonProperty(value="tipoLocalidad")String tipoLocalidad,@JsonProperty(value="precio")Double precio,
			@JsonProperty(value="nomUsuario")String nomUsuario,@JsonProperty(value="nomSitio")String nomSitio,
			@JsonProperty(value="nomEspectaculo")String nomEspectaculo, @JsonProperty(value="duracion")Integer duracion,
			@JsonProperty(value="idioma")String idioma) {
		super();
		this.id = id;
		this.fila = fila;
		this.silla = silla;
		this.fecha = fecha;
		this.tipoLocalidad = tipoLocalidad;
		this.precio = precio;
		this.nomUsuario = nomUsuario;
		this.nomSitio = nomSitio;
		this.nomEspectaculo = nomEspectaculo;
		this.duracion = duracion;
		this.idioma = idioma;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getFila() {
		return fila;
	}

	public void setFila(Integer fila) {
		this.fila = fila;
	}

	public Integer getSilla() {
		return silla;
	}

	public void setSilla(Integer silla) {
		this.silla = silla;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getTipoLocalidad() {
		return tipoLocalidad;
	}

	public void setTipoLocalidad(String tipoLocalidad) {
		this.tipoLocalidad = tipoLocalidad;
	}

	public Double getPrecio() {
		return precio;
	}

	public void setPrecio(Double precio) {
		this.precio = precio;
	}

	public String getNomUsuario() {
		return nomUsuario;
	}

	public void setNomUsuario(String nomUsuario) {
		this.nomUsuario = nomUsuario;
	}

	public String getNomSitio() {
		return nomSitio;
	}

	public void setNomSitio(String nomSitio) {
		this.nomSitio = nomSitio;
	}

	public String getNomEspectaculo() {
		return nomEspectaculo;
	}

	public void setNomEspectaculo(String nomEspectaculo) {
		this.nomEspectaculo = nomEspectaculo;
	}

	public Integer getDuracion() {
		return duracion;
	}

	public void setDuracion(Integer duracion) {
		this.duracion = duracion;
	}

	public String getIdioma() {
		return idioma;
	}

	public void setIdioma(String idioma) {
		this.idioma = idioma;
	}
	
}
