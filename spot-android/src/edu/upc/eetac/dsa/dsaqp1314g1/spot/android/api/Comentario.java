package edu.upc.eetac.dsa.dsaqp1314g1.spot.android.api;

import java.sql.Date;


public class Comentario {
	
	private int idcomentario;
	private int idspot;
	private String usuario;
	private String comentario;
	private String fechacreacion;
	
	public int getIdcomentario() {
		return idcomentario;
	}
	public void setIdcomentario(int idcomentario) {
		this.idcomentario = idcomentario;
	}
	public int getIdspot() {
		return idspot;
	}
	public void setIdspot(int idspot) {
		this.idspot = idspot;
	}
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	public String getComentario() {
		return comentario;
	}
	public void setComentario(String comentario) {
		this.comentario = comentario;
	}
	public String getFechacreacion() {
		return fechacreacion;
	}
	public void setFechacreacion(String fechacreacion) {
		this.fechacreacion = fechacreacion;
	}

}
