package com.example.drachim.festivalapp.fragment;

import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v13.app.FragmentCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.drachim.festivalapp.R;
import com.example.drachim.festivalapp.activity.ContactsActivity;
import com.example.drachim.festivalapp.activity.FestivalActivity;
import com.example.drachim.festivalapp.common.Utilities;
import com.example.drachim.festivalapp.data.LocalStorage;
import com.example.drachim.festivalapp.data.Participant;
import com.example.drachim.festivalapp.data.ParticipantRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class FestivalPlanningFragment extends Fragment implements FragmentCompat.OnRequestPermissionsResultCallback, InputParticipantDialogFragment.Callback, View.OnLongClickListener, View.OnClickListener {

    private static final int READ_CONTACTS_PERMISSIONS_REQUEST_CODE = 1;
    private static final int CONTACT_ACTIVITY_REQUEST = 2;

    private List<Participant> participants;
    private ParticipantRecyclerViewAdapter adapter;
    private RecyclerView recyclerView;

    private FloatingActionButton fab;
    private FloatingActionButton fab_add_single;
    private FloatingActionButton fab_add_multiple;

    private ActionMode actionMode;
    private int festivalId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_planning_list, container, false);

        festivalId = ((FestivalActivity) getActivity()).getFestival().getId();

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));

            participants = LocalStorage.getParticipants(getActivity(), festivalId);
            adapter = new ParticipantRecyclerViewAdapter(participants, this);
            adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                @Override
                public void onChanged() {
                    super.onChanged();
                    saveParticipants();
                }
            });
            adapter.sortAndUpdateList();
            recyclerView.setAdapter(adapter);

            fab_add_multiple = (FloatingActionButton) getActivity().findViewById(R.id.fab2);
            fab_add_single = (FloatingActionButton) getActivity().findViewById(R.id.fab1);

            fab_add_multiple.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Utilities.checkAndRequestReadContactsPermission(FestivalPlanningFragment.this, READ_CONTACTS_PERMISSIONS_REQUEST_CODE)) {
                        openContactsActivity();
                    }
                }
            });

            fab_add_single.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showInputDialog();

                }
            });

            fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (fab_add_multiple.isShown() || fab_add_single.isShown()) {
                        hideFabsMini();
                    } else {
                        showFabsMini();
                    }
                }
            });

        }
        return view;
    }

    private void hideFabsMini() {
        Utilities.animRotateBackward(fab);
        fab_add_multiple.hide();
        fab_add_single.hide();
    }

    private void showFabsMini() {
        Utilities.animRotateForward(fab);
        fab_add_multiple.show();
        fab_add_single.show();
    }


    private void openContactsActivity() {
        Intent intent = new Intent(getActivity(), ContactsActivity.class);
        startActivityForResult(intent, CONTACT_ACTIVITY_REQUEST);
        hideFabsMini();
    }

    private void showInputDialog() {
        DialogFragment dialogFragment = new InputParticipantDialogFragment();
        dialogFragment.setTargetFragment(this, 1); //request code
        dialogFragment.show(getChildFragmentManager(), "dialog");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case READ_CONTACTS_PERMISSIONS_REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openContactsActivity();
                    break;
                } else {
                    break;
                }
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case CONTACT_ACTIVITY_REQUEST:
                if (resultCode == RESULT_OK) {

                    ArrayList<Participant> addedContacts = data.getParcelableArrayListExtra("selectedContacts");

                    participants.addAll(addedContacts);
                    adapter.sortAndUpdateList();

                    String message;
                    if (addedContacts.size() == 1) {
                        message = addedContacts.get(0).getName() + " " + getString(R.string.added);
                    } else {
                        message = addedContacts.size() + " " + getString(R.string.participants) + " " + getString(R.string.added);
                    }
                    Snackbar.make(fab, message, Snackbar.LENGTH_SHORT).show();

                }
                break;
        }
    }

    @Override
    public void accept(String participantName) {
        hideFabsMini();

        if (!participantName.trim().isEmpty()) {

            participants.add(new Participant(participantName));
            adapter.sortAndUpdateList();

            Snackbar.make(fab, participantName + " added", Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void cancel() {
        hideFabsMini();
    }

    /**
     * Toggle the selection state of an item.
     * <p>
     * If the item was the last one in the selection and is unselected, the selection is stopped.
     * Note that the selection must already be started (actionMode must not be null).
     *
     * @param position Position of the item to toggle the selection state
     */
    private void toggleSelection(int position) {
        adapter.toggleSelection(position);
        int count = adapter.getSelectedItemCount();

        if (count == 0) {
            actionMode.finish();
        } else if (count == 1) {
            actionMode.setTitle("1 " + getString(R.string.single_item_selected));
        } else {
            actionMode.setTitle(count + " " + getString(R.string.multiple_items_selected));
        }
    }

    @Override
    public void onClick(View v) {
        if (actionMode != null) {
            toggleSelection(recyclerView.getChildLayoutPosition(v));
        }
    }

    @Override
    public boolean onLongClick(View v) {

        if (actionMode == null) {
            actionMode = ((AppCompatActivity) getActivity()).startSupportActionMode(new ActionMode.Callback() {
                @Override
                public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                    mode.getMenuInflater().inflate(R.menu.menu_cab_planning, menu);
                    fab.hide();
                    hideFabsMini();
                    return true;
                }

                @Override
                public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                    return true;
                }

                @Override
                public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.action_remove:
                            adapter.removeItems(adapter.getSelectedItemIds());
                            adapter.sortAndUpdateList();
                            mode.finish();
                            return true;

                        default:
                            return false;
                    }
                }

                @Override
                public void onDestroyActionMode(ActionMode mode) {
                    adapter.clearSelection();
                    actionMode = null;
                    fab.show();
                }
            });
        }

        toggleSelection(recyclerView.getChildLayoutPosition(v));

        return true;
    }

    private void saveParticipants() {
        LocalStorage.saveParticipants(getActivity(), festivalId, participants);
    }

}