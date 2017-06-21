package com.example.kate.geotest;

import android.location.Location;
import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Kate on 21.06.2017.
 */

public class GeoCityFetcher {

    private static final String TAG = GeoCityFetcher.class.getSimpleName();
    private static final String API_KEY = "AIzaSyBanHTszZI-9UgNn7hPQQyU-gaeEIADgmY";
    private static final String ERROR_MESSAGE_DATA_LOAD_FAILED = "Ошибка загрузки данных";
    private static final String ERROR_MESSAGE_JSON = "Ошибка парсинга Json";
    private Location location;

    public GeoCityFetcher(Location location){
        this.location = location;
    }

    public String getJSONString(String urlSpec) throws IOException {

        OkHttpClient client = new OkHttpClient();


        Request request = new Request.Builder()
                .url(urlSpec)
                .build();

        Response response = client.newCall(request).execute();

        return response.body().string();
    }

    public String fetchItems(){

        String address = null;
        try{
            String url = Uri.parse("https://maps.googleapis.com/maps/api/geocode/json").buildUpon()
                    .appendQueryParameter("latlng", location.getLatitude() + ","
                            + location.getLongitude())
                    .appendQueryParameter("key", API_KEY)
                    .appendQueryParameter("result_type", "locality")
                    .appendQueryParameter("language", "ru")
                    .build().toString();

            String jsonString = getJSONString(url);
            JSONObject jsonBody = new JSONObject(jsonString);
            address = parseItems(jsonBody);
        } catch (IOException e) {

            Log.e(TAG, ERROR_MESSAGE_DATA_LOAD_FAILED, e);
        }catch (JSONException e) {
            Log.e(TAG, ERROR_MESSAGE_JSON, e);
        }

        return address;
    }

    public String parseItems(JSONObject jsonBody) throws IOException, JSONException{

        if(jsonBody.getString("status").equals("OK")) {
            JSONArray jsonArrayResult = jsonBody.getJSONArray("results");
            JSONObject jsonObject = jsonArrayResult.getJSONObject(0);
            JSONArray jsonArray = jsonObject.getJSONArray("address_components");

            JSONObject addressJsonObject = jsonArray.getJSONObject(0);
            return addressJsonObject.getString("long_name");
        }else{return null;}
    }
}
