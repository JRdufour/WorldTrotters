package ca.worldtrotter.stclair.worldtrotters;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;


import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;

import com.google.android.gms.location.places.Place;

import com.google.android.gms.location.places.ui.PlaceAutocomplete;


import java.util.ArrayList;

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
        if(mParam1 != null) {
            currentTrip = db.getTrip(mParam1);
        }


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_trip, container, false);
        activity = getActivity();
        activity.setTitle("Your Trip");
        //hide the fab button
        MainActivity.fab.hide();
        //Edit text for trip name
        final EditText tripNameEditText = view.findViewById(R.id.trip_name_edit_text);
        //set the Edit Text to uneditable

        //give the trip name edit text focus
        //tripNameEditText.requestFocus();
        if(currentTrip != null) {
            tripNameEditText.setText(currentTrip.getName());


            /** This is all going to have to be take out and refactored **/
            //button for adding a new destination
            //CardView addNewLocationButton = view.findViewById(R.id.add_another_location_button);
            //grab the button for adding a trip from the xml
            //Button addTripButton = view.findViewById(R.id.create_trip_button);


            destinationArrayList = db.getAllPlacesForTrip(currentTrip.getTripID());

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


        /**
        //the onClick listner for the add trip button - gets called when the user presses the button to add a new trip
        addTripButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //make sure all the fields have been populated, including the trips name and at least the first destination is filled out
                if(tripNameEditText.getText().toString().trim().length() == 0 ){

                    //popup that says to populate fields
                    Snackbar.make(view, "Please Name Your Trip.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else if(destinationArrayList.size() == 0){
                    Snackbar.make(view, "Please Add At Least One Destination", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }

                else {
                    //handle creating a new Trip

                    destinationArrayList = adapter.getDestinationArrayList();
                    String tripName = tripNameEditText.getText().toString().trim();

                    Trip newTrip = new Trip(tripName, null, null, destinationArrayList.get(0).getStartDateTime());
                    DatabaseHandler db = new DatabaseHandler(getActivity().getBaseContext());
                    int id = db.addTrip(newTrip);
                    //db.deleteAllTrips();
                    for (Destination dest: destinationArrayList) {
                        dest.setTripId(id);
                        db.addDestination(dest);
                    }
                    db.close();
                    //tell the user that the trip was made
                    Snackbar.make(view, "Trip Added!", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    //bring the user back to the trips page
                    getFragmentManager().popBackStack();
                }
            }
        });





        //this button will check if the current destination has been filled in, and if it has, it will launch a new Place Autocomplete intent
        addNewLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //check if the current displayed location has been filled out?
                //handel adding a new location
                try {
                    Intent i = new PlaceAutocomplete.
                            IntentBuilder(PlaceAutocomplete.MODE_OVERLAY).build(activity);
                    startActivityForResult(i, INTENT_REQUEST_CODE);

                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }

            }
        });

         **/

        db.close();
        return view;
    }

    //this method will be called when the user taps a destination's name to edit it

    /**
     * This method gets called whenever the place autocomplete intent resolves, returning a place
     * object. We then add a new destination with that place, allowing the user to specify a start and end time
     * or, in the case the user is editing a destination, we want to change the name and place of the destination they are editing
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == INTENT_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(getActivity(), data);
                Log.i(TAG, "Place: " + place.getName());

                if(requestCode == INTENT_REQUEST_CODE){
                    //the intent was sent from the "add trip" button, add the destination as a new element in the array
                    destinationArrayList.add(new Destination(place.getId(), null, null, 0, place.getName().toString()));
                    adapter.notifyItemInserted(destinationArrayList.size());
                }
                // TODO: add the edit functionality here




            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(getActivity(), data);
                // TODO: Handle the error.
                Log.i(TAG, status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
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

