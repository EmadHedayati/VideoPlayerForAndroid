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
import erixe.android.videoplayer.EVideoInformationModels.EVideoInformationComment;
import erixe.android.videoplayer.EWebServices.EWebServiceStrings;
import erixe.android.videoplayer.R;
import erixe.android.videoplayer.Utilities.EVideoCoversCache;

public class CommentListItemAdapter extends ArrayAdapter<EVideoInformationComment> {

    Context context;
    List<EVideoInformationComment> eVideoInformationComments;
    ViewHolder vh;

    public CommentListItemAdapter(Context context, List<EVideoInformationComment> eVideoInformationComments) {
        super(context, R.layout.comment_list_item_layout, eVideoInformationComments);
        this.context = context;
        this.eVideoInformationComments = eVideoInformationComments;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;

        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.comment_list_item_layout, null);

            vh = new ViewHolder();
            vh.commentListItemUsername = (TextView) convertView.findViewById(R.id.commentListItemUsername);
            vh.commentListItemBody = (TextView) convertView.findViewById(R.id.commentListItemBody);
            vh.commentListItemTime = (TextView) convertView.findViewById(R.id.commentListItemTime);

            convertView.setTag(vh);
        }
        else {
            vh = (ViewHolder) convertView.getTag();
        }

        EVideoInformationComment currentEVideoInformationComment = eVideoInformationComments.get(position);
        vh = (ViewHolder) convertView.getTag();

        vh.commentListItemUsername.setText(currentEVideoInformationComment.user.name);
        vh.commentListItemBody.setText(currentEVideoInformationComment.body);
        vh.commentListItemTime.setText(currentEVideoInformationComment.created_at);

        return convertView;
    }

    public class ViewHolder
    {
        TextView commentListItemUsername;
        TextView commentListItemBody;
        TextView commentListItemTime;
    }

}
