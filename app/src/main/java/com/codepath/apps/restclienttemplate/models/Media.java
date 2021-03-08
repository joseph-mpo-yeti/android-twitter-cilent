package com.codepath.apps.restclienttemplate.models;

import androidx.core.util.Pair;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Parcel
public class Media {
    long id;
    String URL, type, displayURL;
    Map<String, Size> sizes;

    public Media(){}

    public static List<Media> getMediafromJSON(JSONObject entities) throws JSONException {
        List<Media> medias = new ArrayList<>();
        JSONArray med = entities.getJSONArray("media");
        for (int i = 0; i < med.length(); i++) {
            JSONObject mediaObject = med.getJSONObject(i);
            Media newMedia = fromJSON(mediaObject);
            newMedia.sizes = new HashMap<>();
            fillSizes(newMedia.sizes, mediaObject.getJSONObject("sizes"));
            medias.add(newMedia);
        }

        return medias;
    }

    private static void fillSizes(Map<String, Size> sizesMap, JSONObject sizes) throws JSONException {
        sizesMap.put("thumb", Size.fromJSON(sizes.getJSONObject("thumb")));
        sizesMap.put("small", Size.fromJSON(sizes.getJSONObject("small")));
        sizesMap.put("large", Size.fromJSON(sizes.getJSONObject("large")));
        sizesMap.put("medium", Size.fromJSON(sizes.getJSONObject("medium")));
    }


    public static Media fromJSON(JSONObject ob) throws JSONException {
        Media media = new Media();
        media.URL = ob.getString("media_url_https");
        media.type = ob.getString("type");
        media.displayURL = ob.getString("display_url");

        return media;
    }

    public long getId() {
        return id;
    }

    public String getURL() {
        return URL;
    }

    public String getType() {
        return type;
    }

    public String getDisplayURL() {
        return displayURL;
    }

    public Map<String, Size> getSizes() {
        return sizes;
    }

    /*

{
    "display_url": "pic.twitter.com/5J1WJSRCy9",
    "expanded_url": "https://twitter.com/nolan_test/status/930077847535812610/photo/1",
    "id": 9.300778475358126e17,
    "id_str": "930077847535812610",
    "indices": [
    13,
    36
    ],
    "media_url": "http://pbs.twimg.com/media/DOhM30VVwAEpIHq.jpg",
    "media_url_https": "https://pbs.twimg.com/media/DOhM30VVwAEpIHq.jpg"
    "sizes": {
        "thumb": {
            "h": 150,
            "resize": "crop",
            "w": 150
        },
        "large": {
            "h": 1366,
                    "resize": "fit",
                    "w": 2048
        },
        "medium": {
            "h": 800,
                    "resize": "fit",
                    "w": 1200
        },
        "small": {
            "h": 454,
                    "resize": "fit",
                    "w": 680
        }
    },
    "type": "photo",
    "url": "https://t.co/5J1WJSRCy9",
}

 */
}
