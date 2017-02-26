package kr.co.teamnova.chad.capsule;

/**
 * Created by Chad on 2017-02-26.
 */

public class Chat {

    private String strNickname;
    private String strMessage;
    private String time;
    private boolean isTimeLine;

    public Chat(String time) {
        this.time = time;
        this.isTimeLine = true;
        strNickname="";
        strMessage="";
    }

    public Chat(String strNickname, String strMessage, String time) {
        this.strNickname = strNickname;
        this.strMessage = strMessage;
        this.time = time;

        isTimeLine = false;
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

    public boolean isTimeLine() {
        return isTimeLine;
    }

    public void setTimeLine(boolean timeLine) {
        isTimeLine = timeLine;
    }
}
