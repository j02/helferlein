package de.bjoern.helferlein;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ToggleButton;

import java.util.HashMap;

public class Esszimmer extends Zimmer {
    //private static final String TAG = "Esszimmer";
    private static final String ID = "24";
    private EditText tempIst, tempSoll;
    private final HashMap<String, String> map = new HashMap<>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_esszimmer);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        map.put("warm", prefs.getString("esszimmerWarm", ""));
        map.put("kalt", prefs.getString("esszimmerKalt", ""));
        tempIst = (EditText) findViewById(R.id.tempIst);
        tempSoll = (EditText) findViewById(R.id.tempSoll);

        Button setTempButton = (Button) findViewById(R.id.setTempButton);
        setTempButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        setTemp(ID, tempSoll.getText().toString());
                    }
                }).start();
            }
        });

        RadioButton buttonWarm = (RadioButton) findViewById(R.id.buttonWarm);
        buttonWarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            setTemp(ID, map.get("warm"));
                            findViewById(R.id.radioGroup).post(new Runnable() {
                                @Override
                                public void run() {
                                    ((RadioGroup) findViewById(R.id.radioGroup)).check(R.id.buttonWarm);
                                }
                            });
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });

        RadioButton buttonKalt = (RadioButton) findViewById(R.id.buttonKalt);
        buttonKalt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            setTemp(ID, map.get("kalt"));
                            findViewById(R.id.radioGroup).post(new Runnable() {
                                @Override
                                public void run() {
                                    ((RadioGroup) findViewById(R.id.radioGroup)).check(R.id.buttonKalt);
                                }
                            });
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });

        try {
            tempIst.setText(getIntent().getStringArrayExtra("temp")[0]);
            tempSoll.setText(getIntent().getStringArrayExtra("temp")[1]);
            if (tempSoll.getText().toString().equals(map.get("warm"))) ((RadioGroup) findViewById(R.id.radioGroup)).check(R.id.buttonWarm);
            if (tempSoll.getText().toString().equals(map.get("kalt"))) ((RadioGroup) findViewById(R.id.radioGroup)).check(R.id.buttonKalt);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public EditText getTempIst() { return tempIst; }

    @Override
    public EditText getTempSoll() { return tempSoll; }

    @Override
    public ToggleButton getRelais1Button() { return null; }

    @Override
    public ToggleButton getRelais2Button() { return null; }

    @Override
    public ToggleButton getRelais3Button() { return null; }

    @Override
    public ToggleButton getRelais4Button() { return null; }

    @Override
    public String getID() { return ID; }
}
