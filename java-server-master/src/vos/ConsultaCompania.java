package vos;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class ConsultaCompania {	
	
	/**
	 * Nombre de la compañía
	 */
	@JsonProperty(value="id")
	private Long id;
	
	/**
	 * Nombre de la compañía
	 */
	@JsonProperty(value="nombre")
	private String nombre;
	
	/**
	 * Lista de la información de espectáculos
	 */
	@JsonProperty(value="espectaculos")
	private List<CompaniaEspectaculo> espectaculos;
	
	public ConsultaCompania() {
		espectaculos = new ArrayList<CompaniaEspectaculo>();
	}
	
	
	
	public ConsultaCompania(@JsonProperty(value="id")Long id, @JsonProperty(value="nombre")String nombre, 
			@JsonProperty(value="espectaculos")List<CompaniaEspectaculo> espectaculos) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.espectaculos = espectaculos;
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

	public List<CompaniaEspectaculo> getEspectaculos() {
		return espectaculos;
	}

	public void addEspectaculo(CompaniaEspectaculo espectaculo) {
		this.espectaculos.add(espectaculo);
	}
	
}
