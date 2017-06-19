package com.example.kate.geotest.sqlite;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.kate.geotest.R;

/**
 * Created by Kate on 19.06.2017.
 */

public class DBGeoListProvider extends ContentProvider {

    private static final String AUTHORITY = "com.example.kate.geotest.sqlite.DBGeoList";
    private final String type = "text/plain";

    private static final UriMatcher uriMatcher;

    private static final int DB_ITEM = 1;
    private static final int DB_ITEMS = 2;

    static {

        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, "items", DB_ITEMS);
        uriMatcher.addURI(AUTHORITY, "item/#", DB_ITEM);
    }

    private DBGeoList dbGeo = null;

    public static final Uri CONTENT_URI_ITEMS = Uri.parse("content://" +
            AUTHORITY + "/items");

    public static final Uri CONTENT_URI_ITEM = Uri.parse("content://" +
            AUTHORITY + "/item/#");

    @Override
    public boolean onCreate() {

        dbGeo = new DBGeoList(this.getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        if (uriMatcher.match(uri) != DB_ITEMS) {
            return null;
        }
        Cursor c = dbGeo.getWritableDatabase().query(DBGeoList.TableItems.T_NAME, projection, selection,
                selectionArgs, null, null, sortOrder);

        return c;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        if (uri != null)
            return type;
        else
            return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        Uri resultUri = null;

        if (uriMatcher.match(uri) == DB_ITEMS) {

            long id = dbGeo.addItem(values);

            if (id > 0) {
                Uri itemUri = ContentUris.withAppendedId(CONTENT_URI_ITEMS, id);
                resultUri = itemUri;
            }
        } else {
            throw new IllegalArgumentException("Unknow URI " + uri);
        }
        return resultUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {


        if (uriMatcher.match(uri) == DB_ITEM) {

            long id = ContentUris.parseId(uri);

            boolean result = dbGeo.deleteItem(id);

            if (result) {
                return 1;
            }
        } else {
            throw new IllegalArgumentException(getContext().getString(R.string.DBGeoListProvider_UriException) + uri);
        }

        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {

        if (uriMatcher.match(uri) == DB_ITEM) {


            long id = ContentUris.parseId(uri);

            boolean result = dbGeo.updateItem(values, id);

            if (result) {
                return 1;
            }

        } else {
            throw new IllegalArgumentException(getContext().getString(R.string.DBGeoListProvider_UriException) + uri);
        }

        return 0;
    }
}
