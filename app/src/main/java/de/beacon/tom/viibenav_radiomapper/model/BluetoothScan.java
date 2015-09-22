package de.beacon.tom.viibenav_radiomapper.model;


import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by TomTheBomb on 23.06.2015.
 */
public class BluetoothScan {
    public static final String TAG = "BluetoothScan";

    private Activity act;
    private BluetoothAdapter mBluetoothAdapter;
    private static BluetoothScan singleton;
    private Advertisement advert;
    private ScanSettings settings;
    final ArrayList<ScanFilter> filters;

    private boolean scanStarted;
    private boolean killBluetooth;


    private BroadcastReceiver mReceiver;
    private BroadcastReceiver leScanReceiver;

    private BluetoothScan(Activity act) {
        this.act = act;
        advert = new Advertisement(act.getApplicationContext());

        BluetoothManager manager = (BluetoothManager) act.getSystemService(act.BLUETOOTH_SERVICE);
        this.mBluetoothAdapter = manager.getAdapter();
        UUIDFilter uuidFilter = new UUIDFilter();

        filters = new ArrayList<>();
        filters.add(uuidFilter.getScanFilterFromUUIDs("20CAE8A0-A9CF-11E3-A5E2-0800200C9A66"));
        filters.add(uuidFilter.getScanFilterFromUUIDs("B9407F30-F5F8-466E-AFF9-25556B57FE6D"));

        settings = new ScanSettings.Builder().setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY).build();


        init();
    }

    public static BluetoothScan getBtScan(Activity act){
        synchronized (BluetoothScan.class){
            if(singleton == null)
                singleton = new BluetoothScan(act);
            return singleton;
        }
    }

    private void init(){
        scanStarted = false;
        killBluetooth = false;

        if(!mBluetoothAdapter.isEnabled()) {
            mReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    final String action = intent.getAction();

                    if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                        final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,
                                BluetoothAdapter.ERROR);
                        switch (state) {
                            case BluetoothAdapter.STATE_OFF:
                                Log.d(TAG, "Bt OFF.");
                                turnOnBluetooth();
                                break;
                            case BluetoothAdapter.STATE_TURNING_OFF:
                                Log.d(TAG, "Bt turning OFF.");
                                turnOnBluetooth();
                                break;
                            case BluetoothAdapter.STATE_ON:
                                Log.d(TAG, "Bt ON.");
                                mBluetoothAdapter.getBluetoothLeScanner();
                                scanLeDevice(filters);
                                break;
                            case BluetoothAdapter.STATE_TURNING_ON:
                                Log.d(TAG, "Bt turning ON.");
                                break;
                        }
                    }
                }
            };
            // Register for broadcasts on BluetoothAdapter state change
            IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
//            act.registerReceiver(mReceiver,filter);
            LocalBroadcastManager.getInstance(act.getApplicationContext()).registerReceiver(mReceiver, filter);
        } else {

            scanLeDevice(filters);
//            // Register for broadcasts on BluetoothAdapter state change
//            Intent intent = new Intent("measuring boolean changed");
//            intent.putExtra("startedMeasuring", isMeasuring());
//            LocalBroadcastManager.getInstance(activity).sendBroadcast(intent);
        }
    }

    private ScanCallback mScanCallback = new ScanCallback() {

        @Override
        public void onScanResult(int callbackType, ScanResult result) {
//            Log.d(TAG, "onScanResult" + " | callback->"+callbackType);
            processResult(result);
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
//            Log.d(TAG, "onBatchScanResults: "+results.size()+" results");
            for (ScanResult result : results) {
                processResult(result);
            }
        }

        @Override
        public void onScanFailed(int errorCode) {
            Log.w(TAG, "LE Scan Failed: " + errorCode);
        }

        private void processResult(ScanResult result) {
//            Log.d(TAG, "New LE Device: " + result.getDevice().getName() + " @ " + result.getRssi());

            /*
             * Create a new beacon from the list of obtains AD structures
             * and pass it up to the main thread if it is not already listed in OnyxBeacon.onyxBeaconHashmap
             */
//            OnyxBeacon beacon = advert.extractAD(result.getDevice().getAddress(), result.getRssi(), result.getScanRecord().getBytes());
//
//            if(beacon != null)
//                beacon.checkState();

            Message msg = Message.obtain();
            msg.obj = result;
            scanHandler.sendMessage(msg);
        }
    };

    private Handler scanHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            ScanResult result = (ScanResult) msg.obj;
            advert.extractAD(result.getDevice().getAddress(), result.getRssi(), result.getScanRecord().getBytes());
        }
    };



    public void turnOnBluetooth(){
        Log.d(TAG, "method turn on bt");
        if(!mBluetoothAdapter.isEnabled())
            mBluetoothAdapter.enable();
    }

    private void scanLeDevice(final ArrayList<ScanFilter> filter) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!killBluetooth) {
                    if (scanStarted) {
                        mBluetoothAdapter.getBluetoothLeScanner().startScan(filter, settings, mScanCallback);
                        scanStarted = false;
                    } else {
                        mBluetoothAdapter.getBluetoothLeScanner().stopScan(mScanCallback);
                        scanStarted = true;
                    }
                    new Handler().postDelayed(this, 1100);
                }
            }
        }, 0);
    }

    private void killBluetooth(){
        scanStarted = false;
        try {
            if(mBluetoothAdapter.getBluetoothLeScanner() != null)
                mBluetoothAdapter.getBluetoothLeScanner().stopScan(mScanCallback);
            act.unregisterReceiver(mReceiver);
        } catch(IllegalArgumentException | IllegalStateException e){
            Log.e(TAG,e.toString());
        }
        mBluetoothAdapter.disable();
    }

    public void disableBt(){
        killBluetooth = true;
        killBluetooth();
    }

    private class UUIDFilter {
        private ScanFilter getScanFilterFromUUIDs(String uuid){
            // Empty data
            byte[] manData = new byte[]{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};

            // Data Mask
            byte[] mask = new byte[]{0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0,0,0,0};

            // Copy UUID into data array and remove all "-"
            System.arraycopy(hexStringToByteArray(uuid.replace("-", "")), 0, manData, 2, 16);

            // Add data array to filters
            ScanFilter filter = new ScanFilter.Builder().setManufacturerData(76, manData, mask).build();
            return filter;
        }

        public byte[] hexStringToByteArray(String s) {
            int len = s.length();
            byte[] data = new byte[len / 2];
            for (int i = 0; i < len; i += 2) {
                data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                        + Character.digit(s.charAt(i+1), 16));
            }
            return data;
        }

    }

}
