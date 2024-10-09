package com.mm.pwfind;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText email_forgot_edt;
    private Button forgot_Pass_btn;
    private ProgressBar forgot_ProgressBar;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.forgot_password_activity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.forgot), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        email_forgot_edt = findViewById(R.id.forgot_email_edt);
        forgot_ProgressBar = findViewById(R.id.forgot_otp_progressBar);
        forgot_Pass_btn = findViewById(R.id.get_forgot_link);

        setInProgress(false);

        forgot_Pass_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetPassword();
            }
        });
    }
    private void resetPassword(){
        String email = email_forgot_edt.getText().toString();

        if(email.isEmpty()){
            email_forgot_edt.setError("Email is required!");
            return;
        }

        setInProgress(true);
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                setInProgress(false);
                if(task.isSuccessful()){
                    Toast.makeText(ForgotPasswordActivity.this,"Check your email to reset your password!",Toast.LENGTH_LONG).show();
                    finish();
                }else {
                    Toast.makeText(ForgotPasswordActivity.this,"try again! Something wrong",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    void setInProgress(boolean inProgress){
        if(inProgress){
            forgot_ProgressBar.setVisibility(View.VISIBLE);
            forgot_Pass_btn.setVisibility(View.GONE);
        }else {
            forgot_ProgressBar.setVisibility(View.GONE);
            forgot_Pass_btn.setVisibility(View.VISIBLE);
        }
    }
}