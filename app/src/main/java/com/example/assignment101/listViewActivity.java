package com.example.assignment101;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.myibmwatsonapp.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class listViewActivity extends AppCompatActivity {
    private ArrayList<ImageList> results = new ArrayList<>();
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = db.getReference("Images");
        ArrayList<String> keys = new ArrayList<String>();
        ImageListAdapter adapter = new ImageListAdapter(
                this, R.layout.my_listview_item, results
        );
        dbRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                ImageList results = new ImageList(
                        (String) dataSnapshot.child("text").getValue(),
                        (String) (dataSnapshot.child("img").getValue()),
                        (String) dataSnapshot.child("score").getValue());
                adapter.add(results);
                keys.add(dataSnapshot.getKey());
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {    }
            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {    }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {    }});
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener(){
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                        ImageList result = results.get(position);
                        ImageView imageView = findViewById(R.id.imageViewIcon);
                        Uri uri = Uri.parse(result.getImage());
                        Intent intent = new Intent(view.getContext(), editDelete.class);
                        intent.putExtra("pos", position);
                        intent.putExtra("text", result.getText());
                        intent.putExtra("score", result.getScore());
                        intent.putExtra("uri", uri.toString());
                        startActivity(intent);

                    }
        });

    }

    public void add(View view) {
        Intent intent = new Intent(this, imgProcess.class);
        startActivity(intent);
        };

//    public void onActivityResult(int requestCode, int resultCode, Intent intent){
//        super.onActivityResult(requestCode, resultCode, intent);
//      if(requestCode == 2){
//          Bundle pos = getIntent().getExtras();
//          int position = pos.getInt("ibmpos");
//          results.remove(position);
//      }
//      else{
//
//      }
    public void deleteCheck(){
        Bundle checker = getIntent().getExtras();
        String check = checker.getString("delcheck");
        if(check != null){
            Bundle pos = getIntent().getExtras();
            int position = pos.getInt("ibmpos");
            results.remove(position);
    }
    };




}