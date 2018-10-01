package com.example.raghu.wifilocalization;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DisplayMessageActivity extends AppCompatActivity {

    int size = 0;
    List<ScanResult> results;

    String ITEM_KEY = "key";

    ArrayList<HashMap<String, String>> arraylist = new ArrayList<HashMap<String, String>>();
    SimpleAdapter adapter;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference("WIFI_DATA");

//    private Firebase mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        ArrayList<Integer> dbm_list = new ArrayList<Integer>();
        ArrayList<String> ssid_list = new ArrayList<String>();
        ArrayList<String> macid_list = new ArrayList<String>();

        dbm_list.clear();
        ssid_list.clear();
        macid_list.clear();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);
        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        String fingerprint = intent.getStringExtra(MainActivity.EXTRA_MESSAGE1);

        ListView lv = (ListView)findViewById(R.id.list);
        WifiManager wifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wifi.isWifiEnabled() == false)
        {
            Toast.makeText(getApplicationContext(), "wifi is disabled..making it enabled", Toast.LENGTH_LONG).show();
            wifi.setWifiEnabled(true);
        }
        this.adapter = new SimpleAdapter(DisplayMessageActivity.this, arraylist, R.layout.row, new String[] { ITEM_KEY }, new int[] { R.id.list_value });
        lv.setAdapter(this.adapter);

        results = wifi.getScanResults();
        size = results.size();
        int dbm1;
        String ssid1;
        String macid1;
        String s1="IIITV1";
        String s2="IIITV2";
        String s3="IIITV-Staff";
        for(int i=0;i<size;i++)
        {
            dbm1 = results.get(i).level;
            ssid1 = results.get(i).SSID;
            macid1 = results.get(i).BSSID;
            if(ssid1.equals(s1) || ssid1.equals(s2) || ssid1.equals(s3) )
            {
                dbm_list.add(dbm1);
                ssid_list.add(ssid1);
                macid_list.add(macid1);
            }
        }
        //String fingerprint = "F";
        databaseReference.push();
        databaseReference = databaseReference.child(fingerprint);

        for(int j=0;j<dbm_list.size();j++)
        {
            databaseReference.child(macid_list.get(j)).child("Room No").setValue(message);
            databaseReference.child(macid_list.get(j)).child("SSID").setValue(ssid_list.get(j));
            databaseReference.child(macid_list.get(j)).child("dBm").setValue(dbm_list.get(j));
        }
        try
        {
            size = size - 1;
            while (size >= 0)
            {
                HashMap<String, String> item = new HashMap<String, String>();
                item.put(ITEM_KEY, message+"  "+results.get(size).SSID + "  "+ results.get(size).BSSID+"    "+results.get(size).level+"dBm");

                arraylist.add(item);
                size--;
                adapter.notifyDataSetChanged();
            }
        }
        catch (Exception e)
        { }


    }


}

