package com.example.assignment101;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class ImageList {
    String text;
    String image;
    String score;

    public ImageList(String text, String image, String score){
        this.text = text;
        this.image = image;
        this.score = score;
    }
    public String getText(){
        return this.text;
    }
    public String getImage(){
        return this.image;
    }
    public String getScore(){
        return this.score;
    }
    public String toString() {return this.text;}
}
