package com.example.parithyaga;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import DonationManagement.MyDonationList;
import util.AdminApi;

public class DonationMAccount extends AppCompatActivity {

    private TextView adminNamedonations;
    private TextView adminEmaildonations;
    private Button viewp_my_page_don;

    private String currentAdminID;
    private String AdminID;
    private String AdminName;
    private String AdminEmail;

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
        setContentView(R.layout.activity_donation_maccount);

        firebaseAuth = FirebaseAuth.getInstance();
        TextView workIDdonations = (TextView) findViewById(R.id.adminID_donprfl);
        adminNamedonations = findViewById(R.id.admin_name_donprfl);
        adminEmaildonations = findViewById(R.id.admin_email_donprfl);
        viewp_my_page_don = findViewById(R.id.view_my_page_donations);

        if(AdminApi.getInstance()!= null){
            currentAdminID = AdminApi.getInstance().getAdminNumber();
            String AdminID = AdminApi.getInstance().getAdminID();
            AdminName = AdminApi.getInstance().getNameIn();
            AdminEmail = AdminApi.getInstance().getEmail();

            workIDdonations.setText(AdminID);
            adminNamedonations.setText(AdminName);
            adminEmaildonations.setText(AdminEmail);
        }

        viewp_my_page_don.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DonationMAccount.this, MyDonationList.class));
            }
        });

    }
}