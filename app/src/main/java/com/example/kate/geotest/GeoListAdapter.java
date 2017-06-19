package com.example.kate.geotest;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.kate.geotest.sqlite.DBGeoList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kate on 19.06.2017.
 */

public class GeoListAdapter extends RecyclerView.Adapter<GeoListAdapter.MyViewHolder> {

    private List<String> list;

    public GeoListAdapter(Cursor cursor){

        list = new ArrayList<>();

        if(cursor.moveToFirst()){
            while(!cursor.isAfterLast()){
                list.add(cursor.getString(cursor.getColumnIndex(DBGeoList.TableItems.C_GEO)));
                cursor.moveToNext();
            }
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_list, parent, false);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.textGeo.setText(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textGeo;

        public MyViewHolder(View itemView) {
            super(itemView);

            textGeo = (TextView) itemView.findViewById(R.id.textGeo);
        }
    }
}
