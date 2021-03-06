package com.example.patrick.paintCamera;

import android.Manifest;
import android.content.pm.PackageManager;
import android.hardware.SensorManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TextView;

import com.google.android.cameraview.AspectRatio;
import com.google.android.cameraview.CameraView;

public class MainActivity extends AppCompatActivity {

    private Dibujo dibujo;
    private CameraView cameraView;
    private static final int REQUEST_CAMERA_PERMISSION = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dibujo = findViewById(R.id.fondo);
        SensorManager sensorManager = (SensorManager)getSystemService(this.SENSOR_SERVICE);
        dibujo.setTexto((TextView) findViewById(R.id.txtCoordenadas));
        dibujo.setSensores(sensorManager);
        findViewById(R.id.botonLimpiar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dibujo.limpiarPantalla();
            }
        });

        cameraView = findViewById(R.id.camera);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        AspectRatio ratio = new AspectRatio(width,height-10);
        cameraView.setAspectRatio(ratio);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
            return;
        }
        cameraView.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        cameraView.stop();
    }

    @Override
    protected void onStop() {
        super.onStop();
        cameraView.stop();
    }
}