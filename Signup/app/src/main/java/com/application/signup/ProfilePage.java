package com.application.signup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class ProfilePage extends AppCompatActivity {
    private DetailsDAO detailsDAO;
    private ArrayAdapter<Signin> adapter;
    private List<User> dataList = new ArrayList<>();
    FirebaseAuth firebaseAuth;
    DocumentReference reference;
    Button logout;
    String name,phone,mail;
    FirebaseFirestore firebaseFirestore;
    TextView username,usermail,usernumber,roomdata;
    FirebaseUser user;


    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);
        firebaseAuth = FirebaseAuth.getInstance();
        logout=findViewById(R.id.logout);
        username=findViewById(R.id.username);
        roomdata=findViewById(R.id.roomdata);
        usermail=findViewById(R.id.usermail);
        usernumber=findViewById(R.id.usernumber);
        firebaseFirestore= FirebaseFirestore.getInstance();
        user=firebaseAuth.getCurrentUser();

        List<User> userList = DatabaseInitializer.getRoomDataBase(this).detailsDao().getAllUsers();

        StringBuilder stringBuilder = new StringBuilder();
        for (User userr : userList){
            stringBuilder.append("Name: ").append(userr.getUserName()).append("\n")
                    .append("Email: ").append(userr.getUserEmail()).append("\n")
                    .append("Phone Number: ").append(userr.getUserphone()).append("\n\n");
        }
        roomdata.setText(stringBuilder.toString());




        if (user == null){
            Intent  i = new Intent(ProfilePage.this,MainActivity.class);
            startActivity(i);
            finish();

        }
        else {
           String Uid = firebaseAuth.getCurrentUser().getUid();
           reference=firebaseFirestore.collection("Users").document(Uid);
           reference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
               @Override
               public void onSuccess(DocumentSnapshot documentSnapshot) {
                   name =documentSnapshot.getString("Name");
                   mail =documentSnapshot.getString("Email");
                   phone =documentSnapshot.getString("Contact Number");
                   username.setText(name);
                   usermail.setText(mail);
                   usernumber.setText(phone);

               }
           }).addOnFailureListener(new OnFailureListener() {
               @Override
               public void onFailure(@NonNull Exception e) {
                   Toast.makeText(getApplicationContext(), "Error Fetching Data", Toast.LENGTH_SHORT).show();
               }
           });

        }
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent  i = new Intent(ProfilePage.this,MainActivity.class);
                startActivity(i);
                finish();
            }
        });

    }

}