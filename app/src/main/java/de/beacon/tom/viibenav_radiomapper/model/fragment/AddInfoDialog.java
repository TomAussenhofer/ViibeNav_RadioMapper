package de.beacon.tom.viibenav_radiomapper.model.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import de.beacon.tom.viibenav_radiomapper.R;
import de.beacon.tom.viibenav_radiomapper.controller.MainActivity;


/**
 * Created by TomTheBomb on 27.08.2015.
 */
public class AddInfoDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final MainActivity main = (MainActivity) getActivity();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View v = inflater.inflate(R.layout.dialog_add_info, null);
        final EditText person_nameText = (EditText) v.findViewById(R.id.person_name);
        final EditText room_nameText = (EditText) v.findViewById(R.id.room_name);
        final EditText environmentText = (EditText) v.findViewById(R.id.environment);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_dropdown_item_1line, DEFAULT_CATEGORIES);
        final AutoCompleteTextView category = (AutoCompleteTextView) v.findViewById(R.id.autoCompleteTextView);
        category.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                    category.showDropDown();
            }
        });
        category.setAdapter(adapter);
        category.setThreshold(0);//will start working from first character
        category.setTextColor(Color.RED);


        builder.setView(v);
        // Add action buttons
        builder.setPositiveButton(R.string.add_info, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                main.getApplicationUI().getInfo().setEnvironment(environmentText.getText().toString().trim());
                main.getApplicationUI().getInfo().setPerson_name(person_nameText.getText().toString().trim());
                main.getApplicationUI().getInfo().setRoom_name(room_nameText.getText().toString().trim());
                main.getApplicationUI().getInfo().setCategory(category.getText().toString().trim());

            }
        });
        builder.setNegativeButton(R.string.cancel_info, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                AddInfoDialog.this.getDialog().cancel();
            }
        });
        return builder.create();
    }

    private String[] DEFAULT_CATEGORIES = {"Büro","Sekreteriat","Sonstiges"};

}
