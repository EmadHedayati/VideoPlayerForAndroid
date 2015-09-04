package erixe.android.videoplayer.EWebServices;

import android.os.AsyncTask;
import java.util.Map;

public class PostCommentOnVideoTask extends AsyncTask<String, Void, WebServiceRespond> {

    public static final String[] TASK_ERRORS = {"ERROR"};
    private OnTaskCompleteListener listener;
    private static final String URL = EWebServiceStrings.POST_COMMENT_ON_VIDEO_TASK_URL;
    private static final String METHOD_TYPE = EWebServiceStrings.METHOD_TYPE_POST;
    private Map<String, String> paramsMap;

    public PostCommentOnVideoTask(OnTaskCompleteListener listener, Map<String, String> paramsMap)
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
            listener.onPostCommentOnVideoTaskComplete(preWebServiceRespond);
        else {
            WebServiceRespond initServerRespond = Utilities.initializeWebServiceRespond(preWebServiceRespond);
            if(initServerRespond.ok)
                analyzeJsonString(initServerRespond.result.toString());
            listener.onPostCommentOnVideoTaskComplete(initServerRespond);
        }
    }

    private String analyzeJsonString(String json)
    {
        return null;
    }

    public interface OnTaskCompleteListener {
        public void onPostCommentOnVideoTaskComplete(WebServiceRespond webServiceRespond);
    }
}
