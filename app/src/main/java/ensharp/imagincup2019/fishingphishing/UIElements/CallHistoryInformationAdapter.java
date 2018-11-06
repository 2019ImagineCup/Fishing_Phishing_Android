package ensharp.imagincup2019.fishingphishing.UIElements;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ensharp.imagincup2019.fishingphishing.R;
import ensharp.imagincup2019.fishingphishing.RecentCallVO;

public class CallHistoryInformationAdapter extends ArrayAdapter<RecentCallVO> {

    private Context context;
    private List<RecentCallVO> callList = new ArrayList<>();

    public CallHistoryInformationAdapter(@NonNull Context context, ArrayList<RecentCallVO> list) {
        super(context, 0, list);
        this.context = context;
        callList = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItem = convertView;
        if (listItem == null)
            listItem = LayoutInflater.from(context).inflate(R.layout.item_recent_call, parent, false);

        RecentCallVO recentCall = callList.get(position);

        TextView phoneNumber = listItem.findViewById(R.id.phone_number);
        TextView detail = (TextView) listItem.findViewById(R.id.detail);
        TextView time = (TextView) listItem.findViewById(R.id.time);

        phoneNumber.setText(recentCall.getPhoneNumber());
        detail.setText(recentCall.getDetail());
        time.setText(recentCall.getTime());

        return listItem;
    }
}
