package de.beacon.tom.viibenav_radiomapper.model.dbmodels;

import java.util.ArrayList;

import de.beacon.tom.viibenav_radiomapper.model.Coordinate;


/**
 * Created by TomTheBomb on 24.07.2015.
 */
public class AnchorPointDBModel {

    private Coordinate coord;
    private int _id,addInfoID,front_id,back_id;


    private static ArrayList<AnchorPointDBModel> allAnchors;

    public AnchorPointDBModel(int id, Coordinate coord, int front_id, int back_id, int addInfoID) {
        this._id = id;
        this.coord = coord;
        this.addInfoID = addInfoID;
        this.front_id = front_id;
        this.back_id = back_id;
    }


    public static void setAllAnchors(ArrayList<AnchorPointDBModel> allAnchors) {
        AnchorPointDBModel.allAnchors = allAnchors;
    }

    public int getFront_id() {
        return front_id;
    }

    public int getBack_id() {
        return back_id;
    }

    public static ArrayList<AnchorPointDBModel> getAllAnchors() {
        return allAnchors;
    }

    public void setCoord(Coordinate coord) {
        this.coord = coord;
    }

    public Coordinate getCoord() {
        return coord;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public void setAddInfoID(int addInfoID) {
        this.addInfoID = addInfoID;
    }

    public int getAddInfoID() {
        return addInfoID;
    }
}
