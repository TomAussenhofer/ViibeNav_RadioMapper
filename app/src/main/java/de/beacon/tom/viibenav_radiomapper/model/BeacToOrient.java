package de.beacon.tom.viibenav_radiomapper.model;

import java.util.ArrayList;

/**
 * Created by TomTheBomb on 18.09.2015.
 */
public class BeacToOrient {

    private OnyxBeacon[] beaconArray;
    private Orientation orientation;

    public BeacToOrient(ArrayList<OnyxBeacon> beaconList, Orientation orientation) {
        this.beaconArray = beaconList.toArray(new OnyxBeacon[beaconList.size()]);
        this.orientation = orientation;
    }

    public OnyxBeacon[] getBeaconArray() {
        return beaconArray;
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;
    }

//    @Override
//    public String toString() {
//        String res = "{";
//        for (int i = 0; i < beaconArray.length; i++) {
//            if(i!=beaconArray.length-1)
//                res += beaconArray[i].getMedianRSSI()+",";
//            else
//                res += beaconArray[i].getMedianRSSI()+"}";
//        }
//        return res;
//    }
}
