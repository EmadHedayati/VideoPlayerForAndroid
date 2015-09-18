package erixe.android.videoplayer.EVideoInformationModels;

/**
 * Created by emad on 9/1/2015.
 */
public class EVideoInformationComment {
    public String id, body, approved, created_at, updated_at;
    public EVideoInformationUser user = new EVideoInformationUser();
}
