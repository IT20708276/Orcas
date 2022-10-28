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

import DonationManagement.DonationDetails;
import DonationManagement.DonationListFragment;
import model.Donations;

public class UserDonationsRecyclerAdpater extends RecyclerView.Adapter<UserDonationsRecyclerAdpater.ViewHolder> {
    private DonationListFragment context;
    private List<Donations> userdonations;

    public UserDonationsRecyclerAdpater(DonationListFragment context, List<Donations> userdonations) {
        this.context = context;
        this.userdonations = userdonations;
    }


    @NonNull
    @Override
    public UserDonationsRecyclerAdpater.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.all_donations_row,parent,false);
        return new ViewHolder(view, parent.getContext());

    }

    @Override
    public void onBindViewHolder(@NonNull UserDonationsRecyclerAdpater.ViewHolder holder, int position) {
        Donations alldonations = userdonations.get(position);

        holder.donation_topic.setText(alldonations.getOrgName().trim());
        int donation_did = alldonations.getDid();
        long donation_name = alldonations.getDoncontactNumber();
        String donation_list = alldonations.getDonationList();
        String donation_Amount = alldonations.getDonationAmount();

    }

    @Override
    public int getItemCount() {
        return userdonations.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView donation_topic;
        private Button view_user_donations;
        public ViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            donation_topic = itemView.findViewById(R.id.donation_name_user_view);
            view_user_donations = itemView.findViewById(R.id.view_donations_user);

            view_user_donations.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Donations userDonations = userdonations.get(getAdapterPosition());

                    Intent intent = new Intent(context, DonationDetails.class);
                    intent.putExtra("donationdetails",userDonations);
                    intent.putExtra("donation_number",userDonations.getDonationNumber());
                    intent.putExtra("donation_amount",userDonations.getDonationAmount());
                    intent.putExtra("donation_org",userDonations.getOrgName());
                     context.startActivity(intent);
                }
            });
        }
    }
}
