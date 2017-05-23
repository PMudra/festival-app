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
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.drachim.festivalapp.R;
import com.example.drachim.festivalapp.activity.ContactsActivity;
import com.example.drachim.festivalapp.activity.FestivalActivity;
import com.example.drachim.festivalapp.common.Utilities;
import com.example.drachim.festivalapp.data.MyParticipantRecyclerViewAdapter;
import com.example.drachim.festivalapp.data.Participant;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * A fragment representing a list of Items.
 * <p/>
 */
public class FestivalPlanningFragment extends Fragment implements FragmentCompat.OnRequestPermissionsResultCallback, InputParticipantDialogFragment.Callback, View.OnLongClickListener, View.OnClickListener {

    private static final int PICK_CONTACT_REQUEST = 1;
    private static final int CONTACT_ACTIVITY_REQUEST = 3;
    private static final int READ_CONTACTS_PERMISSIONS_REQUEST_CODE = 2;

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private List<Participant> participants;
    private MyParticipantRecyclerViewAdapter adapter;
    private RecyclerView recyclerView;

    private FloatingActionButton fab;
    private FloatingActionButton fab_add_single;
    private FloatingActionButton fab_add_multiple;

    private ActionMode actionMode;

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")

    public static FestivalPlanningFragment newInstance(int columnCount) {
        FestivalPlanningFragment fragment = new FestivalPlanningFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_planning_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }

            participants = new ArrayList<>();
            participants.add(new Participant("Achim"));
            participants.add(new Participant("Manni"));
            participants.add(new Participant("Uwe"));
            participants.add(new Participant("Herbert"));
            participants.add(new Participant("Opa"));
            participants.add(new Participant("Annegret"));

            adapter = new MyParticipantRecyclerViewAdapter(participants, this);
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
                    toggleShowFab();
                }
            });

        }
        return view;
    }

    private void openContactsActivity() {
        Intent intent = new Intent(getActivity(), ContactsActivity.class);
        startActivityForResult(intent, CONTACT_ACTIVITY_REQUEST);
        toggleShowFab();
    }

    private void showInputDialog() {
        DialogFragment dialogFragment = new InputParticipantDialogFragment();
        dialogFragment.setTargetFragment(this, 1); //request code
        dialogFragment.show(getFragmentManager(), "dialog");
    }

    private void toggleShowFab() {
        if (fab_add_multiple.isShown() || fab_add_single.isShown()) {

            getFragmentManager().popBackStack();

            Utilities.animRotateBackward(fab);

            fab_add_multiple.hide();
            fab_add_single.hide();
        } else {
            Utilities.animRotateForward(fab);

            getFragmentManager()
                    .beginTransaction()
                    .addToBackStack(null)
                    .commit();

            fab_add_multiple.show();
            fab_add_single.show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case READ_CONTACTS_PERMISSIONS_REQUEST_CODE:

                for (int grantResult : grantResults) {
                    if (grantResult == PackageManager.PERMISSION_GRANTED) {
                        openContactsActivity();
                        break;
                    } else {
                        break;
                    }
                }
                Log.d("TEST", "erklärung sollte angezeigt werden");
                break;
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

                    ArrayList<Participant> addedParticipants = data.getParcelableArrayListExtra("participants");
                    participants.addAll(addedParticipants);
                    adapter.sortAndUpdateList();

                    String message;
                    if (addedParticipants.size() == 1) {
                        message = addedParticipants.get(0).getName() + " " + getString(R.string.added);
                    } else {
                        message = addedParticipants.size() + " " + getString(R.string.participants) + " " + getString(R.string.added);
                    }
                    Snackbar.make(fab, message, Snackbar.LENGTH_SHORT).show();

                }
                break;
        }

    }

    @Override
    public void accept(String participantName) {
        toggleShowFab();

        if (!participantName.trim().isEmpty()) {

            participants.add(new Participant(participantName));
            adapter.sortAndUpdateList();

            Snackbar.make(fab, participantName + " added", Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void cancel() {
        toggleShowFab();
    }

    /**
     * Toggle the selection state of an item.
     *
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
        } else {
            actionMode.setTitle(String.valueOf(count));
            actionMode.invalidate();
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
                    mode.getMenuInflater().inflate (R.menu.menu_cab_planning, menu);
                    ((FestivalActivity) getActivity()).hideFabs();
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
                            adapter.removeItems(adapter.getSelectedItems());
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

}