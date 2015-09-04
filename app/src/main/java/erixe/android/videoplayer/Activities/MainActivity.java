package erixe.android.videoplayer.Activities;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import erixe.android.videoplayer.EVideoInformationModels.EVideoInformation;
import erixe.android.videoplayer.EWebServices.EWebServiceStrings;
import erixe.android.videoplayer.EWebServices.GetIndexVideosTask;
import erixe.android.videoplayer.EWebServices.GetQueueVideosTask;
import erixe.android.videoplayer.EWebServices.GetShowVideoTask;
import erixe.android.videoplayer.EWebServices.PostAddVideoToQueueTask;
import erixe.android.videoplayer.EWebServices.PostCommentOnVideoTask;
import erixe.android.videoplayer.EWebServices.PostLikeOnVideoTask;
import erixe.android.videoplayer.EWebServices.WebServiceRespond;
import erixe.android.videoplayer.Utilities.EVideoView;
import erixe.android.videoplayer.R;

public class MainActivity extends Activity implements GetShowVideoTask.OnTaskCompleteListener, GetIndexVideosTask.OnTaskCompleteListener,
        GetQueueVideosTask.OnTaskCompleteListener, PostAddVideoToQueueTask.OnTaskCompleteListener,
        PostLikeOnVideoTask.OnTaskCompleteListener, PostCommentOnVideoTask.OnTaskCompleteListener {

    List<EVideoInformation> indexVideosInformation = new ArrayList<>();
    List<EVideoInformation> queueVideosInformation = new ArrayList<>();
    EVideoInformation showVideoInformation;
    EVideoView eVideoView;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        eVideoView = (EVideoView) findViewById(R.id.eVideoView);
        listView = (ListView) findViewById(R.id.listView);

//        new GetIndexVideosTask(MainActivity.this, null).execute("hedayati.emad@gmail.com", "36233623");
        new PostAddVideoToQueueTask(MainActivity.this, null).execute("hedayati.emad@gmail.com", "36233623", "1");
    }

    private void populateListView()
    {
        ListViewAdapter listViewAdapter = new ListViewAdapter(getApplicationContext(), indexVideosInformation);
        listView.setAdapter(listViewAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                new GetShowVideoTask(MainActivity.this, null).execute("hedayati.emad@gmail.com", "36233623", indexVideosInformation.get(i).id);
            }
        });
    }

    @Override
    public void onGetShowVideoTaskComplete(WebServiceRespond webServiceRespond, EVideoInformation showVideoInformation) {
        if(webServiceRespond.ok) {
            this.showVideoInformation = showVideoInformation;
            try {
                eVideoView.pauseMedia();
                eVideoView.setMediaPath(EWebServiceStrings.SERVER_BASE_URL + showVideoInformation.qualities.get(0).file);
                eVideoView.prepareMedia(true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onGetIndexVideosTaskComplete(WebServiceRespond webServiceRespond, List<EVideoInformation> indexVideosInformation) {
        if(webServiceRespond.ok) {
            this.indexVideosInformation = indexVideosInformation;
            populateListView();
        }
    }

    @Override
    public void onGetQueueVideosTaskComplete(WebServiceRespond webServiceRespond, List<EVideoInformation> queueVideosInformation) {
        if(webServiceRespond.ok) {
            this.queueVideosInformation = queueVideosInformation;
        }
    }

    @Override
    public void onPostCommentOnVideoTaskComplete(WebServiceRespond webServiceRespond) {

    }

    @Override
    public void onPostAddVideoToQueueTaskComplete(WebServiceRespond webServiceRespond) {

    }

    @Override
    public void onPostLikeOnVideoTaskComplete(WebServiceRespond webServiceResult) {

    }

    private class ListViewAdapter extends ArrayAdapter<EVideoInformation> {

        Context context;
        List<EVideoInformation> eVideoInformations;
        ViewHolder vh;

        public ListViewAdapter(Context context, List<EVideoInformation> eVideoInformations) {
            super(context, R.layout.list_view_list_item_layout, eVideoInformations);
            this.context = context;
            this.eVideoInformations = eVideoInformations;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder vh;

            if (convertView == null) {
                LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.list_view_list_item_layout, null);

                vh = new ViewHolder();
                vh.listViewVideoCover = (ImageView) convertView.findViewById(R.id.listViewVideoCover);
                vh.listViewVideoDuration = (TextView) convertView.findViewById(R.id.listViewVideoDuration);
                vh.listViewVideoTitle = (TextView) convertView.findViewById(R.id.listViewVideoTitle);
                vh.listViewVideoDescription = (TextView) convertView.findViewById(R.id.listViewVideoDescription);
                vh.listViewAddToQueue = (ImageButton) convertView.findViewById(R.id.listViewAddToQueue);
                vh.listViewLike = (ImageButton) convertView.findViewById(R.id.listViewLike);

                convertView.setTag(vh);
            }
            else {
                vh = (ViewHolder) convertView.getTag();
            }

            EVideoInformation currentEVideoInformation = eVideoInformations.get(position);
            vh = (ViewHolder) convertView.getTag();
            new DownloadImageTask(vh.listViewVideoCover).execute(EWebServiceStrings.SERVER_BASE_URL + currentEVideoInformation.cover.file);
            vh.listViewVideoDuration.setText(currentEVideoInformation.duration);
            vh.listViewVideoTitle.setText(currentEVideoInformation.title);
            vh.listViewVideoDescription.setText(currentEVideoInformation.description);

            vh.listViewLike.setTag(currentEVideoInformation);
            vh.listViewLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EVideoInformation currentEVideoInformation = (EVideoInformation)view.getTag();
                    new PostLikeOnVideoTask(MainActivity.this, null).execute("hedayati.emad@gmail.com", "36233623", currentEVideoInformation.id);
                }
            });

            vh.listViewAddToQueue.setTag(currentEVideoInformation);
            vh.listViewAddToQueue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EVideoInformation currentEVideoInformation = (EVideoInformation)view.getTag();
                    new PostAddVideoToQueueTask(MainActivity.this, null).execute("hedayati.emad@gmail.com", "36233623", currentEVideoInformation.id);
                }
            });

            return convertView;
        }

        public class ViewHolder
        {
            ImageView listViewVideoCover;
            TextView listViewVideoDuration;
            TextView listViewVideoTitle;
            TextView listViewVideoDescription;
            ImageButton listViewAddToQueue;
            ImageButton listViewLike;
        }

        private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
            ImageView bmImage;

            public DownloadImageTask(ImageView bmImage) {
                this.bmImage = bmImage;
            }

            protected Bitmap doInBackground(String... urls) {
                String urlDisplay = urls[0];
                Bitmap mIcon11 = null;
                try {
                    InputStream in = new java.net.URL(urlDisplay).openStream();
                    mIcon11 = BitmapFactory.decodeStream(in);
                } catch (Exception e) {
                    Log.e("Error", e.getMessage());
                    e.printStackTrace();
                }
                return mIcon11;
            }

            protected void onPostExecute(Bitmap result) {
                bmImage.setImageBitmap(result);
            }
        }

    }

//    private class GetShowVideoTask extends AsyncTask<String, Void, String> {
//
//        private OnGetShowVideoTaskListener listener;
//        public GetShowVideoTask(OnGetShowVideoTaskListener listener)
//        {
//            this.listener = listener;
//        }
//
//        @Override
//        protected String doInBackground(String... paramsValue) {
//            String result = "";
//            try {
//                InputStream inputStream = Utilities.getWebServiceInputStream(paramsValue[0], paramsValue[1], GET_VIDEO + paramsValue[2]);
//                if (inputStream != null)
//                    result = Utilities.convertInputStreamToString(inputStream);
//                else
//                    result = "FailedToConnect";
//            } catch (Exception e) {
//                Log.d("InputStream", e.getLocalizedMessage());
//            }
//            return result;
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            analyzeJsonString(result);
//            try {
//                eVideoView.pauseMedia();
//                eVideoView.setMediaPath(SERVER_BASE_URL + showVideoInformation.qualities.get(0).file);
//                eVideoView.prepareMedia(true);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//        private EVideoInformation analyzeJsonString(String result)
//        {
//            EVideoInformation newEVideoInformation = null;
//            try {
//                JSONObject videoInformation = new JSONObject(result).getJSONObject("video");
//                newEVideoInformation = new Gson().fromJson(videoInformation.toString(), EVideoInformation.class);
//                newEVideoInformation.type = EVideoInformation.EVIDEO_INFORMATION_SHOW_TYPE;
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            return newEVideoInformation;
//        }
//    }

//    private class GetIndexVideosTask extends AsyncTask<String, Void, String> {
//
//        @Override
//        protected String doInBackground(String... paramsValue) {
//            String result = "";
//            try {
//                InputStream inputStream = Utilities.getWebServiceInputStream(paramsValue[0], paramsValue[1], GET_ALL_VIDEOS);
//                if (inputStream != null)
//                    result = Utilities.convertInputStreamToString(inputStream);
//                else
//                    result = "FailedToConnect";
//            } catch (Exception e) {
//                Log.d("InputStream", e.getLocalizedMessage());
//            }
//            return result;
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            analyzeJsonString(result);
//            populateListView();
//        }
//
//        private void analyzeJsonString(String result)
//        {
//            try {
//                JSONArray jsonArray = new JSONArray(result);
//                for(int i = 0; i < jsonArray.length(); i++) {
//                    JSONObject videoInformation = jsonArray.getJSONObject(i);
//                    Gson gson = new Gson();
//                    EVideoInformation newEVideoInformation = gson.fromJson(videoInformation.toString(), EVideoInformation.class);
//                    newEVideoInformation.type = EVideoInformation.EVIDEO_INFORMATION_INDEX_TYPE;
//                    indexVideosInformation.add(newEVideoInformation);
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//    }

//    private class GetQueueVideosTask extends AsyncTask<String, Void, String> {
//
//        @Override
//        protected String doInBackground(String... paramsValue) {
//            String result = "";
//            try {
//                InputStream inputStream = Utilities.getWebServiceInputStream(paramsValue[0], paramsValue[1], GET_ALL_QUEUE_VIDEOS);
//                if (inputStream != null)
//                    result = Utilities.convertInputStreamToString(inputStream);
//                else
//                    result = "FailedToConnect";
//            } catch (Exception e) {
//                Log.d("InputStream", e.getLocalizedMessage());
//            }
//            return result;
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            analyzeJsonString(result);
//            populateListView();
//        }
//
//        private void analyzeJsonString(String result)
//        {
//            try {
//                JSONArray jsonArray = new JSONArray(result);
//                for(int i = 0; i < jsonArray.length(); i++) {
//                    JSONObject videoInformation = jsonArray.getJSONObject(i);
//                    Gson gson = new Gson();
//                    EVideoInformation newEVideoInformation = gson.fromJson(videoInformation.toString(), EVideoInformation.class);
//                    newEVideoInformation.type = EVideoInformation.EVIDEO_INFORMATION_QUEUE_TYPE;
//                    queueVideosInformation.add(newEVideoInformation);
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//    }

//    private class GetVideoTask extends AsyncTask<String, Void, String> {
//
//        EVideoInformation newEVideoInformation;
//
//        @Override
//        protected String doInBackground(String... paramsValue) {
//            String result = "";
//            try {
//                InputStream inputStream = Utilities.getWebServiceInputStream(paramsValue[0], paramsValue[1], GET_VIDEO + paramsValue[2]);
//                if (inputStream != null)
//                    result = Utilities.convertInputStreamToString(inputStream);
//                else
//                    result = "FailedToConnect";
//            } catch (Exception e) {
//                Log.d("InputStream", e.getLocalizedMessage());
//            }
//            return result;
//        }
//
//        // onPostExecute displays the results of the AsyncTask.
//        @Override
//        protected void onPostExecute(String result) {
//            analyzeJsonString(result);
//            try {
//                eVideoView.setMediaPath(SERVER_BASE_URL + showVideoInformation.qualities.get(0).file);
////                eVideoView.setMediaPath("http://video.erixe.com/videos/2/qualities/original.mp4");
//                eVideoView.prepareMedia(true);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//        private void analyzeJsonString2(String result)
//        {
//
//        }
//
//        private void analyzeJsonString(String result)
//        {
//            try {
//                JSONObject jsonObject = new JSONObject(result);
//                JSONObject videoInformation = jsonObject.getJSONObject("video");
//                newEVideoInformation = new EVideoInformation();
//
//                analyzeGeneralInformation(videoInformation);
//                analyzeUserInformation(videoInformation);
//                analyzeCoverInformation(videoInformation);
//                analyzeQualitiesInformation(videoInformation);
//                analyzeThumbnailsInformation(videoInformation);
//                analyzeSubtitlesInformation(videoInformation);
//
//                showVideoInformation = newEVideoInformation;
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//
//        private void analyzeGeneralInformation(JSONObject videoInformation){
//            try {
//                Gson gson = new Gson();
//                newEVideoInformation = gson.fromJson(videoInformation.toString(), EVideoInformation.class);
//
//                newEVideoInformation.id = videoInformation.getString("id");
////                newEVideoInformation.title = videoInformation.getString("title");
////                newEVideoInformation.description = videoInformation.getString("description");
////                newEVideoInformation.duration = videoInformation.getString("duration");
////                newEVideoInformation.published = videoInformation.getString("published");
////                newEVideoInformation.user_id = videoInformation.getString("user_id");
////                newEVideoInformation.original_clip_id = videoInformation.getString("original_clip_id");
////                newEVideoInformation.selected_cover_id = videoInformation.getString("selected_cover_id");
////                newEVideoInformation.created_at = videoInformation.getString("created_at");
////                newEVideoInformation.updated_at = videoInformation.getString("updated_at");
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//
//        private void analyzeUserInformation(JSONObject videoInformation){
//            try {
//                JSONObject videoUserInformation = videoInformation.getJSONObject("user");
//                newEVideoInformation.user.id = videoUserInformation.getString("id");
//                newEVideoInformation.user.name = videoUserInformation.getString("name");
//                newEVideoInformation.user.email = videoUserInformation.getString("email");
//                newEVideoInformation.user.confirmed = videoUserInformation.getString("confirmed");
//                newEVideoInformation.user.created_at = videoUserInformation.getString("created_at");
//                newEVideoInformation.user.updated_at = videoUserInformation.getString("updated_at");
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//
//        private void analyzeCoverInformation(JSONObject videoInformation){
//            try {
//                JSONObject videoCoverInformation = videoInformation.getJSONObject("cover");
//                newEVideoInformation.cover.id = videoCoverInformation.getString("id");
//                newEVideoInformation.cover.time = videoCoverInformation.getString("time");
//                newEVideoInformation.cover.file = videoCoverInformation.getString("file");
//                newEVideoInformation.cover.video_id = videoCoverInformation.getString("video_id");
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//
//        private void analyzeQualitiesInformation(JSONObject videoInformation){
//            try {
//                JSONArray videoQualitiesInformation = videoInformation.getJSONArray("qualities");
//
//                EVideoInformationQuality newQuality = new EVideoInformationQuality();
//                for(int i = 0; i < videoQualitiesInformation.length(); i++)
//                {
//                    JSONObject currentVideoQuality = videoQualitiesInformation.getJSONObject(i);
//                    newQuality.id = currentVideoQuality.getString("id");
//                    newQuality.width = currentVideoQuality.getString("width");
//                    newQuality.height = currentVideoQuality.getString("height");
//                    newQuality.file = currentVideoQuality.getString("file");
//                    newQuality.video_id = currentVideoQuality.getString("video_id");
//
//                    newEVideoInformation.qualities.add(newQuality);
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//
//        private void analyzeThumbnailsInformation(JSONObject videoInformation){
//            try {
//                JSONArray videoThumbnailsInformation = videoInformation.getJSONArray("thumbnail");
//
//                EVideoInformationThumbnail newThumbnail = new EVideoInformationThumbnail();
//                for (int i = 0; i < videoThumbnailsInformation.length(); i++) {
//                    JSONObject currentVideoQuality = videoThumbnailsInformation.getJSONObject(i);
//                    newThumbnail.id = currentVideoQuality.getString("id");
//                    newThumbnail.time = currentVideoQuality.getString("time");
//                    newThumbnail.file = currentVideoQuality.getString("file");
//                    newThumbnail.video_id = currentVideoQuality.getString("video_id");
//
//                    newEVideoInformation.thumbnails.add(newThumbnail);
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//
//        private void analyzeSubtitlesInformation(JSONObject videoInformation) {
//            try {
//                JSONArray videoSubtitlesInformation = videoInformation.getJSONArray("subtitles");
//
//                EVideoInformationSubtitle newSubtitle = new EVideoInformationSubtitle();
//                for (int i = 0; i < videoSubtitlesInformation.length(); i++) {
//                    JSONObject currentVideoQuality = videoSubtitlesInformation.getJSONObject(i);
//                    newSubtitle.id = currentVideoQuality.getString("id");
//                    newSubtitle.language_id = currentVideoQuality.getString("language_id");
//                    newSubtitle.file = currentVideoQuality.getString("file");
//                    newSubtitle.video_id = currentVideoQuality.getString("video_id");
//
//                    newEVideoInformation.subtitles.add(newSubtitle);
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//    }

}