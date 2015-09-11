package de.beacon.tom.viibenav_radiomapper.model;

import java.util.ArrayList;

/**
 * Created by TomTheBomb on 01.09.2015.
 *
 * Maps a specific BeaconArray to an orientation
 */
public class BeaconsToOrient {

    private OnyxBeacon[] beaconArray;
    private Orientation orientation;

    public BeaconsToOrient(OnyxBeacon[] beaconArray, Orientation orientation) {
        this.beaconArray = beaconArray;
        this.orientation = orientation;
    }

//    public BeaconsToOrient(HashMap<CharBuffer, OnyxBeacon> tmpBeaconMap, Orientation orientation) {
//        OnyxBeacon[] beaconArray = OnyxBeacon.getBeaconMapAsArr(tmpBeaconMap);
//
//        this.beaconArray = beaconArray;
//        this.orientation = orientation;
//    }
//
//    public static BeaconsToOrient getBeaconsArrToOrient(HashMap<CharBuffer,OnyxBeacon> tmpBeaconMap, Orientation orientation) {
//        OnyxBeacon[] beaconArray = OnyxBeacon.getBeaconMapAsArr(tmpBeaconMap);
//        return new BeaconsToOrient(beaconArray,orientation);
//    }

    public static BeaconsToOrient getBeaconsArrToOrient(ArrayList<OnyxBeacon> tmpBeacons, Orientation orientation) {
        OnyxBeacon[] beaconArray = tmpBeacons.toArray(new OnyxBeacon[tmpBeacons.size()]);
        return new BeaconsToOrient(beaconArray,orientation);
    }

    public OnyxBeacon[] getBeaconArray() {
        return beaconArray;
    }

    public void setBeaconArray(OnyxBeacon[] beaconArray) {
        this.beaconArray = beaconArray;
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;
    }
}
