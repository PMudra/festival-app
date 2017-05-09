package com.example.drachim.festivalapp.fragment;

import android.support.v7.widget.RecyclerView;
import android.text.style.StrikethroughSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.example.drachim.festivalapp.R;
import com.example.drachim.festivalapp.common.Utilities;
import com.example.drachim.festivalapp.data.Participant;
import com.example.drachim.festivalapp.fragment.FestivalPlanningFragment.OnListFragmentInteractionListener;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Participant} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyParticipantRecyclerViewAdapter extends RecyclerView.Adapter<MyParticipantRecyclerViewAdapter.ViewHolder> {

    private static final StrikethroughSpan STRIKE_THROUGH_SPAN = new StrikethroughSpan();

    private final List<Participant> participants;
    private final OnListFragmentInteractionListener mListener;

    public MyParticipantRecyclerViewAdapter(List<Participant> participants, OnListFragmentInteractionListener listener) {
        this.participants = participants;
        this.mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_planning_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.participant = participants.get(position);
        holder.checkBox.setText(holder.participant.getName());

        Utilities.strikeThru(holder.checkBox, !holder.participant.isInterested());
        holder.checkBox.setChecked(holder.participant.isInterested());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.participant);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return participants.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final CheckBox checkBox;
        public Participant participant;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            checkBox = (CheckBox) view.findViewById(R.id.participant);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Utilities.strikeThru(checkBox, !isChecked);
                }
            });
        }

        @Override
        public String toString() {
            return super.toString() + " '";
        }
    }
}
