package edu.upc.eetac.dsa.dsaqp1314g1.spot.android.api;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class Spot {
	private Map<String, Link> links = new HashMap<>();
	private int idspot;
	private String title;
	private double latitud;
	private double longitud;
	private int megusta;
	private String usuario;
	private String deporte;
	private String ciudad;
	private String fechasubida;
	private String imageURL;
	private List<Comentario> comentario;
	
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getImageURL() {
		return imageURL;
	}

	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}
	
	
	public List<Comentario> getComentario() {
		return comentario;
	}

	public void setComentario(List<Comentario> comentario) {
		this.comentario = comentario;
	}
	public Spot () {
		super();
		comentario = new ArrayList<Comentario>();
	}
	
	public void addComentario(Comentario review) {
		comentario.add(review);
	}
	
	public int getIdspot() {
		return idspot;
	}
	public void setIdspot(int idspot) {
		this.idspot = idspot;
	}
	public double getLatitud() {
		return latitud;
	}
	public void setLatitud(double latitud) {
		this.latitud = latitud;
	}
	public double getLongitud() {
		return longitud;
	}
	public void setLongitud(double longitud) {
		this.longitud = longitud;
	}
	public int getMegusta() {
		return megusta;
	}
	public void setMegusta(int megusta) {
		this.megusta = megusta;
	}
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	public String getDeporte() {
		return deporte;
	}
	public void setDeporte(String deporte) {
		this.deporte = deporte;
	}
	public String getCiudad() {
		return ciudad;
	}
	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}
	public String getFechasubida() {
		return fechasubida;
	}
	public void setFechasubida(String fechasubida) {
		this.fechasubida = fechasubida;
	}
	public Map<String, Link> getLinks() {
		return links;
	}

}
