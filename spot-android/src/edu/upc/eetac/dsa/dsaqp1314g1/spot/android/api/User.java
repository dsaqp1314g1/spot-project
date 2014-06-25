package edu.upc.eetac.dsa.dsaqp1314g1.spot.android.api;

import java.util.HashMap;
import java.util.Map;

public class User {
	private Map<String, Link> links = new HashMap<String, Link>();
	private String username;
	private String userpass;
	private String name;
	private String email;
	private SpotCollection spotcollection;
	
	public User () {
		super();
	}
	public SpotCollection getSpotcollection() {
		return spotcollection;
	}
	public void setSpotcollection(SpotCollection spotcollection) {
		this.spotcollection = spotcollection;
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
	public Map<String, Link> getLinks() {
		return links;
	}
}
