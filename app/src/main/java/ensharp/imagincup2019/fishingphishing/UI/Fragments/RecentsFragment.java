package ensharp.imagincup2019.fishingphishing.UI.Fragments;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.CallLog;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.telecom.Call;
import android.util.Log;
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

import ensharp.imagincup2019.fishingphishing.Common.Model.CallHistory;
import ensharp.imagincup2019.fishingphishing.Common.Module.CallHistoryModule;
import ensharp.imagincup2019.fishingphishing.Call.ContactsManager;
import ensharp.imagincup2019.fishingphishing.Common.Model.CurrentCallDate;
import ensharp.imagincup2019.fishingphishing.R;
import ensharp.imagincup2019.fishingphishing.UI.UIElements.CallHistoryInformationAdapter;
import ensharp.imagincup2019.fishingphishing.UI.UIElements.ViewFindUtils;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class RecentsFragment extends Fragment {

    private String TAG = "RecentsFragment";

    private View view;
    private TextView title;
    private String[] titles = {"모두", "번호별로"};
    private SegmentTabLayout tabLayout;
    private ListView list;
    private CallHistoryInformationAdapter listViewAdapter;
    private ArrayList<CallHistory> historyList = new ArrayList<>();

    private RealmConfiguration callHistoryConfig;
    private Realm historyRealm;

    private boolean isInitialized = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Realm.init(getContext());
        callHistoryConfig = new RealmConfiguration.Builder()
                .name("history.realm")
                .modules(new CallHistoryModule())
                .build();
        historyRealm = Realm.getInstance(callHistoryConfig);
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

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        setHistoryList(tabLayout.getCurrentTab());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

//        historyRealm.close();
    }

    private void setHistoryList(int position) {
        switch (position) {
            case 0:
                getAllCallHistories();
                break;
            case 1:
                historyList.clear();
                historyList.addAll(organizeHistoryList(0, historyList));
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

    public void getAllCallHistories() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("data", 0);

        if (isInitialized)
            return;

        RealmResults<CallHistory> result = historyRealm.where(CallHistory.class).findAll();
        ArrayList<CallHistory> list = new ArrayList<>();
        list.addAll(historyRealm.copyFromRealm(result));

        historyList.clear();
        historyList.addAll(list);

        isInitialized = true;
//        SharedPreferences.Editor editor = getActivity().getSharedPreferences("data", 0).edit();
//        editor.putBoolean("isInitialized", true);
//        editor.apply();
    }

    public void removeCallHistory(Date callTime) {
        try {
            historyRealm.beginTransaction();
            historyRealm.where(CallHistory.class).equalTo("date", callTime)
                    .findFirst()
                    .deleteFromRealm();
            historyRealm.commitTransaction();
        } catch (Exception e) {
            if (historyRealm.isInTransaction()) {
                historyRealm.cancelTransaction();
            }
            throw new RuntimeException(e);
        }
    }

    public void addCallHistory(CallHistory callHistory) {
        try {
            historyRealm.beginTransaction();
            CallHistory newHistory = historyRealm.createObject(CallHistory.class);
            newHistory.setPhoneNumber(callHistory.getPhoneNumber());
            newHistory.setCallType(callHistory.getCallType());
            newHistory.setDate(callHistory.getDate());
            newHistory.setDuration(callHistory.getDuration());
            historyRealm.commitTransaction();
            historyList.add(callHistory);
        } catch (Exception e) {
            if (historyRealm.isInTransaction()) {
                historyRealm.cancelTransaction();
            }
            throw new RuntimeException(e);
        }
    }
}
