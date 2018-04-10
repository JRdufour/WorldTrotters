package ca.worldtrotter.stclair.worldtrotters;

import android.app.DatePickerDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;
import java.util.Date;


/**
#2
 This is the second fragment that will ask the user to input when they would
 */
public class AddTripDateFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private int mParam1 = -1;

    private OnFragmentInteractionListener mListener;

    public AddTripDateFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.

     * @return A new instance of fragment AddTripDateFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddTripDateFragment newInstance(int param1) {
        AddTripDateFragment fragment = new AddTripDateFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_trip_date, container, false);
        if(mParam1 == -1){
            //TODO take the user back to recycler, display error msg
        }

        DatabaseHandler db = new DatabaseHandler(getContext());
        final Trip current = db.getTrip(mParam1);


        final EditText startDate = view.findViewById(R.id.start_date);
        final EditText endDate = view.findViewById(R.id.end_date);
        final Calendar now = Calendar.getInstance();

        //make sure the user cant directly interact with edittexts
        startDate.setKeyListener(null);
        endDate.setKeyListener(null);

        startDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {

                if(hasFocus) {
                    DatePickerDialog picker = new DatePickerDialog(getContext(), null,
                            now.get(Calendar.YEAR),
                            now.get(Calendar.MONTH),
                            now.get(Calendar.DAY_OF_MONTH));
                    picker.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                            Date date = new Date(Helper.formatDate(year, month, day));
                            current.setStartDate(date.getTime());
                            DatabaseHandler db = new DatabaseHandler(getContext());
                            db.updateTrip(current);
                            db.close();
                            startDate.setText(Helper.formatDate(date.getTime()));

                        }
                    });

                    picker.show();
                }
            }
        });

        endDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {

                if(hasFocus) {
                    DatePickerDialog picker = new DatePickerDialog(getContext(), null,
                            now.get(Calendar.YEAR),
                            now.get(Calendar.MONTH),
                            now.get(Calendar.DAY_OF_MONTH));
                    picker.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                            Date date = new Date(Helper.formatDate(year, month, day));
                            current.setEndDate(date.getTime());
                            DatabaseHandler db = new DatabaseHandler(getContext());
                            db.updateTrip(current);
                            db.close();
                            endDate.setText(Helper.formatDate(date.getTime()));
                        }
                    });

                    picker.show();
                }
            }
        });






        return view;
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
