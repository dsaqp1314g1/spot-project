package edu.upc.eetac.dsa.dsaqp1314g1.spot.android;

import java.io.IOException;
import java.net.Authenticator;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.util.Properties;

import edu.upc.eetac.dsa.dsaqp1314g1.spot.android.api.Spot;
import edu.upc.eetac.dsa.dsaqp1314g1.spot.android.api.SpotCollection;
import edu.upc.eetac.dsa.dsaqp1314g1.spot.android.api.SpotgramAPI;
import edu.upc.eetac.dsa.dsaqp1314g1.spot.android.api.SpotgramAndroidException;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class SpotCreateActivity extends Activity{
	 /** Called when the activity is first created. */
    EditText ciudad;
	EditText deporte;
	String city;
	String sport;
	String title;
	String usuario="juan";
	EditText titulo;
	String serverAddress;
	String serverPort;
	String url;
	private final static String TAG = SpotCreateActivity.class.toString();
	
	private class PullSpotsTask extends
	AsyncTask<String, Void, Spot> {
		private ProgressDialog pd;

		@Override
		protected Spot doInBackground(String... params) {
			Spot Spot = null;
			try {
				Spot = SpotgramAPI.getInstance(SpotCreateActivity.this)
						.createSpot(params[0], params[1], params[2], params[3], params[4]);
			} catch (SpotgramAndroidException e) {
				e.printStackTrace();
			}
			return Spot;
		}

		@Override
		protected void onPostExecute(Spot result) {
			finish();
			if (pd != null) {
				pd.dismiss();
			}
		}

		@Override
		protected void onPreExecute() {
			pd = new ProgressDialog(SpotCreateActivity.this);
			pd.setTitle("Searching...");
			pd.setCancelable(false);
			pd.setIndeterminate(true);
			pd.show();
		}

}
	
	@Override//getextras qe se los pasa el mainactivity
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create);
		ciudad = (EditText) findViewById(R.id.textCiudad);
		deporte = (EditText) findViewById(R.id.textDeporte);
		titulo = (EditText) findViewById(R.id.textTitulo);	
		SharedPreferences prefs = getSharedPreferences("spot-profile",
				Context.MODE_PRIVATE);
		final String username = prefs.getString("username", null);
		final String password = prefs.getString("password", null);
	 
		Authenticator.setDefault(new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password
						.toCharArray());
			}
		});
		Log.d(TAG, "authenticated with " + username + ":" + password);
		AssetManager assetManager = getAssets();
    	Properties config = new Properties();
    	try {
    		config.load(assetManager.open("config.properties"));
    		serverAddress = config.getProperty("server.address");
    		serverPort = config.getProperty("server.port");
    		Log.d(TAG, "Configured server " + serverAddress + ":" + serverPort);
    		url = "http://" + serverAddress + ":" + serverPort + "/spot-api/spots/";
    	} catch (IOException e) {
    		Log.e(TAG, e.getMessage(), e);
    		finish();
    	}
//		String urlSpot = (String) getIntent().getExtras().get("url");
//		(new FetchSpotTask()).execute(urlSpot);
	}
	public void sendServer(View v) {

			city = ciudad.getText().toString();
			sport = deporte.getText().toString();
			title = titulo.getText().toString();			
		
		(new PullSpotsTask()).execute(city, sport, title,usuario,url);
	}
    
}
