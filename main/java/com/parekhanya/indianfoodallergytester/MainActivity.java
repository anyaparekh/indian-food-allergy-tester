package com.parekhanya.indianfoodallergytester;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final String[] CAMERA_PERMISSION = new String[]{Manifest.permission.CAMERA};
    private static final int CAMERA_REQUEST_CODE = 10;
    public ArrayList<String> lines;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*AssetManager am = this.getAssets();
        InputStream is = null;
        try {
            lines = new ArrayList<String>();
            is = am.open("testy.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            while(reader.ready()) {
                String strLine = reader.readLine();
                lines.add(strLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        /*Button enableCamera = findViewById(R.id.scanFood);
        enableCamera.setOnClickListener(v -> {
            if (hasCameraPermission()) {
                enableCamera();
            } else {
                requestPermission();
            }
        });*/

    }

    /*private void enableCamera() {
        Intent intent = new Intent(this, CameraActivity.class);
        startActivity(intent);
    }

    private boolean hasCameraPermission() {
        return ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(
                this,
                CAMERA_PERMISSION,
                CAMERA_REQUEST_CODE
        );
    }*/

    public void addAllergies(View view) {
        Intent intent = new Intent(this, AddAllergies.class);
        startActivity(intent);
    }

    public void scanFood(View view) {
        Intent intent = new Intent(this, Camera.class);
        startActivity(intent);
    }
}