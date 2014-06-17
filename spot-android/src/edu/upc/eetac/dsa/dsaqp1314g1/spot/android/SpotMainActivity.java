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
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
    
    public void createSpot(View v) {
    	Intent intent1 = new Intent(this, SpotCreateActivity.class);
		startActivity(intent1);
    }
    public void searchSpots(View v) {
    	Intent intent2 = new Intent(this, SpotSearchActivity.class);
		startActivity(intent2);
    }
    public void about(View v) {
    	Intent intent3 = new Intent(this, AboutActivity.class);
		startActivity(intent3);
    }
}
