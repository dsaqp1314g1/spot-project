package edu.upc.eetac.dsa.dsaqp1314g1.spot.android.api;

public class Deportes {

	//Variables del objecte
	int id;
	String nom;
	
	//Creacio de l'objecte
	public Deportes()
	{
		super();
		this.id = 0;
		this.nom = "";
	}
	//Set-get Nom
	public String getNom()
	{
		return nom;
	}
	public void setNom(String n)
	{
		nom = n;
	}
	//Set-get ID
	public int getID()
	{
		return id;
	}
	public void setID(int i)
	{
		id = i;
	}
}
