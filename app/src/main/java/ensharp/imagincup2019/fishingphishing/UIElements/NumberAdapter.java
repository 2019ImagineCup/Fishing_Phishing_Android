package ensharp.imagincup2019.fishingphishing.UIElements;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;

import java.util.List;

import ensharp.imagincup2019.fishingphishing.Constants;
import ensharp.imagincup2019.fishingphishing.Fragments.NumbersFragment;
import ensharp.imagincup2019.fishingphishing.Fragments.RecentsFragment;
import ensharp.imagincup2019.fishingphishing.R;

public class NumberAdapter extends BaseSwipeAdapter {

    private Context context;
    private List<String> numbers;
    private View view;
    private NumbersFragment fragment;
    private TextView phoneNumber;

    public NumberAdapter(Context context, List<String> numbers) {
        this.context = context;
        this.numbers = numbers;
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    @Override
    public View generateView(int position, ViewGroup parent) {
        view = LayoutInflater.from(context).inflate(R.layout.item_phone_number, null);

        phoneNumber = view.findViewById(R.id.phone_number);

        return view;
    }

    public void setCustomizedFragment(NumbersFragment fragment) {
        this.fragment = fragment;
    }

    @Override
    public void fillValues(final int position, View convertView) {
        String currentNumber = numbers.get(position);

        phoneNumber.setText(currentNumber);

        final SwipeLayout swipeLayout = view.findViewById(getSwipeLayoutResourceId(position));
        swipeLayout.addSwipeListener(new SimpleSwipeListener());

        view.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                swipeLayout.close();
                Constants constants = Constants.getInstance();
                constants.deleteNumber(position);
                numbers.remove(position);
                fragment.setListViewAdapter(numbers);
            }
        });
    }

    @Override
    public int getCount() {
        return numbers.size();
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
