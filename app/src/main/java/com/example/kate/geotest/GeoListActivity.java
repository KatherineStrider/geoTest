package com.example.kate.geotest;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.kate.geotest.sqlite.DBGeoListProvider;

/**
 * Created by Kate on 19.06.2017.
 */

public class GeoListActivity extends AppCompatActivity {

    private RecyclerView geoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geo_list);

        initView();
    }

    private void initView(){

        geoList = (RecyclerView) findViewById(R.id.recycler);
        geoList.setLayoutManager(new LinearLayoutManager(this));

        Uri queryUri = DBGeoListProvider.CONTENT_URI_ITEMS;

        Cursor cursor = getContentResolver().query(queryUri, null, null, null, null);
        GeoListAdapter geoListAdapter = new GeoListAdapter(cursor);
        geoList.setAdapter(geoListAdapter);

    }

}
