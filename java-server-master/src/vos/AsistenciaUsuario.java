package vos;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class AsistenciaUsuario {

	/**
	 * Id del video
	 */
	@JsonProperty(value="id")
	private Long id;
	
	/**
	 * Nombre del usuario
	 */
	@JsonProperty(value="nombre")
	private String nombre;
	
	/**
	 * Correo electrónico del usuario
	 */
	@JsonProperty(value="correoElectronico")
	private String correoElectronico;
	
	/**
	 * Funciones realizadas asistidas por el usuario
	 */
	@JsonProperty(value="realizadas")
	private List<AsistenciaFuncion> realizadas;
	
	/**
	 * Funciones enCurso asistidas por el usuario
	 */
	@JsonProperty(value="enCurso")
	private List<AsistenciaFuncion> enCurso;
	
	/**
	 * Funciones previstas por ser asistidas por el usuario
	 */
	@JsonProperty(value="previstas")
	private List<AsistenciaFuncion> previstas;
	
	/**
	 * Boletas de funciones devueltas por el usuario
	 */
	@JsonProperty(value="devueltas")
	private List<AsistenciaFuncion> devueltas;
	
	public AsistenciaUsuario() {
		// TODO Auto-generated constructor stub
		this.realizadas = new ArrayList<AsistenciaFuncion>();
		this.enCurso = new ArrayList<AsistenciaFuncion>();
		this.previstas = new ArrayList<AsistenciaFuncion>();
		this.devueltas = new ArrayList<AsistenciaFuncion>();
	}

	public AsistenciaUsuario(@JsonProperty("id")Long id, @JsonProperty("nombre")String nombre, @JsonProperty("correoElectronico")String correoElectronico,
			@JsonProperty(value="realizadas")List<AsistenciaFuncion> realizadas,@JsonProperty(value="enCurso")List<AsistenciaFuncion> enCurso,
			@JsonProperty(value="previstas")List<AsistenciaFuncion> previstas,@JsonProperty(value="devueltas")List<AsistenciaFuncion> devueltas) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.correoElectronico = correoElectronico;
		this.realizadas = realizadas;
		this.enCurso = enCurso;
		this.previstas = previstas;
		this.devueltas = devueltas;
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

	public List<AsistenciaFuncion> getRealizadas() {
		return realizadas;
	}

	public void addRealizadas(AsistenciaFuncion realizadas) {
		this.realizadas.add(realizadas);
	}

	public List<AsistenciaFuncion> getEnCurso() {
		return enCurso;
	}

	public void addEnCurso(AsistenciaFuncion enCurso) {
		this.enCurso.add(enCurso);
	}

	public List<AsistenciaFuncion> getPrevistas() {
		return previstas;
	}

	public void addPrevistas(AsistenciaFuncion previstas) {
		this.previstas.add(previstas);
	}

	public List<AsistenciaFuncion> getDevueltas() {
		return devueltas;
	}

	public void addDevueltas(AsistenciaFuncion devueltas) {
		this.devueltas.add(devueltas);
	}
	
}
