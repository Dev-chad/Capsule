package kr.co.teamnova.chad.capsule;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Chad on 2017-01-19.
 */

public class User implements Parcelable {
    private static final String TAG = "User";

    private String email;
    private String name;
    private String nickname;
    private Uri uriProfileImage;
    private String phone;
    private int numOfContent;
    private ArrayList<String> followList;
    private ArrayList<String> followerList;
    private ArrayList<String> likeContentList;

    public User() {

    }

    public User(String email, String[] strUserData) {
        this.email = email;
        this.name = strUserData[Const.INDEX_NAME];
        this.nickname = strUserData[Const.INDEX_NICKNAME];
        this.uriProfileImage = Uri.parse(strUserData[Const.INDEX_PROFILE_IMAGE]);
        this.phone = strUserData[Const.INDEX_PHONE];
        this.numOfContent = Integer.valueOf(strUserData[Const.INDEX_NUM_OF_CONTENT]);
        followerList = new ArrayList<>();
        followList = new ArrayList<>();
        likeContentList = new ArrayList<>();
        if (!strUserData[Const.INDEX_FOLLOW].equals(" ")) {
            Collections.addAll(followList, strUserData[Const.INDEX_FOLLOW].split("::"));
        }

        if (!strUserData[Const.INDEX_FOLLOWER].equals(" ")) {
            Collections.addAll(followerList, strUserData[Const.INDEX_FOLLOWER].split("::"));
        }

        if (!strUserData[Const.INDEX_LIKE_CONTENT].equals(" ")) {
            Collections.addAll(likeContentList, strUserData[Const.INDEX_LIKE_CONTENT].split("::"));
        }
    }

    protected User(Parcel in) {
        email = in.readString();
        name = in.readString();
        nickname = in.readString();
        uriProfileImage = in.readParcelable(Uri.class.getClassLoader());
        phone = in.readString();
        numOfContent = in.readInt();
        followList = in.createStringArrayList();
        followerList = in.createStringArrayList();
        likeContentList = in.createStringArrayList();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(email);
        dest.writeString(name);
        dest.writeString(nickname);
        dest.writeParcelable(uriProfileImage, flags);
        dest.writeString(phone);
        dest.writeInt(numOfContent);
        dest.writeStringList(followList);
        dest.writeStringList(followerList);
        dest.writeStringList(likeContentList);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Uri getUriProfileImage() {
        return uriProfileImage;
    }

    public void setUriProfileImage(Uri uriProfileImage) {
        this.uriProfileImage = uriProfileImage;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getNumOfContent() {
        return numOfContent;
    }

    public void setNumOfContent(int numOfContent) {
        this.numOfContent = numOfContent;
    }

    public ArrayList<String> getFollowList() {
        return followList;
    }

    public void setFollowList(ArrayList<String> followList) {
        this.followList = followList;
    }

    public ArrayList<String> getFollowerList() {
        return followerList;
    }

    public void setFollowerList(ArrayList<String> followerList) {
        this.followerList = followerList;
    }

    public ArrayList<String> getLikeContentList() {
        return likeContentList;
    }

    public void setLikeContentList(ArrayList<String> likeContentList) {
        this.likeContentList = likeContentList;
    }
}
