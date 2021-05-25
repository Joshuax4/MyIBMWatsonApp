package com.example.assignment101;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.fragment.app.FragmentActivity;

import com.example.myibmwatsonapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class editDelete extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_delete);
        ImageView imageView = findViewById(R.id.imageView2);
        TextView textTitle = findViewById(R.id.textView5);
        TextView textScore = findViewById(R.id.textView7);
        Bundle Text = getIntent().getExtras();
        String label = Text.getString("text");
        Bundle Sc = getIntent().getExtras();
        String score = Sc.getString("score");
        Bundle photoUri = getIntent().getExtras();
        String uri = photoUri.getString("uri");
        imageView.setImageURI(Uri.parse(uri));
        textTitle.setText(label);
        textScore.setText(score);
    }

    public void edit(View view) {
        Bundle Text = getIntent().getExtras();
        String label = Text.getString("text");
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        Query childQuery = ref.child("Images").orderByChild("text").equalTo(label);
        Bundle Sc = getIntent().getExtras();
        String score = Sc.getString("score");
        Bundle photoUri = getIntent().getExtras();
        String uri = photoUri.getString("uri");
        Intent intent = new Intent(this, editImg.class);
        intent.putExtra("text", label);
        intent.putExtra("score", score);
        intent.putExtra("uri", uri);
        intent.putExtra("query", childQuery.toString());
        startActivity(intent);
    }

    public void delete(View view){
        Bundle Text = getIntent().getExtras();
        String label = Text.getString("text");
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        Query childQuery = ref.child("Images").orderByChild("text").equalTo(label);

        childQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            private String TAG = null;

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                    childSnapshot.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled", databaseError.toException());
            }
        });
        Intent intent = new Intent(this, listViewActivity.class);
        startActivity(intent);
    }

    //Delete needs to reroute to list page, with onActivityResult, give it a result of 2, and if result_code=2, delete data sent from the edit page back to list page. results.get(position), etc etc. Pass index number through with all other data.

    //OnActivityResult - pass through result code (2?), and use switch/case, where case is 2, and the index number passed through is deleted from the array.

}