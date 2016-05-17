package com.ukiy.updator;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by UKIY on 2015/12/31.
 */
public class UpdateInfo {
    public String new_version;
    public String min_version;
    public String change_log;
    public String url;

    public UpdateInfo(String new_version, String min_version, String change_log, String url) {
        this.new_version = new_version;
        this.min_version = min_version;
        this.change_log = change_log;
        this.url = url;
    }

    public static UpdateInfo parse(String json) throws JSONException {
        UpdateInfo ret = null;
        JSONObject jsonObject = new JSONObject(json);
        JSONObject data = jsonObject.getJSONObject("data");
        String new_version = data.getString("new_version");
        String min_version = data.getString("min_version");
        String change_log = data.getString("change_log");
        String url = data.getString("url");
        ret = new UpdateInfo(new_version, min_version, change_log, url);
        return ret;
    }
}
