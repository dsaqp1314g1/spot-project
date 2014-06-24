package edu.upc.eetac.dsa.dsaqp1314g1.spot.android.api;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class Spinneradapt extends ArrayAdapter<Deportes>{

    private Context context;
    private Deportes[] values;

    public Spinneradapt(Context context, int textViewResourceId,
            Deportes[] values) {
        super(context, textViewResourceId, values);
        this.context = context;
        this.values = values;
    }

    public int getCount(){
       return values.length;
    }

    public Deportes getItem(int position){
       return values[position];
    }

    public long getItemId(int position){
       return position;
    }
    
    // ESTAT "PASSIU"
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView label = new TextView(context);
        label.setTextColor(Color.LTGRAY);
        label.setText("  "+values[position].getNom());

        return label;
    }
    
    // ESTAT "ACTIU"
	@Override    
    public View getDropDownView(int position, View convertView,
            ViewGroup parent) {
        TextView label = new TextView(context);
        label.setTextSize(20);
        label.setTextColor(Color.LTGRAY);
        label.setText("  "+values[position].getNom());

        return label;
    }
}