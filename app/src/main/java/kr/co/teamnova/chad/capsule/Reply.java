package kr.co.teamnova.chad.capsule;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Chad on 2017-02-18.
 */

public class Reply implements Parcelable {
    private User user;
    private String desc;
    private long dateMilliSec;
    private ArrayList<Reply> replyList;

    public Reply(User user, String desc, long dateMilliSec) {
        this.user = user;
        this.desc = desc;
        this.dateMilliSec = dateMilliSec;
        replyList = new ArrayList<>();
    }

    protected Reply(Parcel in) {
        user = in.readParcelable(User.class.getClassLoader());
        desc = in.readString();
        dateMilliSec = in.readLong();
        replyList = in.createTypedArrayList(Reply.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(user, flags);
        dest.writeString(desc);
        dest.writeLong(dateMilliSec);
        dest.writeTypedList(replyList);
    }

    @Override
    public int describeContents() {
        return 0;
    }


    public static final Creator<Reply> CREATOR = new Creator<Reply>() {
        @Override
        public Reply createFromParcel(Parcel in) {
            return new Reply(in);
        }

        @Override
        public Reply[] newArray(int size) {
            return new Reply[size];
        }
    };

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public long getDateMilliSec() {
        return dateMilliSec;
    }

    public void setDateMilliSec(long dateMilliSec) {
        this.dateMilliSec = dateMilliSec;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public ArrayList<Reply> getReplyList() {
        return replyList;
    }

    public void setReplyList(ArrayList<Reply> replyList) {
        this.replyList = replyList;
    }
}
