package com.example.parithyaga;

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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import util.AdminApi;

public class AdminLogin extends AppCompatActivity {
    private Button signupbtnadmin;
    private Button adminlogin;

    private EditText adminID;
    private EditText password;
    private EditText adminEmail;


    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentAdmin;
    private ProgressBar progressBar;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Admin");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_admin_login);

        progressBar = findViewById(R.id.admin_login_prgrs_bar);
        adminlogin = findViewById(R.id.admin_login);
        signupbtnadmin = findViewById(R.id.signup_admin);

        firebaseAuth = FirebaseAuth.getInstance();
        currentAdmin = firebaseAuth.getCurrentUser();

        adminID = findViewById(R.id.enter_adminId);
        password = findViewById(R.id.enter_pwd);
        adminEmail = findViewById(R.id.enter_admin_email);



        signupbtnadmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminLogin.this,RegisterAdmin.class);
                startActivity(intent);
            }
        });

        adminlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginAdmin(adminID.getText().toString().trim(),adminEmail.getText().toString().trim(),password.getText().toString().trim());
            }
        });
    }

    private void loginAdmin(String adminID, String adminemail,String pwd) {
        progressBar.setVisibility(View.VISIBLE);
        if(!TextUtils.isEmpty(adminID)
                && (adminID.matches("^[iI][tT][0-9]{7}$"))
                && !TextUtils.isEmpty(adminemail)
                && !TextUtils.isEmpty(pwd)){
            firebaseAuth.signInWithEmailAndPassword(adminemail,pwd)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            FirebaseUser admin = firebaseAuth.getCurrentUser();
                            Log.d("adminID", "onComplete: "+admin);
                            assert admin != null;
                            final String currentUserId = admin.getUid();
                            collectionReference.whereEqualTo("AdminNumber",currentUserId)
                                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                        @Override
                                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                            if(error != null){
                                                Toast.makeText(AdminLogin.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                            assert value != null;
                                            if(!value.isEmpty()){
                                                progressBar.setVisibility(View.INVISIBLE);
                                                for(QueryDocumentSnapshot snapshot: value){
                                                    AdminApi adminApi = AdminApi.getInstance();
                                                    adminApi.setAdminID(snapshot.getString("AdminID"));
                                                    adminApi.setNameIn(snapshot.getString("AdminName"));
                                                    adminApi.setEmail(snapshot.getString("Admin_Email"));
                                                    adminApi.setPhonenumber(snapshot.getString("Admin_Phone_Number"));
                                                    adminApi.setAdminrole(snapshot.getString("Admin_Role"));

                                                    if(snapshot.getString("Admin_Role").equals("Donation Manager")){
                                                        startActivity(new Intent(AdminLogin.this,DonationMAccount.class));
                                                    }else if(snapshot.getString("Admin_Role").equals("Delivery Manager")){
                                                        startActivity(new Intent(AdminLogin.this,DeliveryMAccount.class));
                                                    }else{
                                                        startActivity(new Intent(AdminLogin.this,MainActivity.class));
                                                    }
                                                }
                                            }
                                        }
                                    });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Toast.makeText(AdminLogin.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();                            progressBar.setVisibility(View.INVISIBLE);
                            startActivity(new Intent(AdminLogin.this,MainActivity.class));
                        }
                    });
        }else{
            progressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(AdminLogin.this, "Please Re-check credentials", Toast.LENGTH_SHORT).show();
        }
    }
}