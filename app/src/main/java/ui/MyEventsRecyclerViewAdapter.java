package ui;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.parithyaga.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import EventManagement.MyEventsFragment;
import EventManagement.UpdateEvent;
import model.CharityEvent;

public class MyEventsRecyclerViewAdapter extends RecyclerView.Adapter<MyEventsRecyclerViewAdapter.ViewHolder>{

    private MyEventsFragment context;
    private List<CharityEvent> charitylist;

    public MyEventsRecyclerViewAdapter(MyEventsFragment context, List<CharityEvent> charitylist) {
        this.context = context;
        this.charitylist = charitylist;
    }

    @NonNull
    @Override
    public MyEventsRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_events_card_row, parent,false);

        return new ViewHolder(view, parent.getContext());
    }

    @Override
    public void onBindViewHolder(@NonNull MyEventsRecyclerViewAdapter.ViewHolder holder, int position) {
        CharityEvent charityEvent = charitylist.get(position);

        holder.eventName.setText(charityEvent.getEventName());
        holder.eventDesc.setText(charityEvent.getEventDesc());
        holder.eventloc.setText(charityEvent.getLocation());
        holder.eventcount.setText(String.valueOf(charityEvent.getParCount()));

        String timeAgo = (String) DateUtils.getRelativeTimeSpanString(charityEvent.getTimeadded().getSeconds() * 1000);
        holder.dateAdded.setText(timeAgo);
    }

    @Override
    public int getItemCount() {
        return charitylist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public CardView my_don_card;
        public TextView eventName;
        public TextView eventDesc;
        public TextView eventloc;
        public TextView eventcount;
        public TextView dateAdded;

        public ImageView locationtag;
        private FloatingActionButton view_event;
        String userId;
        String username;

        public ViewHolder(@NonNull View itemView,Context context) {
            super(itemView);

            eventName = itemView.findViewById(R.id.event_name_text);
            eventDesc = itemView.findViewById(R.id.event_desctext);
            eventloc = itemView.findViewById(R.id.event_locationview);
            eventcount = itemView.findViewById(R.id.prcount_view);
            locationtag = itemView.findViewById(R.id.location_sign);
            dateAdded = itemView.findViewById(R.id.event_timestamp_list);
            my_don_card = itemView.findViewById(R.id.my_events_card);

            my_don_card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CharityEvent charityEvents = charitylist.get(getAdapterPosition());
                    Intent intent = new Intent(context, UpdateEvent.class);
                    intent.putExtra("chairtyevent",charityEvents);
                    context.startActivity(intent);
                }
            });


        }
    }
}
