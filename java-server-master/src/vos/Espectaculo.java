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

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Clase que representa un Video
 * @author Juan
 */
public class Espectaculo {

	//// Atributos

	/**
	 * Id 
	 */
	@JsonProperty(value="id")
	protected Long id;

	/**
	 * Nombre 
	 */
	@JsonProperty(value="nombre")
	protected String nombre;
	

	/**
	 * Duración en minutos 
	 */
	@JsonProperty(value="duracion")
	protected Integer duracion;

	/**
	 * idioma 
	 */
	@JsonProperty(value="idioma")
	protected String idioma;
	
	/**
	 * costo 
	 */
	@JsonProperty(value="costo")
	protected Double costo;
	
	/**
	 * descripcion 
	 */
	@JsonProperty(value="descripcion")
	protected String descripcion;
	
	/**
	 * categorias 
	 */
	@JsonProperty(value="categorias")
	private List<Categoria> categorias;
	
	/**
	 * companias 
	 */
	@JsonProperty(value="companias")
	private List<CompaniaTeatro> companias;
	
	/**
	 * reqerimientosTecnicos 
	 */
	@JsonProperty(value="requerimientosTecnicos")
	private List<RequerimientosTecnicos> requerimientosTecnicos;
	public Espectaculo() {
		// TODO Auto-generated constructor stub
	}
	
	public Espectaculo(@JsonProperty(value="id")Long id,@JsonProperty(value="nombre") String nombre,@JsonProperty(value="duracion") Integer duracion,
	 @JsonProperty("idioma") String idioma, @JsonProperty("costo") double costo, @JsonProperty("descripcion") String descripcion, 
	 @JsonProperty(value="categorias") List<Categoria> categorias, @JsonProperty(value="companias") List<CompaniaTeatro> companias, 
	 @JsonProperty(value="requerimientosTecnicos")List<RequerimientosTecnicos> requerimientosTecnicos) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.duracion = duracion;
		this.idioma = idioma;
		this.costo = costo;
		this.descripcion = descripcion;
		this.categorias = categorias;
		this.companias = companias;
		this.requerimientosTecnicos = requerimientosTecnicos;
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

	public int getDuracion() {
		return duracion;
	}

	public void setDuracion(int duracion) {
		this.duracion = duracion;
	}

	public String getIdioma() {
		return idioma;
	}

	public void setIdioma(String idioma) {
		this.idioma = idioma;
	}

	public Double getCosto() {
		return costo;
	}

	public void setCosto(Double costo) {
		this.costo = costo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public List<Categoria> getCategorias() {
		return categorias;
	}

	public void setCategorias(List<Categoria> categorias) {
		this.categorias = categorias;
	}

	public List<CompaniaTeatro> getCompanias() {
		return companias;
	}

	public void setCompanias(List<CompaniaTeatro> companias) {
		this.companias = companias;
	}

	public List<RequerimientosTecnicos> getRequerimientosTecnicos() {
		return requerimientosTecnicos;
	}

	public void setReqerimientosTecnicos(List<RequerimientosTecnicos> requerimientosTecnicos) {
		this.requerimientosTecnicos = requerimientosTecnicos;
	}

	
}
