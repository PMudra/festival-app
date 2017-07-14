package com.example.drachim.festivalapp.data;

import android.content.res.AssetFileDescriptor;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.support.v4.widget.CompoundButtonCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.drachim.festivalapp.R;
import com.example.drachim.festivalapp.activity.FestivalActivity;
import com.example.drachim.festivalapp.common.Utilities;
import com.example.drachim.festivalapp.fragment.FestivalPlanningFragment;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ParticipantRecyclerViewAdapter extends RecyclerView.Adapter<ParticipantRecyclerViewAdapter.ViewHolder> {

    private final List<Participant> participants;
    private final FestivalPlanningFragment festivalPlanningFragment;
    private final FestivalActivity activity;
    private final com.example.drachim.festivalapp.common.ImageLoader imageLoader;

    private final SparseBooleanArray selectedItems;

    public ParticipantRecyclerViewAdapter(List<Participant> participants, FestivalPlanningFragment festivalPlanningFragment) {
        this.participants = participants;
        this.festivalPlanningFragment = festivalPlanningFragment;
        this.activity = (FestivalActivity) festivalPlanningFragment.getActivity();
        this.selectedItems = new SparseBooleanArray();

        this.imageLoader = new com.example.drachim.festivalapp.common.ImageLoader(this.activity, 100) {
            @Override
            protected Bitmap processBitmap(Object data) {
                // This gets called in a background thread and passed the data from
                // ImageLoader.loadImage().
                return loadContactPhotoThumbnail(data.toString(), getImageSize());
            }
        };

        // Tell the image loader to set the image directly when it's finished loading
        // rather than fading in
        imageLoader.setImageFadeIn(true);

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

        Participant participant = participants.get(position);
        holder.participant = participant;

        // Checkbox
        holder.getCheckBox().setChecked(participant.isInterested());
        holder.getCheckBox().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                participants.get(holder.getAdapterPosition()).setInterested(((CheckBox) v).isChecked());
                sortAndUpdateList();
            }
        });

        // Textview
        holder.getTextView().setText(participant.getName());
        Utilities.strikeThru(holder.getTextView(), !participant.isInterested());

        // Imageview
        Log.d("TEST", participant.getName() + ": " + participant.getPhoto());
        if (participant.getPhoto() != null) {
            holder.getImageView().setVisibility(View.VISIBLE);
            imageLoader.loadImage(participant.getPhoto(), holder.getImageView());
        } else {
            holder.getImageView().setVisibility(View.GONE);
        }

        holder.getView().setOnLongClickListener(festivalPlanningFragment);
        holder.getView().setOnClickListener(festivalPlanningFragment);

        holder.getView().setActivated(isSelected(position));
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
        return getSelectedItemIds().contains(position);
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
        List<Integer> selection = getSelectedItemIds();
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
    public List<Integer> getSelectedItemIds() {
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

    private Bitmap loadContactPhotoThumbnail(String photoData, int imageSize) {
        // Instantiates an AssetFileDescriptor. Given a content Uri pointing to an image file, the
        // ContentResolver can return an AssetFileDescriptor for the file.
        AssetFileDescriptor afd = null;

        // This "try" block catches an Exception if the file descriptor returned from the Contacts
        // Provider doesn't point to an existing file.
        try {
            Uri thumbUri;
            // If Android 3.0 or later, converts the Uri passed as a string to a Uri object.
            thumbUri = Uri.parse(photoData);
            // Retrieves a file descriptor from the Contacts Provider. To learn more about this
            // feature, read the reference documentation for
            // ContentResolver#openAssetFileDescriptor.

            afd = this.activity.getContentResolver().openAssetFileDescriptor(thumbUri, "r");

            // Gets a FileDescriptor from the AssetFileDescriptor. A BitmapFactory object can
            // decode the contents of a file pointed to by a FileDescriptor into a Bitmap.
            FileDescriptor fileDescriptor = afd.getFileDescriptor();

            if (fileDescriptor != null) {
                // Decodes a Bitmap from the image pointed to by the FileDescriptor, and scales it
                // to the specified width and height
                return com.example.drachim.festivalapp.common.ImageLoader.decodeSampledBitmapFromDescriptor(
                        fileDescriptor, imageSize, imageSize);
            }
        } catch (FileNotFoundException e) {
            // If the file pointed to by the thumbnail URI doesn't exist, or the file can't be
            // opened in "read" mode, ContentResolver.openAssetFileDescriptor throws a
            // FileNotFoundException.

        } finally {
            // If an AssetFileDescriptor was returned, try to close it
            if (afd != null) {
                try {
                    afd.close();
                } catch (IOException e) {
                    // Closing a file descriptor might cause an IOException if the file is
                    // already closed. Nothing extra is needed to handle this.
                }
            }
        }

        // If the decoding failed, returns null
        return null;
    }

    private void removeRange(int positionStart, int itemCount) {
        for (int i = 0; i < itemCount; ++i) {
            participants.remove(positionStart);
        }
        notifyItemRangeRemoved(positionStart, itemCount);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private Participant participant;
        private final CheckBox checkBox;
        private final TextView textView;
        private final ImageView imageView;

        ViewHolder(View view) {
            super(view);
            checkBox = (CheckBox) view.findViewById(R.id.cb_participant);
            textView = (TextView) view.findViewById(R.id.tv_participant);
            imageView = (ImageView) view.findViewById(R.id.iv_participant);
        }

        public Participant getParticipant() {
            return participant;
        }

        CheckBox getCheckBox() {
            return checkBox;
        }

        TextView getTextView() {
            return textView;
        }

        ImageView getImageView() {
            return imageView;
        }

        public View getView() {
            return itemView;
        }
    }

}
