package erixe.android.videoplayer.EWebServices;

import android.os.AsyncTask;

import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import erixe.android.videoplayer.EVideoInformationModels.EVideoInformation;

public class GetShowVideoTask extends AsyncTask<String, Void, WebServiceRespond> {

    private OnTaskCompleteListener listener;
    private static final String URL = EWebServiceStrings.GET_SHOW_VIDEO_TASK_URL;
    private static final String METHOD_TYPE = EWebServiceStrings.METHOD_TYPE_GET;
    private Map<String, String> paramsMap;

    public GetShowVideoTask(OnTaskCompleteListener listener, Map<String, String> paramsMap)
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
            listener.onGetShowVideoTaskComplete(preWebServiceRespond, null);
        else {
            WebServiceRespond initServerRespond = Utilities.initializeWebServiceRespond(preWebServiceRespond);
            if(initServerRespond.ok)
                listener.onGetShowVideoTaskComplete(initServerRespond, analyzeJsonString(initServerRespond.result.toString()));
            else
                listener.onGetShowVideoTaskComplete(initServerRespond, null);
        }
    }

    private EVideoInformation analyzeJsonString(String json)
    {
        EVideoInformation newEVideoInformation = null;
        try {
            JSONObject videoInformation = new JSONObject(json).getJSONObject("video");
            newEVideoInformation = new Gson().fromJson(videoInformation.toString(), EVideoInformation.class);
            newEVideoInformation.type = EVideoInformation.EVIDEO_INFORMATION_SHOW_TYPE;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return newEVideoInformation;
    }

    public interface OnTaskCompleteListener {
        public void onGetShowVideoTaskComplete(WebServiceRespond webServiceRespond, EVideoInformation showVideoInformation);
    }
}