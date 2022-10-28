package com.example.parithyaga;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

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
import com.google.firebase.firestore.auth.User;

import java.util.HashMap;
import java.util.Map;

import util.UserApi;

public class RegisterUser extends AppCompatActivity {
    private EditText userName;
    private EditText userEmail;
    private EditText userphn;
    private EditText userpassword;
    private ProgressBar userProgress;
    private Button signUserbtn;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;

    //Firestore connection
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private CollectionReference collectionReference = db.collection("User");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_register_user);
        firebaseAuth = FirebaseAuth.getInstance();
        userName = findViewById(R.id.enter_user_name);
       userEmail = findViewById(R.id.enter_user_email);
       userphn = findViewById(R.id.enter_user_phoneNumber);
        userpassword = findViewById(R.id.enter_user_password);
        userProgress = findViewById(R.id.create_user_progress);
        signUserbtn = findViewById(R.id.signup_user);

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

        signUserbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(userName.getText().toString())
                && !TextUtils.isEmpty(userEmail.getText().toString())
                && !TextUtils.isEmpty(userphn.getText().toString())
                && !TextUtils.isEmpty(userpassword.getText().toString())){

                    String username = userName.getText().toString().trim();
                    String useremail = userEmail.getText().toString().trim();
                    String userphone = userphn.getText().toString().trim();
                    String userpwd = userpassword.getText().toString().trim();

                    createUserEmailAccount(username,useremail,userphone,userpwd);

                }else{
                    Toast.makeText(RegisterUser.this, "Empty Fields Not Allowed", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void createUserEmailAccount(String username, String useremail, String userphone, String userpwd) {
        if(!TextUtils.isEmpty(username)
        && !TextUtils.isEmpty(useremail)
        && !TextUtils.isEmpty(userphone)
        && !TextUtils.isEmpty(userpwd)){
            userProgress.setVisibility(View.VISIBLE);

            firebaseAuth.createUserWithEmailAndPassword(useremail,userpwd)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            currentUser = firebaseAuth.getCurrentUser();
                            assert currentUser != null;
                            String currentUserUid = currentUser.getUid();

                            //creating a user object
                            Map<String,String> userObj = new HashMap<>();
                            userObj.put("User ID",currentUserUid);
                            userObj.put("User Name", username);
                            userObj.put("User Email",useremail);
                            userObj.put("User Phone Number", userphone);

                            //save to the firestor database
                            collectionReference.add(userObj)
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            documentReference.get()
                                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                            if(task.getResult().exists()){
                                                                userProgress.setVisibility(View.INVISIBLE);

                                                                String UserName = task.getResult().getString("User Name");
                                                                String UserEmail = task.getResult().getString("User Email");
                                                                String UserphnNumber = task.getResult().getString("User Phone Number");

                                                                UserApi userApi = UserApi.getInstance();
                                                                userApi.setUserID(currentUserUid);
                                                                userApi.setUserName(UserName);
                                                                userApi.setUserEmail(UserEmail);
                                                                userApi.setUserphoneNumber(UserphnNumber);

                                                                Intent intent = new Intent(RegisterUser.this,UserAccount.class);
                                                                startActivity(intent);
                                                            }else{
                                                                userProgress.setVisibility(View.INVISIBLE);
                                                            }

                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            userProgress.setVisibility(View.INVISIBLE);
                                                            Toast.makeText(RegisterUser.this, e.toString(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(RegisterUser.this, e.toString(), Toast.LENGTH_SHORT).show();
                                        }
                                    });

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

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