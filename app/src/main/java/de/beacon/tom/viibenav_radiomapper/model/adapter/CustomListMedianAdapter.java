package de.beacon.tom.viibenav_radiomapper.model.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import de.beacon.tom.viibenav_radiomapper.R;
import de.beacon.tom.viibenav_radiomapper.model.dbmodels.MedianView;


/**
 * Created by TomTheBomb on 25.07.2015.
 */
public class CustomListMedianAdapter  extends ArrayAdapter<MedianView> {
    public CustomListMedianAdapter(Context context){
        super(context,0);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater tomsInflater = LayoutInflater.from(getContext());
        View customView = tomsInflater.inflate(R.layout.testarea_medians_custom_row, parent, false);

        MedianView m = getItem(position);
        TextView id_median = (TextView) customView.findViewById(R.id.id_medians);
        TextView median_median = (TextView) customView.findViewById(R.id.median_median);
        TextView median_beaconid = (TextView) customView.findViewById(R.id.median_beaconid);
        TextView orientation = (TextView) customView.findViewById(R.id.orientation);

        id_median.setText(""+ m.getId());
        median_median.setText("" + m.getMedian()+" |");
        median_beaconid.setText("" + m.getMedian_beaconid());
        orientation.setText("" + m.getOrientation());

        return customView;
    }
}
