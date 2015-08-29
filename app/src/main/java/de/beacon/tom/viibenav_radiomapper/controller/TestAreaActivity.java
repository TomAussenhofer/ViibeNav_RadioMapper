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
import de.beacon.tom.viibenav_radiomapper.model.DBHandler;
import de.beacon.tom.viibenav_radiomapper.model.Person;
import de.beacon.tom.viibenav_radiomapper.model.RadioMap;
import de.beacon.tom.viibenav_radiomapper.model.adapter.CustomListAnchorAdapter;
import de.beacon.tom.viibenav_radiomapper.model.adapter.CustomListBeaconAdapter;
import de.beacon.tom.viibenav_radiomapper.model.adapter.CustomListInfoAdapter;
import de.beacon.tom.viibenav_radiomapper.model.adapter.CustomListMedianAdapter;
import de.beacon.tom.viibenav_radiomapper.model.dbmodels.AnchorPointDBModel;
import de.beacon.tom.viibenav_radiomapper.model.dbmodels.InfoDBModel;
import de.beacon.tom.viibenav_radiomapper.model.dbmodels.MedianDBModel;
import de.beacon.tom.viibenav_radiomapper.model.dbmodels.OnyxBeaconDBModel;


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


    CustomListAnchorAdapter customListAnchorAdapter;
    CustomListBeaconAdapter customListBeaconAdapter;
    CustomListMedianAdapter customListMedianAdapter;
    CustomListInfoAdapter customListInfoAdapter;

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


        customListAnchorAdapter = new CustomListAnchorAdapter(this);
        customListBeaconAdapter = new CustomListBeaconAdapter(this);
        customListMedianAdapter = new CustomListMedianAdapter(this);
        customListInfoAdapter = new CustomListInfoAdapter(this);

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
        tables = new String[4];
        tables[0] = "AnchorPoint";
        tables[1] = "Beacon";
        tables[2] = "Median";
        tables[3] = "Info";

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item, tables);

        spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(this);
    }


    public void startTest(View view){
//        Runnable r = new Runnable(){
//            @Override
//            public void run() {
//                exec = Executors.newSingleThreadScheduledExecutor();
//                exec.scheduleAtFixedRate(new Runnable() {
//                    @Override
//                    public void run() {
//                        getMostLikelyPositionHandler.sendEmptyMessage(0);
//                    }
//                }, 0, 1100, TimeUnit.MILLISECONDS);
//            }
//        };
//        Thread th = new Thread(r);
//        th.start();



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
                    DBHandler.getDB().deleteAllTables();
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
                    }

                    dialog.dismiss();
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
                tableView.setAdapter(customListAnchorAdapter);
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
        }
    }

    private Handler anchorListHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            DBHandler.getDB().getAllAnchors();

            customListAnchorAdapter.clear();
            customListAnchorAdapter.addAll(AnchorPointDBModel.getAllAnchors());
            customListAnchorAdapter.notifyDataSetChanged();
        }
    };

    private Handler beaconListHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            DBHandler.getDB().getAllBeacons();

            customListBeaconAdapter.clear();
            customListBeaconAdapter.addAll(OnyxBeaconDBModel.getAllBeacons());
            customListBeaconAdapter.notifyDataSetChanged();
        }
    };

    private Handler medianListHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            DBHandler.getDB().getAllMedians();

            customListMedianAdapter.clear();
            customListMedianAdapter.addAll(MedianDBModel.getAllMedians());
            customListMedianAdapter.notifyDataSetChanged();
        }
    };

    private Handler infoListHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            DBHandler.getDB().getAllInfo();

            customListInfoAdapter.clear();
            customListInfoAdapter.addAll(InfoDBModel.getAllInfo());
            customListInfoAdapter.notifyDataSetChanged();
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
