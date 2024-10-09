package com.mm.pwfind;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.hbb20.CountryCodePicker;

public class UserRegistrationActivity extends AppCompatActivity {

    private EditText userR_name_edt,userR_mobile_edt,userR_email_edt,userR_password_edt;
    private CountryCodePicker countryCodePicker;
    private Button userR_GetOTP_btn;
    private TextView back_to_login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.user_registration_activity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.user_register), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        userR_name_edt = findViewById(R.id.userRegister_name_edt);
        countryCodePicker = findViewById(R.id.register_country_code);
        userR_mobile_edt = findViewById(R.id.userRegister_mobile_edt);
        userR_email_edt = findViewById(R.id.userRegister_email_edt);
        userR_password_edt = findViewById(R.id.userRegister_password_edt);
        back_to_login = findViewById(R.id.txt_backto_loginAcc);
        userR_GetOTP_btn = findViewById(R.id.user_register_getOTP_btn);
        countryCodePicker.registerCarrierNumberEditText(userR_mobile_edt);

        userR_GetOTP_btn.setOnClickListener(view -> registerUser());

        back_to_login.setOnClickListener(view -> {
            startActivity(new Intent(UserRegistrationActivity.this, LoginActivity.class));
            finish();
        });
    }
    void registerUser(){
        String name = userR_name_edt.getText().toString();
        String emilId = userR_email_edt.getText().toString();
        String password = userR_password_edt.getText().toString();

        if(name.isEmpty()){
            userR_name_edt.setError("name required");
            return;
        }
        if(!countryCodePicker.isValidFullNumber()){
            userR_mobile_edt.setError("Mobile number not valid");
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(emilId).matches()){
            userR_email_edt.setError("enter valid email");
            return;
        }
        if(password.isEmpty() | password.length()<6){
            userR_password_edt.setError("enter 6 char password");
            return;
        }

        Intent intent  = new Intent(UserRegistrationActivity.this, UserOTPVerification.class);
        intent.putExtra("name",name);
        intent.putExtra("mobileNo",countryCodePicker.getFullNumberWithPlus());
        intent.putExtra("emailId",emilId);
        intent.putExtra("password",password);
        startActivity(intent);
        finish();
    }
}