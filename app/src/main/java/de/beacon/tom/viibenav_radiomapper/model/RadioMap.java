package de.beacon.tom.viibenav_radiomapper.model;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by TomTheBomb on 25.06.2015.
 */
public class RadioMap {

    private static RadioMap singleton;

    private Coordinate coordinate;

    private static ArrayList<AnchorPoint> data;
    private AnchorPoint lastAnchor;

    private RadioMap() {
        this.data = new ArrayList<AnchorPoint>();
        this.coordinate = new Coordinate(0,0,0);
    }

    public static RadioMap createRadioMap(){
        // Avoid possible errors with multiple threads accessing this method -> synchronized
        synchronized(RadioMap.class) {
            if (singleton == null) {
                singleton = new RadioMap();
            }
        }
        return singleton;
    }

    public static RadioMap getRadioMap(){
        return singleton;
    }

    public static void add(AnchorPoint a){
        data.add(a);
    }

    public static void remove(Coordinate coordinate){
        Iterator it = data.iterator();
        while(it.hasNext()){
            AnchorPoint tmp = (AnchorPoint) it.next();
            if(tmp.getCoordinate().equals(coordinate))
                it.remove();
        }
    }

    public static int size(){
        return data.size();
    }

    public static AnchorPoint getLastAnchor() {
        if(data!=null && data.size() != 0)
            return data.get(data.size()-1);

        throw new NullPointerException("Last anchorpoint can not be determined - radiomap is empty.");
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    public double getPosition_x() {
        return coordinate.getX();
    }

    public double getPosition_y() {
        return coordinate.getY();
    }

    public void setPosition_x(int position_x) {
        coordinate.setX(position_x);
    }

    public void setPosition_y(int position_y) {
        coordinate.setX(position_y);
    }

    public void setY_up(){
        coordinate.setY_up();
    }

    public void setY_down(){
        coordinate.setY_down();
    }

    public void setX_up(){
        coordinate.setX_up();
    }

    public void setX_down(){
       coordinate.setX_down();
    }

    public double getFloor() {
        return coordinate.getFloor();
    }

    public static ArrayList<AnchorPoint> getData() {
        return data;
    }

    public void setFloor(int floor) {
        coordinate.setFloor(floor);
    }
}
