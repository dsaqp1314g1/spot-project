package edu.upc.eetac.dsa.dsaqp1314g1.spot.android;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.Authenticator;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import edu.upc.eetac.dsa.dsaqp1314g1.spot.android.api.Deportes;
import edu.upc.eetac.dsa.dsaqp1314g1.spot.android.api.Spinneradapt;
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
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

public class SpotCreateActivity extends Activity{
	 /** Called when the activity is first created. */
	static final int REQUEST_IMAGE_CAPTURE = 1;
	 ImageView iv;
	 LocationManager nlocManager;
	 LocationListener nlocListener;
	EditText ciudad;
	EditText deporte;
	Bitmap imageBitmap;
	private ProgressDialog prog;
	Spinner spin;
	String city;
	String sport;
	String title;
	Deportes[] dep = new Deportes[5];
	String usuario="juan";
	EditText titulo;
	String serverAddress;
	String serverPort;
	String name = "provisional";	
	String url;
	double longit = 1.985938;
	String lon;
	double latit = 41.275792;
	String lat;
	int i = 0;
	private final static String TAG = SpotCreateActivity.class.toString();
	Intent intent =  new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	
	private class PullSpotsTask extends
	AsyncTask<String, Void, Spot> {
		private ProgressDialog pd;
		Spot Spot = null;

		@Override
		protected Spot doInBackground(String... params) {
			try {
				Spot = SpotgramAPI.getInstance(SpotCreateActivity.this)
						.createSpot(params[0], params[1], params[2], params[3], params[4], params[5],params[6]);
			} catch (SpotgramAndroidException e) {
				e.printStackTrace();
			}
			return Spot;
		}

		@Override
		protected void onPostExecute(Spot result) {
			(new PullImageTask()).execute(imagePath, url+"imagen", Spot.getIdspot());
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

	private class PullImageTask extends
	AsyncTask<String, Void, Spot> {
		private ProgressDialog pd;

		@Override
		protected Spot doInBackground(String... params) {
			Spot Spot = null;
			try {
				SpotgramAPI.getInstance(SpotCreateActivity.this)
						.subirImag(params[0], params[1],params[2]);
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
			pd.setTitle("Creating Image...");
			pd.setCancelable(false);
			pd.setIndeterminate(true);
			pd.show();
		}

}
	
	@Override//getextras qe se los pasa el mainactivity
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create);
		iv = (ImageView)findViewById(R.id.imageView1);
		ciudad = (EditText) findViewById(R.id.textCiudad);
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
    	creardeportes();
    	final Spinneradapt spinadapt;
        spinadapt = new Spinneradapt(SpotCreateActivity.this,
        		android.R.layout.simple_spinner_item,
                dep);
            spin = (Spinner) findViewById(R.id.textDeporte);
            spin.setAdapter(spinadapt);
            spin.setOnItemSelectedListener(new OnItemSelectedListener() {               
					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						// TODO Auto-generated method stub						             
                        Deportes depor = spinadapt.getItem(position);
            			sport = depor.getNom();
					}
					@Override
					public void onNothingSelected(AdapterView<?> parent) {
						// TODO Auto-generated method stub
						return;
					}                
         });
            
    	prog = new ProgressDialog(SpotCreateActivity.this);
		prog.setTitle("Receiving Location...");
		prog.setCancelable(false);
		prog.setIndeterminate(true);
		prog.show();
    	nlocManager   = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        nlocListener = new MyLocationListenerNetWork();
        nlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                1000 * 1,  // 1 Sec        
                1,         // 1 meter   
                nlocListener);
	}
	public void sendServer(View v) {
			usuario = "juan";
			city = ciudad.getText().toString();
			title = titulo.getText().toString();			
			lon = String.valueOf(longit);
			lat = String.valueOf(latit);
		(new PullSpotsTask()).execute(usuario,lat,lon, city, sport,title, url);
		
	}
	@Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		 if(nlocManager != null){
		        nlocManager.removeUpdates(nlocListener);
		        Log.d("ServiceForLatLng", "Network Update Released");
		    }
		if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
		        Bundle extras = data.getExtras();
		        imageBitmap = (Bitmap) extras.get("data");
		        iv.setImageBitmap(imageBitmap);
		        Uri selectedImage = data.getData();

                String filePath = getPath(selectedImage);
                String file_extn = filePath.substring(filePath.lastIndexOf(".")+1);
        		Log.d(TAG, filePath);

		    }
		city = getCity();
        ciudad.setText(city);
	}
	
	
	/** Called when the user recieves the location*/
	String imagePath;
	int column_index;
	public String getPath(Uri uri) {
        String[] projection = { MediaColumns.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        column_index = cursor
                .getColumnIndexOrThrow(MediaColumns.DATA);
        cursor.moveToFirst();
        imagePath = cursor.getString(column_index);

        return cursor.getString(column_index);
    }
	private void showSpots() {
		Intent intent = new Intent(this, SpotSearchActivity.class);	
		startActivity(intent);
		finish();
	}

private String getCity(){
	 /*------- To get city name from coordinates -------- */
    String cityName = null;
    Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
    List<Address> addresses;
    try {
        addresses = gcd.getFromLocation(latit,
               longit, 1);
        if (addresses.size() > 0)
            System.out.println(addresses.get(0).getLocality());
        cityName = addresses.get(0).getLocality();
    }
    catch (IOException e) {
        e.printStackTrace();
    }
    String s = "Your Current City is: "
        + cityName;
    Toast.makeText(getApplicationContext(),	s, Toast.LENGTH_SHORT).show();
    return cityName;
}	

//This is for Lat lng which is determine by your wireless or mobile network
public class MyLocationListenerNetWork implements LocationListener  
{
  @Override
  public void onLocationChanged(Location loc)
  {
      latit = loc.getLatitude();
      longit = loc.getLongitude();
      if (prog != null) {
			prog.dismiss();
		}
      startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
      Log.d("LAT & LNG Network:", latit + " " + longit);
  }

  @Override
  public void onProviderDisabled(String provider)
  {
      Log.d("LOG", "Network is OFF!");
  }
  @Override
  public void onProviderEnabled(String provider)
  {
      Log.d("LOG", "Thanks for enabling Network !");
  }
  @Override
  public void onStatusChanged(String provider, int status, Bundle extras)
  {
  }
}
private void creardeportes(){
	
	dep[i] = new Deportes();
	dep[i].setNom("BMX");
	i++;
	dep[i] = new Deportes();
	dep[i].setNom("Parkour");
	i++;
	dep[i] = new Deportes();
	dep[i].setNom("Skate");
	i++;
	dep[i] = new Deportes();
	dep[i].setNom("Ski");
	i++;
	dep[i] = new Deportes();
	dep[i].setNom("Snowboard");
	i++;
}
@Override
public void onDestroy() {
    if(nlocManager != null){
        nlocManager.removeUpdates(nlocListener);
        Log.d("ServiceForLatLng", "Network Update Released");
    }
    if (prog != null) {
		prog.dismiss();
	}
    super.onDestroy();
}

}
