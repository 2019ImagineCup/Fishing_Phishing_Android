package ensharp.imagincup2019.fishingphishing.Fragments;

import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.util.Attributes;
import com.flyco.tablayout.SegmentTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.LongToDoubleFunction;

import ensharp.imagincup2019.fishingphishing.Constants;
import ensharp.imagincup2019.fishingphishing.R;
import ensharp.imagincup2019.fishingphishing.RecentCallVO;
import ensharp.imagincup2019.fishingphishing.UIElements.CallHistoryInformationAdapter;
import ensharp.imagincup2019.fishingphishing.UIElements.ViewFindUtils;

public class RecentsFragment extends Fragment {

    private Constants constants = Constants.getInstance();
    private View view;
    private String[] titles = {"모두", "번호별로"};
    private SegmentTabLayout tabLayout;
    private ListView list;
    private CallHistoryInformationAdapter listViewAdapter;
    private ArrayList<RecentCallVO> historyList = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_recents, container, false);

        tabLayout = ViewFindUtils.find(view, R.id.toggle_tab);
        tabLayout.setTabData(titles);
        tabLayout.setOnTabSelectListener(onTabSelectListener);
        list = view.findViewById(R.id.list);

        historyList.addAll(constants.getRecentCalls());

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
                historyList.addAll(constants.getRecentCalls());
                break;
            case 1:
                ArrayList<RecentCallVO> listToCopy = new ArrayList<>();
                listToCopy.addAll(constants.getRecentCalls());
                historyList.clear();
                historyList.addAll(organizeHistoryList(0, listToCopy));
                break;
        }

        setListViewAdapter(historyList);
    }

    private ArrayList<RecentCallVO> organizeHistoryList(int round, ArrayList<RecentCallVO> callList) {
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

        callList.set(round, new RecentCallVO(callList.get(round).getPhoneNumber(), callList.get(round).getDetail(), String.valueOf(countInString)));

        for (int i = 0; i < indexList.size(); i++) {
            callList.remove(Integer.parseInt(String.valueOf(indexList.get(i))));
        }

        round++;
        return organizeHistoryList(round, callList);
    }

    public void setListViewAdapter(List<RecentCallVO> callList) {
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
}
