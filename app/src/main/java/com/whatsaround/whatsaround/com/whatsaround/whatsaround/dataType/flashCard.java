package com.whatsaround.whatsaround.com.whatsaround.whatsaround.dataType;

import android.media.Image;

/**
 * Created by Kyle on 04/16/2015.
 */
public class flashCard {
    private Image picture;
    private String word;

    public void setPicture(Image picture) {
        this.picture = picture;
    }

    public Image getPicture() {
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
