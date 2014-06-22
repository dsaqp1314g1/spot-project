package edu.upc.eetac.dsa.dsaqp1314g1.spot.android;

import java.io.File;
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
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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
			showSpots();
			if (pd != null) {
				pd.dismiss();
			}
		}

		@Override
		protected void onPreExecute() {
			pd = new ProgressDialog(SpotCreateActivity.this);
			pd.setTitle("Creating Spot...");
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

    	Intent intent =  new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//		String urlSpot = (String) getIntent().getExtras().get("url");
//		(new FetchSpotTask()).execute(urlSpot);
	}
	public void sendServer(View v) {
			usuario = "juan";
			city = ciudad.getText().toString();
			sport = deporte.getText().toString();
			title = titulo.getText().toString();			
		
		(new PullSpotsTask()).execute(usuario, city, sport,title,url);
	}
	/** Called when the user recieves the location*/
	public void sendCamara() {		
		    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		    // Ensure that there's a camera activity to handle the intent
		    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
		        // Create the File where the photo should go
		        File photoFile = null;
		        try {
		        	 photoFile = createImageFile();
				        // Continue only if the File was successfully created		        		            		          
		        	 if (photoFile != null) {
		        	 takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
			                    Uri.fromFile(photoFile));
		        	 }
		        } catch (IOException ex) {
		            // Error occurred while creating the File
		          
		        }
		        }		    		
	}
	String mCurrentPhotoPath;
	private File createImageFile() throws IOException {
	    // Create an image file name
	    String name = "caca " ;
	    
	    String imageFileName = name + "_";
	    File storageDir = Environment.getExternalStoragePublicDirectory(
	            Environment.DIRECTORY_PICTURES);
	    File image = File.createTempFile(
	        imageFileName,  /* prefix */
	        ".png",         /* suffix */
	        storageDir      /* directory */
	    );

	    // Save a file: path for use with ACTION_VIEW intents
	    mCurrentPhotoPath = "file:" + image.getAbsolutePath();
	    return image;
	}
	
	private void showSpots() {
		Intent intent = new Intent(this, SpotSearchActivity.class);
		
		startActivity(intent);
		finish();
	}
}
