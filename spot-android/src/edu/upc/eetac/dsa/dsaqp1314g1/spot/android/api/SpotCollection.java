package edu.upc.eetac.dsa.dsaqp1314g1.spot.android.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpotCollection {
	
	private Map<String, Link> links = new HashMap<String, Link>();
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
	public Map<String, Link> getLinks() {
		return links;
	}
}
