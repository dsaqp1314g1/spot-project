package edu.upc.eetac.dsa.dsaqp1314g1.spot.api.model;

import java.util.ArrayList;
import java.util.List;

public class MensajesCollection {
	
	private ArrayList<Mensajes> mensajes;
	

	public MensajesCollection () {
		super();
		mensajes = new ArrayList<Mensajes>();
	}
	public void addMensajes(Mensajes mensaje) {
		mensajes.add(mensaje);
	}
	public ArrayList<Mensajes> getMensajes() {
		return mensajes;
	}
	public void setMensajes(ArrayList<Mensajes> mensajes) {
		this.mensajes = mensajes;
	}
	
	

}
