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


public class SpotCollection {
	
	@InjectLinks({
		@InjectLink(resource = SpotResource.class, style = Style.ABSOLUTE, rel = "create-spot", title = "Create spot", type = MediaType.API_SPOT),
		@InjectLink(resource = SpotResource.class, style = Style.ABSOLUTE, rel = "spots", title = "spots", type = MediaType.API_SPOT_COLLECTION, method = "getSpotsCollections")})
	private List<Link> links;
	public List<Link> getLinks() {
		return links;
	}
	public void setLinks(List<Link> links) {
		this.links = links;
	}

	private List<Spot> spots;
	public SpotCollection () {
		super();
		spots = new ArrayList<Spot>();
	}
	public void addSpot(Spot spot) {
		spots.add(spot);
	}
	public List<Spot> getSpots() {
		return spots;
	}

	public void setSpots(List<Spot> spots) {
		this.spots = spots;
	}

}
