package ensharp.imagincup2019.fishingphishing.Fragments;

import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.flyco.tablayout.SegmentTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;

import java.util.ArrayList;
import java.util.Arrays;

import ensharp.imagincup2019.fishingphishing.R;
import ensharp.imagincup2019.fishingphishing.RecentCallVO;
import ensharp.imagincup2019.fishingphishing.UIElements.CallHistoryInformationAdapter;
import ensharp.imagincup2019.fishingphishing.UIElements.ViewFindUtils;

public class RecentsFragment extends Fragment {

    private View view;
    private TextView title;
    private String[] titles = {"모두", "번호별로"};
    private SegmentTabLayout tabLayout;
    private ListView list;
    ArrayList<RecentCallVO> historyList;
    CallHistoryInformationAdapter adapter;

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
        title = view.findViewById(R.id.title);
        title.setPaintFlags(Paint.FAKE_BOLD_TEXT_FLAG);
        list = view.findViewById(R.id.list);

        historyList = new ArrayList<>(
                Arrays.asList(new RecentCallVO[] {
                        new RecentCallVO("En# 15기 남민수", "휴대전화", "오후 9:29"),
                        new RecentCallVO("서성범 군대", "휴대전화", "오후 9:11"),
                        new RecentCallVO("En# 16기 김태석", "휴대전화", "오후 7:21"),
                        new RecentCallVO("En# 15기 남민수", "휴대전화", "오후 7:21")
                })
        );

        adapter = new CallHistoryInformationAdapter(getContext(), historyList);
        list.setAdapter(adapter);
        list.setDivider(null);
        list.setDividerHeight(0);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        setHistoryList(tabLayout.getCurrentTab());
    }

    private void setHistoryList(int position) {
        historyList.clear();
        switch (position) {
            case 0:
                historyList = new ArrayList<>(
                        Arrays.asList(new RecentCallVO[] {
                                new RecentCallVO("En# 15기 남민수", "휴대전화", "오후 9:29"),
                                new RecentCallVO("서성범 군대", "휴대전화", "오후 9:11"),
                                new RecentCallVO("En# 16기 김태석", "휴대전화", "오후 7:21"),
                                new RecentCallVO("En# 15기 남민수", "휴대전화", "오후 7:21")
                        })
                );
                break;
            case 1:
                historyList = new ArrayList<>(
                        Arrays.asList(new RecentCallVO[] {
                                new RecentCallVO("En# 15기 남민수", "휴대전화", "2 Calls"),
                                new RecentCallVO("서성범 군대", "휴대전화", "1 Call"),
                                new RecentCallVO("En# 16기 김태석", "휴대전화", "1 Call")
                        })
                );
                break;
        }

        adapter = new CallHistoryInformationAdapter(getContext(), historyList);
        list.setAdapter(adapter);
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
