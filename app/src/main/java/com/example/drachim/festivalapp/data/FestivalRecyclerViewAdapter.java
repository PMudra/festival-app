package com.example.drachim.festivalapp.data;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.drachim.festivalapp.R;
import com.example.drachim.festivalapp.fragment.FestivalListFragment.OnListFragmentInteractionListener;

import java.text.DateFormat;
import java.util.List;

public class FestivalRecyclerViewAdapter extends RecyclerView.Adapter<FestivalRecyclerViewAdapter.ViewHolder> {

    private final List<Festival> festivals;
    private final OnListFragmentInteractionListener listener;

    public FestivalRecyclerViewAdapter(List<Festival> items, OnListFragmentInteractionListener listener) {
        festivals = items;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_festival_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position) {
        viewHolder.festival = festivals.get(position);
        viewHolder.getNameTextView().setText(festivals.get(position).getName());
        viewHolder.getDateTextView().setText(DateFormat.getDateInstance().format(festivals.get(position).getStartDate()));
        viewHolder.getPlaceTextView().setText(festivals.get(position).getPlace());

        viewHolder.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != listener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    listener.onListFragmentInteraction(viewHolder.getFestival());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return festivals.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView nameTextView;
        private final TextView dateTextView;
        private final TextView placeTextView;
        private Festival festival;

        ViewHolder(View view) {
            super(view);
            nameTextView = (TextView) view.findViewById(R.id.name);
            dateTextView = (TextView) view.findViewById(R.id.date);
            placeTextView = (TextView) view.findViewById(R.id.place);
        }

        public Festival getFestival() {
            return festival;
        }

        public TextView getNameTextView() {
            return nameTextView;
        }

        public TextView getDateTextView() {
            return dateTextView;
        }

        public TextView getPlaceTextView() {
            return placeTextView;
        }

        public View getView() {
            return itemView;
        }
    }
}
