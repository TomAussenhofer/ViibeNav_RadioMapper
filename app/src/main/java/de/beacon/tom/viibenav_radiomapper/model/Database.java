package de.beacon.tom.viibenav_radiomapper.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import de.beacon.tom.viibenav_radiomapper.model.dbmodels.FingerprintView;
import de.beacon.tom.viibenav_radiomapper.model.dbmodels.Fingerprint_has_MedianView;
import de.beacon.tom.viibenav_radiomapper.model.dbmodels.InfoView;
import de.beacon.tom.viibenav_radiomapper.model.dbmodels.MedianView;
import de.beacon.tom.viibenav_radiomapper.model.dbmodels.OnyxBeaconView;
import de.beacon.tom.viibenav_radiomapper.model.position.MacToMedian;
import de.beacon.tom.viibenav_radiomapper.model.position.neighbor.DeviationToCoord;


/**
 * Created by TomTheBomb on 14.07.2015.
 */
public class Database extends SQLiteOpenHelper {

    private static final String TAG = "Database";

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "radiomap.db";

    // Fingerprint TABLE
    public static final String TABLE_FINGERPRINT = "fingerprint";
    public static final String FINGERPRINT_COLUMN_ID = "fingerprintid";
    public static final String COLUMN_FLOOR = "floor";
    public static final String COLUMN_X = "x";
    public static final String COLUMN_Y = "y";
    public static final String COLUMN_INFO_ID = "fingerprint_infoid";

//    // MAXIMUM AMOUNT OF BEACONS IN ONE TABLE
//    public static final String TABLE_BEACON_MEDIAN_TO_ANCHOR = "beaconmediantoanchor";
//    public static final String BEACON_MEDIAN_TO_ANCHOR_ID = "id";
//    public static final String COLUMN_BEACON_1 = "beacon1";
//    public static final String COLUMN_BEACON_2 = "beacon2";
//    public static final String COLUMN_BEACON_3 = "beacon3";
//    public static final String COLUMN_BEACON_4 = "beacon4";
//    public static final String COLUMN_BEACON_5 = "beacon5";
//    public static final String COLUMN_BEACON_6 = "beacon6";

    public static final String TABLE_FP_HAS_MEDIAN = "fp_has_median";
    public static final String FP_HAS_MEDIAN_COLUMN_ID = "fp_has_medianid";
    public static final String COLUMN_MEDIAN_ID = "fp_has_median_medianid";
    public static final String COLUMN_FINGERPRINT_ID = "fp_has_median_fingerprintid";

    // MEDIANS TABLE
    public static final String TABLE_MEDIAN = "median";
    public static final String MEDIAN_COLUMN_ID = "medianid";
    public static final String COLUMN_MEDIAN_VALUE = "median";
    public static final String COLUMN_BEACON_ID = "median_beaconid";
    public static final String COLUMN_ORIENTATION ="orientation";

    // BEACONS TABLE
    public static final String TABLE_BEACON = "beacon";
    public static final String BEACON_COLUMN_ID = "beaconid";
    public static final String COLUMN_MAJOR = "major";
    public static final String COLUMN_MINOR = "minor";
    public static final String COLUMN_MAC_ADDRESS = "macAddress";
    public static final String COLUMN_UUID = "uuid";

    // INFO TABLE
    public static final String TABLE_INFO = "info";
    public static final String INFO_COLUMN_ID = "infoid";
    public static final String COLUMN_PERSON_NAME = "personname";
    public static final String COLUMN_ROOM_NAME = "roomname";
    public static final String COLUMN_ENVIRONMENT = "environment";
    public static final String COLUMN_CATEGORY = "category";

    private static Database singleton;

    private Context context;

    private Database(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);

        this.context = context;
    }

    public static Database createDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        // Avoid possible errors with multiple threads accessing this method -> synchronized
        synchronized(Database.class) {
            if (singleton == null) {
                singleton = new Database(context, name, factory, version);
            }
        }
        return singleton;
    }

    public static Database getDB(){
        return singleton;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
//        db.execSQL("SELECT load_extension('./libsqlitefunctions.so')");

        // CREATE BEACONS TABLE
        String query1 = "CREATE TABLE "+ TABLE_BEACON + "(" +
                "'"+ BEACON_COLUMN_ID +"'"+ " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "'"+ COLUMN_MAC_ADDRESS +"'"+ " TEXT, " +
                "'"+ COLUMN_MAJOR+"'"+ " INTEGER, "+
                "'"+ COLUMN_MINOR +"'"+ " INTEGER UNIQUE, "+
                "'"+ COLUMN_UUID +"'"+ " TEXT "+
                ");";
        db.execSQL(query1);

        // CREATE MEDIANS TABLE
        String query2 = "CREATE TABLE "+ TABLE_MEDIAN + "(" +
                "'"+ MEDIAN_COLUMN_ID +"'"+ " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "'"+ COLUMN_MEDIAN_VALUE+"'"+ " INTEGER, "+
                "'"+ COLUMN_BEACON_ID +"'"+ " INTEGER, "/*FOREIGN KEY REFERENCES "+TABLE_BEACONS+"("+BEACONS_COLUMN_ID+")"*/+
                "'"+ COLUMN_ORIENTATION +"'"+ " TEXT "+
                ");";
        db.execSQL(query2);

        // CREATE FINGERPRINT TABLE
        String query3 = "CREATE TABLE "+ TABLE_FINGERPRINT + "(" +
                "'"+ FINGERPRINT_COLUMN_ID +"'"+ " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "'"+ COLUMN_FLOOR +"'"+ " INTEGER, "+
                "'"+ COLUMN_X +"'"+ " INTEGER, "+
                "'"+ COLUMN_Y +"'"+ " INTEGER, "+
                "'"+ COLUMN_INFO_ID +"'"+ " INTEGER  " +
                ");";
        db.execSQL(query3);

        // CREATE INFO TABLE
        String query4 = "CREATE TABLE "+ TABLE_INFO + "(" +
                "'"+ INFO_COLUMN_ID +"'"+ " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "'"+ COLUMN_PERSON_NAME +"'"+ " TEXT, "+
                "'"+ COLUMN_ROOM_NAME +"'"+ " TEXT, "+
                "'"+ COLUMN_ENVIRONMENT +"'"+ " TEXT, "+
                "'"+ COLUMN_CATEGORY+"'"+ " TEXT "+
                ");";
        db.execSQL(query4);

        // CREATE FP_HAS_MEDIAN TABLE
        String query5 = "CREATE TABLE "+ TABLE_FP_HAS_MEDIAN + "(" +
                "'"+ FP_HAS_MEDIAN_COLUMN_ID +"'"+ " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "'"+ COLUMN_MEDIAN_ID +"'"+ " INTEGER, "+
                "'"+ COLUMN_FINGERPRINT_ID +"'"+ " INTEGER "+
                ");";
        db.execSQL(query5);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_FINGERPRINT);
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_MEDIAN);
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_BEACON);
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_INFO);
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_FP_HAS_MEDIAN);
        onCreate(db);
    }

    // Add a new row to the database
    public void addFingerprint(Fingerprint a){
        SQLiteDatabase db = getWritableDatabase();

        //INSERT IN TABLE FINGERPRINT
        ContentValues valuesFingerprint = new ContentValues();
        valuesFingerprint.put(COLUMN_X, a.getCoordinate().getX());
        valuesFingerprint.put(COLUMN_Y, a.getCoordinate().getY());
        valuesFingerprint.put(COLUMN_FLOOR, a.getCoordinate().getFloor());

        Info info = a.getInfo();
        if(info.hasInfo()){
            ContentValues valuesInfo = new ContentValues();
            if(info.hasPersonInfo())
                valuesInfo.put(COLUMN_PERSON_NAME, info.getPerson_name());
            if(info.hasRoomInfo())
                valuesInfo.put(COLUMN_ROOM_NAME, info.getRoom_name());
            if(info.hasEnvironmentInfo())
                valuesInfo.put(COLUMN_ENVIRONMENT, info.getEnvironment());
            if(info.hasCategoryInfo())
                valuesInfo.put(COLUMN_CATEGORY, info.getCategory());

            Log.d(TAG, " Insert table info  "+db.insertOrThrow(TABLE_INFO, null, valuesInfo));

            int infoID = getLastID(db,TABLE_INFO, INFO_COLUMN_ID);
            valuesFingerprint.put(COLUMN_INFO_ID, infoID);
        }

        db.insertOrThrow(TABLE_FINGERPRINT, null, valuesFingerprint);
        int fingerprintid = getLastID(db,TABLE_FINGERPRINT,FINGERPRINT_COLUMN_ID);

        Log.d(TAG,"FRONT ARRAY: "+a.getFront());
        Log.d(TAG,"BACK ARRAY: "+a.getBack());
        ArrayList<Integer> frontBeaconIds = getInsertedMedianIds(db, a.getFront());
        ArrayList<Integer> backBeaconIds = getInsertedMedianIds(db, a.getBack());

//        Log.d(TAG,"frontIDS:"+Util.primitiveListToString(frontBeaconIds));
//        Log.d(TAG,"backIDS:"+Util.primitiveListToString(backBeaconIds));
        addFingerprint_has_Median(fingerprintid,frontBeaconIds,backBeaconIds);
        db.close();
    }

    private ArrayList<Integer> getInsertedMedianIds(SQLiteDatabase db, BeaconToOrient input) {
        ArrayList<Integer> res = new ArrayList<>();
        ArrayList<Integer> beaconIds = new ArrayList<>();
        OnyxBeacon[] beaconArr = input.getBeaconArray();
        addBeacon(beaconIds,beaconArr);

        for (int i = 0; i < beaconArr.length; i++) {
            ContentValues valuesMedian = new ContentValues();
            valuesMedian.put(COLUMN_BEACON_ID, beaconIds.get(i));
            valuesMedian.put(COLUMN_MEDIAN_VALUE, beaconArr[i].getMedianRSSI());
            valuesMedian.put(COLUMN_ORIENTATION, input.getOrientation().toString());

            db.insertOrThrow(TABLE_MEDIAN, null, valuesMedian);
            res.add(getLastID(db, TABLE_MEDIAN, MEDIAN_COLUMN_ID));
        }
        return res;
    }

    public void addFingerprint_has_Median(int fingerprintid,ArrayList<Integer> front, ArrayList<Integer> back){
        SQLiteDatabase db = getWritableDatabase();

        //INSERT FINGERPRINT_HAS_MEDIAN IN TABLE FINGERPRINT
        for (int i : front) {
            ContentValues valuesFingerprint_has_Median = new ContentValues();
            valuesFingerprint_has_Median.put(COLUMN_FINGERPRINT_ID, fingerprintid);
            valuesFingerprint_has_Median.put(COLUMN_MEDIAN_ID, i);

            db.insertOrThrow(TABLE_FP_HAS_MEDIAN, null, valuesFingerprint_has_Median);
        }

        for (int i : back) {
            ContentValues valuesFingerprint_has_Median = new ContentValues();
            valuesFingerprint_has_Median.put(COLUMN_FINGERPRINT_ID, fingerprintid);
            valuesFingerprint_has_Median.put(COLUMN_MEDIAN_ID, i);

            db.insertOrThrow(TABLE_FP_HAS_MEDIAN, null, valuesFingerprint_has_Median);
        }

        db.close();
    }

    private void addBeacon(ArrayList<Integer> insertedIds, OnyxBeacon[] beacon){
        SQLiteDatabase db = getWritableDatabase();

        //INSERT FINGERPRINT_HAS_MEDIAN IN TABLE FINGERPRINT
        for (int i = 0; i < beacon.length; i++) {
            OnyxBeacon b = beacon[i];
            ContentValues valuesBeacon = new ContentValues();
            valuesBeacon.put(COLUMN_MAC_ADDRESS, b.getMacAddressStr());
            valuesBeacon.put(COLUMN_MAJOR, b.getMajor());
            valuesBeacon.put(COLUMN_MINOR, b.getMinor());
            valuesBeacon.put(COLUMN_UUID, b.getUuid());

            long returnVal = db.insertWithOnConflict(TABLE_BEACON, null, valuesBeacon, SQLiteDatabase.CONFLICT_IGNORE);

            if(returnVal == SQLiteDatabase.CONFLICT_IGNORE)
                Log.d(TAG, "CONFLICT IGNORE - no insert");
            else if(returnVal == -1)
                Log.d(TAG, "CONFLICT ERROR - no insert\nFetching corresponding ids to minors and majors...");

            insertedIds.add(getBeaconId(db, b.getMajor(),b.getMinor()));
        }
    }

    private int getBeaconId(SQLiteDatabase db, int major, int minor){
        final String query = "SELECT "+BEACON_COLUMN_ID+" FROM '" + TABLE_BEACON+ "' WHERE "+COLUMN_MAJOR+"="+major+" AND "+COLUMN_MINOR+"="+minor+";";
        Cursor cur = db.rawQuery(query, null);
        cur.moveToFirst();
        int ID = cur.getInt(0);
        cur.close();
        return ID;
    }

    /**
     * Gets the most recent ID, which is the latest inserted entry for a specific TABLE
     * @param db
     * @param TABLE
     * @param id
     * @return
     */
    public int getLastID(SQLiteDatabase db, final String TABLE, final String id) {
        final String query = "SELECT MAX("+id+") FROM '" + TABLE+ "'";
        Cursor cur = db.rawQuery(query, null);
        cur.moveToFirst();
        int ID = cur.getInt(0);
        cur.close();
        return ID;
    }


    public void deleteAllTables(){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS '" + TABLE_FINGERPRINT +"';");
        db.execSQL("DROP TABLE IF EXISTS '" + TABLE_MEDIAN +"';");
        db.execSQL("DROP TABLE IF EXISTS '" + TABLE_BEACON+"';");
        db.execSQL("DROP TABLE IF EXISTS '" + TABLE_INFO+"';");
        db.execSQL("DROP TABLE IF EXISTS '" + TABLE_FP_HAS_MEDIAN + "';");
        onCreate(db);
    }

    /*
    Uses Manhatten distrance for DB
     */
    private String calcManhattenDistance(String column_name, double value){
       return "ABS(MIN("+column_name+" - "+value+"))";
    }

    public ArrayList<DeviationToCoord> getAllDistancesFromMedians(MacToMedian[] map, int maxResults, float threshold) {
//        SQLiteDatabase db = getWritableDatabase();
//
//        ArrayList<DeviationToCoord> devsToCoords = new ArrayList<>();
//        final String LOCAL_COLUMN_DEVIATION = "deviation";
//
//        for (int i = 0; i < map.length; i++) {
//            final String macAddress = map[i].getMacAddressStr();
//            final double median = map[i].getMedian();
////            Log.d(TAG, "MEDIAN IN LOOP "+median);
//
//            String queryOrientation = "";
//            if (map[i].getOrientation().equals(Orientation.back))
//                queryOrientation = "( " + TABLE_FINGERPRINT + "." + COLUMN_BACK + " = " + TABLE_BEACON_MEDIAN_TO_ANCHOR + "." + BEACON_MEDIAN_TO_ANCHOR_ID + " ) ";
//            else if (map[i].getOrientation().equals(Orientation.front))
//                queryOrientation = "( " + TABLE_FINGERPRINT + "." + COLUMN_FRONT + " = " + TABLE_BEACON_MEDIAN_TO_ANCHOR + "." + BEACON_MEDIAN_TO_ANCHOR_ID + " ) ";
//
//            String query = "SELECT " + TABLE_FINGERPRINT + "." + COLUMN_FLOOR + "," + TABLE_FINGERPRINT + "." + COLUMN_X + ", " + TABLE_FINGERPRINT + "." + COLUMN_Y + "," + TABLE_MEDIAN + "." + MEDIANS_COLUMN_ID + ", " + calcManhattenDB_Cmd(median) + " AS " + LOCAL_COLUMN_DEVIATION + " " +
//                    " FROM '" + TABLE_MEDIAN + "' JOIN '" + TABLE_FINGERPRINT + "' JOIN '" + TABLE_BEACON_MEDIAN_TO_ANCHOR + "' WHERE macAddress = '" + macAddress + "' AND " +
//                    " ( " + TABLE_BEACON_MEDIAN_TO_ANCHOR + "." + COLUMN_BEACON_1 + " = " + TABLE_MEDIAN + "." + MEDIANS_COLUMN_ID + " " +
//                    "   OR " + TABLE_BEACON_MEDIAN_TO_ANCHOR + "." + COLUMN_BEACON_2 + " = " + TABLE_MEDIAN + "." + MEDIANS_COLUMN_ID + "  " +
//                    "   OR " + TABLE_BEACON_MEDIAN_TO_ANCHOR + "." + COLUMN_BEACON_3 + " = " + TABLE_MEDIAN + "." + MEDIANS_COLUMN_ID + "  " +
//                    "   OR " + TABLE_BEACON_MEDIAN_TO_ANCHOR + "." + COLUMN_BEACON_4 + " = " + TABLE_MEDIAN + "." + MEDIANS_COLUMN_ID + "  " +
//                    "   OR " + TABLE_BEACON_MEDIAN_TO_ANCHOR + "." + COLUMN_BEACON_5 + " = " + TABLE_MEDIAN + "." + MEDIANS_COLUMN_ID + "  " +
//                    "   OR " + TABLE_BEACON_MEDIAN_TO_ANCHOR + "." + COLUMN_BEACON_6 + " = " + TABLE_MEDIAN + "." + MEDIANS_COLUMN_ID + "  " +
//                    "  ) AND " + queryOrientation +
//                    " GROUP BY " + COLUMN_MEDIAN_VALUE + " HAVING deviation <=" + threshold + " ORDER BY " + LOCAL_COLUMN_DEVIATION + " ASC LIMIT " + maxResults + ";";
//
//            // IMPORTANT - NOTE:
//            // IT MAKES SENSE TO SET UP THE LIMIT TO 5 WHEN MULTIPLE ANCHORS ARE SET IN THE RADIO MAP
//            // BUT NOW FOR TESTING PURPOSES IT DOES NOT MAKE SENSE -> TESTCASE: I ONLY HAVE 2 ANCHORS IN MY MAP IF I SET LIMIT TO >1
//            // I WILL GET BOTH ANCHORS AS POSSIBLE, BUT I ONLY WANT THE ONE MOST LIKELIEST ANCHORPOINT !
//
//            Cursor c = db.rawQuery(query, null);
//            c.moveToFirst();
//
//            while (!c.isAfterLast()) {
//                Coordinate coordinate = new Coordinate(c.getInt(c.getColumnIndex(COLUMN_FLOOR)), c.getInt(c.getColumnIndex(COLUMN_X)), c.getInt(c.getColumnIndex(COLUMN_Y)));
//
//                float deviation = c.getInt(c.getColumnIndex(LOCAL_COLUMN_DEVIATION));
//                devsToCoords.add(new DeviationToCoord(deviation, coordinate));
//
////                Log.d(TAG, "Deviation-Median" + c.getInt(c.getColumnIndex(MEDIANS_COLUMN_ID)) +
////                        " deviation: " + deviation +
////                        " -> Coord: " + coordinate + " macAddress " + macAddress);
//                c.moveToNext();
//            }
//            c.close();
//        }
//        db.close();
////        return devsToCoords.toArray(new DeviationToCoord[devsToCoords.size()]);
//        return devsToCoords;
        return null;
    }




    public Coordinate getCoordFromAnchorId(int id){
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT "+COLUMN_X+", "+COLUMN_Y+", "+COLUMN_FLOOR+" FROM '"+ TABLE_FINGERPRINT + "' WHERE "+ FINGERPRINT_COLUMN_ID +" = '"+id+"';";

        // Cursor point to a location in your results
        Cursor c = db.rawQuery(query,null);
        // Move to the first row in your results
        c.moveToFirst();

        int x = -1;
        int y = -1;
        int floor = -1;

        while(!c.isAfterLast()){
            x = c.getInt(c.getColumnIndex(COLUMN_X));
            y = c.getInt(c.getColumnIndex(COLUMN_Y));
            floor = c.getInt(c.getColumnIndex(COLUMN_FLOOR));
            c.moveToNext();
        }

        c.close();
        db.close();
        Coordinate coord = new Coordinate(floor,x,y);
        return coord;
    }


    public void getAllFingerprints(){
        ArrayList<FingerprintView> res = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM '"+ TABLE_FINGERPRINT + "';";

        // Cursor point to a location in your results
        Cursor c = db.rawQuery(query,null);
        // Move to the first row in your results
        c.moveToFirst();

        int id = 0;
        int floor = 0;
        int x = 0;
        int y = 0;
        int addInfoID = 0;

        while(!c.isAfterLast()){
            id = c.getInt(c.getColumnIndex(FINGERPRINT_COLUMN_ID));
            x = c.getInt(c.getColumnIndex(COLUMN_X));
            y = c.getInt(c.getColumnIndex(COLUMN_Y));
            floor = c.getInt(c.getColumnIndex(COLUMN_FLOOR));
            addInfoID = c.getInt(c.getColumnIndex(COLUMN_INFO_ID));

            res.add(new FingerprintView(id,new Coordinate(floor,x,y),addInfoID));
            c.moveToNext();
        }

        Log.d(TAG, "DONE FETCHING TABLE: FINGERPRINT, SIZE:" + res.size());
        FingerprintView.setAll(res);
        c.close();
        db.close();
    }
//
    public void getAllBeacons(){
        ArrayList<OnyxBeaconView> res = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM '"+TABLE_BEACON + "';";

        // Cursor point to a location in your results
        Cursor c = db.rawQuery(query,null);
        // Move to the first row in your results
        c.moveToFirst();

        int id = 0;
        int major = 0;
        int minor = 0;
        String macAddress = "";
        String uuid = "";

        while(!c.isAfterLast()){
            id = c.getInt(c.getColumnIndex(BEACON_COLUMN_ID));
            major = c.getInt(c.getColumnIndex(COLUMN_MAJOR));
            minor = c.getInt(c.getColumnIndex(COLUMN_MINOR));
            macAddress = c.getString(c.getColumnIndex(COLUMN_MAC_ADDRESS));
            uuid = c.getString(c.getColumnIndex(COLUMN_UUID));
            res.add(new OnyxBeaconView(id,major,minor,macAddress,uuid));
            c.moveToNext();
        }

        Log.d(TAG, "DONE FETCHING TABLE: BEACON SIZE:" + res.size());
        c.close();
        db.close();
        OnyxBeaconView.setAll(res);
    }

    public void getAllMedians(){
        ArrayList<MedianView> res = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM '"+ TABLE_MEDIAN + "';";

        // Cursor point to a location in your results
        Cursor c = db.rawQuery(query,null);
        // Move to the first row in your results
        c.moveToFirst();

        int id = 0;
        int median = 0;
        int median_beaconid = 0;
        String orientation = "";

        while(!c.isAfterLast()){
            id = c.getInt(c.getColumnIndex(MEDIAN_COLUMN_ID));
            median = c.getInt(c.getColumnIndex(COLUMN_MEDIAN_VALUE));
            median_beaconid = c.getInt(c.getColumnIndex(COLUMN_BEACON_ID));
            orientation = c.getString(c.getColumnIndex(COLUMN_ORIENTATION));
            res.add(new MedianView(id,median,median_beaconid,orientation));
            c.moveToNext();
        }

        Log.d(TAG, "DONE FETCHING TABLE: MEDIAN SIZE:" + res.size());
        c.close();
        db.close();
        MedianView.setAll(res);
    }

    public void getAllInfo(){
        ArrayList<InfoView> res = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM '"+TABLE_INFO + "';";

        // Cursor point to a location in your results
        Cursor c = db.rawQuery(query,null);
        // Move to the first row in your results
        c.moveToFirst();

        int id = 0;
        String person_name = "";
        String room_name = "";
        String environment = "";
        String category = "";

        while(!c.isAfterLast()){
            id = c.getInt(c.getColumnIndex(INFO_COLUMN_ID));
            person_name = c.getString(c.getColumnIndex(COLUMN_PERSON_NAME));
            room_name = c.getString(c.getColumnIndex(COLUMN_ROOM_NAME));
            environment = c.getString(c.getColumnIndex(COLUMN_ENVIRONMENT));
            category = c.getString(c.getColumnIndex(COLUMN_CATEGORY));
            res.add(new InfoView(id,person_name,room_name,environment,category));
            c.moveToNext();
        }

        Log.d(TAG, "DONE FETCHING TABLE: INFO SIZE:" + res.size());
        c.close();
        db.close();
        InfoView.setAll(res);
    }

    public void getAllFingerprint_has_Medians(){
        ArrayList<Fingerprint_has_MedianView> res = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM '"+TABLE_FP_HAS_MEDIAN + "';";

        // Cursor point to a location in your results
        Cursor c = db.rawQuery(query,null);
        // Move to the first row in your results
        c.moveToFirst();

        int id = 0;
        int medianid = 0;
        int fingerprintid = 0;

        while(!c.isAfterLast()){
            id = c.getInt(c.getColumnIndex(FP_HAS_MEDIAN_COLUMN_ID));
            medianid = c.getInt(c.getColumnIndex(COLUMN_MEDIAN_ID));
            fingerprintid = c.getInt(c.getColumnIndex(COLUMN_FINGERPRINT_ID));
            res.add(new Fingerprint_has_MedianView(id,fingerprintid,medianid));
            c.moveToNext();
        }

        Log.d(TAG, "DONE FETCHING TABLE: FINGERPRINT_HAS_MEDIAN, SIZE:" + res.size());
        c.close();
        db.close();
        Fingerprint_has_MedianView.setAll(res);
    }



    public String getDBPath(){
        return context.getDatabasePath(Database.DATABASE_NAME).toString();
    }


}
