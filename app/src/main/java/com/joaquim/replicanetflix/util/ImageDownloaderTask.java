package com.joaquim.replicanetflix.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.joaquim.replicanetflix.model.Category;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class ImageDownloaderTask extends AsyncTask<String, Void, Bitmap> {

    private final WeakReference<ImageView> imageViewWeakReference;

    public ImageDownloaderTask(ImageView imageView) {
        this.imageViewWeakReference = new WeakReference<>(imageView);
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        // catching parameters from "MainActivity"  - CategoryTask
        String url = params[0];
        HttpsURLConnection urlConnection = null;

        try {
            URL requestUrl = new URL(url);

            //opening connection
            urlConnection = (HttpsURLConnection) requestUrl.openConnection();

            //setting max time of each action
            urlConnection.setReadTimeout(20000);
            urlConnection.setConnectTimeout(20000);

            //catching server response
            int responseCode = urlConnection.getResponseCode();

            //manipulating server response. If no 200, error
            if (responseCode != 200) {
               return null;
            }

            //if server ok, read and manipulate all data
            InputStream inputStream = urlConnection.getInputStream();
            if (inputStream != null)
                return BitmapFactory.decodeStream(inputStream);


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null)
                urlConnection.disconnect();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (isCancelled())
            bitmap = null;

        ImageView imageView = imageViewWeakReference.get();
        if (imageView != null && bitmap != null) {

            if (bitmap.getWidth() < imageView.getWidth() || bitmap.getHeight() < imageView.getHeight()) {
                Matrix matrix = new Matrix();

                matrix.postScale((float) imageView.getWidth() / (float) bitmap.getWidth(),
                        (float) imageView.getHeight() / (float) bitmap.getHeight());

                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);
            }

            imageView.setImageBitmap(bitmap);
        }
    }
}
