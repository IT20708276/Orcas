package DeliveryManagement;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.parithyaga.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import model.Delivery;

public class PickUpDetails extends AppCompatActivity {
        private TextView pickupOrg;
        private TextView pickupAddress;
        private TextView pickupTime;
        private TextView pickupList;
        private TextView pickupstatus;

        private Button updateDel;
        private Button delete_pickup;
        //getAdapter Details
        private Delivery pickupdeliver;
        private String pickTime;
        private String pickStatus;

    // connection to firestore
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private CollectionReference collectionReference = db.collection("ToPickUp");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_pick_up_details);

        pickupdeliver = (Delivery) getIntent().getSerializableExtra("PickupDel");
        pickTime = getIntent().getStringExtra("pickuptime");
        pickStatus = getIntent().getStringExtra("del_status");

        pickupOrg = findViewById(R.id.pickup_del_orgName);
        pickupAddress = findViewById(R.id.pickup_del_address);
        pickupTime = findViewById(R.id.pickup_time_det);
        pickupList = findViewById(R.id.pickup_del_donlist);
        pickupstatus = findViewById(R.id.pickstatus);

        updateDel = findViewById(R.id.update_del);
        delete_pickup = findViewById(R.id.delete_del);

        pickupOrg.setText(pickupdeliver.getDel_name());
        pickupAddress.setText(pickupdeliver.getDel_location());
        pickupTime.setText(pickupdeliver.getPickupTime());
        pickupList.setText(pickupdeliver.getDel_list());
        pickupstatus.setText(pickupdeliver.getDel_status());

        updateDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PickUpDetails.this,UpdateDelivery.class);
                intent.putExtra("pickupdelivery",pickupdeliver);
                startActivity(intent);
            }
        });

        delete_pickup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DeleteDelivery();
            }
        });

    }

    private void DeleteDelivery() {
        collectionReference.document(pickupdeliver.getDel_id())
                .delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(PickUpDetails.this, "PickUp was Deleted", Toast.LENGTH_SHORT).show();
                            finish();
                            startActivity(new Intent(PickUpDetails.this,PickUpDelivery.class));
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }
}