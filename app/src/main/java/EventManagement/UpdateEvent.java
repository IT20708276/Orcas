package EventManagement;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.parithyaga.R;
import com.example.parithyaga.UserAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
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

public class UpdateEvent extends AppCompatActivity implements View.OnClickListener {
    private Button updateback;
    private EditText eventDateUpdate;
    private ImageView updateeventDate;
    private EditText eventNameupdate;
    private EditText eventDescupdate;
    private EditText eventlocupdate;
    private EditText eventcountupdate;
    private EditText eventcontactupdate;
    private ProgressBar update_eventprogress;

    private String currentUserId;
    private String currentUserName;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;
    private int updateDate;
    private int updateYear;
    private int updateMonth;

    // connection to firestore
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private CollectionReference collectionReference = db.collection("Event");
    private CharityEvent charityEvent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_update_event);

        charityEvent = (CharityEvent) getIntent().getSerializableExtra("chairtyevent");

        eventNameupdate = findViewById(R.id.update_event_name);
        eventDescupdate = findViewById(R.id.update_event_desc);
        eventlocupdate = findViewById(R.id.update_event_location);
        eventcountupdate = findViewById(R.id.update_event_prcount);
        eventcountupdate.setFocusable(false);
        eventcontactupdate = findViewById(R.id.update_event_phn);
        updateback = findViewById(R.id.update_event_backbtn);
        update_eventprogress = findViewById(R.id.sponsor_rec_progress);
        eventDateUpdate = findViewById(R.id.update_event_date);
        updateeventDate  =findViewById(R.id.update_date_picker);

        eventNameupdate.setText(charityEvent.getEventName());
        eventDescupdate.setText(charityEvent.getEventDesc());
        eventlocupdate.setText(charityEvent.getLocation());
        eventcountupdate.setText(String.valueOf(charityEvent.getParCount()));
        eventcontactupdate.setText("0"+String.valueOf(charityEvent.getPhoneNumber()));
        String pattern = "dd-MM-yyyy";
        DateFormat dateFormat = new SimpleDateFormat(pattern);
        Date updatedDate = charityEvent.getEventDate();
        String eventupdate = dateFormat.format(updatedDate);
        eventDateUpdate.setText(eventupdate);
        eventDateUpdate.setFocusable(false);


        if(UserApi.getInstance() != null) {
            currentUserId = UserApi.getInstance().getUserID();
            currentUserName = UserApi.getInstance().getUserName();

        }
        authStateListener = firebaseAuth -> {
            user = firebaseAuth.getCurrentUser();
            if(user != null){
                Log.d("userins", "onCreate: "+user.getEmail());
            }else{

            }
        };

        update_eventprogress.setVisibility(View.INVISIBLE);
        findViewById(R.id.update_event_btn).setOnClickListener(UpdateEvent.this);
        findViewById(R.id.delete_eventbtn).setOnClickListener(UpdateEvent.this);

        updateback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UpdateEvent.this, UserAccount.class));
            }
        });

        updateeventDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                updateDate = calendar.get(Calendar.DATE);
                updateYear = calendar.get(Calendar.YEAR);
                updateMonth = calendar.get(Calendar.MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(UpdateEvent.this, android.R.style.Theme_DeviceDefault_Dialog, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        eventDateUpdate.setText(dayOfMonth+"-"+month+"-"+year);

                    }
                }, updateYear,updateMonth,updateDate);
                datePickerDialog.show();

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.update_event_btn:
                AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
                builder2.setTitle("Are you sure you want to update this event?");
                builder2.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        try {
                            updateEvent();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                });
                builder2.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog ad2 = builder2.create();
                ad2.show();
                break;
            case R.id.delete_eventbtn:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Are you sure you want to delete this event?");
                builder.setMessage("This event will be deleted from your events");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteEvent();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog ad = builder.create();
                ad.show();

                break;
        }
    }

    private void updateEvent() throws ParseException {
        Pattern p = Pattern.compile("^\\d{10}$");

        String name = eventNameupdate.getText().toString().trim();
        String desc = eventDescupdate.getText().toString().trim();
        String dateUpdate = eventDateUpdate.getText().toString().trim();
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date event_date = dateFormat.parse(dateUpdate);

        String location = eventlocupdate.getText().toString().trim();
        String count_update = eventcountupdate.getText().toString().trim();
        int eprcount_update = Integer.parseInt(count_update);
        String contacts = eventcontactupdate.getText().toString().trim();
        long ephone_update = 0;
        if(contacts.isEmpty()){
            ephone_update = -99;
        }else{
            ephone_update = Long.parseLong(contacts);
        }
        Matcher m = p.matcher(contacts);

        update_eventprogress.setVisibility(View.VISIBLE);
        if(m.matches()){
            if(!TextUtils.isEmpty(name)
                    && !TextUtils.isEmpty(desc)
                    && ephone_update != -99
                    && !TextUtils.isEmpty(location)
                    && !TextUtils.isEmpty(dateUpdate)
                    && !TextUtils.isEmpty(count_update)
                    && !TextUtils.isEmpty(contacts)){
                //update the details
                CharityEvent charityEvent1 = new CharityEvent();
                charityEvent1.setEventName(name);
                charityEvent1.setEventDesc(desc);
                charityEvent1.setTimeadded(new Timestamp(new Date()));
                charityEvent1.setLocation(location);
                charityEvent1.setParCount(eprcount_update);
                charityEvent1.setPhoneNumber(ephone_update);
                charityEvent1.setUserName(currentUserName);
                charityEvent1.setUserId(currentUserId);
                Log.d("userID", "updateEvent: "+currentUserId);

                collectionReference.document(charityEvent.getEventId())
                        .update(
                                "eventName",charityEvent1.getEventName(),
                                "eventDesc",charityEvent1.getEventDesc(),
                                "location",charityEvent1.getLocation(),
                                "parCount",charityEvent1.getParCount(),
                                "timeadded", charityEvent1.getTimeadded(),
                                "eventDate", charityEvent1.getEventDate(),
                                "phoneNumber",charityEvent1.getPhoneNumber(),
                                "userId",charityEvent1.getUserId(),
                                "userName",charityEvent1.getUserName()
                        ).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                update_eventprogress.setVisibility(View.INVISIBLE);
                                Toast.makeText(UpdateEvent.this, "Event Updated", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(UpdateEvent.this, UserAccount.class));
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                update_eventprogress.setVisibility(View.INVISIBLE);
                                Log.d("eventup", "onFailure: "+e.toString());
                                Toast.makeText(UpdateEvent.this, "Error in Updating", Toast.LENGTH_SHORT).show();
                            }
                        });
            }else{
                update_eventprogress.setVisibility(View.INVISIBLE);
                Toast.makeText(UpdateEvent.this, "Event Details cannot be empty", Toast.LENGTH_LONG).show();

            }
        }else{
            Toast.makeText(this, "Please enter a valid PhoneNumber", Toast.LENGTH_SHORT).show();
        }


    }
    private void deleteEvent() {
        update_eventprogress.setVisibility(View.VISIBLE);
        collectionReference.document(charityEvent.getEventId()).delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            update_eventprogress.setVisibility(View.INVISIBLE);
                            Toast.makeText(UpdateEvent.this, "Event Deleted", Toast.LENGTH_LONG).show();
                            finish();
                            startActivity(new Intent(UpdateEvent.this, UserAccount.class));

                        }else{
                            update_eventprogress.setVisibility(View.INVISIBLE);
                            Toast.makeText(UpdateEvent.this, "Event could not be Deleted", Toast.LENGTH_LONG).show();

                        }
                    }
                });
    }
}