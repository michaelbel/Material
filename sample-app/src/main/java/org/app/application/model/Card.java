package org.app.application.model;

public class Card {

    private int mId;
    private int mImage;
    private String mText1;
    private String mText2;
    private String mText3;

    public Card(int id, int image, String text1, String text2, String text3) {
        this.mId = id;
        this.mImage = image;
        this.mText1 = text1;
        this.mText2 = text2;
        this.mText3 = text3;
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

    public String getText3() {
        return mText3;
    }
}