package edu.upc.eetac.dsa.dsaqp1314g1.spot.android;

import java.text.SimpleDateFormat;

import edu.upc.eetac.dsa.dsaqp1314g1.spot.android.api.Spot;
import edu.upc.eetac.dsa.dsaqp1314g1.spot.android.api.SpotgramAPI;
import edu.upc.eetac.dsa.dsaqp1314g1.spot.android.api.SpotgramAndroidException;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;
 
public class SpotDetailActivity extends Activity {
	private final static String TAG = SpotDetailActivity.class.getName();
 
	@Override//getextras qe se los pasa el mainactivity
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sting_detail_layout);
		String urlSpot = (String) getIntent().getExtras().get("url");
		(new FetchSpotTask()).execute(urlSpot);
	}
	//metodo privado a partir del Spot recuèrado del servicio damos valores a las etiketas qe hemos dado
	private void loadSpot(Spot Spot) {
		TextView tvDetailSubject = (TextView) findViewById(R.id.tvDetailSubject);
		TextView tvDetailContent = (TextView) findViewById(R.id.tvDetailContent);
		TextView tvDetailUsername = (TextView) findViewById(R.id.tvDetailUsername);
		TextView tvDetailDate = (TextView) findViewById(R.id.tvDetailDate);
	 
		tvDetailSubject.setText(Spot.getTitle());
		tvDetailContent.setText(Spot.getCiudad());
		tvDetailUsername.setText(Spot.getUsuario());
		tvDetailDate.setText(Spot.getDeporte());
	}
	//Clase anidada fetchSpottasktarea en background va a beeterapi, obtiene instancia i llama get Spot pasando la url que hemos recperado del recurso
	//decalramos esas strin task , tipo de parametros del background 
	
	
	private class FetchSpotTask extends AsyncTask<String, Void, Spot> {
		private ProgressDialog pd;
	 
		@Override
		protected Spot doInBackground(String... params) {
			Spot Spot = null;
			try {
				Spot = SpotgramAPI.getInstance(SpotDetailActivity.this)
						.getSpot(params[0]);
			} catch (SpotgramAndroidException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return Spot;
		}
	 
		@Override
		protected void onPostExecute(Spot result) {
			loadSpot(result);
			if (pd != null) {
				pd.dismiss();
			}
		}
	 
		@Override
		protected void onPreExecute() {
			pd = new ProgressDialog(SpotDetailActivity.this);
			pd.setTitle("Loading...");
			pd.setCancelable(false);
			pd.setIndeterminate(true);
			pd.show();
		}
	 
	}
}
