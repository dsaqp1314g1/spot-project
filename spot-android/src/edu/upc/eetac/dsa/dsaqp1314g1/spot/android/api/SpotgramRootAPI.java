package edu.upc.eetac.dsa.dsaqp1314g1.spot.android.api;

import java.util.HashMap;
import java.util.Map;
 
public class SpotgramRootAPI {
 
	private Map<String, Link> links;
 
	public SpotgramRootAPI() {
		links = new HashMap<>();
	}
 
	public Map<String, Link> getLinks() {
		return links;
	}
 
}