package org.app.application.model;

public class RecItem {

    private int mId;
    private int mImage;
    private String mText1;
    private String mText2;

    public RecItem(int id, int image, String text1, String text2) {
        this.mId = id;
        this.mImage = image;
        this.mText1 = text1;
        this.mText2 = text2;
    }

    public int getId() {
        return mId;
    }

    public int getImage() {
        return mImage;
    }

    public String getText1() {
        return mText1;
    }

    public String getText2() {
        return mText2;
    }
}