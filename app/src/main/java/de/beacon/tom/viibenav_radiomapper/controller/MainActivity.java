package de.beacon.tom.viibenav_radiomapper.controller;

import android.app.DialogFragment;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import de.beacon.tom.viibenav_radiomapper.R;
import de.beacon.tom.viibenav_radiomapper.model.BluetoothScan;
import de.beacon.tom.viibenav_radiomapper.model.Database;
import de.beacon.tom.viibenav_radiomapper.model.ExportImportDB;
import de.beacon.tom.viibenav_radiomapper.model.RadioMap;
import de.beacon.tom.viibenav_radiomapper.model.SensorHelper;
import de.beacon.tom.viibenav_radiomapper.model.WiFiConnector;
import de.beacon.tom.viibenav_radiomapper.model.fragment.SettingsDialog;


public class MainActivity extends ViibeActivity implements SensorEventListener {

    public static final String TAG = "MainActivity";

    private Application applicationUI;
    private BluetoothScan btScan;
    private ExportImportDB exportImport;
    private SensorHelper sh;

    boolean leaveToNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_main);

    }

    private void init(){
        btScan = BluetoothScan.getBtScan(this);
        btScan.onResumeOperation();

        applicationUI = new Application(this);
        exportImport = new ExportImportDB(this);
        RadioMap.getRadioMap();
        Database.createDB(this, null, null, 1);
        WiFiConnector.getConnector(this);

        sh = SensorHelper.getSensorHelper(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    public void exportClicked(View view){
        exportImport.exportDB(Database.getDB().getDBPath());
    }

    public void importClicked(View view){
        exportImport.importDB();
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

        init();
        SensorHelper.getSensorHelper(this).onResumeOperation(this);

    }

    @Override
    protected void onPause() {
        super.onPause();
        SensorHelper.getSensorHelper(this).onPauseOperation(this);
    }

    @Override
    public void onHide() {
        super.onHide();
    }

    private void cleanUp(){
        // When application is paused turn on WiFi again

    }

    public Application getApplicationUI() {
        return applicationUI;
    }

}
