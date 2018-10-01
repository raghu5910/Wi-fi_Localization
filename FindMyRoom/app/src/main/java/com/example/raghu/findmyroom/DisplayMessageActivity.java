package com.example.raghu.findmyroom;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class DisplayMessageActivity extends AppCompatActivity {


    private static final String TAG = "ViewDatabase";
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private  String userID;
    List<ScanResult> results;
    int size=0;
    static int index = 0;
    LinkedList<String> childs;
    List<WifiData> wifiData1;
    List<WifiData> wifiData2;
    List<ClientFingerprint> maclist;
    String mac_id1;
    String mac_id2;
    int given_dbm1;
    int given_dbm2;
    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);
        text = (TextView) findViewById(R.id.textView2);

        childs = new LinkedList<String>();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference().child("WIFI_DATA");

        Log.d("reference ", String.valueOf(myRef));

        //final String mac_id = "b6:6d:83:6c:d1:44";
        WifiManager wifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wifi.isWifiEnabled() == false)
        {
            Toast.makeText(getApplicationContext(), "wifi is disabled..making it enabled", Toast.LENGTH_LONG).show();
            wifi.setWifiEnabled(true);
        }

        results = wifi.getScanResults();
        size = results.size();
        int dbm1;
        String ssid1;
        String macid1;
        String s1="IIITV1";
        String s2="IIITV2";
        String s3="IIITV-Staff";
        maclist = new LinkedList<ClientFingerprint>();
        maclist.clear();
        for(int i=0;i<size;i++)
        {

            //dbm1 = Math.abs(results.get(i).level);
            dbm1 = results.get(i).level;
            ssid1 = results.get(i).SSID;
            macid1 = results.get(i).BSSID;
            if(ssid1.equals(s1) || ssid1.equals(s2) || ssid1.equals(s3) )
            {
                ClientFingerprint f1 = new ClientFingerprint();
                f1.setBssid(macid1);
                f1.setDBM(dbm1);
                maclist.add(f1);
            }

        }

        Collections.sort(maclist, new compare());

        int z = maclist.size();
        mac_id1=maclist.get(0).getBssid();
        mac_id2=maclist.get(1).getBssid();
        given_dbm1=maclist.get(0).getDBM();
        given_dbm2=maclist.get(1).getDBM();
       /* mac_id2 = "24:c9:a1:09:a1:58";
        mac_id1 = "24:c9:a1:49:a1:58";
        given_dbm2 = - 61;
        given_dbm1 = - 61;*/
        wifiData1 = new LinkedList<WifiData>();
        wifiData1.clear();
        wifiData2 = new LinkedList<WifiData>();
        wifiData2.clear();
        myRef.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                long count = dataSnapshot.getChildrenCount();
                Log.d("Count ", String.valueOf(count));

                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren())
                {
                    Log.d("Keyyy",dataSnapshot1.getKey());

                    for(DataSnapshot dataSnapshot2: dataSnapshot1.getChildren())
                    {
                        Log.d("Keyyy",dataSnapshot2.getKey());
                        String macc = dataSnapshot2.getKey();

                        if(macc.equals(mac_id1))
                        {
                            WifiData data = new WifiData();
                            wifiData1.add(data);
                            data.setDbm((Long) dataSnapshot2.child("dBm").getValue());
                            data.setRoomno((String) dataSnapshot2.child("Room No").getValue());
                            data.setBSSID((String) dataSnapshot2.child("SSID").getValue());
                            Log.d("dbmvalue", "mac1"+String.valueOf(data.getDbm()));
                            Log.d("roomno",data.getRoomno());
                        }
                        else if ( macc.equals(mac_id2))
                        {
                            WifiData data = new WifiData();
                            wifiData2.add(data);
                            data.setDbm((Long) dataSnapshot2.child("dBm").getValue());
                            data.setRoomno((String) dataSnapshot2.child("Room No").getValue());
                            data.setBSSID((String) dataSnapshot2.child("SSID").getValue());
                            Log.d("dbmvalue", "mac2"+String.valueOf(data.getDbm()));
                            Log.d("roomno",data.getRoomno());

                        }
                    }
                    childs.add(dataSnapshot1.getKey().toString());
                }

                size = childs.size();
                Log.d("Totalfingerprints", String.valueOf(size));
                //textView.setText(String.valueOf(wifiData.get(0).getRoomno()));
                Log.d("firstfingerprint", childs.get(0));
                Log.d("ssssssssss", String.valueOf(given_dbm1)+String.valueOf(given_dbm2));
                String room1=Calculate(wifiData1,given_dbm1);
                String room2=Calculate(wifiData2,given_dbm2);
                Log.d("room1", room1);
                Log.d("room2", room2);
                text.setText(room1);

            }


        @Override
        public void onCancelled(DatabaseError databaseError)
        {

        }
    }
        );

}


    public String Calculate(List<WifiData> wifiData , int given_dbm )
    {
        int abs_given_dbm = Math.abs(given_dbm);
        int min_diff=120;
        String min_room="";
        int abs_dbmfin;
        int diff;
        int diff1;
        int k=0;
        for(int i=0;i<wifiData.size();i++)
        {
            k = (int)wifiData.get(i).dbm;
            abs_dbmfin = Math.abs(k);
            diff = abs_dbmfin-abs_given_dbm;
            diff1 = Math.abs(diff);
            if(diff1 <= min_diff)
            {
                min_diff = diff;
                min_room = wifiData.get(i).Room;
            }
            Log.d("mindiff", String.valueOf(min_diff));
            Log.d("minroom", min_room);
        }
        return min_room;
    }


}

class compare implements Comparator<ClientFingerprint> {

    public int compare(ClientFingerprint c1, ClientFingerprint c2)
    {
        if(c1.getDBM() < c2.getDBM() )
        {
            return 1;
        }
        else
        {
            return -1;
        }
    }
}
