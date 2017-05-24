package vos;

import org.codehaus.jackson.annotate.JsonProperty;

public class CompaniaSitio {

	/**
	 * ID del espectaculo
	 */
	@JsonProperty(value="id")
	private Long id;
	
	/**
	 * Nombre del sitio
	 */
	@JsonProperty(value="nombre")
	private String nombre;
	
	/**
	 * Ocupación del sitio
	 */
	@JsonProperty(value="ocupacion")
	private Double ocupacion;
	
	
	public CompaniaSitio() {
		// TODO Auto-generated constructor stub
	}
	
	
	public CompaniaSitio(@JsonProperty(value="id")Long id,@JsonProperty(value="nombre")String nombre, @JsonProperty(value="ocupacion")Double ocupacion) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.ocupacion = ocupacion;
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

	public Double getOcupacion() {
		return ocupacion;
	}

	public void setOcupacion(Double ocupacion) {
		this.ocupacion = ocupacion;
	}
}
