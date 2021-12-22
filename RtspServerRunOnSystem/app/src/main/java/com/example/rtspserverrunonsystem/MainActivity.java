package com.example.rtspserverrunonsystem;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Camera;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import net.majorkernelpanic.streaming.SessionBuilder;
import net.majorkernelpanic.streaming.gl.SurfaceView;
import net.majorkernelpanic.streaming.video.VideoQuality;
import net.majorkernelpanic.streaming.video.VideoStream;

import java.security.Permission;

public class MainActivity extends AppCompatActivity {
    ToggleButton btnStartService;
    SurfaceView mView;
    int width = 1920;
    int height = 1080;

    int framerate = 20;

    int biterate = 5500*1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        btnStartService = findViewById(R.id.playButton);
        mView = findViewById(R.id.surfaceView);
        buildSession();

        btnStartService.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    startService();
                }
                else{
                    stopService();
                }
            }
        });

        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.CAMERA}, 0);
        }
    }



    @Override
    protected void onStart() {
        super.onStart();
        if(isServiceStart(RtspService.class)){
            btnStartService.setChecked(true);
        }
        VideoStream.onActivity = true;
    }

    public void startService() {
        if(!isServiceStart(RtspService.class)){
            Intent serviceIntent = new Intent(this, RtspService.class);
            serviceIntent.putExtra("inputExtra", "Foreground Service Example in Android");
            ContextCompat.startForegroundService(this, serviceIntent);
        }
    }

    public void stopService() {
        if(isServiceStart(RtspService.class)){
            Intent serviceIntent = new Intent(this, RtspService.class);
            stopService(serviceIntent);
        }
    }


    public void buildSession(){
        SessionBuilder.getInstance()
                .setPreviewOrientation(90)// đổi góc hiển thị
                .setSurfaceView(mView)// chọn màn hình hiển thị
                .setContext(this)
                .setVideoEncoder( SessionBuilder.VIDEO_H264)// videc codec
                .setAudioEncoder(SessionBuilder.AUDIO_NONE)// audio codec
                .setVideoQuality(new VideoQuality(width, height, framerate, biterate));// video quality
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        VideoStream.onActivity = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        VideoStream.onActivity = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        VideoStream.onActivity = true;
    }
    private boolean isServiceStart(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}