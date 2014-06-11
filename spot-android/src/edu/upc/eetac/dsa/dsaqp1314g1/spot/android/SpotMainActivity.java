package edu.upc.eetac.dsa.dsaqp1314g1.spot.android;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;

public class SpotMainActivity extends Activity
{
	double longit=1.234567;
	double latit=42.87654;
	static final int REQUEST_IMAGE_CAPTURE = 1;
	static final int REQUEST_TAKE_PHOTO = 1;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
    
    public void createSpot(View v) {
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
	        	 Intent intent1 = new Intent(this, SpotCreateActivity.class);
	     		 startActivity(intent1);
	        	 }
	        } catch (IOException ex) {
	            // Error occurred while creating the File
	          
	        }
	        }		
    }
    public void searchSpots(View v) {
    	Intent intent2 = new Intent(this, SpotSearchActivity.class);
		startActivity(intent2);
    }
    public void about(View v) {
    	Intent intent3 = new Intent(this, AboutActivity.class);
		startActivity(intent3);
    }

	String mCurrentPhotoPath;
    private File createImageFile() throws IOException {
	    // Create an image file name
	    String lati = Double.toString(latit);
	    String longi = Double.toString(longit);
	    String name = "lat: "+ lati + " long: " +longi;
	    
	    String imageFileName = name + "_";
	    File storageDir = Environment.getExternalStoragePublicDirectory(
	            Environment.DIRECTORY_PICTURES);
	    File image = File.createTempFile(
	        imageFileName,  /* prefix */
	        ".jpg",         /* suffix */
	        storageDir      /* directory */
	    );

	    // Save a file: path for use with ACTION_VIEW intents
	    mCurrentPhotoPath = "file:" + image.getAbsolutePath();
	    return image;
	}
}
