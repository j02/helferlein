package de.bjoern.helferlein;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Main_Tabbed extends AppCompatActivity {
    private static final String TAG = "Main_Tabbed";
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private String username, password;
    private final HashMap<String, String> map = new HashMap<>();

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        switch (item.getItemId()) {
            case R.id.warm:
                new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        setTemp("21", map.get("wohnzimmerWarm")); //Wohnzimmer
                        setTemp("24", map.get("esszimmerWarm")); //Esszimmer
                        setTemp("22", map.get("schlafzimmerWarm")); //Schlafzimmer
                        setTemp("23", map.get("badezimmerWarm")); //Badezimmer
                        setTemp("18", map.get("arbeitszimmerWarm")); //Arbeitszimmer
                        findViewById(R.id.warm).post(new Runnable() {
                            @Override
                            public void run() {
                                ((TextView) findViewById(R.id.warm)).setTextColor(Color.RED);
                                ((TextView) findViewById(R.id.kalt)).setTextColor(Color.WHITE);
                            }
                        });
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "wrong username/password", Toast.LENGTH_SHORT).show();
                    }

                }
            }).start();
                return true;

            case R.id.kalt:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            setTemp("21", map.get("wohnzimmerKalt")); //Wohnzimmer
                            setTemp("24", map.get("esszimmerKalt")); //Esszimmer
                            setTemp("22", map.get("schlafzimmerKalt")); //Schlafzimmer
                            setTemp("23", map.get("badezimmerKalt")); //Badezimmer
                            setTemp("18", map.get("arbeitszimmerKalt")); //Arbeitszimmer
                            findViewById(R.id.warm).post(new Runnable() {
                                @Override
                                public void run() {
                                    ((TextView) findViewById(R.id.kalt)).setTextColor(Color.GREEN);
                                    ((TextView) findViewById(R.id.warm)).setTextColor(Color.WHITE);
                                }
                            });
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "wrong username/password", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).start();
                return true;

            case R.id.settings:
                Main_Tabbed.this.startActivity(new Intent(Main_Tabbed.this, SettingsActivity.class));
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_tabbed);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        username = prefs.getString("username", "");
        password = prefs.getString("password", "");
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
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new Abstellkammer(), "Abstellkammer");
        adapter.addFragment(new Arbeitszimmer(), "Arbeitszimmer");
        adapter.addFragment(new Badezimmer(), "Badezimmer");
        adapter.addFragment(new Esszimmer(), "Esszimmer");
        adapter.addFragment(new Schlafzimmer(), "Schlafzimmer");
        adapter.addFragment(new TVundMusik(), "TV und Musik");
        adapter.addFragment(new Wohnzimmer(), "Wohnzimmer");
        viewPager.setAdapter(adapter);
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

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}