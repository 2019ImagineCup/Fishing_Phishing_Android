package ensharp.imagincup2019.fishingphishing.UI.UIElements;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ensharp.imagincup2019.fishingphishing.Database.Model.HistoryItem;
import ensharp.imagincup2019.fishingphishing.UI.Fragments.RecentsFragment;
import ensharp.imagincup2019.fishingphishing.R;

public class CallHistoryInformationAdapter extends BaseSwipeAdapter {

    private Context context;
    private List<HistoryItem> recentCalls;
    private RecentsFragment fragment;
    private TextView phoneNumber;
    private TextView detail;
    private TextView time;

    public CallHistoryInformationAdapter(Context context, List<HistoryItem> recentCalls) {
        this.context = context;
        this.recentCalls = recentCalls;
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    @Override
    public View generateView(int position, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.item_recent_call, null);
    }

    public void setCustomizedFragment(RecentsFragment fragment) {
        this.fragment = fragment;
    }

    @Override
    public void fillValues(final int position, final View convertView) {
        final HistoryItem currentCall = recentCalls.get(position);

        phoneNumber = convertView.findViewById(R.id.phone_number);
        detail = convertView.findViewById(R.id.detail);
        time = convertView.findViewById(R.id.time);

        phoneNumber.setText(currentCall.number);
//        detail.setText(currentCall.getPhoneType());
        detail.setText("Phone Number");
        setDisplayedTime(currentCall);

        final SwipeLayout swipeLayout = (SwipeLayout) convertView.findViewById(getSwipeLayoutResourceId(position));
        swipeLayout.addSwipeListener(new SimpleSwipeListener());

        convertView.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                swipeLayout.close();
                fragment.setListViewAdapter(recentCalls);
                fragment.removeCallHistory(recentCalls.get(position).date);
                recentCalls.remove(position);
            }
        });
    }

    public void setDisplayedTime(HistoryItem currentCall) {
        if (currentCall.date.contains("call")) {
            time.setText(currentCall.date);
            return;
        }

        Date now = new Date();
        Date call = null;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy MM dd HH:mm:ss");
        try {
            call = formatter.parse(currentCall.date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long timeDifference = now.getTime() - call.getTime();
        long dayDifference = timeDifference / (24 * 60 * 60 * 1000);
        dayDifference = Math.abs(dayDifference);

        SimpleDateFormat format;
        if (dayDifference > 7) {
            format = new SimpleDateFormat("yyyy. MM. dd");
            time.setText(format.format(call));
        } else if (dayDifference > 1) {
            format = new SimpleDateFormat("E", new Locale("en", "US"));
            time.setText(format.format(call));
        } else if (dayDifference == 1) {
            time.setText("Yesterday");
        } else {
            format = new SimpleDateFormat("a hh:mm", new Locale("en", "US"));
            time.setText(format.format(call));
        }
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
