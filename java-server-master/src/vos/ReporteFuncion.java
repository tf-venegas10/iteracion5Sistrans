package vos;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class ReporteFuncion {
	/**
	 * Id 
	 */
	@JsonProperty(value="id")
	private Long id;
	
	@JsonProperty(value="localidadReportes")
	private List<LocalidadReporte> localidadReportes;
	
	@JsonProperty(value="boletas_vendidas")
	private int boletas_vendidas;
	
	@JsonProperty(value="producido")
	private Double producido;
	
	public Double getProducido() {
		return producido;
	}


	public void setProducido(Double producido) {
		this.producido = producido;
	}

	public ReporteFuncion() {
		// TODO Auto-generated constructor stub
	}




	public Long getId() {
		return id;
	}




	public void setId(Long id) {
		this.id = id;
	}




	public List<LocalidadReporte> getLocalidadReportes() {
		return localidadReportes;
	}




	public void setLocalidadReportes(List<LocalidadReporte> localidadReportes) {
		this.localidadReportes = localidadReportes;
	}




	public int getBoletas_vendidas() {
		return boletas_vendidas;
	}




	public void setBoletas_vendidas(int boletas_vendidas) {
		this.boletas_vendidas = boletas_vendidas;
	}
	
	
	
	

}
