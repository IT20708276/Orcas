package DonationManagement;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.parithyaga.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import PaymentManagement.UserCardList;
import model.Donations;

public class DonationDetails extends AppCompatActivity {
    private TextView donationNameheader;
    private TextView doncontactdet;
    private TextView donationNeeded;
    private TextView donationlist;
    private TextView diddet;
    private Button donatebtn;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;

    // connection to firestore
    private FirebaseFirestore db = FirebaseFirestore.getInstance();


    private CollectionReference collectionReference = db.collection("Donation");

    //pass Adapter Details
    private Donations userDonations;
    private String donationAmount;
    private String donationOrgname;
    private String donationNum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_donation_details);


        userDonations = (Donations) getIntent().getSerializableExtra("donationdetails");


        donationNameheader = findViewById(R.id.donation_name_banner);
        diddet = findViewById(R.id.donation_number_det);
        doncontactdet = findViewById(R.id.donation_contacts_det);
        donationNeeded = findViewById(R.id.amount_needed);
        donatebtn = findViewById(R.id.donatebtn);
        donationlist = findViewById(R.id.donation_list);

        donationNameheader.setText(userDonations.getOrgName());
        diddet.setText(String.valueOf(userDonations.getDid()));
        doncontactdet.setText(String.valueOf(userDonations.getDoncontactNumber()));
        donationNeeded.setText(userDonations.getDonationAmount());
        donationlist.setText(userDonations.getDonationList());

       // userDonationsRecyclerAdpater.setClickedDonation(donatedAmount,donationNum,donationName);
        donatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DonationDetails.this, UserCardList.class);
                intent.putExtra("userDonation",userDonations);
                startActivity(intent);

            }
        });
    }
}