package de.beacon.tom.viibenav_radiomapper.model.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import de.beacon.tom.viibenav_radiomapper.R;
import de.beacon.tom.viibenav_radiomapper.controller.Application;


/**
 * Created by TomTheBomb on 27.08.2015.
 */
public class AddInfoDialog extends DialogFragment {

    private Application application;

    public AddInfoDialog(Application application) {
        this.application = application;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View v = inflater.inflate(R.layout.dialog_add_info, null);
        final EditText person_nameText = (EditText) v.findViewById(R.id.person_name);
        final EditText room_nameText = (EditText) v.findViewById(R.id.room_name);
        final EditText environmentText = (EditText) v.findViewById(R.id.person_name);


        builder.setView(v);
                // Add action buttons
                builder.setPositiveButton(R.string.add_info, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                            application.getAddInfo().setEnvironment(environmentText.getText().toString());
                            application.getAddInfo().setPerson_name(person_nameText.getText().toString());
                            application.getAddInfo().setRoom_name(room_nameText.getText().toString());
                    }
                });
                builder.setNegativeButton(R.string.cancel_info, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        AddInfoDialog.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }

}
