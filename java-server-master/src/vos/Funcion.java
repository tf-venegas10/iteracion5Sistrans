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

import java.sql.Timestamp;
import java.util.List;

import org.codehaus.jackson.annotate.*;

import oracle.sql.TIMESTAMP;

/**
 * Clase que representa un Video
 * @author Juan
 */
public class Funcion {

	//// Atributos

	/**
	 * Id 
	 */
	@JsonProperty(value="id")
	private Long id;

	/**
	 * fecha
	 */
	@JsonProperty(value="fecha")
	private String fecha;

	/**
	 * espectaculo
	 */
	@JsonProperty(value="espectaculo")
	private Espectaculo espectaculo;

	/**
	 * realizado
	 */
	@JsonProperty(value="realizado")
	private boolean realizado;

	/**
	 * boletas
	 */
	@JsonProperty(value="boletas")
	private List<Boleta> boletas;

	/**
	 * sitio
	 */
	@JsonProperty(value="sitio")
	private Sitio sitio;


	public Sitio getSitio() {
		return sitio;
	}

	public void setSitio(Sitio sitio) {
		this.sitio = sitio;
	}

	public List<Boleta> getBoletas() {
		return boletas;
	}

	public void setBoletas(List<Boleta> boletas) {
		this.boletas = boletas;
	}

	public Funcion(@JsonProperty(value="id")Long id,@JsonProperty(value="fecha")String fecha,
			@JsonProperty(value="realizado")boolean realizado,@JsonProperty(value="espectaculo")Espectaculo espectaculo, @JsonProperty(value="sitio")
	Sitio sitio ) {
		super();
		this.id=id;
		this.fecha=(fecha);
		this.realizado=realizado;
		this.espectaculo=espectaculo;
		this.sitio=sitio;
	}

	public Funcion() {
		// TODO Auto-generated constructor stub
	}
	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getFecha() {
		return fecha;
	}


	public void setFecha(String fecha) {
		this.fecha = fecha;
	}


	public boolean isRealizado() {
		return realizado;
	}


	public void setRealizado(boolean realizado) {
		this.realizado = realizado;
	}


	public Espectaculo getEspectaculo() {
		return espectaculo;
	}


	public void setEspectaculo(Espectaculo espectaculo) {
		this.espectaculo = espectaculo;
	}


}
