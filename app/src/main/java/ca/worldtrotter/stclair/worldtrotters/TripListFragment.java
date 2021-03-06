package ca.worldtrotter.stclair.worldtrotters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;

import java.util.ArrayList;

import jp.wasabeef.recyclerview.animators.SlideInDownAnimator;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TripListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TripListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TripListFragment extends Fragment {
    FragmentManager fm;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public TripListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TripListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TripListFragment newInstance(String param1, String param2) {
        TripListFragment fragment = new TripListFragment();
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
        View view = inflater.inflate(R.layout.fragment_trip_list, container, false);

        fm = getFragmentManager();

        //show the main fab button
        MainActivity.fab.show();
        MainActivity.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //fragment transaction new TripFragment
                FragmentTransaction transaction = fm.beginTransaction();
                transaction.setCustomAnimations(R.anim.slide_in_left_fragment_animation, R.anim.slide_out_right_fragment_animation, R.anim.slide_out_left, R.anim.slide_in_right);
                transaction.replace(R.id.main_content, new CreateTripFragment(), "recycler");
                transaction.addToBackStack("recycler");
                transaction.commit();
            }
        });

        getActivity().setTitle("Your Trips");
        //make some sample data to use
        ArrayList<Trip> tripList = new ArrayList<>();
        DatabaseHandler db = new DatabaseHandler(getActivity().getBaseContext());
        tripList = db.getAllTrips();
        db.close();


        //link the recycler view from XML
        RecyclerView recycler = view.findViewById(R.id.trips_recycler_view);
        //make a new custom adapter
        TripRecyclerViewCustomAdapter adapter = new TripRecyclerViewCustomAdapter(tripList, fm);
        //set the adapter
        recycler.setAdapter(adapter);
        //make a new linear layout manager
        LinearLayoutManager manager = new LinearLayoutManager(getContext()){
            @Override
            public boolean supportsPredictiveItemAnimations(){return true;}
        };
        //set the new layout manager
        recycler.setLayoutManager(manager);
        //set a new item animator
        recycler.setItemAnimator(new SlideInDownAnimator());

        if(tripList.size() == 0){
            TapTargetView.showFor(getActivity(),
                    TapTarget.forView(MainActivity.fab ,
                            "You dont have any trips!",
                            "Tap here to create your first trip!")
                            .outerCircleColor(R.color.secondaryDarkColor)
                            .targetCircleColor(R.color.secondaryColor)
                            .icon(getResources().getDrawable(R.drawable.ic_add_black_24dp)));
        }

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



