package com.example.bipinsubedi.lsense;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.IOException;
import java.lang.String;
import java.sql.Struct;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class MainActivity extends AppCompatActivity  {
    SurfaceView cameraView;
    TextView textView;
    CameraSource cameraSource;
    final int RequestCameraPermissionID=1001;
    TextToSpeech tospeech;
    int speechresult;
    boolean tap=false;

    public void toggle(){
        tap = !tap;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
       switch (requestCode){
           case RequestCameraPermissionID:
           {
               if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                   if(ActivityCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.CAMERA)!=PackageManager.PERMISSION_GRANTED){
                       return;
                   }
                   try{
                       cameraSource.start(cameraView.getHolder());
                   }catch (IOException e){

                   }
               }
           }
       }
    }
  /*  public boolean onTouch(View v, MotionEvent e){
        switch (e.getAction()){
            case MotionEvent.ACTION_DOWN:
                if(v.getId()==cameraView.getId()){
                    Toast.makeText(MainActivity.this,"FuckOFf",Toast.LENGTH_LONG).show();
                }
        }
        return true;
    }*/
  public void capture(){

  }
    public void getspeechinput(View v){
        Toast.makeText(MainActivity.this,"Try",Toast.LENGTH_LONG).show();
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        toggle();
        if(intent.resolveActivity(getPackageManager())!=null){
            startActivityForResult(intent,10);
        }
        else{
            Toast.makeText(MainActivity.this, " your device doesn't support speech recognition! ", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(tospeech != null){
            tospeech.stop();
            tospeech.shutdown();
        }
    }

    public void Makespeech(boolean onoff, String a){
        if(onoff){
            if(speechresult==TextToSpeech.LANG_MISSING_DATA || speechresult == TextToSpeech.LANG_NOT_SUPPORTED){
                Toast.makeText(MainActivity.this, " Get the next device , your device doesn't support this feature", Toast.LENGTH_LONG).show();
            }
            else{
                tospeech.speak(a,TextToSpeech.QUEUE_FLUSH,null );
            }
        }
        else{
            tospeech.stop();
        }
    }
    String voiceinputdata;
    public void processcommand(String x){
        Toast.makeText(MainActivity.this, x.toUpperCase(), Toast.LENGTH_SHORT).show();
        if (x.toUpperCase().equals("READ")){
            Makespeech(true,textView.getText().toString());
            Toast.makeText(MainActivity.this,textView.getText().toString(), Toast.LENGTH_SHORT).show();
        }
        else if(x.toLowerCase().equals("calculate")){
            Makespeech(true,  new Calculate(textView.getText().toString()).finalanswer);
        }
        else if(x.toUpperCase().equals("TIME")){
            Date currentTime = Calendar.getInstance().getTime();
            String timecmd= "The time is "+ currentTime.toString();
            Makespeech(true, timecmd);

        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 10:
                if (resultCode==RESULT_OK && data != null){
                  ArrayList<String> Result= data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                  voiceinputdata = Result.get(0);
                  processcommand(voiceinputdata);
                }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cameraView=(SurfaceView) findViewById(R.id.surface_view);
        textView= (TextView) findViewById(R.id.text_view);
        tospeech=new TextToSpeech(MainActivity.this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if(i==TextToSpeech.SUCCESS){
                    speechresult=tospeech.setLanguage(Locale.getDefault());
                }
                else{
                    Toast.makeText(MainActivity.this, " your device doesn't support the text to speech feature",Toast.LENGTH_LONG).show();
                }
            }
        });
        TextRecognizer textRecognizer= new TextRecognizer.Builder(getApplicationContext()).build();

        if(!textRecognizer.isOperational()){
            Log.w("Main Activity","Detector dependencies are not yet available!");
        }
        else{
            cameraSource= new CameraSource.Builder(getApplicationContext(),textRecognizer)
                    .setFacing(CameraSource.CAMERA_FACING_BACK)
                    .setRequestedPreviewSize(1280,1024)
                    .setRequestedFps(2.0f)
                    .setAutoFocusEnabled(true)
                    .build();
            cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(SurfaceHolder surfaceHolder) {
                    try{
                        if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{Manifest.permission.CAMERA},RequestCameraPermissionID);
                            return;
                        }
                        cameraSource.start(cameraView.getHolder());



                    }
                    catch(IOException e){

                    }
                }



                @Override
                public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

                }

                @Override
                public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                    cameraSource.stop();
                }
            });
            textRecognizer.setProcessor(new Detector.Processor<TextBlock>() {
                @Override
                public void release() {

                }

                @Override
                public void receiveDetections(Detector.Detections<TextBlock> detections) {
                    final SparseArray<TextBlock> items = detections.getDetectedItems();
                    if(items.size() !=0){
                        textView.post(new Runnable() {
                            @Override
                            public void run() {
                                StringBuilder stringBuilder= new StringBuilder();
                                for(int i=0; i<items.size(); ++i){
                                    TextBlock item=items.valueAt(i);
                                    stringBuilder.append(item.getValue());
                                    stringBuilder.append("\n");
                                }
                                textView.setText(stringBuilder.toString());
                            }
                        });
                    }
                }
            });

        }
    }



}
