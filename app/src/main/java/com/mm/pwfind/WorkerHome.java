package com.mm.pwfind;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import de.hdodenhof.circleimageview.CircleImageView;

public class WorkerHome extends AppCompatActivity {
    private ImageButton wLogout_btn;
    private TextView name_dis,skill_dis,status_dis,mobileno_dis,email_dis,gender_dis,dateofbirth_dis,landmark_dis,city_dis,state_dis;
    private CircleImageView image_dis;
    FirebaseAuth authProfile;
    private Button edit_Btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.worker_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.worker_home), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        image_dis = findViewById(R.id.display_Image);
        name_dis = findViewById(R.id.display_Name);
        skill_dis = findViewById(R.id.display_Skill);
        status_dis = findViewById(R.id.display_Status);
        mobileno_dis = findViewById(R.id.display_Mobileno);
        email_dis = findViewById(R.id.display_Emailid);
        gender_dis = findViewById(R.id.display_Gender);
        dateofbirth_dis = findViewById(R.id.display_DOB);
        landmark_dis = findViewById(R.id.display_Landmark);
        city_dis = findViewById(R.id.display_City);
        state_dis = findViewById(R.id.display_State);
        edit_Btn = findViewById(R.id.wEdit_btn);



        edit_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(WorkerHome.this, UpdateWorker.class));
            }
        });

        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();
        if(firebaseUser == null){
            Toast.makeText(WorkerHome.this,"Something went wrong!",Toast.LENGTH_LONG).show();
        }else {
            getWorkerData(firebaseUser);
        }


        wLogout_btn = findViewById(R.id.worker_Logout_btn);
        wLogout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(WorkerHome.this,SplashScreenActivity.class));
                finish();
            }
        });
    }
    void getWorkerData(FirebaseUser firebaseUser) {
        String userid = firebaseUser.getUid();
        DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("WorkerRegisteredDetails");
        referenceProfile.child(userid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ReadWorkerModel readWorkerModel = snapshot.getValue(ReadWorkerModel.class);
                if (readWorkerModel != null) {
                    String name = readWorkerModel.getName();
                    String skill = readWorkerModel.getSkill();
                    String landmark = readWorkerModel.getLandmark();
                    String city = readWorkerModel.getCity();
                    String state = readWorkerModel.getState();
                    String status = readWorkerModel.getVerified();
                    String gender = readWorkerModel.getGender();
                    String mobileno = readWorkerModel.getMobile();
                    String dob = readWorkerModel.getDateofbirth();
                    String emailid = readWorkerModel.getEmail();
                    String image = readWorkerModel.getImage();

                    name_dis.setText(name);
                    skill_dis.setText(skill);
                    landmark_dis.setText(landmark);
                    city_dis.setText(city);
                    state_dis.setText(state);
                    status_dis.setText(status);
                    gender_dis.setText(gender);
                    mobileno_dis.setText(mobileno);
                    dateofbirth_dis.setText(dob);
                    email_dis.setText(emailid);
                    Glide.with(WorkerHome.this).load(image).into(image_dis);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(WorkerHome.this, "NETWORK ERROR", Toast.LENGTH_LONG).show();
            }
        });
    }
}