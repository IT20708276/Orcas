package com.example.parithyaga;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import DeliveryManagement.ToBeDeliveredList;
import util.AdminApi;

public class DeliveryMAccount extends AppCompatActivity {
    private TextView adminIDdelivery;
    private TextView adminNamedelivery;
    private TextView adminEmaildelivery;
    private Button viewp_my_page;

    private String currentAdminID;
    private String AdminID2;
    private String AdminName2;
    private String AdminEmail2;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;

    //Connection to firestore
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_delivery_maccount);

                firebaseAuth = FirebaseAuth.getInstance();

                adminIDdelivery = findViewById(R.id.admin_ID_delprfl);
                adminNamedelivery= findViewById(R.id.admin_name_delprfl);
                adminEmaildelivery =findViewById(R.id.admin_email_delprfl);
                viewp_my_page = findViewById(R.id.view_my_page_delivery);

        if(AdminApi.getInstance()!= null){
            currentAdminID = AdminApi.getInstance().getAdminNumber();
            AdminID2 = AdminApi.getInstance().getAdminID();
            AdminName2 = AdminApi.getInstance().getNameIn();
            AdminEmail2 = AdminApi.getInstance().getEmail();

            adminIDdelivery.setText(AdminID2);
            adminNamedelivery.setText(AdminName2);
            adminEmaildelivery.setText(AdminEmail2);
        }

        viewp_my_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DeliveryMAccount.this, ToBeDeliveredList.class);
                startActivity(intent);
            }
        });
    }
}