package com.example.assignment101;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import com.example.myibmwatsonapp.R;
import com.google.gson.Gson;
import com.ibm.watson.developer_cloud.android.library.camera.CameraHelper;
import com.ibm.watson.developer_cloud.android.library.camera.GalleryHelper;
import com.ibm.watson.developer_cloud.service.security.IamOptions;
import com.ibm.watson.developer_cloud.visual_recognition.v3.VisualRecognition;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifiedImages;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifyOptions;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Arrays;

import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;





public class imgVerify extends AppCompatActivity {

    private ImageView imageView;
    private TextView textView;
    private VisualRecognition visualRecognition;
    private CameraHelper cameraHelper;
    private GalleryHelper galleryHelper;
    private Bitmap photoBitmap;
    private Uri contentUri;
    private final String api_key = "o5k9tqf0fkSwY13bCKQSju4YTwp8OuwSqAU4LDFohLfa";
    private File FileOutputStream;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_img_verify);
        ImageView imageView = findViewById(R.id.verImage);
        TextView textView = findViewById(R.id.verText);
        TextView textScore = findViewById(R.id.textScore);
//        byte[] byteArray = getIntent().getByteArrayExtra("img");
//        Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        Bundle photoUri = getIntent().getExtras();
        String uri = photoUri.getString("uri");
        Uri img = Uri.parse(uri);
        Bundle photoText = getIntent().getExtras();
        String label = photoText.getString("text");
        Bundle score = getIntent().getExtras();
        String photoScore = score.getString("score");
        Bundle ibmText = getIntent().getExtras();
        String ibm = ibmText.getString("ibm");
        if(uri != null){
            this.setTitle("IBM Watson ML Cloud Services");
            textView.setText(label);
            imageView.setImageURI(img);
            if(ibm != null){
            textScore.setText("IBM Watson: Detected item: " + label + "\nDetected score: " + photoScore);}
            else{
                this.setTitle("Google Firebase ML Cloud Services");
                textScore.setText("Firebase ML: Detected item: " + label + "\nDetected score: " + photoScore);}
        }



        }



    public void gotoeditImg(View view) {
        Intent intent = new Intent(this, editImg.class);
        TextView textScore = findViewById(R.id.textScore);
        TextView textView = findViewById(R.id.verText);
//        byte[] byteArray = getIntent().getByteArrayExtra("img");
        Bundle photoUri = getIntent().getExtras();
        String uri = photoUri.getString("uri");
        intent.putExtra("text", textView.getText());
        intent.putExtra("score", textScore.getText());
        intent.putExtra("uri", uri);
        startActivity(intent);
    }
}
