package erixe.android.videoplayer.EWebServices;

/**
 * Created by emad on 9/4/2015.
 */
public class WebServiceRespond {
    public boolean ok;
    public String description;
    public String result;

    public WebServiceRespond(){

    }

    public WebServiceRespond(boolean taskSucceeded, String message, String json)
    {
        this.ok = taskSucceeded;
        this.description = message;
        this.result = json;
    }
}
