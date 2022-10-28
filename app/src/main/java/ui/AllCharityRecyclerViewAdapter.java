package ui;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.parithyaga.R;

import java.util.List;

import EventManagement.All_EventsFragment;
import EventManagement.EventDetails;
import model.CharityEvent;

public class AllCharityRecyclerViewAdapter extends RecyclerView.Adapter<AllCharityRecyclerViewAdapter.AllEventViewHolder> {

    private All_EventsFragment context;
    private List<CharityEvent> allcharityEventList;

    public AllCharityRecyclerViewAdapter(All_EventsFragment context, List<CharityEvent> allcharityEventList) {
        this.context = context;
        this.allcharityEventList = allcharityEventList;
    }

    @NonNull
    @Override
    public AllCharityRecyclerViewAdapter.AllEventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.all_event_row,parent,false);

        return new AllEventViewHolder(view, parent.getContext());
    }

    @Override
    public void onBindViewHolder(@NonNull AllCharityRecyclerViewAdapter.AllEventViewHolder holder, int position) {

        CharityEvent charityEvent = allcharityEventList.get(position);

        holder.alleventName.setText(charityEvent.getEventName());
        holder.alleventDesc.setText(charityEvent.getEventDesc());
        holder.alleventloc.setText(charityEvent.getLocation());
        holder.alleventcount.setText(String.valueOf(charityEvent.getParCount()));
        holder.allcontacts.setText("0"+String.valueOf(charityEvent.getPhoneNumber()));
        String timeAgo = (String) DateUtils.getRelativeTimeSpanString(charityEvent.getTimeadded().getSeconds() * 1000);
        holder.alldateAdded.setText(timeAgo);
        holder.allname.setText(charityEvent.getUserName());

    }


    @Override
    public int getItemCount() {
        return allcharityEventList.size();
    }

    public class AllEventViewHolder extends RecyclerView.ViewHolder {
        public TextView alleventName;
        public TextView alleventDesc;
        public TextView alleventloc;
        public TextView alleventcount;
        public TextView alldateAdded;
        public TextView allcontacts;
        public TextView allname;
        String userId;
        String username;
        public AllEventViewHolder(@NonNull View itemView,Context context) {
            super(itemView);

            alleventName = itemView.findViewById(R.id.all_event_name_text);
            alleventDesc = itemView.findViewById(R.id.all_event_desctext);
            alleventloc = itemView.findViewById(R.id.all_event_locationview);
            alleventcount = itemView.findViewById(R.id.all_prcount_view);
            alldateAdded = itemView.findViewById(R.id.all_event_timestamp_list);
            allcontacts = itemView.findViewById(R.id.all_contact);
            allname = itemView.findViewById(R.id.all_usersname);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CharityEvent allcharityEvent = allcharityEventList.get(getAdapterPosition());
                    Intent intent = new Intent(context, EventDetails.class);
                    intent.putExtra("allchairtyevent",allcharityEvent);
                    intent.putExtra("SponsrAmount",allcharityEvent.getSponsrAmount());
                    context.startActivity(intent);
                }
            });


        }



    }
}