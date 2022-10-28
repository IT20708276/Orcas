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

import DeliveryManagement.PickUpDelivery;
import DeliveryManagement.PickUpDetails;
import model.Delivery;

public class PickupDeliveryRecyclerViewAdapter extends RecyclerView.Adapter<PickupDeliveryRecyclerViewAdapter.PickUpViewHolder> {
    private PickUpDelivery context;
    private List<Delivery> deliveryList;

    public PickupDeliveryRecyclerViewAdapter(PickUpDelivery context, List<Delivery> deliveryList) {
        this.context = context;
        this.deliveryList = deliveryList;
    }

    @NonNull
    @Override
    public PickupDeliveryRecyclerViewAdapter.PickUpViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pickup_del_row,parent,false);

        return new PickUpViewHolder(view,parent.getContext());
    }

    @Override
    public void onBindViewHolder(@NonNull PickupDeliveryRecyclerViewAdapter.PickUpViewHolder holder, int position) {
        Delivery pickupdel = deliveryList.get(position);
        holder.pickName.setText(pickupdel.getDel_name());
        holder.pickLoc.setText(pickupdel.getDel_location());
        holder.pickTime.setText(pickupdel.getPickupTime());
        String donationList = pickupdel.getDel_list();

    }

    @Override
    public int getItemCount() {
        return deliveryList.size();
    }

    public class PickUpViewHolder extends RecyclerView.ViewHolder {

        private TextView pickName;
        private TextView pickLoc;
        private TextView pickTime;

        private Button viewpick;
        public PickUpViewHolder(@NonNull View itemView, Context context) {
            super(itemView);

            pickName = itemView.findViewById(R.id.pick_del_name);
            pickLoc = itemView.findViewById(R.id.pickup_location);
            pickTime = itemView.findViewById(R.id.pickup_time);

            viewpick = itemView.findViewById(R.id.view_del);

            viewpick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Delivery pickupdel = deliveryList.get(getAdapterPosition());

                    Intent intent = new Intent(context, PickUpDetails.class);
                    intent.putExtra("PickupDel",pickupdel);
                    intent.putExtra("del_status",pickupdel.getDel_status());
                    intent.putExtra("pickupList",pickupdel.getDel_list());
                    intent.putExtra("pickupName",pickupdel.getDel_name());
                    intent.putExtra("pickuplocation",pickupdel.getDel_location());
                    intent.putExtra("pickuptime",pickupdel.getPickupTime());
                    context.startActivity(intent);
                }
            });

        }
    }
}
