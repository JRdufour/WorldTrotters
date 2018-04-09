package ca.worldtrotter.stclair.worldtrotters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Dufour on 2018-04-03.
 */

public class DestinationRecyclerViewAdapter extends RecyclerView.Adapter {

    ArrayList<Destination> destinationArrayList;

    Context context;

    public ArrayList<Destination> getDestinationArrayList() {
        return destinationArrayList;
    }

    public void setDestinationArrayList(ArrayList<Destination> destinationArrayList) {
        this.destinationArrayList = destinationArrayList;
    }


    public DestinationRecyclerViewAdapter(ArrayList<Destination> destinationArrayList) {
        this.destinationArrayList = destinationArrayList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_destination_item, parent, false);
        final CustomViewHolder viewHolder = new CustomViewHolder(view);

        context = parent.getContext();
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final Destination current = destinationArrayList.get(position);

        ((CustomViewHolder) holder).destinationName.setText(current.getName());

        //add the functionality to remove a destination from the trip


//        ((CustomViewHolder) holder).cancelButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //TODO add popup to confirm
//                int pos = holder.getAdapterPosition();
//                destinationArrayList.remove(pos);
//                notifyItemRemoved(pos);
//
//                DatabaseHandler db = new DatabaseHandler(context);
//                db.deleteDestination(current.getId());
//                db.close();
//
//            }
//        });

        //handel the user adding start times and end times for their trip
        ((CustomViewHolder) holder).startDateTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(!hasFocus){
                    //the user has left focus of this text field
                    String startDate = ((CustomViewHolder)holder).startDateTime.getText().toString().trim();
                    if( startDate != null){
                        //there is an input
                        current.setStartDateTime(startDate);
                    }
                }
            }
        });


        ((CustomViewHolder) holder).endDateTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(!hasFocus){
                    //the user has left focus of this text field
                    String startDate = ((CustomViewHolder)holder).endDateTime.getText().toString().trim();
                    if( startDate != null){
                        //there is an input
                        current.setEndDateTime(startDate);
                    }
                }
            }
        });

        //set up the list view that is going to hold the toDoItems
        final ArrayList<String> toDoItemValues = new ArrayList<>();
//        toDoItemValues.add("Eat Burger");
//        toDoItemValues.add("See Sights");
//        toDoItemValues.add("Get a tattoo");

        if(toDoItemValues.size() == 0){
            ((CustomViewHolder) holder).list.setVisibility(View.GONE);
        }
        //create a new array adapter for the list items
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_list_item_1, android.R.id.text1, toDoItemValues);

        //set the adapter to the listview
        ((CustomViewHolder)holder).toDoItemListView.setAdapter(adapter);


        ((CustomViewHolder)holder).addAgendaItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //handel the action for the add agenda item button
                toDoItemValues.add("Test");
                adapter.notifyDataSetChanged();
            }
        });

        Picasso.get().load("file://" + current.getImagePath()).into(((CustomViewHolder) holder).backgroundImage);
    }


    @Override
    public int getItemCount() {
        return destinationArrayList.size();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView destinationName;
        protected TextView startDateTime;
        protected TextView endDateTime;
        protected ImageView showMoreButton;
        protected ImageView addAgendaItemButton;
        protected MyListView toDoItemListView;
        protected ImageView backgroundImage;
        protected MyListView list;

        public CustomViewHolder(View view) {
            super(view);

            destinationName = view.findViewById(R.id.destination_name_text);
            showMoreButton = view.findViewById(R.id.destination_show_more_button);
            startDateTime = (EditText) view.findViewById(R.id.add_trip_start_date);
            endDateTime = (EditText) view.findViewById(R.id.add_trip_end_date);
            addAgendaItemButton = view.findViewById(R.id.add_agenda_item_button);
            toDoItemListView = view.findViewById(R.id.to_do_item_list_view);
            backgroundImage = view.findViewById(R.id.destination_background_image);
            list = view.findViewById(R.id.to_do_item_list_view);
        }
    }

}