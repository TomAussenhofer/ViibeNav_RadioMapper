package de.beacon.tom.viibenav_radiomapper.model.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import de.beacon.tom.viibenav_radiomapper.R;
import de.beacon.tom.viibenav_radiomapper.model.dbmodels.BeaconMedianToAnchorDBModel;

/**
 * Created by TomTheBomb on 03.09.2015.
 */
public class CustomBeaconMedianToAnchorAdapter extends ArrayAdapter<BeaconMedianToAnchorDBModel> {


    public CustomBeaconMedianToAnchorAdapter(Context context){
        super(context,0);

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater tomsInflater = LayoutInflater.from(getContext());
        View customView = tomsInflater.inflate(R.layout.testarea_beaconmediantoanchor_custom_row, parent, false);

        BeaconMedianToAnchorDBModel b = getItem(position);
        TextView id = (TextView) customView.findViewById(R.id.beacMedToAnch_id);
        TextView b1 = (TextView) customView.findViewById(R.id.beacon1);
        TextView b2 = (TextView) customView.findViewById(R.id.beacon2);
        TextView b3 = (TextView) customView.findViewById(R.id.beacon3);
        TextView b4 = (TextView) customView.findViewById(R.id.beacon4);
        TextView b5 = (TextView) customView.findViewById(R.id.beacon5);
        TextView b6 = (TextView) customView.findViewById(R.id.beacon6);

        id.setText(""+b.getId());
        b1.setText(""+b.getBeacon_1());
        b2.setText(""+b.getBeacon_2());
        b3.setText(""+b.getBeacon_3());
        b4.setText(""+b.getBeacon_4());
        b5.setText(""+b.getBeacon_5());
        b6.setText(""+b.getBeacon_6());

        return customView;
    }
}
