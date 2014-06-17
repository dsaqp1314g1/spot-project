package edu.upc.eetac.dsa.dsaqp1314g1.spot.api;


import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import edu.upc.eetac.dsa.dsaqp1314g1.spot.api.model.SpotError;

@SuppressWarnings("serial")
public class NotAllowedException extends WebApplicationException {
	private final static String MESSAGE = "You are not allowed";

	public NotAllowedException() {
		super(Response
				.status(Response.Status.FORBIDDEN)
				.entity(new SpotError(Response.Status.FORBIDDEN
						.getStatusCode(), MESSAGE))
				.type(MediaType.API_ERROR).build());
	}

}