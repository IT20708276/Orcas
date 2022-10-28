package PaymentManagement;

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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import model.Card;
import util.UserApi;

public class InsertCard extends AppCompatActivity {
    private EditText card_number;
    private EditText card_name;
    private EditText valid_through;
    private EditText cvn_code;
    private RadioButton accpeted;
    private Button addcard;
    private ProgressBar pay_progress;

    private String currentUserId;
    private String currentUserName;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;


    //Connection to Firestore
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    private final CollectionReference collectionReference = db.collection("PaymentCards");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_insert_card);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();


        card_number = findViewById(R.id.card_number_insrt);
        card_name = findViewById(R.id.card_name);
        valid_through = findViewById(R.id.valid_through_input);
        cvn_code = findViewById(R.id.cvn_code);
        accpeted = findViewById(R.id.accepted_check);
        addcard = findViewById(R.id.add_card_details);
        pay_progress= findViewById(R.id.progress_payment);
        if(UserApi.getInstance() != null) {
            currentUserId = UserApi.getInstance().getUserID();
            currentUserName = UserApi.getInstance().getUserName();

        }
        addcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    saveCardDeatils();
                } catch (ParseException e) {
                    Toast.makeText(InsertCard.this, "Card Format: XXXX_XXXX_XXXX_XXXX, Date Format: DD/MM/YYYY", Toast.LENGTH_SHORT).show();
                }
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

    private void saveCardDeatils() throws ParseException {
        FirebaseUser currentuser = firebaseAuth.getCurrentUser();
        assert currentuser != null;
        String cardownderUid = currentuser.getUid();

        String cardNumber = card_number.getText().toString().trim();
        int cardnumberlen = cardNumber.length();
        String cardOwnerName = card_name.getText().toString().trim();
        String usercvnNumber = cvn_code.getText().toString().trim();
        int lengthcvn = usercvnNumber.length();
        String validthrough = valid_through.getText().toString().trim();
        Date validDate = new SimpleDateFormat("dd/MM/yyyy").parse(validthrough);

        Boolean checkedacc = accpeted.isChecked();
        String checked = String.valueOf(checkedacc);

        if(!TextUtils.isEmpty(cardNumber) &&
                !TextUtils.isEmpty(cardOwnerName) &&
                !TextUtils.isEmpty(validthrough) &&
                !TextUtils.isEmpty(usercvnNumber) &&
                accpeted.isChecked() &&
                !TextUtils.isEmpty(checked)
                && lengthcvn == 4
        && cardnumberlen == 19){

            pay_progress.setVisibility(View.VISIBLE);

            Card card = new Card();
            card.setCardNumber(cardNumber);
            card.setCardName(cardOwnerName);
            card.setValidThrough(validDate);
            card.setCnvNumber(usercvnNumber);
            card.setUserName(currentUserName);
            card.setUserEmail(currentuser.getEmail());

            collectionReference.add(card)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Toast.makeText(InsertCard.this, "Card was Added", Toast.LENGTH_SHORT).show();
                            pay_progress.setVisibility(View.INVISIBLE);
                            startActivity(new Intent(InsertCard.this, UserCardList.class));
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("cardfail", "onFailure: "+e.toString());
                            pay_progress.setVisibility(View.INVISIBLE);
                            Toast.makeText(InsertCard.this, "Card wasn't inserted", Toast.LENGTH_SHORT).show();
                        }
                    });
        }else{
            pay_progress.setVisibility(View.INVISIBLE);
            Toast.makeText(this, "Check details Again", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        user = firebaseAuth.getCurrentUser();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

}