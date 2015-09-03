package de.beacon.tom.viibenav_radiomapper.model.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import de.beacon.tom.viibenav_radiomapper.R;
import de.beacon.tom.viibenav_radiomapper.controller.MainActivity;
import de.beacon.tom.viibenav_radiomapper.model.SensorHelper;

/**
 * Created by TomTheBomb on 02.09.2015.
 */
public class SecondMeasureDialog  extends DialogFragment {

    private SensorHelper sh;
    private ScheduledExecutorService exec;

    private TextView degreeTV;
    private ImageView arrowImage;

    private Boolean frontMeasuredFirst;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            frontMeasuredFirst = getArguments().getBoolean("frontMeasuredFirst");

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            // Get the layout inflater
            LayoutInflater inflater = getActivity().getLayoutInflater();

            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            View v = inflater.inflate(R.layout.dialog_second_measure, null);
            degreeTV = (TextView) v.findViewById(R.id.dialog_second_measure_degreeTV);
            arrowImage = (ImageView) v.findViewById(R.id.dialog_second_measure_arrowImageView);
            Button second_measure = (Button) v.findViewById(R.id.dialog_second_measure_measure);
            Button cancel = (Button) v.findViewById(R.id.dialog_second_measure_cancel);

            sh = SensorHelper.getSensorHelper(getActivity().getApplicationContext());
            execScheduled();

            builder.setView(v);
            second_measure.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MainActivity main = (MainActivity) getActivity();
                    main.getApplicationUI().startMeasurement(false);
                    cleanUp();

                    dismiss();
                }
            });

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SecondMeasureDialog.this.getDialog().cancel();
                    cleanUp();

                    dismiss();
                }
            });
                    // Add action buttons
//            builder.setPositiveButton(R.string.set, new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int id) {
//
//                    if(!isEmpty(FSPL_CONST))
//                        Setup.FSPL_ELEMENT = Float.parseFloat(FSPL_CONST.getText().toString());
//                    if(!isEmpty(FP_AMT_THRESH))
//                        Setup.MEASUREMENT_AMT_THRESHOLD = Integer.valueOf(FP_AMT_THRESH.getText().toString());
//                    if(!isEmpty(OF_AMT_THRESH))
//                        Setup.ON_THE_FLY_AMT_THRESHOLD = Integer.valueOf(OF_AMT_THRESH.getText().toString());
//                }
//            });
//            builder.setNegativeButton(R.string.cancel_info, new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int id) {
//                    SecondMeasureDialog.this.getDialog().cancel();
//                }
//            });
            return builder.create();
        }

        public void execScheduled() {
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    exec = Executors.newSingleThreadScheduledExecutor();
                    exec.scheduleAtFixedRate(new Runnable() {
                        @Override
                        public void run() {
                            handler.sendEmptyMessage(0);
                        }
                    }, 0, 250, TimeUnit.MILLISECONDS);

                }
            };
            Thread th = new Thread(r);
            th.start();
        }

        private Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                sh.updateTextView(degreeTV, frontMeasuredFirst);
                sh.animateImage(arrowImage);
            }
        };

        private void cleanUp(){
            exec.shutdown();
        }


        private boolean isEmpty(EditText t){
            return t.getText().toString().isEmpty();
        }

}
