package kr.co.teamnova.chad.capsule;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Chad on 2017-01-19.
 */

public class User implements Parcelable{

    private String email;
    private String nickname;
    private String phone;
    private Uri uriProfileImage;

    public User(){

    }

    public User(String email, String nickname, String phone, Uri uriProfileImage) {
        this.email = email;
        this.nickname = nickname;
        this.phone = phone;
        this.uriProfileImage = uriProfileImage;
    }

    protected User(Parcel in) {
        email = in.readString();
        nickname = in.readString();
        phone = in.readString();
        uriProfileImage = in.readParcelable(Uri.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(email);
        dest.writeString(nickname);
        dest.writeString(phone);
        dest.writeParcelable(uriProfileImage, flags);
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

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Uri getUriProfileImage() {
        return uriProfileImage;
    }

    public void setUriProfileImage(Uri uriProfileImage) {
        this.uriProfileImage = uriProfileImage;
    }
}
