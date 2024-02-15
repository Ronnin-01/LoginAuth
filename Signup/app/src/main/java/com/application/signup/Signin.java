package com.application.signup;

import static io.grpc.okhttp.internal.Platform.get;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Signin extends AppCompatActivity {
    TextView signupact;
    EditText loginemail,loginpassword;
    String UserEmailId,UserPassword,UserId,UserName,Userphone;
    Button signinbtn;
    FirebaseFirestore firebaseFirestore;
    DocumentReference userRef;
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
        setContentView(R.layout.activity_signin);
        signupact=findViewById(R.id.tosignuppg);
        signinbtn=findViewById(R.id.signinbtn);
        loginpassword=findViewById(R.id.edtpasswordin);
        loginemail=findViewById(R.id.edtemailin);
        firebaseAuth=FirebaseAuth.getInstance();


        signinbtn.setOnClickListener(view -> {
            UserEmailId = loginemail.getText().toString();
            UserPassword = loginpassword.getText().toString();

            firebaseAuth.signInWithEmailAndPassword(UserEmailId, UserPassword)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            UserId=firebaseAuth.getCurrentUser().getUid();
                            Intent i =new Intent(Signin.this,ProfilePage.class);
                            startActivity(i);
                        } else {
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        });
        signupact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(Signin.this,MainActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
}