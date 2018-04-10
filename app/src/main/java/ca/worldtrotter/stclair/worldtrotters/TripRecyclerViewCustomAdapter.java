package ca.worldtrotter.stclair.worldtrotters;


import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

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

        final EditText input = new EditText(context);
        input.setHint("Enter new trip name");
        input.setInputType(InputType.TYPE_CLASS_TEXT);

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
                                new AlertDialog.Builder(context)
                                .setView(input)
                                .setTitle("Update Trip Name")
                                .setMessage("Are you sure you want to update the trip's name?")
                                        .setIcon(R.drawable.ic_error_black_24dp)
                                .setPositiveButton("Cancel",null)
                                .setNegativeButton("Update", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        ((CustomViewHolder) holder).tripName.setText(input.getText().toString());
                                    }
                                })
                                .show();
                                break;
                            case R.id.action_delete:
                                new AlertDialog.Builder(context)
                                        .setTitle("Delete Trip")
                                        .setMessage("Are you sure you want to delete this trip?")
                                        .setIcon(R.drawable.ic_error_black_24dp)
                                        .setPositiveButton("No", null)
                                        .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                //Grab the trip in the array list
                                                int theTrip = holder.getAdapterPosition();
                                                //Grab the database
                                                DatabaseHandler db = new DatabaseHandler(context);
                                                //Delete the trip from the database
                                                //Grab the trip from the locations array list
                                                //Grab that trip ID and delete it from the database
                                                db.deleteTrip(tripList.get(theTrip).getTripID());
                                                //Also delete the object from the ArrayList
                                                tripList.remove(theTrip);
                                                //Refresh the RecyclerView
                                                notifyItemRemoved(theTrip);
                                            }
                                        })
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
