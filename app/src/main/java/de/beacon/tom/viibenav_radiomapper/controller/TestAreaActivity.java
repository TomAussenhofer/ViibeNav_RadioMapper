package de.beacon.tom.viibenav_radiomapper.controller;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.concurrent.ScheduledExecutorService;

import de.beacon.tom.viibenav_radiomapper.R;
import de.beacon.tom.viibenav_radiomapper.model.Connector;
import de.beacon.tom.viibenav_radiomapper.model.Database;
import de.beacon.tom.viibenav_radiomapper.model.Person;
import de.beacon.tom.viibenav_radiomapper.model.RadioMap;
import de.beacon.tom.viibenav_radiomapper.model.adapter.CustomListFingerprint_has_MedianAdapter;
import de.beacon.tom.viibenav_radiomapper.model.adapter.CustomListFingerprintAdapter;
import de.beacon.tom.viibenav_radiomapper.model.adapter.CustomListBeaconAdapter;
import de.beacon.tom.viibenav_radiomapper.model.adapter.CustomListInfoAdapter;
import de.beacon.tom.viibenav_radiomapper.model.adapter.CustomListMedianAdapter;
import de.beacon.tom.viibenav_radiomapper.model.dbmodels.FingerprintView;
import de.beacon.tom.viibenav_radiomapper.model.dbmodels.Fingerprint_has_MedianView;
import de.beacon.tom.viibenav_radiomapper.model.dbmodels.InfoView;
import de.beacon.tom.viibenav_radiomapper.model.dbmodels.MedianView;
import de.beacon.tom.viibenav_radiomapper.model.dbmodels.OnyxBeaconView;


/**
 * Created by TomTheBomb on 21.07.2015.
 */
public class TestAreaActivity extends Activity implements AdapterView.OnItemSelectedListener {

    private static final String TAG = "TestAreaActivity";


    private ScheduledExecutorService exec;

    Person person;
    Button test;
    String[] tables;
    Spinner spinner;
    private int selectedItem;


    CustomListFingerprintAdapter customListFingerprintAdapter;
    CustomListBeaconAdapter customListBeaconAdapter;
    CustomListMedianAdapter customListMedianAdapter;
    CustomListInfoAdapter customListInfoAdapter;
    CustomListFingerprint_has_MedianAdapter customListFingerprint_has_medianAdapter;

    ListView tableView;
    LinearLayout tableHeadView;

    TextView testCoords;
    CheckBox loop;

    boolean loopTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_test_area);


        customListFingerprintAdapter = new CustomListFingerprintAdapter(this);
        customListBeaconAdapter = new CustomListBeaconAdapter(this);
        customListMedianAdapter = new CustomListMedianAdapter(this);
        customListInfoAdapter = new CustomListInfoAdapter(this);
        customListFingerprint_has_medianAdapter = new CustomListFingerprint_has_MedianAdapter(this);

        tableView = (ListView) findViewById(R.id.tableView);
        tableHeadView = (LinearLayout) findViewById(R.id.tableHeadView);

        testCoords = (TextView) findViewById(R.id._testCoords);
        loop = (CheckBox) findViewById(R.id.loop);
        selectedItem = -1;

        initSpinner();

        loopTest = false;

        test = (Button) findViewById(R.id.test);
        person = new Person(this);

    }

    private void initSpinner(){
        tables = new String[5];
        tables[0] = "Fingerprint";
        tables[1] = "Beacon";
        tables[2] = "Median";
        tables[3] = "Info";
        tables[4] = "Fingerprint_has_Median";

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item, tables);

        spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(this);
    }


    public void startTest(View view){
        // start one time measuring
        Runnable r = new Runnable(){
            @Override
            public void run() {

                    getMostLikelyPositionHandler.sendEmptyMessage(0);

            }
        };
        Thread th = new Thread(r);
        th.start();

    }

    public void deleteTables(View view){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Delete all tables?");
            builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Database.getDB().deleteAllTables();
                    RadioMap.getData().clear();

                    // reload cleared List which was selected
                    switch(selectedItem){
                        case 0:
                            anchorListHandler.sendEmptyMessage(0);
                            break;
                        case 1:
                            beaconListHandler.sendEmptyMessage(0);
                            break;
                        case 2:
                            medianListHandler.sendEmptyMessage(0);
                            break;
                        case 3:
                            infoListHandler.sendEmptyMessage(0);
                            break;
                        case 4:
                            fingerprint_has_medianHandler.sendEmptyMessage(0);
                            break;
                    }

                    dialog.dismiss();
                    RadioMap.getRadioMap().deleteData();
                }
            });
            builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
    }

    public void updateLikelyCoordsView(){
        testCoords.setText("x: "+person.getCoord().getX()+" y: "+person.getCoord().getY()+" f: "+person.getCoord().getFloor());
    }

    private Handler getMostLikelyPositionHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            // clear Beacon map to make sure only beacons from the newest execution cycle are taken into account
//            OnyxBeacon.clearMap();
//            NOT SURE IF WORKS
            person.getMostLikelyPosition();
        }
    };

    public void triggerLoop(View view){
        if(loop.isChecked())
            loopTest = true;
        else
            loopTest = false;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int selected = spinner.getSelectedItemPosition();
        tableHeadView.removeAllViewsInLayout();
        switch(selected){
            case 0:
                tableView.setAdapter(customListFingerprintAdapter);
                tableHeadView.addView(getLayoutInflater().inflate(R.layout.testarea_anchors_custom_row, null),
                        tableHeadView.getLayoutParams().width,tableHeadView.getLayoutParams().height);
                anchorListHandler.sendEmptyMessage(0);
                selectedItem = 0;
                break;
            case 1:
                tableView.setAdapter(customListBeaconAdapter);
                tableHeadView.addView(getLayoutInflater().inflate(R.layout.testarea_beacons_custom_row, null),
                        tableHeadView.getLayoutParams().width, tableHeadView.getLayoutParams().height);
                beaconListHandler.sendEmptyMessage(0);
                selectedItem = 1;
                break;
            case 2:
              tableView.setAdapter(customListMedianAdapter);
                tableHeadView.addView(getLayoutInflater().inflate(R.layout.testarea_medians_custom_row, null),
                        tableHeadView.getLayoutParams().width, tableHeadView.getLayoutParams().height);
                medianListHandler.sendEmptyMessage(0);
                selectedItem = 2;
                break;
            case 3:
                tableView.setAdapter(customListInfoAdapter);
                tableHeadView.addView(getLayoutInflater().inflate(R.layout.testarea_info_custom_row, null),
                        tableHeadView.getLayoutParams().width, tableHeadView.getLayoutParams().height);
                infoListHandler.sendEmptyMessage(0);
                selectedItem = 3;
                break;
            case 4:
                tableView.setAdapter(customListFingerprint_has_medianAdapter);
                tableHeadView.addView(getLayoutInflater().inflate(R.layout.testarea_fingerprint_has_median_custom_row, null),
                        tableHeadView.getLayoutParams().width, tableHeadView.getLayoutParams().height);
                fingerprint_has_medianHandler.sendEmptyMessage(0);
                selectedItem = 4;
                break;
        }
    }

    private Handler anchorListHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Database.getDB().getAllFingerprints();

            customListFingerprintAdapter.clear();
            customListFingerprintAdapter.addAll(FingerprintView.getAll());
            customListFingerprintAdapter.notifyDataSetChanged();
        }
    };

    private Handler beaconListHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Database.getDB().getAllBeacons();

            customListBeaconAdapter.clear();
            customListBeaconAdapter.addAll(OnyxBeaconView.getAll());
            customListBeaconAdapter.notifyDataSetChanged();
        }
    };

    private Handler medianListHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Database.getDB().getAllMedians();

            customListMedianAdapter.clear();
            customListMedianAdapter.addAll(MedianView.getAll());
            customListMedianAdapter.notifyDataSetChanged();
        }
    };

    private Handler infoListHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Database.getDB().getAllInfo();

            customListInfoAdapter.clear();
            customListInfoAdapter.addAll(InfoView.getAll());
            customListInfoAdapter.notifyDataSetChanged();
        }
    };

    private Handler fingerprint_has_medianHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Database.getDB().getAllFingerprint_has_Medians();

            customListFingerprint_has_medianAdapter.clear();
            customListFingerprint_has_medianAdapter.addAll(Fingerprint_has_MedianView.getAll());
            customListFingerprint_has_medianAdapter.notifyDataSetChanged();
        }
    };

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    protected void onResume() {
        super.onResume();

        if(Connector.getConnector().WiFiEnabled())
            Connector.getConnector().disableWiFi();

    }

    public boolean isLoopTest() {
        return loopTest;
    }
}
