package com.whatsaround.whatsaround.dataType;

/**
 * Created by Kyle on 03/29/2015.
 */
public class PictureWord {
    private String word;
    private String picUri;

    public PictureWord(String object, String uri){
        word = object;
        picUri = uri;
    }

    @Override
    public String toString() {
        return word;
    }
}
