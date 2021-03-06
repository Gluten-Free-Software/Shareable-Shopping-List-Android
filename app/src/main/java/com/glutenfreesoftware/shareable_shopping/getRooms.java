package com.glutenfreesoftware.shareable_shopping;

import android.os.AsyncTask;
import android.os.Message;
import android.util.JsonReader;
import android.util.Log;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class getRooms extends AsyncTask<URL,Integer,List<RoomObj>> {
    public interface OnPostExecute {
        void onPostExecute(List<RoomObj> messages);
    }

    OnPostExecute callback;

    public getRooms(OnPostExecute callback) {
        this.callback = callback;
    }

    @Override
    protected List<RoomObj> doInBackground(URL... urls) {
        if(urls.length < 1) return Collections.EMPTY_LIST;


        //SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        List<RoomObj> result = new ArrayList<>();

        HttpURLConnection con = null;
        try {
            con = (HttpURLConnection)urls[0].openConnection();
            JsonReader jr = new JsonReader(new InputStreamReader(con.getInputStream()));
            jr.beginArray();
            while (jr.hasNext()) {
                String roomName = null;
                String roomOwner = null;
                jr.beginObject();
                while (jr.hasNext()) {
                    switch (jr.nextName()) {
                        case "roomName":
                            roomName = jr.nextString();
                            break;
                        case "roomOwner":
                            roomOwner = jr.nextString();
                            break;
                        default:
                            jr.skipValue();
                    }
                }
                jr.endObject();
                result.add(new RoomObj(roomName, roomOwner));
            }
            jr.endArray();
        } catch (IOException e) {
            Log.e("LoadThumb","Failed to load rooms from " + urls[0],e);
        }


        return result;
    }

    @Override
    protected void onPostExecute(List<RoomObj> rooms) {
        if(callback != null)
            callback.onPostExecute(rooms);
    }
}