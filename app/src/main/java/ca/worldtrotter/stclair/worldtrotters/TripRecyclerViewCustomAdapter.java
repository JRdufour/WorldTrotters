package ca.worldtrotter.stclair.worldtrotters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import ca.worldtrotter.stclair.worldtrotters.Trip;

/**
 * Created by Dufour on 2018-03-27.
 */

public class TripRecyclerViewCustomAdapter extends RecyclerView.Adapter {
    private ArrayList<Trip> tripList;
    Context context;

    public TripRecyclerViewCustomAdapter(ArrayList<Trip> tripList){
        this.tripList = tripList;
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
        Trip currentTrip = tripList.get(position);
        ((CustomViewHolder) holder).tripName.setText(currentTrip.getName());
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
