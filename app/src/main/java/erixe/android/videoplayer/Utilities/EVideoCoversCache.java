package erixe.android.videoplayer.Utilities;

import android.graphics.Bitmap;
import android.util.LruCache;

/**
 * Created by emad on 9/5/2015.
 */
public class EVideoCoversCache {

    public static EVideoCoversCache eVideoCoversCache;
    private LruCache<String, Bitmap> mMemoryCache;

    public EVideoCoversCache()
    {
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;
        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                // The cache size will be measured in kilobytes rather than
                // number of items.
                return bitmap.getByteCount() / 1024;
            }
        };
    }

    public static EVideoCoversCache getInstance()
    {
        if(eVideoCoversCache == null)
        {
            eVideoCoversCache = new EVideoCoversCache();
            return eVideoCoversCache;
        }
        else
            return eVideoCoversCache;
    }

    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    public Bitmap getBitmapFromMemCache(String key) {
        return mMemoryCache.get(key);
    }

}
