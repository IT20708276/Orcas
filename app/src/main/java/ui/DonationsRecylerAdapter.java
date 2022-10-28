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

import DonationManagement.UpdateDonation;
import DonationManagement.UpdateDonationAmount;
import model.Donations;

public class DonationsRecylerAdapter extends RecyclerView.Adapter<DonationsRecylerAdapter.ViewHolder> {
    private Context context;
    private List<Donations> donationsList;

    public DonationsRecylerAdapter(Context context, List<Donations> donationsList) {
        this.context = context;
        this.donationsList = donationsList;
    }

    @NonNull
    @Override
    public DonationsRecylerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.donation_row,parent,false);

        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull DonationsRecylerAdapter.ViewHolder holder, int position) {
        Donations donations = donationsList.get(position);

        holder.did_text.setText(String.valueOf(donations.getDid()).trim());
        holder.orgName.setText(donations.getOrgName().trim());
        String contactNumber = "+(94)"+String.valueOf(donations.getDoncontactNumber());
        holder.don_contact.setText(contactNumber.trim());
        String donationList = donations.getDonationList();
        String donationAmount = donations.getDonationAmount();

    }

    @Override
    public int getItemCount() {
        return donationsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView did_text;
        private TextView orgName;
        private TextView don_contact;
        private TextView don_list;
        private TextView don_amount;

       private Button updateDonation;
       private Button updateAmount;


        public ViewHolder(@NonNull View itemView,Context ctx) {
            super(itemView);
            context = ctx;
            did_text = itemView.findViewById(R.id.did_text);
            orgName = itemView.findViewById(R.id.card_number_text);
            don_contact = itemView.findViewById(R.id.contact_don_text);
            updateDonation = itemView.findViewById(R.id.update_this_card);
            updateAmount = itemView.findViewById(R.id.update_don_amount);

            updateDonation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Donations donations = donationsList.get(getAdapterPosition());
                    Intent intent = new Intent(context, UpdateDonation.class);
                    intent.putExtra("thisdonation",donations);
                    context.startActivity(intent);
                }
            });

            updateAmount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Donations donations = donationsList.get(getAdapterPosition());
                    Intent intent = new Intent(context, UpdateDonationAmount.class);
                    intent.putExtra("thisdonAmount",donations);
                    context.startActivity(intent);
                }
            });
        }
    }
}
