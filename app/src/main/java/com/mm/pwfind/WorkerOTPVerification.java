package com.mm.pwfind;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class WorkerOTPVerification extends AppCompatActivity {

    private String name,skill,landmark,city,state,verified,gender,mobile,dateofbirth,email,password;
    private EditText workerOTP_edt;
    private Button workerR_btn;
    private ProgressBar workerR_progressBar;
    private TextView worker_otp_resent_txt;
    private Uri imgUri;
    private long timeoutSeconds = 60l;
    private String verifcationCode;
    private PhoneAuthProvider.ForceResendingToken fResendingToken;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.worker_otp_verification_activity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.worker_otp), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        name = getIntent().getExtras().getString("name");
        skill = getIntent().getExtras().getString("skill");
        landmark = getIntent().getExtras().getString("landmark");
        city = getIntent().getExtras().getString("city");
        state = getIntent().getExtras().getString("state");
        verified = getIntent().getExtras().getString("verified");
        imgUri = getIntent().getParcelableExtra("img");
        gender = getIntent().getExtras().getString("gender");
        mobile = getIntent().getExtras().getString("mobile");
        dateofbirth = getIntent().getExtras().getString("dob");
        email = getIntent().getExtras().getString("emailId_w");
        password = getIntent().getExtras().getString("password_w");

        workerOTP_edt = findViewById(R.id.worker_otp_edt);
        workerR_progressBar = findViewById(R.id.worker_register_otp_progressBar);
        workerR_btn = findViewById(R.id.register_worker_btn);
        worker_otp_resent_txt = findViewById(R.id.wotp_Resend_txt);

        sendOTP(mobile,false);

        worker_otp_resent_txt.setOnClickListener(view -> {
            sendOTP(mobile,true);
        });

        workerR_btn.setOnClickListener(view -> {
            String enteredOTP = workerOTP_edt.getText().toString();
            PhoneAuthCredential credential =  PhoneAuthProvider.getCredential(verifcationCode,enteredOTP);
            signIn(credential);
            setInProgress(true);
        });
    }
    void sendOTP(String phoneNumber,boolean isResent){
        startResendTimer();
        setInProgress(true);
        PhoneAuthOptions.Builder builder =
        PhoneAuthOptions.newBuilder(mAuth)
        .setPhoneNumber(mobile)
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
                Toast.makeText(WorkerOTPVerification.this,"OTP not sent due to internal error!",Toast.LENGTH_LONG).show();
                setInProgress(false);
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                verifcationCode = s;
                fResendingToken = forceResendingToken;
                Toast.makeText(WorkerOTPVerification.this,"OTP sent successfully",Toast.LENGTH_LONG).show();
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
                    saveWorkerData();
                }else {
                    Toast.makeText(WorkerOTPVerification.this,"OTP varificatin faild!",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    void saveWorkerData(){
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                         if(task.isSuccessful()){
                             ProgressDialog dialog = new ProgressDialog(WorkerOTPVerification.this);
                             dialog.setTitle("File Uploding");
                             dialog.show();
                             FirebaseStorage storage = FirebaseStorage.getInstance();
                             StorageReference uploader = storage.getReference("Image1" + new Random().nextInt(100));
                             uploader.putFile(imgUri)
                                     .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                         @Override
                                         public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                             uploader.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                 @Override
                                                 public void onSuccess(Uri uri) {
                                                     dialog.dismiss();
                                                     FirebaseDatabase db = FirebaseDatabase.getInstance();
                                                     DatabaseReference root = db.getReference("WorkerRegisteredDetails");
                                                     WorkerModel workerModel = new WorkerModel(name,skill,landmark,city,state,verified,gender,mobile,dateofbirth,email,password,uri.toString());
                                                     root.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(workerModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                         @Override
                                                         public void onComplete(@NonNull Task<Void> task) {
                                                             if(task.isSuccessful()){
                                                                 Toast.makeText(WorkerOTPVerification.this,"Worker has been registered successfully!",Toast.LENGTH_SHORT).show();
                                                                 startActivity(new Intent(WorkerOTPVerification.this, LoginActivity.class));
                                                                 finish();
                                                             }
                                                             else{
                                                                 Toast.makeText(WorkerOTPVerification.this,"Failed to register! try again",Toast.LENGTH_LONG).show();
                                                             }
                                                         }
                                                     });
                                                 }
                                             });

                                         }
                                     }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onProgress(@NonNull @NotNull UploadTask.TaskSnapshot snapshot) {
                                        float percent = (100*snapshot.getBytesTransferred()/snapshot.getTotalByteCount());
                                        dialog.setMessage("Uploaded :" +(int)percent+" %");
                                        }
                                    });

                         }else {
                             Toast.makeText(WorkerOTPVerification.this,"Failed to register!",Toast.LENGTH_LONG).show();
                         }
                    }
                });
    }

    void startResendTimer(){
        worker_otp_resent_txt.setEnabled(false);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                timeoutSeconds--;
                worker_otp_resent_txt.setText("Resend OTP in "+timeoutSeconds+" seconds");
                if(timeoutSeconds<=0){
                    timeoutSeconds=60l;
                    timer.cancel();
                    runOnUiThread(() -> {
                        worker_otp_resent_txt.setEnabled(true);
                    });
                }
            }
        },0,1000);
    }
    void setInProgress(boolean inProgress){
        if(inProgress){
            workerR_progressBar.setVisibility(View.VISIBLE);
            workerR_btn.setVisibility(View.GONE);
        }else {
            workerR_progressBar.setVisibility(View.GONE);
            workerR_btn.setVisibility(View.VISIBLE);
        }
    }
}