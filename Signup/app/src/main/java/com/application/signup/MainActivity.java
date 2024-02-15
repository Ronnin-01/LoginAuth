package com.application.signup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.RoomDatabase;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private DetailsDAO detailsDAO;
    Button singup;
    String UserEmailId,UserPassword,UserName,UserId,Userphone;
    EditText signupName,signupphone, signupEmail, signupUserName,signupPassword;
    TextView signInRedirect;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if(currentUser != null){
            Intent i =new Intent(getApplicationContext(),ProfilePage.class);
            startActivity(i);
            finish();
        }
    }


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        singup=findViewById(R.id.signupbtn);
        signInRedirect=findViewById(R.id.signintv);
        firebaseAuth=FirebaseAuth.getInstance();
        signupEmail=findViewById(R.id.edtemail);
        signupName=findViewById(R.id.edtname);
        signupPassword=findViewById(R.id.edtpassword);
        signupphone=findViewById(R.id.edtphone);
        firebaseFirestore=FirebaseFirestore.getInstance();

        signInRedirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,Signin.class);
                startActivity(i);
            }
        });
        singup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserEmailId = signupEmail.getText().toString().trim();
                UserPassword = signupPassword.getText().toString().trim();
                UserName = signupName.getText().toString().trim();
                Userphone = signupphone.getText().toString().trim();
                User user= new User(UserName,Userphone,UserEmailId);
                DatabaseInitializer.getRoomDataBase(MainActivity.this).detailsDao().insert(user);

                if (TextUtils.isEmpty(UserEmailId)){
                    Toast.makeText(MainActivity.this, "Enter Email", Toast.LENGTH_SHORT).show();
                    return;
                    }
                if (TextUtils.isEmpty(UserPassword)){
                    Toast.makeText(MainActivity.this, "Enter Password", Toast.LENGTH_SHORT).show();
                    return;
                }
                firebaseAuth.createUserWithEmailAndPassword(UserEmailId, UserPassword)
                        .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {

                                    UserId = firebaseAuth.getCurrentUser().getUid();
                                    DocumentReference documentReference = firebaseFirestore.collection("Users").document(UserId);
                                    Map<String,Object>user = new HashMap<>();
                                    user.put("Email",UserEmailId);
                                    user.put("Name",UserName);
                                    user.put("Contact Number", Userphone);
                                    documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Intent i =new Intent(getApplicationContext(),ProfilePage.class);
                                            startActivity(i);
                                            finish();

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            if (e instanceof FirebaseNetworkException){
                                                Toast.makeText(getApplicationContext(), "No Internet", Toast.LENGTH_SHORT).show();

                                            }
                                            Toast.makeText(getApplicationContext(), "Values not stored", Toast.LENGTH_SHORT).show();
                                        }
                                    });


                                    Toast.makeText(MainActivity.this, "UserAdded Successful",
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(MainActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}