package edu.upc.eetac.dsa.dsaqp1314g1.spot.api.model;

import java.util.ArrayList;
import java.util.List;

public class ActualizacionesCollection {
	
	private List<Actualizaciones> actualizacion;
	public ActualizacionesCollection () {
		super();
		actualizacion = new ArrayList<Actualizaciones>();
	}
	public List<Actualizaciones> getActualizacion() {
		return actualizacion;
	}
	public void setActualizacion(List<Actualizaciones> actualizacion) {
		this.actualizacion = actualizacion;
	}
	
	public void addActualizacion(Actualizaciones act) {
		actualizacion.add(act);
	}
}
