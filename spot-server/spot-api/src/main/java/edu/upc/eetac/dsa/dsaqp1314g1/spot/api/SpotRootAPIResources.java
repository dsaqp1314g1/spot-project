package edu.upc.eetac.dsa.dsaqp1314g1.spot.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import edu.upc.eetac.dsa.dsaqp1314g1.spot.api.model.SpotRootAPI;

@Path("/")
public class SpotRootAPIResources {
	
	@GET
	public SpotRootAPI getRootAPI() {
		SpotRootAPI api = new SpotRootAPI();
		System.out.println("SpotRootApi hecho");
		return api;
	}

}
