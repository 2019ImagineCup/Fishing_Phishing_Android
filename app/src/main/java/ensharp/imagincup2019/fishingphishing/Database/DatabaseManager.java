package ensharp.imagincup2019.fishingphishing.Database;

import android.util.Log;

import com.activeandroid.query.Select;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ensharp.imagincup2019.fishingphishing.Database.Model.CallHistory;
import ensharp.imagincup2019.fishingphishing.Database.Model.HistoryItem;

public class DatabaseManager {

    public static List<CallHistory> getHistories() {
        return new Select().from(CallHistory.class).execute();
    }

    public static List<HistoryItem> getItems() {
        return new Select().from(HistoryItem.class).orderBy("date DESC").execute();
    }

    public static List<HistoryItem> getItemsByNumber() {
        List<CallHistory> histories = getHistories();
        List<HistoryItem> items = new ArrayList<>();

        for (int i = histories.size() - 1; i >= 0; i--) {
            HistoryItem newItem;
            int size = histories.get(i).historyItems().size();

            newItem = histories.get(i).historyItems().get(0);
            if (size == 1) {
                newItem.date = size + "call";
            } else {
                newItem.date = size + "calls";
            }

            items.add(newItem);
        }

        return items;
    }

    public static CallHistory getHistoryByNumber(String number) {
        CallHistory history = new Select().from(CallHistory.class).where("Number = ?", number).executeSingle();

        return history;
    }

    public static HistoryItem getItemByDate(String date) {
        HistoryItem item = new Select().from(HistoryItem.class).where("Date = ?", date).executeSingle();

        return item;
    }
}
