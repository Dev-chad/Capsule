package kr.co.teamnova.chad.capsule;

import android.net.Uri;

/**
 * Created by Chad on 2017-02-02.
 */

public class ListViewContent {
    private String fileName;
    private String date;
    private String desc;
    private Uri image;
    private String publisher;
    private String publisherEmail;
    private Uri publisherProfileImage;

    public ListViewContent(){

    }

    public ListViewContent(Uri image, String desc, Uri publisherProfileImage, String publisher, String publisherEmail, String strDate, String fileName){

        this.fileName = fileName;
        date = strDate;
        this.desc = desc;
        this.image = image;
        this.publisher = publisher;
        this.publisherEmail = publisherEmail;
        this.publisherProfileImage = publisherProfileImage;
    }

    public void setDate(String strDate){
        date = strDate;
    }

    public void setDesc(String desc){
        this.desc = desc;
    }

    public void setImage(Uri image){
        this.image = image;
    }

    public void setPublisher(String strPublisher){
        publisher = strPublisher;
    }

    public void setPublisherEmail(String publisherEmail){
        this.publisherEmail = publisherEmail;
    }

    public void setPublisherProfileImage(Uri publisherProfileImage){
        this.publisherProfileImage = publisherProfileImage;
    }

    public void setFileName(String fileName){
        this.fileName = fileName;
    }

    public String getDate(){
        return date;
    }

    public String getContentDesc(){
        return desc;
    }

    public Uri getContentImage(){
        return image;
    }

    public String getPublisherName(){
        return publisher;
    }

    public String getPublisherEmail(){ return publisherEmail;}

    public Uri getPublisherProfileImage(){
        return publisherProfileImage;
    }

    public String getFileName(){ return fileName;}
}
