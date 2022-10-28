package PaymentManagement;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.parithyaga.R;
import com.example.parithyaga.UserAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import model.Card;
import model.Donations;
import util.UserApi;

public class PaymentPage extends AppCompatActivity{
    private RadioGroup donateAmount;
    private RadioButton chosenAmount;
    private Button choosebtn;
    private FloatingActionButton backbtn;

    private String currentUserID;
    private String currentUserName;
    String organizationName;
    String donatedAmount;
    float donatinAmount;
    float result;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;

    // connection to firestore
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private CollectionReference collectionReference = db.collection("Donation");
    private CollectionReference collectionReference2 = db.collection("UserDonations");
    //to getAdapter details
    private Donations donations;
    private Card carddetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_page2);

        donateAmount = findViewById(R.id.donation_packages);
        choosebtn = findViewById(R.id.donate_calcbtn);
        backbtn = findViewById(R.id.goto_userprfl);


        carddetails = (Card) getIntent().getSerializableExtra("choosecardDet");
        donations = (Donations) getIntent().getSerializableExtra("donationbundle");

        if(UserApi.getInstance() != null){
            currentUserID = UserApi.getInstance().getUserID();
            currentUserName = UserApi.getInstance().getUserName();
        }

        authStateListener = firebaseAuth -> {
            user = firebaseAuth.getCurrentUser();
            if(user != null){
                Log.d("userins", "onCreate: "+user.getEmail());
            }else{

            }
        };


        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(PaymentPage.this, UserAccount.class);
                startActivity(intent);
            }
        });
       choosebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             //CreateDonation();
                if(donateAmount.getCheckedRadioButtonId() != -1){
                    int selectedpackage = donateAmount.getCheckedRadioButtonId();
                    chosenAmount =(RadioButton)findViewById(selectedpackage);
                    String chosenpkcge = chosenAmount.getText().toString().trim();

                    UpdateDonationAmount(chosenpkcge);


                }else{
                    Toast.makeText(PaymentPage.this, "Choose an amount to Donate", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }



    private void UpdateDonationAmount(String choice) {
       super.onStart();
       donatinAmount = Float.parseFloat(choice);

       collectionReference
               .document(donations.getDonationNumber())
               .get()
               .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                   @Override
                   public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                       if(task.isSuccessful()){
                           DocumentSnapshot documentSnapshot =task.getResult();
                           organizationName = documentSnapshot.getString("orgName");
                           donatedAmount = documentSnapshot.getString("donationAmount");
                            String formatedonation =donatedAmount.replace("Rs."," ");
                            float donate = Float.parseFloat(formatedonation);
                            result = (donate- donatinAmount);


                            if(result > 0){
                                String formatresult = "Rs."+result;
                                collectionReference.document(documentSnapshot.getId())
                                        .update(
                                                "donationAmount",formatresult
                                        ).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(PaymentPage.this, "Your Donation is appericiated. Thank you!!", Toast.LENGTH_SHORT).show();

                                            }
                                        });
                            }else{
                                result = 0;
                                String formatresult = "Rs."+result;
                                collectionReference.document(documentSnapshot.getId())
                                        .update(
                                                "donationAmount",formatresult
                                        ).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(PaymentPage.this, "Your Donation is appericiated. Thank you!!", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }


                       }

                   }
               }).addOnFailureListener(new OnFailureListener() {
                   @Override
                   public void onFailure(@NonNull Exception e) {
                       Toast.makeText(PaymentPage.this, e.toString(), Toast.LENGTH_SHORT).show();
                   }
               });

    }


}