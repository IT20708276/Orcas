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

import EventManagement.Sponsorpage;
import model.Card;
import model.CharityEvent;

public class ChooseCardRecyclerView extends RecyclerView.Adapter<ChooseCardRecyclerView.ViewHolder>{
    private Context context;
    private List<Card> choosecard;
   private CharityEvent chosenevent;

    public ChooseCardRecyclerView(Context context, List<Card> choosecard, CharityEvent chosenevent) {
        this.context = context;
        this.choosecard = choosecard;
        this.chosenevent = chosenevent;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.choose_card_row,parent,false);

        return new ChooseCardRecyclerView.ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Card choiceOfcard = choosecard.get(position);
        holder.chcardNumber.setText(choiceOfcard.getCardNumber());
        holder.chcardName.setText(choiceOfcard.getCardName());
    }

    @Override
    public int getItemCount() {
        return choosecard.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView chcardNumber;
        private TextView chcardName;
        private Button choosebtn;

        public ViewHolder(@NonNull View itemView,Context ctx) {
            super(itemView);
            context = ctx;
            chcardNumber = itemView.findViewById(R.id.choose_card_name);
            chcardName = itemView.findViewById(R.id.choose_card_number);
            choosebtn = itemView.findViewById(R.id.choose_btn);

            choosebtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Card chosencard = choosecard.get(getAdapterPosition());
                    Intent intent = new Intent(context, Sponsorpage.class);
                    intent.putExtra("SponsorCard",chosencard);
                    intent.putExtra("SponsorEvent",chosenevent);
                    context.startActivity(intent);
                }
            });

        }
    }
}
