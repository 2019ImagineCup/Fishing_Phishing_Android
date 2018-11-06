package ensharp.imagincup2019.fishingphishing;

public class RecentCallVO {

    private String phoneNumber;
    private String detail;
    private String time;

    public RecentCallVO(String phoneNumber, String detail, String time) {
        this.phoneNumber = phoneNumber;
        this.detail = detail;
        this.time = time;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getDetail() {
        return detail;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
