package com.example.fa_re;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * QuestionCreator:
 *
 * This is all of the code for the Question Creator Activity which is for the creation of questions
 * that the user may be tested on. The questions are stored on a file in the Android device's
 * external memory.
 *
 * All of the code in QuestionCreator.java and activity_question_creator.xml was written by
 * Jose R. Martinez Torres
 */

public class QuestionCreator extends AppCompatActivity {

    private EditText mFileName;
    private EditText mQuestion;
    private EditText [] mChoices;
    private CheckBox [] mCheckboxes;
    private Button mDone;
    private Button mNextButton;
    private String mFullFileName;
    private String mBody = "";
    private int mUserPressed = -1;
    private boolean mAppend = true;
    private boolean fileChosen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_creator);

        mFileName = findViewById(R.id.fileName_text);
        mQuestion = findViewById(R.id.question_text);

        mChoices = new EditText[]{findViewById(R.id.choice_a), findViewById(R.id.choice_b),
                findViewById(R.id.choice_c), findViewById(R.id.choice_d)};
        mCheckboxes = new CheckBox[]{findViewById(R.id.a_check_QuestionCreator), findViewById(R.id.b_check_QuestionCreator),
                findViewById(R.id.c_check_QuestionCreator), findViewById(R.id.d_check_QuestionCreator)};

        Button checkFile = findViewById(R.id.check_file_button);
        mNextButton = findViewById(R.id.question_confirm);
        mDone = findViewById(R.id.done_button);

        mNextButton.setEnabled(false);
        mDone.setEnabled(false);

        // runs when mCheckFile Button is clicked.
        checkFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            // Adds extension to file name and checks if a name was provided
            mFullFileName = "UserGenerated/" + mFileName.getText().toString() + ".txt";
            if (mFullFileName.equals("UserGenerated/.txt")) {
                Toast.makeText(QuestionCreator.this, R.string.fileNameError_Toast, Toast.LENGTH_SHORT).show();
            }
            else {

                if (hasExternalStoragePrivateFile() && !fileChosen) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(QuestionCreator.this);
                    builder.setTitle("File Found");
                    builder.setMessage(mFullFileName +" has been found. Would you like to append to it?");
                    builder.setPositiveButton("Append", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            mAppend = true;
                            fileChosen = true;
                            mDone.setEnabled(true);
                            dialog.dismiss();
                        }
                    });

                    builder.setNegativeButton("Overwrite", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mAppend = false;
                            fileChosen = true;
                            mDone.setEnabled(true);
                            dialog.dismiss();
                        }
                    });

                    builder.setNeutralButton(" Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
                else if (hasExternalStoragePrivateFile() && fileChosen) {
                    Toast.makeText(QuestionCreator.this, mFullFileName +
                            " has been found. You may begin adding questions", Toast.LENGTH_SHORT).show();

                }
                else {
                    pDialogBoxCreateFile();
                    fileChosen = true;
                }
            }
            }
        });

        mCheckboxes[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkOtherBoxes(0);
                checkEmptyTextFields();
            }
        });
        mCheckboxes[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkOtherBoxes(1);
                checkEmptyTextFields();
            }
        });
        mCheckboxes[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkOtherBoxes(2);
                checkEmptyTextFields();
            }
        });
        mCheckboxes[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkOtherBoxes(3);
                checkEmptyTextFields();
            }
        });

        // Executes when the EditText boxes are written
        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                checkEmptyTextFields();
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkEmptyTextFields();
            }
            @Override
            public void afterTextChanged(Editable s) {
                checkEmptyTextFields();
            }
        };

        mQuestion.addTextChangedListener(watcher);
        mChoices[0].addTextChangedListener(watcher);
        mChoices[1].addTextChangedListener(watcher);
        mChoices[2].addTextChangedListener(watcher);
        mChoices[3].addTextChangedListener(watcher);

        // Button for confirming the current question, adding it and working on the next one.
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(QuestionCreator.this);
                builder.setTitle("Confirm Question");
                builder.setMessage("Are you sure you want to add the current question?");

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        LinearLayout viewer = findViewById(R.id.questionViewer);
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT);


                        // Array of TextViews that are created dynamically
                        // These display the previous question so the user can notice errors if they
                        // didn't before
                        TextView [] prevQuest = new TextView[]{
                            new TextView(QuestionCreator.this), new TextView(QuestionCreator.this),
                            new TextView(QuestionCreator.this), new TextView(QuestionCreator.this),
                            new TextView(QuestionCreator.this), new TextView(QuestionCreator.this)};

                        // Sets attributes for the TextView objects
                        for (int i = 0; i < 6; i++) {
                            prevQuest[i].setLayoutParams(lp);
                            prevQuest[i].setPadding(8, 16, 8, 0);
                            prevQuest[i].setTextSize(20);
                            prevQuest[i].setTypeface(null, Typeface.BOLD);

                            if(i == 0)      prevQuest[i].setText("-"+mQuestion.getText().toString());
                            else if(i == 5) prevQuest[i].setText("Answer = " + (mUserPressed + 1));
                            else            prevQuest[i].setText("    " + mChoices[i-1].getText().toString());

                            viewer.addView(prevQuest[i]);
                        }
                        storeQuestionToString();
                        mNextButton.setEnabled(false);
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton(" NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
                mDone.setEnabled(true);

            }
        });

        // Button for when the user is done adding questions.
        // Writes questions to file and clears everything.
        mDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if (mAppend) {
                pDialogBoxWriteFile("Write to File", "Are you sure that you want to " +
                        "append the current questions to " + mFullFileName + "?", mBody);
            }
            else{
                pDialogBoxWriteFile("Write to File","Are you sure that you want to " +
                        "write the current questions to " + mFullFileName + "?", mBody );
            }
            mDone.setEnabled(false);

            fileChosen = false;

            }
        });
    }

    // Un-checks other CheckBoxes so only one is allowed.
    void checkOtherBoxes(int index) {
        mUserPressed = index;
        for(int i = 0; i < 4; i++) {
            if (i != index) mCheckboxes[i].setChecked(false);
        }
    }

    //Stores the current question that was written into the String mBody and clears all EditTexts.
    void storeQuestionToString(){
        String question = "question = " + mQuestion.getText().toString().trim();
        String answer = "answer = " + mUserPressed;

        String choices = "choices = { \n" +
                "a) "+ mChoices[0].getText().toString().trim() + "\n" +
                "b) "+ mChoices[1].getText().toString().trim() + "\n" +
                "c) "+ mChoices[2].getText().toString().trim() + "\n" +
                "d) "+ mChoices[3].getText().toString().trim() + "\n}";
        mBody += question + "\n" + answer + "\n" + choices + "\n\n";
        Toast.makeText(QuestionCreator.this, "Question has been added", Toast.LENGTH_SHORT).show();
        mQuestion.getText().clear();
        for (EditText et: mChoices) {
            et.getText().clear();
        }
        mCheckboxes[mUserPressed].setChecked(false);
        mUserPressed = -1;

    }

    // Checks if all of the necessary fields have been completed before allowing to submit question.
    void checkEmptyTextFields() {
        String a = mQuestion.getText().toString().trim();
        String b = mChoices[0].getText().toString().trim();
        String c = mChoices[1].getText().toString().trim();
        String d = mChoices[2].getText().toString().trim();
        String e = mChoices[3].getText().toString().trim();

        if (a.isEmpty() || b.isEmpty() || c.isEmpty() || d.isEmpty() || e.isEmpty() || mUserPressed == -1) {
            mNextButton.setEnabled(false);
        }
        else{
            mNextButton.setEnabled(true);
        }
    }

    /**
     * Writes the Questions that have been input into the activity to the txt file with the provided
     * name.
     *
     * @param context     The context in which the method is being used. In this case QuestionCreator.class
     * @param sFileName   Name of the file that will be written to
     * @param sBody       Text that will be written to the file.
     */
    void writeToExternalStorage(Context context, String sFileName, String sBody) {
        // Path for external storage
        File path = new File(context.getExternalFilesDir(null), sFileName);
        try {
            OutputStream os = new FileOutputStream(path, mAppend);
            byte[] data = sBody.getBytes();
            os.write(data);
            os.flush();
            os.close();

        } catch (IOException e) {
            Log.w("ExternalStorage", "Error writing " + path, e);
        }
    }

    // Checks if a file exists on the app's designated external storage folder
    boolean hasExternalStoragePrivateFile() {
        File file = new File(QuestionCreator.this.getExternalFilesDir(null), mFullFileName);
        return file.exists();
    }

    // Prompts the user with a Dialog Box in order to create a file.
    private void pDialogBoxCreateFile(){
        AlertDialog.Builder builder = new AlertDialog.Builder(QuestionCreator.this);

        builder.setTitle("File Not Found");
        builder.setMessage(mFullFileName + " does not exist. Would you like to create it?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                writeToExternalStorage(QuestionCreator.this, mFullFileName, "");
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    /**
     *
     * Method for displaying a Dialog Box to confirm the user's requests.
     * Method takes in 3 (three) parameters:
     *
     * @param title    String that will be the title of the DialogBox
     * @param message  String displayed under title and provides context as to what
     *                 the Yes/No Boxes will do.
     * @param sBody    Text that will be written to the file
     */
    private void pDialogBoxWriteFile(String title, String message, final String sBody) {


        AlertDialog.Builder builder = new AlertDialog.Builder(QuestionCreator.this);

        builder.setTitle(title);
        builder.setMessage(message);

        // Writes questions to file and restarts the QuestionCreator Activity
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                writeToExternalStorage(QuestionCreator.this, mFullFileName, sBody);
                Toast.makeText(QuestionCreator.this, "Questions have been written to file." +
                        " Please choose a new file to write to.", Toast.LENGTH_SHORT).show();
                Intent intent = getIntent();
                finish();
                startActivity(intent);
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

}
