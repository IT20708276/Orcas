package ui;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.parithyaga.R;

import java.util.List;

import DeliveryManagement.PendingDelivery;
import DeliveryManagement.PendingInsert;
import model.Donations;

public class PendingDelRecyclerViewAdapter extends RecyclerView.Adapter<PendingDelRecyclerViewAdapter.PendingDelViewHolder> {
   private PendingDelivery context;
   private List<Donations> pendingdel;

    public PendingDelRecyclerViewAdapter(PendingDelivery context, List<Donations> pendingdel) {
        this.context = context;
        this.pendingdel = pendingdel;
    }



    @NonNull
    @Override
    public PendingDelRecyclerViewAdapter.PendingDelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pending_del_row,parent,false);

        return new PendingDelViewHolder(view, parent.getContext());
    }

    @Override
    public void onBindViewHolder(@NonNull PendingDelRecyclerViewAdapter.PendingDelViewHolder holder, int position) {
        Donations penddonations = pendingdel.get(position);

        holder.donName.setText(penddonations.getOrgName());
        holder.donlocation.setText(penddonations.getOrglocation());
        holder.doncontacts.setText(String.valueOf(penddonations.getDoncontactNumber()));
        String pendinglist = penddonations.getDonationList();
        String pendingId = String.valueOf(penddonations.getDid());

    }

    @Override
    public int getItemCount() {
        return pendingdel.size();
    }

    public class PendingDelViewHolder extends RecyclerView.ViewHolder {

        private TextView donName;
        private TextView donlocation;
        private TextView doncontacts;

        private Button viewdel;
        private Button add_to_pickUp;

        public PendingDelViewHolder(@NonNull View itemView,Context context) {
            super(itemView);

            donName = itemView.findViewById(R.id.pend_del_name);
            donlocation = itemView.findViewById(R.id.update_del_location);
            doncontacts = itemView.findViewById(R.id.del_contacts);

            viewdel = itemView.findViewById(R.id.view_del);
            add_to_pickUp = itemView.findViewById(R.id.add_to_pick);

            add_to_pickUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Donations pendingDon = pendingdel.get(getAdapterPosition());

                    Intent intent = new Intent(context, PendingInsert.class);
                    intent.putExtra("pendingDetails",pendingDon);
                    intent.putExtra("pendinglocation",pendingDon.getOrglocation());
                    intent.putExtra("pendDonList",pendingDon.getDonationList());
                    intent.putExtra("pendingName",pendingDon.getOrgName());
                    context.startActivity(intent);
                }
            });

        }
    }
}
