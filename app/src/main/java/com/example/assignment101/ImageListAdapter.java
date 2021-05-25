package com.example.assignment101;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myibmwatsonapp.R;

import java.util.ArrayList;

public class ImageListAdapter extends ArrayAdapter<ImageList> {
    ArrayList<ImageList> results;
    public ImageListAdapter(Context context, int resource, ArrayList<ImageList>objects){
        super(context, resource, objects);
        results = objects;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).
                    inflate(R.layout.my_listview_item, parent, false);
        }
        ImageList result = results.get(position);
        ImageView icon = (ImageView) convertView.findViewById(R.id.imageViewIcon);
        //todo - crashes here if you add a second image from ListViewActivity
        String uri = result.getImage();
        icon.setImageURI(Uri.parse(uri));
        TextView title = (TextView) convertView.findViewById(R.id.textViewTitle);
        title.setText(result.getText());
        return convertView;
    }

}

