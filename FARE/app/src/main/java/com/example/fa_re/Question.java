//Thomas Haines and Jose Martinez
//Question class, holds information for one question

package com.example.fa_re;

public class Question {

    private int mTextResId;
    private int mAnswer;

    private String mQuestionString;

    public Question (String question, int answer) {
        mQuestionString = question;
        mAnswer = answer;
    }


    public int getTextResId() {
        return mTextResId;
    }

    public void setTextResId(int textResId) {
        mTextResId = textResId;
    }


    public int isAnswerTrue() {
        return mAnswer;
    }

    public String getQuestionString() {
        return mQuestionString;
    }

}
