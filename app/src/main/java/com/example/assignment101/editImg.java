package com.example.assignment101;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myibmwatsonapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;

public class editImg extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_img);
        ImageView imageView = findViewById(R.id.imageView2);
        EditText textView = findViewById(R.id.textView2);
        EditText textScore = findViewById(R.id.textView5);
        Bundle Text = getIntent().getExtras();
        String label = Text.getString("text");
        Bundle Sc = getIntent().getExtras();
        String score = Sc.getString("score");
        Bundle photoUri = getIntent().getExtras();
        String uri = photoUri.getString("uri");
        Uri img = Uri.parse(uri);
//        byte[] byteArray = getIntent().getByteArrayExtra("img");
//        Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        Bundle query = getIntent().getExtras();
        String editQ = query.getString("query");
        if (img != null) {
            imageView.setImageURI(img);
        }
        if (score != null) {
            textScore.setText(score);
        }
        if (label != null) {
            textView.setText(label);
        }

        }


    public void cancel(View view) { //This should just go back to imgProcess
       Intent intent = new Intent(this, imgProcess.class);
       startActivity(intent);
    }

    public void save(View view) {
        Bundle query = getIntent().getExtras();
        String editQ = query.getString("query");
        EditText textView = findViewById(R.id.textView2);
        EditText textScore = findViewById(R.id.textView5);
        textView.setText(textView.getText());
        textScore.setText(textScore.getText());
        if(editQ != null){
            updateDB();
        }
        else{
        addImagesToRealtimeDB();}
    }
    public void addImagesToRealtimeDB() {
        EditText textView = findViewById(R.id.textView2);
        EditText textScore = findViewById(R.id.textView5);
//        byte[] byteArray = getIntent().getByteArrayExtra("img");
//        Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
//        String imgDB = Base64.encodeToString(byteArray, Base64.DEFAULT);
        Bundle photoUri = getIntent().getExtras();
        String uri = photoUri.getString("uri");
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = db.getReference("Images");
        ImageList results = new ImageList(textView.getText().toString(), uri, textScore.getText().toString());
        String key = dbRef.push().getKey();

        dbRef.child(key).child("text").setValue(results.getText());
        dbRef.child(key).child("score").setValue(results.getScore());
        dbRef.child(key).child("img").setValue(uri);

        Intent intent = new Intent(this, listViewActivity.class);
        startActivity(intent);}

        public void updateDB(){
            EditText textView = findViewById(R.id.textView2);
            EditText textScore = findViewById(R.id.textView5);
            Bundle Text = getIntent().getExtras();
            String label = Text.getString("text");
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
            Query childQuery = ref.child("Images").orderByChild("text").equalTo(label);

            childQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                private String TAG = null;

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                        childSnapshot.getRef().child("text").setValue(textView.getText().toString());
                        childSnapshot.getRef().child("score").setValue(textScore.getText().toString());
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e(TAG, "onCancelled", databaseError.toException());
                }
            });
            Intent intent = new Intent(this, listViewActivity.class);
            startActivity(intent);
        };
};


//}




