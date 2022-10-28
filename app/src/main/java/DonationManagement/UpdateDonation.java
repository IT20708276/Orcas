package DonationManagement;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.parithyaga.DonationMAccount;
import com.example.parithyaga.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import model.Donations;
import util.AdminApi;

public class UpdateDonation extends AppCompatActivity {
    private EditText updatedid;
    private EditText update_orgName;
    private EditText update_contacts;
    private EditText updateDonationList;
    private EditText updateDonationAmnt;
    private EditText updateDonationlocation;
    private ProgressBar donationUpdateprogress;

    private String currentAdminId;
    private String currentAdminName;

    private Button donupdatebtn;
    private FloatingActionButton dondeletebtn;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;

    // connection to firestore
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private CollectionReference collectionReference = db.collection("Donation");
    private Donations donations;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_donation);

         donations = (Donations) getIntent().getSerializableExtra("thisdonation");

        updatedid = findViewById(R.id.update_tot_amnt);
        updatedid.setFocusable(false);
        update_orgName = findViewById(R.id.update_don_amnt);
        update_contacts = findViewById(R.id.update_remain_amnt);
       updateDonationList = findViewById(R.id.update_donation_list);
       updateDonationlocation = findViewById(R.id.update_donation_location);
       updateDonationAmnt = findViewById(R.id.update_donation_amount);
        updateDonationAmnt.setFocusable(false);
       donationUpdateprogress = findViewById(R.id.progressBardonupdate);

       updatedid.setText(String.valueOf(donations.getDid()));
       update_orgName.setText(donations.getOrgName());
       update_contacts.setText(String.valueOf(donations.getDoncontactNumber()));
       updateDonationList.setText(donations.getDonationList());
       updateDonationAmnt.setText(donations.getDonationAmount());
        updateDonationlocation.setText(donations.getOrglocation());
        donupdatebtn = findViewById(R.id.update_donationsbtn);
        dondeletebtn = findViewById(R.id.delete_donation_btn);

        if(AdminApi.getInstance()!= null){
            currentAdminId = AdminApi.getInstance().getAdminID();
            currentAdminName= AdminApi.getInstance().getNameIn();
        }
        authStateListener = firebaseAuth -> {
            user = firebaseAuth.getCurrentUser();
            if(user != null){
                Log.d("userins", "onCreate: "+user.getEmail());
            }else{

            }
        };

        donationUpdateprogress.setVisibility(View.INVISIBLE);
        donupdatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateDonation();
            }
        });

        dondeletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DeleteDonation();
            }
        });
    }

    private void DeleteDonation() {
        collectionReference.document(donations.getDonationNumber())
                .delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(UpdateDonation.this, "Donation Deleted", Toast.LENGTH_SHORT).show();
                            finish();
                            startActivity(new Intent(UpdateDonation.this, DonationMAccount.class));

                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UpdateDonation.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }

                });
    }

    private void updateDonation() {
        String did = updatedid.getText().toString().trim();
        int DonationID = Integer.parseInt(did);

        String orgName = update_orgName.getText().toString().trim();
        String contactDon = update_contacts.getText().toString().trim();
        long donationcontact = Long.parseLong(contactDon);
        String donationloc = updateDonationlocation.getText().toString().trim();
        String donationList = updateDonationList.getText().toString().trim();
        String donationAmount = updateDonationAmnt.getText().toString().trim();

        donationUpdateprogress.setVisibility(View.VISIBLE);

        //Update the donation
        Donations donations1 = new Donations();
        donations1.setDid(DonationID);
        donations1.setOrgName(orgName);
        donations1.setDoncontactNumber(donationcontact);
        donations1.setOrglocation(donationloc);
        donations1.setDonationList(donationList);
        donations1.setDonationAmount(donationAmount);

        collectionReference.document(donations.getDonationNumber())
                .update(
                        "orgName",donations1.getOrgName(),
                        "doncontactNumber",donations1.getDoncontactNumber(),
                        "donationList",donations1.getDonationList(),
                        "donation_location",donations1.getOrglocation(),
                        "donationAmount",donations1.getDonationAmount()

                ).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        donationUpdateprogress.setVisibility(View.INVISIBLE);
                        Toast.makeText(UpdateDonation.this, "Donation Updated", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(UpdateDonation.this,MyDonationList.class));
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        donationUpdateprogress.setVisibility(View.INVISIBLE);
                        Log.d("UpdateError", "onFailure: "+ e.getMessage());
                    }
                });

    }
}