package vos;

import org.codehaus.jackson.annotate.JsonProperty;

public class AsistenciaFuncion {
	
	/**
	 * ID de la función
	 */
	@JsonProperty(value="id")
	private Long id;
	
	/**
	 * Fecha de realización de la función
	 */
	@JsonProperty(value="fecha")
	private String fecha;
	
	/**
	 * Nombre del espectáculo
	 */
	@JsonProperty(value="espectaculo")
	private String espectaculo;
	
	/**
	 * Duracion del espectáculo
	 */
	@JsonProperty(value="duracion")
	private Integer duracion;
	
	/**
	 * Cantidad de boletas para el espectaculo
	 */
	@JsonProperty(value="cantidadBoletas")
	private Integer cantidadBoletas;
	
	public AsistenciaFuncion() {
		// TODO Auto-generated constructor stub
	}

	public AsistenciaFuncion(@JsonProperty("id")Long id, @JsonProperty("fecha") String fecha, @JsonProperty("espectaculo") String espectaculo,
			@JsonProperty(value="duracion")Integer duracion,@JsonProperty(value="cantidadBoletas")Integer cantidadBoletas) {
		super();
		this.id = id;
		this.fecha = fecha;
		this.espectaculo = espectaculo;
		this.duracion = duracion;
		this.cantidadBoletas = cantidadBoletas;
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

	public String getEspectaculo() {
		return espectaculo;
	}

	public void setEspectaculo(String espectaculo) {
		this.espectaculo = espectaculo;
	}
	
	public Integer getDuracion() {
		return duracion;
	}

	public void setDuracion(Integer duracion) {
		this.duracion = duracion;
	}

	public Integer getCantidadBoletas() {
		return cantidadBoletas;
	}

	public void setCantidadBoletas(Integer cantidadBoletas) {
		this.cantidadBoletas = cantidadBoletas;
	}
	
}
