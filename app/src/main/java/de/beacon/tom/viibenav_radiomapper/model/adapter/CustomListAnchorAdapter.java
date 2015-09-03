package de.beacon.tom.viibenav_radiomapper.model.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import de.beacon.tom.viibenav_radiomapper.R;
import de.beacon.tom.viibenav_radiomapper.model.dbmodels.AnchorPointDBModel;


/**
 * Created by TomTheBomb on 24.07.2015.
 */
public class CustomListAnchorAdapter extends ArrayAdapter<AnchorPointDBModel> {

    public CustomListAnchorAdapter(Context context){
        super(context,0);

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater tomsInflater = LayoutInflater.from(getContext());
        View customView = tomsInflater.inflate(R.layout.testarea_anchors_custom_row, parent, false);

        AnchorPointDBModel a = getItem(position);
        TextView id = (TextView) customView.findViewById(R.id.dbAnchorID);
        TextView coord = (TextView) customView.findViewById(R.id.dbCoord);
        TextView front = (TextView) customView.findViewById(R.id.dbAnchorFrontID);
        TextView back = (TextView) customView.findViewById(R.id.dbAnchorBackID);
        TextView info = (TextView) customView.findViewById(R.id.dbAnchorInfoID);

        id.setText("" + a.get_id());
        coord.setText("" + a.getCoord().getFloor()+" | " + a.getCoord().getX()+" | " + a.getCoord().getY()+" | ");
        front.setText(""+a.getFront_id());
        back.setText(""+a.getBack_id());
        info.setText(""+a.getAddInfoID());

        return customView;
    }
}
