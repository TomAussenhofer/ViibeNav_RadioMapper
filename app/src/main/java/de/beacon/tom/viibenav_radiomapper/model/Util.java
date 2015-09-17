package de.beacon.tom.viibenav_radiomapper.model;

import android.widget.EditText;

import java.math.RoundingMode;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


/**
 * Created by TomTheBomb on 26.06.2015.
 */
public class Util {

    private static final String TAG = "Util";
    public static DecimalFormat df;

    static{
        df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);
    }

    public static double twoDecimals(double data){
        return Double.parseDouble(df.format(data).replaceAll(",", "."));
    }

    public static float twoDecimals(float data){
        return Float.parseFloat(df.format(data).replaceAll(",", "."));
    }

    /**
     * Converts the Value of a textView to an Integer
     * @param t
     * @return
     */
    public static int textViewToInt(EditText t){
            return Integer.valueOf(t.toString());
    }


    public static CharBuffer strToCharBuff(String str){
            char[] data = str.toCharArray();
            ByteBuffer bb = ByteBuffer.allocate(data.length * 2);
            CharBuffer cb = bb.asCharBuffer();
            cb.put(data);
            cb.rewind();
            return cb;
    }

    public static String getDateTimeToStr(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss");
        return sdf.format(c.getTime());
    }

    public static String primitiveListToString(ArrayList<? extends Object> data){
        String res = "{ ";
        for(int i=0;i<data.size();i++)
            if(i<data.size()-1)
                res += data.get(i) + ",";
            else
                res += data.get(i);
        res += " }";
        return res;
    }

    public static String primitiveArrayToString(Object[] data){
        String res = "{ ";
        for(int i=0;i<data.length;i++)
            if(i<data.length-1)
                res += data[i] + ",";
            else
                res += data[i];
        res += " }";
        return res;
    }

    public static long timeDiff_MillisToNow(long millisAgo){
        return System.currentTimeMillis()-millisAgo;
    }

    public static boolean hasSufficientSendingFreq(long time){
        return Util.timeDiff_MillisToNow(time) <= Definitions.TIME_LAST_SIGNAL_THRESHOLD;
    }

    /**
     * Deep clones an ArrayList<OnyxBeacon> beacon
     * @param beacons
     * @return (deep cloned) ArrayList<OnyxBeacon> result
     */
    public static ArrayList<OnyxBeacon> cloneBeacons(ArrayList<OnyxBeacon> beacons){
        ArrayList<OnyxBeacon> res = new ArrayList<>();
        for (OnyxBeacon tmp : beacons) res.add(tmp.clone());
        return res;
    }

}
