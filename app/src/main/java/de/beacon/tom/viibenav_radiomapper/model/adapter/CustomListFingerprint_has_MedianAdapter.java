package de.beacon.tom.viibenav_radiomapper.model.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import de.beacon.tom.viibenav_radiomapper.R;
import de.beacon.tom.viibenav_radiomapper.model.dbmodels.Fingerprint_has_MedianView;

/**
 * Created by TomTheBomb on 03.09.2015.
 */
public class CustomListFingerprint_has_MedianAdapter extends ArrayAdapter<Fingerprint_has_MedianView> {


    public CustomListFingerprint_has_MedianAdapter(Context context){
        super(context,0);

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater tomsInflater = LayoutInflater.from(getContext());
        View customView = tomsInflater.inflate(R.layout.testarea_fingerprint_has_median_custom_row, parent, false);

        Fingerprint_has_MedianView b = getItem(position);
        TextView id = (TextView) customView.findViewById(R.id.fingerprint_has_medianid);
        TextView fp_has_median_fingerprintid = (TextView) customView.findViewById(R.id.fingerprint_has_median_fingerprintid);
        TextView fp_has_median_medianid = (TextView) customView.findViewById(R.id.fingerprint_has_median_medianid);

        id.setText(""+b.getId());
        fp_has_median_fingerprintid.setText(""+b.getFingerprintid());
        fp_has_median_medianid.setText(""+b.getMedianid());

        return customView;
    }
}
