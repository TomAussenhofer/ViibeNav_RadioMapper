package de.beacon.tom.viibenav_radiomapper.model;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by TomTheBomb on 23.06.2015.
 */
public class Fingerprint {

    private Coordinate coordinate;
    private Info info;
    /**
     * contains an array of MacAddresses mapped to medians
     * 90 degrees - 270 degrees
     */
    private BeacToOrient front;

    /**
     * contains an array of MacAddresses mapped to medians
     * 0-90 degrees and 270-360 degrees
     */
    private BeacToOrient back;

    public Fingerprint(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

//    public void setBeacToOrientation(HashMap<CharBuffer, OnyxBeacon> input){
//        if(UserOrientation.getOrientationFromDegree(orientation).equals(Orientation.front))
//            setFront(BeaconsToOrient.getBeaconsArrToOrient(input, UserOrientation.getOrientationFromSensorHelper()));
//        else if(UserOrientation.getOrientationFromSensorHelper().equals(Orientation.back))
//            setBack(BeaconsToOrient.getBeaconsArrToOrient(input,UserOrientation.getOrientationFromSensorHelper()));
//    }

    public void setBeacToOrientation(ArrayList<OnyxBeacon> input, Orientation currentOrientation){
        Log.d("Measurement", "Orientation when setting: "+currentOrientation);
        if(currentOrientation.equals(Orientation.front))
            front = new BeacToOrient(input, currentOrientation);
        else if(currentOrientation.equals(Orientation.back))
            back = new BeacToOrient(input, currentOrientation);
    }

    public boolean isFrontAndBackSet(){
        if(front != null && back != null)
            return true;
        return false;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public BeacToOrient getFront() {
        return front;
    }

    public BeacToOrient getBack() {
        return back;
    }

    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }
}
