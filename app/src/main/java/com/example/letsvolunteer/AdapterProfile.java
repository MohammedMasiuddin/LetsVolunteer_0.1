package com.example.letsvolunteer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdapterProfile extends RecyclerView.Adapter<AdapterProfile.ViewHolder> {
    List<MyDataType> list;
    public Context context;
    public AdapterProfile(List<MyDataType> list){
        this.list=list;
    }
    @NonNull
    @org.jetbrains.annotations.NotNull
    @Override
    public AdapterProfile.ViewHolder onCreateViewHolder(@NonNull @org.jetbrains.annotations.NotNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.profile,parent,false);
        context=parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @org.jetbrains.annotations.NotNull AdapterProfile.ViewHolder holder, int position) {
        MyDataType object=list.get(position);
        holder.setData(list.get(position).name,list.get(position).date_of_post);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.profile_title2)
        TextView name;
        @BindView(R.id.profile_picture_view)
        ImageView imageView;

        @BindView(R.id.profile_timeOrDateOfPost)
        TextView timeOrDateOfPost;
        @BindView(R.id.linearLayout)
        LinearLayout linearLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
        public void setData(String nameData,String dateOfPost){
            name.setText(nameData);
            if (!dateOfPost.matches("null")){timeOrDateOfPost.setText(dateOfPost);
            }
            //imageView.setBackgroundResource(R.drawable.nameOfImage);
        }
    }
}
