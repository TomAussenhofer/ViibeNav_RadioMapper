package de.beacon.tom.viibenav_radiomapper.model;

import android.util.Log;

import java.util.ArrayList;

import de.beacon.tom.viibenav_radiomapper.controller.TestAreaActivity;
import de.beacon.tom.viibenav_radiomapper.model.position.MacToMedian;
import de.beacon.tom.viibenav_radiomapper.model.position.PositionAlgorithm;
import de.beacon.tom.viibenav_radiomapper.model.position.neighbor.Ewknn;


/**
 * Created by TomTheBomb on 23.07.2015.
 */
public class Person {

    private static final String TAG = "Person";

    private Coordinate coord;
    private Measurement measurement;
    private TestAreaActivity test;

    private PositionAlgorithm algorithm;

    public Person(TestAreaActivity test) {
        this.test = test;
        coord = new Coordinate(-1,-1,-1);
        measurement = new Measurement();

        algorithm = new Ewknn();
    }

    public void getMostLikelyPosition(){
        ArrayList<OnyxBeacon> surrounding = OnyxBeacon.filterSurroundingBeacons();
        getOnTheFlyMedians(surrounding);

    }

    public void getOnTheFlyMedians(ArrayList<OnyxBeacon> surrounding){
        Log.d(TAG, "SURROUNDING " + surrounding.size());
        ArrayList<Integer> supposedAnchorPointIds = new ArrayList<>();

        // START MEASURING
        measurement.setState(Measurement.State.isMeasuring);
        for(OnyxBeacon tmp : surrounding) {
            if (!tmp.isMeasurementStarted())
                tmp.setMeasurementStarted(true);
        }
        measurement.overallOnTheFlyCalcProcess(surrounding, this);
    }

    public void estimatePos(MacToMedian[] data){
        setCoord(getAlgorithm().estimatePos(data));
        getTest().updateLikelyCoordsView();
    }

    public void checkLoop(){
        if(isLoop())
            getMostLikelyPosition();
    }

    private boolean isLoop(){
        return test.isLoopTest();
    }

    private Coordinate getCoordFromAnchorId(int id){
        return Database.getDB().getCoordFromAnchorId(id);
    }

    public void setCoord(Coordinate coord) {
        this.coord = coord;
    }

    public Coordinate getCoord() {
        return coord;
    }

    public TestAreaActivity getTest() {
        return test;
    }

    public Measurement getMeasurement() {
        return measurement;
    }

    public PositionAlgorithm getAlgorithm() {
        return algorithm;
    }
}
