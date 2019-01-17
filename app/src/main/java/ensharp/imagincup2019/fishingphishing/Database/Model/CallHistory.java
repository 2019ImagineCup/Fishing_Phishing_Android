package ensharp.imagincup2019.fishingphishing.Database.Model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.List;

@Table(name = "Histories")
public class CallHistory extends Model {
    @Column(name = "Number")
    public String number;

    public List<HistoryItem> historyItems() {
        return getMany(HistoryItem.class, "CallHistory");
    }

    public CallHistory() {
        super();
    }
}
