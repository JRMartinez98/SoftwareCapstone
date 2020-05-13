//Thomas Haines and Jose Martinez
//Answer class, holds information for the four answers choices to a question and which one is the correct answer

package com.example.fa_re;

public class Answer {

    private int[] mAnswers = new int[4];
    private String[] mAnswerString = new String[4];


    public Answer(String a, String b, String c, String d){
        mAnswerString[0] = a;
        mAnswerString[1] = b;
        mAnswerString[2] = c;
        mAnswerString[3] = d;
    }
    public int[] getAnswers() {
        return mAnswers;
    }

    public String[] getAnswerString() {
        return mAnswerString;
    }


}
