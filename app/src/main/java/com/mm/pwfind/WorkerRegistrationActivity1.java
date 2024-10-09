package com.mm.pwfind;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class WorkerRegistrationActivity1 extends AppCompatActivity {
    private EditText workerR_name,workerR_landmark,workerR_city,workerR_state;
    private Spinner workerR_skill;
    private String skill="";
    private Button workerR_nextBtn;
    private TextView back_to_login;
    private String workerR_verified = "In process";
    private String[] skillList = {"Select work type","ELECTRICIAN","PLUMBER","CIVIL","AC REPAIR","LABOUR","AUTOMOTIVE MECHANIC","CARPENTERS","CONSTRUCTION LABORER","WELDER","FARMER","PAINTER","DRIVER"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.worker_registration_activity1);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.worker_register1), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        workerR_name = findViewById(R.id.name_edit_worker);
        workerR_name.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        workerR_skill = findViewById(R.id.skill_select_worker);
        workerR_landmark = findViewById(R.id.landmark_edit_worker);
        workerR_landmark.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        workerR_city = findViewById(R.id.city_edit_worker);
        workerR_city.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        workerR_state = findViewById(R.id.state_edit_worker);
        workerR_state.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        back_to_login = findViewById(R.id.wtxt_backto_loginAcc);
        workerR_nextBtn = findViewById(R.id.wNext_btn);

        back_to_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(WorkerRegistrationActivity1.this, LoginActivity.class));
                finish();
            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(WorkerRegistrationActivity1.this, android.R.layout.simple_spinner_item,skillList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        workerR_skill.setAdapter(adapter);
        workerR_skill.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String pos = adapterView.getItemAtPosition(i).toString();
                if(!pos.equals(skillList[0])){
                    skill = adapterView.getItemAtPosition(i).toString();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(WorkerRegistrationActivity1.this,"select your work type",Toast.LENGTH_LONG).show();
            }
        });

        workerR_nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = workerR_name.getText().toString();
                String landmark = workerR_landmark.getText().toString();
                String city = workerR_city.getText().toString();
                String state = workerR_state.getText().toString();
                if(name.isEmpty()){
                    workerR_name.setError("name required");
                    return;
                }
                if (skill.isEmpty()){
                    Toast.makeText(WorkerRegistrationActivity1.this,"select your work type",Toast.LENGTH_LONG).show();
                    return;
                }
                if(landmark.isEmpty()){
                    workerR_landmark.setError("landmark missing");
                    return;
                }
                if(city.isEmpty()){
                    workerR_city.setError("city name required");
                    return;
                }
                if(state.isEmpty()){
                    workerR_state.setError("state name required");
                    return;
                }
                Intent intent = new Intent(WorkerRegistrationActivity1.this, WorkerRegistrationActivity2.class);
                intent.putExtra("name",name);
                intent.putExtra("skill",skill);
                intent.putExtra("landmark",landmark);
                intent.putExtra("city",city);
                intent.putExtra("state",state);
                intent.putExtra("verified",workerR_verified);
                startActivity(intent);
            }
        });

    }
}