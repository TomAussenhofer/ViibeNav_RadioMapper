package de.beacon.tom.viibenav_radiomapper.model;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;

import de.beacon.tom.viibenav_radiomapper.controller.MainActivity;
import de.beacon.tom.viibenav_radiomapper.model.fragment.SecondMeasureDialog;
import de.beacon.tom.viibenav_radiomapper.model.position.MacToMedian;


/**
 * Created by TomTheBomb on 12.07.2015.
 */
public class Measurement {

    public static final String TAG = "Measurement";

    public MainActivity main;
    public Measurement.State state;

    public enum State{
        isMeasuring,notMeasuring;
    };

    private int measurementSize;
    private boolean firstMeasure;
    private long start;
    private SensorHelper sh;

    public void overallCalcProgress(final long start, final ArrayList<OnyxBeacon> beacons, MainActivity main, boolean firstMeasure){
            this.main = main;
            this.start = start;
            this.firstMeasure = firstMeasure;
            this.sh = SensorHelper.getSensorHelper(main.getApplicationContext());

            measurementSize = beacons.size();
        new AsyncMeasure().execute(beacons);
    }

    public class AsyncMeasure extends AsyncTask<ArrayList<OnyxBeacon>, Integer, String> {

        ArrayList<OnyxBeacon> beacons;
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(main);
            dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            dialog.setTitle("Messung von " + measurementSize + " Beacons");
            dialog.setMax(measurementSize);
            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    cleanBeacons();
                    RadioMap.getRadioMap().deleteLastAnchor();
                    main.getApplicationUI().updateLayer1();
                    cleanInfo();
                }
            });
            dialog.show();
        }

        @Override
        protected String doInBackground(ArrayList<OnyxBeacon>... params) {
            ArrayList<OnyxBeacon> beacons = params[0];
            this.beacons = (ArrayList<OnyxBeacon>)beacons.clone();
            while(isMeasuring()) {
                Iterator<OnyxBeacon> it = beacons.iterator();
                while (it.hasNext()) {
                    if (it.next().isMeasurementDone()) {
                        publishProgress(1);
                        it.remove();
                    }
                }
                // break out if list is empty = all calcs are done
                if(beacons.isEmpty())
                    setState(State.notMeasuring);
            }
            dialog.dismiss();

            return "";
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            dialog.incrementProgressBy(values[0]);
        }

        @Override
        protected void onPostExecute(String result) {
                long ende = System.currentTimeMillis()-start;
                Log.d(TAG, "Dauer: " + ende / 1000 + "s");

                if(firstMeasure) {
                    Fingerprint fingerprint = new Fingerprint(RadioMap.getRadioMap().getCoordinate());
                    fingerprint.setBeacToOrientation(Util.cloneBeacons(beacons), sh.getOrientationFromDegree());
                    fingerprint.setInfo(main.getApplicationUI().getInfo());

                    RadioMap.add(fingerprint);

                    Boolean frontMeasuredFirst = false;
                    if(fingerprint.getFront() != null)
                        frontMeasuredFirst = true;
                    else if (fingerprint.getBack() != null)
                        frontMeasuredFirst = false;

                    if(beacons.size()<Definitions.MIN_BEACONS_FOR_MEASURE)
                        Toast.makeText(main,"Zu wenig beacons! beacons: "+Definitions.MIN_BEACONS_FOR_MEASURE, Toast.LENGTH_SHORT).show();



                    SecondMeasureDialog secondMeasureDialog = new SecondMeasureDialog();
                    Bundle b = new Bundle();
                    b.putBoolean("frontMeasuredFirst",frontMeasuredFirst);
                    secondMeasureDialog.setArguments(b);
                    secondMeasureDialog.show(main.getFragmentManager(), "dialog");
                    cleanBeacons();
                } else {
                    Log.d(TAG, "Second measurement");
                    Fingerprint a = RadioMap.getLastAnchor();

                    a.setBeacToOrientation(Util.cloneBeacons(beacons), sh.getOrientationFromDegree());
                    if(a.isFrontAndBackSet())
                        Database.getDB().addFingerprint(a);


                    main.getApplicationUI().updateLayer1();

                    cleanInfo();
                    cleanBeacons();
                }
        }

        private void cleanBeacons(){
            if(beacons != null)
                for(OnyxBeacon b : this.beacons)
                    b.resetMedianMeasurement();
        }

        private void cleanInfo(){
            main.getApplicationUI().getInfo().reset();
        }


    }

    public void setState(State state) {
        Log.d(TAG, "STATE: " + state);
        this.state = state;
    }

    public State getState() {
        return state;
    }

    public boolean isMeasuring(){
       if(getState().equals(State.isMeasuring))
           return true;
        return false;
    }




    // AREA FOR ON THE FLY MEASUREMENT

    private Person person;

    /**
     * Only Call this function for on the Fly measurement to identify position - NOT for saving data in the RadioMap (For saving data to RadioMap use overallCalcProcess())
     * @param beacons
     *
     */
    public void overallOnTheFlyCalcProcess(final ArrayList<OnyxBeacon> beacons, Person person){
        this.person = person;
        this.measurementSize = beacons.size();

        new AsyncOnTheFlyMeasure().execute(beacons);
    }

    public class AsyncOnTheFlyMeasure extends AsyncTask<ArrayList<OnyxBeacon>, Integer, String> {

        ArrayList<OnyxBeacon> beacons;
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(person.getTest());
            dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            dialog.setTitle("Messung von " + measurementSize + " Beacons");
            dialog.setMax(measurementSize);
            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    cleanUp();
                }
            });
            dialog.show();
        }

        @Override
        protected String doInBackground(ArrayList<OnyxBeacon>... params) {
            ArrayList<OnyxBeacon> beacons = params[0];
            this.beacons = (ArrayList<OnyxBeacon>)beacons.clone();
            while(isMeasuring()) {
                Iterator<OnyxBeacon> it = beacons.iterator();
                while (it.hasNext()) {
                    OnyxBeacon tmp = it.next();
                    if (tmp.isMeasurementDone()) {
                        publishProgress(1);
                        it.remove();
                    }
                    if(beacons.isEmpty())
                        setState(State.notMeasuring);
//                    Log.d(TAG, "MeasurementDone: "+tmp.isMeasurementDone());
                }
                // break out if list is empty = all calcs are done

            }
            dialog.dismiss();

            return "";
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            dialog.incrementProgressBy(values[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d(TAG,"ONPOST EXECUTE");

            MacToMedian[] data = MacToMedian.listToMacToMedianArr(beacons,SensorHelper.getSensorHelper(main).getOrientationFromDegree());
            person.estimatePos(data);
            cleanUp();
            person.checkLoop();
        }

        private void cleanUp(){
            for(OnyxBeacon b : this.beacons)
                b.resetMedianMeasurement();

        }



    }

}
