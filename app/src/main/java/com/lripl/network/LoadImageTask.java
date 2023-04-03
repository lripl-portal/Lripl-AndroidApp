package com.lripl.network;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;

import com.lripl.utils.Utils;
import com.lripl.utils.ZoomableImageView;
import com.pnikosis.materialishprogress.ProgressWheel;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoadImageTask extends AsyncTask<String, Bitmap,Bitmap> {

    private ZoomableImageView imageView;
    private ProgressWheel progressWheel;
    public LoadImageTask(ProgressWheel progressWheel,ZoomableImageView imageView){
        this.imageView = imageView;
        this.progressWheel = progressWheel;
    }


    @Override
    protected Bitmap doInBackground(String... param) {
        return getBitmapFromURL(param[0]);
    }
    protected void onPostExecute(Bitmap result){
        super.onPostExecute(result);
        if(imageView != null && result != null) {
            progressWheel.setVisibility(View.INVISIBLE);
            imageView.setImageBitmap(result);
        }

    }

    private Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }
}
