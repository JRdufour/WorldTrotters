package ca.worldtrotter.stclair.worldtrotters;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Dufour on 2018-04-03.
 */

public class DestinationRecyclerViewAdapter extends RecyclerView.Adapter {

    ArrayList<Destination> destinationArrayList;
    Context context;
    private boolean agendaShown = false;

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
        //grab a local copy og the current destination
        final Destination current = destinationArrayList.get(position);
        //grab a local copy of the viewholder we're on
        final CustomViewHolder localHolder = (CustomViewHolder) holder;


        //set the name of the destination
        localHolder.destinationName.setText(current.getName());
        //turn off the edit texts for start and end dates
        localHolder.startDateTime.setKeyListener(null);
        localHolder.startDateTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editStartDate(current);
                notifyDataSetChanged();
            }
        });
        localHolder.endDateTime.setKeyListener(null);
        localHolder.endDateTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editEndDate(current);
            }
        });
        localHolder.startDateTime.setFocusable(false);
        localHolder.endDateTime.setFocusable(false);
        //hide the agenda components
        localHolder.line.setVisibility(View.GONE);
        localHolder.gridLayout.setVisibility(View.GONE);
        localHolder.toDoItemListView.setVisibility(View.GONE);

        if(current.getStartDateTime() != 0) {
            localHolder.startDateTime.setText(Helper.formatDate(current.getStartDateTime(), "MMMM dd"));
        }
        if(current.getEndDateTime() != 0) {
            localHolder.endDateTime.setText(Helper.formatDate(current.getEndDateTime(), "MMMM dd"));
        }
        //set up the list view that is going to hold the toDoItems
        final ArrayList<ToDoItem> toDoItemValues;
        DatabaseHandler db = new DatabaseHandler(context);
        toDoItemValues = db.getAllToDoItems(current.getId());

        //create a new array adapter for the list items
        final ArrayAdapter<ToDoItem> adapter = new ArrayAdapter<ToDoItem>(context,
                android.R.layout.simple_list_item_1, android.R.id.text1, toDoItemValues);

        //set the adapter to the listview
        localHolder.toDoItemListView.setAdapter(adapter);
        localHolder.toDoItemListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                DatabaseHandler db = new DatabaseHandler(context);
                ToDoItem current = toDoItemValues.get(position);
                toDoItemValues.remove(current);
                db.deleteToDoItem(current.getId());
                adapter.notifyDataSetChanged();
                db.close();
                return false;
            }
        });

        localHolder.addAgendaItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //handel the action for the add agenda item button
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Add Agenda Item");
                final EditText inputName = new EditText(context);
                builder.setView(inputName);
                builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String agendaItem = inputName.getText().toString();
                        if(!agendaItem.matches("")){
                            DatabaseHandler db = new DatabaseHandler(context);
                            ToDoItem item = new ToDoItem(current.getId(), agendaItem, null);
                            db.addToDoItem(item);
                            db.close();
                            toDoItemValues.add(item);
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
                builder.setNegativeButton("Cancel", null);
                builder.show();
                adapter.notifyDataSetChanged();
            }
        });

        Image image = db.getImage(current.getPlaceId());

        if(image != null) {
            String path = image.getImagePath();
            Picasso.with(context).load("file://" + path).into(((CustomViewHolder) holder).backgroundImage);
        }

        /** This is the menu for each Destination item **/

        localHolder.menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu menu = new PopupMenu(context, view);
                MenuInflater inflater = menu.getMenuInflater();

                inflater.inflate(R.menu.destination_popup, menu.getMenu());
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            /** DELETE **/
                            case R.id.destination_menu_delete:
                                //handel deleting the item
                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                builder.setTitle("Delete Destination");
                                builder.setMessage("Are you sure you want to delete " + current.getName() + "?");
                                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        if (destinationArrayList.size() != 1) {
                                            //handle deleting the current trip
                                            DatabaseHandler db = new DatabaseHandler(context);
                                            db.deleteDestination(current.getId());
                                            int pos = destinationArrayList.indexOf(current);
                                            destinationArrayList.remove(current);
                                            notifyItemRemoved(pos);
                                            db.close();
                                        } else {
                                            Toast.makeText(context, "You must have at least one destination", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                                builder.setNegativeButton("No", null);
                                builder.show();


                                break;
                            /** TOGGLE AGENDA**/
                            case R.id.destination_menu_agenda:
                                //agenda was set to add
                                //TODO change menu item text to the remove agneda item text
                                Log.d("TEST", "AGENDA PRESSED");

                                if (!agendaShown){
                                    localHolder.line.setVisibility(View.VISIBLE);
                                    localHolder.gridLayout.setVisibility(View.VISIBLE);
                                    localHolder.toDoItemListView.setVisibility(View.VISIBLE);
                                    agendaShown = true;
                                 } else {
                                       localHolder.line.setVisibility(View.GONE);
                                       localHolder.gridLayout.setVisibility(View.GONE);
                                       localHolder.toDoItemListView.setVisibility(View.GONE);
                                        agendaShown = false;
                                }
                                break;

                            case R.id.destination_menu_clear_dates:
                                clearDates(current);

                            case R.id.destination_menu_explore:
                                //handle exploring
                                exploreDestination(current);
                                break;

                            default:
                        }

                        return false;
                    }
                });
                menu.show();
            }
        });
    }


    @Override
    public int getItemCount() {
        return destinationArrayList.size();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView destinationName;
        protected TextView startDateTime;
        protected TextView endDateTime;
        protected ImageView menuButton;
        protected ImageView addAgendaItemButton;
        protected MyListView toDoItemListView;
        protected ImageView backgroundImage;
        protected GridLayout gridLayout;
        protected View line;


        public CustomViewHolder(View view) {
            super(view);

            destinationName = view.findViewById(R.id.destination_name_text);
            menuButton = view.findViewById(R.id.destination_menu_button);
            startDateTime = (EditText) view.findViewById(R.id.add_trip_start_date);
            endDateTime = (EditText) view.findViewById(R.id.add_trip_end_date);
            addAgendaItemButton = view.findViewById(R.id.add_agenda_item_button);
            toDoItemListView = view.findViewById(R.id.to_do_item_list_view);
            backgroundImage = view.findViewById(R.id.destination_background_image);
            gridLayout = view.findViewById(R.id.gridLayout);
            line = view.findViewById(R.id.view);

        }
    }

    private void editDates(final Destination dest){
        editEndDate(dest);
        editStartDate(dest);

    }

    private void editStartDate(final Destination dest){


        Calendar now = Calendar.getInstance();
        if(dest.getStartDateTime() != 0){
            now.setTime(new Date(dest.getStartDateTime()));
        } else {
            int position = destinationArrayList.indexOf(dest);
            if(position != 0) {
                Destination previous = destinationArrayList.get(position - 1);
                long previousDestionationEndDate = previous.getEndDateTime();
                if(previousDestionationEndDate != 0){
                    now.setTime(new Date(previousDestionationEndDate));
                }
            }

        }

        final DatabaseHandler db = new DatabaseHandler(context);
        DatePickerDialog picker = new DatePickerDialog(context, null,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH));
        picker.setMessage("Select Start Date");


        picker.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                Date date = new Date(Helper.formatDate(year, month, day));
                dest.setStartDateTime(date.getTime());
                db.upDateDestination(dest);
                db.close();
                notifyDataSetChanged();
            }
        });
        picker.show();
    }

    private void editEndDate(final Destination dest){
        Calendar now = Calendar.getInstance();
        if(dest.getEndDateTime() != 0){
            now.setTime(new Date(dest.getEndDateTime()));
        }else if (dest.getStartDateTime() != 0){
            now.setTime(new Date(dest.getStartDateTime()));
        }
        final DatabaseHandler db = new DatabaseHandler(context);
        DatePickerDialog picker = new DatePickerDialog(context, null,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH));
        picker.setMessage("Select End Date");


        picker.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                Date date = new Date(Helper.formatDate(year, month, day));
                dest.setEndDateTime(date.getTime());
                db.upDateDestination(dest);
                db.close();
                notifyDataSetChanged();
            }
        });
        picker.show();
    }

    private void clearDates(Destination dest){
        DatabaseHandler db = new DatabaseHandler(context);
        dest.setStartDateTime(0);
        dest.setEndDateTime(0);
        db.upDateDestination(dest);
        db.close();
        notifyDataSetChanged();
    }

    private void exploreDestination(Destination dest){
        String id = dest.getPlaceId();
        GoogleApiClient client = MainActivity.googleClient;

        if(TextUtils.isEmpty(id) || client == null || !client.isConnected()) {

        }else {

            Places.GeoDataApi.getPlaceById(client, id).setResultCallback(new ResultCallback<PlaceBuffer>() {
                @Override
                public void onResult(@NonNull PlaceBuffer places) {
                    if (places.getStatus().isSuccess()) {
                        Place place = places.get(0);
                        double lat = place.getLatLng().latitude;
                        double lon = place.getLatLng().longitude;

                        String intentString = "geo:" + lat + "," + lon;
                        Uri intentUri = Uri.parse(intentString);
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, intentUri);
                        //mapIntent.setPackage("com.google.android.apps.maps");
                        if(mapIntent.resolveActivity(context.getPackageManager()) != null) {
                            context.startActivity(mapIntent);
                        }


                    }
                    // release the PlaceBuffer to prevent a memory leak
                    places.release();
                }
            });
        }
    }


}
