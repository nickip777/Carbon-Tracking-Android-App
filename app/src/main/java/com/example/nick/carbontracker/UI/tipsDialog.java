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
import android.widget.Button;
import android.widget.TextView;

import com.example.nick.carbontracker.R;
import com.example.nick.carbontracker.model.Singleton;

/**
 * Displays the tips in a dialog
 * Has a next button to traverse the tips
 */

public class tipsDialog extends AppCompatDialogFragment {
    private Singleton singleton= Singleton.getInstance();
    private int index = 0;
    private int size = singleton.getTipsSize();

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Create the view to show
        final View v = LayoutInflater.from(getActivity()).inflate(R.layout.tips_dialog, null);

        Button next = (Button) v.findViewById(R.id.nextTipButton);
        updateTextViews(v);
        activateButton(next,v);

        // Create a button Listener
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.i("MyApp", "You clicked a dialog button. ");
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        singleton.saveUserData(getContext(), Singleton.TIP_HISTORY);
                        break;
                }
            }
        };
        // Build the alert dialog
        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.tips_to_reduce)
                .setView(v)
                .setPositiveButton(R.string.close, listener)
                .create();
    }

    private void activateButton(final Button button, final View layout) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch(button.getId()) {
                    case R.id.nextTipButton:
                        index = (index + 1) % size;
                        break;
                    default:
                        break;
                }
                updateTextViews(layout);
            }
        });
    }

    private void updateTextViews(View v) {
        //TextView prompt = (TextView) v.findViewById(R.id.tipsPromptTV);
        TextView counter = (TextView) v.findViewById(R.id.indexDisplayTV );
        TextView tip = (TextView) v.findViewById(R.id.tipsDisplayTV );

        String promptText = getString(R.string.you_had) + singleton.numCarTrips() +getString(R.string.car_trips_generating) +
                singleton.totalCarbonConsumedCar() + "kg of CO2." ;
        String counterText = (index+1) + "/" + size;

        //prompt.setText(promptText);
        counter.setText(counterText);
        tip.setText(singleton.getTip(index));
    }
}
