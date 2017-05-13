package com.example.drachim.festivalapp.data;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.example.drachim.festivalapp.R;
import com.example.drachim.festivalapp.common.Utilities;
import com.example.drachim.festivalapp.fragment.FestivalPlanningFragment.OnListFragmentInteractionListener;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Participant} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyParticipantRecyclerViewAdapter extends RecyclerView.Adapter<MyParticipantRecyclerViewAdapter.ViewHolder> {

    private final List<Participant> participants;
    private final OnListFragmentInteractionListener mListener;

    public MyParticipantRecyclerViewAdapter(List<Participant> participants, OnListFragmentInteractionListener listener) {
        this.participants = participants;
        this.mListener = listener;

        sortList();
    }

    public void sortAndUpdateList() {
        sortList();
        notifyDataSetChanged();
    }

    /**
     * First sort by isInterested.
     * Second sort by participant name.
     */
    private void sortList() {
        Collections.sort(participants, new Comparator<Participant>() {
            @Override
            public int compare(final Participant p1, final Participant p2) {
                int c = Boolean.compare(p2.isInterested(), p1.isInterested());
                if (c == 0) {
                    c = p1.getName().compareTo(p2.getName());
                }
                return c;
            }});
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

        holder.checkBox.setChecked(holder.participant.isInterested());
        Utilities.strikeThru(holder.checkBox, !holder.participant.isInterested());

        RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(holder.itemView.getContext().getResources(), participants.get(holder.getAdapterPosition()).getPhoto());
        drawable.setCircular(true);
        holder.checkBox.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                participants.get(holder.getAdapterPosition()).setInterested(holder.checkBox.isChecked());
                sortAndUpdateList();
            }
        });

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
        }

        @Override
        public String toString() {
            return super.toString() + " '";
        }
    }

}
