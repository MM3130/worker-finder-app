package com.mm.pwfind;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyAdapter extends FirebaseRecyclerAdapter<RecyclerViewModel,MyAdapter.myViewHolder> {
    Context context;
    public MyAdapter(@NonNull FirebaseRecyclerOptions<RecyclerViewModel> options) {
        super(options);
    }
    public MyAdapter(@NonNull FirebaseRecyclerOptions<RecyclerViewModel> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder myViewHolder, int i, @NonNull RecyclerViewModel recyclerViewModel) {
        myViewHolder.name.setText(recyclerViewModel.getName());
        myViewHolder.skill.setText(recyclerViewModel.getSkill());
        myViewHolder.city.setText(recyclerViewModel.getCity());
        myViewHolder.status.setText(recyclerViewModel.getVerified());
        Glide.with(myViewHolder.img.getContext()).load(recyclerViewModel.getImage()).into(myViewHolder.img);
        myViewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.user_home_framelayout,new WorkerDetailsShowFrag(recyclerViewModel.getName(),recyclerViewModel.getSkill(),recyclerViewModel.getLandmark(),recyclerViewModel.getCity(),recyclerViewModel.getState(),recyclerViewModel.getVerified(),recyclerViewModel.getGender(),recyclerViewModel.getMobile(),recyclerViewModel.getImage())).addToBackStack(null).commit();
            }
        });
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.worker_details_show_cardview,parent,false);
        return new myViewHolder(view);
    }

    public class myViewHolder extends RecyclerView.ViewHolder {

        CircleImageView img;
        TextView name,skill,city,status;
        LinearLayout linearLayout;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            img =(CircleImageView) itemView.findViewById(R.id.display_img);
            name =(TextView) itemView.findViewById(R.id.display_Fname);
            skill =(TextView) itemView.findViewById(R.id.display_worktype);
            city =(TextView) itemView.findViewById(R.id.display_city);
            status = (TextView)itemView.findViewById(R.id.display_status);
            linearLayout=(LinearLayout) itemView.findViewById(R.id.display_layout);
        }
    }
}
