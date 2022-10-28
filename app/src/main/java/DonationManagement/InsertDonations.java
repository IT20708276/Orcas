package DonationManagement;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.parithyaga.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import model.Donations;

public class InsertDonations extends AppCompatActivity {
    private EditText Did;
    private EditText orgName;
    private EditText contactNum;
    private EditText donlist;
    private EditText donamount;
    private EditText donlocation;

    private ProgressBar donationprogress;
    private Button insrtdonbtn;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;


    //Connection to Firestore
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    private final CollectionReference collectionReference = db.collection("Donation");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_donations);
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();


        Did = findViewById(R.id.update_tot_amnt);
        orgName = findViewById(R.id.update_don_amnt);
        contactNum = findViewById(R.id.update_remain_amnt);
        donlist = findViewById(R.id.update_donation_list);
        donamount = findViewById(R.id.update_donation_amount);
        donlocation = findViewById(R.id.donation_location);
        donationprogress = findViewById(R.id.progressBardon);
        insrtdonbtn = findViewById(R.id.add_donation);


        insrtdonbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveDonation();
            }
        });

        authStateListener = firebaseAuth -> {
            user = firebaseAuth.getCurrentUser();
            if(user != null){
                Log.d("userins", "onCreate: "+user.getEmail());
            }else{

            }
        };
    }

    private void saveDonation() {
        String donationDid = Did.getText().toString().trim();

        int donDid=0;
        if(donationDid.isEmpty()){
           donDid = -99;

        }
        if(!donationDid.isEmpty()){
            donDid = Integer.parseInt(donationDid);

        }

            String donOrgName = orgName.getText().toString().trim();
            String doncontactNum = contactNum.getText().toString().trim();
            long donationcontact=0;

        if(doncontactNum.isEmpty()){
           donationcontact = -99;
        }
        if(!doncontactNum.isEmpty()){
            donationcontact = Long.parseLong(doncontactNum);
        }

            String donationlist = donlist.getText().toString().trim();
            String donloc = donlocation.getText().toString().trim();
            String donationamnt = donamount.getText().toString().trim();
            String donamount = "Rs."+donationamnt;

        donationprogress.setVisibility(View.VISIBLE);

        if(!TextUtils.isEmpty(donationDid) &&
                !TextUtils.isEmpty(donOrgName)&&
                donDid != -99 &&
                donationcontact != -99 &&
                !TextUtils.isEmpty(donloc) &&
                !TextUtils.isEmpty(doncontactNum)&&
                !TextUtils.isEmpty(donationlist) &&
                !TextUtils.isEmpty(donationamnt)){

            //Create a Donation object
            Donations donations = new Donations();
            donations.setDid(donDid);
            donations.setOrgName(donOrgName);
            donations.setDoncontactNumber(donationcontact);
            donations.setDonationList(donationlist);
            donations.setOrglocation(donloc);
            donations.setDonationAmount(donamount);

        collectionReference.add(donations).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(InsertDonations.this, "Donation Added", Toast.LENGTH_LONG).show();
                donationprogress.setVisibility(View.INVISIBLE);
                startActivity(new Intent(InsertDonations.this,MyDonationList.class));
                finish();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(InsertDonations.this, e.toString(), Toast.LENGTH_SHORT).show();

            }
        });


        }else{
            donationprogress.setVisibility(View.INVISIBLE);
            Toast.makeText(this, "Enter details", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        user = firebaseAuth.getCurrentUser();
        firebaseAuth.addAuthStateListener(authStateListener);

    }
}