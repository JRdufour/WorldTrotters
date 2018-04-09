package ca.worldtrotter.stclair.worldtrotters;


import android.app.FragmentTransaction;
import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

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
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        //Set all the items in the customviewHolder
        final Trip currentTrip = tripList.get(position);
        ((CustomViewHolder) holder).tripName.setText(currentTrip.getName());
//        ((CustomViewHolder)holder).image.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                android.support.v4.app.FragmentTransaction transaction = fm.beginTransaction();
//                transaction.replace(R.id.main_content, TripFragment.newInstance(currentTrip.getTripID()));
//                transaction.addToBackStack(null);
//                transaction.commit();
//            }
//        });

        ((CustomViewHolder) holder).menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //creating a popup menu
                PopupMenu popup = new PopupMenu(context, ((CustomViewHolder) holder).menu);
                //inflating menu from xml resource
                popup.inflate(R.menu.mymenu);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_edit:
                                android.support.v4.app.FragmentTransaction transaction = fm.beginTransaction();
                                transaction.replace(R.id.main_content, TripFragment.newInstance(currentTrip.getTripID()));
                                transaction.addToBackStack(null);
                                transaction.commit();
                                break;
                            case R.id.action_delete:
                                //handle menu2 click
                                break;
                            case R.id.action_completed:
                                //handle menu3 click
                                break;
                        }
                        return false;
                    }
                });

                //displaying the popup
                popup.show();
            }
        });
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
        protected ImageView menu;

        public CustomViewHolder(View view){
            super(view);
            this.tripName = view.findViewById(R.id.recycler_name);
            this.photoAttribution = view.findViewById(R.id.recycler_photo_attribution);
            this.image = view.findViewById(R.id.recycler_background_image);
            this.menu = view.findViewById(R.id.optionsMenu);
        }
    }
}
