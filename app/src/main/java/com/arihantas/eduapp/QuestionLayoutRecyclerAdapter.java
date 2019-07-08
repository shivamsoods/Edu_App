package com.arihantas.eduapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class QuestionLayoutRecyclerAdapter extends RecyclerView.Adapter<QuestionLayoutRecyclerAdapter.FlagViewHolder> {

    private ArrayList<mcqQuestion> quesList;

    public QuestionLayoutRecyclerAdapter(ArrayList<mcqQuestion> quesList){
        this.quesList=quesList;
    }

    @NonNull
    @Override
    public FlagViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.question_layout,parent,false);
        FlagViewHolder  flagViewHolder=new FlagViewHolder(view);
        return flagViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FlagViewHolder holder, int position) {
            mcqQuestion question_id= quesList.get(position);
            holder.tvQuestionNumber.setText(position);
            if(question_id.isFlag()){
                holder.ivFlag.setImageResource(R.mipmap.ic_launcher_round);
            }
            else {
                holder.ivFlag.setImageResource(R.drawable.common_google_signin_btn_icon_dark_normal);
            }


    }

    @Override
    public int getItemCount() {
        return quesList.size();
    }

    public static class FlagViewHolder extends RecyclerView.ViewHolder{

        ImageView ivFlag;
        TextView tvQuestionNumber;
        public FlagViewHolder(@NonNull View itemView) {
            super(itemView);
            ivFlag=itemView.findViewById(R.id.iv_layout_flag);
            tvQuestionNumber=itemView.findViewById(R.id.tv_layout_question_number);
        }
    }
}
