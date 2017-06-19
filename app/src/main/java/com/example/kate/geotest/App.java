package com.example.kate.geotest;

import android.app.Application;

import com.example.kate.geotest.sqlite.DBGeoList;

/**
 * Created by Kate on 19.06.2017.
 */

public class App extends Application {

    private static DBGeoList dbGeoList = null;

    @Override
    public void onCreate() {

        super.onCreate();

        dbGeoList = new DBGeoList(getApplicationContext());
        dbGeoList.getWritableDatabase();
    }

    public static DBGeoList getDB() {
        return dbGeoList;
    }
}
