package com.example.application.asynctask;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

/**
 * Created by application on 16/08/09.
 */
public class MonochromeTask extends AsyncTask<Bitmap, Integer, Bitmap> {

    private ImageView mImageView;
    private ProgressDialog mDialog;
    private Context mContext;

    public MonochromeTask(Context context, ImageView imageView){
        super();
        mContext = context;
        mImageView = imageView;
        Log.d("MonochromeTask", "mContext = " + mContext.toString());
    }

    @Override
    protected void onPreExecute() {
        // 処理前にプログレスバーを準備
        mDialog = new ProgressDialog(mContext);
        mDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mDialog.setIndeterminate(false);
        mDialog.setMax(100); // 100%表記
        mDialog.show();
    }

    @Override
    protected Bitmap doInBackground(Bitmap... bitmap) {

        // 非同期で処理する
        Bitmap out = bitmap[0].copy(Bitmap.Config.ARGB_8888, true);

        int width = out.getWidth();
        int height = out.getWidth();
        int totalPixcel = width*height;

        int i,j;
        for(j = 0; j < height; j++){
            for(i = 0; i < width; i++){
                int pixelColor = out.getPixel(i, j);
                int y = (int)(0.299 * Color.red(pixelColor) +
                        0.587 * Color.green(pixelColor) +
                        0.114 * Color.blue(pixelColor));
                out.setPixel(i, j, Color.rgb(y, y, y));
                /*try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }*/
                float percent = ((i + j*height) / (float)totalPixcel) * 100;
                publishProgress((int)percent);
            }
        }

        return out;
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
        // 実行中
        mDialog.setProgress(progress[0]);
    }

    @Override
    protected void onPostExecute(Bitmap result) {

        // 実行後にImageViewへ反映
        mDialog.dismiss();
        mImageView.setImageBitmap(result);
    }
}
