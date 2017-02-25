package kr.co.teamnova.chad.capsule;

/**
 * Created by Chad on 2017-02-26.
 */

public class Chat {

    private String strNickname;
    private String strMessage;
    private String time;
    private boolean isLast;

    public Chat() {
    }

    public Chat(String strNickname, String strMessage, String time, boolean isLast) {
        this.strNickname = strNickname;
        this.strMessage = strMessage;
        this.time = time;
        this.isLast = isLast;
    }

    public String getStrNickname() {
        return strNickname;
    }

    public void setStrNickname(String strNickname) {
        this.strNickname = strNickname;
    }


    public String getStrMessage() {
        return strMessage;
    }

    public void setStrMessage(String strMessage) {
        this.strMessage = strMessage;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isLast() {
        return isLast;
    }

    public void setLast(boolean last) {
        isLast = last;
    }
}
