package de.beacon.tom.viibenav_radiomapper.controller;

import android.app.Activity;
import android.app.DialogFragment;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import de.beacon.tom.viibenav_radiomapper.R;
import de.beacon.tom.viibenav_radiomapper.model.DBHandler;
import de.beacon.tom.viibenav_radiomapper.model.RadioMap;
import de.beacon.tom.viibenav_radiomapper.model.fragment.SettingsDialog;


public class MainActivity extends Activity implements SensorEventListener {


    Application applicationUI;
    BluetoothScan bluetoothScan;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.activity_main);

        RadioMap.createRadioMap();
        DBHandler.createDB(this, null, null, 1);
        Connector.createConnector((WifiManager) getSystemService(this.WIFI_SERVICE));

        applicationUI = new Application(this);

        BluetoothManager manager = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
        bluetoothScan = new BluetoothScan(applicationUI,manager.getAdapter());

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    public void prefsClicked(View view){ applicationUI.prefsClicked(view); }

    public void clickUp(View view){
        applicationUI.clickUp(view);
    }

    public void clickRight(View view){
        applicationUI.clickRight(view);
    }

    public void clickDown(View view){
        applicationUI.clickDown(view);
    }

    public void clickLeft(View view){
        applicationUI.clickLeft(view);
    }

    public void clickInfo(View view){
        applicationUI.clickInfo(view);
    }

    public void createInfo(View view) { applicationUI.createInfo(view); }

    /**
     * This method delegates the UI order to start median measurement to application layer
     */
    public void startMeasurement(View view){
        applicationUI.startMeasurement(view);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        applicationUI.onSensorChangedOperation(event);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) { }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch(id){
            case R.id.action_settings:
                DialogFragment dialogFrag = new SettingsDialog();
                dialogFrag.show(getFragmentManager(), "MySettingsDialogFg");
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

        applicationUI.onResumeOperation(this);

        // Turn Off WiFi signals on activity start as it mitigates position estimation
        if(Connector.getConnector().WiFiEnabled())
            Connector.getConnector().disableWiFi();

        /*
         * We need to enforce that Bluetooth is first enabled, and take the
         * user to settings to enable it if they have not done so.
         */
        if (bluetoothScan.getmBluetoothAdapter() == null || !bluetoothScan.getmBluetoothAdapter().isEnabled()) {
            //Bluetooth is disabled
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(enableBtIntent);
            finish();
            return;
        }

        /*
         * Check for Bluetooth LE Support.  In production, our manifest entry will keep this
         * from installing on these devices, but this will allow test devices or other
         * sideloads to report whether or not the feature exists.
         */
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, "No LE Support.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        //Begin scanning for LE devices
        bluetoothScan.startScan();
    }

    @Override
    protected void onPause() {
        super.onPause();

        applicationUI.onPauseOperation(this);

        // When application is paused turn on WiFi again
        if(!Connector.getConnector().WiFiEnabled())
            Connector.getConnector().enableWiFi();
    }

    public Application getApplicationUI() {
        return applicationUI;
    }


    public BluetoothScan getBluetoothScan() {
        return bluetoothScan;
    }


}
