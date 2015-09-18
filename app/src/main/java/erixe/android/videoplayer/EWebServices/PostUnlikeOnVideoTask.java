package erixe.android.videoplayer.EWebServices;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class PostUnlikeOnVideoTask extends AsyncTask<String, Void, WebServiceRespond> {

    public static final String[] TASK_ERRORS = {"ERROR"};
    private OnTaskCompleteListener listener;
    private static final String URL = EWebServiceStrings.POST_UNLIKE_ON_VIDEO_TASK_URL;
    private static final String METHOD_TYPE = EWebServiceStrings.METHOD_TYPE_POST;
    private Map<String, String> paramsMap;

    public PostUnlikeOnVideoTask(OnTaskCompleteListener listener, Map<String, String> paramsMap)
    {
        this.listener = listener;
        this.paramsMap = paramsMap;
    }

    @Override
    protected WebServiceRespond doInBackground(String... paramsValue) {
        return Utilities.getPreWebServiceRespond(METHOD_TYPE, paramsValue[0], paramsValue[1],
                EWebServiceStrings.getURLWithId(URL, paramsValue[2]), paramsMap);
    }

    @Override
    protected void onPostExecute(WebServiceRespond preWebServiceRespond) {
        if(!preWebServiceRespond.ok)
            listener.onPostUnlikeOnVideoTaskComplete(preWebServiceRespond, null);
        else {
            WebServiceRespond initServerRespond = Utilities.initializeWebServiceRespond(preWebServiceRespond);
            if(initServerRespond.ok)
                listener.onPostUnlikeOnVideoTaskComplete(initServerRespond, analyzeJsonString(initServerRespond.result));
            else
                listener.onPostUnlikeOnVideoTaskComplete(initServerRespond, null);
        }
    }

    private String analyzeJsonString(String json)
    {
        try {
            String likes = new JSONObject(json).getString("likes");
            return likes;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public interface OnTaskCompleteListener {
        public void onPostUnlikeOnVideoTaskComplete(WebServiceRespond webServiceRespond, String likes);
    }
}
