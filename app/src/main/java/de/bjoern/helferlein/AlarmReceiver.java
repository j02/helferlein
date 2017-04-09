package de.bjoern.helferlein;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Base64;
import android.util.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class AlarmReceiver extends BroadcastReceiver { //TODO
    private static final String TAG = "AlarmReceiver";
    private String username, password;

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        username = prefs.getString("username", "");
        password = prefs.getString("password", "");
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "");
        wl.acquire();

        // Put here YOUR code.
        String zimmer = Integer.toString(intent.getIntExtra("zimmer", 0));
        String temp = intent.getStringExtra("temp");
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.icon)
                        .setContentTitle("Helferlein")
                        .setContentText("Zimmer " + zimmer + " Temp " + temp);
        builder.notify();

        wl.release(); //TODO: set alarms after reboot
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
