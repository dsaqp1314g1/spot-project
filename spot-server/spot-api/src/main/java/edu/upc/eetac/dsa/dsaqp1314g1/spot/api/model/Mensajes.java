package edu.upc.eetac.dsa.dsaqp1314g1.spot.api.model;

public class Mensajes {
	
	private int idmensaje;
	private String userTx;
	private String userRx;
	private String mensaje;
	private String fechacreacion;
	
	public int getIdmensaje() {
		return idmensaje;
	}
	public void setIdmensaje(int idmensaje) {
		this.idmensaje = idmensaje;
	}
	public String getUserTx() {
		return userTx;
	}
	public void setUserTx(String userTx) {
		this.userTx = userTx;
	}
	public String getUserRx() {
		return userRx;
	}
	public void setUserRx(String userRx) {
		this.userRx = userRx;
	}
	public String getMensaje() {
		return mensaje;
	}
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
	public String getFechacreacion() {
		return fechacreacion;
	}
	public void setFechacreacion(String fechacreacion) {
		this.fechacreacion = fechacreacion;
	}

}
