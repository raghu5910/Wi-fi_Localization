package com.example.raghu.findmyroom;

/**
 * Created by Raghu on 13-04-2018.
 */

public class WifiData {

     String Room;
     String SSID;
     long dbm;

    public WifiData(){

    }

    public String getBSSID() {
        return SSID;
    }

    public void setBSSID(String SSID) {
        this.SSID = SSID;
    }

    public String getRoomno() {
        return Room;
    }

    public void setRoomno(String Room) {
        this.Room = Room;
    }

    public long getDbm() {
        return dbm;
    }

    public void setDbm(long dbm) {
        this.dbm = dbm;
    }
}
