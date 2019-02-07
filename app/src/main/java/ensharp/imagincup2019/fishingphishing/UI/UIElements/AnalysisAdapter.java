package ensharp.imagincup2019.fishingphishing.UI.UIElements;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.github.aakira.expandablelayout.ExpandableLayout;
import com.github.aakira.expandablelayout.ExpandableLayoutListenerAdapter;
import com.github.aakira.expandablelayout.ExpandableLinearLayout;
import com.github.aakira.expandablelayout.Utils;
import com.github.mikephil.charting.charts.LineChart;

import java.util.List;

import ensharp.imagincup2019.fishingphishing.Common.Constants;
import ensharp.imagincup2019.fishingphishing.Common.VO.CallLogVO;
import ensharp.imagincup2019.fishingphishing.R;
import ensharp.imagincup2019.fishingphishing.UI.Fragments.LogFragment;
import ensharp.imagincup2019.fishingphishing.UI.Graph.LineGraph;

public class AnalysisAdapter extends RecyclerView.Adapter<AnalysisAdapter.ViewHolder> {

    private final List<CallLogVO> logList;
    private Context context;
    private SparseBooleanArray expandState = new SparseBooleanArray();
    private LogFragment fragment;

    public AnalysisAdapter(final List<CallLogVO> logList, LogFragment fragment) {
        this.logList = logList;
        this.fragment = fragment;
        for (int i = 0; i < logList.size(); i++) {
            expandState.append(i, false);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        this.context = parent.getContext();
        return new ViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.item_call_log, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final CallLogVO item = logList.get(position);
        holder.setIsRecyclable(false);
        holder.phoneNumber.setText(item.getPhoneNumber());
        holder.date.setText(item.getDate());
        holder.time.setText(item.getTime());
        holder.category.setText(item.getCategory());
        holder.period.setText(item.getPeriod());

        if (item.getCategory().equals("부재중 전화")) {
            holder.buttonLayout.setVisibility(View.INVISIBLE);
        }



        holder.itemView.setBackgroundColor(ContextCompat.getColor(context, android.R.color.white));
        holder.expandableLayout.setInRecyclerView(true);
        holder.expandableLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.detail_information_background_color));
        holder.expandableLayout.setInterpolator(Utils.createInterpolator(Utils.FAST_OUT_SLOW_IN_INTERPOLATOR));
        holder.expandableLayout.setExpanded(expandState.get(position));
        holder.expandableLayout.setListener(new ExpandableLayoutListenerAdapter() {
            @Override
            public void onPreOpen() {
                createRotateAnimator(holder.buttonLayout, 0f, 180f).start();
                expandState.put(position, true);
            }

            @Override
            public void onPreClose() {
                createRotateAnimator(holder.buttonLayout, 180f, 0f).start();
                expandState.put(position, false);
            }
        });

        holder.buttonLayout.setRotation(expandState.get(position) ? 180f : 0f);
        holder.buttonLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickButton(holder.expandableLayout);
            }
        });

        holder.swipeLayout.addSwipeListener(new SimpleSwipeListener());
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.swipeLayout.close();
                Constants constants = Constants.getInstance();
                constants.deleteLog(position);
                logList.remove(position);
                fragment.setListViewAdapter(logList);
            }
        });
    }

    private void onClickButton(final ExpandableLayout expandableLayout) {
        expandableLayout.toggle();
    }

    @Override
    public int getItemCount() {
        return logList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView phoneNumber;
        public TextView date;
        public TextView time;
        public TextView category;
        public TextView period;
        public RelativeLayout buttonLayout;
        public SwipeLayout swipeLayout;
        public Button deleteButton;
        /**
         * You must use the ExpandableLinearLayout in the recycler view.
         * The ExpandableRelativeLayout doesn't work.
         */
        public ExpandableLinearLayout expandableLayout;

        public ViewHolder(View view) {
            super(view);
            phoneNumber = view.findViewById(R.id.phone_number);
            date = view.findViewById(R.id.date);
            time = view.findViewById(R.id.time);
            category = view.findViewById(R.id.category);
            period = view.findViewById(R.id.period);
            buttonLayout = view.findViewById(R.id.button);
            expandableLayout = view.findViewById(R.id.expandableLayout);
            swipeLayout = view.findViewById(R.id.swipe);
            deleteButton = view.findViewById(R.id.delete);

//            LineChart line = (LineChart)expandableLayout.findViewById(R.id.chart);
//            LineGraph lineGraph = new LineGraph(line);
//            lineGraph.setLineChart();
        }
    }

    public ObjectAnimator createRotateAnimator(final View target, final float from, final float to) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(target, "rotation", from, to);
        animator.setDuration(300);
        animator.setInterpolator(Utils.createInterpolator(Utils.LINEAR_INTERPOLATOR));
        return animator;
    }
}
