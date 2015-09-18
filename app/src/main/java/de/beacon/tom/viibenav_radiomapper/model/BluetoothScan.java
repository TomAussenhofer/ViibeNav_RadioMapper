package de.beacon.tom.viibenav_radiomapper.model;


import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.util.Log;

import java.util.List;


/**
 * Created by TomTheBomb on 23.06.2015.
 */
public class BluetoothScan {
    public static final String TAG = "BluetoothScan";

    private Activity act;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothLeScanner mBluetoothLeScanner;

    private static BluetoothScan singleton;
    private Advertisement advert;
    private BroadcastReceiver mReceiver;
    private ScanSettings settings;
    private boolean scanStarted;
    private boolean killBluetooth;

    private BluetoothScan(Activity act) {
        this.act = act;
        advert = new Advertisement(act.getApplicationContext());

        BluetoothManager manager = (BluetoothManager) act.getSystemService(act.BLUETOOTH_SERVICE);
        this.mBluetoothAdapter = manager.getAdapter();
        this.mBluetoothLeScanner = mBluetoothAdapter.getBluetoothLeScanner();

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
                            if(!killBluetooth)
                                startScan();
                            else
                                killBluetooth();
                            break;
                        case BluetoothAdapter.STATE_TURNING_ON:
                            Log.d(TAG, "Bt turning ON.");
                            if(killBluetooth)
                                killBluetooth();
                            break;
                    }
                }
            }
        };
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

        turnOnBluetooth();
        // Register for broadcasts on BluetoothAdapter state change
        IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        act.registerReceiver(mReceiver, filter);
    }

    public void onResumeOperation(){
        init();
    }

    private void startScan(){
//        Log.d(TAG, " method startScan");
        // Scan for devices advertising the thermometer
        // does not work
//        ScanFilter beaconFilter = new ScanFilter.Builder().setServiceUuid(ParcelUuid.fromString(Advertisement.filterUUID)).build();
//        // works
//        ScanFilter filter2 = new ScanFilter.Builder().setDeviceAddress(tagMAC).build();
//        ArrayList<ScanFilter> filters = new ArrayList<ScanFilter>();
//        // does not work
//         filters.add(beaconFilter);
//        filters.add(filter2);

        mBluetoothLeScanner = mBluetoothAdapter.getBluetoothLeScanner();
        settings = new ScanSettings.Builder().setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY).build();
//        Log.d(TAG,"Bluetooth enabled: "+mBluetoothAdapter.isEnabled() + " Try to start scanning...");
        scanLeDevice();

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
            OnyxBeacon beacon = advert.extractAD(result.getDevice().getAddress(), result.getRssi(), result.getScanRecord().getBytes());

            if(beacon != null)
                beacon.checkState();

//            Message msg = Message.obtain();
//            msg.obj = beacon;
//            mHandler.sendMessage(msg);
        }
    };

    public void turnOnBluetooth(){
        /*
         * We need to enforce that Bluetooth is first enabled, and take the
         * user to settings to enable it if they have not done so.
         */
//        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
//            //Bluetooth is disabled
////            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
////            startActivity(enableBtIntent);
//            mBluetoothAdapter.enable();
//        }

        Log.d(TAG, "method turn on bt");
        if(!mBluetoothAdapter.isEnabled())
            mBluetoothAdapter.enable();
        else
            startScan();
    }

    private void scanLeDevice() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (scanStarted) {
                        mBluetoothLeScanner.startScan(null, settings, mScanCallback);
                        scanStarted = false;
                } else {
                    if(!killBluetooth) {
                        mBluetoothLeScanner.stopScan(mScanCallback);
                        scanStarted = true;
                    }
                }

                if (!killBluetooth)
                    new Handler().postDelayed(this, 400);
                else
                    killBluetooth();
            }
        }, 0);
    }

    public BluetoothAdapter getmBluetoothAdapter() {
        return mBluetoothAdapter;
    }

    private void killBluetooth(){
        scanStarted = false;
        try {
            if(mBluetoothLeScanner != null)
                mBluetoothLeScanner.stopScan(mScanCallback);
            act.unregisterReceiver(mReceiver);
        } catch(IllegalArgumentException | IllegalStateException e){
            Log.e(TAG,e.toString());
        }
        mBluetoothAdapter.disable();
    }

    public void disableBt(){
        killBluetooth = true;
    }

    public void setKillBluetooth(boolean killBluetooth) {
        this.killBluetooth = killBluetooth;
    }
}
