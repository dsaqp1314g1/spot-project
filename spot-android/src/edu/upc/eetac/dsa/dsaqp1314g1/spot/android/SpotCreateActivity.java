package edu.upc.eetac.dsa.dsaqp1314g1.spot.android;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class SpotCreateActivity extends Activity{
	public class MainActivity extends Activity {
		double LAT = 41.279275;
		double LONG = 1.976776;
		
		static final int REQUEST_IMAGE_CAPTURE = 1;
		static final int REQUEST_TAKE_PHOTO = 1;
		ImageView resultat;
		Button cam;
		double longit;
		double latit;
		LocationManager mlocManager;
		
		
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.create);
		}

		public void seguent() {
			// TODO Auto-generated method stub		

			String Text = "My current location is: " +latit +" "  +longit;
			Toast.makeText(getApplicationContext(),	Text, Toast.LENGTH_SHORT).show();
			getCity();
		}
		
		private void getCity(){
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
	        String s = "My Current City is: "
	            + cityName;
	        Toast.makeText(getApplicationContext(),	s, Toast.LENGTH_SHORT).show();
		}	


		
		@Override 
		public void onPause()
		{
		    super.onPause();
		    //mlocManager.removeUpdates(mlocListener);
		}
	}

}
