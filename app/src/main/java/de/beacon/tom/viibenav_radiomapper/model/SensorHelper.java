package de.beacon.tom.viibenav_radiomapper.model;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Message;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import de.beacon.tom.viibenav_radiomapper.controller.MainActivity;

/**
 * Created by Dima on 04/08/2015.
 */
public class SensorHelper {
    SensorManager mSensorManager;
    Sensor mAccelerometer;
    Sensor mMagnetometer;
    float[] mLastAccelerometer = new float[3];
    float[] mLastMagnetometer = new float[3];
    boolean mLastAccelerometerSet = false;
    boolean mLastMagnetometerSet = false;
    float[] mR = new float[9];
    float[] mOrientation = new float[3];
    float mCurrentDegree = 0f;

    int meter = 0;
    int grad = 0;
    TextView degreeTV;
    ImageView arrowImage;

    private boolean calcInProgress = false;
    private long startTime = 0;
    private long curTime = 0;
    private boolean timeToAddValue = false;
    private ArrayList<Float> degreeValuesForMedian = new ArrayList<>();

    /**
     * Ausrichtung des Smartphones zum Nordpol
     */
    private static int orientation = 0;

    public static int getOrientation() {
        return orientation;
    }

    private ScheduledExecutorService exec;

    public SensorHelper(Context c, ImageView arrowImage, TextView degreeTV) {
        mSensorManager = (SensorManager) c.getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        this.arrowImage = arrowImage;
        this.degreeTV = degreeTV;
        meter = 0;
        grad = 0;
        String text =  grad + "\u00B0";
        this.degreeTV.setText(text);

        Runnable r = new Runnable(){
            @Override
            public void run() {
                exec = Executors.newSingleThreadScheduledExecutor();
                exec.scheduleAtFixedRate(new Runnable() {
                    @Override
                    public void run() {
//                        Log.d("Measurement", OnyxBeacon.getBeaconMapAsList().size() + "");
                        handler.sendEmptyMessage(0);
                    }
                }, 0, 750, TimeUnit.MILLISECONDS);

            }
        };
        Thread th = new Thread(r);
        th.start();
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            String text =  grad + "\u00B0";
            String orientationStr = UserOrientation.getOrientationFromSensorHelper().toString();
            degreeTV.setText(text + "|" + orientationStr);
        }
    };

    public void onResumeOperation(MainActivity n) {
        mSensorManager.registerListener(n, mAccelerometer, SensorManager.SENSOR_DELAY_GAME);
        mSensorManager.registerListener(n, mMagnetometer, SensorManager.SENSOR_DELAY_GAME);
    }

    public void onPauseOperation(MainActivity n) {
        mSensorManager.unregisterListener(n, mAccelerometer);
        mSensorManager.unregisterListener(n, mMagnetometer);
    }

    public void onSensorChangedOperation(SensorEvent event) {
        if (event.sensor == mAccelerometer) {
            System.arraycopy(event.values, 0, mLastAccelerometer, 0, event.values.length);
            mLastAccelerometerSet = true;
        } else if (event.sensor == mMagnetometer) {
            System.arraycopy(event.values, 0, mLastMagnetometer, 0, event.values.length);
            mLastMagnetometerSet = true;
        }
        if (mLastAccelerometerSet && mLastMagnetometerSet) {
            SensorManager.getRotationMatrix(mR, null, mLastAccelerometer, mLastMagnetometer);
            SensorManager.getOrientation(mR, mOrientation);
            float azimuthInRadians = mOrientation[0];
            float azimuthInDegrees = (float) (Math.toDegrees(azimuthInRadians) + 360) % 360;
            animateImage(azimuthInDegrees);
        }
    }

    private void animateImage(float degrees) {
        RotateAnimation ra = new RotateAnimation(
                mCurrentDegree,
                -degrees,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f);

        ra.setDuration(250);
        ra.setFillAfter(true);

        arrowImage.startAnimation(ra);

        mCurrentDegree = -degrees;
        grad = (int) -mCurrentDegree;
        orientation = grad;

    }



    public ScheduledExecutorService getExec() {
        return exec;
    }
}
