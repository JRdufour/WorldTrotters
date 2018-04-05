package ca.worldtrotter.stclair.worldtrotters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;


/**
 * #1
This is the first fragment the user will see when they want to set up a new trip
The fragment will ask the user where they would like to go, with a PlaceAutoComplete widget
 */
public class NameTrip extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private Trip currentTrip;
    private TextView destinationTextView;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private final int INTENT_REQUEST_CODE = 1000;
    private OnFragmentInteractionListener mListener;

    public NameTrip() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NameTrip.
     */
    // TODO: Rename and change types and number of parameters
    public static NameTrip newInstance(String param1, String param2) {
        NameTrip fragment = new NameTrip();
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
        View view = inflater.inflate(R.layout.fragment_name_trip, container, false);
        //grab the edit text
        currentTrip = new Trip();
        EditText destinationEditText = view.findViewById(R.id.name_trip_edit_text);
        destinationTextView = view.findViewById(R.id.destination_text_view);
        destinationEditText.setEnabled(false);
        destinationEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent i = new PlaceAutocomplete.
                            IntentBuilder(PlaceAutocomplete.MODE_OVERLAY).build(getActivity());
                    startActivityForResult(i, INTENT_REQUEST_CODE);

                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }

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
                currentTrip.setName(place.getName().toString() + " Trip");

                DatabaseHandler db = new DatabaseHandler(getContext());
                int id = db.addTrip(currentTrip);

                Destination destination = new Destination(place.getId(), null, null, id, place.getName().toString());
                db.addDestination(destination);
                destinationTextView.setText(place.getName().toString());

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
