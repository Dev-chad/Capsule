package kr.co.teamnova.chad.capsule;

import android.net.Uri;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Chad on 2017-02-02.
 */

public class ListViewContent {
    private String fileName;
    private String date;
    private String desc;
    private String publisher;
    private String publisherEmail;
    private Uri publisherProfileImage;
    private Uri image;
    private long dateMillisecond;

    public ListViewContent(){

    }

    public ListViewContent(Uri image, String desc, Uri publisherProfileImage, String publisher, String publisherEmail, long dateMillisecond, String fileName){

        this.fileName = fileName;
        this.dateMillisecond = dateMillisecond;
        this.desc = desc;
        date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date(dateMillisecond));
        this.image = image;
        this.publisher = publisher;
        this.publisherEmail = publisherEmail;
        this.publisherProfileImage = publisherProfileImage;
    }

    public void setDateFromString(String strDate){
        date = strDate;
    }

    public void setDateFromLong(Long dateMillisecond){
        this.dateMillisecond = dateMillisecond;
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

    public Long getDateToMillisecond(){
        return dateMillisecond;
    }
}
