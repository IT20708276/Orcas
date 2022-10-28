package DeliveryManagement;

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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.parithyaga.DeliveryMAccount;
import com.example.parithyaga.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import model.Delivery;
import model.Donations;
import util.AdminApi;

public class PendingInsert extends AppCompatActivity {
    private RadioGroup pendinglist;
    private RadioButton chosenpending;
    private TextView del_number;
    private TextView del_name;
    private TextView delperson;
    private TextView del_location;
    private Button savedel;
    private EditText pickupTime;
    private ProgressBar delinsrtprog;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;

    // connection to firestore
    private FirebaseFirestore db = FirebaseFirestore.getInstance();


    private CollectionReference collectionReference = db.collection("ToPickUp");
    private CollectionReference collectionReference2 = db.collection("Donation");
    //pass Adapter Details
    private Donations pickupdonation;
    private String deliveryId;
    private String deliverName;
    private String deliveryLocation;
    private String currentDeliveryMId;
    private String currentDeliveryMUN;
    private String deliveryList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_pending_insert);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        pickupdonation = (Donations) getIntent().getSerializableExtra("pendingDetails");
        deliveryId = String.valueOf(pickupdonation.getDid());
        deliverName = getIntent().getStringExtra("pendingName");
        deliveryLocation = getIntent().getStringExtra("pendinglocation");
        deliveryList = getIntent().getStringExtra("pendDonList");

        del_number = findViewById(R.id.update_del_id);
        del_name = findViewById(R.id.update_del_name);
        del_location = findViewById(R.id.update_del_location);
        delperson = findViewById(R.id.update_del_person);
        savedel = findViewById(R.id.save_del);
        pickupTime = findViewById(R.id.update_del_pickupTime);
        pendinglist = findViewById(R.id.update_del_status);
        delinsrtprog = findViewById(R.id.progress_bar_del);

        del_number.setText(deliveryId);
        del_name.setText(deliverName);
        del_location.setText(deliveryLocation);

        if(AdminApi.getInstance() != null){
            currentDeliveryMId = AdminApi.getInstance().getAdminID();
            currentDeliveryMUN = AdminApi.getInstance().getNameIn();
            delperson.setText(currentDeliveryMUN);
        }

        authStateListener = firebaseAuth -> {
            user = firebaseAuth.getCurrentUser();
            if(user != null){
                Log.d("userins", "onCreate: "+user.getEmail());
            }else{

            }
        };

        savedel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pendinglist.getCheckedRadioButtonId()!= -1){
                    int selectedstatus = pendinglist.getCheckedRadioButtonId();
                    chosenpending = (RadioButton) findViewById(selectedstatus);
                    String chosenstatus = chosenpending.getText().toString().trim();

                    savePickUp(chosenstatus,deliveryList);
                }else{
                    Toast.makeText(PendingInsert.this, "Mention the pick up Status", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void savePickUp(String choiceStatus,String deliveryList) {
        String deliveryId = del_number.getText().toString().trim();
        String deliveryName = del_name.getText().toString().trim();
        String deliveryLoc = del_location.getText().toString().trim();
        String delperson = currentDeliveryMUN;
        String pickUpTime = pickupTime.getText().toString().trim();

        delinsrtprog.setVisibility(View.VISIBLE);


        if(!TextUtils.isEmpty(deliveryId) &&
                !TextUtils.isEmpty(deliveryName)&&
                !TextUtils.isEmpty(deliveryLoc) &&
                !TextUtils.isEmpty(pickUpTime)){

            Delivery delivery = new Delivery();
            delivery.setDel_id(deliveryId);
            delivery.setDel_name(deliveryName);
            delivery.setDel_location(deliveryLoc);
            delivery.setDel_status(choiceStatus);
            delivery.setUserName(delperson);
            delivery.setDel_list(deliveryList);
            delivery.setPickupTime(pickUpTime);

            collectionReference.add(delivery)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            delinsrtprog.setVisibility(View.INVISIBLE);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(PendingInsert.this, "Donation is still in list", Toast.LENGTH_LONG).show();
                            delinsrtprog.setVisibility(View.INVISIBLE);
                        }
                    });

           collectionReference2.document(pickupdonation.getDonationNumber())
                    .delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(PendingInsert.this, "Moved to PickUp", Toast.LENGTH_SHORT).show();
                                delinsrtprog.setVisibility(View.INVISIBLE);
                                startActivity(new Intent(PendingInsert.this, DeliveryMAccount.class));
                                finish();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            delinsrtprog.setVisibility(View.INVISIBLE);
                            Toast.makeText(PendingInsert.this, "Moving to pick up fail", Toast.LENGTH_LONG).show();
                        }
                    });
        }else{
            delinsrtprog.setVisibility(View.VISIBLE);
            Toast.makeText(this, "Enter Details", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        user = firebaseAuth.getCurrentUser();
        firebaseAuth.addAuthStateListener(authStateListener);

    }
}