//Thomas Haines and Jose Martinez
//Displays the quiz information saved in the profile text file

package com.example.fa_re;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Scanner;

public class ProfileActivity extends AppCompatActivity {

    //private TextView mPercentView;
    private Button mMenuButton;
    private String mProfilePath;
    private TextView mUsernameText;
    private Profile mCurrentUser;

    //Thomas Haines
    //Intent method to be used by other classes to pass information such as the percentage and subject from a quiz to this class
    public static Intent newIntent(Context packageContext, double profilePercent, String quizSubject) {
        Intent intent = new Intent(packageContext, ProfileActivity.class);
        intent.putExtra("EXTRA_ANSWER", profilePercent);
        intent.putExtra("EXTRA_SUBJECT", quizSubject);
        return intent;
    }

    //Thomas Haines
    //Runs when this activity is started
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //Get the values passed in an intent
        double percentage = getIntent().getDoubleExtra("EXTRA_ANSWER", 50.0);
        String subject = getIntent().getStringExtra("EXTRA_SUBJECT");


        //Get file path for profile text file to write to
        File profileDirectory = new File(getExternalFilesDir(null), "Profile/");
        for(File f : profileDirectory.listFiles()){
            mProfilePath = f.getAbsolutePath();
        }
        mUsernameText = findViewById(R.id.username);


        //If subject is not null, an intent started this activity and a new score needs to be logged in the profile
        if(subject != null){
            //Put the results into a string
            String result = String.format("%.2f", percentage) + "% For "+subject+"\n \n";

            //Write the score and quiz subject received from the intent to the file
            try {
                OutputStream os = new FileOutputStream(mProfilePath, true);
                byte[] data = result.getBytes();
                os.write(data);
                os.flush();
                os.close();

            } catch (IOException e) {
                Log.w("ExternalStorage", "Error writing " + mProfilePath, e);
            }
        }

        //Load information from the profile text file and display it in the activity
        loadProfileInformation();

        //Create menu button for user to return to the menu
        mMenuButton = findViewById(R.id.menu_button);
        mMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchActivity(MainActivity.class);
            }
        });


    }

    //Thomas Haines, Code written by Jose Martinez reused and altered
    //Loads the information from the profile text file onto the profile page for viewing
    private void loadProfileInformation(){
        try{
            //Initialize scanner to scan profile text file
            Scanner fileScan;
            File filePath = new File(mProfilePath);
            fileScan = new Scanner(filePath);

            String token;
            int index = 0;

            //Get username token
            token = nextToken(fileScan);
            if(token.equals("username=")){
                token = fileScan.nextLine();
                mUsernameText.setText(token);
            }
            while (fileScan.hasNext()) {
                //Get each line from the file holding a quiz score and subject
                token = fileScan.nextLine();

                //Get linearlayout to add textview to
                LinearLayout profilelayout = findViewById(R.id.profileLinearLayout);
                //Add a textview to the profileActivity to hold the quiz information
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                TextView newTV = new TextView(this);
                newTV.setLayoutParams(lp);
                newTV.setPadding(8, 16, 8, 0);
                newTV.setText(token);
                profilelayout.addView(newTV);
            }

        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Jose Martinez
    //Retrieve the next token in a file
    private String nextToken(Scanner scan) {

        if (scan.hasNext()) {
            return scan.next();
        }
        return null;
    }

    private void switchActivity(Class destination){
        Intent intent = new Intent(ProfileActivity.this, destination);
        startActivity(intent);
    }

}
