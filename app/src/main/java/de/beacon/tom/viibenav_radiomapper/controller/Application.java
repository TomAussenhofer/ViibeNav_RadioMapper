package de.beacon.tom.viibenav_radiomapper.controller;

import android.app.DialogFragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.SensorEvent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import de.beacon.tom.viibenav_radiomapper.R;
import de.beacon.tom.viibenav_radiomapper.model.AddInfo;
import de.beacon.tom.viibenav_radiomapper.model.OnyxBeacon;
import de.beacon.tom.viibenav_radiomapper.model.RadioMap;
import de.beacon.tom.viibenav_radiomapper.model.SensorHelper;
import de.beacon.tom.viibenav_radiomapper.model.fragment.AddInfoDialog;


/**
 * Created by TomTheBomb on 23.06.2015.
 */
public class Application{

    private static final String TAG = "ApplicationUI";

    public MainActivity main;

    /*
    GUI elements
    Consider the UI is divided into three layers:
     ________________________________________
    |                                        |
    |                 Overview               |
    |________________________________________|
    |                                        |
    |               RSSIs around             |
    |________________________________________|
    |                                        |
    |           Navigation elements          |
    |________________________________________|
     */

    // Layer 1
    private TextView totalAnchor,lastX,lastY,lastEtage;

    // Layer 2
    private TextView anzahlBeaconView,minIDsFeld,degreeTV;
    private ImageView arrowImage;

    private Handler calcMediansHandler;
    private Measurement measurement;

    private AddInfo addInfo;

    private Layer3 layer3;

    private ScheduledExecutorService execOrientation;
    private SensorHelper sh;

    private BroadcastReceiver mReceiver;

    Application(MainActivity main){
        this.main = main;
        measurement = new Measurement();
        sh = SensorHelper.getSensorHelper(main.getApplicationContext());

        // initialize GUI elemnts
        initGUI();
        initHandler();

    }

    private void initGUI(){
        // Layer 1
        totalAnchor = (TextView) main.findViewById(R.id.totalAnchor);
        lastX = (TextView) main.findViewById(R.id.lastX);
        lastY = (TextView) main.findViewById(R.id.lastY);
        lastEtage = (TextView) main.findViewById(R.id.lastEtage);

        // Layer 2
        anzahlBeaconView = (TextView) main.findViewById(R.id.anzahlBeaconFeld);
        minIDsFeld = (TextView) main.findViewById(R.id.minIDsFeld);
        arrowImage = (ImageView) main.findViewById(R.id.arrowImageView);
        degreeTV = (TextView) main.findViewById(R.id.dialog_second_measure_degreeTV);


        addInfo = new AddInfo();
        layer3 = new Layer3(main);
    }

    private void initHandler(){
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                    boolean minIDadded = intent.getBooleanExtra("minID",false);
                Log.d(TAG, "new MinID");
                    if(minIDadded == true)
                        updateLayer2();
            }
        };

        LocalBroadcastManager.getInstance(main).registerReceiver(mReceiver,new IntentFilter("minIDadded"));

        execScheduled();

        calcMediansHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                // sets device state to measuring, which deactivates GUI elements
                startMeasurement(true);
            }
        };
    }

    public void startMeasurement(boolean firstMeasurement){
        measurement.setState(Measurement.State.isMeasuring);
        ArrayList<OnyxBeacon> calcBeacons = new ArrayList<>();
        Iterator it = OnyxBeacon.filterSurroundingBeacons().iterator();
        while (it.hasNext()) {
            OnyxBeacon tmp = (OnyxBeacon) it.next();
            tmp.setMeasurementStarted(true);
            calcBeacons.add(tmp);
        }
        Log.d(TAG, "MESSE BEACONS: " + calcBeacons.size());
        measurement.overallCalcProgress(System.currentTimeMillis(), calcBeacons, main, firstMeasurement);
    }

    public void execScheduled() {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                execOrientation = Executors.newSingleThreadScheduledExecutor();
                execOrientation.scheduleAtFixedRate(new Runnable() {
                    @Override
                    public void run() {
                        orientationHandler.sendEmptyMessage(0);
                    }
                }, 0, 250, TimeUnit.MILLISECONDS);

            }
        };
        Thread th = new Thread(r);
        th.start();
    }

    private Handler orientationHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            sh.animateImage(arrowImage);
            sh.updateTextView(degreeTV);
        }
    };

    /**
     * By invoking this method you start median measurement for the beacons found nearby.
     * It will only start median measurement for the beacons already listed in the onyxBeaconHashMap.
     */
    public void startMeasurement(View view){
        calcMediansHandler.sendEmptyMessage(0);
    }

    public void onSensorChangedOperation(SensorEvent event){sh.onSensorChangedOperation(event);}
    public void onResumeOperation(MainActivity n){sh.onResumeOperation(n);}
    public void onPauseOperation(MainActivity n){sh.onPauseOperation(n);}

    public void updateLayer1(){
        totalAnchor.setText("" + RadioMap.size());
        lastX.setText(""+RadioMap.getRadioMap().getLastAnchor().getCoordinate().getX());
        lastY.setText(""+RadioMap.getRadioMap().getLastAnchor().getCoordinate().getY());
        lastEtage.setText("" + RadioMap.getRadioMap().getLastAnchor().getCoordinate().getFloor());
    }

    public void updateLayer2(){
        anzahlBeaconView.setText(""+OnyxBeacon.beaconMap.size());
        String minIDs = "";
        Iterator it = OnyxBeacon.beaconMap.entrySet().iterator();
        int count = 0;
        while (it.hasNext()) {
            count++;
            Map.Entry<String,OnyxBeacon> pair = (Map.Entry)it.next();
                minIDs += "|" + pair.getValue().getMinor();
                if((count % 4) == 0)
                    minIDs += System.lineSeparator();
        }
        minIDsFeld.setText(minIDs);
    }

    public void clickUp(View view){
        layer3.clickUp(view);
    }
    public void clickRight(View view){
        layer3.clickRight(view);
    }
    public void clickDown(View view){
       layer3.clickDown(view);
    }
    public void clickLeft(View view){
       layer3.clickLeft(view);
    }

    public void prefsClicked(View view){
        Intent intent = new Intent(main, TestAreaActivity.class);
        main.startActivityForResult(intent, 0);
    }

    public void clickInfo(View view){
        Intent intent = new Intent(main, InfoActivity.class);
        main.startActivityForResult(intent, 0);
    }

    public void createInfo(View view){
        AddInfoDialog aid = new AddInfoDialog();
        DialogFragment dialogFrag = aid;
        aid.show(main.getFragmentManager(), "MyDF");
    }

    public AddInfo getAddInfo() {
        return addInfo;
    }

    public ScheduledExecutorService getExecOrientation() {
        return execOrientation;
    }
}
