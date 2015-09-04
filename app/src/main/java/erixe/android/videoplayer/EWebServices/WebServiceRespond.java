package erixe.android.videoplayer.EWebServices;

import org.json.JSONObject;

/**
 * Created by emad on 9/4/2015.
 */
public class WebServiceRespond {
    public boolean ok;
    public String description;
    public JSONObject result;

    public WebServiceRespond(){

    }

    public WebServiceRespond(boolean taskSucceeded, String message, JSONObject json)
    {
        this.ok = taskSucceeded;
        this.description = message;
        this.result = json;
    }
}
