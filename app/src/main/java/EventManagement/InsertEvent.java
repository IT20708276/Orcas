package EventManagement;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.parithyaga.R;
import com.example.parithyaga.UserAccount;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import model.CharityEvent;
import util.UserApi;

public class InsertEvent extends AppCompatActivity {
    private ProgressBar progressBar;
    private EditText eventName;
    private EditText eventdesc;
    private EditText eventlocation;
    private EditText parcount;
    private EditText phoneNumber;
    private EditText event_date;
    private ImageView calendarbtn;
    private Button addevent;
    private Button backinsrt;

    private String currentUserId;
    private String currentUserName;
    private int mDate;
    private int mYear;
    private int mMonth;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;


    //Connection to Firestore
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    private final CollectionReference collectionReference = db.collection("Event");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_insert_event);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();



        addevent = findViewById(R.id.add_this_event);
        addevent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    saveEvent();
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        });

        eventName = findViewById(R.id.add_event_name);
        eventdesc = findViewById(R.id.add_eventDesc);
        progressBar = findViewById(R.id.add_event_progress);
        eventlocation = findViewById(R.id.add_eventloc);
        parcount = findViewById(R.id.add_initial_count);
        phoneNumber = findViewById(R.id.add_phone_number);
        backinsrt = findViewById(R.id.backbtn_insrtEvent);
        event_date = findViewById(R.id.add_event_date);
        calendarbtn = findViewById(R.id.date_picker);
        event_date.setFocusable(false);
        progressBar.setVisibility(View.INVISIBLE);

        if(UserApi.getInstance() != null) {
            currentUserId = UserApi.getInstance().getUserID();
            currentUserName = UserApi.getInstance().getUserName();

        }
        Log.d("currentID", "onCreate: "+currentUserId);
        Log.d("currentUserName", "onCreate: "+currentUserName);
        authStateListener = firebaseAuth -> {
            user = firebaseAuth.getCurrentUser();
            if(user != null){
                Log.d("userins", "onCreate: "+user.getEmail());
            }else{

            }
        };

        calendarbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                mDate = calendar.get(Calendar.DATE);
                mYear = calendar.get(Calendar.YEAR);
                mMonth = calendar.get(Calendar.MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(InsertEvent.this, android.R.style.Theme_DeviceDefault_Dialog, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        event_date.setText(dayOfMonth+"-"+month+"-"+year);

                    }
                }, mYear,mMonth,mDate);
                datePickerDialog.show();

            }
        });
        backinsrt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(InsertEvent.this, UserAccount.class));
            }
        });



    }



    private void saveEvent() throws ParseException {

        Pattern p = Pattern.compile("^\\d{10}$");

        String ename = eventName.getText().toString().trim();
        String edesc = eventdesc.getText().toString().trim();
        String eloc = eventlocation.getText().toString().trim();
        String eventDate = event_date.getText().toString().trim();
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date event_date = dateFormat.parse(eventDate);

        String participantcount = parcount.getText().toString().trim();
        String sponsrAmount = "0.00";
        int eprcount = 0;
        if(participantcount.isEmpty()){
            eprcount = -99;
        }
        if(!participantcount.isEmpty()){
            eprcount = Integer.parseInt(participantcount);
        }
        String contact = phoneNumber.getText().toString().trim();
        long ephone = 0;
        if(contact.isEmpty()){
            ephone = -99;
        }else{
            ephone = Long.parseLong(contact);
        }
        Matcher m = p.matcher(contact);

        progressBar.setVisibility(View.VISIBLE);
        if(m.matches()){
            if(!TextUtils.isEmpty(ename) &&
                    !TextUtils.isEmpty(edesc)&&
                    ephone != -99 &&
                    eprcount != -99 &&
                    !TextUtils.isEmpty(eloc)&&
                    !TextUtils.isEmpty(eventDate) &&
                    !TextUtils.isEmpty(eventDate)&&
                    !TextUtils.isEmpty(participantcount) &&
                    !TextUtils.isEmpty(contact)){

                //Create a Charity Event object
                CharityEvent charityEvent = new CharityEvent();
                charityEvent.setEventName(ename);
                charityEvent.setEventDesc(edesc);
                charityEvent.setTimeadded(new Timestamp(new Date()));
                charityEvent.setLocation(eloc);
                charityEvent.setEventDate(event_date);
                charityEvent.setSponsrAmount(sponsrAmount);
                charityEvent.setParCount(eprcount);
                charityEvent.setPhoneNumber(ephone);
                charityEvent.setUserName(currentUserName);
                charityEvent.setUserId(currentUserId);

                //invoke collection reference
                collectionReference.add(charityEvent)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(InsertEvent.this, "Event Added", Toast.LENGTH_LONG).show();
                                //Adding Karma points
                                progressBar.setVisibility(View.INVISIBLE);
                                startActivity(new Intent(InsertEvent.this, UserAccount.class));
                                finish();

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(InsertEvent.this, "Error in inserting event", Toast.LENGTH_SHORT).show();
                                Log.d("inprob", "onFailure: "+ e.toString());
                            }
                        });


            }else{

                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(this, "Insert Details", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this, "Please enter a valid PhoneNumber", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        user = firebaseAuth.getCurrentUser();
        firebaseAuth.addAuthStateListener(authStateListener);

    }

}