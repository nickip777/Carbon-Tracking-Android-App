package com.example.nick.carbontracker.UI;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nick.carbontracker.R;
import com.example.nick.carbontracker.model.Car;
import com.example.nick.carbontracker.model.Singleton;

/**
 * Creates a Dialog for data entry when user is trying to add a car
 */

public class AddCarDialog extends AppCompatDialogFragment {
    private Singleton singleton = Singleton.getInstance();
    private String userMake = "";
    private String userModel = "";
    private int userYear = 0;

    //arrays to store make model and year

    private int iconsID;

    //defaults
    private String[] defaultModel = {"Please Enter Make First!"};
    private String[] defaultYear = {"Please Enter Model First!"};
    private EditText editName;

    private int savedPosition = 0;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Create the view to show
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.add_car_dialog, null);
        final Singleton singleton = Singleton.getInstance();

        //setup edit text field
        editName = (EditText) v.findViewById(R.id.nickNameEditText);

        //initialize spinners
        final Spinner makeSpinner = (Spinner) v.findViewById(R.id.makeDropDown);
        final Spinner modelSpinner = (Spinner) v.findViewById(R.id.modelDropDown);
        final Spinner yearSpinner = (Spinner) v.findViewById(R.id.yearDropDown);
        final Spinner iconSpinner = (Spinner) v.findViewById(R.id.iconDropDown);



        // Populates the spinners with the available model and make choices
        populateSpinner(makeSpinner, singleton.getMakeSpinner());
        populateSpinner(modelSpinner, defaultModel);
        populateSpinner(yearSpinner, defaultModel);
        populateImageSpinner(iconSpinner);

        //when user selects make
        makeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    //populate model spinner
                    populateSpinner(modelSpinner, singleton.getModelSpinner()[position]);
                    savedPosition = position;
                    //save user selection
                    userMake = makeSpinner.getSelectedItem().toString();
                } else {
                    populateSpinner(modelSpinner, defaultModel);
                    userMake = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //when user selects model
        modelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //if user selects nothing
                if (savedPosition == 0) {
                    //populate spinner default
                    populateSpinner(yearSpinner, defaultModel);
                    userModel = "";
                } else if (position == 0) {
                    populateSpinner(yearSpinner, defaultYear);
                    userModel = "";
                }
                //when user selects model
                else {
                    //populate spinner default
                    populateSpinner(yearSpinner, singleton.getYearSpinner()[savedPosition][position]);
                    userModel = modelSpinner.getSelectedItem().toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //when user selects year
        yearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0)
                    //save user selection
                    userYear = Integer.parseInt(yearSpinner.getSelectedItem().toString());
                else
                    userYear = 0;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        iconSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch(position) {
                    case 0:
                        iconsID = R.drawable.car_red_icon;
                        break;
                    case 1:
                        iconsID = R.drawable.car_blue_icon;
                        break;
                    case 2:
                        iconsID = R.drawable.car_orange_icon;
                        break;
                    case 3:
                        iconsID = R.drawable.car_purple_icon;
                        break;
                    case 4:
                        iconsID = R.drawable.racing_car_icon;
                        break;
                    default:
                        Toast.makeText(getContext(),"no icon selected",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //check if in edit mode
        if (!getArguments().getBoolean("EditMode")) {
            // Create a button Listener
            DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Log.i("MyApp", "You clicked a dialog button. ");
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            //check nickname input
                            String nickName = editName.getText().toString();
                            if (userMake.length() == 0 && userModel.length() == 0 && userYear == 0 && nickName.length() == 0) {
                                Toast.makeText(getContext(), "Please Enter NickName, Make, Model, and Year!", Toast.LENGTH_LONG).show();
                            } else {
                                //create car class and store in singleton
                                Car car = new Car(userMake, userModel, nickName, "fueltype", "transmission", userYear, 1, 1, 1, iconsID);
                                singleton.checkCar(car, getContext());
                                if (singleton.getCars().size() > 1)
                                    showDialog();
                                else if (singleton.getCars().size() == 0)
                                    Toast.makeText(getContext(), "Sorry the car is not available", Toast.LENGTH_LONG).show();
                                else {
                                    singleton.addCar(singleton.getCars().get(0));
                                    updateList();
                                }
                                break;
                            }
                        case DialogInterface.BUTTON_NEGATIVE:
                            break;
                    }
                }
            };

            // Build the alert dialog
            return new AlertDialog.Builder(getActivity())
                    .setTitle(getString(R.string.add_car))
                    .setView(v)
                    .setPositiveButton(android.R.string.ok, listener)
                    .setNegativeButton(android.R.string.cancel, listener)
                    .create();
        } else { // Edit / Delete  Car
            final int position = getArguments().getInt("Position");
            updateEditText(v, position);
            DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Log.i("MyApp", "You clicked a dialog button. ");
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            //check user input for nickname
                            String nickName = editName.getText().toString();
                            if (userMake.length() == 0 && userModel.length() == 0 && userYear == 0 && nickName.length() == 0) {
                                Toast.makeText(getContext(), getString(R.string.please_enter), Toast.LENGTH_LONG).show();
                            } else {
                                //create new car class and add to singleton
                                Car car = new Car(userMake, userModel, nickName, "fueltype", "transmission", userYear, 1, 1, 1, iconsID);
                                singleton.checkCar(car, getContext());
                                if (singleton.getCars().size() > 1)
                                    showDialog();
                                else if (singleton.getCars().size() == 0)
                                    Toast.makeText(getContext(), getString(R.string.car_not_available), Toast.LENGTH_LONG).show();
                                else {
                                    singleton.updateJourneysCars(car, position);
                                    singleton.editCar(position, singleton.getCars().get(0));
                                    updateList();
                                }
                                break;
                            }

                        case DialogInterface.BUTTON_NEGATIVE:
                            break;
                        case DialogInterface.BUTTON_NEUTRAL:
                            singleton.getUserCars().remove(position);
                            updateList();

                    }
                }
            };

            // Build the alert dialog
            return new AlertDialog.Builder(getActivity())
                    .setTitle(getString(R.string.edit_delete_car))
                    .setView(v)
                    .setPositiveButton(android.R.string.ok, listener)
                    .setNegativeButton(android.R.string.cancel, listener)
                    .setNeutralButton(getString(R.string.delete), listener)
                    .create();

        }
    }

    // Updates EditText if editing
    private void updateEditText(View v, int position) {
        EditText ET = (EditText) v.findViewById(R.id.nickNameEditText);
        Car car = singleton.getUserCars().get(position);
        ET.setText(car.getNickName());
    }

    //show dialog
    private void showDialog() {
        Bundle bundle = new Bundle();
        bundle.putInt("Position", getArguments().getInt("Position"));
        bundle.putBoolean("EditMode", getArguments().getBoolean("EditMode"));

        FragmentManager manager = getFragmentManager();
        carListDialog dialog = new carListDialog();
        dialog.setArguments(bundle);
        dialog.show(manager, "carListDialog");
    }

    //update list view in activity
    private void updateList() {
        ((TransportationModeActivity) getActivity()).populateTransportationList();
        ((TransportationModeActivity) getActivity()).carClickCallback();
    }

    private void populateSpinner(Spinner spinner, String[] list) {
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getActivity(),
                R.layout.support_simple_spinner_dropdown_item,
                list);

        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

    }

    private void populateImageSpinner(Spinner spinner) {
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<Car> adapter = new MyListAdapter();

        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

    }

    private class MyListAdapter extends ArrayAdapter<Car>{
        public MyListAdapter() {
            super(getActivity(),R.layout.icon_car_spinner,R.id.icon_name_text,Singleton.getInstance().getCarIconSpinner());
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View itemView = convertView;
            if(itemView == null){
                Bundle bundle=new Bundle();
                itemView = getLayoutInflater(bundle).inflate(R.layout.icon_car_spinner,parent,false);
            }
            Car currentCar = Singleton.getInstance().getCarIconSpinner().get(position);

            ImageView imageView = (ImageView)itemView.findViewById(R.id.icon_item_spinner);
            imageView.setImageResource(currentCar.getIconID());

            TextView nickNameText = (TextView)itemView.findViewById(R.id.icon_name_text);
            nickNameText.setText(currentCar.getNickName());

            return itemView;
        }
    }

}
