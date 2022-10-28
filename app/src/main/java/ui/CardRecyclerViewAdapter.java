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

import PaymentManagement.PaymentPage;
import PaymentManagement.UpdateCard;
import model.Card;
import model.Donations;

public class CardRecyclerViewAdapter extends RecyclerView.Adapter<CardRecyclerViewAdapter.ViewHolder> {
   private Context context;
   private List<Card> allusercardslist;
    private Donations donations;
    public CardRecyclerViewAdapter(Context context, List<Card> allusercardslist,Donations donations) {
        this.context = context;
        this.allusercardslist = allusercardslist;
        this.donations = donations;

    }

    @NonNull
    @Override
    public CardRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.user_cards_row,parent,false);

        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull CardRecyclerViewAdapter.ViewHolder holder, int position) {
        Card card = allusercardslist.get(position);
        holder.cardName.setText(card.getCardName());
        holder.cardNumber.setText(card.getCardNumber());

    }

    @Override
    public int getItemCount() {
        return allusercardslist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView cardNumber;
        private TextView cardName;
        private Button paybtn;
        private Button Updatecardbtn;
        public ViewHolder(@NonNull View itemView,Context ctx) {
            super(itemView);
            context =ctx;
            cardNumber = itemView.findViewById(R.id.card_number_text);
            cardName = itemView.findViewById(R.id.card_name_text);
            paybtn = itemView.findViewById(R.id.pay_from_card);
            Updatecardbtn = itemView.findViewById(R.id.update_this_card);

            paybtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Card userchoseCard = allusercardslist.get(getAdapterPosition());

                    Intent intent = new Intent(context, PaymentPage.class);
                    intent.putExtra("choosecardDet",userchoseCard);
                    intent.putExtra("donationbundle",donations);
                    context.startActivity(intent);
                }
            });

            Updatecardbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Card updateCard = allusercardslist.get(getAdapterPosition());
                    Intent intent = new Intent(context, UpdateCard.class);
                    intent.putExtra("UpdateCardDet",updateCard);
                    context.startActivity(intent);
                }
            });
        }
    }
}
