package com.example.kate.geotest.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

/**
 * Created by Kate on 19.06.2017.
 */

public class DBGeoList extends DBSQLite {

    private static final String SQL_WHERE_BY_ID = BaseColumns._ID + "=?";
    private static final String DB_NAME = "DBGeoList.db";
    private static final int DB_VERSION = 2;

    public DBGeoList(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        DBSQLite.execSQL(db, TableItems.SQL_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        DBSQLite.dropTable(db, TableItems.T_NAME);

        this.onCreate(db);
    }

    public long addItem(String address, String body) {

        ContentValues v = new ContentValues();

        v.put(TableItems.C_GEO, address);

        return this.getWritableDatabase().insert(TableItems.T_NAME, null, v);

    }

    public long addItem(ContentValues values) {
        return this.getWritableDatabase().insert(TableItems.T_NAME, null, values);
    }

    public boolean updateItem(String address, String body, long id) {

        ContentValues v = new ContentValues();

        v.put(TableItems.C_GEO, address);

        return 1 == this.getWritableDatabase().update(TableItems.T_NAME, v,
                SQL_WHERE_BY_ID, new String[]{String.valueOf(id)});
    }

    public boolean updateItem(ContentValues values, long id) {

        return 1 == this.getWritableDatabase().update(TableItems.T_NAME, values,
                SQL_WHERE_BY_ID, new String[]{String.valueOf(id)});
    }

    public boolean deleteItem(long id) {
        return 1 == this.getWritableDatabase().delete(
                TableItems.T_NAME, SQL_WHERE_BY_ID,
                new String[]{String.valueOf(id)});
    }

    public static class TableItems implements BaseColumns {

        public static final String T_NAME = "tItems";
        public static final String C_GEO = "geo";

        public static final String SQL_CREATE = "CREATE TABLE " + T_NAME +
                " (" + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                C_GEO + " TEXT"
                + ")";
    }
}
