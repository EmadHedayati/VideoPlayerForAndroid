package erixe.android.videoplayer.EVideoInformationModels;

/**
 * Created by emad on 9/1/2015.
 */
public class EQueueInformation {
    public String id, video_id, user_id, queued_at;
    public EVideoInformation video = new EVideoInformation();
    public EVideoInformationUser user = new EVideoInformationUser();
}
