package edu.upc.eetac.dsa.dsaqp1314g1.spot.android.api;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Properties;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
 


import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.util.Log;
 
public class SpotgramAPI {
	private final static String TAG = SpotgramAPI.class.getName();
	private static SpotgramAPI instance = null;
	private URL url;
 
	private SpotgramRootAPI rootAPI = null;
 
	private SpotgramAPI(Context context) throws IOException,
			SpotgramAndroidException {
		super();
 
		AssetManager assetManager = context.getAssets();
		Properties config = new Properties();
		config.load(assetManager.open("config.properties"));//carga fichero configuracion 
		String serverAddress = config.getProperty("server.address");//obtiene los valores de es fichero
		String serverPort = config.getProperty("server.port");
		url = new URL("http://" + serverAddress + ":" + serverPort
				+ "/spot-api"); //se qeda cn la base url esta si utilizamos hateoas nunca cambia
 
		Log.d("LINKS", url.toString());
		getRootAPI();
	}
 
	public final static SpotgramAPI getInstance(Context context)
			throws SpotgramAndroidException {
		if (instance == null)
			try {
				instance = new SpotgramAPI(context);//context es la actividad, para recuperar valores del fichero conf.
			} catch (IOException e) {
				throw new SpotgramAndroidException(
						"Can't load configuration file");
			}
		return instance;
	}
 
	private void getRootAPI() throws SpotgramAndroidException { 
		Log.d(TAG, "getRootAPI()");
		rootAPI = new SpotgramRootAPI();
		HttpURLConnection urlConnection = null;
		try {
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.setDoInput(true);// true por defecto, significa que qiero leer
			urlConnection.connect();
		} catch (IOException e) {
			throw new SpotgramAndroidException(
					"Can't connect to SPOT API Web Service");
		}
 
		BufferedReader reader;
		try {//lee json que le devuelve htps://localhost:8080/beeterapi
			reader = new BufferedReader(new InputStreamReader(
					urlConnection.getInputStream()));
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
 
			JSONObject jsonObject = new JSONObject(sb.toString());// aparti de un string y objeto json lo convierte
			JSONArray jsonLinks = jsonObject.getJSONArray("links");//asi poder manipular y obtener get, arrays.. 
			parseLinks(jsonLinks, rootAPI.getLinks());//lo proceso con el metodo priado de esta clase y lo guardas en el modelo rootAPI
		} catch (IOException e) {
			throw new SpotgramAndroidException(
					"Can't get response from SPOT API Web Service");
		} catch (JSONException e) {
			throw new SpotgramAndroidException("Error parsing SPOT Root API");
		}
 
	}
 
	public SpotCollection getSpots() throws SpotgramAndroidException {
		Log.d(TAG, "getSpots()");
		SpotCollection Spots = new SpotCollection();
 
		HttpURLConnection urlConnection = null;
		try {
			urlConnection = (HttpURLConnection) new URL(rootAPI.getLinks()
					.get("spots").getTarget()).openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.setDoInput(true);
			urlConnection.connect();
		} catch (IOException e) {
			throw new SpotgramAndroidException(
					"Can't connect to SPOT API Web Service");
		}
 
		BufferedReader reader;
		try {
			reader = new BufferedReader(new InputStreamReader(
					urlConnection.getInputStream()));
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
 
			JSONObject jsonObject = new JSONObject(sb.toString());
			JSONArray jsonLinks = jsonObject.getJSONArray("links");//atributoss
			parseLinks(jsonLinks, Spots.getLinks());
 
			JSONArray jsonSpots = jsonObject.getJSONArray("spots");
			for (int i = 0; i < jsonSpots.length(); i++) {
				Spot Spot = new Spot();//creo un Spot
				JSONObject jsonSpot = jsonSpots.getJSONObject(i);// le doy valor a traves del array y lo añado a la coleccion qe es lo qe lo devuelves
				Spot.setUsuario(jsonSpot.getString("usuario"));
				Spot.setIdspot(jsonSpot.getString("idspot"));//se van añadiendo
				Spot.setTitle(jsonSpot.getString("title"));
				Spot.setDeporte(jsonSpot.getString("deporte"));
				Spot.setCiudad(jsonSpot.getString("ciudad"));
				jsonLinks = jsonSpot.getJSONArray("links");
				parseLinks(jsonLinks, Spot.getLinks());
				Spots.getSpots().add(Spot);
			}
		} catch (IOException e) {
			throw new SpotgramAndroidException(
					"Can't get response from Spot API Web Service");
		} catch (JSONException e) {
			throw new SpotgramAndroidException("Error parsing Spot Root API");
		}
 
		return Spots;
	}
	public SpotCollection getSpots(URL urlSpot) throws SpotgramAndroidException {
		Log.d(TAG, "getSpots()");
		SpotCollection Spots = new SpotCollection();
 
		HttpURLConnection urlConnection = null;
		try {
			//URL url = new URL(urlSpot);
			urlConnection = (HttpURLConnection) urlSpot.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.setDoInput(true);
			urlConnection.connect();
		} catch (IOException e) {
			throw new SpotgramAndroidException(
					"Can't connect to SPOT API Web Service");
		}
 
		BufferedReader reader;
		try {
			reader = new BufferedReader(new InputStreamReader(
					urlConnection.getInputStream()));
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
 
			JSONObject jsonObject = new JSONObject(sb.toString());
			JSONArray jsonLinks = jsonObject.getJSONArray("links");//atributoss
			parseLinks(jsonLinks, Spots.getLinks());
 
			JSONArray jsonSpots = jsonObject.getJSONArray("spots");
			for (int i = 0; i < jsonSpots.length(); i++) {
				Spot Spot = new Spot();//creo un Spot
				JSONObject jsonSpot = jsonSpots.getJSONObject(i);// le doy valor a traves del array y lo añado a la coleccion qe es lo qe lo devuelves
				Spot.setUsuario(jsonSpot.getString("usuario"));
				Spot.setIdspot(jsonSpot.getString("idspot"));//se van añadiendo
				Spot.setTitle(jsonSpot.getString("title"));
				Spot.setDeporte(jsonSpot.getString("deporte"));
				Spot.setCiudad(jsonSpot.getString("ciudad"));
				jsonLinks = jsonSpot.getJSONArray("links");
				parseLinks(jsonLinks, Spot.getLinks());
				Spots.getSpots().add(Spot);
			}
		} catch (IOException e) {
			throw new SpotgramAndroidException(
					"Can't get response from Spot API Web Service");
		} catch (JSONException e) {
			throw new SpotgramAndroidException("Error parsing Spot Root API");
		}
 
		return Spots;
	}
 
	private void parseLinks(JSONArray jsonLinks, Map<String, Link> map)
			throws SpotgramAndroidException, JSONException {
		for (int i = 0; i < jsonLinks.length(); i++) {
			Link link = SimpleLinkHeaderParser
					.parseLink(jsonLinks.getString(i));
			String rel = link.getParameters().get("rel");//tb podria obteet el title i el target(?) pRA QITARME LOS ESPACIOS BLANCOS DE ENCIAM
			String rels[] = rel.split("\\s");
			for (String s : rels)
				map.put(s, link);
		}
	}
	
	public Spot getSpot(URL urlSpot) throws SpotgramAndroidException {
		Spot Spot = new Spot();
	 
		HttpURLConnection urlConnection = null;
		try {
			//URL url = new URL(urlSpot);
			urlConnection = (HttpURLConnection) urlSpot.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.setDoInput(true);
			urlConnection.connect();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					urlConnection.getInputStream()));
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
			JSONObject jsonSpot = new JSONObject(sb.toString()); //revuperado json, i procesado json
			Spot.setUsuario(jsonSpot.getString("usuario"));
			Spot.setIdspot(jsonSpot.getString("idspot"));
			Spot.setTitle(jsonSpot.getString("title"));
			Spot.setCiudad(jsonSpot.getString("ciudad"));
			Spot.setDeporte(jsonSpot.getString("deporte"));
			JSONArray jsonLinks = jsonSpot.getJSONArray("links");
			parseLinks(jsonLinks, Spot.getLinks());
		} catch (MalformedURLException e) {
			Log.e(TAG, e.getMessage(), e);
			throw new SpotgramAndroidException("Bad Spot url");
		} catch (IOException e) {
			Log.e(TAG, e.getMessage(), e);
			throw new SpotgramAndroidException("Exception when getting the Spot");
		} catch (JSONException e) {
			Log.e(TAG, e.getMessage(), e);
			throw new SpotgramAndroidException("Exception parsing response");
		}
	 
		return Spot;
	}
	public Spot createSpot(String usuario, String lat, String lon, String ciudad, String deporte, String title, String urlSpot, Bitmap bitmap)
			throws SpotgramAndroidException {
		URL urls = null;
		try {
			urls = new URL(urlSpot);
		} catch (MalformedURLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		Log.d(TAG, "createSpot()");
		Spot spot = new Spot();
		spot.setDeporte(deporte);
		spot.setLatitud(Double.parseDouble(lat));
		spot.setLongitud(Double.parseDouble(lon));
		spot.setTitle(title);
		spot.setCiudad(ciudad);
		spot.setUsuario(usuario);
		Log.d(TAG, usuario);
		Log.d(TAG, ciudad);
		HttpURLConnection urlConnection = null;
		try {			
			JSONObject jsonSpot = createJsonSpot(spot);
			URL urlPostSpots = urls;
			urlConnection = (HttpURLConnection) urlPostSpots.openConnection();
			urlConnection.setRequestProperty("Accept",
					MediaType.API_SPOT);
			urlConnection.setRequestProperty("Content-Type",
					MediaType.API_SPOT);
			urlConnection.setRequestMethod("POST");
			urlConnection.setDoInput(true);
			urlConnection.setDoOutput(true);
			urlConnection.connect();
			PrintWriter writer = new PrintWriter(
					urlConnection.getOutputStream());
			 ByteArrayOutputStream bao = new ByteArrayOutputStream();
			    bitmap.compress(Bitmap.CompressFormat.PNG, 30, bao);
			    byte[] data = bao.toByteArray();
			writer.println(jsonSpot.toString());
			writer.close();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					urlConnection.getInputStream()));
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
			jsonSpot = new JSONObject(sb.toString());

			spot.setUsuario(jsonSpot.getString("usuario"));
			spot.setLatitud(jsonSpot.getDouble("latitud"));
			spot.setLongitud(jsonSpot.getDouble("longitud"));
			spot.setDeporte(jsonSpot.getString("deporte"));
			spot.setCiudad(jsonSpot.getString("ciudad"));
			spot.setTitle(jsonSpot.getString("title"));
		} catch (JSONException e) {
			Log.e(TAG, e.getMessage(), e);
			throw new SpotgramAndroidException("Error parsing response");
		} catch (IOException e) {
			Log.e(TAG, e.getMessage(), e);
			throw new SpotgramAndroidException("Error getting response");
		} finally {
			if (urlConnection != null)
				urlConnection.disconnect();
		}
		return spot;
	}
	//writeSpot activity progrso void i Spot tipo de retorno Spots..params (aqi esta tanto el subject como el content) 
	//onpostexecute recarga lista con todos los Spots inclusive el nuevo k hemos creado
	//oncreate carga layout
	//dosmetodos postSpot y cancel
	//finish acaba actividad y vuelve a la anterior en este caso a la lista de Spots , mostrat actividad tal i como estaba
	//en el showSpots parecido al finish pero con la lista actualizada
	//post obtenemos  do input leemos, doutpu envamios, createSpotjson, atraves de el metodo pivado, se crea json object i se van colocando valores 
	private JSONObject createJsonSpot(Spot Spot) throws JSONException {
		JSONObject jsonSpot = new JSONObject();
		jsonSpot.put("title", Spot.getTitle());
		jsonSpot.put("deporte", Spot.getDeporte());
		jsonSpot.put("latitud", Spot.getLatitud());
		jsonSpot.put("longitud", Spot.getLongitud());
		jsonSpot.put("usuario", Spot.getUsuario());
		jsonSpot.put("ciudad", Spot.getCiudad());
	 
		return jsonSpot;
	}
}