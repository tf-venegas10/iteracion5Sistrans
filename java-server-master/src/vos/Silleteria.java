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
 *
 * @author Tomas Venegas
 */
public class Silleteria {

	//// Atributos

	/**
	 * Id del video
	 */
	@JsonProperty(value="id")
	private Long id;

	/**
	 * Nombre del video
	 */
	@JsonProperty(value="nombre")
	private String nombre;

	
	public Silleteria(@JsonProperty(value="id")long id, @JsonProperty(value="nombre")String nombre) {
		super();
		this.id = id;
		this.nombre = nombre;
	}


	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}


	public String getNombre() {
		return nombre;
	}


	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public Silleteria() {
		// TODO Auto-generated constructor stub
	}
	



}
