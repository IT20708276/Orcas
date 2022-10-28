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

import util.UserApi;

public class UserLogin extends AppCompatActivity {
    private Button signupbtnuser;
    private EditText userloginEmail;
    private EditText userloginPassword;
    private Button loginUserbtn;
    private ProgressBar userLoginProgress;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("User");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_user_login);
        firebaseAuth = FirebaseAuth.getInstance();

        userloginEmail = findViewById(R.id.user_login_email);
        userloginPassword = findViewById(R.id.user_login_password);

        signupbtnuser = findViewById(R.id.signup_user);
        loginUserbtn = findViewById(R.id.login_user_button);
        userLoginProgress = findViewById(R.id.user_login_progress);


        signupbtnuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserLogin.this,RegisterUser.class);
                startActivity(intent);
            }
        });

        loginUserbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser(userloginEmail.getText().toString().trim(),userloginPassword.getText().toString().trim());
            }
        });
    }

    private void loginUser(String email, String password) {
        userLoginProgress.setVisibility(View.VISIBLE);
        if(!TextUtils.isEmpty(email)
        && !TextUtils.isEmpty(password)){
            firebaseAuth.signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            assert user != null;
                            final String currentUserId = user.getUid();
                            Log.d("cuurent", "onComplete: "+currentUserId);
                            collectionReference.whereEqualTo("User ID",currentUserId)
                                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                        @Override
                                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                            if(error != null){
                                                Toast.makeText(UserLogin.this, error.toString(), Toast.LENGTH_SHORT).show();
                                            }
                                            assert value != null;
                                            if(!value.isEmpty()){
                                                userLoginProgress.setVisibility(View.INVISIBLE);
                                                for(QueryDocumentSnapshot snapshot: value){
                                                    UserApi userApi = UserApi.getInstance();
                                                    userApi.setUserName(snapshot.getString("User Name"));
                                                    userApi.setUserEmail(snapshot.getString("User Email"));
                                                    userApi.setUserphoneNumber(snapshot.getString("User Phone Number"));
                                                    userApi.setUserID(snapshot.getString("User ID"));
                                                    startActivity(new Intent(UserLogin.this,UserAccount.class));
                                                }

                                            }
                                        }
                                    });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(UserLogin.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                             userLoginProgress.setVisibility(View.INVISIBLE);
                            Log.d("loginuser", "onFailure: "+e.getMessage());

                        }
                    });
        }else{
            userLoginProgress.setVisibility(View.INVISIBLE);
            Toast.makeText(UserLogin.this, "Please Re-check credentials", Toast.LENGTH_SHORT).show();

        }

    }
}