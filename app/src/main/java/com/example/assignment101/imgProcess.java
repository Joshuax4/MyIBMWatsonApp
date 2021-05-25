package com.example.assignment101;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.myibmwatsonapp.R;

public class imgProcess extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_img_process);
    }


    public void gotocaptureLoadIBM(View view) {
        Intent intent = new Intent(this, captureLoad.class);
        intent.putExtra("ibmwatson", "Welcome to IBM Watson Machine Learning");
        startActivity(intent);
    }

    public void gotocaptureLoadML(View view) {
        Intent intent = new Intent(this, captureLoad.class);
        intent.putExtra("mlkit", "Welcome to Google Machine Learning Kit");
        startActivity(intent);
    }
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.assignment_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        Intent intent = new Intent(this, captureLoad.class);
        switch (item.getItemId()){
            case R.id.ibm:
                Toast.makeText(this, "Google ML Kit selected", Toast.LENGTH_SHORT);
                intent.putExtra("ibmwatson", "Welcome to IBM Watson Machine Learning");
                startActivity(intent);
                return true;

            case R.id.ml:
                Toast.makeText(this, "IBM Watson selected", Toast.LENGTH_SHORT);
                intent.putExtra("mlkit", "Welcome to Google Machine Learning Kit");
                startActivity(intent);
                return true;
        }
        return true;
}
}