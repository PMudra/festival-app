package com.example.drachim.festivalapp.fragment;

import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v13.app.FragmentCompat;
import android.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.drachim.festivalapp.R;
import com.example.drachim.festivalapp.common.Utilities;
import com.example.drachim.festivalapp.data.MyParticipantRecyclerViewAdapter;
import com.example.drachim.festivalapp.data.Participant;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class FestivalPlanningFragment extends Fragment implements FragmentCompat.OnRequestPermissionsResultCallback  {

    private static final int PICK_CONTACT_REQUEST = 1;
    private static final int READ_CONTACTS_PERMISSIONS_REQUEST_CODE = 2;

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
                    if (Utilities.checkAndRequestReadContactsPermission(FestivalPlanningFragment.this, READ_CONTACTS_PERMISSIONS_REQUEST_CODE)) {
                        openContactPicker();
                    }

                }
            });
        }
        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case READ_CONTACTS_PERMISSIONS_REQUEST_CODE:

                for (int grantResult : grantResults) {
                    if (grantResult == PackageManager.PERMISSION_DENIED) {
                        break;
                    }
                }
                openContactPicker();
                break;
            default:
                break;
        }
    }

    private void openContactPicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, PICK_CONTACT_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case PICK_CONTACT_REQUEST:

                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    Participant participant = getSelectedContact(uri);

                    participants.add(participant);
                    adapter.sortAndUpdateList();

                    Snackbar.make(getView(), participant.getName() + " added", Snackbar.LENGTH_SHORT).show();
                }

                break;
        }

    }

    private Participant getSelectedContact(Uri contactUri) {
        String[] projection = {Phone.DISPLAY_NAME, Phone.PHOTO_THUMBNAIL_URI};

        CursorLoader loader = new CursorLoader(getActivity());
        loader.setProjection(projection);
        loader.setUri(contactUri);

        Cursor cursor = loader.loadInBackground();
        cursor.moveToFirst();

        String contactName = cursor.getString(cursor.getColumnIndex(Phone.DISPLAY_NAME));

        Participant participant = new Participant(contactName, true);

        if (Utilities.checkReadContactsPermission(getActivity())) {
            String contactThumbnail = cursor.getString(cursor.getColumnIndex(Phone.PHOTO_THUMBNAIL_URI));

            Bitmap bp = null;
            if (contactThumbnail != null) {
                try {
                    Uri uri = Uri.parse(contactThumbnail);
                    bp = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);

                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            if (bp != null) {
                participant.setPhoto(bp);
            }
        }

        return participant;
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
