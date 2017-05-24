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

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Clase que representa un Video
 * @author omas VVVVedefgn
 */
public class Localidad {

	//// Atributos

	/**
	 * Id
	 */
	@JsonProperty(value="id")
	private Long id;

	/**
	 * Tipo
	 */
	@JsonProperty(value="tipo")
	private String tipo;

	/**
	 * esNumerada
	 */
	@JsonProperty(value="esNumerada")
	private boolean esNumerada;
	
	/**
	 * numeroDePuestos
	 */
	@JsonProperty(value="numeroDePuestos")
	private int numeroDePuestos;
	
	/**
	 * numeroDePuestos
	 */
	@JsonProperty(value="filas")
	private Integer filas;
	
	/**
	 * sillasPorFila
	 */
	@JsonProperty(value="sillasPorFila")
	private Integer sillasPorFila;

	/**
	 * sillasPorFila
	 
	@JsonProperty(value="boletas")
	private List<Boleta> boletas;
	*/
	/**
	 * precio
	 */
	@JsonProperty(value="precio")
	private double precio;
	
	public Localidad() {
		// TODO Auto-generated constructor stub
	}

	public Localidad(@JsonProperty(value="id")Long id, @JsonProperty(value="tipo")String tipo, 
			@JsonProperty(value="esNumerada")boolean esNumerada, @JsonProperty(value="numeroDePuestos")int numeroDePuestos,
			@JsonProperty(value="filas")Integer filas, @JsonProperty(value="sillasPorFila")Integer sillasPorFila) {
		super();
		this.id = id;
		this.tipo = tipo;
		this.esNumerada = esNumerada;
		this.numeroDePuestos = numeroDePuestos;
		this.filas = filas;
		this.sillasPorFila = sillasPorFila;
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public boolean isEsNumerada() {
		return esNumerada;
	}

	public void setEsNumerada(boolean esNumerada) {
		this.esNumerada = esNumerada;
	}

	public int getNumeroDePuestos() {
		return numeroDePuestos;
	}

	public void setNumeroDePuestos(int numeroDePuestos) {
		this.numeroDePuestos = numeroDePuestos;
	}

	public Integer getFilas() {
		return filas;
	}

	public void setFilas(Integer filas) {
		this.filas = filas;
	}

	public Integer getSillasPorFila() {
		return sillasPorFila;
	}

	public void setSillasPorFila(Integer sillasPorFila) {
		this.sillasPorFila = sillasPorFila;
	}


	public double getPrecio() {
		return precio;
	}

	public void setPrecio(double precio) {
		this.precio = precio;
	}
	
}
