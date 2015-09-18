package erixe.android.videoplayer.EWebServices;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

/**
 * Created by emad on 9/3/2015.
 */
public class EWebServiceStrings {

    public static final String SERVER_BASE_URL = "http://video.erixe.com";

    public static final String API = "/api";
    public static final String VIDEO = "/video";
    public static final String USER = "/user";

    public static final String METHOD_TYPE_GET = "GET";
    public static final String METHOD_TYPE_POST = "POST";

    public static final String ID = "{id}";

    public static final String TASK_FAILED_TO_CONNECT = "FAILED_TO_CONNECT";
    public static final String TASK_AN_EXCEPTION_ACURED = "AN_EXCEPTION_ACURED";
    public static final String TASK_SUCCEEDED = "true";
    public static final String TASK_FAILED = "false";

    public static final String GET_SHOW_VIDEO_TASK_URL = SERVER_BASE_URL + API + VIDEO + "/" + ID;
    public static final String GET_INDEX_VIDEOS_TASK_URL = SERVER_BASE_URL + API + VIDEO;
    public static final String GET_SEARCH_TASK_URL = SERVER_BASE_URL + API + VIDEO + "/search";
    public static final String GET_QUEUE_VIDEOS_TASK_URL = SERVER_BASE_URL + API + USER + "/queue";

    public static final String POST_LIKE_ON_VIDEO_TASK_URL = SERVER_BASE_URL + API + VIDEO + "/" + ID + "/like";
    public static final String POST_UNLIKE_ON_VIDEO_TASK_URL = SERVER_BASE_URL + API + VIDEO + "/" + ID + "/unlike";
    public static final String POST_ADD_VIDEO_TO_QUEUE_TASK_URL = SERVER_BASE_URL + API + VIDEO + "/" + ID + "/enqueue";
    public static final String POST_REMOVE_VIDEO_FROM_QUEUE_TASK_URL = SERVER_BASE_URL + API + VIDEO + "/" + ID + "/dequeue";
    public static final String POST_COMMENT_ON_VIDEO_TASK_URL = SERVER_BASE_URL + API + VIDEO + "/" + ID + "/comment";

    public static String getURLWithId(String URL, String id)
    {
        return URL.replace("{id}", id);
    }

    public static String addParams(Map<String, String> paramsMap, String webserviceUrl)
    {
        StringBuilder tmp = new StringBuilder(webserviceUrl + "?");
        boolean first = true;

        for (Map.Entry<String, String> pair : paramsMap.entrySet())
        {
            if (first)
                first = false;
            else
                tmp.append("&");

            try {
                tmp.append(URLEncoder.encode(pair.getKey(), "UTF-8"));
                tmp.append("=");
                tmp.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        return tmp.toString();
    }

}
