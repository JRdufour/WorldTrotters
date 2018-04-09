package ca.worldtrotter.stclair.worldtrotters;

import android.app.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.design.internal.NavigationMenu;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;


import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;

import com.google.android.gms.location.places.Place;

import com.google.android.gms.location.places.ui.PlaceAutocomplete;


import java.util.ArrayList;

import io.github.yavski.fabspeeddial.FabSpeedDial;
import io.github.yavski.fabspeeddial.SimpleMenuListenerAdapter;
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TripFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TripFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TripFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    int INTENT_REQUEST_CODE = 1000;
    private static Activity activity;
    private DestinationRecyclerViewAdapter adapter;

    // TODO: Rename and change types of parameters
    private Integer mParam1;

    private OnFragmentInteractionListener mListener;


    //this will store the first destination as a place object
    ArrayList<Destination> destinationArrayList;
    RecyclerView destinationRecylcer;
    TextView tripNameTextView = null;
    FragmentManager fm;
    SwipeRefreshLayout refresher;
    private Trip currentTrip;

    public TripFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.

     * @return A new instance of fragment TripFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TripFragment newInstance(int param1) {
        TripFragment fragment = new TripFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getInt(ARG_PARAM1);
        }
    }
    //Using this method to hide the edit texts I am using for destination inputs. When the user fills out a destination, the next one in the array will appear
    private void hideDestinations(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        DatabaseHandler db = new DatabaseHandler(getContext());
        fm = getFragmentManager();
        if(mParam1 != null) {
            currentTrip = db.getTrip(mParam1);
        }


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_trip, container, false);
        activity = getActivity();
        activity.setTitle("Your Trip");
        //hide the fab button
        MainActivity.fab.hide();
        //Edit text for trip name
        tripNameTextView = view.findViewById(R.id.trip_name_text_view);
        TextView startDate = view.findViewById(R.id.trip_start_date_text_view);
        TextView endDate = view.findViewById(R.id.trip_end_date_text_view);
        LinearLayout datesLayout = view.findViewById(R.id.dates_layout);

        destinationArrayList = new ArrayList<>();
        //tripNameEditText.requestFocus();
        if(currentTrip != null) {
            tripNameTextView.setText(currentTrip.getName());
            destinationArrayList = db.getAllPlacesForTrip(currentTrip.getTripID());
            if(currentTrip.getStartDate() != 0){
                //startDate.setText(currentTrip.getStartDate());
                //TODO format the dates
                datesLayout.setVisibility(View.VISIBLE);
                startDate.setText(Helper.formatDate(currentTrip.getStartDate(), "MMMM d") + " - ");
            }else{
                datesLayout.setVisibility(View.GONE);
            }
            if(currentTrip.getStartDate() != 0){
                //endDate.setText(currentTrip.getStartDate());
                endDate.setText(Helper.formatDate(currentTrip.getEndDate()));
            }else {
                endDate.setText("");
            }

            /** This is all going to have to be take out and refactored **/
            //button for adding a new destination
            //CardView addNewLocationButton = view.findViewById(R.id.add_another_location_button);
            //grab the button for adding a trip from the xml
            //Button addTripButton = view.findViewById(R.id.create_trip_button);


        }
        //grab the recycler view
        destinationRecylcer = view.findViewById(R.id.destination_recycler_view);
        //destinationRecylcer.setHasFixedSize(true);
        //set the adatper for the recycler view
        adapter = new DestinationRecyclerViewAdapter(destinationArrayList);
        destinationRecylcer.setAdapter(adapter);
        //make the linear layout manager
        LinearLayoutManager manager = new LinearLayoutManager(getActivity().getBaseContext()){
            @Override
            public boolean supportsPredictiveItemAnimations() {return true;}
        };
        destinationRecylcer.setLayoutManager(manager);
        //set the item animator
        destinationRecylcer.setItemAnimator(new SlideInLeftAnimator());
        destinationRecylcer.getItemAnimator().setAddDuration(1000);

        refresher = view.findViewById(R.id.destination_recycler_swipe_layout);

        refresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresher.setRefreshing(true);
                refreshRecycler();
                refresher.setRefreshing(false);
            }
        });

        FabSpeedDial fabSpeedDial = view.findViewById(R.id.trip_fragment_fab);

        /**
         * The menu listner for the fab menu, currently has 4 options
         * Add a destination to the trip
         * Edit the trip's name
         * Edit the trip's dates
         * Delete the trip
         */
        fabSpeedDial.setMenuListener(new SimpleMenuListenerAdapter(){
            @Override
            public boolean onPrepareMenu(NavigationMenu navigationMenu) {
                // TODO: Do something with yout menu items, or return false if you don't want to show them
                return true;
            }

            @Override
            public boolean onMenuItemSelected(MenuItem menuItem) {
                //TODO: Start some activity
                int id = menuItem.getItemId();
                if(id == R.id.action_add_destination){
                    //handle adding another destination to the trip
                    try {
                        Intent i = new PlaceAutocomplete.
                                IntentBuilder(PlaceAutocomplete.MODE_OVERLAY).build(getActivity());
                        startActivityForResult(i, INTENT_REQUEST_CODE);

                    } catch (GooglePlayServicesRepairableException e) {
                        e.printStackTrace();
                    } catch (GooglePlayServicesNotAvailableException e) {
                        e.printStackTrace();
                    }

                } else if (id == R.id.action_edit_name){
                    //handle letting the user edit the trip's name
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    //builder.setMessage("Name your trip");
                    builder.setTitle("Edit Trip Name");
                    final EditText inputName = new EditText(getContext());
                    inputName.setHint("Trip Name");
                    inputName.setText(currentTrip.getName());
                    builder.setView(inputName);
                    builder.setPositiveButton("Change", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String name = inputName.getText().toString();
                            if(name != null){
                                currentTrip.setName(name);
                            }
                            DatabaseHandler db = new DatabaseHandler(getContext());
                            db.updateTrip(currentTrip);
                            db.close();
                            tripNameTextView.setText(name);
                        }
                    });
                    builder.setNegativeButton("Cancel", null);
                    builder.show();

                } else if (id == R.id.action_edit_times){
                    //handle letting the user edit the trip's start and end dates
                } else if (id == R.id.action_delete){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Delete Trip");
                    builder.setMessage("Are you sure you want to delete " + currentTrip.getName() + "?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //handle deleting the current trip
                            DatabaseHandler db = new DatabaseHandler(getContext());
                            db.deleteTrip(currentTrip.getTripID());
                            db.close();
                            fm.popBackStack();
                        }
                    });
                    builder.setNegativeButton("No", null);
                    builder.show();

                }
                return false;
            }
        });


        db.close();
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == INTENT_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(getActivity(), data);
                Log.i(TAG, "Place: " + place.getName());
                //add the place to the database
                DatabaseHandler db = new DatabaseHandler(getContext());
                Destination dest = new Destination(place.getId(),
                        0, 0,
                        currentTrip.getTripID(),
                        place.getName().toString());
                int id = db.addDestination(dest);
                Helper.addPlacePhoto(getContext(), place.getId());
                db.close();
                dest = db.getDestination(id);
                destinationArrayList.add(dest);
                adapter.notifyDataSetChanged();
            }
        }
    }

    public void refreshRecycler(){

    }



    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
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
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    /**
     * This class is the custom adapter for the recycler view that holds the destinations the user is adding to the trip
     */

}

