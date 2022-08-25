package com.parekhanya.indianfoodallergytester;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.parekhanya.indianfoodallergytester.ml.Model;

import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.label.Category;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

public class Camera extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    String currentPhotoPath;
    ImageView imageView;
    String label;

    @SuppressLint("QueryPermissionsNeeded")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        ActivityResultLauncher<Intent> getResult = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent intent = result.getData();
                    }
                }
        );
        imageView = findViewById(R.id.imageView);
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                       "com.parekhanya.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                @SuppressLint("SetTextI18n") ActivityResultLauncher<Intent> ActivityResultLauncher = registerForActivityResult(
                        new ActivityResultContracts.StartActivityForResult(),
                        result -> {
                            if (result.getResultCode() == Activity.RESULT_OK) {
                                Intent data = result.getData();
                                Bundle extras = data.getExtras();
                                Bitmap imageBitmap = (Bitmap) extras.get("data");
                                imageView.setImageBitmap(imageBitmap);
                                try {
                                    Model model = Model.newInstance(getApplicationContext());
                                    // Creates inputs for reference
                                    TensorImage image = TensorImage.fromBitmap(imageBitmap);
                                    // Runs model inference and gets result
                                    Model.Outputs outputs = model.process(image);
                                    List<Category> probability = outputs.getProbabilityAsCategoryList();
                                    float maxScore = 0;
                                    for(int x = 0; x < probability.size(); x++) {
                                        if(probability.get(x).getScore() > maxScore) {
                                            maxScore = probability.get(x).getScore();
                                            label = probability.get(x).getLabel();
                                        }
                                    }
                                    TextView textView = findViewById(R.id.textView);
                                    textView.setText("There is a "+(maxScore*100)+"% chance the food identified is "+label);
                                    // Releases model resources if no longer used.
                                    model.close();
                                } catch (IOException e) {
                                    Log.i("error", "model didn't load :(");
                                }
                            }
                        });
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                ActivityResultLauncher.launch(intent);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    public void check(View view) {
        Intent intent = new Intent(this, CheckAllergies.class);
        intent.putExtra("label", label);
        startActivity(intent);
    }
}

    /*ImageView imageView;
    String currentPhotoPath;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageView = findViewById(R.id.imageView);
        ActivityResultLauncher<Intent> getResult = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent intent = result.getData();
                        /*Bundle extras = intent.getExtras();
                        Bitmap imageBitmap = (Bitmap) extras.get("data");
                        imageView.setImageBitmap(imageBitmap);
                    }
                }
        );
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Log.i("error", "take picture intent");
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.parekhanya.android.fileprovider",
                        photoFile);
                File imageFile = new File(Environment.getExternalStorageDirectory()
                        + "/MyApp/Camera.jpg");
                File file = new File(Environment.getExternalStorageDirectory() + "/MyApp");
                if (!file.exists()) {
                    file.mkdir();
                }
            Uri imageUri = FileProvider.getUriForFile(
                    Camera.this,
                    "com.parekhanya.android.fileprovider", //(use your app signature + ".provider" )
                    imageFile);

            //@SuppressLint("SdCardPath") Uri uriSavedImage = Uri.fromFile(new File("/sdcard/flashCropped.png"));
            Log.i("uri", String.valueOf(photoURI));
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            getResult.launch(takePictureIntent);
            }
        }
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix
                ".jpg",         /* suffix
                storageDir      /* directory
        );
        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }*/

