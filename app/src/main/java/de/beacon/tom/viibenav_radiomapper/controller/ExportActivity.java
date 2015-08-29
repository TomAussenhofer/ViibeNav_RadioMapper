package de.beacon.tom.viibenav_radiomapper.controller;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import de.beacon.tom.viibenav_radiomapper.R;


/**
 * Created by TomTheBomb on 21.07.2015.
 */
public class ExportActivity extends Activity {

    private static final String TAG = "Export";

    private File path;
    private String state;
    private Button saveBytes;
    private TextView exportName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_export);

//        state = Environment.getExternalStorageState();
//        path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
//        checkPermission();
//
//        saveBytes = (Button) findViewById(R.id.saveBytes);
//        exportName = (TextView) findViewById(R.id.exportName);
//        exportName.setText(Util.getDateTimeToStr());
    }

    public void saveBytes(View view){
        try {
            saveToByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveToByteArray() throws Exception {
//        String filename = exportName.getText()+".ser";
//        File radio = new File(path.getAbsolutePath() /*+ "/RadioMapper"*/);
//
//        radio.mkdirs();
//
//        File f = new File(radio, filename );
//
//            OutputStream os = new FileOutputStream(f);
//            ObjectOutputStream oos = new ObjectOutputStream(os);
//            oos.writeObject(RadioMap.getData());
//            oos.close();
//            os.close();
//            Toast.makeText(this, "Serialized RadioMap saved in "+filename, Toast.LENGTH_SHORT);
    }

    public void saveCSV(View view){
//        try {
//            saveToCSVFile();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    private void saveToCSVFile() throws Exception {
        String filename = exportName.getText()+".csv";
        File radio = new File(path.getAbsolutePath() + "/RadioMapper");

        radio.mkdirs();
        File f = new File(radio, filename );

        Toast.makeText(this, "CSV RadioMap saved in " + filename, Toast.LENGTH_SHORT);
    }

    private boolean checkPermission(){
        // if we can read AND write
        if(state.equals(Environment.MEDIA_MOUNTED))
            return true;
        else if(state.equals(Environment.MEDIA_MOUNTED_READ_ONLY)) {
            Toast.makeText(this, "You only have read permission - check preferences!", Toast.LENGTH_SHORT);
            return false;
        }else {
            Toast.makeText(this, "You can not read or write - check preferences!", Toast.LENGTH_SHORT);
            return false;
        }
    }
}
