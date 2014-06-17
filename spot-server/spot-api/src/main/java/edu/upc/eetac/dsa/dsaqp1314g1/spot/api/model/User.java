package edu.upc.eetac.dsa.dsaqp1314g1.spot.api.model;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Link;

import org.glassfish.jersey.linking.InjectLink;
import org.glassfish.jersey.linking.InjectLinks;
import org.glassfish.jersey.linking.InjectLink.Style;

import edu.upc.eetac.dsa.dsaqp1314g1.spot.api.MediaType;
import edu.upc.eetac.dsa.dsaqp1314g1.spot.api.UserResource;

public class User {
	
	@InjectLinks({
		@InjectLink(resource = UserResource.class, style = Style.ABSOLUTE, rel = "abrir-spots-user", title = "abrir-spot-user", method = "getSpots", type = MediaType.API_SPOT_COLLECTION)})
	private List<Link> links;
	private String username;
	private String userpass;
	private String name;
	private String email;
	private SpotCollection spotcollection;
	private ActualizacionesCollection actualizacionescollection;
	
	public User () {
		super();
	}
	public ActualizacionesCollection getActualizacionescollection() {
		return actualizacionescollection;
	}

	public void setActualizacionescollection(
			ActualizacionesCollection actualizacionescollection) {
		this.actualizacionescollection = actualizacionescollection;
	}

	public SpotCollection getSpotcollection() {
		return spotcollection;
	}
	public void setSpotcollection(SpotCollection spotcollection) {
		this.spotcollection = spotcollection;
	}
	public List<Link> getLinks() {
		return links;
	}
	public void setLinks(List<Link> links) {
		this.links = links;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getUserpass() {
		return userpass;
	}
	public void setUserpass(String userpass) {
		this.userpass = userpass;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
}
