package DonationManagement;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.parithyaga.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import model.Donations;

public class UpdateDonationAmount extends AppCompatActivity {
    private EditText totAmount;
    private EditText dontdAmount;
    private EditText remainAmount;

    private Button updateAmount;
    private FloatingActionButton calculateAmount;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;

    // connection to firestore
    private FirebaseFirestore db = FirebaseFirestore.getInstance();


    private CollectionReference collectionReference = db.collection("Donation");

    //to pass the adapter details
    private Donations donations;
    float remainingAmount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_donation_amount);

        donations = (Donations) getIntent().getSerializableExtra("thisdonAmount");

        totAmount= findViewById(R.id.update_tot_amnt);

        dontdAmount = findViewById(R.id.update_don_amnt);
        remainAmount = findViewById(R.id.update_remain_amnt);
        remainAmount.setFocusable(false);

        updateAmount = findViewById(R.id.update_donationsbtn);
        updateAmount.setEnabled(false);
       calculateAmount = findViewById(R.id.calculate);


       totAmount.setText(donations.getDonationAmount());

       calculateAmount.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               remainingAmount = calculateRemainder(dontdAmount.getText().toString(),totAmount.getText().toString());
               updateAmount.setVisibility(View.VISIBLE);
               updateAmount.setClickable(true);
               updateAmount.setEnabled(true);
           }
       });

       updateAmount.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               UpdateAmount(String.valueOf(remainingAmount));
           }
       });


    }

    private float calculateRemainder(String donatedAmount,String wantedAmount) {
        if(!TextUtils.isEmpty(donatedAmount)
        && !TextUtils.isEmpty(wantedAmount)){
            String wantedformat =  wantedAmount.replace("Rs."," ");

            Log.d("format", "calculateRemainder: "+wantedformat);
            float dontedAmt = Float.parseFloat(donatedAmount);
            float wantdAmnt = Float.parseFloat(wantedformat);
            float remainderAmnt = (wantdAmnt - dontedAmt);
            Log.d("Result", "calculateRemainder: "+ remainderAmnt);
            if(remainderAmnt<=0){
                remainderAmnt = 0;
                remainAmount.setText("Rs. "+remainderAmnt);
                return remainderAmnt;
            }else{
                remainAmount.setText("Rs. "+remainderAmnt);
               return remainderAmnt;
            }
        }else{
            Toast.makeText(this, "Please input Donated Amount", Toast.LENGTH_SHORT).show();
            return 0;
        }
    }

    private void UpdateAmount(String remainder) {

        String formatRemainder = "Rs."+remainder;
        collectionReference.document(donations.getDonationNumber())
                .update(
                        "donationAmount",formatRemainder
                ).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(UpdateDonationAmount.this, "The Donation Amount has been Updated", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(UpdateDonationAmount.this,MyDonationList.class));
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UpdateDonationAmount.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}