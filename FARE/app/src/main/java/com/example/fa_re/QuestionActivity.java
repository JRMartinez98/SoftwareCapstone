//Thomas Haines and Jose Martinez
//This class implements a quiz using questions from text files, up to ten questions per quiz
//The questions are shuffled before the quiz starts.
//It provides the user the ability to check each answer and see the correct answer if they get it wrong
//The final score for the quiz is passed to the profile

package com.example.fa_re;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class QuestionActivity extends AppCompatActivity {

    private static final String TAG = "QuestionActivity";
    //String to be the key for saving the mCurrentIndex in Bundle
    private static final String KEY_INDEX = "index";
    private static final String KEY_QUESTIONS = "correct questions";
    private static final String KEY_QUESTION_INDEX = "question index";
    private static final String KEY_USER_ANSWER = "user pressed";

    private CheckBox mACheckBox;
    private CheckBox mBCheckBox;
    private CheckBox mCCheckBox;
    private CheckBox mDCheckBox;
    private Button mCheckAnswerButton;
    private Button mShowAnswerButton;
    private Button mNextButton;
    private TextView mQuestionTextView;
    private int mUserPressed = -1;
    private Answer[] mAnswerBank;
    private Question[] mQuestionBank;
    private Integer[] mQuestionIndex;
    private int[] mQuestionIndexSaved;
    private String mSubject;

    private int mCurrentIndex = 0;
    private int mCorrectQuestions = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        //Thomas Haines and Jose Martinez
        //Determine which file to use for testing
        String filename;
        mSubject = getIntent().getStringExtra("EXTRA_SUBJECT");
        assert mSubject != null;
        String from;
        if (mSubject.equals("Regular Languages Quiz")) {
            filename = "RegularLanguageQuestions.txt";
            from = "assets";
        } else if (mSubject.equals("Context Free Languages Quiz")) {
            filename = "ContextFreeLanguagesQuestions.txt";
            from = "assets";
        } else if (mSubject.equals("Non-Context-Free Languages Quiz")){
            filename = "NonContextFreeLanguagesQuestions.txt";
            from = "assets";
        }else {
            filename = "UserGenerated/" + mSubject;
            from = "external";
        }

        try {
            loadQuestions(filename, from);
            //Thomas Haines
            //Shuffle the questions
            List<Integer> intList = Arrays.asList(mQuestionIndex);
            Collections.shuffle(intList);
            intList.toArray(mQuestionIndex);
            Log.d(TAG, Arrays.toString(mQuestionIndex));
            mQuestionIndexSaved = new int[mQuestionIndex.length];
            for(int i=0; i<mQuestionIndex.length; i++){
                mQuestionIndexSaved[i] = mQuestionIndex[i];
            }
            Log.d(TAG, Arrays.toString(mQuestionIndexSaved));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //Get the saved instances when the screen is rotated to retain the current question and any changes made by the user to the current page
        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
            mCorrectQuestions = savedInstanceState.getInt(KEY_QUESTIONS, 0);
            mQuestionIndexSaved = savedInstanceState.getIntArray(KEY_QUESTION_INDEX);
            mUserPressed = savedInstanceState.getInt(KEY_USER_ANSWER, -1);
        }

        mQuestionTextView = findViewById(R.id.question_text_view);

        //Create checkboxes for each answer which are mutually exclusive
        mACheckBox = findViewById(R.id.a_check);
        mACheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBCheckBox.setChecked(false);
                mCCheckBox.setChecked(false);
                mDCheckBox.setChecked(false);
                mUserPressed = 0;
            }
        });

        mBCheckBox = findViewById(R.id.b_check);
        mBCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mACheckBox.setChecked(false);
                mCCheckBox.setChecked(false);
                mDCheckBox.setChecked(false);
                mUserPressed = 1;
            }
        });

        mCCheckBox = findViewById(R.id.c_check);
        mCCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mACheckBox.setChecked(false);
                mBCheckBox.setChecked(false);
                mDCheckBox.setChecked(false);
                mUserPressed = 2;
            }
        });

        mDCheckBox = findViewById(R.id.d_check);
        mDCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mACheckBox.setChecked(false);
                mBCheckBox.setChecked(false);
                mCCheckBox.setChecked(false);
                mUserPressed = 3;
            }
        });


        mCheckAnswerButton = findViewById(R.id.check_answer_button);
        mCheckAnswerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(mUserPressed);
                mACheckBox.setEnabled(false);
                mBCheckBox.setEnabled(false);
                mCCheckBox.setEnabled(false);
                mDCheckBox.setEnabled(false);
                mNextButton.setEnabled(true);
            }
        });

        //Show answer button which highlights the correct answer in green when pressed
        //This button is only shown after the user checks the answer if they get it incorrect
        mShowAnswerButton = findViewById(R.id.show_answer_button);
        mShowAnswerButton.setVisibility(View.GONE);
        mShowAnswerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int answerIsCorrect = mQuestionBank[mQuestionIndex[mCurrentIndex]].isAnswerTrue();
                setCheckBoxTextColor(answerIsCorrect, Color.GREEN);

            }
        });

        //Next button to go to the next question
        //Checks if the user has reached the end of the quiz, updates the question if it is not finished
        mNextButton = findViewById(R.id.next_button);
        mNextButton.setEnabled(false);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkCompleted(mCurrentIndex);
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                updateQuestion();
                mNextButton.setEnabled(false);
            }
        });

        updateQuestion();

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
    }

    //Thomas Haines
    //Save necessary values when the screen is rotated so information is not lost
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
        savedInstanceState.putInt(KEY_QUESTIONS, mCorrectQuestions);
        savedInstanceState.putIntArray(KEY_QUESTION_INDEX, mQuestionIndexSaved);
        savedInstanceState.putInt(KEY_USER_ANSWER, mUserPressed);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy()");
    }

    private String nextToken(Scanner scan) {

        if (scan.hasNext()) {
            return scan.next();
        }
        return null;
    }

     public static Answer[] addAnswer(int n, Answer ansArray[], Answer x) {
         List<Answer> arrList = new ArrayList<Answer>(Arrays.asList(ansArray));
         arrList.add(x);
         ansArray = arrList.toArray(ansArray);

         return ansArray;
     }

    public static Question[] addQuestion(int n, Question questArray[], Question x) {
        List<Question> arrList = new ArrayList<Question>(Arrays.asList(questArray));
        arrList.add(x);
        questArray = arrList.toArray(questArray);

        return questArray;
    }

    /**
     * Written by Jose Martinez
     * The method reads from a file and stores the questions and answers into their respective
     * Arrays for testing the user.
     * @param filename The name of the file that will be loaded with the questions.
     * @param from     The location from which the file is being read. The values can be either
     *                 assets to indicate you are reading from assets or external to read
     *                 from the external storage on the Android device.
     * @throws FileNotFoundException For the File class
     */
    private void loadQuestions(String filename, String from) throws FileNotFoundException {
        try {
            Scanner fileScan;

            if (from.equals("assets")) {
                DataInputStream textFileStream = new DataInputStream(getAssets().open(filename));
                fileScan = new Scanner(textFileStream);
            }

            else {
                File file = new File(getExternalFilesDir(null), filename);
                fileScan = new Scanner(file);
            }
            String token;

            //ArrayLists for Questions, Answers and Index
            List<Question> questionArrayList = new ArrayList<>();
            List<Answer> answerArrayList = new ArrayList<>();
            List<Integer> questionIndexList = new ArrayList<>();


            int index = 0;
            while (fileScan.hasNext()) {
                Log.d(TAG, "index is " + index);
                token = nextToken(fileScan);

                // Collects and stores question if it follows the right format.
                if (token.equals("question")) {
                    if (nextToken(fileScan).equals("=")) {
                        String question = fileScan.nextLine();
                        Log.d(TAG, "String is " + question);
                        while( !(fileScan.next().equals("answer")) ) {
                            question += "\n" + fileScan.nextLine();
                        }
                        nextToken(fileScan);
                        int answer = fileScan.nextInt();
                        Log.d(TAG, "Answer is " + answer);
                        questionArrayList.add(new Question(question, answer));
                    }
                }
                token = nextToken(fileScan);
                //Collects the answers
                if (token.equals("choices")) {
                    fileScan.nextLine();
                    String answer1 = fileScan.nextLine();
                    String answer2 = fileScan.nextLine();
                    String answer3 = fileScan.nextLine();
                    String answer4 = fileScan.nextLine();
                    answerArrayList.add(new Answer(answer1, answer2, answer3, answer4));
                    nextToken(fileScan);
                }

                questionIndexList.add(index);
                index++;
            }

            //Initializes arrays for the total questions and adds the info from the lists.
            mAnswerBank = new Answer[index];
            mQuestionBank = new Question[index];
            mQuestionIndex = new Integer[index];
            questionArrayList.toArray(mQuestionBank);
            answerArrayList.toArray(mAnswerBank);
            questionIndexList.toArray(mQuestionIndex);
            fileScan.close();


        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

    //Thomas Haines
    //Update the question, return all buttons and checkboxes to their appropriate state for the beginning of a question.
    private void updateQuestion(){
        String question = mQuestionBank[mQuestionIndexSaved[mCurrentIndex]].getQuestionString();
        mQuestionTextView.setText(question);
        mACheckBox.setText(mAnswerBank[mQuestionIndexSaved[mCurrentIndex]].getAnswerString()[0]);
        mBCheckBox.setText(mAnswerBank[mQuestionIndexSaved[mCurrentIndex]].getAnswerString()[1]);
        mCCheckBox.setText(mAnswerBank[mQuestionIndexSaved[mCurrentIndex]].getAnswerString()[2]);
        mDCheckBox.setText(mAnswerBank[mQuestionIndexSaved[mCurrentIndex]].getAnswerString()[3]);
        mACheckBox.setChecked(false);
        mBCheckBox.setChecked(false);
        mCCheckBox.setChecked(false);
        mDCheckBox.setChecked(false);
        mACheckBox.setEnabled(true);
        mBCheckBox.setEnabled(true);
        mCCheckBox.setEnabled(true);
        mDCheckBox.setEnabled(true);
        resetCheckBoxColor();
        mShowAnswerButton.setVisibility(View.GONE);
        mUserPressed = -1;
    }

    //Thomas Haines
    //Check if the answer the user selected is correct
    //Integer 0-3 passed in corresponding to a character. 0=a, 1=b, 2=c, 3=d
    private void checkAnswer(int userPressedChar) {
        int answerIsCorrect = mQuestionBank[mQuestionIndexSaved[mCurrentIndex]].isAnswerTrue();

        //If the answer is correct show the string correct! on the bottom of the screen
        if (userPressedChar == answerIsCorrect) {
            mCorrectQuestions++;
            Toast.makeText(QuestionActivity.this, R.string.correct_toast, Toast.LENGTH_SHORT).show();
        }
        //If the answer is incorrect show the string incorrect at the bottom of the string
        //Make the show answer button visible so the user can know the correct answer
        else{
            Toast.makeText(QuestionActivity.this, R.string.incorrect_toast, Toast.LENGTH_SHORT).show();
            mShowAnswerButton.setVisibility(View.VISIBLE);
            if (userPressedChar >= 0)
                setCheckBoxTextColor(userPressedChar, Color.RED);
        }
    }

    //Thomas Haines
    //Check if the user has reached the end of the quiz
    private void checkCompleted(int questionNumber) {
        //If there are more than 10 questions in the quiz stop the quiz at ten questions
        if(mQuestionBank.length >= 10) {
            if (questionNumber == 9) {
                double percentage = ((double) mCorrectQuestions / 10) * 100;
                Log.d(TAG, "In if score is" + Double.toString(percentage));
                Intent intent = ProfileActivity.newIntent(QuestionActivity.this, percentage, mSubject);
                startActivity(intent);
                mCorrectQuestions = 0;
            }
        // If there are fewer than ten questions stop the quiz once all the questions have been asked
        }else if (questionNumber == mQuestionBank.length - 1) {
            double percentage = ((double) mCorrectQuestions / mQuestionBank.length) * 100;
            Log.d(TAG, "In else score is " + Double.toString(percentage));
            Intent intent = ProfileActivity.newIntent(QuestionActivity.this, percentage, mSubject);
            startActivity(intent);
            mCorrectQuestions = 0;
        }
    }

    //Thomas Haines
    //Set the given checkbox text color to the color passed in as the parameter
    private void setCheckBoxTextColor(int checkBoxIndex, int color){
        if(checkBoxIndex == 0){
            mACheckBox.setTextColor(color);
        }
        else if(checkBoxIndex ==1){
            mBCheckBox.setTextColor(color);
        }
        else if(checkBoxIndex ==2){
            mCCheckBox.setTextColor(color);
        }
        else{
            mDCheckBox.setTextColor(color);
        }
    }

    //Thomas Haines
     //Reset all checkbox text to the color black
    private void resetCheckBoxColor(){
        mACheckBox.setTextColor(Color.BLACK);
        mBCheckBox.setTextColor(Color.BLACK);
        mCCheckBox.setTextColor(Color.BLACK);
        mDCheckBox.setTextColor(Color.BLACK);
    }

    //Thomas Haines
     //Intent to pass the string for the desired quiz subject to this activity from the main activity
     public static Intent newIntent(Context packageContext, String quizSubject) {
         Intent intent = new Intent(packageContext, QuestionActivity.class);
         intent.putExtra("EXTRA_SUBJECT", quizSubject);
         return intent;
     }
}
