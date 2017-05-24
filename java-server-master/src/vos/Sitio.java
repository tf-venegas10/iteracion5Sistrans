package vos;

import java.sql.Timestamp;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

import oracle.sql.TIMESTAMP;

public class Sitio {
	
////Atributos

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

	/**
	 * Duración en minutos del video
	 */
	@JsonProperty(value="abierto")
	private Boolean abierto;

	/**
	 * Duración en minutos del video
	 */
	@JsonProperty(value="proteccionLluvia")
	private Boolean proteccionLluvia;
	
	/**
	 * Duración en minutos del video
	 */
	@JsonProperty(value="horaApertura")
	private Timestamp horaApertura;
	
	/**
	 * Duración en minutos del video
	 */
	@JsonProperty(value="horaCierre")
	private Timestamp horaCierre;
	
	/**
	 * tipo silleteria
	 */
	@JsonProperty(value="tipoSilleteria")
	private Silleteria tipoSilleteria;
	
	/**
	 * funciones
	 */
	@JsonProperty(value="funciones")
	private List<Funcion> funciones;
	
	/**
	 * aptos
	 */
	@JsonProperty(value="aptos")
	private List<Apto> aptos;
	
	/**
	 * localidades
	 */
	@JsonProperty(value="localidades")
	private List<Localidad> localidades;
	
	/**
	 * requerimientosTecnicos
	 */
	@JsonProperty(value="requerimientosTecnicos")
	private List<RequerimientosTecnicos> requerimientosTecnicos;
	
	public Sitio() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Método constructor de la clase video
	 * <b>post: </b> Crea el video con los valores que entran como parámetro
	 * @param id - Id del video.
	 * @param name - Nombre del video. name != null
	 * @param duration - Duración en minutos del video.
	 */
	public Sitio(@JsonProperty(value="id")Long id, @JsonProperty(value="nombre")String nombre,
			@JsonProperty(value="abierto") Boolean abierto,@JsonProperty(value="proteccionLluvia") Boolean proteccionLluvia,
			@JsonProperty(value="horaApertura") Timestamp horaApertura, @JsonProperty(value="horaCierre") Timestamp horaCierre, 
			@JsonProperty(value="tipoSilleteria")Silleteria tipoSilleteria, @JsonProperty(value="funciones")List<Funcion> funciones,
			@JsonProperty(value="aptos")List<Apto> aptos, @JsonProperty(value="localidades")List<Localidad> localidades,
			@JsonProperty(value="requerimientosTecnicos")List<RequerimientosTecnicos> requerimientosTecnicos) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.abierto = abierto;
		this.proteccionLluvia = proteccionLluvia;
		this.horaApertura = horaApertura;
		this.horaCierre = horaCierre;
		this.tipoSilleteria = tipoSilleteria;
		this.funciones = funciones;
		this.aptos = aptos;
		this.localidades = localidades;
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



	public Boolean getAbierto() {
		return abierto;
	}

	public void setAbierto(Boolean abierto) {
		this.abierto = abierto;
	}

	public Boolean getProteccionLluvia() {
		return proteccionLluvia;
	}

	public void setProteccionLluvia(Boolean proteccionLluvia) {
		this.proteccionLluvia = proteccionLluvia;
	}

	public Timestamp getHoraApertura() {
		return horaApertura;
	}

	public void setHoraApertura(Timestamp horaApertura) {
		this.horaApertura = horaApertura;
	}

	public Timestamp getHoraCierre() {
		return horaCierre;
	}

	public void setHoraCierre(Timestamp horaCierre) {
		this.horaCierre = horaCierre;
	}

	public Silleteria getTipoSilleteria() {
		return tipoSilleteria;
	}

	public void setTipoSilleteria(Silleteria tipoSilleteria) {
		this.tipoSilleteria = tipoSilleteria;
	}

	public void setRequerimientosTecnicos(List<RequerimientosTecnicos> requerimientosTecnicos) {
		this.requerimientosTecnicos = requerimientosTecnicos;
	}

	public List<Funcion> getFunciones() {
		return funciones;
	}

	public void setFunciones(List<Funcion> funciones) {
		this.funciones = funciones;
	}

	public List<Apto> getAptos() {
		return aptos;
	}

	public void setAptos(List<Apto> aptos) {
		this.aptos = aptos;
	}

	public List<Localidad> getLocalidades() {
		return localidades;
	}

	public void setLocalidades(List<Localidad> localidades) {
		this.localidades = localidades;
	}

	
	

}
