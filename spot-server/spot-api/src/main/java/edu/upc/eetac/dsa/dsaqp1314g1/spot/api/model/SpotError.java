package edu.upc.eetac.dsa.dsaqp1314g1.spot.api.model;

public class SpotError {
	private int status;
	private String message;
 
	public SpotError() {
		super();
	}
 
	public SpotError(int status, String message) {
		super();
		this.status = status;
		this.message = message;
	}
 
	public int getStatus() {
		return status;
	}
 
	public void setStatus(int status) {
		this.status = status;
	}
 
	public String getMessage() {
		return message;
	}
 
	public void setMessage(String message) {
		this.message = message;
	}
}
