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
import android.widget.EditText;
import android.widget.Toast;

import com.example.nick.carbontracker.R;
import com.example.nick.carbontracker.model.Route;
import com.example.nick.carbontracker.model.Singleton;

/**
 * Creates a Dialog for data entry when user is trying to add a route
 * checks if in edit mode
 * saves route to singleton
 */

public class AddRouteDialog extends AppCompatDialogFragment {
    private Singleton singleton = Singleton.getInstance();

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Create the view to show
        final View v = LayoutInflater.from(getActivity()).inflate(R.layout.add_route_dialog, null);

        // Create a button Listener
        if (!getArguments().getBoolean("EditMode")) {
            DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Log.i("MyApp", "You clicked a dialog button. ");
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            //save and update
                            saveRouteToUserList(v);
                            updateList();
                        case DialogInterface.BUTTON_NEGATIVE:
                            break;
                    }
                }
            };
            // Build the alert dialog
            return new AlertDialog.Builder(getActivity())
                    .setTitle(getString(R.string.addRoute))
                    .setView(v)
                    .setPositiveButton(android.R.string.ok, listener)
                    .setNegativeButton(android.R.string.cancel, listener)
                    .create();
        } else {
            // Edit and Delete Dialog
            final int position = getArguments().getInt("Position");
            populateEditTexts(v, position);

            DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Log.i("MyApp", "You clicked a dialog button. ");
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            //save and update listview
                            saveRouteToUserList(v);
                            updateList();
                            break;
                        case DialogInterface.BUTTON_NEUTRAL:
                            //delete route and update
                            deleteRoute(position);
                            updateList();
                            break;
                        case DialogInterface.BUTTON_NEGATIVE:
                            break;
                    }
                }
            };

            // Build the alert dialog
            return new AlertDialog.Builder(getActivity())
                    .setTitle(getString(R.string.editRoute))
                    .setView(v)
                    .setPositiveButton(android.R.string.ok, listener)
                    .setNeutralButton(getString(R.string.delete), listener)
                    .setNegativeButton(android.R.string.cancel, listener)
                    .create();
        }

    }

    private void saveRouteToUserList(View v) {
        // Get EditTexts
        EditText inputName = (EditText) v.findViewById(R.id.routeNameEditText);
        EditText inputHiDist = (EditText) v.findViewById(R.id.hiDistEditText);
        EditText inputCityDist = (EditText) v.findViewById(R.id.cityDistEditText);

        // Check if any editText is empty
        if (isEmpty(inputHiDist,"") && isEmpty(inputCityDist,"")) {
            // Convert editText data to integers
            String routeName = inputName.getText().toString();
            Double hiDist = Double.parseDouble(inputHiDist.getText().toString());
            Double cityDist = Double.parseDouble(inputCityDist.getText().toString());

            // Check whether any distance is zero
            if (checkIfZero(cityDist) && checkIfZero(hiDist)) {
                saveRoute(routeName, hiDist, cityDist);
            } else { // Display  Error
                String ErrorZero = getString(R.string.zeroError);
                Toast.makeText(getContext(), ErrorZero, Toast.LENGTH_SHORT).show();
            }
        } else if (isEmpty(inputHiDist,"") && !isEmpty(inputCityDist,"")) {
            // Convert editText data to integers
            String routeName = inputName.getText().toString();
            Double hiDist = Double.parseDouble(inputHiDist.getText().toString());
            Double cityDist = 0.0;

            // Check whether any distance is zero
            if (checkIfZero(hiDist)) {
                saveRoute(routeName, hiDist, cityDist);
            } else { // Display  Error
                String ErrorZero = getString(R.string.zeroError);
                Toast.makeText(getContext(), ErrorZero, Toast.LENGTH_SHORT).show();
            }
        } else if(!isEmpty(inputHiDist,"") && isEmpty(inputCityDist,"")) {
            // Convert editText data to integers
            String routeName = inputName.getText().toString();
            Double hiDist = 0.0;
            Double cityDist = Double.parseDouble(inputCityDist.getText().toString());

            // Check whether any distance is zero
            if (checkIfZero(cityDist)) {
                saveRoute(routeName, hiDist, cityDist);
            } else { // Display  Error
                String ErrorZero = getString(R.string.zeroError);
                Toast.makeText(getContext(), ErrorZero, Toast.LENGTH_SHORT).show();
            }
        } else { // Display Error
            String ErrorNull = getString(R.string.nullError);
            Toast.makeText(getContext(), ErrorNull, Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkIfZero(Double dist) {
        double tol = 0.000000001;
        if (dist > tol) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    //check if edit texts are empty
    private boolean isEmpty(EditText ET, String cond) {
        if (ET.getText().toString().equals(cond)) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    // Saves or edits route
    private void saveRoute(String routeName, double hiDist, double cityDist) {
        if (!getArguments().getBoolean("EditMode")) {
            // Save Route
            String entryValid = getString(R.string.added_route_with_name) + routeName +
                    getString(R.string.high_distance) + hiDist +
                    getString(R.string.city_dist) + cityDist;
            Route userRoute = new Route(routeName, hiDist, cityDist);
            Toast.makeText(getContext(), entryValid, Toast.LENGTH_LONG).show();
            singleton.addRoute(userRoute);
        } else { // Edit route
            int position = getArguments().getInt("Position");
            String entryValid = getString(R.string.added_route_with_name) + routeName +
                    getString(R.string.high_distance) + hiDist +
                    getString(R.string.city_dist) + cityDist;
            Route userRoute = new Route(routeName, hiDist, cityDist);
            Toast.makeText(getContext(), entryValid, Toast.LENGTH_LONG).show();
            singleton.updateJourneysRoutes(userRoute, position);
            singleton.editRoute(position, userRoute);
        }

    }

    //update listview in activity
    private void updateList() {
        ((SelectRouteActivity) getActivity()).populateRouteList();
        ((SelectRouteActivity) getActivity()).routeClickCallback();
    }

    //populate text fields for edit
    private void populateEditTexts(View v, int position) {
        EditText Name = (EditText) v.findViewById(R.id.routeNameEditText);
        EditText HiDist = (EditText) v.findViewById(R.id.hiDistEditText);
        EditText CityDist = (EditText) v.findViewById(R.id.cityDistEditText);

        Route route = singleton.getUserRoutes().get(position);
        String hDist = "" + route.getHighwayDistance();
        String cDist = "" + route.getCityDistance();

        Name.setText(route.getName());
        HiDist.setText(hDist);
        CityDist.setText(cDist);
    }

    private void deleteRoute(int pos) {
        singleton.getUserRoutes().remove(pos);
    }
}



