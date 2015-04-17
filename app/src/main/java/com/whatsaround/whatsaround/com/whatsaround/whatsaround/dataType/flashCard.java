package com.whatsaround.whatsaround.com.whatsaround.whatsaround.dataType;

import android.graphics.Bitmap;
import android.media.Image;

/**
 * Created by Kyle on 04/16/2015.
 */
public class flashCard {
    private Bitmap picture;
    private String word;

    public void setPicture(Bitmap picture) { this.picture = picture; }

    public Bitmap getPicture() {
        return picture;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getWord() {
        return word;
    }

    @Override
    public String toString() {
        return word;
    }
}
