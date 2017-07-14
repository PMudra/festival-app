package com.example.drachim.festivalapp.data;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.drachim.festivalapp.R;
import com.example.drachim.festivalapp.fragment.AbstractFestivalListFragment;

import java.util.List;

public class FestivalRecyclerViewAdapter extends RecyclerView.Adapter<FestivalRecyclerViewAdapter.ViewHolder> {

    private final List<Festival> festivals;
    private final AbstractFestivalListFragment.OnFestivalListInteractionListener listener;

    public FestivalRecyclerViewAdapter(List<Festival> items, AbstractFestivalListFragment.OnFestivalListInteractionListener listener) {
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
        Festival festival = festivals.get(position);
        viewHolder.festival = festival;
        viewHolder.getNameTextView().setText(festival.getName());
        String dateRange = FestivalHelper.getDateRange(festival, viewHolder.getDateTextView().getContext());
        viewHolder.getDateTextView().setText(dateRange);
        viewHolder.getPlaceTextView().setText(festival.getPlace());

        FestivalImageLoader.loadProfileImage(festival.getId(), viewHolder.getImageView());

        viewHolder.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != listener) {
                    listener.onFestivalClicked(viewHolder.getFestival().getId());
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
        private final ImageView imageView;
        private Festival festival;

        ViewHolder(View view) {
            super(view);
            nameTextView = (TextView) view.findViewById(R.id.name);
            dateTextView = (TextView) view.findViewById(R.id.date);
            placeTextView = (TextView) view.findViewById(R.id.place);
            imageView = (ImageView) view.findViewById(R.id.image);
        }

        public Festival getFestival() {
            return festival;
        }

        TextView getNameTextView() {
            return nameTextView;
        }

        TextView getDateTextView() {
            return dateTextView;
        }

        TextView getPlaceTextView() {
            return placeTextView;
        }

        public ImageView getImageView() {
            return imageView;
        }

        public View getView() {
            return itemView;
        }
    }
}
