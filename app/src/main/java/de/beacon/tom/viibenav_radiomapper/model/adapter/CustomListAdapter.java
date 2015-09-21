package de.beacon.tom.viibenav_radiomapper.model.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import de.beacon.tom.viibenav_radiomapper.R;
import de.beacon.tom.viibenav_radiomapper.model.DistanceCalculation;
import de.beacon.tom.viibenav_radiomapper.model.OnyxBeacon;
import de.beacon.tom.viibenav_radiomapper.model.Util;


/**
 * Created by TomTheBomb on 26.05.2015.
 */

public class CustomListAdapter extends ArrayAdapter<OnyxBeacon> {


    public CustomListAdapter(Context context){
        super(context,0);

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            LayoutInflater tomsInflater = LayoutInflater.from(getContext());
            convertView = tomsInflater.inflate(R.layout.info_custom_row, parent, false);
        }

        OnyxBeacon beaconItem = getItem(position);
        TextView macAdressText = (TextView) convertView.findViewById(R.id.macAdressText);
        TextView majMinText = (TextView) convertView.findViewById(R.id.majMinText);
        TextView distancingText = (TextView) convertView.findViewById(R.id.distancingText);
        ImageView onyxImage = (ImageView) convertView.findViewById(R.id.onyxImage);

        macAdressText.setText(beaconItem.getMacAddress().toString() + " | " + beaconItem.getRssi());
        majMinText.setText("Major: "+beaconItem.getMajor()+", Minor: "+beaconItem.getMinor());
        distancingText.setText("FSPL: "+ Util.twoDecimals(DistanceCalculation.calculateDistanceFromFreeSpacePathLossModel((double) beaconItem.getRssi(), beaconItem.getTxPower()))+"m | Regr."+Util.twoDecimals(DistanceCalculation.calculateDistanceFromBestFitRegression((double) beaconItem.getRssi(), beaconItem.getTxPower()))+"m");
        onyxImage.setImageResource(R.mipmap.onyx_transparent);

        return convertView;
    }
}
