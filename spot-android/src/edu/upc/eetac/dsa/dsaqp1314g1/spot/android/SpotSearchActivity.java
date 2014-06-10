package edu.upc.eetac.dsa.dsaqp1314g1.spot.android;

import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.util.ArrayList;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import edu.upc.eetac.dsa.dsaqp1314g1.spot.android.api.Spot;
import edu.upc.eetac.dsa.dsaqp1314g1.spot.android.api.SpotCollection;
import edu.upc.eetac.dsa.dsaqp1314g1.spot.android.api.SpotgramAPI;
import edu.upc.eetac.dsa.dsaqp1314g1.spot.android.api.SpotgramAndroidException;

public class SpotSearchActivity extends ListActivity {

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
			pd.setTitle("Searching...");
			pd.setCancelable(false);
			pd.setIndeterminate(true);
			pd.show();
		}

	}
	private void addSpots(SpotCollection Spots){
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
	 
		SpotList = new ArrayList<>();
		adapter = new SpotAdapter(this, SpotList);
		setListAdapter(adapter);
		(new FetchSpotsTask()).execute();
	}
	

	@Override
	// detecta click en la pantalla
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Spot Spot = SpotList.get(position);
		Log.d(TAG, Spot.getLinks().get("self").getTarget());

		Intent intent = new Intent(this, SpotDetailActivity.class);
		intent.putExtra("url", Spot.getLinks().get("self").getTarget());
		startActivity(intent);
	}


}
