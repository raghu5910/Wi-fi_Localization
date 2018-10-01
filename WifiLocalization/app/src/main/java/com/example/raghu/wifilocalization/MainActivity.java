package com.example.raghu.wifilocalization;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.lang.Object;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    public static final String EXTRA_MESSAGE1 = "com.example.myfirstapp.MESSAGE1";
    private static final int PERMISSION_REQUEST_CODE = 1;
    private Button button;
    private String[] perms = {Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.INTERNET,
            Manifest.permission.CHANGE_WIFI_STATE,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }


    /** Called when the user taps the Send button */
    public void sendMessage(View view)
    {
        Toast.makeText(MainActivity.this, "Scanning", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText editText = (EditText) findViewById(R.id.editText);
        EditText editText2 = (EditText)  findViewById(R.id.editText2);
        String message = editText.getText().toString();
        String fingerprint = editText2.getText().toString();
        intent.putExtra(EXTRA_MESSAGE1, fingerprint);
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);

    }



}
