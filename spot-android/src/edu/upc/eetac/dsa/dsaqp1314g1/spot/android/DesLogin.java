package edu.upc.eetac.dsa.dsaqp1314g1.spot.android;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

public class DesLogin extends Activity{
	private final static String TAG = DesLogin.class.toString();
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SharedPreferences prefs = getSharedPreferences("spot-profile",
				Context.MODE_PRIVATE);
		final String username = prefs.getString("username", null);
		final String password = prefs.getString("password", null);
	 
		
		SharedPreferences.Editor editor = prefs.edit();
		editor.clear();
		boolean done = editor.commit();
		if (done)
			Log.d(TAG, "preferences cleared");
		else
			Log.d(TAG, "preferences not cleared. THIS A SEVERE PROBLEM");
 
		//THIS MUST RETURN NULL!!
		Log.d(TAG, "authenticated with " + username + ":" + password);
		startLoginActivity();
	}
	private void startLoginActivity(){
		 Intent intent = new Intent(this, LoginActivity.class);
			startActivity(intent);
			finish();
	}
}
