package ca.worldtrotter.stclair.worldtrotters;

import android.content.Context;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import com.rd.PageIndicatorView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AboutUsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AboutUsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AboutUsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    FragmentManager fm;

    public AboutUsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AboutUsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AboutUsFragment newInstance(String param1, String param2) {
        AboutUsFragment fragment = new AboutUsFragment();
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

        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        Window window = getActivity().getWindow();
        // finally change the color
        window.setStatusBarColor(ContextCompat.getColor(getContext(),R.color.colorPrimaryDark));

        //Hide the main FAB
        MainActivity.fab.hide();

        //change title
        getActivity().setTitle("About Us");

        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_about_us, container, false);

        //Import the ViewPager into the property viewPager
        ViewPager viewPager = view.findViewById(R.id.aboutViewPager);

        //Create a new custom adapter from the CustomPagerAdapter
        CustomPagerAdapter customPagerAdapter = new CustomPagerAdapter(getChildFragmentManager());

        //add a pageTransformer
        viewPager.setPageTransformer(true, new DrawFromBackTransformer());

        //Set the adapter to the viewpager
        viewPager.setAdapter(customPagerAdapter);

        //Link the pageIndicatorView
        PageIndicatorView pageIndicatorView = (PageIndicatorView) view.findViewById(R.id.pageIndicatorView);
        pageIndicatorView.setViewPager(viewPager);




        //Import the get started button in and set an onclick listener to go to the tripListFragment
        fm = getActivity().getSupportFragmentManager();
        Button getStarted = view.findViewById(R.id.getStartedButton);
        getStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction t = fm.beginTransaction();
                t.setCustomAnimations(R.anim.slide_in_left_fragment_animation, R.anim.slide_out_right_fragment_animation,
                        R.anim.slide_in_right, R.anim.slide_out_left);
                t.replace(R.id.main_content, new TripListFragment());
                t.addToBackStack(null);
                t.commit();

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

    /**
     *
     * @author Said Elshibiny
     * Creating the CustomPagerAdapter for the ViewPager
     */

    public class CustomPagerAdapter extends FragmentPagerAdapter {

        public CustomPagerAdapter(FragmentManager fm){
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            switch (position){
                case 0:
                    return NewInstanceFragment.newInstance(R.drawable.colosseum,"Rome, Italy", "The Roman Colosseum is located in the center of Rome, " +
                            "it was used for gladiatorial contests and it can hold up to 80,000 spectators.");
                case 1:
                    return NewInstanceFragment.newInstance(R.drawable.eiffel, "Paris, France", "The Eiffel Tower is located on the Champ de Mars in Paris and " +
                            "named after the engineer Gustave Eiffel. The iron tower is 324 metres tall.");
                case 2:
                    return NewInstanceFragment.newInstance(R.drawable.pyramids, "Giza, Egypt", "The Giza pyramids are located in the heart of Giza and " +
                            "they were built around 2500 years B.C. as tombs for the pharaohs (country's ruler).");
                case 3:
                    return NewInstanceFragment.newInstance(R.drawable.taj, "Agra, India", "Taj Mahal, is an ivory-white marble tomb" +
                            "located on south bank of the Yamuna river. It was built in 1653, in memory of Shah Jahan wife");
                case 4:
                    return NewInstanceFragment.newInstance(R.drawable.wall, "Huairou, China", "The Great Wall of China is series of fortifications" +
                            "made of stone. It's 21,196 km long and was built in 220 B.C to protect against invasions");
                default:
                    return NewInstanceFragment.newInstance(R.drawable.zion, "Utah", "Zion Park, located in Utah");
            }
        }

        @Override
        public int getCount() {
            //Return 5 Pages
            return 5;
        }
    }

    //Create the viewPager Transition
    public class DrawFromBackTransformer implements ViewPager.PageTransformer {

        private static final float MIN_SCALE = 0.75f;

        @Override
        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();

            if (position < -1 || position > 1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0);
                return;
            }

            if (position <= 0) { // [-1,0]
                // Use the default slide transition when moving to the left page
                // Fade the page out.
                view.setAlpha(1 + position);
                // Counteract the default slide transition
                view.setTranslationX(pageWidth * -position);

                // Scale the page down (between MIN_SCALE and 1)
                float scaleFactor = MIN_SCALE
                        + (1 - MIN_SCALE) * (1 - Math.abs(position));
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);
                return;

            }

            if (position > 0.5 && position <= 1) { // (0,1]
                // Fade the page out.
                view.setAlpha(0);

                // Counteract the default slide transition
                view.setTranslationX(pageWidth * -position);
                return;
            }
            if (position > 0.3 && position <= 0.5) { // (0,1]
                // Fade the page out.
                view.setAlpha(1);

                // Counteract the default slide transition
                view.setTranslationX(pageWidth * position);

                float scaleFactor = MIN_SCALE;
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);
                return;
            }
            if (position <= 0.3) { // (0,1]
                // Fade the page out.
                view.setAlpha(1);
                // Counteract the default slide transition
                view.setTranslationX(pageWidth * position);

                // Scale the page down (between MIN_SCALE and 1)
                float v = (float) (0.3 - position);
                v = v >= 0.25f ? 0.25f : v;
                float scaleFactor = MIN_SCALE + v;
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);
            }
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
