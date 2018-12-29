package ensharp.imagincup2019.fishingphishing.Database.Model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.List;

@Table(name = "HistoryItem")
public class HistoryItem extends Model {
    @Column(name = "Number")
    public String number;
    @Column(name = "CallType")
    public String callType;
    @Column(name = "PhoneType")
    public String phoneType;
    @Column(name = "Date")
    public String date;
    @Column(name = "Duration")
    public long duration;
    @Column(name = "CallHistory")
    public CallHistory callHistory;

    public HistoryItem() {
        super();
    }
}
