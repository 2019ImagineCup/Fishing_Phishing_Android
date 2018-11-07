package ensharp.imagincup2019.fishingphishing.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.daimajia.swipe.util.Attributes;

import java.util.ArrayList;
import java.util.List;

import ensharp.imagincup2019.fishingphishing.Constants;
import ensharp.imagincup2019.fishingphishing.R;
import ensharp.imagincup2019.fishingphishing.UIElements.CallHistoryInformationAdapter;
import ensharp.imagincup2019.fishingphishing.UIElements.NumberAdapter;

public class NumbersFragment extends Fragment {

    private Constants constants = Constants.getInstance();
    private View view;
    private ListView list;
    private NumberAdapter listViewAdapter;
    private ArrayList<String> numberList = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_numbers, container, false);

        list = view.findViewById(R.id.list);
        numberList.addAll(constants.getNumbers());
        setListViewAdapter(numberList);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        numberList.clear();
        numberList.addAll(constants.getNumbers());
        setListViewAdapter(numberList);
    }

    public void setListViewAdapter(List<String> numberList) {
        listViewAdapter = new NumberAdapter(getContext(), numberList);
        listViewAdapter.setCustomizedFragment(this);
        list.setAdapter(listViewAdapter);
        listViewAdapter.setMode(Attributes.Mode.Single);
    }
}
