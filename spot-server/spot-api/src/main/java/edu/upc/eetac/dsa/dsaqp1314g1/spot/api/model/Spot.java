package edu.upc.eetac.dsa.dsaqp1314g1.spot.api.model;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Link;

import org.glassfish.jersey.linking.Binding;
import org.glassfish.jersey.linking.InjectLink;
import org.glassfish.jersey.linking.InjectLinks;
import org.glassfish.jersey.linking.InjectLink.Style;

import edu.upc.eetac.dsa.dsaqp1314g1.spot.api.MediaType;
import edu.upc.eetac.dsa.dsaqp1314g1.spot.api.SpotResource;
import edu.upc.eetac.dsa.dsaqp1314g1.spot.api.UserResource;

public class Spot {

	@InjectLinks({
		@InjectLink(resource = SpotResource.class, style = Style.ABSOLUTE, rel = "abrir-spot", title = "abrir-spot", method = "getSpot", bindings = @Binding(name = "idspot", value = "${instance.idspot}"), type = MediaType.API_SPOT),
		@InjectLink(resource = UserResource.class, style = Style.ABSOLUTE, rel = "abrir-user", title = "abrir-user", method = "getUser", bindings = @Binding(name = "username", value = "${instance.usuario}"), type = MediaType.API_USER)})
	private List<Link> links;
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
	private List<BotonMegusta> botonmegusta;
	
	public List<BotonMegusta> getBotonmegusta() {
		return botonmegusta;
	}

	public void setBotonmegusta(List<BotonMegusta> botonmegusta) {
		this.botonmegusta = botonmegusta;
	}

	public List<Link> getLinks() {
		return links;
	}

	public void setLinks(List<Link> links) {
		this.links = links;
	}

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
		botonmegusta = new ArrayList<BotonMegusta>();
	}
	
	public void addComentario(Comentario review) {
		comentario.add(review);
	}
	public void addBotonMegusta(BotonMegusta bmegus) {
		botonmegusta.add(bmegus);
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
	
	

}
