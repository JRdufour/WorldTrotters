package ca.worldtrotter.stclair.worldtrotters;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NewInstanceFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewInstanceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

/**
 * @author Said Elshibiny
 * This fragment acts as the newInstance that helps in poopualting the ViewPager
 */


public class NewInstanceFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";

    // TODO: Rename and change types of parameters
    private int mParam1;
    private String mParam2;
    private String mParam3;

    private OnFragmentInteractionListener mListener;

    public NewInstanceFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @param param3 Parameter 3.
     * @return A new instance of fragment NewInstanceFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewInstanceFragment newInstance(int param1, String param2, String param3) {
        NewInstanceFragment fragment = new NewInstanceFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putString(ARG_PARAM3, param3);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getInt(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            mParam3 = getArguments().getString(ARG_PARAM3);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        /**
         * @author Said Elshibiny
         * Separating the view and using mParam1 to change the ImageView in the ViewPager
         */

        //Separate the view
        View view = inflater.inflate(R.layout.fragment_new_instance, container, false);

        //If mParam1 not empty then populate
        if(mParam1 != 0){
            ImageView image = (ImageView) view.findViewById(R.id.viewPagerImage);
            image.setImageResource(mParam1);
        }

        //If mParam2 not empty then populate
        if(mParam2 != null){
            TextView textView = (TextView) view.findViewById(R.id.viewPagerTitle);
            textView.setText(mParam2);
        }

        //If mParam2 not empty then populate
        if(mParam3 != null){
            TextView textView = (TextView) view.findViewById(R.id.viewPagerDescription);
            textView.setText(mParam3);
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
