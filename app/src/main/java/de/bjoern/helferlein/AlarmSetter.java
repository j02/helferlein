package de.bjoern.helferlein;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import java.util.Calendar;

public class AlarmSetter extends AppCompatActivity { //TODO
    //private static final String TAG = "AlarmSetter";
    private EditText m, h, dom, mon, dow, temp;
    private int zimmer_id;
    private Spinner dropdown;
    private Button ok, cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        dropdown = (Spinner) findViewById(R.id.spinner);
        m = (EditText) findViewById(R.id.m_0);
        h = (EditText) findViewById(R.id.h_0);
        dom = (EditText) findViewById(R.id.dom_0);
        mon = (EditText) findViewById(R.id.mon_0);
        dow = (EditText) findViewById(R.id.dow_0);
        temp = (EditText) findViewById(R.id.temp_0);
        /*HashMap<Integer, String> map = new HashMap<>();
        map.put(17, "Arbeitszimmer");
        map.put(22, "Badezimmer");
        map.put(23, "Esszimmer");
        map.put(21, "Schlafzimmer");
        map.put(20, "Wohnzimmer");
        String[] items = new String[]{map.get(17), map.get(22), map.get(23), map.get(21), map.get(20)};*/
        String[] items = new String[]{"Arbeitszimmer", "Badezimmer", "Esszimmer", "Schlafzimmer", "Wohnzimmer"};
        ArrayAdapter<String> adapter = new ArrayAdapter<> (this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        zimmer_id = 17;
                        break;
                    case 1:
                        zimmer_id = 22;
                        break;
                    case 2:
                        zimmer_id = 23;
                        break;
                    case 3:
                        zimmer_id = 21;
                        break;
                    case 4:
                        zimmer_id = 20;
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //nothing
            }
        });
        ok = (Button) findViewById(R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAlarm(getApplicationContext());
            }
        });
    }

    public void setAlarm(Context context)
    {
        int interval = 0;
        final AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, AlarmReceiver.class);
        i.putExtra("zimmer", zimmer_id);
        i.putExtra("temp", temp.getText().toString());
        final PendingIntent pIntent = PendingIntent.getBroadcast(context, 0, i, 0); //different IDs for different alarms
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.SUNDAY);
        if (!mon.getText().toString().equals("0")) calendar.set(Calendar.MONTH, Integer.parseInt(mon.getText().toString()));
        if (dow.getText().toString().equals("0")) {
            interval = 1000 * 60 * 60 * 24;
        } else {
            calendar.set(Calendar.DAY_OF_WEEK, Integer.parseInt(dow.getText().toString()));
        }
        if (dom.getText().toString().equals("0")) {
            interval = 1000 * 60 * 60 * 24;
        } else {
            calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dom.getText().toString()));
        }
        if (h.getText().toString().equals("0")) {
            interval = 1000 * 60 * 60;
        } else {
            calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(h.getText().toString()));
        }
        if (m.getText().toString().equals("0")) {
            interval = 1000 * 60;
        } else {
            calendar.set(Calendar.MINUTE, Integer.parseInt(m.getText().toString()));
        }
        if (interval != 0) {
            am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), interval, pIntent);
            Toast.makeText(context, "setRepeating " + Integer.toString(interval), Toast.LENGTH_SHORT).show();
        } else {
            am.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pIntent);
            Toast.makeText(context, "setExact " + Long.toString(calendar.getTimeInMillis()), Toast.LENGTH_SHORT).show();
        }

        /*cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                am.cancel(pIntent);
            }
        });*/
    }
}
