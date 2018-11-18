package ensharp.imagincup2019.fishingphishing.Common.Model;

import java.util.Date;

import io.realm.RealmObject;

public class CallHistory extends RealmObject {

    private String phoneNumber;
    private String callType;    // 수신/발신/부재중 등
    private String phoneType;   // 휴대전화/지역/국가
    private Date date;
    private int duration;
    private String count;

    public CallHistory() {

    }

    public CallHistory(String phoneNumber, String callType, String phoneType, Date date, int duration) {
        this.phoneNumber = phoneNumber;
        this.callType = callType;
        this.phoneType = phoneType;
        this.date = date;
        this.duration = duration;
    }

    public CallHistory(String phoneNumber, String phoneType, String count) {
        this.phoneNumber = phoneNumber;
        this.phoneType = phoneType;
        this.count = count;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCallType() {
        return callType;
    }

    public void setCallType(String callType) {
        this.callType = callType;
    }

    public String getPhoneType() {
        return phoneType;
    }

    public void setPhoneType(String phoneType) {
        this.phoneType = phoneType;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }
}
