package com.mm.pwfind;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;


public class WorkerDetailsShowFrag extends Fragment {
    public String name,skill,landmark,city,state,verified,gender,mobile,image;

    public WorkerDetailsShowFrag() {
        // Required empty public constructor
    }

    public WorkerDetailsShowFrag(String name, String skill, String landmark, String city, String state, String verified, String gender, String mobile, String image) {
        this.name = name;
        this.skill = skill;
        this.landmark = landmark;
        this.city = city;
        this.state = state;
        this.verified = verified;
        this.gender = gender;
        this.mobile = mobile;
        this.image = image;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_worker_details_show, container, false);

        ImageView imageholder = view.findViewById(R.id.img_wRegister);
        TextView nameholder = view.findViewById(R.id.name_holder);
        TextView skillholder = view.findViewById(R.id.skill_holder);
        TextView statusholder = view.findViewById(R.id.status_holder);
        TextView landmarkholder = view.findViewById(R.id.landmark_holder);
        TextView cityholder = view.findViewById(R.id.city_holder);
        TextView stateholder = view.findViewById(R.id.state_holder);
        TextView genderholdeer = view.findViewById(R.id.gender_holder);
        Button callbtn = view.findViewById(R.id.call_btn);

        nameholder.setText(name);
        skillholder.setText(skill);
        landmarkholder.setText(landmark);
        cityholder.setText(city);
        stateholder.setText(state);
        statusholder.setText(verified);
        genderholdeer.setText(gender);
        Glide.with(getContext()).load(image).into(imageholder);

        callbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+mobile)));
            }
        });

        return view;
    }
}