package vos;

import org.codehaus.jackson.annotate.JsonProperty;

public class Tupla {
	@JsonProperty(value="funciones")
	private Funcion [] funciones;
	
	@JsonProperty(value="localidades")
	private Localidad[] localidades;
	
	public Tupla(@JsonProperty(value="funciones") Funcion [] funciones,@JsonProperty(value="localidades") Localidad[] localidades) {
		this.funciones=funciones;
		this.localidades=localidades;
	}
	public Tupla() {
		// TODO Auto-generated constructor stub
	}
	public Funcion[] getFunciones() {
		return funciones;
	}
	public void setFunciones(Funcion[] funciones) {
		this.funciones = funciones;
	}
	public Localidad[] getLocalidades() {
		return localidades;
	}
	public void setLocalidades(Localidad[] localidades) {
		this.localidades = localidades;
	}
	
	
}
