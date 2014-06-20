package edu.upc.eetac.dsa.dsaqp1314g1.spot.api.model;

import java.util.List;

import javax.ws.rs.core.Link;

import org.glassfish.jersey.linking.Binding;
import org.glassfish.jersey.linking.InjectLink;
import org.glassfish.jersey.linking.InjectLinks;
import org.glassfish.jersey.linking.InjectLink.Style;

import edu.upc.eetac.dsa.dsaqp1314g1.spot.api.MediaType;
import edu.upc.eetac.dsa.dsaqp1314g1.spot.api.SpotResource;

public class Actualizaciones {
	
	@InjectLinks({
		@InjectLink(resource = SpotResource.class, style = Style.ABSOLUTE, rel = "abrir-spot", title = "abrir-spot", method = "getSpot", bindings = @Binding(name = "idspot", value = "${instance.idspot}"), type = MediaType.API_SPOT)})
	private List<Link> links;
	private int idcomentario;
	private int idspot;
	private String nombrecomentario;
	private String userspot;
	private String usercomentario;
	private String fechacreacion;
	
	public List<Link> getLinks() {
		return links;
	}

	public void setLinks(List<Link> links) {
		this.links = links;
	}
	public int getIdspot() {
		return idspot;
	}
	public void setIdspot(int idspot) {
		this.idspot = idspot;
	}
	public String getNombrecomentario() {
		return nombrecomentario;
	}
	public void setNombrecomentario(String nombrecomentario) {
		this.nombrecomentario = nombrecomentario;
	}
	public int getIdcomentario() {
		return idcomentario;
	}
	public void setIdcomentario(int idcomentario) {
		this.idcomentario = idcomentario;
	}
	public String getUserspot() {
		return userspot;
	}
	public void setUserspot(String userspot) {
		this.userspot = userspot;
	}
	public String getUsercomentario() {
		return usercomentario;
	}
	public void setUsercomentario(String usercomentario) {
		this.usercomentario = usercomentario;
	}
	public String getFechacreacion() {
		return fechacreacion;
	}
	public void setFechacreacion(String fechacreacion) {
		this.fechacreacion = fechacreacion;
	}
	
	

}
