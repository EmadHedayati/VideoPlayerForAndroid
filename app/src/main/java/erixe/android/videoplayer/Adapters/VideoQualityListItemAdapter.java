package erixe.android.videoplayer.Adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import erixe.android.videoplayer.EVideoInformationModels.EVideoInformationComment;
import erixe.android.videoplayer.EVideoInformationModels.EVideoInformationQuality;
import erixe.android.videoplayer.R;

public class VideoQualityListItemAdapter extends ArrayAdapter<EVideoInformationQuality> {

    Context context;
    List<EVideoInformationQuality> eVideoInformationQualities;
    ViewHolder vh;

    public VideoQualityListItemAdapter(Context context, List<EVideoInformationQuality> eVideoInformationQualities) {
        super(context, R.layout.video_quality_list_item_layout, eVideoInformationQualities);
        this.context = context;
        this.eVideoInformationQualities = eVideoInformationQualities;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;

        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.video_quality_list_item_layout, null);

            vh = new ViewHolder();
            vh.videoQualityListItemTextView = (Button) convertView.findViewById(R.id.videoQualityListItemTextView);

            convertView.setTag(vh);
        }
        else {
            vh = (ViewHolder) convertView.getTag();
        }

        EVideoInformationQuality currentEVideoInformationQuality = eVideoInformationQualities.get(position);
        vh = (ViewHolder) convertView.getTag();

        vh.videoQualityListItemTextView.setText(currentEVideoInformationQuality.height + " P");

        return convertView;
    }

    public class ViewHolder
    {
        Button videoQualityListItemTextView;
    }

}
