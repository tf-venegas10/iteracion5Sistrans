package vos;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class CompaniaEspectaculo {

	/**
	 * ID del espectaculo
	 */
	@JsonProperty(value = "id")
	private Long id;

	/**
	 * Nombre del espectaculo
	 */
	@JsonProperty(value = "nombre")
	private String nombre;

	/**
	 * Asistencia total del espect�culo
	 */
	@JsonProperty(value = "asistTotal")
	private Integer asistTotal;

	/**
	 * Asistencia de usuarios registrados
	 */
	@JsonProperty(value = "asistRegistrados")
	private Integer asistRegistrados;

	/**
	 * Dinero recaudado
	 */
	@JsonProperty(value = "dineroRecaudado")
	private Double dineroRecaudado;

	/**
	 * Lista de la informaci�n de sitios
	 */
	@JsonProperty(value = "sitios")
	private List<CompaniaSitio> sitios;

	public CompaniaEspectaculo() {
		// TODO Auto-generated constructor stub
		this.sitios = new ArrayList<CompaniaSitio>();
	}

	public CompaniaEspectaculo(@JsonProperty(value = "id") Long id, @JsonProperty(value = "nombre") String nombre,
			@JsonProperty(value = "asistTotal") Integer asistTotal,
			@JsonProperty(value = "asistRegistrados") Integer asistRegistrados,
			@JsonProperty(value = "dineroRecaudado") Double dineroRecaudado) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.asistTotal = asistTotal;
		this.asistRegistrados = asistRegistrados;
		this.dineroRecaudado = dineroRecaudado;
		this.sitios = new ArrayList<CompaniaSitio>();
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

	public Integer getAsistTotal() {
		return asistTotal;
	}

	public void setAsistTotal(Integer asistTotal) {
		this.asistTotal = asistTotal;
	}

	public Integer getAsistRegistrados() {
		return asistRegistrados;
	}

	public void setAsistRegistrados(Integer asistRegistrados) {
		this.asistRegistrados = asistRegistrados;
	}

	public Double getDineroRecaudado() {
		return dineroRecaudado;
	}

	public void setDineroRecaudado(Double dineroRecaudado) {
		this.dineroRecaudado = dineroRecaudado;
	}

	public List<CompaniaSitio> getSitios() {
		return sitios;
	}

	public void addSitio(CompaniaSitio sitio) {
		this.sitios.add(sitio);
	}
}
