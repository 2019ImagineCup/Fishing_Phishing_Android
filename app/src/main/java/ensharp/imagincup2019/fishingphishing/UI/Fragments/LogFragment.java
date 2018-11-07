package ensharp.imagincup2019.fishingphishing.UI.Fragments;

import android.os.Bundle;
import android.provider.CallLog;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.telecom.Call;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.daimajia.swipe.util.Attributes;
import com.flyco.tablayout.SegmentTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ensharp.imagincup2019.fishingphishing.Common.Constants;
import ensharp.imagincup2019.fishingphishing.R;
import ensharp.imagincup2019.fishingphishing.Common.VO.CallLogVO;
import ensharp.imagincup2019.fishingphishing.Common.VO.RecentCallVO;
import ensharp.imagincup2019.fishingphishing.UI.UIElements.CallLogAdapter;
import ensharp.imagincup2019.fishingphishing.UI.UIElements.NumberAdapter;
import ensharp.imagincup2019.fishingphishing.UI.UIElements.ViewFindUtils;

public class LogFragment extends Fragment {

    private Constants constants = Constants.getInstance();
    private View view;
    private String[] titles = {"모두", "번호별로"};
    private SegmentTabLayout tabLayout;
    private ListView list;
    private CallLogAdapter listViewAdapter;
    private ArrayList<CallLogVO> logList = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_log, container, false);
        tabLayout = ViewFindUtils.find(view, R.id.toggle_tab);
        tabLayout.setTabData(titles);
        tabLayout.setOnTabSelectListener(onTabSelectListener);
        list = view.findViewById(R.id.list);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        setLogList(tabLayout.getCurrentTab());
    }

    private void setLogList(int position) {
        switch (position) {
            case 0:
                logList.clear();
                logList.addAll(constants.getLogs());
                break;
            case 1:
                ArrayList<CallLogVO> listToCopy = new ArrayList<>();
                listToCopy.addAll(constants.getLogs());
                logList.clear();
                logList.addAll(organizeLogList(0, listToCopy));
                break;
        }

        setListViewAdapter(logList);
    }

    private ArrayList<CallLogVO> organizeLogList(int round, ArrayList<CallLogVO> logList) {
        if (round == logList.size()) return logList;

        int count = 1;
        ArrayList<Integer> indexList = new ArrayList<>();
        ArrayList<CallLogVO> listToCopy = new ArrayList<>();
        for (int i = logList.size() - 1; i > round; i--) {
            if (logList.get(i).getPhoneNumber().equals(logList.get(round).getPhoneNumber())) {
                count++;
                indexList.add(i);
                listToCopy.add(new CallLogVO(logList.get(i)));
            }
        }

        for (int i = 0; i < indexList.size(); i++) {
            logList.remove(Integer.parseInt(String.valueOf(indexList.get(i))));
        }

        for (int i = listToCopy.size() - 1, j = round + 1; i >= 0; i--, j++) {
            logList.add(j, listToCopy.get(i));
        }

        round += count;
        return organizeLogList(round, logList);
    }

    public void setListViewAdapter(List<CallLogVO> logList) {
        listViewAdapter = new CallLogAdapter(getContext(), logList);
        listViewAdapter.setCustomizedFragment(this);
        list.setAdapter(listViewAdapter);
        listViewAdapter.setMode(Attributes.Mode.Single);
    }

    private OnTabSelectListener onTabSelectListener = new OnTabSelectListener() {
        @Override
        public void onTabSelect(int position) {
            setLogList(position);
        }

        @Override
        public void onTabReselect(int position) {

        }
    };
}
