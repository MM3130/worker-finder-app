package com.mm.pwfind;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SplashScreenActivity extends AppCompatActivity {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    String cEmail="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.splash_screen_activity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseUser firebaseUser = mAuth.getCurrentUser();
                if(firebaseUser!=null){
//                    String userid = firebaseUser.getUid();
//                    DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("UserRegisteredData");
//                    referenceProfile.child(userid);
//                    if(userid.equals(referenceProfile.child(userid))){
//                        startActivity(new Intent(SplashScreenActivity.this, UserHome.class));
//                        finish();
//                    }else {
//                        startActivity(new Intent(SplashScreenActivity.this, WorkerHome.class));
//                        finish();
//                    }
                    //there is some user logged in
                    startActivity(new Intent(SplashScreenActivity.this, UserHome.class));
                    finish();
                }else{
                    //no one logged in
                    startActivity(new Intent(SplashScreenActivity.this,LoginActivity.class));
                    finish();
                }
            }
        },1000);
    }
}