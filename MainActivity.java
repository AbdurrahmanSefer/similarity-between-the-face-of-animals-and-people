package com.seferapp.animals_and_pepol_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.LinearLayout;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import com.facebook.ads.AdSettings;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_WRITE_PERMISSION = 786;
    Uri file;

    private AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AdSettings.addTestDevice("3feb7756-ab2d-4cde-ac77-36dab31225a4");
        new LoadBanner().execute();
    }
    public void  chek_photo(View v)
    {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);
        }
        CropImage.startPickImageActivity(MainActivity.this);
    }
    @Override
    @SuppressLint("NewApi")
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try{
            if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
                Uri imageUri = CropImage.getPickImageResultUri(this, data);
                //NOW CROP IMAGE URI
                CropImage.activity(imageUri)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setMultiTouchEnabled(true)
                        //REQUEST COMPRESS SIZE
                        .setRequestedSize(800, 800)
                        //ASPECT RATIO, DELETE IF YOU NEED CROP ANY SIZE
                        .setAspectRatio(1,1)
                        .start(this);
            }

            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {
                    file=result.getUri();
                    Intent intent=new Intent(this,photo_sonuc.class);
                    intent.putExtra("fotgraf",file);
                    startActivity(intent);
                }
            }
        }catch (Exception ex) {
            String hata=ex.toString();
        }


    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 0) {
            if (requestCode == REQUEST_WRITE_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //THIS IS HAPPEN WHEN USER CLICK ALLOW ON PERMISSION
                //START PICK IMAGE ACTIVITY
                CropImage.startPickImageActivity(MainActivity.this);
            }
        }
    }
    private static File getOutputMediaFile(){
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "CameraDemo");
        if (!mediaStorageDir.exists()){
            if (!mediaStorageDir.mkdirs()){
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return new File(mediaStorageDir.getPath() + File.separator +
                "IMG_"+ timeStamp + ".jpg");
    }
    public class LoadBanner extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                SetBanner();
            } catch (Exception ex) {

            }

            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }
        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
        @Override
        protected void onCancelled(Void result) {
            super.onCancelled(result);
        }
    }
    private void SetBanner(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    adView = new AdView(getApplicationContext(), getResources().getString(R.string.bannerId), AdSize.BANNER_HEIGHT_50);
                    LinearLayout adContainer = (LinearLayout) findViewById(R.id.Ust_Banner);
                    if (adContainer.getChildCount() > 0)
                        RemoveLayout(adContainer);
                    setText(adContainer);
                    adView.setAdListener(new AdListener() {
                        @Override
                        public void onError(Ad ad, AdError adError) {
                        }
                        @Override
                        public void onAdLoaded(Ad ad){

                        }
                        @Override
                        public void onAdClicked(Ad ad) {

                        }
                        @Override
                        public void onLoggingImpression(Ad ad) {
                        }
                    });
                    adView.loadAd();
                } catch (Exception ex) {
                }
            }
        });
    }

    private void setText(final LinearLayout text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    text.addView(adView);
                } catch (Exception ex) {

                }

            }
        });
    }
    private void RemoveLayout(final LinearLayout text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                text.removeAllViews();
            }
        });
    }
}
