package com.mm.pwfind;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class UpdateWorker extends AppCompatActivity {
    FirebaseAuth authProfile;
    private Button update_Btn;
    private ImageButton updateBack_img_btn;
    DatabaseReference referenceProfile;
    private EditText name_up,skill_up,landmark_up,city_up,state_up,mobile_up,image_up;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.update_worker_activity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        name_up  = findViewById(R.id.name_Update);
        name_up.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        skill_up = findViewById(R.id.skill_Update);
        skill_up.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        landmark_up = findViewById(R.id.landmark_Update);
        landmark_up.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        city_up = findViewById(R.id.city_Update);
        city_up.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        state_up = findViewById(R.id.state_Update);
        state_up.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        mobile_up = findViewById(R.id.mobile_Update);
        image_up = findViewById(R.id.image_Update);
        update_Btn = findViewById(R.id.update_btn);

        updateBack_img_btn = findViewById(R.id.back_btn_update);

        updateBack_img_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();
        if(firebaseUser == null){
            Toast.makeText(UpdateWorker.this,"Something went wrong!",Toast.LENGTH_LONG).show();
        }else {
            fetchWorkerData(firebaseUser);
            update_Btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    saveWorkerData(firebaseUser);
                }
            });
        }
    }

    public void fetchWorkerData(FirebaseUser  firebaseUser){
        String userid = firebaseUser.getUid();
        referenceProfile = FirebaseDatabase.getInstance().getReference("WorkerRegisteredDetails");
        referenceProfile.child(userid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UpdateModelClass updateModelClass = snapshot.getValue(UpdateModelClass.class);
                if (updateModelClass != null){
                    String u_name = updateModelClass.getName();
                    String u_skill = updateModelClass.getSkill();
                    String u_landmark = updateModelClass.getLandmark();
                    String u_city = updateModelClass.getCity();
                    String u_state = updateModelClass.getState();
                    String u_mobile = updateModelClass.getMobile();
                    String u_img = updateModelClass.getImage();

                    name_up.setText(u_name);
                    skill_up.setText(u_skill);
                    landmark_up.setText(u_landmark);
                    city_up.setText(u_city);
                    state_up.setText(u_state);
                    mobile_up.setText(u_mobile);
                    image_up.setText(u_img);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UpdateWorker.this, "NETWORK ERROR", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void saveWorkerData(FirebaseUser  firebaseUser){

        String userid = firebaseUser.getUid();
        String updated_name = name_up.getText().toString();
        String updated_skill = skill_up.getText().toString();
        String updated_landmark = landmark_up.getText().toString();
        String updated_city = city_up.getText().toString();
        String updated_state = state_up.getText().toString();
        String updated_mobile = mobile_up.getText().toString();
        String updated_img = image_up.getText().toString();

        Map<String, Object> userUpdates = new HashMap<>();
        userUpdates.put("name",updated_name);
        userUpdates.put("skill",updated_skill);
        userUpdates.put("landmark",updated_landmark);
        userUpdates.put("city",updated_city);
        userUpdates.put("state",updated_state);
        userUpdates.put("mobile",updated_mobile);
        userUpdates.put("image",updated_img);

        referenceProfile.child(userid).updateChildren(userUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(UpdateWorker.this, "Data updated successfully!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(UpdateWorker.this, LoginActivity.class));
                    finish();
                } else {
                    Toast.makeText(UpdateWorker.this, "Failed to update data.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}