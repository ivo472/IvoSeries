package br.ivo.seriessearch.database;

public class InfoSeries {
	private int Id = 0;
	private String Titulo;
	private int status;
	private String information;
		
	public static final int STATUS_UNDEFINED = 0;
	public static final int STATUS_CHECK = 1;
	public static final int STATUS_INFO = 2;
	
	public int getId() {
		return Id;
	}
	public void setId(int id) {
		Id = id;
	}
	public String getTitulo() {
		return Titulo;
	}
	public void setTitulo(String titulo) {
		Titulo = titulo;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
}
