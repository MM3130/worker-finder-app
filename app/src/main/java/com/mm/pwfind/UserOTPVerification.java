package com.mm.pwfind;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class UserOTPVerification extends AppCompatActivity {
    private String name,mobileno,emailid,password;
    private EditText userOTP_edt;
    private Button userRegister_btn;
    private ProgressBar userOTP_Progressbar;
    private TextView user_otp_resent_txt;
    private long timeoutSeconds = 60l;
    private String verifcationCode;
    private PhoneAuthProvider.ForceResendingToken fResendingToken;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.user_otp_verification_activity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.user_otp), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        name = getIntent().getExtras().getString("name");
        mobileno = getIntent().getExtras().getString("mobileNo");
        emailid = getIntent().getExtras().getString("emailId");
        password = getIntent().getExtras().getString("password");

        userOTP_edt = findViewById(R.id.user_otp_edt);
        userOTP_Progressbar = findViewById(R.id.register_otp_progressBar);
        userRegister_btn = findViewById(R.id.user_register_btn);
        user_otp_resent_txt = findViewById(R.id.otp_Resend_txt);

        sendOTP(mobileno,false);

        user_otp_resent_txt.setOnClickListener(view -> {
            sendOTP(mobileno,true);
        });

        userRegister_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String enteredOTP = userOTP_edt.getText().toString();
                PhoneAuthCredential credential =  PhoneAuthProvider.getCredential(verifcationCode,enteredOTP);
                signIn(credential);
                setInProgress(true);
            }
        });
    }
    void sendOTP(String mobileno,boolean isResent){
        startResendTimer();
        setInProgress(true);
        PhoneAuthOptions.Builder builder =
        PhoneAuthOptions.newBuilder(mAuth)
        .setPhoneNumber(mobileno)
        .setTimeout(timeoutSeconds, TimeUnit.SECONDS)
        .setActivity(this)
        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                signIn(phoneAuthCredential);
                setInProgress(false);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Toast.makeText(UserOTPVerification.this,"OTP not sent due to internal error!",Toast.LENGTH_LONG).show();
                setInProgress(false);
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                verifcationCode = s;
                fResendingToken = forceResendingToken;
                Toast.makeText(UserOTPVerification.this,"OTP sent successfully",Toast.LENGTH_LONG).show();
                setInProgress(false);
            }
        });
        if(isResent){
            PhoneAuthProvider.verifyPhoneNumber(builder.setForceResendingToken(fResendingToken).build());
        }else {
            PhoneAuthProvider.verifyPhoneNumber(builder.build());
        }
    }
    void signIn(PhoneAuthCredential phoneAuthCredential){
        setInProgress(true);
        mAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    saveUserData();
                }else {
                    Toast.makeText(UserOTPVerification.this,"OTP varificatin faild!",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    void saveUserData(){
        mAuth.createUserWithEmailAndPassword(emailid,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            UserModel userModel = new UserModel(name,mobileno,emailid,password);
                            FirebaseDatabase.getInstance().getReference("UserRegisteredData")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(userModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(UserOTPVerification.this,"User has been registered successfully!",Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(UserOTPVerification.this, LoginActivity.class));
                                                finish();
                                            }else {
                                                Toast.makeText(UserOTPVerification.this,"Failed to register! try again",Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                        }else {
                            Toast.makeText(UserOTPVerification.this,"Failed to register!",Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
    void startResendTimer(){
        user_otp_resent_txt.setEnabled(false);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                timeoutSeconds--;
                user_otp_resent_txt.setText("Resend OTP in "+timeoutSeconds+" seconds");
                if(timeoutSeconds<=0){
                    timeoutSeconds=60l;
                    timer.cancel();
                    runOnUiThread(() -> {
                        user_otp_resent_txt.setEnabled(true);
                    });
                }
            }
        },0,1000);
    }
    void setInProgress(boolean inProgress){
        if(inProgress){
            userOTP_Progressbar.setVisibility(View.VISIBLE);
            userRegister_btn.setVisibility(View.GONE);
        }else {
            userOTP_Progressbar.setVisibility(View.GONE);
            userRegister_btn.setVisibility(View.VISIBLE);
        }
    }
}