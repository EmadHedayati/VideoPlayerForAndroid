package erixe.android.videoplayer.EWebServices;

/**
 * Created by emad on 9/3/2015.
 */
public class EWebServiceStrings {

    public static final String SERVER_BASE_URL = "http://video.erixe.com";

    public static final String METHOD_TYPE_GET = "GET";
    public static final String METHOD_TYPE_POST = "POST";

    public static final String ID = "{id}";

    public static final String TASK_FAILED_TO_CONNECT = "FAILED_TO_CONNECT";
    public static final String TASK_AN_EXCEPTION_ACURED = "AN_EXCEPTION_ACURED";
    public static final String TASK_SUCCEEDED = "SUCCEEDED";
    public static final String TASK_FAILED = "FAILED";

    public static final String GET_SHOW_VIDEO_TASK_URL = SERVER_BASE_URL + "/api/video/" + ID;
    public static final String GET_INDEX_VIDEOS_TASK_URL = SERVER_BASE_URL + "/api/video";
    public static final String GET_QUEUE_VIDEOS_TASK_URL = SERVER_BASE_URL + "/api/user/queue";

    public static final String POST_LIKE_ON_VIDEO_TASK_URL = SERVER_BASE_URL + "/video/" + ID + "/like";
    public static final String POST_ADD_VIDEO_TO_QUEUE_TASK_URL = SERVER_BASE_URL + "/video/" + ID + "/addToQueue";
    public static final String POST_COMMENT_ON_VIDEO_TASK_URL = SERVER_BASE_URL + "/video/" + ID + "/comment";

    public static String getURLWithId(String URL, String id)
    {
        return URL.replace("{id}", id);
    }

}
