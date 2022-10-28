package EventManagement;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import model.CharityEvent;
import util.UserApi;

public class SponsorRecords extends AppCompatActivity {
    private TableLayout tableLayout;
    private Button sponsorlistback;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;
    private String currentUserId;
    private String currentUserName;
    private ProgressBar sponsorRecprogress;

    // connection to firestore
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private int increment;
    private CollectionReference collectionReference = db.collection("Sponsorships");

    private List<CharityEvent> sponsorList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_sponsor_records);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        sponsorRecprogress = findViewById(R.id.sponsor_rec_progress);
        tableLayout = (TableLayout) findViewById(R.id.sponsor_table);
        sponsorlistback = findViewById(R.id.sponsor_list_back);

        if(UserApi.getInstance() != null) {
            currentUserId = UserApi.getInstance().getUserID();
            currentUserName = UserApi.getInstance().getUserName();

        }
        getAllSponsors();


        sponsorlistback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SponsorRecords.this, UserAccount.class));
            }
        });
    }

    private void getAllSponsors() {
        super.onStart();
        sponsorRecprogress.setVisibility(View.VISIBLE);
        Log.d("eventUserID", "onSuccess: "+currentUserId);

        collectionReference
                .whereEqualTo("UserId",currentUserId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()){
                            for (QueryDocumentSnapshot sponsors : queryDocumentSnapshots){
                                String context = sponsors.getString("EventName");
                                String context2 = sponsors.getString("SponsorShip");
                                TableRow tableRow = new TableRow(SponsorRecords.this);
                                if(queryDocumentSnapshots.getDocuments().indexOf(sponsors) %2 == 0){
                                    tableRow.setBackgroundColor( Color.parseColor("#F1F0F0"));

                                }
                                TextView eventName = new TextView(SponsorRecords.this);
                                eventName.setPadding(8,8,8,8);
                                eventName.setTextSize(20);
                                TextView sponsoruser = new TextView(SponsorRecords.this);
                                sponsoruser.setPadding(5,5,5,5);
                                sponsoruser.setTextSize(20);
                                eventName.setText(context+":");
                                sponsoruser.setText("Rs. "+context2);
                                Log.d("eventSponsored", "onSuccess: "+context);
                                tableRow.addView(eventName);
                                tableRow.addView(sponsoruser);

                                tableLayout.addView(tableRow);
                                sponsorRecprogress.setVisibility(View.INVISIBLE);
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SponsorRecords.this, "Failed to retrieve", Toast.LENGTH_SHORT).show();
                        sponsorRecprogress.setVisibility(View.INVISIBLE);
                    }
                });
    }
}