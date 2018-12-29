package ensharp.imagincup2019.fishingphishing.UI.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
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
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ensharp.imagincup2019.fishingphishing.Database.DatabaseManager;
import ensharp.imagincup2019.fishingphishing.Database.Model.CallHistory;
import ensharp.imagincup2019.fishingphishing.Database.Model.HistoryItem;
import ensharp.imagincup2019.fishingphishing.R;
import ensharp.imagincup2019.fishingphishing.UI.UIElements.CallHistoryInformationAdapter;
import ensharp.imagincup2019.fishingphishing.UI.UIElements.ViewFindUtils;

public class RecentsFragment extends Fragment {

    private View view;

    // Toggle tab
    private SegmentTabLayout tabLayout;
    private String[] titles = {"All", "By numbers"};

    // List
    @BindView(R.id.list)
    ListView list;

    private CallHistoryInformationAdapter listViewAdapter;
    private List<HistoryItem> historyList = new ArrayList<>();

    /**
     * Initialize view, toggle tab
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return view
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_recents, container, false);
        ButterKnife.bind(this, view);

        // Initialize toggle tab
        tabLayout = ViewFindUtils.find(view, R.id.toggle_tab);
        tabLayout.setTabData(titles);
        tabLayout.setOnTabSelectListener(onTabSelectListener);

        loadData();

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
                getAllCallHistories();
                break;
            case 1:
                getCallHistoriesByNumber();
                break;
        }
        setListViewAdapter(historyList);
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
        historyList.clear();
        historyList.addAll(DatabaseManager.getItems());
    }

    public void getCallHistoriesByNumber() {
        historyList.clear();
        historyList.addAll(DatabaseManager.getItemsByNumber());
    }

    public void setListViewAdapter(List<HistoryItem> callList) {
        listViewAdapter = new CallHistoryInformationAdapter(getContext(), callList);
        listViewAdapter.setCustomizedFragment(this);
        list.setAdapter(listViewAdapter);
        listViewAdapter.setMode(Attributes.Mode.Single);
    }

    public void loadData() {
        historyList = DatabaseManager.getItems();
        setListViewAdapter(historyList);
    }

    public void reloadItem() {
        loadData();
        list.invalidate();
    }

    public void addCallHistory(HistoryItem item) {
        CallHistory history = DatabaseManager.getHistoryByNumber(item.number);
        if (history == null) {
            CallHistory newHistory = new CallHistory();
            newHistory.number = item.number;
            newHistory.save();

            item.callHistory = newHistory;
        } else {
            item.callHistory = history;
        }

        item.save();
        reloadItem();
    }

    public void removeCallHistory(String callTime) {
        HistoryItem itemToDelete = DatabaseManager.getItemByDate(callTime);
        itemToDelete.delete();
        reloadItem();
    }
}
