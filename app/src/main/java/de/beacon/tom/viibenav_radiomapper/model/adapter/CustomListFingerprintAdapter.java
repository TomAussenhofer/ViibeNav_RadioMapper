package de.beacon.tom.viibenav_radiomapper.model.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import de.beacon.tom.viibenav_radiomapper.R;
import de.beacon.tom.viibenav_radiomapper.model.dbmodels.FingerprintView;


/**
 * Created by TomTheBomb on 24.07.2015.
 */
public class CustomListFingerprintAdapter extends ArrayAdapter<FingerprintView> {

    public CustomListFingerprintAdapter(Context context){
        super(context,0);

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater tomsInflater = LayoutInflater.from(getContext());
        View customView = tomsInflater.inflate(R.layout.testarea_anchors_custom_row, parent, false);

        FingerprintView a = getItem(position);
        TextView id = (TextView) customView.findViewById(R.id.dbAnchorID);
        TextView coord = (TextView) customView.findViewById(R.id.dbCoord);
        TextView info = (TextView) customView.findViewById(R.id.dbAnchorInfoID);

        id.setText("" + a.getId());
        coord.setText("" + a.getCoord().getFloor()+" | " + a.getCoord().getX()+" | " + a.getCoord().getY()+" | ");
        info.setText(""+a.getAddInfoID());

        return customView;
    }
}
