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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.nick.carbontracker.R;
import com.example.nick.carbontracker.model.Singleton;

/**
 * Dialog for add car
 * adds car to singleton
 * populates spinner for dropdown menu
 * checks for edit mode and has delete button
 */

public class carListDialog extends AppCompatDialogFragment {
    private int carPosition;
    private Singleton singleton = Singleton.getInstance();

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.car_list_dialog, null);

        //populate listview
        populateCarList(v);

        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.i("MyApp", "You clicked a dialog button. ");
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        //add car to singleton
                        int position = getArguments().getInt("Position");
                        if (!getArguments().getBoolean("EditMode")) {
                            singleton.addCar(singleton.getCars().get(carPosition));
                        }
                        //else update car in current singleton
                        else {
                            singleton.updateJourneysCars(singleton.getCars().get(carPosition), position);
                            singleton.editCar(position, singleton.getCars().get(carPosition));
                        }
                        //update listview
                        updateList();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };
        return new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.carList))
                .setView(v)
                .setPositiveButton(android.R.string.ok, listener)
                .setNegativeButton(android.R.string.cancel, listener)
                .create();
    }

    //update listview
    private void updateList() {
        ((TransportationModeActivity) getActivity()).populateTransportationList();
        ((TransportationModeActivity) getActivity()).carClickCallback();
    }

    //populate the list view
    public void populateCarList(View v) {
        String[] carList = new String[singleton.getCars().size()];
        for (int i = 0; i < singleton.getCars().size(); i++) {
            carList[i] = singleton.getCars().get(i).getMake() + " " + singleton.getCars().get(i).getModelName()
                    + " " + singleton.getCars().get(i).getYearMade() + " " + singleton.getCars().get(i).getTransmission()
                    + " " + singleton.getCars().get(i).getEngineDisplacement() + " L";
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.car_list_dialog, R.id.carList, carList);

        ListView list = (ListView) v.findViewById(R.id.listOfCarData);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                carPosition = position;
            }
        });
    }
}
