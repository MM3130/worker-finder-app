package com.mm.pwfind;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileFragment extends Fragment {

    private EditText profile_name,profile_email,profile_mobileno;
    private Button logout_btn;
    private ProgressBar profile_ProgressBar;
    FirebaseAuth authProfile;

    public ProfileFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_profile, container, false);

        profile_name = view.findViewById(R.id.user_profile_name);
        profile_mobileno = view.findViewById(R.id.user_profile_mobile);
        profile_email = view.findViewById(R.id.user_profile_emailid);
        profile_ProgressBar = view.findViewById(R.id.user_profile_progressBar);
        logout_btn = view.findViewById(R.id.logout_btn);

        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();
        if(firebaseUser == null){
            Toast.makeText(getContext(),"Something went wrong!",Toast.LENGTH_LONG).show();
        }else {
            setInProgress(true);
            getUserData(firebaseUser);
        }

        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getContext(),SplashScreenActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        return view;
    }
    void getUserData(FirebaseUser firebaseUser) {
        String userid = firebaseUser.getUid();
        DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("UserRegisteredData");
        referenceProfile.child(userid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ReadUserModel readUserModel = snapshot.getValue(ReadUserModel.class);
                if (readUserModel != null) {
                    String name = readUserModel.getName();
                    String mobileNo = readUserModel.getMobileno();
                    String email = readUserModel.getEmailid();

                    profile_name.setText(name);
                    profile_mobileno.setText(mobileNo);
                    profile_email.setText(email);
                }
                setInProgress(false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "NETWORK ERROR", Toast.LENGTH_LONG).show();
                setInProgress(false);
            }
        });
    }
    public void setInProgress(boolean inProgress){
        if(inProgress){
            profile_ProgressBar.setVisibility(View.VISIBLE);
        }else {
            profile_ProgressBar.setVisibility(View.GONE);
        }
    }
}