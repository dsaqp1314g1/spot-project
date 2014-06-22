package edu.upc.eetac.dsa.dsaqp1314g1.spot.api.model;

import java.util.ArrayList;
import java.util.List;

public class ActuMegustaCollection {
	
	private List<Megusta> megusta;
	public ActuMegustaCollection () {
		super();
		megusta = new ArrayList<Megusta>();
	}
	public List<Megusta> getActualizacion() {
		return megusta;
	}
	public void setActualizacion(List<Megusta> actualizacion) {
		this.megusta = actualizacion;
	}
	
	public void addActualizacion(Megusta act) {
		megusta.add(act);
	}
}
