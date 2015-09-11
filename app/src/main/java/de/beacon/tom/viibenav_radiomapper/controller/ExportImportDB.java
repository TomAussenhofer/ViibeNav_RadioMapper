package de.beacon.tom.viibenav_radiomapper.controller;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;

import de.beacon.tom.viibenav_radiomapper.model.DBHandler;

/**
 * Created by TomTheBomb on 04.09.2015.
 */
public class ExportImportDB{

    public static final String TAG = "ExportImportDB";
    private Context context;

    public ExportImportDB(Context context) {
        this.context = context;
    }

    public void exportDB(String DB_FILEPATH){

        if (isSDCardWriteable()) {
            // Open your local db as the input stream
            String inFileName = DB_FILEPATH;
            File dbFile = new File(inFileName);

            try{
            FileInputStream fis = new FileInputStream(dbFile);

            String outFileName = Environment.getExternalStorageDirectory() + "/radiomap/radiomap.db";
            Log.d("TAG", "external storage dir: " + outFileName);
            File export = new File(outFileName);
            export.getParentFile().mkdirs();
            boolean readable = export.setReadable(true,false);
            Log.d(TAG, " export set readable: " + readable);
            Log.d("DB_FILEPATH", DB_FILEPATH);

            // Open the empty db as the output stream

                OutputStream output = new FileOutputStream(outFileName);
                // transfer bytes from the inputfile to the outputfile
                byte[] buffer = new byte[1024];
                int length;
                while ((length = fis.read(buffer)) > 0) {
                    output.write(buffer, 0, length);
                }
                // Close the streams
                output.flush();
                output.close();
                fis.close();

                Toast.makeText(context,"Exporting DB worked", Toast.LENGTH_LONG).show();
            } catch(Exception e){
                Toast.makeText(context,"ERROR: "+e.toString(), Toast.LENGTH_LONG).show();
                Log.e(TAG,"ERROR: " + e.toString());
            }
        }
    }

    private boolean isSDCardWriteable() {
        boolean rc = false;
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            rc = true;
        }
        return rc;
    }


    //importing database
    public void importDB() {


            String currentDBPath = DBHandler.getDB().getDBPath();
            String backupDBPath  = Environment.getExternalStorageDirectory() + "/radiomap/radiomap.db";
            File backupDB = new File(backupDBPath);
            backupDB.setReadable(true, false);
            File currentDB  = new File(currentDBPath);

        try{

            FileChannel src = new FileInputStream(backupDB).getChannel();
            FileChannel dst = new FileOutputStream(currentDB).getChannel();

            dst.transferFrom(src, 0, src.size());
            src.close();
            dst.close();
            Log.d(TAG, "Importing DB worked! ");
            Toast.makeText(context, currentDB.toString()+" worked! ",
                    Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            Log.e("ERROR: ",e.toString());
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG)
                    .show();
        }
    }
}
