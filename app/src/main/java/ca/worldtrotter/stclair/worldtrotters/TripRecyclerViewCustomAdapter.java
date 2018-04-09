package ca.worldtrotter.stclair.worldtrotters;


import android.app.FragmentTransaction;
import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import ca.worldtrotter.stclair.worldtrotters.Trip;

/**
 * Created by Dufour on 2018-03-27.
 */

public class TripRecyclerViewCustomAdapter extends RecyclerView.Adapter {
    private ArrayList<Trip> tripList;
    private FragmentManager fm;
    Context context;

    public TripRecyclerViewCustomAdapter(ArrayList<Trip> tripList, FragmentManager fm){
        this.tripList = tripList;
        this.fm = fm;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //when we create a viewholder we want to associate it to the xml element
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_trip_item, parent, false);


        final CustomViewHolder viewHolder = new CustomViewHolder(view);
        //We can add any onClickListners we want to trigger on the view here

        context = parent.getContext();
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        //Set all the items in the customviewHolder
        final Trip currentTrip = tripList.get(position);
        CustomViewHolder holder1 = ((CustomViewHolder) holder);
        ((CustomViewHolder) holder).tripName.setText(currentTrip.getName());
        ((CustomViewHolder) holder).image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.support.v4.app.FragmentTransaction transaction = fm.beginTransaction();
                transaction.replace(R.id.main_content, TripFragment.newInstance(currentTrip.getTripID()));
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        //grab the image location from the database and add the image to the imagaview
        DatabaseHandler db = new DatabaseHandler(context);
        Image image =  db.getImageForTrip(currentTrip.getTripID());
        String imagePath = "";
        if(image != null){
            imagePath = image.getImagePath();
        }
        //Log.d("IMAGE_PATH_FROM_DB", imagePath + " ");
        Picasso.get().load("file://" + imagePath).into(holder1.image);

    }

    @Override
    public int getItemCount() {
        if(tripList != null){
            return tripList.size();
        }
        return 0;
    }

    class CustomViewHolder extends RecyclerView.ViewHolder{
        protected TextView tripName;
        protected TextView photoAttribution;
        protected ImageView image;

        public CustomViewHolder(View view){
            super(view);
            this.tripName = view.findViewById(R.id.recycler_name);
            this.photoAttribution = view.findViewById(R.id.recycler_photo_attribution);
            this.image = view.findViewById(R.id.recycler_background_image);
        }
    }
}
