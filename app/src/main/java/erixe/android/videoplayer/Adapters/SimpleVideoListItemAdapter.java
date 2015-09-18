package erixe.android.videoplayer.Adapters;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import erixe.android.videoplayer.EVideoInformationModels.EVideoInformation;
import erixe.android.videoplayer.EWebServices.EWebServiceStrings;
import erixe.android.videoplayer.R;
import erixe.android.videoplayer.Utilities.EVideoCoversCache;

public class SimpleVideoListItemAdapter extends ArrayAdapter<EVideoInformation> {

    Context context;
    List<EVideoInformation> eVideoInformations;
    ViewHolder vh;

    public SimpleVideoListItemAdapter(Context context, List<EVideoInformation> eVideoInformations) {
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
            vh.listViewVideoLikes = (TextView) convertView.findViewById(R.id.listViewVideoLikes);
            vh.listViewVideoViews = (TextView) convertView.findViewById(R.id.listViewVideoViews);

            convertView.setTag(vh);
        }
        else {
            vh = (ViewHolder) convertView.getTag();
        }

        EVideoInformation currentEVideoInformation = eVideoInformations.get(position);
        vh = (ViewHolder) convertView.getTag();

        vh.listViewVideoDuration.setText(currentEVideoInformation.duration);
        setListViewVideoDurationText(Integer.parseInt(currentEVideoInformation.duration), vh.listViewVideoDuration);
        vh.listViewVideoTitle.setText(currentEVideoInformation.title);
        vh.listViewVideoDescription.setText(currentEVideoInformation.description);
        vh.listViewVideoLikes.setText(currentEVideoInformation.likes);
        vh.listViewVideoViews.setText(currentEVideoInformation.views + " views");

        try {
            loadBitmap(vh.listViewVideoCover, currentEVideoInformation);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return convertView;
    }

    public class ViewHolder
    {
        ImageView listViewVideoCover;
        TextView listViewVideoDuration;
        TextView listViewVideoTitle;
        TextView listViewVideoDescription;
        TextView listViewVideoLikes;
        TextView listViewVideoViews;
    }

    private void setListViewVideoDurationText(double videoDuration, TextView textView)
    {
        String minStr, secondStr;
        int hour = (int) (videoDuration / 3600);
        int min = (int) ((videoDuration - hour * 3600) / 60);
        int second = (int) (videoDuration - hour * 3600 - min * 60);

        if (min < 10)
            minStr = "0" + min;
        else
            minStr = String.valueOf(min);

        if (second < 10)
            secondStr = "0" + second;
        else
            secondStr = String.valueOf(second);

        String finalTime = "";
        if(hour == 0)
            finalTime = minStr + ":" + secondStr;
        else
            finalTime = hour + ":" + minStr + ":" + secondStr;

        textView.setText(finalTime);
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;
        String bitmapImageId;

        public DownloadImageTask(ImageView bmImage, String bmImageId) {
            this.imageView = bmImage;
            this.bitmapImageId = bmImageId;
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
            imageView.setImageBitmap(result);
            EVideoCoversCache.getInstance().addBitmapToMemoryCache(bitmapImageId, result);
        }
    }

    public void loadBitmap(ImageView imageView, EVideoInformation currentEVideoInformation) throws IOException {
        final Bitmap bitmap = EVideoCoversCache.getInstance().getBitmapFromMemCache(currentEVideoInformation.id);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        } else {
            new DownloadImageTask(imageView, currentEVideoInformation.id)
                    .execute(EWebServiceStrings.SERVER_BASE_URL + currentEVideoInformation.cover.file);
        }
    }
}
