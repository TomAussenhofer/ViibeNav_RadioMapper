package de.beacon.tom.viibenav_radiomapper.model.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import de.beacon.tom.viibenav_radiomapper.R;

/**
 * Created by TomTheBomb on 02.09.2015.
 */
public class SecondMeasureDialog  extends DialogFragment {

    private int resultCode;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            resultCode = 1;

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            // Get the layout inflater
            LayoutInflater inflater = getActivity().getLayoutInflater();

            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            View v = inflater.inflate(R.layout.dialog_second_measure, null);
            final TextView degreeTV = (TextView) v.findViewById(R.id.dialog_second_measure_degreeTV);
            final Button second_measure = (Button) v.findViewById(R.id.dialog_second_measure_measure);
            final Button cancel = (Button) v.findViewById(R.id.dialog_second_measure_cancel);


            builder.setView(v);

            second_measure.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("TAG","clicked");
                    getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, getActivity().getIntent());
                    dismiss();
                }
            });

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SecondMeasureDialog.this.getDialog().cancel();
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

        private boolean isEmpty(EditText t){
            return t.getText().toString().isEmpty();
        }

}
