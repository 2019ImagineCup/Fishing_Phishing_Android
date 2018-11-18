package ensharp.imagincup2019.fishingphishing.Common.Model;

import java.util.Date;

import io.realm.RealmObject;

public class CurrentCallDate extends RealmObject {

    private Date date;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
