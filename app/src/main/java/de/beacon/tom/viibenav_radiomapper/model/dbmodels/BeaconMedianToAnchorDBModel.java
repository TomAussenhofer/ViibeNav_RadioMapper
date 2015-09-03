package de.beacon.tom.viibenav_radiomapper.model.dbmodels;

/**
 * Created by TomTheBomb on 03.09.2015.
 */
public class BeaconMedianToAnchorDBModel {

    private int beacon_1, beacon_2, beacon_3, beacon_4, beacon_5, beacon_6;

    private static BeaconMedianToAnchorDBModel[] allBeaconMedianToAnchor;

    public BeaconMedianToAnchorDBModel(int beacon_1, int beacon_2, int beacon_3, int beacon_4, int beacon_5, int beacon_6) {
        this.beacon_1 = beacon_1;
        this.beacon_2 = beacon_2;
        this.beacon_3 = beacon_3;
        this.beacon_4 = beacon_4;
        this.beacon_5 = beacon_5;
        this.beacon_6 = beacon_6;
    }

    public static BeaconMedianToAnchorDBModel[] getAllBeaconMedianToAnchor() {
        return allBeaconMedianToAnchor;
    }

    public static void setAllBeaconMedianToAnchor(BeaconMedianToAnchorDBModel[] allBeaconMedianToAnchor) {
        BeaconMedianToAnchorDBModel.allBeaconMedianToAnchor = allBeaconMedianToAnchor;
    }

    public int getBeacon_1() {
        return beacon_1;
    }

    public int getBeacon_2() {
        return beacon_2;
    }

    public int getBeacon_3() {
        return beacon_3;
    }

    public int getBeacon_4() {
        return beacon_4;
    }

    public int getBeacon_5() {
        return beacon_5;
    }

    public int getBeacon_6() {
        return beacon_6;
    }
}
