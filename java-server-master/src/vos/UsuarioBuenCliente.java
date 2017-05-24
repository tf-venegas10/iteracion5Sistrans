package vos;

import java.sql.SQLException;

import org.codehaus.jackson.annotate.JsonProperty;

public class UsuarioBuenCliente extends UsuarioSimple{

	/**
	 * Id del video
	 */
	@JsonProperty(value="cantidadBoletas")
	private Integer cantidadBoletas;
	
	public UsuarioBuenCliente(){
		
	}
	
	public UsuarioBuenCliente(@JsonProperty(value="id")Long id, @JsonProperty(value="nombre")String nombre, 
			@JsonProperty(value="correoElectronico")String correoElectronico, @JsonProperty(value="fechaNacimiento")String fechaNacimiento,
			@JsonProperty(value="rol")String rol,@JsonProperty(value="cantidadBoletas")Integer cantidadBoletas) throws SQLException,Exception {
		super(id, nombre, correoElectronico, fechaNacimiento, rol);
		this.cantidadBoletas = cantidadBoletas;
	}

	public Integer getCantidadBoletas() {
		return cantidadBoletas;
	}

	public void setCantidadBoletas(Integer cantidadBoletas) {
		this.cantidadBoletas = cantidadBoletas;
	}
	
}
