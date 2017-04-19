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
    private EditText editTime, editTemp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        dropdown = (Spinner) findViewById(R.id.spinner);
        editTime = (EditText) findViewById(R.id.edit_time);
        editTemp = (EditText) findViewById(R.id.edit_temp);

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
        //int interval = 0;
        String time = editTime.getText().toString();
        int temp = Integer.parseInt(editTemp.getText().toString());
        final AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(time.substring(0,1)));
        calendar.set(Calendar.MINUTE, Integer.parseInt(time.substring(3,4)));
        Intent i = new Intent(context, AlarmReceiver.class);
        i.putExtra("zimmer", zimmer_id);
        i.putExtra("temp", temp);
        i.putExtra("calendar", calendar);
        final PendingIntent pIntent = PendingIntent.getBroadcast(context, 0, i, 0); //different IDs for different alarms

        /*cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                am.cancel(pIntent);
            }
        });*/
    }
}
