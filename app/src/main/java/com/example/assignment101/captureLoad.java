package com.example.assignment101;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import com.example.myibmwatsonapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.objects.DetectedObject;
import com.google.mlkit.vision.objects.ObjectDetection;
import com.google.mlkit.vision.objects.ObjectDetector;
import com.google.mlkit.vision.objects.ObjectDetectorOptionsBase;
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions;
import com.ibm.watson.developer_cloud.android.library.camera.CameraHelper;
import com.ibm.watson.developer_cloud.android.library.camera.GalleryHelper;
import com.ibm.watson.developer_cloud.service.security.IamOptions;
import com.ibm.watson.developer_cloud.visual_recognition.v3.VisualRecognition;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifiedImages;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifyOptions;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;


public class captureLoad extends AppCompatActivity {

    private ImageView imageView;
    private TextView textView;
    private VisualRecognition visualRecognition;
    private CameraHelper cameraHelper;
    private GalleryHelper galleryHelper;
    private File photoFile;
    private Bitmap photoBitmap;
    private Uri contentUri;
    private final String api_key = "o5k9tqf0fkSwY13bCKQSju4YTwp8OuwSqAU4LDFohLfa";
    private Bitmap bmp;
    private String photoLabel;
    private static final int PICK_IMAGE_REQUEST = 1001;
    private static final int REQUEST_IMAGE_CAPTURE = 1000;
    private static final int REQUEST_PERMISSION = 3000;
    private Activity activity;
    private Uri outputFileUri;
    private final String TAG = "";
    private String currentPhotoPath;

/*      TODO - COMPRESS IMAGES FROM CAMERA CAPTURE
        TODO - ACTUALLY UPLOAD FILES TO DATABASE NOT LOCAL ARRAY
        TODO - BUG FIXES EXITING OUT OF CAMERA/LOAD
        TODO - CANCEL NEEDS TO REDIRECT TO IMGPROCESS WITHOUT SAVE
        TODO - GENERAL FORMATTING/PADDING
*
* */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture_load);
        Bundle extras = getIntent().getExtras();
        String ibm = extras.getString("ibmwatson");
        activity = this;
        if (ibm != null) {
            this.setTitle("IBM Watson ML Cloud Services");
            imageView = findViewById(R.id.imageView);
            textView = findViewById(R.id.textView);
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.ibmwatson1));
            TextView ibmText = findViewById(R.id.ibmText);
            ibmText.setText(ibm);
            cameraHelper = new CameraHelper(this);
            galleryHelper = new GalleryHelper(this);
            IamOptions options = new IamOptions.Builder()
                    .apiKey(api_key)
                    .build();
            visualRecognition = new VisualRecognition("2020-04-28", options);
        }
        else {
            this.setTitle("Google Firebase ML Cloud Services");
            imageView = findViewById(R.id.imageView);
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.mlkit1));
        }
    }

    public void capture(View view) {
        Bundle extras = getIntent().getExtras();
        String ibm = extras.getString("ibmwatson");
        if (ibm != null) {
            cameraHelper.dispatchTakePictureIntent();
        }
        else{
            if (checkPermissions() == false)
                return;
            Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            outputFileUri = getContentResolver()
                    .insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new ContentValues());
            takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            startActivityForResult(takePhotoIntent, REQUEST_IMAGE_CAPTURE);
        }
        }


    public void load(View view) {
        Bundle extras = getIntent().getExtras();
        String ibm = extras.getString("ibmwatson");
        if (ibm != null) {
            galleryHelper.dispatchGalleryIntent();
        }
        else{
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            activity.startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bundle extras = getIntent().getExtras();
        String ibm = extras.getString("ibmwatson");
        if (ibm != null) {
            if (requestCode == CameraHelper.REQUEST_IMAGE_CAPTURE) {
                photoBitmap = cameraHelper.getBitmap(resultCode);
                photoFile = cameraHelper.getFile(resultCode);
                imageView.setImageBitmap(photoBitmap);
                contentUri = Uri.parse(photoFile.toString());
            }

            if (requestCode == GalleryHelper.PICK_IMAGE_REQUEST) {
                photoBitmap = galleryHelper.getBitmap(resultCode, data);
                photoFile = galleryHelper.getFile(resultCode, data);
                imageView.setImageBitmap(photoBitmap);
                contentUri = Uri.parse(photoFile.toString());


            }

            runBackgroundThread();

        }
        else{
            TextView scoreText = findViewById(R.id.scoreText);
            TextView ibmText = findViewById(R.id.ibmText);
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                photoBitmap = getCapturedImage();
                imageView.setImageBitmap(photoBitmap);
            }if (requestCode == PICK_IMAGE_REQUEST){
                photoBitmap = getBitmap(resultCode, data);
                imageView.setImageBitmap(photoBitmap);
                outputFileUri = data.getData();
               }



                    InputImage image = InputImage.fromBitmap(photoBitmap, 0);

                    ObjectDetectorOptions options =new ObjectDetectorOptions.Builder()
                            .setDetectorMode(ObjectDetectorOptionsBase.SINGLE_IMAGE_MODE)
                            .enableMultipleObjects()
                            .enableClassification()
                            .build();

                    ObjectDetector objectDetector = ObjectDetection.getClient(options);

                    objectDetector.process(image).addOnSuccessListener(new OnSuccessListener<List<DetectedObject>>() {
                        public void onSuccess(List<DetectedObject> firebaseVisionObjects) {
                            if (firebaseVisionObjects.size() == 0) {
                                ibmText.setText("Detected objects: Unknown");
                                return;
                            } else {
                                ibmText.setText("Detected objects:");
                                for (DetectedObject object : firebaseVisionObjects) {
                                    if (object.getLabels().size() == 0)
                                        ibmText.setText("Detected objects: Unknown");
                                    else {
                                        for (DetectedObject.Label label : object.getLabels()) {
                                            CharSequence currText = ibmText.getText();
                                            CharSequence currScore = null;
                                            scoreText.setText(currScore + " " + label.getConfidence());
                                            scoreText.toString();
                                            if (label.getText().length() > 2) {
                                                ibmText.setText(currText + " " + label.getText());
                                            } else {
                                                ibmText.setText(currText + " " + "unknown");
                                            }
                                        }
                                    }
                                }
                            }
                            gotoImgVerify();
                        }
                    })
                     .addOnFailureListener(new OnFailureListener() {
                         @Override
                         public void onFailure(@NonNull Exception e) {
                             ibmText.setText("Failed");
                         }
                     });
            }

        }

    private Bitmap getCapturedImage() {
        Bitmap srcImage = null;
        try {
            srcImage = InputImage.fromFilePath(getBaseContext(), outputFileUri)
                    .getBitmapInternal();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return srcImage;}
    public Bitmap getBitmap(int resultCode, Intent data) {
    if (resultCode == RESULT_OK) {
        Uri targetUri = data.getData();
        contentUri = targetUri;
        try {
            return BitmapFactory.decodeStream(activity.getContentResolver().openInputStream(targetUri));
        } catch (FileNotFoundException e) {
            Log.e(TAG, "File Not Found", e);
            return null;
        }
    }
        Log.e(TAG, "Result Code was not OK");
    return null;
}


    private void runBackgroundThread() {
        TextView ibmText = findViewById(R.id.ibmText);
        textView = findViewById(R.id.textView);
        TextView scoreText = findViewById(R.id.scoreText);
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                InputStream imagesStream = null;
                try {
                    imagesStream = new FileInputStream(photoFile);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                ClassifyOptions classifyOptions = new ClassifyOptions.Builder()
                        .imagesFile(imagesStream)
                        .imagesFilename(photoFile.getName())
                        .threshold((float) 0.6)
                        .classifierIds(Arrays.asList("default"))
                        .build();
                ClassifiedImages result = visualRecognition.classify(classifyOptions).execute();

                Gson gson = new Gson();
                String json = gson.toJson(result);
                Log.d("json", json);
                String classLabel = null;
                try {
                    classLabel = new JSONObject(json)
                            .getJSONArray("images")
                            .getJSONObject(0)
                            .getJSONArray("classifiers")
                            .getJSONObject(0)
                            .getJSONArray("classes")
                            .getJSONObject(0)
                            .getString("class");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                final String label = classLabel;
                String getScore = null;
                try {
                    getScore = new JSONObject(json)
                            .getJSONArray("images")
                            .getJSONObject(0)
                            .getJSONArray("classifiers")
                            .getJSONObject(0)
                            .getJSONArray("classes")
                            .getJSONObject(0)
                            .getString("score");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String finalGetScore = getScore;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ibmText.setText(label);
                        scoreText.setText(finalGetScore);
                        Intent intent = new Intent(getApplicationContext(), imgVerify.class);
                        intent.putExtra("text", ibmText.getText());
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        photoBitmap.compress(Bitmap.CompressFormat.PNG, 10, stream);
                        contentUri = Uri.parse(photoFile.toString());
                        byte[] bytes = stream.toByteArray();
//                        intent.putExtra("img", bytes);
                        intent.putExtra("score", scoreText.getText());
                        intent.putExtra("ibm", "ibm");
                        intent.putExtra("uri", contentUri.toString());
                        startActivity(intent);
                    }
                });
            }
        });


    }
    private boolean checkPermissions() {
    String permissions[] = {
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
    boolean grantCamera = ContextCompat.checkSelfPermission(activity, permissions[0]) == PackageManager.PERMISSION_GRANTED;boolean grantExternal =                ContextCompat.checkSelfPermission(activity, permissions[1]) == PackageManager.PERMISSION_GRANTED;
    if (!grantCamera && !grantExternal) {
        ActivityCompat.requestPermissions(activity, permissions, REQUEST_PERMISSION);        }
    else if (!grantCamera) {
        ActivityCompat.requestPermissions(activity, new String[]{permissions[0]}, REQUEST_PERMISSION);
    }
    else if (!grantExternal) {
        ActivityCompat.requestPermissions(activity, new String[]{permissions[1]}, REQUEST_PERMISSION);        }

    return grantCamera && grantExternal;


}
    private void gotoImgVerify(){
        TextView scoreText = findViewById(R.id.scoreText);
        ImageView imageView = findViewById(R.id.imageView);
        TextView ibmText = findViewById(R.id.ibmText);
        Intent intent = new Intent(this, imgVerify.class);
        intent.putExtra("text", ibmText.getText());
//        intent.putExtra("img", bytes);
        intent.putExtra("score", scoreText.getText());
        intent.putExtra("uri", outputFileUri.toString());
        intent.putExtra("mlkit", "mlkit");
        startActivity(intent);
    }
}

