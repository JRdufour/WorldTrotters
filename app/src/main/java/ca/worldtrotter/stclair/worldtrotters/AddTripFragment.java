package ca.worldtrotter.stclair.worldtrotters;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;


import java.io.Console;
import java.util.ArrayList;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddTripFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddTripFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddTripFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    //this variable will track the current destination we are working with
    int currentDestinationIndexPosition = 0;
   
    //this will store the first destination as a place object
    ArrayList<Place> placeArrayList;

    ArrayList<EditText> editTextDestinationArray;

    public AddTripFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddTripFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddTripFragment newInstance(String param1, String param2) {
        AddTripFragment fragment = new AddTripFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    //Using this method to hide the edit texts I am using for destination inputs. When the user fills out a destination, the next one in the array will appear
    private void hideDestinations(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_trip, container, false);
        getActivity().setTitle("Create a new Trip");
        //hide the fab button
        MainActivity.fab.hide();
        //Edit text for trip name
        final EditText tripName = view.findViewById(R.id.trip_name_edit_text);
        //button for adding a new destination
        CardView addNewLocationButton = view.findViewById(R.id.add_another_location_button);
        //grab the button for adding a trip from the xml
        Button addTripButton = view.findViewById(R.id.create_trip_button);
        placeArrayList = new ArrayList<>();

        //make an array list of edit texts that the user can add a destination to
        editTextDestinationArray = new ArrayList<>();
        
        editTextDestinationArray.add((EditText) view.findViewById(R.id.add_trip_destination_edit_text));
        //grab the next destinations and hide them

        editTextDestinationArray.add((EditText) view.findViewById(R.id.add_trip_destination_edit_text2));
        editTextDestinationArray.add((EditText) view.findViewById(R.id.add_trip_destination_edit_text3));
        editTextDestinationArray.add((EditText) view.findViewById(R.id.add_trip_destination_edit_text4));
        editTextDestinationArray.add((EditText) view.findViewById(R.id.add_trip_destination_edit_text5));

        for (int i = 1; i < editTextDestinationArray.size(); i++){
            editTextDestinationArray.get(i).setVisibility(View.GONE);
        }


        //Set the on click listners for the intents
        for(EditText editText: editTextDestinationArray){
            editText.setKeyListener(null);
            editText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        Intent i = new PlaceAutocomplete.
                                IntentBuilder(PlaceAutocomplete.MODE_OVERLAY).build(getActivity());
                        startActivityForResult(i, currentDestinationIndexPosition);

                    } catch (GooglePlayServicesRepairableException e) {
                        e.printStackTrace();
                    } catch (GooglePlayServicesNotAvailableException e) {
                        e.printStackTrace();
                    }
                }
            });

        }

        //this makes the destination edit text not editable to ensure the google search bar will come up




        //the onClick listner for the add trip button - gets called when the user presses the button to add a new trip
        addTripButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //make sure all the fields have been populated, including the trips name and at least the first destination is filled out
                if(tripName.getText().toString().trim().length() == 0 || editTextDestinationArray.get(0).getText().toString().trim().length() == 0){

                    //popup that says to populate fields
                    Snackbar.make(view, "Please make sure all fields are filled.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else {
                    //handle creating a new Trip

                    Trip newTrip = new Trip(tripName.getText().toString(), null, null, null);
                    DatabaseHandler db = new DatabaseHandler(getActivity().getBaseContext());
                    db.addTrip(newTrip);
                    db.close();
                    //bring the user back to the trips page
                    getFragmentManager().popBackStack();
                }



            }
        });

        //this button will check if the current destination has been filled in, and if it has, it will show the next destination edit text
        addNewLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //check if the current destination's edit text is filled - this is the only way I know how to do this
                if (editTextDestinationArray.get(currentDestinationIndexPosition).getText().toString().trim().length() != 0)
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == currentDestinationIndexPosition) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(getActivity(), data);
                Log.i(TAG, "Place: " + place.getName());
                editTextDestinationArray.get(currentDestinationIndexPosition).setText(place.getName());
                placeArrayList.add(place);

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


}
