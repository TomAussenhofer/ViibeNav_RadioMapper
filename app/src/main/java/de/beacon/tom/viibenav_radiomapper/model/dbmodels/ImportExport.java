package de.beacon.tom.viibenav_radiomapper.model.dbmodels;

import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by TomTheBomb on 28.08.2015.
 */
public class ImportExport {

        public static void backupDatabase(String DB_FILEPATH) throws IOException {

            if (isSDCardWriteable()) {
                // Open your local db as the input stream
                String inFileName = DB_FILEPATH;
                File dbFile = new File(inFileName);
                FileInputStream fis = new FileInputStream(dbFile);

                String outFileName = Environment.getExternalStorageDirectory() + "/radiomapdb";
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
            }
        }

        private static boolean isSDCardWriteable() {
            boolean rc = false;
            String state = Environment.getExternalStorageState();
            if (Environment.MEDIA_MOUNTED.equals(state)) {
                rc = true;
            }
            return rc;
        }


}
