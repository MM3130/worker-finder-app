package com.mm.pwfind;

import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class SearchWorkerActivity extends AppCompatActivity {

    private ImageButton searchBack_img_btn,search_btn;
    private EditText search_worker_edt;
    private RecyclerView search_recyclerView;
    MyAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.search_worker_activity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.search), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        search_worker_edt = findViewById(R.id.search_edt);
        search_worker_edt.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        search_recyclerView = findViewById(R.id.search_user_recyclerview);
        searchBack_img_btn = findViewById(R.id.back_btn);
        search_btn = findViewById(R.id.search_worker_btn);

        search_worker_edt.requestFocus();

        searchBack_img_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchTerm = search_worker_edt.getText().toString();
                if(searchTerm.isEmpty() || searchTerm.length()<3){
                    search_worker_edt.setError("Invalid input");
                    return;
                }
                setupSearchRecyclerView(searchTerm);
            }
        });

    }

    void setupSearchRecyclerView(String searchData){

        FirebaseRecyclerOptions<RecyclerViewModel> options =
                new FirebaseRecyclerOptions.Builder<RecyclerViewModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("WorkerRegisteredDetails").orderByChild("skill").startAt(searchData).endAt(searchData+"~"), RecyclerViewModel.class)
                        .build();
        adapter = new MyAdapter(options,getApplicationContext());
        search_recyclerView.setLayoutManager(new LinearLayoutManager(this));
        search_recyclerView.setAdapter(adapter);
        adapter.startListening();

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(adapter!=null){
            adapter.startListening();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(adapter!=null){
            adapter.stopListening();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(adapter!=null){
            adapter.startListening();
        }
    }
}