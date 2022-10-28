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

import com.example.parithyaga.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import model.Delivery;

public class UpdateDelivery extends AppCompatActivity {
    private RadioGroup updatependingList;
    private RadioButton chosenStatus;
    private RadioButton pickupoption;
    private RadioButton onthewayopt;
    private RadioButton delopt;
    private TextView update_del_number;
    private TextView update_del_name;
    private TextView updatdelperson;
    private TextView update_del_location;
    private Button update_pickup;
    private ProgressBar updatedelprog;

    private EditText update_pickUpTime;
    private RadioGroup updatestatusgroup;
    private RadioButton updateradbtn;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;

    // connection to firestore
    private FirebaseFirestore db = FirebaseFirestore.getInstance();


    private CollectionReference collectionReference = db.collection("ToPickUp");
    private Delivery updateDel;
    private String deliverid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_update_delivery);

        updateDel = (Delivery) getIntent().getSerializableExtra("pickupdelivery");

        update_del_number = findViewById(R.id.update_del_id);
        update_del_name = findViewById(R.id.update_del_name);
        updatdelperson = findViewById(R.id.update_del_person);
        updatependingList =findViewById(R.id.update_del_status);

        update_del_location = findViewById(R.id.update_del_location);
        update_pickup = findViewById(R.id.update_del);

        pickupoption = findViewById(R.id.update_pickup_option);
        onthewayopt = findViewById(R.id.update_onthe_way_option);
        delopt = findViewById(R.id.update_delivered_option);
        update_pickUpTime = findViewById(R.id.update_del_pickupTime);
        updatedelprog = findViewById(R.id.update_del_progress);

        update_del_number.setText(updateDel.getDelNumber());
        update_del_name.setText(updateDel.getDel_name());
        updatdelperson.setText(updateDel.getUserName());
        update_del_location.setText(updateDel.getDel_location());
        update_pickUpTime.setText(updateDel.getPickupTime());
        String chosenstatus = updateDel.getDel_status();
        if(chosenstatus.equalsIgnoreCase("Pick Up")){
            pickupoption.setChecked(true);

        }else if(chosenstatus.equalsIgnoreCase("On the way")){
            onthewayopt.setChecked(true);
        }else{
            delopt.setChecked(true);
        }


        update_pickup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    UpdatePickUp();

            }
        });
    }

    private void UpdatePickUp() {
        String pickupTime =update_pickUpTime.getText().toString();
        int selectedid = updatependingList.getCheckedRadioButtonId();
        chosenStatus = (RadioButton) findViewById(selectedid);
        String pickupstat = chosenStatus.getText().toString();

        updatedelprog.setVisibility(View.VISIBLE);
        if(!TextUtils.isEmpty(pickupTime)
        && !TextUtils.isEmpty(pickupstat)){
            Delivery delivery1 = new Delivery();
            delivery1.setPickupTime(pickupTime);
            delivery1.setDel_status(pickupstat);

            collectionReference.document(updateDel.getDel_id())
                    .update(
                            "pickupTime",delivery1.getPickupTime(),
                            "del_status",delivery1.getDel_status()
                    ).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            updatedelprog.setVisibility(View.INVISIBLE);
                            Toast.makeText(UpdateDelivery.this, "This Package was Updated", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(UpdateDelivery.this,ToBeDeliveredList.class));
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            updatedelprog.setVisibility(View.INVISIBLE);
                            Toast.makeText(UpdateDelivery.this, "Failed to update", Toast.LENGTH_SHORT).show();
                            Log.d("deliveryerro", "onFailure: "+e.getMessage());
                        }
                    });

        }else{
            Toast.makeText(UpdateDelivery.this, "Field is empty", Toast.LENGTH_SHORT).show();

        }


    }
}