package com.example.ianand.adobeapphub;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

/**
 * Created by iAnand on 28-01-2015.
 */
public class AppDetailActivity extends Activity {

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_detail);

        TextView appTitle = (TextView)findViewById(R.id.appDetailTitle);
        ImageView appImage = (ImageView)findViewById(R.id.imageView);
        TextView appRating = (TextView)findViewById(R.id.rating);
        TextView appDesc = (TextView)findViewById(R.id.desc);
        TextView appType = (TextView)findViewById(R.id.appType);
        TextView appInAppPurchase = (TextView)findViewById(R.id.inapppurchase);
        TextView appLastUpdated = (TextView)findViewById(R.id.lastUpdated);
        Button btnAppStore = (Button)findViewById(R.id.btnAppStore);
        Button btnShareTo = (Button)findViewById(R.id.shareTo);

        Intent i = getIntent();
        final HashMap<String, String> appDetails = (HashMap<String, String>)i.getSerializableExtra("AppDetail");

        appTitle.setText(appDetails.get("name"));
        appRating.setText(appDetails.get("rating"));
        appDesc.setText(appDetails.get("description"));
        appType.setText(appDetails.get("type"));
        appInAppPurchase.setText(appDetails.get("inapp-purchase"));
        appLastUpdated.setText(appDetails.get("last updated"));
        new ImageLoadTask(appDetails.get("image"), appImage).execute();

        btnAppStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(appDetails.get("url")));
                startActivity(newIntent);
            }
        });

        btnShareTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TEXT, "This is shared from App hub" + appDetails.get("name"));
                shareIntent.setType("text/plain");
                startActivity(shareIntent);
            }
        });
    }

    private class ImageLoadTask extends AsyncTask<Void, Void, Bitmap> {

        private String url;
        private ImageView imageView;

        public ImageLoadTask(String url, ImageView imageView) {
            this.url = url;
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            try {
                URL urlConnection = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) urlConnection
                        .openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            imageView.setImageBitmap(result);
        }
    }
}


