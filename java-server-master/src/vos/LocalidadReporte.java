package vos;

import org.codehaus.jackson.annotate.JsonProperty;

public class LocalidadReporte {

	/**
	 * tipo 
	 */
	@JsonProperty(value="tipo")
	private String tipo;
	
	
	/**
	 * boletas vendidas por localidad 
	 */
	@JsonProperty(value="boletas_vendidas_localidad")
	private int boletas_vendidas_localidad;
	
	@JsonProperty(value="boletas_vendidas_registrados")
	private int boletas_vendidas_registrados;
	
	@JsonProperty(value="producido_registrados")
	private Double producido_registrados;
	
	@JsonProperty(value="boletas_vendidas_no_registrados")
	private int boletas_vendidas_no_registrados;
	
	@JsonProperty(value="producido_no_registrados")
	private Double producido_no_registrados;

	@JsonProperty(value="producido_localidad")
	private Double producido_localidad;
	
public LocalidadReporte() {
	// TODO Auto-generated constructor stub
}


public String getTipo() {
	return tipo;
}


public void setTipo(String tipo) {
	this.tipo = tipo;
}



public Double getProducido_localidad() {
	return producido_localidad;
}


public void setProducido_localidad(Double producido_localidad) {
	this.producido_localidad = producido_localidad;
}


public int getBoletas_vendidas_localidad() {
	return boletas_vendidas_localidad;
}


public void setBoletas_vendidas_localidad(int boletas_vendidas_localidad) {
	this.boletas_vendidas_localidad = boletas_vendidas_localidad;
}




public int getBoletas_vendidas_registrados() {
	return boletas_vendidas_registrados;
}


public void setBoletas_vendidas_registrados(int boletas_vendidas_registrados) {
	this.boletas_vendidas_registrados = boletas_vendidas_registrados;
}


public Double getProducido_registrados() {
	return producido_registrados;
}


public void setProducido_registrados(Double producido_registrados) {
	this.producido_registrados = producido_registrados;
}


public int getBoletas_vendidas_no_registrados() {
	return boletas_vendidas_no_registrados;
}


public void setBoletas_vendidas_no_registrados(int boletas_vendidas_no_registrados) {
	this.boletas_vendidas_no_registrados = boletas_vendidas_no_registrados;
}


public Double getProducido_no_registrados() {
	return producido_no_registrados;
}


public void setProducido_no_registrados(Double producido_no_registrados) {
	this.producido_no_registrados = producido_no_registrados;
}

}
