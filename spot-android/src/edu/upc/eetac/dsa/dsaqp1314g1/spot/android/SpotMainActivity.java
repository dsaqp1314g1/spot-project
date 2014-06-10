package edu.upc.eetac.dsa.dsaqp1314g1.spot.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
    	Intent intent = new Intent(this, SpotCreateActivity.class);
		startActivity(intent);
		finish();
    }
    public void searchSpots(View v) {
    	Intent intent = new Intent(this, SpotSearchActivity.class);
		startActivity(intent);
		finish();
    }
    public void about(View v) {
    	
    }
}
