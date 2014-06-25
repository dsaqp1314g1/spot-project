package edu.upc.eetac.dsa.dsaqp1314g1.spot.android;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Properties;

import edu.upc.eetac.dsa.dsaqp1314g1.spot.android.api.Comentario;
import edu.upc.eetac.dsa.dsaqp1314g1.spot.android.api.Spot;
import edu.upc.eetac.dsa.dsaqp1314g1.spot.android.api.SpotgramAPI;
import edu.upc.eetac.dsa.dsaqp1314g1.spot.android.api.SpotgramAndroidException;
import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
public class SpotDetailActivity extends ListActivity {
	private final static String TAG = SpotDetailActivity.class.getName();
	private CommentAdapter adapter;
	 String urlimatge = null;
	 ImageView imagespot;
	 Bitmap bimage;
	 String urlIM;
		String serverAddress;
		String serverPort;
	private ArrayList<Comentario> CommentList;
	@Override//getextras qe se los pasa el mainactivity
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sting_detail_layout);
		URL url = null;
		try {
			url = new URL((String) getIntent().getExtras().get("url"));
		} catch (MalformedURLException e) {
		}
		AssetManager assetManager = getAssets();
    	Properties config = new Properties();
    	
    	try {
    		config.load(assetManager.open("config.properties"));
    		serverAddress = config.getProperty("server.address");
    		serverPort = config.getProperty("server.port");
    	}catch (IOException e) {
    		Log.e(TAG, e.getMessage(), e);
    		finish();
    	}
    	urlimatge = "http://" + serverAddress+ "/imgenes/";	
    	
		CommentList = new ArrayList<>();
		adapter = new CommentAdapter(this, CommentList);
		setListAdapter(adapter);
		(new FetchSpotTask()).execute(url);
//		String urlSpot = (String) getIntent().getExtras().get("url");
//		(new FetchSpotTask()).execute(urlSpot);
	}
	//metodo privado a partir del Spot recuèrado del servicio damos valores a las etiketas qe hemos dado
	private void loadSpot(Spot Spot) {
		TextView tvDetailSubject = (TextView) findViewById(R.id.tvDetailSubject);
		TextView tvDetailUsername = (TextView) findViewById(R.id.tvDetailUsername);
		TextView tvDetailDate = (TextView) findViewById(R.id.tvDetailDate);
		imagespot = (ImageView) findViewById(R.id.spotimagen);
		tvDetailSubject.setText(Spot.getTitle());
		tvDetailUsername.setText(Spot.getUsuario());
		tvDetailDate.setText(Spot.getDeporte());
		
		CommentList.addAll(Spot.getComentario());
		adapter.notifyDataSetChanged();
		Log.e(TAG,urlIM);
		
		//imagespot.setBackground(Spot.getImageURL());
	}
	 
	private class FetchSpotTask extends AsyncTask<URL, Void, Spot> {
		private ProgressDialog pd;
	 
		@Override
		protected Spot doInBackground(URL... params) {
			Spot Spot = null;
			try {
				Spot = SpotgramAPI.getInstance(SpotDetailActivity.this)
						.getSpot(params[0]);
				urlIM = urlimatge+Spot.getIdspot()+".png";
				bimage=  getBitmapFromURL(urlIM);
			} catch (SpotgramAndroidException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return Spot;
		}
	 
		@Override
		protected void onPostExecute(Spot result) {
			loadSpot(result);
			
			imagespot.setImageBitmap(bimage);
			
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
	public Bitmap getBitmapFromURL(String src) {
        try {           
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            Log.e("Bitmap","returned");
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
