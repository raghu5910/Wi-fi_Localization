package com.example.raghu.findmyroom;

/**
 * Created by Raghu on 15-04-2018.
 */

public class ClientFingerprint {
    String Bssid;
    int DBM;

    public String getBssid() {
        return Bssid;
    }

    public void setBssid(String bssid) {
        Bssid = bssid;
    }

    public int getDBM() {
        return DBM;
    }

    public void setDBM(int DBM) {
        this.DBM = DBM;
    }


}
