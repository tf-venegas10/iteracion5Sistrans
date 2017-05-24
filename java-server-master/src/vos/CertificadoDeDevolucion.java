package vos;

import org.codehaus.jackson.annotate.JsonProperty;

public class CertificadoDeDevolucion {
	

	/**
	 * Atributo que hace referencia al id del certificado
	 */
	@JsonProperty(value="id")
	private Long id;
	
	/**
	 * Atributo que hace referencia al valor de devolución
	 */
	@JsonProperty(value="valor")
	private double valor;
	
	/**
	 * Atributo que hacer referencia al nombre del cliente
	 */
	@JsonProperty(value="nomCliente")
	private String nomCliente;
	
	/**
	 * Atributo que hacer referencia al correo del cliente
	 */
	@JsonProperty(value="corrCliente")
	private String corrCliente;

	public CertificadoDeDevolucion() {
		// TODO Auto-generated constructor stub
	}
	
	public CertificadoDeDevolucion(@JsonProperty(value="id")Long id, @JsonProperty(value="valor") Double valor,
			@JsonProperty(value="nomCliente")String nomCliente, @JsonProperty(value="corrCliente")String corrCliente) {
		super();
		this.id = id;
		this.valor = valor;
		this.nomCliente = nomCliente;
		this.corrCliente = corrCliente;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public double getValor() {
		return valor;
	}

	public void setValor(double valor) {
		this.valor = valor;
	}

	public String getNomCliente() {
		return nomCliente;
	}

	public void setNomCliente(String nomCliente) {
		this.nomCliente = nomCliente;
	}

	public String getCorrCliente() {
		return corrCliente;
	}

	public void setCorrCliente(String corrCliente) {
		this.corrCliente = corrCliente;
	}
	
}
