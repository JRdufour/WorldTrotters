package ca.worldtrotter.stclair.worldtrotters;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Animatable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.Html;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

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
        context = parent.getContext();

        final CustomViewHolder viewHolder = new CustomViewHolder(view);
        //We can add any onClickListners we want to trigger on the view here


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        //Set all the items in the customviewHolder
        final Trip currentTrip = tripList.get(position);
        CustomViewHolder holder1 = ((CustomViewHolder) holder);
        ((CustomViewHolder) holder).tripName.setText(currentTrip.getName());
        ((CustomViewHolder)holder).image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment f = TripFragment.newInstance(currentTrip.getTripID(), false);
                android.support.v4.app.FragmentTransaction transaction = fm.beginTransaction();
                transaction.setCustomAnimations(R.anim.slide_in_left_fragment_animation, R.anim.slide_out_right_fragment_animation, R.anim.slide_in_right, R.anim.slide_out_left);
                transaction.replace(R.id.main_content, TripFragment.newInstance(currentTrip.getTripID(), false));
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });




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
                                //handle letting the user edit the trip's name
                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                final EditText input = new EditText(context);
                                input.setText(currentTrip.getName());
                                builder.setView(input);
                                builder.setTitle("Update Trip's Name");
                                builder.setMessage("Are you sure you want to update the trip's name?")
                                        .setIcon(R.drawable.ic_error_black_24dp);
                                builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //grab the input and store it inside the variable tripName
                                        String tripName = input.getText().toString();
                                        //Grab the database
                                        DatabaseHandler db = new DatabaseHandler(context);
                                        //check if the string is not a number or empty
                                        if(tripName == null || tripName == " " || tripName.isEmpty()){
                                            Toast.makeText(context, "Please enter a valid name", Toast.LENGTH_SHORT).show();
                                        }else{
                                            //update the trips name
                                            currentTrip.setName(tripName);
                                            //update the trip in the db
                                            db.updateTrip(currentTrip);
                                            //update the tripname on the cardview
                                            notifyDataSetChanged();
                                        }
                                    }
                                });
                                        builder.setNegativeButton("Cancel",null);
                                builder.show();
                                break;
                            case R.id.action_delete:
                                new AlertDialog.Builder(context)
                                        .setTitle("Delete Trip")
                                        .setMessage("Are you sure you want to delete this trip?")
                                        .setIcon(R.drawable.ic_error_black_24dp)
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                //Grab the trip in the array list
                                                int theTrip = holder.getAdapterPosition();
                                                //Grab the database
                                                DatabaseHandler db = new DatabaseHandler(context);
                                                //Delete the trip from the database
                                                //Grab the trip from the tripList array list
                                                //Grab that trip ID and delete it from the database
                                                db.deleteTrip(tripList.get(theTrip).getTripID());
                                                int position = tripList.indexOf(theTrip);
                                                //Also delete the object from the ArrayList
                                                tripList.remove(theTrip);
                                                //Refresh the RecyclerView
                                                notifyItemRemoved(theTrip);
                                            }
                                        })

                                        .setNegativeButton("No", null)
                                        .show();
                                break;
                            case R.id.action_completed:
                                Toast.makeText(context, "Trip Marked as Completed", Toast.LENGTH_SHORT).show();
                                break;
                        }
                        return false;
                    }
                });

                //displaying the popup
                popup.show();
            }
        });

        //grab the image location from the database and add the image to the imagaview
        DatabaseHandler db = new DatabaseHandler(context);
        Image image =  db.getImageForTrip(currentTrip.getTripID());
        String imagePath = "";
        if(image != null){
            imagePath = image.getImagePath();

            //Log.d("IMAGE_PATH_FROM_DB", imagePath + " ");
            Picasso.get().load("file://" + imagePath).into(holder1.image);
            holder1.photoAttribution.setText("Photo: " + Html.fromHtml(image.getAttribution(), 0));
        }


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
