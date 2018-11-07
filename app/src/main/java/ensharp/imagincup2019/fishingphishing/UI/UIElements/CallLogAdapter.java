package ensharp.imagincup2019.fishingphishing.UI.UIElements;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;

import java.util.List;

import ensharp.imagincup2019.fishingphishing.Common.Constants;
import ensharp.imagincup2019.fishingphishing.Common.VO.CallLogVO;
import ensharp.imagincup2019.fishingphishing.Common.VO.RecentCallVO;
import ensharp.imagincup2019.fishingphishing.R;
import ensharp.imagincup2019.fishingphishing.UI.Fragments.LogFragment;

public class CallLogAdapter extends BaseSwipeAdapter {

    private Context context;
    private List<CallLogVO> logList;
    private View view;
    private LogFragment fragment;
    private TextView phoneNumber;
    private TextView date;
    private TextView time;
    private TextView category;
    private TextView period;
    private ImageView play;
    private ImageView fold;

    public CallLogAdapter(Context context, List<CallLogVO> logList) {
        this.context = context;
        this.logList = logList;
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    @Override
    public View generateView(int position, ViewGroup parent) {
        view = LayoutInflater.from(context).inflate(R.layout.item_call_log, null);

        phoneNumber = view.findViewById(R.id.phone_number);
        date = view.findViewById(R.id.date);
        time = view.findViewById(R.id.time);
        category = view.findViewById(R.id.category);
        period = view.findViewById(R.id.period);
        play = view.findViewById(R.id.play);
        fold = view.findViewById(R.id.adjust);

        return view;
    }

    public void setCustomizedFragment(LogFragment fragment) {
        this.fragment = fragment;
    }

    @Override
    public void fillValues(final int position, View convertView) {
        final CallLogVO currentLog = logList.get(position);

        phoneNumber.setText(currentLog.getPhoneNumber());
        date.setText(currentLog.getDate());
        time.setText(currentLog.getTime());
        category.setText(currentLog.getCategory());
        period.setText(currentLog.getPeriod());

        if (currentLog.getCategory().equals("부재중 전화")) {
            play.setVisibility(View.INVISIBLE);
            fold.setVisibility(View.INVISIBLE);
        }

        final SwipeLayout swipeLayout = (SwipeLayout) view.findViewById(getSwipeLayoutResourceId(position));
        swipeLayout.addSwipeListener(new SimpleSwipeListener());

        view.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                swipeLayout.close();
                Constants constants = Constants.getInstance();
                constants.deleteLog(position);
                logList.remove(position);
                fragment.setListViewAdapter(logList);
            }
        });
    }

    @Override
    public int getCount() {
        return logList.size();
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
