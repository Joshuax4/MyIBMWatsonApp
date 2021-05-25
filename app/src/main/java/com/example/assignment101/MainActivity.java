package com.example.assignment101;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myibmwatsonapp.R;
import com.ibm.watson.developer_cloud.android.library.camera.CameraHelper;
import com.ibm.watson.developer_cloud.android.library.camera.GalleryHelper;
import com.ibm.watson.developer_cloud.visual_recognition.v3.VisualRecognition;

import java.io.File;
import java.util.zip.Inflater;

 //TODO - BUG FIXES EXITING OUT OF CAMERA/LOAD
//TODO - Make photos work from camera

public class MainActivity extends AppCompatActivity {

    private MenuItem item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void gotoimgPro(View view) {
        Intent intent = new Intent(this, imgProcess.class);
        startActivity(intent);
    }
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.assignment_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        Intent intent = new Intent(this, imgProcess.class);
        switch (item.getItemId()){
            case R.id.ml:
                Toast.makeText(this, "Google ML Kit selected", Toast.LENGTH_SHORT);
                startActivity(intent);
                return true;

            case R.id.ibm:
                Toast.makeText(this, "IBM Watson selected", Toast.LENGTH_SHORT);
                startActivity(intent);
                return true;
        }


        return true;
    }
}

