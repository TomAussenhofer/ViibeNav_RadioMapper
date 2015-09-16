package de.beacon.tom.viibenav_radiomapper.model.dbmodels;

import java.util.ArrayList;
import java.util.Objects;

import de.beacon.tom.viibenav_radiomapper.model.Coordinate;


/**
 * Created by TomTheBomb on 24.07.2015.
 */
public class FingerprintView {

    private Coordinate coord;
    private int id,addInfoID;

    private static ArrayList<FingerprintView> all = new ArrayList<>();

    public FingerprintView(int id, Coordinate coord, int addInfoID) {
        this.id = id;
        this.coord = coord;
        this.addInfoID = addInfoID;
    }


    public static void setAll(ArrayList<FingerprintView> all) {
        FingerprintView.all = all;
    }

    public static ArrayList<FingerprintView> getAll() {
        return all;
    }

    public Coordinate getCoord() {
        return coord;
    }

    public int getId() {
        return id;
    }

    public int getAddInfoID() {
        return addInfoID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FingerprintView that = (FingerprintView) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
