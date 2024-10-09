package com.mm.pwfind;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;


public class WorkerFragment extends Fragment {

    private RecyclerView recyclerView_WorkerFrag;
    MyAdapter myAdapter;

    public WorkerFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_worker, container, false);

        recyclerView_WorkerFrag = view.findViewById(R.id.recyclerview_worker_frag);
        recyclerView_WorkerFrag.setLayoutManager(new LinearLayoutManager(getContext()));
        FirebaseRecyclerOptions<RecyclerViewModel> options =
                new FirebaseRecyclerOptions.Builder<RecyclerViewModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("WorkerRegisteredDetails"), RecyclerViewModel.class)
                        .build();

        myAdapter = new MyAdapter(options);
        recyclerView_WorkerFrag.setAdapter(myAdapter);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(myAdapter!=null){
            myAdapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(myAdapter!=null){
            myAdapter.stopListening();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(myAdapter!=null){
            myAdapter.startListening();
        }
    }
}