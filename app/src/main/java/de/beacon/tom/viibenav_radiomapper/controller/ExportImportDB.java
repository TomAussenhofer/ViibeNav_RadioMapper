package de.beacon.tom.viibenav_radiomapper.controller;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.FileChannel;

import de.beacon.tom.viibenav_radiomapper.R;
import de.beacon.tom.viibenav_radiomapper.model.DBHandler;

/**
 * Created by TomTheBomb on 04.09.2015.
 */
public class ExportImportDB extends Activity {

    public static final String TAG = "ExportImportDB";
    public static final String SAVE_DB_FILENAME = "radiomap.db";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export);

    }

    public void importClicked(View view){
        importDB();
    }

    public void export(View view){
        try {
            backupDatabase(DBHandler.getDB().getDBPath());
        } catch (IOException e) {
            Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_LONG)
                    .show();
        }
    }

    public void backupDatabase(String DB_FILEPATH) throws IOException {

        if (isSDCardWriteable()) {
            // Open your local db as the input stream
            String inFileName = DB_FILEPATH;
            File dbFile = new File(inFileName);
            FileInputStream fis = new FileInputStream(dbFile);

            String outFileName = Environment.getExternalStorageDirectory() + "/radiomap/radiomap.db";
            Log.d("TAG", "external storage dir: " + outFileName);
            File export = new File(outFileName);
            export.getParentFile().mkdirs();
            boolean readable = export.setReadable(true,false);
            Log.d(TAG, " export set readable: " + readable);
            Log.d("DB_FILEPATH", DB_FILEPATH);

            // Open the empty db as the output stream
            try{
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

                Toast.makeText(this,"Exporting DB worked", Toast.LENGTH_LONG).show();
            } catch(Exception e){
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
    private void importDB() {


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
            Toast.makeText(getBaseContext(), currentDB.toString()+" worked! ",
                    Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            Log.e("ERROR: ",e.toString());
            Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_LONG)
                    .show();
        }
    }
}
