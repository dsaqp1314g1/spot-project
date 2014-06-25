package edu.upc.eetac.dsa.dsaqp1314g1.spot.android;

import java.io.IOException;
import java.net.Authenticator;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.util.ArrayList;
import java.util.Properties;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import edu.upc.eetac.dsa.dsaqp1314g1.spot.android.api.Spot;
import edu.upc.eetac.dsa.dsaqp1314g1.spot.android.api.SpotCollection;
import edu.upc.eetac.dsa.dsaqp1314g1.spot.android.api.SpotgramAPI;
import edu.upc.eetac.dsa.dsaqp1314g1.spot.android.api.SpotgramAndroidException;

public class SpotSearchActivity extends ListActivity {
	String serverAddress;
	String serverPort;
	EditText nombrecity;
	EditText nombresport;
	private class FetchSpotsTask extends
			AsyncTask<Void, Void, SpotCollection> {
		private ProgressDialog pd;

		@Override
		protected SpotCollection doInBackground(Void... params) {
			SpotCollection Spots = null;
			try {
				Spots = SpotgramAPI.getInstance(SpotSearchActivity.this)
						.getSpots();
			} catch (SpotgramAndroidException e) {
				e.printStackTrace();
			}
			return Spots;
		}

		@Override
		protected void onPostExecute(SpotCollection result) {
			addSpots(result);
			if (pd != null) {
				pd.dismiss();
			}
		}

		@Override
		protected void onPreExecute() {
			pd = new ProgressDialog(SpotSearchActivity.this);
			pd.setTitle("Receiving Spots...");
			pd.setCancelable(false);
			pd.setIndeterminate(true);
			pd.show();
		}

	}
	private void addSpots(SpotCollection Spots){
		SpotList.clear();
		SpotList.addAll(Spots.getSpots());
		adapter.notifyDataSetChanged();
	}
	private ArrayList<Spot> SpotList;
	private SpotAdapter adapter;

	private final static String TAG = SpotSearchActivity.class.toString();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.beeter_layout);	

		nombrecity = (EditText) findViewById(R.id.textCity);
		nombresport = (EditText) findViewById(R.id.textSport);	
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
    	} catch (IOException e) {
    		Log.e(TAG, e.getMessage(), e);
    		finish();
    	}
		SpotList = new ArrayList<>();
		adapter = new SpotAdapter(this, SpotList);
		setListAdapter(adapter);
		(new FetchSpotsTask()).execute();
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.beeter_actions, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.miWrite:
			Intent intent = new Intent(this, SpotCreateActivity.class);
			startActivity(intent);
			return true;
		case R.id.miAbout:
			Intent intent2 = new Intent(this, AboutActivity.class);
			startActivity(intent2);
			return true;
		case R.id.miOut:
			Intent intent3 = new Intent(this, DesLogin.class);
			startActivity(intent3);
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}

	}
	public void searchAll(View v) {
		(new FetchSpotsTask()).execute();
	}
    public void searchBy(View v) {
    	AssetManager assetManager = getAssets();
    	Properties config = new Properties();
    	try {
    		config.load(assetManager.open("config.properties"));
    		serverAddress = config.getProperty("server.address");
    		serverPort = config.getProperty("server.port");
     
    		Log.d(TAG, "Configured server " + serverAddress + ":" + serverPort);
    	} catch (IOException e) {
    		Log.e(TAG, e.getMessage(), e);
    		finish();
    	}
    	Editable ciudad = nombrecity.getText();
    	Editable modal = nombresport.getText();
    	URL url = null;
    	try {
    		url = new URL("http://" + serverAddress + ":" + serverPort
    				+ "/spot-api/spots/search?ciudad="+ciudad+"&deporte="+modal);
    	} catch (MalformedURLException e) {
    		return;
    	}
		(new FetchSpotingTask()).execute(url);
    }
	@Override
	// detecta click en la pantalla
	protected void onListItemClick(ListView l, View v, int position, long id) {
		
		position = position+1;
    	// HATEOAS version
    	URL url = null;
    	try {
    		url = new URL("http://" + serverAddress + ":" + serverPort
    				+ "/spot-api/spots/" + position);
    	} catch (MalformedURLException e) {
    		return;
    	}
    	Intent intent = new Intent(this, SpotDetailActivity.class);
    	intent.putExtra("url", url.toString());
    	startActivity(intent);
//    	
//    	VERSION CON HATEOAS
//    	
//		Spot Spot = SpotList.get(position);
//		Log.d(TAG, Spot.getLinks().get("abrir-spot").getTarget());
//
//		Intent intent = new Intent(this, SpotDetailActivity.class);
//		intent.putExtra("url", Spot.getLinks().get("abrir-spot").getTarget());
//		startActivity(intent);
	}
	private class FetchSpotingTask extends AsyncTask<URL, Void, SpotCollection> {
		private ProgressDialog pd;

		@Override
		protected SpotCollection doInBackground(URL... params) {
			SpotCollection Spots = null;
			try {
				Spots = SpotgramAPI.getInstance(SpotSearchActivity.this)
						.getSpots(params[0]);
			} catch (SpotgramAndroidException e) {
				e.printStackTrace();
			}
			return Spots;
		}

		@Override
		protected void onPostExecute(SpotCollection result) {
			addSpots(result);
			if (pd != null) {
				pd.dismiss();
			}
		}

		@Override
		protected void onPreExecute() {
			pd = new ProgressDialog(SpotSearchActivity.this);
			pd.setTitle("Receiving spot...");
			pd.setCancelable(false);
			pd.setIndeterminate(true);
			pd.show();
		}

	 
	}

}
