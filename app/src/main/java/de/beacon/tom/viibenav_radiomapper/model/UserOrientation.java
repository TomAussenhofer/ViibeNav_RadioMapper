package de.beacon.tom.viibenav_radiomapper.model;

/**
 * Created by TomTheBomb on 01.09.2015.
 */
public class UserOrientation {

    public static Orientation getOrientationFromDegree(int degree){
        if(degree >= 90 && degree < 270)
            return Orientation.back;
        else if ( (degree >= 0 && degree < 90) || (degree >= 270 && degree <= 360))
            return Orientation.front;
        else
            return Orientation.undetermined;
    }

    public static Orientation getOrientationFromSensorHelper(){
        if(SensorHelper.getOrientation() >= 90 && SensorHelper.getOrientation() < 270)
            return Orientation.back;
        else if ( (SensorHelper.getOrientation() >= 0 && SensorHelper.getOrientation() < 90) || (SensorHelper.getOrientation() >= 270 && SensorHelper.getOrientation() <= 360))
            return Orientation.front;
        else
            return Orientation.undetermined;
    }

}
