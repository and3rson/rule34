package com.dunai.rule34.helpers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ImageView;
import com.dunai.rule34.R;
import com.dunai.rule34.interfaces.OnDownloadImageListener;

import java.io.*;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Formatter;
import java.util.HashMap;

public class DownloadAndCacheImageTask extends AsyncTask<String, Void, Bitmap> {
    private static final String TAG = "resonance/DownloadAndCacheImageTask";
    private final OnDownloadImageListener listener;
    ImageView bmImage;

    private boolean isCached = false;
    private Point downSize = null;
    private Point upSize = null;
    private Context context;

    public DownloadAndCacheImageTask(Context context, ImageView bmImage, @Nullable OnDownloadImageListener listener) {
        this.context = context;
        this.bmImage = bmImage;
        this.listener = listener;
    }

    public DownloadAndCacheImageTask downscaleTo(Point downSize) {
        this.downSize = downSize;
        return this;
    }

    public DownloadAndCacheImageTask upscaleTo(Point upSize) {
        this.upSize = upSize;
        return this;
    }

    public Bitmap getFromCache(String slug) {
        File image = new File(this.context.getCacheDir(), slug + "_r34.jpg");
        if (image.exists() && !image.isDirectory()) {
            return BitmapFactory.decodeFile(image.getAbsolutePath());
        } else {
            return null;
        }
    }

    public void run(String... params) {
        if (this.bmImage != null) {
            if (params[0].equals(this.bmImage.getTag(R.id.TAG_URL))) {
                return;
            } else {
                this.bmImage.setImageDrawable(this.bmImage.getContext().getResources().getDrawable(R.drawable.empty));
            }
        }
        this.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
    }

    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];

        if (this.bmImage != null) {
            this.bmImage.setTag(R.id.TAG_URL, urldisplay);
        }

        String slug;

        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.reset();
            md5.update(urldisplay.getBytes());
            byte[] digest = md5.digest();
            BigInteger bigInt = new BigInteger(1, digest);
            String hashtext = bigInt.toString(16);
            // Now we need to zero pad it if you actually want the full 32 chars.
            while (hashtext.length() < 32 ) {
                hashtext = "0" + hashtext;
            }
            slug = hashtext;
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, e.getMessage(), e);
            return null;
        }
//        String slug = urldisplay.replaceAll("[^a-zA-Z0-9_\\-]+", "_");

        Bitmap mIcon11 = this.getFromCache(slug);

        if (mIcon11 == null) {
            Log.i(TAG, "Downloading image for URL " + urldisplay);
            this.isCached = false;
            try {
                URL dest = new URL(urldisplay);
                HttpURLConnection yc = (HttpURLConnection) dest.openConnection();
//                yc.setInstanceFollowRedirects(true);
                yc.setUseCaches(false);
                yc.connect();
                if (yc.getResponseCode() == 301 || yc.getResponseCode() == 302) {
                    dest = yc.getURL();
                    yc = (HttpURLConnection) dest.openConnection();
                }
//                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(yc.getInputStream());
            } catch (Exception e) {
                Log.e(TAG, "Error: " + e.getMessage());
                e.printStackTrace();
                return null;
            }

            try {
                File cacheFile = new File(this.context.getCacheDir(), slug + "_r34.jpg");
                FileOutputStream out = new FileOutputStream(cacheFile);
                if (mIcon11 != null) {
                    mIcon11.compress(Bitmap.CompressFormat.JPEG, 90, out);
                }
            } catch(FileNotFoundException e) {
                Log.i(TAG, e.getMessage(), e);
                return null;
            }
        } else {
            Log.i(TAG, "Loading cached image for URL " + urldisplay);
            this.isCached = true;
        }
        return mIcon11;
    }

    protected void onPostExecute(Bitmap result) {
        if (result == null) {
            return;
        }

        if (this.downSize != null) {
            float w = result.getWidth();
            float h = result.getHeight();

            float kx = w / this.downSize.x;
            float ky = h / this.downSize.y;

            Log.i(TAG, new Formatter().format("Image size: %dx%d, koefs: %fx%f", (int) w, (int) h, kx, ky).toString());

            int ws, hs;

            if (kx > 1 && ky > 1) {
                if (kx > ky) {
                    ws = (int) (w / ky);
                    hs = (int) (h / ky);
                } else {
                    ws = (int) (w / kx);
                    hs = (int) (h / kx);
                }
                result = Bitmap.createScaledBitmap(result, ws, hs, false);

                Log.i(TAG, new Formatter().format("Image downsized from %dx%d to %dx%d", (int) w, (int) h, ws, hs).toString());
            }
        }

        if (this.upSize != null) {
            result = Bitmap.createScaledBitmap(result, this.upSize.x, this.upSize.y, true);
        }

        if (bmImage != null) {
            bmImage.setImageBitmap(result);
        }

        if (listener != null) {
            listener.onDownload(result);
        }
    }

    @Override
    protected void onCancelled(Bitmap result) {
        super.onCancelled();
    }
}
