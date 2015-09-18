package de.beacon.tom.viibenav_radiomapper.controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

import de.beacon.tom.viibenav_radiomapper.model.BluetoothScan;
import de.beacon.tom.viibenav_radiomapper.model.WiFiConnector;

/**
 * Created by TomTheBomb on 18.09.2015.
 */
public class ViibeActivity extends Activity {

    public static final String TAG = "ViibeActivity";
    private boolean initAnotherActivity;

    @Override
    protected void onResume() {
        super.onResume();

        initAnotherActivity = false;

        // Turn Off WiFi signals on activity resume as it mitigates position estimation
        if(WiFiConnector.getConnector(this).WiFiEnabled())
            WiFiConnector.getConnector(this).disableWiFi();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(!initAnotherActivity)
            onHide();
        Log.d(TAG, "initAnotherActivity? "+ initAnotherActivity);
    }

    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if(moveTaskToBack(false) == false) {
                initAnotherActivity = true;
                Log.d(TAG, "moveToBack Failed - not root");
            } else {
                Log.d(TAG, "MOVE TASK TO BACK");
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        initAnotherActivity = true;
        super.startActivityForResult(intent, requestCode);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode, Bundle options) {
        initAnotherActivity = true;
        super.startActivityForResult(intent, requestCode, options);
    }

    /**
     * On Hide gets called, then the user hides the application by leaving to home screen, showing the application in the android task manager
     * (rectangle symbol) or pressing the back button on the root activity (which leads to switching to android home screen)
     */
    public void onHide(){
        Log.d(TAG, "onHide Activity");
        if(!WiFiConnector.getConnector(this).WiFiEnabled())
            WiFiConnector.getConnector(this).enableWiFi();
        if(BluetoothScan.getBtScan(this).getmBluetoothAdapter().isEnabled())
            BluetoothScan.getBtScan(this).disableBt();
    }
}
