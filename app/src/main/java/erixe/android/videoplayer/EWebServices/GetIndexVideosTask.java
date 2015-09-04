package erixe.android.videoplayer.EWebServices;

import android.os.AsyncTask;

import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import erixe.android.videoplayer.EVideoInformationModels.EVideoInformation;

public class GetIndexVideosTask extends AsyncTask<String, Void, WebServiceRespond> {

    private OnTaskCompleteListener listener;
    private static final String URL = EWebServiceStrings.GET_INDEX_VIDEOS_TASK_URL;
    private static final String METHOD_TYPE = EWebServiceStrings.METHOD_TYPE_GET;
    private Map<String, String> paramsMap;

    public GetIndexVideosTask(OnTaskCompleteListener listener, Map<String, String> paramsMap)
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
            listener.onGetIndexVideosTaskComplete(preWebServiceRespond, null);
        else {
            WebServiceRespond initServerRespond = Utilities.initializeWebServiceRespond(preWebServiceRespond);
            if(initServerRespond.ok)
                listener.onGetIndexVideosTaskComplete(initServerRespond, analyzeJsonString(initServerRespond.result.toString()));
            else
                listener.onGetIndexVideosTaskComplete(initServerRespond, null);
        }
    }

    private List<EVideoInformation> analyzeJsonString(String json)
    {
        List<EVideoInformation> indexVideosInformation = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(json);
            for(int i = 0; i < jsonArray.length(); i++) {
                JSONObject videoInformation = jsonArray.getJSONObject(i);
                EVideoInformation newEVideoInformation = new Gson().fromJson(videoInformation.toString(), EVideoInformation.class);
                newEVideoInformation.type = EVideoInformation.EVIDEO_INFORMATION_INDEX_TYPE;
                indexVideosInformation.add(newEVideoInformation);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return indexVideosInformation;
    }

    public interface OnTaskCompleteListener {
        public void onGetIndexVideosTaskComplete(WebServiceRespond webServiceRespond, List<EVideoInformation> indexVideosInformation);
    }
}

