package de.beacon.tom.viibenav_radiomapper.controller;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.ListView;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import de.beacon.tom.viibenav_radiomapper.R;
import de.beacon.tom.viibenav_radiomapper.model.OnyxBeacon;
import de.beacon.tom.viibenav_radiomapper.model.adapter.CustomListAdapter;


/**
 * Created by TomTheBomb on 15.07.2015.
 */
public class InfoActivity extends Activity {

    ScheduledExecutorService exec;
    CustomListAdapter listAdapter;
    ListView beaconListView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.info_activity);

        listAdapter = new CustomListAdapter(this);
        beaconListView = (ListView) findViewById(R.id.myListView);
        beaconListView.setAdapter(listAdapter);

        Runnable r = new Runnable(){
            @Override
            public void run() {
                exec = Executors.newSingleThreadScheduledExecutor();
                exec.scheduleAtFixedRate(new Runnable() {
                    @Override
                    public void run() {
//                        Log.d("Measurement", OnyxBeacon.getBeaconMapAsList().size() + "");
                        infoHandler.sendEmptyMessage(0);
                    }
                }, 0, 1, TimeUnit.SECONDS);

            }
        };
        Thread th = new Thread(r);
        th.start();

    }

    Handler infoHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            listAdapter.clear();
            listAdapter.addAll(OnyxBeacon.getBeaconMapAsList());
            listAdapter.notifyDataSetChanged();
        }
    };

    public void onBackPressed(){
        exec.shutdown();
        super.onBackPressed();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch(keyCode){
            case KeyEvent.KEYCODE_BACK:
                onBackPressed();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
