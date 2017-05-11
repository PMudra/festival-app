package com.example.drachim.festivalapp.fragment;

import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.drachim.festivalapp.R;
import com.example.drachim.festivalapp.data.Participant;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class FestivalPlanningFragment extends Fragment {

    private static final int PICK_CONTACT_REQUEST = 1;

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private List<Participant> participants;
    private MyParticipantRecyclerViewAdapter adapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FestivalPlanningFragment() {
    }

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
            final RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }

            participants = new ArrayList<>();
            participants.add(new Participant("Achim", true));
            participants.add(new Participant("Manni", true));
            participants.add(new Participant("Uwe", true));
            participants.add(new Participant("Herbert", true));
            participants.add(new Participant("Opa", false));
            participants.add(new Participant("Annegret", false));

            adapter = new MyParticipantRecyclerViewAdapter(participants, mListener);
            recyclerView.setAdapter(adapter);

            FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                    startActivityForResult(intent, PICK_CONTACT_REQUEST);
                }
            });
        }
        return view;
    }

    @Override

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case PICK_CONTACT_REQUEST:

                if (resultCode == RESULT_OK) {
                    contactPicked(data);
                }
                break;
        }

    }

    private void contactPicked(Intent data) {
        Uri contactUri = data.getData();
        String[] projection = {Phone.DISPLAY_NAME};

        CursorLoader loader = new CursorLoader(getActivity());
        loader.setProjection(projection);
        loader.setUri(contactUri);

        Cursor cursor = loader.loadInBackground();
        cursor.moveToFirst();

        int column = cursor.getColumnIndex(Phone.DISPLAY_NAME);
        String contactName = cursor.getString(column);

        participants.add(new Participant(contactName, true));
        adapter.sortAndUpdateList();

        Snackbar.make(getView(), contactName + " added", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Participant item);
    }
}
