package com.gt.go4lunch.data.repositories.places;

import android.graphics.Bitmap;
import android.util.LruCache;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.gt.go4lunch.MainApplication;

public class GooglePhotoRepo {

    private static GooglePhotoRepo sInstance;

    public static GooglePhotoRepo getInstance() {

        if (sInstance == null) {

            synchronized (GooglePhotoRepo.class) {

                if (sInstance == null) {

                    sInstance = new GooglePhotoRepo();
                }
            }
        }
        return sInstance;
    }

    private LruCache<String, Bitmap> photoCache = new LruCache<String, Bitmap>(50 * 1024 * 1024) {
        @Override
        protected int sizeOf(String key, Bitmap value) {
            return value.getByteCount();
        }
    }; // 50MiB

    public void getBitmapImage(String photoID, Callback callback) {

        String request = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=100&maxheight=100&photoreference=" + photoID + "&key=AIzaSyD2QiEmegY_f7FtO4vi1CJtt-Cfvtwxf5w";

        Bitmap image = photoCache.get(request);

        if (image != null) {
            callback.onImageLoaded(image);
        }

        Glide.with(MainApplication.getInstance()).asBitmap().load(request).listener(
                new RequestListener<Bitmap>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                        callback.onRequestFailed();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                        photoCache.put(request, resource);
                        callback.onImageLoaded(resource);
                        return false;
                    }
                }
        ).submit();
    }

    public interface Callback {

        void onImageLoaded(Bitmap image);

        void onRequestFailed();
    }
}
