package vos;

import java.sql.SQLException;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;




public class Usuario extends UsuarioSimple{

	/**
	 * contrasena
	 */
	@JsonProperty(value="contrasena")
	private String contrasena;
	
	/**
	 * preferenciaSitio
	 */
	@JsonProperty(value="boletas")
	private List<Boleta> boletas;
	
	/**
	 * preferenciaCategoria
	 */
	@JsonProperty(value="preferenciaCategoria")
	protected List<Categoria> preferenciaCategoria;
	
	/**
	 * preferenciaSitio
	 */

	@JsonProperty(value="preferenciaSitio")
	protected List<Sitio> preferenciaSitio;
	

	public Usuario(@JsonProperty(value="id")Long id, @JsonProperty(value="nombre")String nombre, 
			@JsonProperty(value="correoElectronico")String correoElectronico, @JsonProperty(value="fechaNacimiento")String fechaNacimiento, 
			@JsonProperty(value="contrasena")String contrasena, @JsonProperty(value="rol")String rol ) throws SQLException,Exception {
		super(id, nombre, correoElectronico, fechaNacimiento, rol);
		this.contrasena = contrasena;
	}

	public Usuario() {
		// TODO Auto-generated constructor stub
	}

	public String getContrasena() {
		return contrasena;
	}

	public void setContrasena(String contrasena) {
		this.contrasena = contrasena;
	}

	public List<Categoria> getPreferenciaCategoria() {
		return preferenciaCategoria;
	}

	public void setPreferenciaCategoria(List<Categoria> preferenciaCategoria) {
		this.preferenciaCategoria = preferenciaCategoria;
	}

	public List<Sitio> getPreferenciaSitio() {
		return preferenciaSitio;
	}

	public void setPreferenciaSitio(List<Sitio> preferenciaSitio) {
		this.preferenciaSitio = preferenciaSitio;
	}

	public List<Boleta> getBoletas() {
		return boletas;
	}

	public void setBoletas(List<Boleta> boletas) {
		this.boletas = boletas;
	}
	
	
}
