package kr.co.teamnova.chad.capsule;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Chad on 2017-02-02.
 */

public class ListViewContent implements Parcelable{
    final static int MODE_TIME_RELATIVE = 1;
    final static int MODE_TIME_ABSOLUTE = 2;

    private String fileName;
    private String date;
    private String desc;
    private String publisher;
    private String publisherEmail;
    private Uri publisherProfileImage;
    private Uri image;
    private String location;
    private long dateMillisecond;
    private int timeMode = MODE_TIME_RELATIVE;

    public ListViewContent() {

    }

    public ListViewContent(Uri image, String desc, Uri publisherProfileImage, String publisher, String publisherEmail, long dateMillisecond, String fileName) {

        this.fileName = fileName;
        this.dateMillisecond = dateMillisecond;
        this.desc = desc;
        date = new SimpleDateFormat("yyyy/MM/dd/ HH:mm:ss").format(new Date(dateMillisecond));
        this.image = image;
        this.publisher = publisher;
        this.publisherEmail = publisherEmail;
        this.publisherProfileImage = publisherProfileImage;
        location = null;
    }

    protected ListViewContent(Parcel in) {
        fileName = in.readString();
        date = in.readString();
        desc = in.readString();
        publisher = in.readString();
        publisherEmail = in.readString();
        publisherProfileImage = in.readParcelable(Uri.class.getClassLoader());
        image = in.readParcelable(Uri.class.getClassLoader());
        location = in.readString();
        dateMillisecond = in.readLong();
        timeMode = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(fileName);
        dest.writeString(date);
        dest.writeString(desc);
        dest.writeString(publisher);
        dest.writeString(publisherEmail);
        dest.writeParcelable(publisherProfileImage, flags);
        dest.writeParcelable(image, flags);
        dest.writeString(location);
        dest.writeLong(dateMillisecond);
        dest.writeInt(timeMode);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ListViewContent> CREATOR = new Creator<ListViewContent>() {
        @Override
        public ListViewContent createFromParcel(Parcel in) {
            return new ListViewContent(in);
        }

        @Override
        public ListViewContent[] newArray(int size) {
            return new ListViewContent[size];
        }
    };

    public void setDateFromString(String strDate) {
        date = strDate;
    }

    public void setDateFromLong(Long dateMillisecond) {
        this.dateMillisecond = dateMillisecond;
        date = new SimpleDateFormat("yyyy년 MM월 dd일  HH:mm").format(new Date(dateMillisecond));
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setImage(Uri image) {
        this.image = image;
    }

    public void setPublisher(String strPublisher) {
        publisher = strPublisher;
    }

    public void setPublisherEmail(String publisherEmail) {
        this.publisherEmail = publisherEmail;
    }

    public void setPublisherProfileImage(Uri publisherProfileImage) {
        this.publisherProfileImage = publisherProfileImage;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setTimeMode(int mode) {
        timeMode = mode;
    }

    public String getDate() {
        return date;
    }

    public String getContentDesc() {
        return desc;
    }

    public Uri getContentImage() {
        return image;
    }

    public String getPublisherName() {
        return publisher;
    }

    public String getPublisherEmail() {
        return publisherEmail;
    }

    public Uri getPublisherProfileImage() {
        return publisherProfileImage;
    }

    public String getFileName() {
        return fileName;
    }

    public Long getDateToMillisecond() {
        return dateMillisecond;
    }

    public int getTimeMode() {
        return timeMode;
    }
}
