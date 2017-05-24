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

import org.codehaus.jackson.annotate.*;

/**
 * Clase que representa un Video
 * @author Juan
 */
public class Boleta {

	//// Atributos

	/**
	 * Id 
	 */
	@JsonProperty(value="id")
	private Long id;
	
	/**
	 * vendido
	 */
	@JsonProperty(value="vendido")
	private boolean vendido;
	
	/**
	 * fila
	 */
	@JsonProperty(value="fila")
	private Integer fila;
	
	/**
	 * silla
	 */
	@JsonProperty(value="silla")
	private Integer silla;
	
	/**
	 * funcion
	 */
	@JsonProperty(value="funcion")
	private Funcion funcion;
	
	/**
	 * localidad
	 */
	@JsonProperty(value="localidad")
	private Localidad localidad;
	
	/**
	 * usuario
	 */
	@JsonProperty(value="usuario")
	private Usuario usuario;

	
	public Boleta() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * 
	 * @param id
	 * @param precio
	 * @param vendido
	 * @param fila
	 * @param silla
	 * @param funcion
	 * @param localidad
	 */
	public Boleta(@JsonProperty(value="id")Long id, @JsonProperty(value="precio")double precio, 
			@JsonProperty(value="vendido")boolean vendido, @JsonProperty(value="fila")Integer fila, 
			@JsonProperty(value="silla")Integer silla,@JsonProperty(value="funcion")Funcion funcion,
			@JsonProperty(value="localidad")Localidad localidad,@JsonProperty(value="usuario")Usuario usuario) {
		super();
		this.id = id;
		this.vendido = vendido;
		this.fila = fila;
		this.silla = silla;
		this.funcion = funcion;
		this.localidad = localidad;
		this.usuario = usuario;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}



	public boolean isVendido() {
		return vendido;
	}

	public void setVendido(boolean vendido) {
		this.vendido = vendido;
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

	public Funcion getFuncion() {
		return funcion;
	}

	public void setFuncion(Funcion funcion) {
		this.funcion = funcion;
	}

	public Localidad getLocalidad() {
		return localidad;
	}

	public void setLocalidad(Localidad localidad) {
		this.localidad = localidad;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
}
