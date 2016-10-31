package de.bjoern.helferlein;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ToggleButton;

public class TVundMusik extends Zimmer {
    //private static final String TAG = "TVundMusik";
    private static final String ID = "19";
    private ToggleButton relais1Button, relais2Button, relais3Button;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tvundmusik);

        relais1Button = (ToggleButton) findViewById(R.id.relais1Button);
        relais1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        toggle("relais1", ID);
                    }
                }).start();
            }
        });

        relais2Button = (ToggleButton) findViewById(R.id.relais2Button);
        relais2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        toggle("relais2", ID);
                    }
                }).start();
            }
        });

        relais3Button = (ToggleButton) findViewById(R.id.relais3Button);
        relais3Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        toggle("relais3", ID);
                    }
                }).start();
            }
        });

        try {
            relais1Button.setChecked(getIntent().getIntArrayExtra("relais")[0] == 1);
            relais2Button.setChecked(getIntent().getIntArrayExtra("relais")[1] == 1);
            relais3Button.setChecked(getIntent().getIntArrayExtra("relais")[2] == 1);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public EditText getTempIst() { return null; }

    @Override
    public EditText getTempSoll() { return null; }

    @Override
    public ToggleButton getRelais1Button() { return relais1Button; }

    @Override
    public ToggleButton getRelais2Button() { return relais2Button; }

    @Override
    public ToggleButton getRelais3Button() { return relais3Button; }

    @Override
    public ToggleButton getRelais4Button() { return null; }

    @Override
    public String getID() { return ID; }
}
