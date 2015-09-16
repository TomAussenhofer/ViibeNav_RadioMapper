package de.beacon.tom.viibenav_radiomapper.model.dbmodels;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by TomTheBomb on 25.07.2015.
 */
public class MedianView {

    private int id;
    private int median;
    private int median_beaconid;
    private String orientation;

    private static ArrayList<MedianView> all = new ArrayList<>();

    public MedianView(int id, int median, int median_beaconid, String orientation) {
        this.id = id;
        this.median = median;
        this.median_beaconid = median_beaconid;
        this.orientation = orientation;
    }

    public static void setAll(ArrayList<MedianView> all) {
        MedianView.all = all;
    }

    public static ArrayList<MedianView> getAll() {
        return all;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMedian() {
        return median;
    }

    public void setMedian(int median) {
        this.median = median;
    }

    public String getOrientation() {
        return orientation;
    }

    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }

    public int getMedian_beaconid() {
        return median_beaconid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MedianView that = (MedianView) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
