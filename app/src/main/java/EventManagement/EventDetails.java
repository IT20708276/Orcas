package EventManagement;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.parithyaga.R;
import com.example.parithyaga.UserAccount;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import model.CharityEvent;

public class EventDetails extends AppCompatActivity {
    private TextView det_prcount;
    private TextView eventDate;
    private TextView det_eventname;
    private TextView det_eventDesc;
    private TextView det_eventloc;
    private TextView det_username;
    private TextView det_dateadded;
    private EditText sponsorAmount;
    private FloatingActionButton join_event;
    private TextView paritcipant_text_det;
    private Button sponsrbtn;
    private TextView event_contact_det;
    private Button backbtn;

    private Timestamp timeadded;
    private Date datefromserver;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;

    // connection to firestore
    private FirebaseFirestore db = FirebaseFirestore.getInstance();


    private CollectionReference collectionReference = db.collection("Event");

    //to pass the adapter details
    private CharityEvent charityEvent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_event_details);

        charityEvent = (CharityEvent) getIntent().getSerializableExtra("allchairtyevent");

        det_eventname = findViewById(R.id.event_name_text);
        det_eventDesc = findViewById(R.id.event_desctext2);
        det_eventloc = findViewById(R.id.event_locationview);
        det_prcount = findViewById(R.id.prcount_view);
        sponsorAmount = findViewById(R.id.sponsor_amount_text);
        sponsorAmount.setFocusable(false);
        eventDate = findViewById(R.id.event_name_date);
        det_username = findViewById(R.id.username_list_text);
        event_contact_det = findViewById(R.id.user_contacts_list);
        paritcipant_text_det = findViewById(R.id.participant_tag);
        join_event = findViewById(R.id.joint_event_btn);
        backbtn = findViewById(R.id.back_btn_eventdet);
        sponsrbtn = findViewById(R.id.sponsr_btn);

        det_eventname.setText(charityEvent.getEventName());
        det_eventDesc.setText(charityEvent.getEventDesc());
        det_eventloc.setText(charityEvent.getLocation());
        det_username.setText(charityEvent.getUserName());
        det_prcount.setText(String.valueOf(charityEvent.getParCount()));
        Date eventDetDate = charityEvent.getEventDate();
        String pattern = "dd-MM-yyyy";
        DateFormat dateFormat = new SimpleDateFormat(pattern);
        String eventdetails = dateFormat.format(eventDetDate);
        eventDate.setText(eventdetails.trim());
        event_contact_det.setText("0"+String.valueOf(charityEvent.getPhoneNumber()));
        sponsorAmount.setText(charityEvent.getSponsrAmount());

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EventDetails.this, UserAccount.class));
            }
        });
        join_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateParCount();
                SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(EventDetails.this);
                pref.edit().putBoolean("isEnabled",false).commit();
                join_event.setEnabled(false);

            }
        });

        sponsrbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(EventDetails.this,ChooseCard.class);
                intent.putExtra("charityEvent",charityEvent);
                startActivity(intent);

            }
        });
    }

    private void updateParCount() {
        collectionReference.document(charityEvent.getEventId())
                .update(
                        "parCount", FieldValue.increment(1)
                ).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(EventDetails.this, "You joined an event", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(EventDetails.this,UserAccount.class));

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("details", "onFailure: "+e.toString());
                        Toast.makeText(EventDetails.this, "Joining Failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        join_event.setEnabled(true);
    }


}