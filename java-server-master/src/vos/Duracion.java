package vos;

import org.codehaus.jackson.annotate.JsonProperty;

import oracle.sql.DATE;

public class Duracion {

	/**
	 * Id 
	 */
	@JsonProperty(value="id")
	private Long id;

	/**
	 * fechaInicio
	 */
	@JsonProperty(value="fechaInicio")
	private DATE fechaInicio;
	
	/**
	 * fechaFin
	 */
	@JsonProperty(value="fechaFin")
	private DATE fechaFin;
	public Duracion() {
		// TODO Auto-generated constructor stub
	}

	public Duracion(@JsonProperty(value="id")Long id, @JsonProperty(value="fechaInicio")DATE fechaInicio,
			@JsonProperty(value="fechaFin")DATE fechaFin) {
		super();
		this.id = id;
		this.fechaInicio = fechaInicio;
		this.fechaFin = fechaFin;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public DATE getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(DATE fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public DATE getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(DATE fechaFin) {
		this.fechaFin = fechaFin;
	}
	
}
