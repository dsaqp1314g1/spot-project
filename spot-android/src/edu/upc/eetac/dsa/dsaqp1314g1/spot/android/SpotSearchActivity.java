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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import edu.upc.eetac.dsa.dsaqp1314g1.spot.android.api.Spot;
import edu.upc.eetac.dsa.dsaqp1314g1.spot.android.api.SpotCollection;
import edu.upc.eetac.dsa.dsaqp1314g1.spot.android.api.SpotgramAndroidException;
import edu.upc.eetac.dsa.egalmes.beeter.android.api.*;

public class SpotSearchActivity extends ListActivity {

	private class FetchStingsTask extends
			AsyncTask<Void, Void, SpotCollection> {
		private ProgressDialog pd;

		@Override
		protected SpotCollection doInBackground(Void... params) {
			SpotCollection stings = null;
			try {
				stings = BeeterAPI.getInstance(SpotSearchActivity.this)
						.getStings();
			} catch (SpotgramAndroidException e) {
				e.printStackTrace();
			}
			return stings;
		}

		@Override
		protected void onPostExecute(SpotCollection result) {
			addStings(result);
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
	private void addStings(SpotCollection stings){
		stingList.addAll(stings.getSpots());
		adapter.notifyDataSetChanged();
	}
	private ArrayList<Spot> stingList;
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
	 
		stingList = new ArrayList<>();
		adapter = new SpotAdapter(this, stingList);
		setListAdapter(adapter);
		(new FetchStingsTask()).execute();
	}
	

	@Override
	// detecta click en la pantalla
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Sting sting = stingList.get(position);
		Log.d(TAG, sting.getLinks().get("self").getTarget());

		Intent intent = new Intent(this, SpotDetailActivity.class);
		intent.putExtra("url", sting.getLinks().get("self").getTarget());
		startActivity(intent);
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
			Intent intent = new Intent(this, WriteStingActivity.class);
			startActivity(intent);
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}

	}

}
