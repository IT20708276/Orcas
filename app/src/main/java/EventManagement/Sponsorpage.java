package EventManagement;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.parithyaga.R;
import com.example.parithyaga.UserAccount;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import model.Card;
import model.CharityEvent;
import util.UserApi;

public class Sponsorpage extends AppCompatActivity {
    private EditText add_sponsor;
    private Button sponsorthis;
    private Button backbtn;
    private ProgressBar sponsor_page;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;

    // connection to firestore
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private String currentUserId;
    private String currentUserName;


    private CollectionReference collectionReference = db.collection("Event");
    private CollectionReference collectionReference2 = db.collection("Sponsorships");
    float sponsorship;
    float convert;
    String initial;
    CharityEvent sponsorEvent;
    Card sponsorCard;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_sponsorpage);

        sponsorCard = (Card) getIntent().getSerializableExtra("SponsorCard");
        sponsorEvent = (CharityEvent) getIntent().getSerializableExtra("SponsorEvent");

        initial = sponsorEvent.getSponsrAmount();
        convert = Float.parseFloat(initial);

        add_sponsor= findViewById(R.id.enter_sposorship_amount);
        sponsorthis  = findViewById(R.id.sponsor_update_btn);
        backbtn = findViewById(R.id.update_event_backbtn);
        sponsor_page = findViewById(R.id.sponsor_progress);

        Log.d("nodocument?", "onCreate: "+sponsorEvent.getEventName());

        if(UserApi.getInstance() != null) {
            currentUserId = UserApi.getInstance().getUserID();
            currentUserName = UserApi.getInstance().getUserName();

        }
        sponsorthis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SponsorEvent(add_sponsor.getText().toString().trim());

            }
        });

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Sponsorpage.this, UserAccount.class));
            }
        });

    }

    private void SponsorEvent(String sponsor) {
        sponsor_page.setVisibility(View.VISIBLE);
        if(!TextUtils.isEmpty(sponsor)){
            sponsorship = Float.parseFloat(sponsor);
            Float addedAmount = (convert+sponsorship);
            String sponserComplete = addedAmount.toString().trim();
            collectionReference.document(sponsorEvent.getEventId())
                    .update(
                            "sponsrAmount",sponserComplete
                    ).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Map<String, Object> data2 = new HashMap<>();
                            data2.put("EventName",sponsorEvent.getEventName() );
                            data2.put("SponsorShip", sponsor);
                            data2.put("UserName",currentUserName);
                            data2.put("UserId",currentUserId);
                            collectionReference2.add(data2);
                            Toast.makeText(Sponsorpage.this, "Your Sponsorship is Apreciated. Thank you", Toast.LENGTH_SHORT).show();
                            sponsor_page.setVisibility(View.INVISIBLE);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("errprSpons", "onFailure: "+e.toString());
                            Toast.makeText(Sponsorpage.this, "Your sponsorship wasn't made", Toast.LENGTH_LONG).show();
                            sponsor_page.setVisibility(View.INVISIBLE);
                        }
                    });



        }else{
            Toast.makeText(this, "Please enter your amount", Toast.LENGTH_SHORT).show();
            sponsor_page.setVisibility(View.INVISIBLE);

        }
    }
}