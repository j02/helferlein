package de.bjoern.helferlein;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.ToggleButton;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class Main extends Zimmer {
    //private static final String TAG = "Main";
    private JSONObject jObject;
    private Timer timer;
    private final HashMap<String, String> map = new HashMap<>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        map.put("wohnzimmerWarm", prefs.getString("wohnzimmerWarm", ""));
        map.put("wohnzimmerKalt", prefs.getString("wohnzimmerKalt", ""));
        map.put("esszimmerWarm", prefs.getString("esszimmerWarm", ""));
        map.put("esszimmerKalt", prefs.getString("esszimmerKalt", ""));
        map.put("schlafzimmerWarm", prefs.getString("schlafzimmerWarm", ""));
        map.put("schlafzimmerKalt", prefs.getString("schlafzimmerKalt", ""));
        map.put("badezimmerWarm", prefs.getString("badezimmerWarm", ""));
        map.put("badezimmerKalt", prefs.getString("badezimmerKalt", ""));
        map.put("arbeitszimmerWarm", prefs.getString("arbeitszimmerWarm", ""));
        map.put("arbeitszimmerKalt", prefs.getString("arbeitszimmerKalt", ""));

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                jObject = getJSONObject();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (Float.toString(jObject.getJSONObject("21").getInt("hr20_tempset") / 100F).equals(map.get("wohnzimmerWarm"))
                                    && Float.toString(jObject.getJSONObject("24").getInt("hr20_tempset") / 100F).equals(map.get("esszimmerWarm"))
                                    && Float.toString(jObject.getJSONObject("22").getInt("hr20_tempset") / 100F).equals(map.get("schlafzimmerWarm"))
                                    && Float.toString(jObject.getJSONObject("23").getInt("hr20_tempset") / 100F).equals(map.get("badezimmerWarm"))
                                    && Float.toString(jObject.getJSONObject("18").getInt("hr20_tempset") / 100F).equals(map.get("arbeitszimmerWarm"))) {
                                ((RadioGroup) findViewById(R.id.radioGroup)).check(R.id.buttonWarm);
                            } else if (Float.toString(jObject.getJSONObject("21").getInt("hr20_tempset") / 100F).equals(map.get("wohnzimmerKalt"))
                                    && Float.toString(jObject.getJSONObject("24").getInt("hr20_tempset") / 100F).equals(map.get("esszimmerKalt"))
                                    && Float.toString(jObject.getJSONObject("22").getInt("hr20_tempset") / 100F).equals(map.get("schlafzimmerKalt"))
                                    && Float.toString(jObject.getJSONObject("23").getInt("hr20_tempset") / 100F).equals(map.get("badezimmerKalt"))
                                    && Float.toString(jObject.getJSONObject("18").getInt("hr20_tempset") / 100F).equals(map.get("arbeitszimmerKalt"))) {
                                ((RadioGroup) findViewById(R.id.radioGroup)).check(R.id.buttonKalt);
                            } else {
                                ((RadioGroup) findViewById(R.id.radioGroup)).clearCheck();
                            }
                        } catch (JSONException | NullPointerException e) {
                            if (e instanceof NullPointerException) Toast.makeText(getApplicationContext(), "Wrong username/password", Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
        t.start();

        findViewById(R.id.abstellkammer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(Main.this, Abstellkammer.class);
                int[] intsRelais = new int[4];
                try {
                    for (int i = 0; i < 4; i++) intsRelais[i] = jObject.getJSONObject("20").getJSONArray("relais").getJSONObject(i).getInt("state");
                    myIntent.putExtra("relais", intsRelais);
                } catch (JSONException | NullPointerException e) {
                    e.printStackTrace();
                }
                if (jObject != null) Main.this.startActivity(myIntent);
                else Toast.makeText(getApplicationContext(), "Wrong username/password", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.arbeitszimmer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(Main.this, Arbeitszimmer.class);
                int[] intsRelais = new int[4];
                try {
                    for (int i = 0; i < 4; i++) intsRelais[i] = jObject.getJSONObject("18").getJSONArray("relais").getJSONObject(i).getInt("state");
                    myIntent.putExtra("temp", new String[] {
                            Float.toString(jObject.getJSONObject("18").getInt("hr20_tempis") / 100F),
                            Float.toString(jObject.getJSONObject("18").getInt("hr20_tempset") / 100F),
                            Integer.toString(jObject.getJSONObject("18").getInt("hr20_window")).equals("0") ? "Zu" : "Auf"
                    });
                    myIntent.putExtra("relais", intsRelais);
                } catch (JSONException | NullPointerException e) {
                    e.printStackTrace();
                }
                if (jObject != null) Main.this.startActivity(myIntent);
                else Toast.makeText(getApplicationContext(), "Wrong username/password", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.badezimmer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(Main.this, Badezimmer.class);
                int[] intsRelais = new int[4];
                try {
                    for (int i = 0; i < 4; i++) intsRelais[i] = jObject.getJSONObject("23").getJSONArray("relais").getJSONObject(i).getInt("state");
                    myIntent.putExtra("temp", new String[] {
                            Float.toString(jObject.getJSONObject("23").getInt("hr20_tempis") / 100F),
                            Float.toString(jObject.getJSONObject("23").getInt("hr20_tempset") / 100F),
                    });
                    myIntent.putExtra("relais", intsRelais);
                } catch (JSONException | NullPointerException e) {
                    e.printStackTrace();
                }
                if (jObject != null) Main.this.startActivity(myIntent);
                else Toast.makeText(getApplicationContext(), "Wrong username/password", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.esszimmer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(Main.this, Esszimmer.class);
                try {
                    myIntent.putExtra("temp", new String[] {
                            Float.toString(jObject.getJSONObject("24").getInt("hr20_tempis") / 100F),
                            Float.toString(jObject.getJSONObject("24").getInt("hr20_tempset") / 100F)
                    });
                } catch (JSONException | NullPointerException e) {
                    e.printStackTrace();
                }
                if (jObject != null) Main.this.startActivity(myIntent);
                else Toast.makeText(getApplicationContext(), "Wrong username/password", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.schlafzimmer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(Main.this, Schlafzimmer.class);
                int[] intsRelais = new int[4];
                try {
                    for (int i = 0; i < 4; i++) intsRelais[i] = jObject.getJSONObject("22").getJSONArray("relais").getJSONObject(i).getInt("state");
                    myIntent.putExtra("temp", new String[] {
                            Float.toString(jObject.getJSONObject("22").getInt("hr20_tempis") / 100F),
                            Float.toString(jObject.getJSONObject("22").getInt("hr20_tempset") / 100F),
                            Integer.toString(jObject.getJSONObject("22").getInt("hr20_window")).equals("0") ? "Zu" : "Auf"
                    });
                    myIntent.putExtra("relais", intsRelais);
                } catch (JSONException | NullPointerException e) {
                    e.printStackTrace();
                }
                if (jObject != null) Main.this.startActivity(myIntent);
                else Toast.makeText(getApplicationContext(), "Wrong username/password", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.tvundmusik).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(Main.this, TVundMusik.class);
                int[] intsRelais = new int[4];
                try {
                    for (int i = 0; i < 4; i++) intsRelais[i] = jObject.getJSONObject("19").getJSONArray("relais").getJSONObject(i).getInt("state");
                    myIntent.putExtra("relais", intsRelais);
                } catch (JSONException | NullPointerException e) {
                    e.printStackTrace();
                }
                if (jObject != null) Main.this.startActivity(myIntent);
                else Toast.makeText(getApplicationContext(), "Wrong username/password", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.wohnzimmer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(Main.this, Wohnzimmer.class);
                int[] intsRelais = new int[4];
                try {
                    for (int i = 0; i < 4; i++) intsRelais[i] = jObject.getJSONObject("21").getJSONArray("relais").getJSONObject(i).getInt("state");
                    myIntent.putExtra("temp", new String[] {
                            Float.toString(jObject.getJSONObject("21").getInt("hr20_tempis") / 100F),
                            Float.toString(jObject.getJSONObject("21").getInt("hr20_tempset") / 100F),
                            Integer.toString(jObject.getJSONObject("21").getInt("hr20_window")).equals("0") ? "Zu" : "Auf"
                    });
                    myIntent.putExtra("relais", intsRelais);
                } catch (JSONException | NullPointerException e) {
                    e.printStackTrace();
                }
                if (jObject != null) Main.this.startActivity(myIntent);
                else Toast.makeText(getApplicationContext(), "Wrong username/password", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.settings).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Main.this, SettingsActivity.class));
            }
        });

        findViewById(R.id.buttonWarm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            setTemp("21", map.get("wohnzimmerWarm")); //Wohnzimmer
                            setTemp("24", map.get("esszimmerWarm")); //Esszimmer
                            setTemp("22", map.get("schlafzimmerWarm")); //Schlafzimmer
                            setTemp("23", map.get("badezimmerWarm")); //Badezimmer
                            setTemp("18", map.get("arbeitszimmerWarm")); //Arbeitszimmer
                            findViewById(R.id.radioGroup).post(new Runnable() {
                                @Override
                                public void run() {
                                    ((RadioGroup) findViewById(R.id.radioGroup)).check(R.id.buttonWarm);
                                }
                            });
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Wrong username/password", Toast.LENGTH_SHORT).show();
                        }

                    }
                }).start();
            }
        });

        findViewById(R.id.buttonKalt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            setTemp("21", map.get("wohnzimmerKalt")); //Wohnzimmer
                            setTemp("24", map.get("esszimmerKalt")); //Esszimmer
                            setTemp("22", map.get("schlafzimmerKalt")); //Schlafzimmer
                            setTemp("23", map.get("badezimmerKalt")); //Badezimmer
                            setTemp("18", map.get("arbeitszimmerKalt")); //Arbeitszimmer
                            findViewById(R.id.radioGroup).post(new Runnable() {
                                @Override
                                public void run() {
                                    ((RadioGroup) findViewById(R.id.radioGroup)).check(R.id.buttonKalt);
                                }
                            });
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Wrong username/password", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).start();
            }
        });
    }

    @Override
    protected void parseJSON(int delay) {
        if (timer != null) {
            timer.cancel();
            timer.purge();
        }

        timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                //Log.v(TAG, "parseJSON");
                jObject = getJSONObject();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (Float.toString(jObject.getJSONObject("21").getInt("hr20_tempset") / 100F).equals(map.get("wohnzimmerWarm"))
                                    && Float.toString(jObject.getJSONObject("24").getInt("hr20_tempset") / 100F).equals(map.get("esszimmerWarm"))
                                    && Float.toString(jObject.getJSONObject("22").getInt("hr20_tempset") / 100F).equals(map.get("schlafzimmerWarm"))
                                    && Float.toString(jObject.getJSONObject("23").getInt("hr20_tempset") / 100F).equals(map.get("badezimmerWarm"))
                                    && Float.toString(jObject.getJSONObject("18").getInt("hr20_tempset") / 100F).equals(map.get("arbeitszimmerWarm"))) {
                                ((RadioGroup) findViewById(R.id.radioGroup)).check(R.id.buttonWarm);
                            } else if (Float.toString(jObject.getJSONObject("21").getInt("hr20_tempset") / 100F).equals(map.get("wohnzimmerKalt"))
                                    && Float.toString(jObject.getJSONObject("24").getInt("hr20_tempset") / 100F).equals(map.get("esszimmerKalt"))
                                    && Float.toString(jObject.getJSONObject("22").getInt("hr20_tempset") / 100F).equals(map.get("schlafzimmerKalt"))
                                    && Float.toString(jObject.getJSONObject("23").getInt("hr20_tempset") / 100F).equals(map.get("badezimmerKalt"))
                                    && Float.toString(jObject.getJSONObject("18").getInt("hr20_tempset") / 100F).equals(map.get("arbeitszimmerKalt"))) {
                                ((RadioGroup) findViewById(R.id.radioGroup)).check(R.id.buttonKalt);
                            } else {
                                ((RadioGroup) findViewById(R.id.radioGroup)).clearCheck();
                            }
                        } catch (JSONException | NullPointerException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        };
        timer.scheduleAtFixedRate(task, delay, delay);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        map.put("wohnzimmerWarm", prefs.getString("wohnzimmerWarm", ""));
        map.put("wohnzimmerKalt", prefs.getString("wohnzimmerKalt", ""));
        map.put("esszimmerWarm", prefs.getString("esszimmerWarm", ""));
        map.put("esszimmerKalt", prefs.getString("esszimmerKalt", ""));
        map.put("schlafzimmerWarm", prefs.getString("schlafzimmerWarm", ""));
        map.put("schlafzimmerKalt", prefs.getString("schlafzimmerKalt", ""));
        map.put("badezimmerWarm", prefs.getString("badezimmerWarm", ""));
        map.put("badezimmerKalt", prefs.getString("badezimmerKalt", ""));
        map.put("arbeitszimmerWarm", prefs.getString("arbeitszimmerWarm", ""));
        map.put("arbeitszimmerKalt", prefs.getString("arbeitszimmerKalt", ""));
    }

    @Override
    public EditText getTempIst() { return null; }

    @Override
    public EditText getTempSoll() { return null; }

    @Override
    public ToggleButton getRelais1Button() { return null; }

    @Override
    public ToggleButton getRelais2Button() { return null; }

    @Override
    public ToggleButton getRelais3Button() { return null; }

    @Override
    public ToggleButton getRelais4Button() { return null; }

    @Override
    public String getID() { return null; }
}

