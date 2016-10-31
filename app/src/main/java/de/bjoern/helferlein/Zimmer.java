package de.bjoern.helferlein;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;
import android.widget.EditText;
import android.widget.ToggleButton;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

public abstract class Zimmer extends Activity {
    private static final String TAG = "Zimmer";
    private JSONObject jObject;
    private Timer timer;
    private String username, password;

    protected abstract EditText getTempIst();

    protected abstract EditText getTempSoll();

    protected abstract ToggleButton getRelais1Button();

    protected abstract ToggleButton getRelais2Button();

    protected abstract ToggleButton getRelais3Button();

    protected abstract ToggleButton getRelais4Button();

    protected abstract String getID();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        username = prefs.getString("username", "");
        password = prefs.getString("password", "");
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        username = prefs.getString("username", "");
        password = prefs.getString("password", "");
        parseJSON(5000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        parseJSON(30*60*1000);
    }

    void parseJSON(int delay) {
        if (timer != null) {
            timer.cancel();
            timer.purge();
        }

        timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                try {
                    jObject = getJSONObject().getJSONObject(getID());
                    if (getTempIst() != null
                            && Float.parseFloat(getTempIst().getText().toString()) != jObject.getInt("hr20_tempis") / 100F) {
                        getTempIst().post(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    getTempIst().setText(Float.toString(jObject.getInt("hr20_tempis") / 100F));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                    if (getTempSoll() != null
                            && Float.parseFloat(getTempSoll().getText().toString()) != jObject.getInt("hr20_tempset") / 100F) {
                        getTempSoll().post(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    getTempSoll().setText(Float.toString(jObject.getInt("hr20_tempset") / 100F));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                    if (getRelais1Button() != null
                            && !((jObject.getJSONArray("relais").getJSONObject(0).getInt("state") == 1)
                            == (getRelais1Button().getText() == getRelais1Button().getTextOn()))) {
                        getRelais1Button().post(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    getRelais1Button().setChecked(jObject.getJSONArray("relais").getJSONObject(0).getInt("state") == 1);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                    if (getRelais2Button() != null
                            && !((jObject.getJSONArray("relais").getJSONObject(1).getInt("state") == 1)
                            == (getRelais2Button().getText() == getRelais2Button().getTextOn()))) {
                        getRelais2Button().post(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    getRelais2Button().setChecked(jObject.getJSONArray("relais").getJSONObject(1).getInt("state") == 1);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                    if (getRelais3Button() != null
                            && !((jObject.getJSONArray("relais").getJSONObject(2).getInt("state") == 1)
                            == (getRelais3Button().getText() == getRelais3Button().getTextOn()))) {
                        getRelais3Button().post(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    getRelais3Button().setChecked(jObject.getJSONArray("relais").getJSONObject(2).getInt("state") == 1);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                    if (getRelais4Button() != null
                            && !((jObject.getJSONArray("relais").getJSONObject(3).getInt("state") == 1)
                            == (getRelais4Button().getText() == getRelais4Button().getTextOn()))) {
                        getRelais4Button().post(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    getRelais4Button().setChecked(jObject.getJSONArray("relais").getJSONObject(3).getInt("state") == 1);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                } catch (JSONException | NullPointerException e) {
                    e.printStackTrace();
                }
            }
        };

        timer.scheduleAtFixedRate(task, delay, delay);
    }
	
	JSONObject getJSONObject() {
        //Log.v(TAG, "getJSONObject");
		JSONObject jObject = null;
		String encoding = Base64.encodeToString((username + ":" + password).getBytes(), Base64.NO_WRAP);
		HttpURLConnection connection;
		try {
			connection = (HttpURLConnection) new URL("http://www.bjoern-b.de/home-automation/had_get_json.php").openConnection();
			connection.setRequestProperty("Authorization", "Basic " + encoding);
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = in.readLine()) != null)
			{
				sb.append(line);
			}
			in.close();
			try {
				jObject = new JSONObject(sb.toString());
			} catch (JSONException | NullPointerException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return jObject;
	}
	
	void toggle(String relais, String address) {
		Log.v(TAG, "toggle");
		String encoding = Base64.encodeToString((username + ":" + password).getBytes(), Base64.NO_WRAP);
		HttpURLConnection connection;
		try {
			connection = (HttpURLConnection) new URL("http://www.bjoern-b.de/home-automation/status_table.php?action=set_" + relais + "&address=" + address).openConnection();
			connection.setRequestProperty("Authorization", "Basic " + encoding);
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	void setTemp(String address, String temp) {
        Log.v(TAG, "setTemp");
		temp = temp.replaceAll("[^0-9]", "");
		if (temp.length() < 3) temp += "0";
		String encoding = Base64.encodeToString((username + ":" + password).getBytes(), Base64.NO_WRAP);
		HttpURLConnection connection;
		try {
			connection = (HttpURLConnection) new URL("http://www.bjoern-b.de/home-automation/status_table.php?action=set_temperature&address=" + address + "&temperature=" + temp).openConnection();
			connection.setRequestProperty("Authorization", "Basic " + encoding);
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}