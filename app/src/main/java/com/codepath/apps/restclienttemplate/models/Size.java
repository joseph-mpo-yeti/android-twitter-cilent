package com.codepath.apps.restclienttemplate.models;


import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

@Parcel
public class Size {
    private int width, height;
    private String resize;

    public Size(){}

    public static Size fromJSON(JSONObject ob) throws JSONException {
        Size s = new Size();
        s.height = ob.getInt("h");
        s.width = ob.getInt("w");
        s.resize = ob.getString("resize");
        return s;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String getResize() {
        return resize;
    }
}