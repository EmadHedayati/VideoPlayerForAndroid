package erixe.android.videoplayer.EWebServices;

import android.os.AsyncTask;

import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import erixe.android.videoplayer.EVideoInformationModels.EQueueInformation;
import erixe.android.videoplayer.EVideoInformationModels.EVideoInformation;

public class GetQueueVideosTask extends AsyncTask<String, Void, WebServiceRespond> {

    private OnTaskCompleteListener listener;
    private static final String URL = EWebServiceStrings.GET_QUEUE_VIDEOS_TASK_URL;
    private static final String METHOD_TYPE = EWebServiceStrings.METHOD_TYPE_GET;
    private Map<String, String> paramsMap;

    public GetQueueVideosTask(OnTaskCompleteListener listener, Map<String, String> paramsMap)
    {
        this.listener = listener;
        this.paramsMap = paramsMap;
    }

    @Override
    protected WebServiceRespond doInBackground(String... paramsValue) {
        return Utilities.getPreWebServiceRespond(METHOD_TYPE, paramsValue[0], paramsValue[1], URL, paramsMap);
    }

    @Override
    protected void onPostExecute(WebServiceRespond preWebServiceRespond) {
        if(!preWebServiceRespond.ok)
            listener.onGetQueueVideosTaskComplete(preWebServiceRespond, null);
        else {
            WebServiceRespond initServerRespond = Utilities.initializeWebServiceRespond(preWebServiceRespond);
            if(initServerRespond.ok)
                listener.onGetQueueVideosTaskComplete(initServerRespond, analyzeJsonString(initServerRespond.result));
            else
                listener.onGetQueueVideosTaskComplete(initServerRespond, null);
        }
    }

    private List<EQueueInformation> analyzeJsonString(String json)
    {
        List<EQueueInformation> queueVideosInformation = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(json);
            for(int i = 0; i < jsonArray.length(); i++) {
                JSONObject videoInformation = jsonArray.getJSONObject(i);
                EQueueInformation newEQueueInformation = new Gson().fromJson(videoInformation.toString(), EQueueInformation.class);
                newEQueueInformation.video.type = EVideoInformation.EVIDEO_INFORMATION_QUEUE_TYPE;
                queueVideosInformation.add(newEQueueInformation);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return queueVideosInformation;
    }

    public interface OnTaskCompleteListener {
        public void onGetQueueVideosTaskComplete(WebServiceRespond webServiceRespond, List<EQueueInformation> queueVideosInformation);
    }
}
