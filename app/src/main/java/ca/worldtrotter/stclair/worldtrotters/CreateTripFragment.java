package ca.worldtrotter.stclair.worldtrotters;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.ArrayList;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;


/**
 * #1
This is the first fragment the user will see when they want to set up a new trip
The fragment will ask the user where they would like to go, with a PlaceAutoComplete widget
 */
public class CreateTripFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private Trip currentTrip;
    private ArrayAdapter<Destination> adapter;
    //this will hold an array of places the user wants to go to on their trip
    private ArrayList<Destination> destinations;

    private Button destinationButton;
    private TextView headerTextView;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private final int INTENT_REQUEST_CODE = 1000;
    private OnFragmentInteractionListener mListener;

    public CreateTripFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreateTripFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateTripFragment newInstance(String param1, String param2) {
        CreateTripFragment fragment = new CreateTripFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_trip, container, false);
        MainActivity.fab.hide();
        getActivity().setTitle("Create A Trip");
        //grab the edit text
        currentTrip = new Trip();
        currentTrip.setTripID(-1);
        destinations = new ArrayList<>();
        destinationButton = view.findViewById(R.id.destination_button);
        headerTextView = view.findViewById(R.id.name_trip_header);



        destinationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                    String autocompleteLocation = sharedPreferences.getString("aclocation","CAN");

                    AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                            .setCountry(autocompleteLocation)
                            .build();

                    Intent i = new PlaceAutocomplete.
                            IntentBuilder(PlaceAutocomplete.MODE_OVERLAY).setFilter(typeFilter).build(getActivity());


                    startActivityForResult(i, INTENT_REQUEST_CODE);

                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }

            }
        });

        Button nextButton = view.findViewById(R.id.name_trip_next_button);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (destinations.size() == 0) {

                    Toast.makeText(getContext(), "Please add at least one destination", Toast.LENGTH_LONG);
                } else {
                    DatabaseHandler db = new DatabaseHandler(getContext());
                    for (Destination dest: destinations) {
                        db.addDestination(dest);
                    }

                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.setCustomAnimations(R.anim.slide_in_left_fragment_animation, R.anim.slide_out_right_fragment_animation, R.anim.slide_out_left, R.anim.slide_in_right);
                    transaction.replace(R.id.main_content, AddTripDateFragment.newInstance(currentTrip.getTripID()));
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            }
        });


        ListView destinationListView = view.findViewById(R.id.name_trip_destination_list_view);
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, android.R.id.text1, destinations);

        destinationListView.setAdapter(adapter);
        destinationListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                destinations.remove(position);
                adapter.notifyDataSetChanged();
                return true;
            }
        });
        return view;
    }


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

                String tripName = place.getName().toString() + " Trip";
                currentTrip.setName(tripName);
                headerTextView.setText(tripName);
                //see if the trip has already been added to the database
                if(currentTrip.getTripID() == -1) {
                    DatabaseHandler db = new DatabaseHandler(getContext());
                    int id = db.addTrip(currentTrip);
                    currentTrip = db.getTrip(id);
                }
                Destination newDest = new Destination(place.getId(), 0, 0, currentTrip.getTripID(), place.getName().toString());
                newDest.setTripId(currentTrip.getTripID());
                destinations.add(newDest);
                Helper.addPlacePhoto(getContext(), place.getId());

                adapter.notifyDataSetChanged();

                if(requestCode == INTENT_REQUEST_CODE){
                    //the intent was sent from the "add trip" button, add the destination as a new element in the array
                    //destinationArrayList.add(new Destination(place.getId(), null, null, 0, place.getName().toString()));
                    //adapter.notifyItemInserted(destinationArrayList.size());
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
        if(destinations.size() != 0){
            destinationButton.setText("Add Another Destination");
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
        if(destinations.size() == 0){
            DatabaseHandler db = new DatabaseHandler(getContext());
            db.deleteTrip(currentTrip.getTripID());
            db.close();
        }
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
