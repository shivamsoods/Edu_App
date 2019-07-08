package com.arihantas.eduapp;

import android.content.Context;
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
        FlagViewHolder  flagViewHolder=new FlagViewHolder(view,quesList);
        return flagViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FlagViewHolder holder, int position) {
            mcqQuestion question_id= quesList.get(position);
            holder.tvQuestionNumber.setText("Question "+ (position+1));
            if(question_id.isFlag()){
                holder.ivFlag.setImageResource(R.drawable.ic_launcher_foreground);
            }
            else {
                holder.ivFlag.setImageResource(R.drawable.common_google_signin_btn_icon_dark_normal);
            }


    }

    @Override
    public int getItemCount() {
        return quesList.size();
    }


    public static class FlagViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView ivFlag;
        TextView tvQuestionNumber;
        ArrayList<mcqQuestion> quesList;

        public FlagViewHolder(@NonNull View itemView,ArrayList<mcqQuestion> quesList) {
            super(itemView);
            ivFlag=itemView.findViewById(R.id.iv_layout_flag);
            tvQuestionNumber=itemView.findViewById(R.id.tv_layout_question_number);
            itemView.setOnClickListener(this);
            this.quesList=quesList;

        }


        @Override
        public void onClick(View v) {

            int quesClick=getAdapterPosition();


        }
    }
}
