package edu.upc.eetac.dsa.dsaqp1314g1.spot.api.model;

import java.sql.Date;
import java.util.List;

import javax.ws.rs.core.Link;

import org.glassfish.jersey.linking.Binding;
import org.glassfish.jersey.linking.InjectLink;
import org.glassfish.jersey.linking.InjectLinks;
import org.glassfish.jersey.linking.InjectLink.Style;

import edu.upc.eetac.dsa.dsaqp1314g1.spot.api.MediaType;
import edu.upc.eetac.dsa.dsaqp1314g1.spot.api.UserResource;

public class Comentario {

	@InjectLinks({
		@InjectLink(resource = UserResource.class, style = Style.ABSOLUTE, rel = "abrir-user", title = "abrir-user", method = "getUser", bindings = @Binding(name = "username", value = "${instance.usuario}"), type = MediaType.API_USER)})
	private List<Link> links;
	private int idcomentario;
	private int idspot;
	private String usuario;
	private String comentario;
	private Date fechacreacion;
	
	public List<Link> getLinks() {
		return links;
	}
	public void setLinks(List<Link> links) {
		this.links = links;
	}
	
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
	public Date getFechacreacion() {
		return fechacreacion;
	}
	public void setFechacreacion(Date fechacreacion) {
		this.fechacreacion = fechacreacion;
	}

}
