package vos;

import org.codehaus.jackson.annotate.JsonProperty;

public class BoletasCompra {
	
	/**
	 * Id de la boleta
	 */
	@JsonProperty(value="id")
	private Long id;
	
	/**
	 * Nombre del espectáculo
	 */
	@JsonProperty(value="nomEspectaculo")
	private String nomEspectaculo;
	
	/**
	 * Fecha de realización de la función
	 */
	@JsonProperty(value="fecha")
	private String fecha;
	
	/**
	 * Nombre del sitio del espectaculo
	 */
	@JsonProperty(value="nomSitio")
	private String nomSitio;
	
	/**
	 * Cantidad de boletas vendidas
	 */
	@JsonProperty(value="vendidas")
	private Integer vendidas;
	
	/**
	 * Cantidad de usuarios registrados
	 */
	@JsonProperty(value="registrados")
	private Integer registrados;
	
	public BoletasCompra() 
	{
		// TODO Auto-generated constructor stub
	}

	public BoletasCompra(@JsonProperty(value="id")Long id, @JsonProperty(value="nomEspectaculo")String nomEspectaculo, 
			@JsonProperty(value="fecha")String fecha, @JsonProperty(value="nomSitio")String nomSitio, 
			@JsonProperty(value="vendidas")Integer vendidas,@JsonProperty(value="registrados")Integer registrados) {
		super();
		this.id = id;
		this.nomEspectaculo = nomEspectaculo;
		this.fecha = fecha;
		this.nomSitio = nomSitio;
		this.vendidas = vendidas;
		this.registrados = registrados;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNomEspectaculo() {
		return nomEspectaculo;
	}

	public void setNomEspectaculo(String nomEspectaculo) {
		this.nomEspectaculo = nomEspectaculo;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getNomSitio() {
		return nomSitio;
	}

	public void setNomSitio(String nomSitio) {
		this.nomSitio = nomSitio;
	}

	public Integer getVendidas() {
		return vendidas;
	}

	public void setVendidas(Integer vendidas) {
		this.vendidas = vendidas;
	}

	public Integer getRegistrados() {
		return registrados;
	}

	public void setRegistrados(Integer registrados) {
		this.registrados = registrados;
	}
	
	
}
