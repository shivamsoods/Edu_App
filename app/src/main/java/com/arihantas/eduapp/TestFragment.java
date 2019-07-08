package com.arihantas.eduapp;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class TestFragment extends Fragment {


    private Button btnA, btnB, btnC, btnD, btnSubmit, btnNext, btnPrev,btnFlag;
    private TextView tvQuestion, tvLayout;
    private String optionSelected = "none";
    private int totalItem;
    private int listItem = 0;
    private ArrayList<mcqQuestion> quesList = new ArrayList<>();
    private ArrayList<mcqQuestion> quesList1 = new ArrayList<>();
    private int score = 0;
    private boolean isLoading = true;
    private RelativeLayout rlMcqProgress;
    private FirebaseAuth mAuth;
    private  boolean allAnswered=false;
    private RecyclerView rvQuesLayout;
    private RecyclerView.LayoutManager layoutManager;
    private QuestionLayoutRecyclerAdapter adapter;


    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("Question");

    public TestFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_test, container, false);

        btnA = view.findViewById(R.id.btn_mcq_A);
        btnB = view.findViewById(R.id.btn_mcq_B);
        btnC = view.findViewById(R.id.btn_mcq_C);
        btnD = view.findViewById(R.id.btn_mcq_D);
        btnSubmit = view.findViewById(R.id.btn_mcq_submit);
        btnNext = view.findViewById(R.id.btn_mcq_next);
        btnPrev = view.findViewById(R.id.btn_mcq_prev);
        btnFlag=view.findViewById(R.id.btn_mcq_flag);

        tvQuestion = view.findViewById(R.id.tv_mcq_question);
        tvLayout = view.findViewById(R.id.tv_mcq_question_layout);

        rvQuesLayout=view.findViewById(R.id.rv_layout_question_test);
        makeRecyclerView();

        rlMcqProgress = view.findViewById(R.id.rl_mcq_progress);
        rlMcqProgress.setVisibility(View.VISIBLE);


        mAuth = FirebaseAuth.getInstance();
        getFData();


        btnA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quesList.get(listItem).setSelAnswer("optA");
                setButtonColor();
                btnA.setBackgroundResource(R.color.sky_blue);

            }
        });

        btnB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quesList.get(listItem).setSelAnswer("optB");
                setButtonColor();
                btnB.setBackgroundResource(R.color.sky_blue);
            }
        });
        btnC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quesList.get(listItem).setSelAnswer("optC");
                setButtonColor();
                btnC.setBackgroundResource(R.color.sky_blue);
            }
        });
        btnD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quesList.get(listItem).setSelAnswer("optD");
                setButtonColor();
                btnD.setBackgroundResource(R.color.sky_blue);
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                displayNextQues();

            }
        });

        btnFlag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(quesList.get(listItem).isFlag()){
                    btnFlag.setBackgroundColor(ContextCompat.getColor(Objects.requireNonNull(getActivity()),R.color.grey_light));
                    btnFlag.setTextColor(ContextCompat.getColor(Objects.requireNonNull(getActivity()),R.color.grey));
                    quesList.get(listItem).setFlag(false);}
                else {
                    btnFlag.setBackgroundColor(ContextCompat.getColor(Objects.requireNonNull(getActivity()),R.color.grey));
                    btnFlag.setTextColor(ContextCompat.getColor(Objects.requireNonNull(getActivity()),R.color.white));
                    quesList.get(listItem).setFlag(true);
                }

            }
        });

        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayPrevQues();
            }
        });


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i=0;i<totalItem;i++){
                    if(quesList.get(i).getSelAnswer().equals(quesList.get(i).getAnswer())){
                        score++;
                    }
                    if(quesList.get(i).getSelAnswer().equals("none")){
                        allAnswered=false;

                    }
                    else {
                        allAnswered=true;
                    }
                }


                if(allAnswered){
                    Toast.makeText(getActivity(), "Your Score is "+ score, Toast.LENGTH_LONG).show();

                    assert getFragmentManager() != null;
                    getFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, new StudyFragment())
                            .commit();
                }
                else {
                    Toast.makeText(getActivity(), "Answer all the questions", Toast.LENGTH_SHORT).show();
                }

            }
        });
        return view;
    }

    private void makeRecyclerView(){
        layoutManager=new LinearLayoutManager(getContext());
        //rvQuesLayout.setHasFixedSize(true);
        rvQuesLayout.setLayoutManager(layoutManager);
        adapter=new QuestionLayoutRecyclerAdapter(quesList1);
        rvQuesLayout.setAdapter(adapter);
    }

    private void setDisplay(int item) {

        btnA.setText(quesList.get(item).getOptA());
        btnB.setText(quesList.get(item).getOptB());
        btnC.setText(quesList.get(item).getOptC());
        btnD.setText(quesList.get(item).getOptD());
        tvQuestion.setText(quesList.get(item).getQuestion());

    }


    private void getFData() {

        myRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                    String qtext = (String) messageSnapshot.child("qText").getValue();
                    String optA = (String) messageSnapshot.child("optA").getValue();
                    String optB = (String) messageSnapshot.child("optB").getValue();
                    String optC = (String) messageSnapshot.child("optC").getValue();
                    String optD = (String) messageSnapshot.child("optD").getValue();
                    String answer = (String) messageSnapshot.child("answer").getValue();

                    quesList.add(new mcqQuestion(qtext, optA, optB, optC, optD, answer,"none",false));
                    Log.d("TOTAL ITEM", String.valueOf(quesList.size()));

                }

                totalItem = quesList.size();
                isLoading = false;

                if (!isLoading) {
                    setDisplay(listItem);
                    rlMcqProgress.setVisibility(View.GONE);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void setButtonColor() {
        btnA.setBackgroundResource(R.color.grey_light);
        btnB.setBackgroundResource(R.color.grey_light);
        btnC.setBackgroundResource(R.color.grey_light);
        btnD.setBackgroundResource(R.color.grey_light);

    }



    private void displayNextQues() {

        if (listItem < totalItem-1) {
            listItem++;
            setDisplay(listItem);
            setButtonColor();

            switch (quesList.get(listItem).getSelAnswer()) {
                case "optA":
                    btnA.setBackgroundResource(R.color.sky_blue);
                    break;
                case "optB":
                    btnB.setBackgroundResource(R.color.sky_blue);
                    break;
                case "optC":
                    btnC.setBackgroundResource(R.color.sky_blue);
                    break;
                case "optD":
                    btnD.setBackgroundResource(R.color.sky_blue);
                    break;
                default:
                    setButtonColor();
                    break;


            }


        } else {
            btnNext.setVisibility(View.GONE);
            btnSubmit.setVisibility(View.VISIBLE);
        }


    }

    private void displayPrevQues() {
btnNext.setVisibility(View.VISIBLE);

        if (listItem > 0 && listItem < totalItem) {
            listItem--;
            setDisplay(listItem);
            setButtonColor();

            switch (quesList.get(listItem).getSelAnswer()) {
                case "optA":
                    btnA.setBackgroundResource(R.color.sky_blue);
                    break;
                case "optB":
                    btnB.setBackgroundResource(R.color.sky_blue);
                    break;
                case "optC":
                    btnC.setBackgroundResource(R.color.sky_blue);
                    break;
                case "optD":
                    btnD.setBackgroundResource(R.color.sky_blue);
                    break;
                default:
                    setButtonColor();
                    break;


            }

        } else {

            Toast.makeText(getActivity(), "Cannot GO back!!", Toast.LENGTH_SHORT).show();
        }


    }


}
