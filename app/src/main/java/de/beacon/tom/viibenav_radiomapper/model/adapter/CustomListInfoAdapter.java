package de.beacon.tom.viibenav_radiomapper.model.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import de.beacon.tom.viibenav_radiomapper.R;
import de.beacon.tom.viibenav_radiomapper.model.dbmodels.InfoDBModel;

/**
 * Created by TomTheBomb on 27.08.2015.
 */
public class CustomListInfoAdapter extends ArrayAdapter<InfoDBModel> {

    public CustomListInfoAdapter(Context context){
        super(context,0);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater tomsInflater = LayoutInflater.from(getContext());
        View customView = tomsInflater.inflate(R.layout.testarea_info_custom_row, parent, false);

        InfoDBModel tmp = getItem(position);
        TextView id = (TextView) customView.findViewById(R.id.dbInfoID);
        TextView person_name = (TextView) customView.findViewById(R.id.dbInfoPersonName);
        TextView room_name = (TextView) customView.findViewById(R.id.dbInfoRoomName);
        TextView environment = (TextView) customView.findViewById(R.id.dbInfoEnvironment);
        TextView category = (TextView) customView.findViewById(R.id.dbInfoCategory);

        id.setText(""+ tmp.getId());
        person_name.setText("" + tmp.getPerson_name());
        room_name.setText(""+tmp.getRoom_name());
        environment.setText(""+tmp.getEnvironment());
        category.setText(""+tmp.getCategory());


        return customView;
    }
}
