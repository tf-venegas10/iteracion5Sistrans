package vos;

import java.sql.Date;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class UsuarioSimple {
	/**
	 * Id del video
	 */
	@JsonProperty(value="id")
	protected Long id;
	
	/**
	 * nombre
	 */
	@JsonProperty(value="nombre")
	protected String nombre;
	
	/**
	 * correoElectronico
	 */
	@JsonProperty(value="correoElectronico")
	protected String correoElectronico;
	
	/**
	 * fehcaNacimiento
	 */
	@JsonProperty(value="fechaNacimiento")
	protected Date fechaNacimiento;
	
	/**
	 * Rol del cliente
	 */
	@JsonProperty(value="rol")
	protected String rol;

	public UsuarioSimple(@JsonProperty(value="id")Long id, @JsonProperty(value="nombre")String nombre, 
			@JsonProperty(value="correoElectronico")String correoElectronico, @JsonProperty(value="fechaNacimiento")String fechaNacimiento,
			@JsonProperty(value="rol")String rol ) throws SQLException,Exception {
		super();
		this.id = id;
		this.nombre = nombre;
		this.correoElectronico = correoElectronico;
		java.util.Date d = new SimpleDateFormat("yyyy-MM-dd").parse(fechaNacimiento);
		this.fechaNacimiento = new Date(d.getTime());
		this.rol = rol;
	}

	public UsuarioSimple() {
		// TODO Auto-generated constructor stub
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

	public String getCorreoElectronico() {
		return correoElectronico;
	}

	public void setCorreoElectronico(String correoElectronico) {
		this.correoElectronico = correoElectronico;
	}

	public Date getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(Date fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	public String getRol() {
		return rol;
	}

	public void setRol(String rol) {
		this.rol = rol;
	}
}
