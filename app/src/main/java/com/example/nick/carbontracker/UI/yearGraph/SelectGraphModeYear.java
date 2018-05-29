package com.example.nick.carbontracker.UI.yearGraph;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.nick.carbontracker.R;

/**
 * select graph mode for year
 */
public class SelectGraphModeYear extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_graph_mode_year);
        setModeButton();
        setRouteButton();
        setStackedButton();
    }
    //METHODS FOR ACTION BAR******************************************************************
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    //when user clicks action bar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_cancel_button:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    private void setStackedButton() {
        Button btn = (Button) findViewById(R.id.stackedGraphYear);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), YearGraph.class);
                startActivity(intent);
            }
        });
    }

    private void setRouteButton() {
        Button btn = (Button) findViewById(R.id.routeGraphYear);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), YearRouteGraph.class);
                startActivity(intent);
            }
        });
    }

    private void setModeButton() {
        Button btn = (Button) findViewById(R.id.modeGraphYear);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), YearModePieGraph.class);
                startActivity(intent);
            }
        });
    }
}
