package com.solomon.sqlitesample;

/**
 * Created by user on 12/15/2015.
 */
public class Book {
    private String mImageId;
    private String mBookTitle;
    private String mBookAuthor;


    public Book(String id, String title, String author){
        mImageId = id;
        mBookTitle = title;
        mBookAuthor = author;
    }

    public String getImageId() {
        return mImageId;
    }
    public String getBookTitle() {
        return mBookTitle;
    }
    public String getBookAuthor() {
        return mBookAuthor;
    }
    public void setImageId(String id) {
        mImageId = id;
    }
    public void setBookTitle(String title) {
        mBookTitle = title;
    }
    public void setBookAuthor(String author) {
        mBookAuthor = author;
    }
}