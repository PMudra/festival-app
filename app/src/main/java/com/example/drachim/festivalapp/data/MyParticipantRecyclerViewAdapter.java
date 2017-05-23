package com.example.drachim.festivalapp.data;

import android.content.res.ColorStateList;
import android.os.Build;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.widget.CompoundButtonCompat;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.drachim.festivalapp.R;
import com.example.drachim.festivalapp.activity.FestivalActivity;
import com.example.drachim.festivalapp.common.Utilities;
import com.example.drachim.festivalapp.fragment.FestivalPlanningFragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Participant}
 * TODO: Replace the implementation with code for your data type.
 */
public class MyParticipantRecyclerViewAdapter extends RecyclerView.Adapter<MyParticipantRecyclerViewAdapter.ViewHolder> {

    private final List<Participant> participants;
    private FestivalPlanningFragment festivalPlanningFragment;
    private final FestivalActivity activity;

    private SparseBooleanArray selectedItems;

    public MyParticipantRecyclerViewAdapter(List<Participant> participants, FestivalPlanningFragment festivalPlanningFragment) {
        this.participants = participants;
        this.festivalPlanningFragment = festivalPlanningFragment;

        activity = (FestivalActivity) festivalPlanningFragment.getActivity();

        selectedItems = new SparseBooleanArray();

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
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        if (activity.hasFestivalAccentColor()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ColorStateList  colorStateList = new ColorStateList(
                        new int[][]{
                                new int[]{-android.R.attr.state_checked}, // unchecked
                                new int[]{android.R.attr.state_checked} , // checked
                        },
                        new int[]{
                                activity.getFestivalAccentColor(),
                                activity.getFestivalAccentColor(),
                        }
                );
                CompoundButtonCompat.setButtonTintList(holder.checkBox, colorStateList);
            }
        }

        holder.participant = participants.get(position);
        holder.textView.setText(holder.participant.getName());

        holder.checkBox.setChecked(holder.participant.isInterested());
        Utilities.strikeThru(holder.textView, !holder.participant.isInterested());

        RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(holder.itemView.getContext().getResources(), participants.get(holder.getAdapterPosition()).getPhoto());
        drawable.setCircular(true);
        holder.textView.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                participants.get(holder.getAdapterPosition()).setInterested(holder.checkBox.isChecked());
                sortAndUpdateList();
            }
        });

        holder.mView.setOnLongClickListener(festivalPlanningFragment);
        holder.mView.setOnClickListener(festivalPlanningFragment);

        holder.mView.setOnClickListener(festivalPlanningFragment);
        holder.mView.setOnLongClickListener(festivalPlanningFragment);

        holder.mView.setActivated(isSelected(position));

    }

    @Override
    public int getItemCount() {
        return participants.size();
    }

    /**
     * Indicates if the item at position position is selected
     * @param position Position of the item to check
     * @return true if the item is selected, false otherwise
     */
    private boolean isSelected(int position) {
        return getSelectedItems().contains(position);
    }

    /**
     * Toggle the selection status of the item at a given position
     * @param position Position of the item to toggle the selection status for
     */
    public void toggleSelection(int position) {
        if (selectedItems.get(position, false)) {
            selectedItems.delete(position);
        } else {
            selectedItems.put(position, true);
        }
        notifyItemChanged(position);
    }

    /**
     * Clear the selection status for all items
     */
    public void clearSelection() {
        List<Integer> selection = getSelectedItems();
        selectedItems.clear();
        for (Integer i : selection) {
            notifyItemChanged(i);
        }
    }

    /**
     * Count the selected items
     * @return Selected items count
     */
    public int getSelectedItemCount() {
        return selectedItems.size();
    }

    /**
     * Indicates the list of selected items
     * @return List of selected items ids
     */
    public List<Integer> getSelectedItems() {
        List<Integer> items = new ArrayList<>(selectedItems.size());
        for (int i = 0; i < selectedItems.size(); ++i) {
            items.add(selectedItems.keyAt(i));
        }
        return items;
    }

    private void removeItem(int position) {
        participants.remove(position);
        notifyItemRemoved(position);
    }

    public void removeItems(List<Integer> positions) {
        // Reverse-sort the list
        Collections.sort(positions, new Comparator<Integer>() {
            @Override
            public int compare(Integer lhs, Integer rhs) {
                return rhs - lhs;
            }
        });

        // Split the list in ranges
        while (!positions.isEmpty()) {
            if (positions.size() == 1) {
                removeItem(positions.get(0));
                positions.remove(0);
            } else {
                int count = 1;
                while (positions.size() > count && positions.get(count).equals(positions.get(count - 1) - 1)) {
                    ++count;
                }

                if (count == 1) {
                    removeItem(positions.get(0));
                } else {
                    removeRange(positions.get(count - 1), count);
                }

                for (int i = 0; i < count; ++i) {
                    positions.remove(0);
                }
            }
        }
    }

    private void removeRange(int positionStart, int itemCount) {
        for (int i = 0; i < itemCount; ++i) {
            participants.remove(positionStart);
        }
        notifyItemRangeRemoved(positionStart, itemCount);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final CheckBox checkBox;
        final TextView textView;
        Participant participant;

        ViewHolder(View view) {
            super(view);
            mView = view;
            checkBox = (CheckBox) view.findViewById(R.id.cb_participant);
            textView = (TextView) view.findViewById(R.id.tv_participant);
        }

        @Override
        public String toString() {
            return super.toString() + " '";
        }

    }

}
