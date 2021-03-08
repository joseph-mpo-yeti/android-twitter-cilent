package com.codepath.apps.restclienttemplate.models;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

@Parcel
public class User {
    String name, screenName, publicImgUrl;
    boolean verified;

    public User(){}

    public static User fromJSON(JSONObject ob) throws JSONException {
        User user = new User();
        user.name = ob.getString("name");
        user.screenName = ob.getString("screen_name");
        user.publicImgUrl = ob.getString("profile_image_url_https");
        user.verified = ob.getBoolean("verified");
        return user;
    }

    public String getName() {
        return name;
    }

    public String getScreenName() {
        return screenName;
    }

    public String getPublicImgUrl() {
        return publicImgUrl.replace("_normal", "");
    }

    public boolean isVerified() {
        return verified;
    }
}
