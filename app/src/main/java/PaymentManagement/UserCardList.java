package PaymentManagement;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.parithyaga.R;
import com.example.parithyaga.UserAccount;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import model.Card;
import model.Donations;
import ui.CardRecyclerViewAdapter;
import util.UserApi;

public class UserCardList extends AppCompatActivity {
        private Button backbtn;
        private FloatingActionButton add_user_card;
        private List<Card> usersCards;
        private RecyclerView cardrecyclerviews;
        private CardRecyclerViewAdapter cardRecyclerViewAdapter;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;

    private String currentUserId;
    private String currentUserName;

    // connection to firestore
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("PaymentCards");

    private Donations donationuser ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_user_card_list2);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        donationuser = (Donations) getIntent().getSerializableExtra("userDonation");


        if(UserApi.getInstance() != null) {
            currentUserId = UserApi.getInstance().getUserID();
            currentUserName = UserApi.getInstance().getUserName();

        }
        add_user_card = findViewById(R.id.add_user_card_btn);
        usersCards = new ArrayList<>();
        backbtn = findViewById(R.id.backbtn_userCardlist);

        cardrecyclerviews = findViewById(R.id.user_cards_recycler_view);
        cardrecyclerviews.setHasFixedSize(true);
        cardrecyclerviews.setLayoutManager(new LinearLayoutManager(this));
        getAllUserCards();


        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(UserCardList.this, UserAccount.class);
                startActivity(intent);
            }
        });

        add_user_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(UserCardList.this,InsertCard.class);
                startActivity(intent);

            }
        });


    }

    private void getAllUserCards() {
        super.onStart();
        collectionReference.whereEqualTo("userName",currentUserName)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(!queryDocumentSnapshots.isEmpty()){
                            for(QueryDocumentSnapshot alluserscards :queryDocumentSnapshots){
                                Card cards = alluserscards.toObject(Card.class);
                                cards.setCardId(alluserscards.getId());
                                usersCards.add(cards);
                            }


                            cardRecyclerViewAdapter = new CardRecyclerViewAdapter(UserCardList.this,usersCards,donationuser);

                            cardrecyclerviews.setAdapter(cardRecyclerViewAdapter);
                            cardRecyclerViewAdapter.notifyDataSetChanged();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UserCardList.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}