package de.bjoern.helferlein;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ToggleButton;
import java.util.HashMap;

public class Esszimmer extends Zimmer_Fragment {
    //private static final String TAG = "Esszimmer";
    private static final String ID = "24";
    private RelativeLayout rt;
    private EditText tempIst, tempSoll;
    private ProgressBar progress, progressTemp;
    private final HashMap<String, String> map = new HashMap<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_esszimmer, container, false);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        map.put("warm", prefs.getString("esszimmerWarm", ""));
        map.put("kalt", prefs.getString("esszimmerKalt", ""));
        tempIst = (EditText) view.findViewById(R.id.tempIst);
        tempSoll = (EditText) view.findViewById(R.id.tempSoll);

        rt = (RelativeLayout) view.findViewById(R.id.rt);
        rt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                rt.requestFocus();
            }
        });

        progress = (ProgressBar) view.findViewById(R.id.progress);
        progressTemp = (ProgressBar) view.findViewById(R.id.progressTemp);

        Button setTempButton = (Button) view.findViewById(R.id.setTempButton);
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

        RadioButton buttonWarm = (RadioButton) view.findViewById(R.id.buttonWarm);
        buttonWarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            setTemp(ID, map.get("warm"));
                            view.findViewById(R.id.radioGroup).post(new Runnable() {
                                @Override
                                public void run() {
                                    ((RadioGroup) view.findViewById(R.id.radioGroup)).check(R.id.buttonWarm);
                                }
                            });
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });

        RadioButton buttonKalt = (RadioButton) view.findViewById(R.id.buttonKalt);
        buttonKalt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            setTemp(ID, map.get("kalt"));
                            view.findViewById(R.id.radioGroup).post(new Runnable() {
                                @Override
                                public void run() {
                                    ((RadioGroup) view.findViewById(R.id.radioGroup)).check(R.id.buttonKalt);
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
            tempIst.setText(getActivity().getIntent().getStringArrayExtra("temp")[0]);
            tempSoll.setText(getActivity().getIntent().getStringArrayExtra("temp")[1]);
            if (tempSoll.getText().toString().equals(map.get("warm"))) ((RadioGroup) view.findViewById(R.id.radioGroup)).check(R.id.buttonWarm);
            if (tempSoll.getText().toString().equals(map.get("kalt"))) ((RadioGroup) view.findViewById(R.id.radioGroup)).check(R.id.buttonKalt);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        return view;
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

    @Override
    public ProgressBar getProgressBar() { return progress; }

    @Override
    public ProgressBar getProgressBarTemp() { return progressTemp; }
}
