package com.seferapp.animals_and_pepol_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class photo_sonuc extends AppCompatActivity {
     Uri file=null;
     ImageView user_photo;
     ImageView sonuc_photo;
    String adres="";
    File file_tosend;
    InterstitialAd interstitialAd;
    MediaPlayer btnSes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_sonuc);
        file = (Uri)getIntent().getExtras().get("fotgraf");
        user_photo=(ImageView)findViewById(R.id.user_photo_img_view);
        sonuc_photo=(ImageView)findViewById(R.id.sonuc_photo_img_view);
        adres= file.getPath();
         file_tosend = new File(adres);
         btnSes = MediaPlayer.create(this, R.raw.sonuc_sesi);
        Glide.with(getApplicationContext()).load(file_tosend).centerCrop().into(user_photo);
        Glide.with(getApplicationContext()).load(R.drawable.anmals_loading_foto).centerCrop().into(sonuc_photo);
        btnSes.start();
        LoadGecis();
        new getir_sonucu().execute();
    }
    class getir_sonucu extends AsyncTask<Void, Void, String> {
        protected String doInBackground(Void... params) {
            adres= file.getPath();
            String dosya = "";
            HttpURLConnection connection = null;
            DataOutputStream outputStream = null;
            InputStream inputStream = null;
            String twoHyphens = "--";
            String boundary = "*****" + Long.toString(System.currentTimeMillis()) + "*****";
            String lineEnd = "\r\n";
            String result = "";
            int bytesRead, bytesAvailable, bufferSize;
            byte[] buffer;
            int maxBufferSize = 1 * 1024 * 1024;
            String[] q = adres.split("/");
            int idx = q.length-1;
             String urlTo = "http://10.43.33.70/WebApplication1/api/fotoCheck/fotoBenzet";
           // String urlTo = "http://hayvanlar.seferapp.site/api/fotoCheck/fotoBenzet";
            try {
                FileInputStream fileInputStream = new FileInputStream(file_tosend);
                URL url = new URL(urlTo);
                connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.setUseCaches(false);
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Connection", "Keep-Alive");
                connection.setRequestProperty("User-Agent", "Android Multipart HTTP Client 1.0");
                connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
                outputStream = new DataOutputStream(connection.getOutputStream());
                outputStream.writeBytes(twoHyphens + boundary + lineEnd);
                outputStream.writeBytes("Content-Disposition: form-data; name=\"" + "image" + "\"; filename=\"" + q[idx] + "\"" + lineEnd);
                outputStream.writeBytes("Content-Type: " + "image/jpg" + lineEnd);
                outputStream.writeBytes("Content-Transfer-Encoding: binary" + lineEnd);
                outputStream.writeBytes(lineEnd);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                while (bytesRead>0){
                    outputStream.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                }
                outputStream.writeBytes(lineEnd);
                outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
                connection.connect();
                inputStream = connection.getInputStream();
                BufferedReader br = null;
                br = new BufferedReader(new InputStreamReader(inputStream));
                String satir;
                while((satir = br.readLine()) != null) {
                    dosya += satir;
                }
                fileInputStream.close();
                inputStream.close();
                outputStream.flush();
                outputStream.close();
                connection.disconnect();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }catch (MalformedURLException e) {
                e.printStackTrace();
            }catch (IOException e) {
                e.printStackTrace();
            }
            return dosya;
        }
        protected void onPostExecute(String s) {
            try{

                int sonuc=Integer.valueOf(s);
                int img=0;
                switch (sonuc)
                {
                    case 0:
                        img =R.drawable.aslan_1;
                        break;
                    case 1:
                        img =R.drawable.ayi_2;
                        break;
                    case 2:
                        img =R.drawable.gorela_3;
                        break;
                    case 3:
                        img =R.drawable.kedi_4;
                        break;
                    case 4:
                        img =R.drawable.kopek_5;
                        break;
                    case 5:
                        img =R.drawable.maymon_6;
                        break;
                    case 6:
                        img =R.drawable.nimir_7;
                        break;
                    case 7:
                        img =R.drawable.tilki_8;
                        break;
                    case 8:
                        img =R.drawable.ziip_9;
                        break;
                        default:
                            img=R.drawable.tryagin;
                }
                Glide.with(getApplicationContext()).load(img).centerCrop().into(sonuc_photo);
            } catch (Exception ex) {
                Glide.with(getApplicationContext()).load(R.drawable.tryagin).centerCrop().into(sonuc_photo);
            }
            btnSes.stop();
        }
    }

    @Override
    public void onBackPressed() {
        try{
            btnSes.stop();
            if(interstitialAd.isAdLoaded())
            {
                interstitialAd.show();
            }
        }catch (Exception ex){

        }
        super.onBackPressed();
    }
    public void  uygulamayi_paylas(View v)
    {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT,
                "Hey check out my app at: https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }
    private void LoadGecis(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try{
                    interstitialAd = new InterstitialAd(getApplicationContext(),"445565436122081_445928332752458");
                    interstitialAd.setAdListener(new InterstitialAdListener() {
                        @Override
                        public void onInterstitialDisplayed(Ad ad) {
                        }
                        @Override
                        public void onInterstitialDismissed(Ad ad) {
                            // Interstitial dismissed callback
                        }
                        @Override
                        public void onError(Ad ad, AdError adError) {
                            // Ad error callback
                        }
                        @Override
                        public void onAdLoaded(Ad ad) {

                        }
                        @Override
                        public void onAdClicked(Ad ad) {
                        }
                        @Override
                        public void onLoggingImpression(Ad ad) {
                        }
                    });
                    interstitialAd.loadAd();
                }catch (Exception ex){

                }
            }
        });
    }

}
