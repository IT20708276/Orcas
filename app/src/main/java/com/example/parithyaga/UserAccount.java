package com.example.parithyaga;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import util.UserApi;

public class UserAccount extends AppCompatActivity {
    private TextView userNameText;
    private TextView userEmailText;
    private TextView userPhoneText;
    private FloatingActionButton gotofragbtn;

    private String currentUserID;
    private String UserName;
    private String userEmail;
    private String userPhone;

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
        setContentView(R.layout.activity_user_account);

        firebaseAuth = FirebaseAuth.getInstance();
        userNameText = findViewById(R.id.user_Name_text);
        userEmailText = findViewById(R.id.donation_contacts_det);
        userPhoneText = findViewById(R.id.user_phone_text);
        gotofragbtn = findViewById(R.id.go_to_mypage);

        if(UserApi.getInstance()!= null){
            currentUserID = UserApi.getInstance().getUserID();
            UserName = UserApi.getInstance().getUserName();
            userEmail = UserApi.getInstance().getUserEmail();
            userPhone = UserApi.getInstance().getUserphoneNumber();

            userNameText.setText(UserName);
            userEmailText.setText(userEmail);
            userPhoneText.setText(userPhone);

        }

        gotofragbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserAccount.this,UserViewPage.class));
            }
        });
    }
}