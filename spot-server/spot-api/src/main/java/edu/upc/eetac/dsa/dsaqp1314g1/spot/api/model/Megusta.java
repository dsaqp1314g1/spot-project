package edu.upc.eetac.dsa.dsaqp1314g1.spot.api.model;

import java.util.List;

import javax.ws.rs.core.Link;

import org.glassfish.jersey.linking.Binding;
import org.glassfish.jersey.linking.InjectLink;
import org.glassfish.jersey.linking.InjectLinks;
import org.glassfish.jersey.linking.InjectLink.Style;

import edu.upc.eetac.dsa.dsaqp1314g1.spot.api.MediaType;
import edu.upc.eetac.dsa.dsaqp1314g1.spot.api.SpotResource;

public class Megusta {
	@InjectLinks({
		@InjectLink(resource = SpotResource.class, style = Style.ABSOLUTE, rel = "abrir-spot", title = "abrir-spot", method = "getSpot", bindings = @Binding(name = "idspot", value = "${instance.idspot}"), type = MediaType.API_SPOT)})
	private List<Link> links;
	private int idspot;
	private String nombrespot;
	private String estado;
	private String userspot;
	private String usermegusta;
	private String fechacreacion;
	
	public List<Link> getLinks() {
		return links;
	}
	public void setLinks(List<Link> links) {
		this.links = links;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public int getIdspot() {
		return idspot;
	}
	public void setIdspot(int idspot) {
		this.idspot = idspot;
	}
	public String getNombrespot() {
		return nombrespot;
	}
	public void setNombrespot(String nombrespot) {
		this.nombrespot = nombrespot;
	}
	public String getUserspot() {
		return userspot;
	}
	public void setUserspot(String userspot) {
		this.userspot = userspot;
	}
	public String getUsermegusta() {
		return usermegusta;
	}
	public void setUsermegusta(String usermegusta) {
		this.usermegusta = usermegusta;
	}
	public String getFechacreacion() {
		return fechacreacion;
	}
	public void setFechacreacion(String fechacreacion) {
		this.fechacreacion = fechacreacion;
	}
}
