package ensharp.imagincup2019.fishingphishing.UI.Fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.daimajia.swipe.util.Attributes;
import com.flyco.tablayout.SegmentTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ensharp.imagincup2019.fishingphishing.Call.CallHistory;
import ensharp.imagincup2019.fishingphishing.Call.CallHistoryModule;
import ensharp.imagincup2019.fishingphishing.Call.ContactsManager;
import ensharp.imagincup2019.fishingphishing.Call.CurrentCallDate;
import ensharp.imagincup2019.fishingphishing.Common.Constants;
import ensharp.imagincup2019.fishingphishing.R;
import ensharp.imagincup2019.fishingphishing.UI.UIElements.CallHistoryInformationAdapter;
import ensharp.imagincup2019.fishingphishing.UI.UIElements.ViewFindUtils;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class RecentsFragment extends Fragment {

    private View view;
    private TextView title;
    private String[] titles = {"모두", "번호별로"};
    private SegmentTabLayout tabLayout;
    private ListView list;
    private CallHistoryInformationAdapter listViewAdapter;
    private ArrayList<CallHistory> historyList = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_recents, container, false);
        title = view.findViewById(R.id.title);
        title.setText(R.string.recents_fragment_name);
        tabLayout = ViewFindUtils.find(view, R.id.toggle_tab);
        tabLayout.setTabData(titles);
        tabLayout.setOnTabSelectListener(onTabSelectListener);
        list = view.findViewById(R.id.list);

        Realm.init(getContext());

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        setHistoryList(tabLayout.getCurrentTab());
    }

    private void setHistoryList(int position) {
        switch (position) {
            case 0:
                historyList.clear();
                historyList.addAll(getAllCallHistories());
                break;
            case 1:
                historyList.clear();
                historyList.addAll(organizeHistoryList(0, getAllCallHistories()));
                break;
        }

        setListViewAdapter(historyList);
    }

    private ArrayList<CallHistory> organizeHistoryList(int round, ArrayList<CallHistory> callList) {
        if (round == callList.size()) return callList;

        int count = 1;
        ArrayList<Integer> indexList = new ArrayList<>();
        for (int i = callList.size() - 1; i > round; i--) {
            if (callList.get(i).getPhoneNumber().equals(callList.get(round).getPhoneNumber())) {
                count++;
                indexList.add(i);
            }
        }

        String countInString;
        if (count != 1) countInString = String.valueOf(count) + " Calls";
        else countInString = String.valueOf(count) + " Call";

        callList.set(round, new CallHistory(callList.get(round).getPhoneNumber(), callList.get(round).getPhoneType(), String.valueOf(countInString)));

        for (int i = 0; i < indexList.size(); i++) {
            callList.remove(Integer.parseInt(String.valueOf(indexList.get(i))));
        }

        round++;
        return organizeHistoryList(round, callList);
    }

    public void setListViewAdapter(List<CallHistory> callList) {
        listViewAdapter = new CallHistoryInformationAdapter(getContext(), callList);
        listViewAdapter.setCustomizedFragment(this);
        list.setAdapter(listViewAdapter);
        listViewAdapter.setMode(Attributes.Mode.Single);
    }

    private OnTabSelectListener onTabSelectListener = new OnTabSelectListener() {
        @Override
        public void onTabSelect(int position) {
            setHistoryList(position);
        }

        @Override
        public void onTabReselect(int position) {

        }
    };

    public ArrayList<CallHistory> getAllCallHistories() {

        Cursor managedCursor = getContext().getContentResolver().query(CallLog.Calls.CONTENT_URI, null, null, null, null);
        int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
        int type = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
        int date = managedCursor.getColumnIndex(CallLog.Calls.DATE);
        int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);

        RealmConfiguration callHistoryConfig = new RealmConfiguration.Builder()
                .name("history.realm")
                .modules(new CallHistoryModule())
                .build();

        Realm historyRealm = Realm.getInstance(callHistoryConfig);

        final Date currentSynchronizedTime;
        if (historyRealm.where(CurrentCallDate.class).count() == 0){
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.YEAR, 2000);
            cal.set(Calendar.MONTH, Calendar.JANUARY);
            cal.set(Calendar.DAY_OF_MONTH, 0);
            cal.set(Calendar.HOUR, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            currentSynchronizedTime = cal.getTime();
            historyRealm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    CurrentCallDate currentCallDate = realm.createObject(CurrentCallDate.class);
                    currentCallDate.setDate(currentSynchronizedTime);
                }
            });
        } else {
            currentSynchronizedTime = historyRealm.where(CurrentCallDate.class).findFirst().getDate();
        }

        ContactsManager contactsManager= new ContactsManager();
        contactsManager.setContext(getContext());
        contactsManager.setContactList();
        historyRealm.beginTransaction();
        while (managedCursor.moveToNext()) {
            if (currentSynchronizedTime.after(new Date(Long.valueOf(managedCursor.getString(date))))) {
                continue;
            }

            if (contactsManager.isExist(managedCursor.getString(number))) {
                continue;
            }

            String phoneNumber = managedCursor.getString(number);
            String callType = managedCursor.getString(type);
            String callDate = managedCursor.getString(date);
            Date callDayTime = new Date(Long.valueOf(callDate));
            String dir = null;

            int dircode = Integer.parseInt(callType);
            switch (dircode) {
                case CallLog.Calls.OUTGOING_TYPE:   // 발신
                    dir = "OUTGOING";
                    break;
                case CallLog.Calls.INCOMING_TYPE:   // 수신
                    dir = "INCOMING";
                    break;
                case CallLog.Calls.MISSED_TYPE:     // 부재중
                    dir = "MISSED";
                    break;
            }

            CallHistory newHistory = historyRealm.createObject(CallHistory.class);
            newHistory.setPhoneNumber(phoneNumber);
            newHistory.setCallType(dir);
            newHistory.setDate(callDayTime);
            newHistory.setDuration(duration);
        }

        RealmResults<CallHistory> result = historyRealm.where(CallHistory.class).findAll();
        ArrayList<CallHistory> list = new ArrayList<>();
        list.addAll(historyRealm.copyFromRealm(result));

        historyRealm.where(CurrentCallDate.class).findFirst().setDate(Calendar.getInstance().getTime());

        historyRealm.commitTransaction();
        historyRealm.close();
        managedCursor.close();

        return list;
    }

    public void removeCallHistory(Date callTime) {

        RealmConfiguration callHistoryConfig = new RealmConfiguration.Builder()
                .name("history.realm")
                .modules(new CallHistoryModule())
                .build();

        Realm historyRealm = Realm.getInstance(callHistoryConfig);
        historyRealm.beginTransaction();

        historyRealm.where(CallHistory.class).equalTo("date", callTime)
                .findFirst()
                .deleteFromRealm();

        historyRealm.commitTransaction();
        historyRealm.close();
    }
}
