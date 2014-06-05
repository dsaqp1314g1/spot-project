package edu.upc.eetac.dsa.dsaqp1314g1.spot.api.model;

import java.util.List;

import javax.ws.rs.core.Link;

import org.glassfish.jersey.linking.InjectLink;
import org.glassfish.jersey.linking.InjectLinks;
import org.glassfish.jersey.linking.InjectLink.Style;

import edu.upc.eetac.dsa.dsaqp1314g1.spot.api.MediaType;
import edu.upc.eetac.dsa.dsaqp1314g1.spot.api.SpotResource;
import edu.upc.eetac.dsa.dsaqp1314g1.spot.api.SpotRootAPIResources;
import edu.upc.eetac.dsa.dsaqp1314g1.spot.api.UserResource;

public class SpotRootAPI {
	
	@InjectLinks({
		@InjectLink(resource = SpotRootAPIResources.class, style = Style.ABSOLUTE, rel = "self bookmark home", title = "Spot Root API", method = "getRootAPI"),
		@InjectLink(resource = SpotResource.class, style = Style.ABSOLUTE, rel = "spots", title = "spots", type = MediaType.API_SPOT_COLLECTION, method = "getSpotsCollections")})
private List<Link> links;

public List<Link> getLinks() {
	return links;
}

public void setLinks(List<Link> links) {
	this.links = links;
}

}
