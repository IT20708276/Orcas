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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import util.AdminApi;

public class RegisterAdmin extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;

    //Firestore connection
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private CollectionReference collectionReference = db.collection("Admin");

    private EditText AdminenteradminID;
    private EditText Adminnameinti;
    private EditText Adminenteremail;
    private EditText Adminenterphone;
    private EditText Adminpwd;
    private RadioGroup adminroles;
    private RadioButton adminbtns;
    private Button savebtn;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_register_admin);

        firebaseAuth = FirebaseAuth.getInstance();
        adminroles = findViewById(R.id.radioGroup);
        progressBar = findViewById(R.id.create_acct_progress);
        savebtn = findViewById(R.id.signup_admin);
        Adminnameinti = findViewById(R.id.admin_name_donprfl);
        AdminenteradminID = findViewById(R.id.adminID);
        Adminenteremail = findViewById(R.id.admin_email);
        Adminenterphone = findViewById(R.id.admin_telephn);
        Adminpwd = findViewById(R.id.admin_password);

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                currentUser = firebaseAuth.getCurrentUser();
                if(currentUser != null){
                    //this means user has already logged in

                }else{
                    //no user yet.......
                }
            }
        };


        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!TextUtils.isEmpty(AdminenteradminID.getText().toString())
                        && !TextUtils.isEmpty(Adminnameinti.getText().toString())
                        && !TextUtils.isEmpty(Adminenteremail.getText().toString())
                        && !TextUtils.isEmpty(Adminenterphone.getText().toString())
                        && !TextUtils.isEmpty(Adminpwd.getText().toString())){

                    String adminID = AdminenteradminID.getText().toString().trim();
                    String adminName = Adminnameinti.getText().toString().trim();
                    String adminemail = Adminenteremail.getText().toString().trim();
                    String adminphnnumber = Adminenterphone.getText().toString().trim();
                    String adminpwd = Adminpwd.getText().toString().trim();

                    int selectedid = adminroles.getCheckedRadioButtonId();
                    adminbtns = (RadioButton) findViewById(selectedid);
                    String adminrole = adminbtns.getText().toString();

                    createUserEmailAccount(adminID,adminName,adminemail,adminphnnumber,adminpwd,adminrole);

                }else{
                    Toast.makeText(RegisterAdmin.this, "Empty Fields Not Allowed", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void createUserEmailAccount(String adminID,String adminName,String adminemail, String adminphnnumber, String adminpwd, String adminrole) {
            if(!TextUtils.isEmpty(adminID)
                    && !TextUtils.isEmpty(adminName)
                    && !TextUtils.isEmpty(adminemail)
                    && !TextUtils.isEmpty(adminphnnumber)
                    && !TextUtils.isEmpty(adminpwd)
                    && !TextUtils.isEmpty(adminrole)){
                progressBar.setVisibility(View.VISIBLE);
                firebaseAuth.createUserWithEmailAndPassword(adminemail,adminpwd)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                currentUser = firebaseAuth.getCurrentUser();
                                assert currentUser != null;
                                String currentadminID = currentUser.getUid();

                                //Creating a admin map so we can create a admin in the admin collection
                                Map<String,String> adminObj= new HashMap<>();
                                adminObj.put("AdminNumber", currentadminID);
                                adminObj.put("AdminName",adminName);
                                adminObj.put("AdminID", adminID);
                                adminObj.put("Admin_Email", adminemail);
                                adminObj.put("Admin_Phone_Number",adminphnnumber);
                                adminObj.put("Admin_Role",adminrole);

                                //save to the firestor database
                                collectionReference.add(adminObj)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                documentReference.get()
                                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                if(task.getResult().exists()){
                                                                    progressBar.setVisibility(View.INVISIBLE);
                                                                    String AdminID = task.getResult().getString("AdminID");
                                                                    String AdminName = task.getResult().getString("AdminName");
                                                                    String AdminEmail = task.getResult().getString("Admin_Email");
                                                                    String AdminphnNumber = task.getResult().getString("Admin_Phone_Number");
                                                                    String Adminrole = task.getResult().getString("Admin_Role");

                                                                    AdminApi adminApi = AdminApi.getInstance();
                                                                    adminApi.setAdminNumber(currentadminID);
                                                                    adminApi.setNameIn(AdminName);
                                                                    adminApi.setAdminID(AdminID);
                                                                    adminApi.setPhonenumber(AdminphnNumber);
                                                                    adminApi.setEmail(AdminEmail);
                                                                    adminApi.setAdminrole(Adminrole);

                                                                    Intent intent;

                                                                    if(Adminrole.equals("Donation Manager")){
                                                                        intent = new Intent(RegisterAdmin.this, DonationMAccount.class);
                                                                        intent.putExtra("AdminID",AdminID);
                                                                        intent.putExtra("AdminName",AdminName);
                                                                        intent.putExtra("Admin_Phone_Number",AdminphnNumber);
                                                                        intent.putExtra("Admin_Email",AdminEmail);
                                                                        intent.putExtra("Admin_Role",Adminrole);
                                                                        startActivity(intent);
                                                                    }else{
                                                                        intent = new Intent(RegisterAdmin.this, DeliveryMAccount.class);
                                                                        intent.putExtra("AdminID",AdminID);
                                                                        intent.putExtra("AdminName",AdminName);
                                                                        intent.putExtra("Admin_Phone_Number",AdminphnNumber);
                                                                        intent.putExtra("Admin_Email",AdminEmail);
                                                                        intent.putExtra("Admin_Role",Adminrole);
                                                                        startActivity(intent);
                                                                    }



                                                                }else{
                                                                    progressBar.setVisibility(View.INVISIBLE);
                                                                }
                                                            }
                                                        });
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(RegisterAdmin.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                                                Log.d("admincreate2", "onFailure: "+e.getMessage().toString());
                                            }
                                        });

                            }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(RegisterAdmin.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();

                            }
                        });
            }else{
                //something went wrong
            }
    }

    @Override
    protected void onStart() {
        super.onStart();

        currentUser = firebaseAuth.getCurrentUser();
        firebaseAuth.addAuthStateListener(authStateListener);
    }
}