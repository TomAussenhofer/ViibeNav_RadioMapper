package de.beacon.tom.viibenav_radiomapper.controller;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ListView;

import de.beacon.tom.viibenav_radiomapper.R;
import de.beacon.tom.viibenav_radiomapper.model.BluetoothScan;
import de.beacon.tom.viibenav_radiomapper.model.OnyxBeacon;
import de.beacon.tom.viibenav_radiomapper.model.adapter.CustomListAdapter;


/**
 * Created by TomTheBomb on 15.07.2015.
 */
public class InfoActivity extends ViibeActivity {

    public static final String TAG = "InfoActivity";

    CustomListAdapter listAdapter;
    ListView beaconListView;
    private boolean shutdown;
    private Handler beaconSignals;
    private Handler followUpHandler;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"CREATING INFO");
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.info_activity);


        listAdapter = new CustomListAdapter(this);
        beaconListView = (ListView) findViewById(R.id.myListView);
        beaconListView.setAdapter(listAdapter);
    }

    public void init(){
        shutdown = false;
        beaconSignals = new Handler();
        beaconSignals.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!shutdown) {
                    infoHandler.sendEmptyMessage(0);
                    new Handler().postDelayed(this, 1000);
                    Log.d(TAG, "INSIDE HANDLER");
                }
            }
        }, 0);
    }

    Handler infoHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            listAdapter.clear();
            listAdapter.addAll(OnyxBeacon.getBeaconMapAsList());
            listAdapter.notifyDataSetChanged();
        }
    };

    @Override
    protected void onPause() {
        shutdown = true;
        beaconSignals.removeCallbacksAndMessages(null);
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        init();
        BluetoothScan.getBtScan(this).onResumeOperation();
        Log.d(TAG, "RESUMING IN INFO");
    }


}
