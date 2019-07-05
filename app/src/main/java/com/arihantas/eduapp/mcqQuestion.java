package com.arihantas.eduapp;


import android.app.Activity;

public class mcqQuestion extends Activity {


    private String question;
    private String optA;
    private String optB;
    private String optC;
    private String optD;
    private String answer;
    private String selAnswer;


    public mcqQuestion( String question, String optA, String optB, String optC, String optD, String answer,String selAnswer) {

        this.question = question;
        this.optA = optA;
        this.optB = optB;
        this.optC = optC;
        this.optD = optD;
        this.answer = answer;
        this.selAnswer=selAnswer;
    }



    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getOptA() {
        return optA;
    }

    public void setOptA(String optA) {
        this.optA = optA;
    }

    public String getOptB() {
        return optB;
    }

    public void setOptB(String optB) {
        this.optB = optB;
    }

    public String getOptC() {
        return optC;
    }

    public void setOptC(String optC) {
        this.optC = optC;
    }

    public String getOptD() {
        return optD;
    }

    public void setOptD(String optD) {
        this.optD = optD;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getSelAnswer() {return selAnswer;}

    public void setSelAnswer(String selAnswer) {this.selAnswer = selAnswer;}
}


