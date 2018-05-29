package com.example.nick.carbontracker.UI;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.example.nick.carbontracker.R;
import com.example.nick.carbontracker.UI.JourneyList;
import com.example.nick.carbontracker.model.Singleton;

public class DeleteJourneyDialog extends AppCompatDialogFragment {
    private Singleton singleton = Singleton.getInstance();

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final View v = LayoutInflater.from(getActivity()).inflate(R.layout.delete_journey_dialog, null);

        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.i("MyApp", "You clicked a dialog button. ");
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        //save and update
                        Bundle bundle = getArguments();
                        int position = bundle.getInt("deletePosition");
                        singleton.removeJourney(position);
                        ((JourneyList) getActivity()).populateJourneyList();
                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };
        return new AlertDialog.Builder(getActivity())
                .setTitle("Delete Journey")
                .setView(v)
                .setPositiveButton(android.R.string.ok, listener)
                .setNegativeButton(android.R.string.cancel, listener)
                .create();
    }
}
