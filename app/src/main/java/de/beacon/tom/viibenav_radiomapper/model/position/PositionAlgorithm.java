package de.beacon.tom.viibenav_radiomapper.model.position;


import de.beacon.tom.viibenav_radiomapper.model.Coordinate;

/**
 * Created by TomTheBomb on 16.08.2015.
 */
public interface PositionAlgorithm {

    public static final String TAG = "PositionAlgorithm";

    public Coordinate estimatePos(MacToMedian[] map);
}
