package com.bwc.genie.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bwc.genie.R;
import com.bwc.genie.glucose.GlucoseTest;
import com.bwc.genie.util.DateUtils;

import java.util.ArrayList;


public class UserTestsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int TEST_ITEM_TYPE = 1;
    public static final int DAY_OF_TEST_ITEM_TYPE = 2;

    private ArrayList<UserTestsAdapterItem> dataSet;

    public UserTestsAdapter(ArrayList<UserTestsAdapterItem> dataSet) {
        this.dataSet = dataSet;
    }

    public void setDataSet(ArrayList<UserTestsAdapterItem> dataSet) {
        this.dataSet = dataSet;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case TEST_ITEM_TYPE:
                View glucoseTestListItemView = inflater.inflate(R.layout.list_item_test_layout, parent, false);
                viewHolder = new GlucoseTestViewHolder(glucoseTestListItemView);
                break;
            case DAY_OF_TEST_ITEM_TYPE:
                View dayOfTestListItemView = inflater.inflate(R.layout.list_item_day_of_test_layout, parent, false);
                viewHolder = new DayViewHolder(dayOfTestListItemView);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case TEST_ITEM_TYPE:
                GlucoseTestViewHolder glucoseTestViewHolder = (GlucoseTestViewHolder) holder;
                configureGlucoseTestViewHolder(glucoseTestViewHolder, position);
                break;
            case DAY_OF_TEST_ITEM_TYPE:
                DayViewHolder dayViewHolder = (DayViewHolder) holder;
                configureDayViewHolder(dayViewHolder, position);
                break;
        }
    }


    @Override
    public int getItemViewType(int position) {

        if (dataSet.get(position).getListItem() instanceof GlucoseTest) {
            return UserTestsAdapter.TEST_ITEM_TYPE;
        } else if (dataSet.get(position).getListItem() instanceof DayOfTest) {
            return UserTestsAdapter.DAY_OF_TEST_ITEM_TYPE;
        }
        return -1;
    }


    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    private void configureGlucoseTestViewHolder(GlucoseTestViewHolder glucoseTestViewHolder, int position) {
        UserTestsAdapterItem userTestsAdapterItem = dataSet.get(position);

        GlucoseTest glucoseTest = (GlucoseTest) userTestsAdapterItem.getListItem();
        if (glucoseTest != null) {
            glucoseTestViewHolder.getGlucoseLevelTextView().setText(glucoseTest.getGlucoseLevel());
            glucoseTestViewHolder.getTimeOfTestTextView().setText(DateUtils.getTimeOfTestFormat(glucoseTest.getDateTimeOfTest()));
        }
    }

    private void configureDayViewHolder(DayViewHolder dayViewHolder, int position) {

        DayOfTest dayOfTest = (DayOfTest) dataSet.get(position).getListItem();
        if (dayOfTest != null) {
            dayViewHolder.getDayOfTestTextView().setText(dayOfTest.getDate());

        }
    }

    public static class GlucoseTestViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        private TextView glucoseLevelTextView;
        private TextView timeOfTestTextView;
        public GlucoseTestViewHolder(View v) {
            super(v);
            glucoseLevelTextView = (TextView) v.findViewById(R.id.qlucose_level_textview_id);
            timeOfTestTextView = (TextView) v.findViewById(R.id.time_of_test_textview);
        }

        public TextView getGlucoseLevelTextView() {
            return glucoseLevelTextView;
        }

        public void setGlucoseLevelTextView(TextView glucoseLevelTextView) {
            this.glucoseLevelTextView = glucoseLevelTextView;
        }

        public TextView getTimeOfTestTextView() {
            return timeOfTestTextView;
        }

        public void setTimeOfTestTextView(TextView timeOfTestTextView) {
            this.timeOfTestTextView = timeOfTestTextView;
        }
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class DayViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        private TextView dayOfTestTextView;

        public DayViewHolder(View v) {
            super(v);
            dayOfTestTextView = (TextView) v.findViewById(R.id.day_of_test_textview_id);
        }

        public TextView getDayOfTestTextView() {
            return dayOfTestTextView;
        }

        public void setDayOfTestTextView(TextView dayOfTestTextView) {
            this.dayOfTestTextView = dayOfTestTextView;
        }
    }

}
