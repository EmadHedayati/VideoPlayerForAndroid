package erixe.android.videoplayer.EVideoInformationModels;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by emad on 9/1/2015.
 */
public class EVideoInformation {

    public static final int EVIDEO_INFORMATION_INDEX_TYPE = 1;
    public static final int EVIDEO_INFORMATION_SHOW_TYPE = 2;
    public static final int EVIDEO_INFORMATION_QUEUE_TYPE = 3;

    public int type;
    public String id, title, description, duration, published, user_id, original_clip_id, selected_cover_id, created_at, updated_at;
    public EVideoInformationUser user = new EVideoInformationUser();
    public EVideoInformationCover cover = new EVideoInformationCover();
    public List<EVideoInformationQuality> qualities = new ArrayList<>();
    public List<EVideoInformationThumbnail> thumbnails = new ArrayList<>();
    public List<EVideoInformationSubtitle> subtitles = new ArrayList<>();

    public EVideoInformation()
    {

    }
}
