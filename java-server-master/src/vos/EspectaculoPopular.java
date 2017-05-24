package vos;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class EspectaculoPopular extends Espectaculo{

	public EspectaculoPopular() {
		super();
	}

	public EspectaculoPopular(@JsonProperty(value="id")Long id,@JsonProperty(value="nombre") String nombre,@JsonProperty(value="duracion") int duracion,
			@JsonProperty("idioma") String idioma, @JsonProperty("costo") double costo, @JsonProperty("descripcion") String descripcion, 
			@JsonProperty(value="categorias") List<Categoria> categorias, @JsonProperty(value="companias") List<CompaniaTeatro> companias, 
			@JsonProperty(value="requerimientosTecnicos")List<RequerimientosTecnicos> requerimientosTecnicos) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.duracion = duracion;
		this.idioma = idioma;
		this.costo = costo;
		this.descripcion = descripcion;
	}
}
