package de.beacon.tom.viibenav_radiomapper.model;

import java.util.ArrayList;


/**
 * Created by TomTheBomb on 23.06.2015.
 */
public class AnchorPoint {

    private Coordinate coordinate;

    private AddInfo addInfo;

    /**
     * contains an array of MacAddresses mapped to medians
     * 90 degrees - 270 degrees
     */
    private BeaconsToOrient front;

    /**
     * contains an array of MacAddresses mapped to medians
     * 0-90 degrees and 270-360 degrees
     */
    private BeaconsToOrient back;

    public AnchorPoint(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

//    public void setMacToMedianWithOrientation(HashMap<CharBuffer, OnyxBeacon> input){
//        if(UserOrientation.getOrientationFromDegree(orientation).equals(Orientation.front))
//            setFront(BeaconsToOrient.getBeaconsArrToOrient(input, UserOrientation.getOrientationFromSensorHelper()));
//        else if(UserOrientation.getOrientationFromSensorHelper().equals(Orientation.back))
//            setBack(BeaconsToOrient.getBeaconsArrToOrient(input,UserOrientation.getOrientationFromSensorHelper()));
//    }

    public void setMacToMedianWithOrientation(ArrayList<OnyxBeacon> input, Orientation currentOrientation){
        if(currentOrientation.equals(Orientation.front))
            setFront(BeaconsToOrient.getBeaconsArrToOrient(input, currentOrientation));
        else if(currentOrientation.equals(Orientation.back))
            setBack(BeaconsToOrient.getBeaconsArrToOrient(input, currentOrientation));
    }

    public boolean isFrontAndBackSet(){
        if(front != null && back != null)
            return true;
        return false;
    }

    public boolean isFrontSet(){
        if( front != null)
            return true;
        else
            return false;
    }

    public boolean isBackSet(){
        if( back != null)
            return true;
        else
            return false;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public BeaconsToOrient getFront() {
        return front;
    }

    public void setFront(BeaconsToOrient front) {
        this.front = front;
    }

    public BeaconsToOrient getBack() {
        return back;
    }

    public void setBack(BeaconsToOrient back) {
        this.back = back;
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    public AddInfo getAddInfo() {
        return addInfo;
    }

    public void setAddInfo(AddInfo addInfo) {
        this.addInfo = addInfo;
    }
}
