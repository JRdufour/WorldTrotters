package ca.worldtrotter.stclair.worldtrotters;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;


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
        localHolder.endDateTime.setKeyListener(null);
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
        final ArrayList<String> toDoItemValues = new ArrayList<>();
        toDoItemValues.add("Eat Burger");
        toDoItemValues.add("See Sights");
        toDoItemValues.add("Get a tattoo");


        //create a new array adapter for the list items
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_list_item_1, android.R.id.text1, toDoItemValues);

        //set the adapter to the listview
        localHolder.toDoItemListView.setAdapter(adapter);

        localHolder.addAgendaItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //handel the action for the add agenda item button
                toDoItemValues.add("Test");
                adapter.notifyDataSetChanged();
            }
        });

        DatabaseHandler db = new DatabaseHandler(context);
        Image image = db.getImage(current.getPlaceId());

        if(image != null) {
            String path = image.getImagePath();
            Picasso.get().load("file://" + path).into(((CustomViewHolder) holder).backgroundImage);
        }


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
                                /** EDIT DATES**/
                            case R.id.destination_menu_edit_dates:
                                //handle editing dates
                                editDates(current);
                                break;
                            /** TOGGLE AGENDA**/
                            case R.id.destination_menu_agenda:
                                //agenda was set to add
                                //TODO change menu item text to the remove agneda item text
                                Log.d("TEST", "AGENDA PRESSED");

                                localHolder.line.setVisibility(View.VISIBLE);
                                localHolder.gridLayout.setVisibility(View.VISIBLE);
                                localHolder.toDoItemListView.setVisibility(View.VISIBLE);

//                                if (localHolder.line.getVisibility() == View.INVISIBLE || localHolder.line.getVisibility() == View.GONE) {
//                                    localHolder.line.setVisibility(View.GONE);
//                                    localHolder.gridLayout.setVisibility(View.GONE);
//                                    localHolder.toDoItemListView.setVisibility(View.GONE);
//                                } else {
//                                    localHolder.line.setVisibility(View.VISIBLE);
//                                    localHolder.gridLayout.setVisibility(View.VISIBLE);
//                                    localHolder.toDoItemListView.setVisibility(View.VISIBLE);
//                                }


                            case R.id.destination_menu_explore:
                                //handle exploring
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
        final Calendar now = Calendar.getInstance();

        DatePickerDialog picker = new DatePickerDialog(context, null,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH));
        picker.setMessage("Select Start Date");
        picker.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                Date date = new Date(Helper.formatDate(year, month, day));
                dest.setStartDateTime(date.getTime());
                final DatePickerDialog picker2 = new DatePickerDialog(context, null,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH));
                picker2.setMessage("Select End Date");
                picker2.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        Date date = new Date(Helper.formatDate(i, i1, i2));
                        dest.setEndDateTime(date.getTime());
                        DatabaseHandler db = new DatabaseHandler(context);
                        db.upDateDestination(dest);
                        db.close();
                        notifyDataSetChanged();
                    }
                });
                picker2.show();
            }
        });
        picker.show();
    }


}