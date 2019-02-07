package ensharp.imagincup2019.fishingphishing.UI.UIElements;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.github.aakira.expandablelayout.ExpandableLayout;
import com.github.aakira.expandablelayout.ExpandableLayoutListenerAdapter;
import com.github.aakira.expandablelayout.ExpandableLinearLayout;
import com.github.aakira.expandablelayout.Utils;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;
import java.util.List;

import ensharp.imagincup2019.fishingphishing.Common.Constants;
import ensharp.imagincup2019.fishingphishing.Common.VO.CallLogVO;
import ensharp.imagincup2019.fishingphishing.R;
import ensharp.imagincup2019.fishingphishing.UI.Fragments.LogFragment;
import ensharp.imagincup2019.fishingphishing.UI.Graph.LineGraph;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

public class StickyAdapter extends BaseAdapter implements StickyListHeadersAdapter, SectionIndexer {

    private final Context context;
    private List<CallLogVO> logList;
    private int[] sectionIndices;
    private String[] sectionStrings;
    private LayoutInflater inflater;
    private SparseBooleanArray expandState = new SparseBooleanArray();
    private LogFragment fragment;

    public StickyAdapter(Context context, LogFragment fragment, List<CallLogVO> logList) {
        this.context = context;
        this.fragment = fragment;
        this.logList = logList;
        inflater = LayoutInflater.from(context);
        sectionIndices = getSectionIndices();
        sectionStrings = getSectionStrings();
        for (int i = 0; i < logList.size(); i++) {
            expandState.append(i, false);
        }
    }

    public int[] getSectionIndices() {
        ArrayList<Integer> sectionIndices = new ArrayList<>();
        String lastFirstString = logList.get(0).getPhoneNumber();
        sectionIndices.add(0);

        for (int i = 0; i < logList.size(); i++) {
            if (!logList.get(i).getPhoneNumber().equals(lastFirstString)) {
                lastFirstString = new String(logList.get(i).getPhoneNumber());
                sectionIndices.add(i);
            }
        }

        int[] sections = new int[sectionIndices.size()];
        for (int i = 0; i < sectionIndices.size(); i++) {
            sections[i] = sectionIndices.get(i);
        }

        return sections;
    }

    public String[] getSectionStrings() {
        String[] strings = new String[sectionIndices.length];
        for (int i = 0; i < sectionIndices.length; i++) {
            strings[i] = logList.get(sectionIndices[i]).getPhoneNumber();
        }

        return strings;
    }

    @Override
    public int getCount() {
        return logList.size();
    }

    @Override
    public Object getItem(int position) {
        return logList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_organized_call_log, parent, false);
            holder.date = (TextView) convertView.findViewById(R.id.date);
            holder.time = (TextView) convertView.findViewById(R.id.time);
            holder.category = (TextView) convertView.findViewById(R.id.category);
            holder.period = (TextView) convertView.findViewById(R.id.period);
            holder.buttonLayout = (RelativeLayout) convertView.findViewById(R.id.button);
            holder.swipeLayout = (SwipeLayout) convertView.findViewById(R.id.swipe);
            holder.deleteButton = (Button) convertView.findViewById(R.id.delete);
            holder.expandableLayout = (ExpandableLinearLayout) convertView.findViewById(R.id.expandableLayout);

            LineChart line = (LineChart)holder.expandableLayout.findViewById(R.id.chart);
            LineGraph lineGraph = new LineGraph(line);
            List<Entry> entries = new ArrayList<>();

            for(int i=0 ; i < logList.get(position).getAccuracyList().size() ; i++){
                entries.add(new Entry(i+1, Float.parseFloat(logList.get(position).getAccuracyList().get(i))));
            }

            lineGraph.setLineChart(entries);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.date.setText(logList.get(position).getDate());
        holder.time.setText(logList.get(position).getTime());
        holder.category.setText(logList.get(position).getCategory());
        holder.period.setText(logList.get(position).getPeriod());

        if (logList.get(position).getCategory().equals("부재중 전화")) {
            holder.buttonLayout.setVisibility(View.INVISIBLE);
        }

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
            public void onClick(View v) {
                holder.swipeLayout.close();
                Constants constants = Constants.getInstance();
                constants.deleteLog(position);
                logList.remove(position);
                fragment.setListViewAdapter(logList);
            }
        });

        return convertView;
    }

    private void onClickButton(final ExpandableLayout expandableLayout) {
        expandableLayout.toggle();
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        HeaderViewHolder holder;

        if (convertView == null) {
            holder = new HeaderViewHolder();
            convertView = inflater.inflate(R.layout.item_sticky_list_header, parent, false);
            holder.header = (TextView) convertView.findViewById(R.id.header);
            convertView.setTag(holder);
        } else {
            holder = (HeaderViewHolder) convertView.getTag();
        }

        holder.header.setText(logList.get(position).getPhoneNumber());

        return convertView;
    }

    @Override
    public long getHeaderId(int position) {
        return logList.get(position).getTag();
    }

    @Override
    public int getPositionForSection(int section) {
        if (sectionIndices.length == 0) {
            return 0;
        }

        if (section >= sectionIndices.length) {
            section = sectionIndices.length - 1;
        } else if (section < 0) {
            section = 0;
        }

        return sectionIndices[section];
    }

    @Override
    public int getSectionForPosition(int position) {
        for (int i = 0; i < sectionIndices.length; i++) {
            if (position < sectionIndices[i]) {
                return i - 1;
            }
        }

        return sectionIndices.length - 1;
    }

    @Override
    public Object[] getSections() {
        return sectionStrings;
    }

    class HeaderViewHolder {
        TextView header;
    }

    public static class ViewHolder {
        public TextView date;
        public TextView time;
        public TextView category;
        public TextView period;
        public RelativeLayout buttonLayout;
        public SwipeLayout swipeLayout;
        public Button deleteButton;
        public ExpandableLinearLayout expandableLayout;
    }

    public ObjectAnimator createRotateAnimator(final View target, final float from, final float to) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(target, "rotation", from, to);
        animator.setDuration(300);
        animator.setInterpolator(Utils.createInterpolator(Utils.LINEAR_INTERPOLATOR));
        return animator;
    }
}
