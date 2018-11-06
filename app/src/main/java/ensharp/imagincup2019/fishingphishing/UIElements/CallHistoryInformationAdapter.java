package ensharp.imagincup2019.fishingphishing.UIElements;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;

import java.util.List;

import ensharp.imagincup2019.fishingphishing.Constants;
import ensharp.imagincup2019.fishingphishing.Fragments.RecentsFragment;
import ensharp.imagincup2019.fishingphishing.R;
import ensharp.imagincup2019.fishingphishing.RecentCallVO;

public class CallHistoryInformationAdapter extends BaseSwipeAdapter {

    private Context context;
    private List<RecentCallVO> recentCalls;
    private View view;
    private RecentsFragment fragment;
    private TextView phoneNumber;
    private TextView detail;
    private TextView time;

    public CallHistoryInformationAdapter(Context context, List<RecentCallVO> recentCalls) {
        this.context = context;
        this.recentCalls = recentCalls;
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    @Override
    public View generateView(int position, ViewGroup parent) {
        view = LayoutInflater.from(context).inflate(R.layout.item_recent_call, null);

        phoneNumber = view.findViewById(R.id.phone_number);
        detail = view.findViewById(R.id.detail);
        time = view.findViewById(R.id.time);

        return view;
    }

    public void setCustomizedFragment(RecentsFragment fragment) {
        this.fragment = fragment;
    }

    @Override
    public void fillValues(final int position, View convertView) {
        RecentCallVO currentCall = recentCalls.get(position);

        phoneNumber.setText(currentCall.getPhoneNumber());
        detail.setText(currentCall.getDetail());
        time.setText(currentCall.getTime());

        final SwipeLayout swipeLayout = (SwipeLayout) view.findViewById(getSwipeLayoutResourceId(position));
        swipeLayout.addSwipeListener(new SimpleSwipeListener());

        view.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                swipeLayout.close();
                Constants constants = Constants.getInstance();
                constants.deleteRecentCall(position);
                recentCalls.remove(position);
                fragment.setListViewAdapter(recentCalls);
            }
        });
    }

    @Override
    public int getCount() {
        return recentCalls.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
