package com.mm.pwfind;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.hbb20.CountryCodePicker;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class WorkerRegistrationActivity2 extends AppCompatActivity {

    private String name,skill,landmark,city,state,verified;
    private CircleImageView wImage;
    private RadioGroup wInfo_gender;
    private RadioButton wInfo_m,wInfo_f,wInfo_other;
    private CountryCodePicker wCountryCodePicker;
    private EditText workerR_mobile,workerR_dateofbirth,workerR_email,workerR_password;
    private Button worker_otpGet_btn;
    private static final int REQUEST_CODE_OPEN_DOCUMENT = 1;
    private int year,month,day;
    private Uri imageUri;
    private Calendar selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.worker_registration_activity2);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.worker_register2), (v, insets) -> {
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

        wImage = findViewById(R.id.worker_img);
        wInfo_gender = findViewById(R.id.info_gendergroup);
        wInfo_m = findViewById(R.id.info_male);
        wInfo_f = findViewById(R.id.info_female);
        wInfo_other = findViewById(R.id.info_other);
        wCountryCodePicker = findViewById(R.id.w_register_country_code);
        workerR_mobile = findViewById(R.id.wRegister_mobileNo_edt);
        workerR_dateofbirth = findViewById(R.id.wRegister_dateofbirth_edt);
        workerR_email = findViewById(R.id.wRegister_emailid_edit);
        workerR_password = findViewById(R.id.wRegister_password_edt);
        worker_otpGet_btn = findViewById(R.id.w_getOTP_btn);
        wCountryCodePicker.registerCarrierNumberEditText(workerR_mobile);

        wImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gallery = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                gallery.setType("image/*");
                gallery.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(gallery,"Select Picture"),REQUEST_CODE_OPEN_DOCUMENT);
            }
        });

        workerR_dateofbirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });

        worker_otpGet_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getWorkerDetails();
            }
        });
    }

    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                WorkerRegistrationActivity2.this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    Calendar selectedDate = Calendar.getInstance();
                    selectedDate.set(selectedYear, selectedMonth, selectedDay);
                    if (isAgeGreaterThan18(selectedDate)) {
                        String dateString = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                        workerR_dateofbirth.setText(dateString);
                    } else {
                        workerR_dateofbirth.setText("");
                        Toast.makeText(WorkerRegistrationActivity2.this, "Age must be greater than 18", Toast.LENGTH_SHORT).show();
                    }
                },
                year, month, day);
        datePickerDialog.show();
    }

    private boolean isAgeGreaterThan18(Calendar selectedDate) {
        Calendar today = Calendar.getInstance();
        int age = today.get(Calendar.YEAR) - selectedDate.get(Calendar.YEAR);
        if (today.get(Calendar.DAY_OF_YEAR) < selectedDate.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }
        return age >= 18;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_OPEN_DOCUMENT && resultCode == RESULT_OK){
            imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imageUri);
                wImage.setImageBitmap(bitmap);
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
    void getWorkerDetails(){
        String gender = "";
        String dateofbirth = workerR_dateofbirth.getText().toString();
        String email_w = workerR_email.getText().toString();
        String password_w = workerR_password.getText().toString();
        if(imageUri==null){
            Toast.makeText(WorkerRegistrationActivity2.this,"select your image",Toast.LENGTH_LONG).show();
            return;
        }
        if(!wInfo_m.isChecked() && !wInfo_m.isChecked() && !wInfo_m.isChecked()){
            Toast.makeText(WorkerRegistrationActivity2.this,"gender required",Toast.LENGTH_LONG).show();
            return;
        }
        if(wInfo_m.isChecked()){
            gender="MALE";
        }
        if(wInfo_f.isChecked()){
            gender="FEMALE";
        }
        if(wInfo_other.isChecked()){
            gender="OTHER";
        }
        if(!wCountryCodePicker.isValidFullNumber()){
            workerR_mobile.setError("mobile number not valid");
            return;
        }
        if(dateofbirth.isEmpty()){
            Toast.makeText(WorkerRegistrationActivity2.this,"date of birth required",Toast.LENGTH_LONG).show();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email_w).matches()){
            workerR_email.setError("enter valid email");
            return;
        }
        if(password_w.isEmpty() | password_w.length()<6){
            workerR_password.setError("enter 6 char password");
            return;
        }
        String finalGender = gender;
        Intent intent = new Intent(WorkerRegistrationActivity2.this, WorkerOTPVerification.class);

        intent.putExtra("name",name);
        intent.putExtra("skill",skill);
        intent.putExtra("landmark",landmark);
        intent.putExtra("city",city);
        intent.putExtra("state",state);
        intent.putExtra("verified",verified);
        intent.putExtra("img",imageUri);
        intent.putExtra("gender",finalGender);
        intent.putExtra("mobile",wCountryCodePicker.getFullNumberWithPlus());
        intent.putExtra("dob",dateofbirth);
        intent.putExtra("emailId_w",email_w);
        intent.putExtra("password_w",password_w);
        startActivity(intent);
    }
}