package com.example.kate.geotest.sqlite;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public abstract class DBSQLite extends SQLiteOpenHelper {

    private Context context = null;
    private static String dropTableExists = "DROP TABLE IF EXISTS ";

    public DBSQLite(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
    }

    protected Context getContext(){
        return context;
    }

    public Cursor getReadableCursor(String table) {
        return this.getReadableDatabase().query(table, null, null, null, null,
                null, null);
    }

    public Cursor getReadableCursorWithSelectedData(String table, String[] columnNames) {
        return this.getReadableDatabase().query(table, columnNames, null, null, null,
                null, null);
    }

    public Cursor getWritableCursor(String table) {
        return this.getWritableDatabase().query(table, null, null, null, null,
                null, null);
    }

    public static boolean execSQL(SQLiteDatabase db, String sql){

        if (db == null) return false;

        try {
            db.execSQL(sql);
        }catch (SQLException e){
            return false;
        }
        return true;
    }

    public static boolean dropTable(SQLiteDatabase db, String table) {
        return DBSQLite.execSQL(db, dropTableExists + table);
    }
}
