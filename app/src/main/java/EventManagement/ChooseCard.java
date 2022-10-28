package EventManagement;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.parithyaga.R;
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

import PaymentManagement.InsertCard;
import model.Card;
import model.CharityEvent;
import ui.ChooseCardRecyclerView;
import util.UserApi;

public class ChooseCard extends AppCompatActivity {
    private FloatingActionButton add_card;
    private List<Card> choosecard;
    private RecyclerView choosenRecyclerView;
    private ChooseCardRecyclerView chooseCardRecyclerViewAdapter;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;

    private String currentUserId;
    private String currentUserName;

    // connection to firestore
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("PaymentCards");
    private CharityEvent chosenEvent;
    private Card pickedcard;
    private ProgressBar chooseCardprogress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_choose_card);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        chosenEvent = (CharityEvent) getIntent().getSerializableExtra("charityEvent");

        if(UserApi.getInstance() != null) {
            currentUserId = UserApi.getInstance().getUserID();
            currentUserName = UserApi.getInstance().getUserName();

        }

        add_card = findViewById(R.id.choose_card_add);
        choosenRecyclerView = findViewById(R.id.choose_card_recycler);
        chooseCardprogress = findViewById(R.id.choose_card_progress);

        choosecard = new ArrayList<>();
        choosenRecyclerView.setHasFixedSize(true);
        choosenRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        getCardList();

        add_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(ChooseCard.this, InsertCard.class);
                startActivity(intent);

            }
        });
    }

    private void getCardList() {
        super.onStart();
        chooseCardprogress.setVisibility(View.VISIBLE);
        collectionReference.whereEqualTo("userName",currentUserName)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(!queryDocumentSnapshots.isEmpty()){
                            for(QueryDocumentSnapshot chosencards :queryDocumentSnapshots){
                                Card choiceCard = chosencards.toObject(Card.class);
                                Log.d("choiceCard", "onSuccess: "+choiceCard.getCardNumber());
                                choiceCard.setCardId(chosencards.getId());

                                choosecard.add(choiceCard);
                                chooseCardprogress.setVisibility(View.INVISIBLE);

                            }


                            chooseCardRecyclerViewAdapter = new ChooseCardRecyclerView(ChooseCard.this,choosecard,chosenEvent);
                            choosenRecyclerView.setAdapter(chooseCardRecyclerViewAdapter);
                            chooseCardRecyclerViewAdapter.notifyDataSetChanged();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ChooseCard.this, "Error in loading card details", Toast.LENGTH_SHORT).show();
                        chooseCardprogress.setVisibility(View.INVISIBLE);
                    }
                });
    }
}