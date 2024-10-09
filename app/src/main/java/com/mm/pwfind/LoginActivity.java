package com.mm.pwfind;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

public class LoginActivity extends AppCompatActivity {

    private Button signIn_btn;
    private TextView forgot_pass_text,new_register_txt,select_language_txt;
    private EditText login_Email_edt,login_Pass_edt;
    private ProgressBar login_ProgressBar;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private Dialog dialog;
    private Button btn_userRDialog,btn_workerRDialog;
    String checkEmail="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();
        EdgeToEdge.enable(this);
        setContentView(R.layout.login_activity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.login), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dialog = new Dialog(LoginActivity.this);
        dialog.setContentView(R.layout.custom_dialog_box);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.custom_dialog_bg));
        dialog.setCancelable(true);

        login_Email_edt  = findViewById(R.id.login_email_edt);
        login_Pass_edt = findViewById(R.id.login_password_edt);
        forgot_pass_text = findViewById(R.id.txt_forgot_password);
        new_register_txt  = findViewById(R.id.txt_register_newAcc);
        login_ProgressBar = findViewById(R.id.login_ProgressBar);
        signIn_btn = findViewById(R.id.btn_signIn);
        select_language_txt = findViewById(R.id.language_choose_txt);

        btn_userRDialog = dialog.findViewById(R.id.btn_dialog_UserActivity);
        btn_userRDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, UserRegistrationActivity.class));
                dialog.dismiss();
            }
        });

        btn_workerRDialog = dialog.findViewById(R.id.btn_dialog_WorkerActivity);
        btn_workerRDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, WorkerRegistrationActivity1.class));
                dialog.dismiss();
            }
        });

        new_register_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
            }
        });

        setInProgress(false);
        signIn_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userlogin();
            }
        });

        forgot_pass_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
            }
        });

        select_language_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeLanguage();
            }
        });

    }
    private void changeLanguage() {
        final String languages[] ={"English","मराठी","हिंदी","ಕನ್ನಡ"};
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        mBuilder.setTitle("Select Language");
        mBuilder.setSingleChoiceItems(languages, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                if(which==0){
                    setLocale("en");
                    recreate();
                }
                else if(which==1){
                    setLocale("mr");
                    recreate();
                }
                else if(which==2){
                    setLocale("hi");
                    recreate();
                }
                else if(which==3){
                    setLocale("ka");
                    recreate();
                }
            }
        });
        mBuilder.create();
        mBuilder.show();
    }
    private void setLocale(String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getBaseContext().getResources().updateConfiguration(configuration,getBaseContext().getResources().getDisplayMetrics());

        SharedPreferences.Editor editor = getSharedPreferences("Settings",MODE_PRIVATE).edit();
        editor.putString("app_lang",language);
        editor.apply();
    }
    private void loadLocale(){
        SharedPreferences preferences = getSharedPreferences("Settings",MODE_PRIVATE);
        String language = preferences.getString("app_lang","");
        setLocale(language);
    }
    void userlogin(){
        String email = login_Email_edt.getText().toString();
        String password = login_Pass_edt.getText().toString();

        if(email.isEmpty()){
            login_Email_edt.setError("Email is required!");
            return;
        }
        if(password.isEmpty()){
            login_Pass_edt.setError("Password is required!");
            return;
        }

        setInProgress(true);
        mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        setInProgress(false);
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        String userid = firebaseUser.getUid();
                        DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("UserRegisteredData");
                        referenceProfile.child(userid).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                ReadUserModel readUserModel = snapshot.getValue(ReadUserModel.class);
                                if (readUserModel != null){
                                    checkEmail = readUserModel.getEmailid();
                                    if (task.isSuccessful()){
                                        finish();
                                        if(email.equals(checkEmail)){
                                            Toast.makeText(LoginActivity.this,"Login Successfull",Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(LoginActivity.this, UserHome.class));
                                            finish();
                                        }
                                    } else{
                                        Toast.makeText(LoginActivity.this,"Failed to login! please check entered details",Toast.LENGTH_LONG).show();
                                        setInProgress(false);
                                    }
                                }else{
                                    if (task.isSuccessful()) {
                                        Toast.makeText(LoginActivity.this,"Login Successfull",Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(LoginActivity.this, WorkerHome.class));
                                        finish();
                                    }else {
                                        Toast.makeText(LoginActivity.this,"Failed to login! please check entered details",Toast.LENGTH_LONG).show();
                                        setInProgress(false);
                                    }
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(LoginActivity.this,"NETWORK ERROR",Toast.LENGTH_SHORT).show();
                            }
                        });
//                        if (task.isSuccessful()) {
//                            finish();
//                            Toast.makeText(LoginActivity.this,"Login Successfull",Toast.LENGTH_SHORT).show();
//                            startActivity(new Intent(LoginActivity.this, WorkerHome.class));
//                            finish();
//                        }else {
//                            Toast.makeText(LoginActivity.this, "Failed to login! please check entered details", Toast.LENGTH_LONG).show();
//                            setInProgress(false);
//                        }
                    }
                });
    }
    void setInProgress(boolean inProgress){
        if(inProgress){
            login_ProgressBar.setVisibility(View.VISIBLE);
            signIn_btn.setVisibility(View.GONE);
        }else {
            login_ProgressBar.setVisibility(View.GONE);
            signIn_btn.setVisibility(View.VISIBLE);
        }
    }
}