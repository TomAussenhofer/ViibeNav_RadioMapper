package de.beacon.tom.viibenav_radiomapper.model.dbmodels;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by TomTheBomb on 03.09.2015.
 */
public class Fingerprint_has_MedianView {

    private int id, fingerprintid, medianid;

    private static ArrayList<Fingerprint_has_MedianView> all = new ArrayList<>();

    public Fingerprint_has_MedianView(int id, int fingerprintid, int medianid) {
        this.id = id;
        this.fingerprintid = fingerprintid;
        this.medianid = medianid;
    }

    public static ArrayList<Fingerprint_has_MedianView> getAll() {
        return all;
    }

    public static void setAll(ArrayList<Fingerprint_has_MedianView> all) {
        Fingerprint_has_MedianView.all = all;
    }

    public int getId() {
        return id;
    }

    public int getFingerprintid() {
        return fingerprintid;
    }

    public int getMedianid() {
        return medianid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Fingerprint_has_MedianView that = (Fingerprint_has_MedianView) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
