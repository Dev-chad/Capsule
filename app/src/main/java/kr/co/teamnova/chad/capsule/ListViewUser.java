package kr.co.teamnova.chad.capsule;

import android.net.Uri;

/**
 * Created by Chad on 2017-02-02.
 */

public class ListViewUser {
    private String nickname;
    private String email;
    private Uri uriProfileImage;
    boolean isFollow = false;

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setProfileImageUri(Uri uri) {
        uriProfileImage = uri;
    }

    public void setFollow(){
        isFollow = !isFollow;
    }

    public void setFollow(boolean isFollow){
        this.isFollow = isFollow;
    }

    public String getNickname() {
        return nickname;
    }

    public String getEmail(){
        return email;
    }

    public Uri getProfileImageUri(){
        return uriProfileImage;
    }

    public boolean isFollow(){
        return isFollow;
    }
}
