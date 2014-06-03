package edu.upc.eetac.dsa.dsaqp1314g1.spot.api.model;

import java.util.ArrayList;
import java.util.List;

public class SpotCollection {
	
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
