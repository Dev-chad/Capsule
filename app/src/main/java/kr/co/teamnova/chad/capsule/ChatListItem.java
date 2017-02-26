package kr.co.teamnova.chad.capsule;

import android.net.Uri;

/**
 * Created by Chad on 2017-02-26.
 */

public class ChatListItem {

    private String nickName;
    private String email;
    private Uri uriProfileImage;
    private String lastChat;
    private long date;

    public ChatListItem(String nickName, Uri uriProfileImage, String lastChat, String email, long date) {
        this.nickName = nickName;
        this.uriProfileImage = uriProfileImage;
        this.lastChat = lastChat;
        this.date = date;
        this.email = email;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Uri getUriProfileImage() {
        return uriProfileImage;
    }

    public void setUriProfileImage(Uri uriProfileImage) {
        this.uriProfileImage = uriProfileImage;
    }

    public String getLastChat() {
        return lastChat;
    }

    public void setLastChat(String lastChat) {
        this.lastChat = lastChat;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
