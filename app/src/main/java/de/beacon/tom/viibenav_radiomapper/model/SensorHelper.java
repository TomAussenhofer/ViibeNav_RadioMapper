package de.beacon.tom.viibenav_radiomapper.model;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.ScheduledExecutorService;

import de.beacon.tom.viibenav_radiomapper.controller.MainActivity;

/**
 * Created by Dima on 04/08/2015.
 */
public class SensorHelper {

    private static final String TAG = "SensorHelper";

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

    private String degreeSign;
    int meter = 0;
    int grad = 0;

    private boolean calcInProgress = false;
    private long startTime = 0;
    private long curTime = 0;
    private boolean timeToAddValue = false;
    private ArrayList<Float> degreeValuesForMedian = new ArrayList<>();

    private float azimuthInDegrees;

    /**
     * Ausrichtung des Smartphones zum Nordpol
     */
    private static int orientation = 0;

    public static int getOrientation() {
        return orientation;
    }

    private ScheduledExecutorService exec;
    private static SensorHelper singleton;

    private SensorHelper(Context c) {
        mSensorManager = (SensorManager) c.getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        azimuthInDegrees = 0;
        grad = 0;
        degreeSign = "\u00B0";

    }

    public static SensorHelper getSensorHelper(Context c) {
        synchronized(SensorHelper.class){
            if(singleton == null)
                singleton = new SensorHelper(c);
        }
        return singleton;
    }

//

//    public void execScheduled() {
//
//        Runnable r = new Runnable() {
//            @Override
//            public void run() {
//                exec = Executors.newSingleThreadScheduledExecutor();
//                exec.scheduleAtFixedRate(new Runnable() {
//                    @Override
//                    public void run() {
////                        Boolean frontMeasuredFirst = null;
////                        Message msg = Message.obtain();
////                        msg.obj = beacon;
////                        mHandler.sendMessage(msg);
//                        handler.sendEmptyMessage(0);
//                    }
//                }, 0, 250, TimeUnit.MILLISECONDS);
//
//            }
//        };
//        Thread th = new Thread(r);
//        th.start();
//    }

//    private Handler handler = new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//
//            String text =  "";
//            String orientationStr = UserOrientation.getOrientationFromSensorHelper().toString();
//
//
//            if (frontMeasuredFirst == null){
//                text = grad + "\u00B0";
//            }else if(frontMeasuredFirst == 1){
//                if(UserOrientation.getOrientationFromSensorHelper().equals(Orientation.front))
//                    text =  "drehen!";
//                else if(UserOrientation.getOrientationFromSensorHelper().equals(Orientation.back))
//                    text = grad + "\u00B0";
//            } else if (frontMeasuredFirst == 0){
//                if(UserOrientation.getOrientationFromSensorHelper().equals(Orientation.front))
//                    text = grad + "\u00B0";
//                else if(UserOrientation.getOrientationFromSensorHelper().equals(Orientation.back))
//                    text =  "drehen!";
//            }
//
//            animateImage(azimuthInDegrees);
//        }
//    };

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
            azimuthInDegrees = (float) (Math.toDegrees(azimuthInRadians) + 360) % 360;
            orientation = (int) azimuthInDegrees;
        }
    }

    public void animateImage(ImageView arrowImage) {
        float degrees = azimuthInDegrees;
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
    }

    public void updateUI(TextView tv){
            tv.setText(orientation + degreeSign + "|" + UserOrientation.getOrientationFromSensorHelper());
    }

    public void updateUI(TextView tv,Button second_measure, boolean frontMeasuredFirst){
        if(frontMeasuredFirst) {
            if(UserOrientation.getOrientationFromSensorHelper().equals(Orientation.back)) {
                tv.setTextColor(Color.BLACK);
                tv.setText(orientation + degreeSign + "|" + UserOrientation.getOrientationFromSensorHelper());
                second_measure.setEnabled(true);
            } else {
                tv.setTextColor(Color.RED);
                tv.setText("drehen!");
                second_measure.setEnabled(false);
            }
        } else if(!frontMeasuredFirst){
            if(UserOrientation.getOrientationFromSensorHelper().equals(Orientation.front)) {
                tv.setTextColor(Color.BLACK);
                tv.setText(orientation + degreeSign + "|" + UserOrientation.getOrientationFromSensorHelper());
                second_measure.setEnabled(true);
            } else {
                tv.setTextColor(Color.RED);
                tv.setText("drehen!");
                second_measure.setEnabled(false);
            }
        }
    }

    public ScheduledExecutorService getExec() {
        return exec;
    }
}
