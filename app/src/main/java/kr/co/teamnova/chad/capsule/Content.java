package kr.co.teamnova.chad.capsule;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Chad on 2017-02-02.
 */

public class Content implements Parcelable {
    final static int MODE_TIME_RELATIVE = 1;
    final static int MODE_TIME_ABSOLUTE = 2;

    private String date;
    private String desc;
    private String publisher;
    private String publisherEmail;
    private Uri publisherProfileImage;
    private Uri image;
    private String location;
    private String feeling;
    private String weather;
    private long dateMillisecond;
    private int timeMode = MODE_TIME_RELATIVE;
    private ArrayList<String> likeUserList;
    private ArrayList<Reply> replyList;

    private boolean contentDetailCheck;

    public Content() {

    }


    public Content(Uri image, String desc, Uri publisherProfileImage, String publisher, String publisherEmail, long dateMillisecond, String location, String feeling, String weather, ArrayList<String> likeUserList, ArrayList<Reply> replyList) {

        this.dateMillisecond = dateMillisecond;
        this.desc = desc;
        date = new SimpleDateFormat("yyyy년 MM월 dd일 HH:mm").format(new Date(dateMillisecond));
        this.image = image;
        this.publisher = publisher;
        this.publisherEmail = publisherEmail;
        this.publisherProfileImage = publisherProfileImage;
        this.location = location;
        this.weather = weather;
        this.feeling = feeling;
        contentDetailCheck = false;
        this.likeUserList = likeUserList;
        this.replyList = replyList;
    }


    protected Content(Parcel in) {
        date = in.readString();
        desc = in.readString();
        publisher = in.readString();
        publisherEmail = in.readString();
        publisherProfileImage = in.readParcelable(Uri.class.getClassLoader());
        image = in.readParcelable(Uri.class.getClassLoader());
        location = in.readString();
        feeling = in.readString();
        weather = in.readString();
        dateMillisecond = in.readLong();
        timeMode = in.readInt();
        likeUserList = in.createStringArrayList();
        replyList = in.createTypedArrayList(Reply.CREATOR);
        contentDetailCheck = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(date);
        dest.writeString(desc);
        dest.writeString(publisher);
        dest.writeString(publisherEmail);
        dest.writeParcelable(publisherProfileImage, flags);
        dest.writeParcelable(image, flags);
        dest.writeString(location);
        dest.writeString(feeling);
        dest.writeString(weather);
        dest.writeLong(dateMillisecond);
        dest.writeInt(timeMode);
        dest.writeStringList(likeUserList);
        dest.writeTypedList(replyList);
        dest.writeByte((byte) (contentDetailCheck ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Content> CREATOR = new Creator<Content>() {
        @Override
        public Content createFromParcel(Parcel in) {
            return new Content(in);
        }

        @Override
        public Content[] newArray(int size) {
            return new Content[size];
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


    public void setTimeMode(int mode) {
        timeMode = mode;
    }

    public boolean isContentDetailCheck() {
        return contentDetailCheck;
    }

    public void setContentDetailCheck(boolean contentDetailCheck) {
        this.contentDetailCheck = contentDetailCheck;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getFeeling() {
        return feeling;
    }

    public void setFeeling(String feeling) {
        this.feeling = feeling;
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


    public Long getDateToMillisecond() {
        return dateMillisecond;
    }

    public int getTimeMode() {
        return timeMode;
    }

    public ArrayList<String> getLikeUserList() {
        return likeUserList;
    }

    public void setLikeUserList(ArrayList<String> likeUserList) {
        this.likeUserList = likeUserList;
    }

    public int getLikeCount() {
        return likeUserList.size();
    }

    public ArrayList<Reply> getReplyList() {
        return replyList;
    }

    public void setReplyList(ArrayList<Reply> replyList) {
        this.replyList = replyList;
    }

    public int getReplyCount() {
        int size = 0;
        for(Reply reply : replyList){
            size += reply.getReplyList().size();
        }

        size += replyList.size();
        return size;
    }
}
