//Thomas Haines and Jose Martinez
//This is the activity that runs when the app is launched.
//It displays the options for the actions the user can take which are
//go to the testing section, view the user profile, or create new questions

package com.example.fa_re;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;


public class MainActivity extends AppCompatActivity {

    private String mDirectoryName;
    private String mUsernameText;
    private Profile mCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        File file = new File(getExternalFilesDir(null),"UserGenerated/");
        file.mkdir();

        //Thomas Haines
        //If there is no profile already created, prompt the user to enter their name for
        //and create a profile with that name
        mDirectoryName = "Profile/";
        if(!hasExternalStoragePrivateFile()){
            final File profileDirectory = new File(getExternalFilesDir(null),"Profile/");
            profileDirectory.mkdir();

            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Create Profile");
            builder.setMessage("Input name for profile.");

            //Put an EditText in the alert dialog box
            final EditText mUserName = new EditText(MainActivity.this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            mUserName.setLayoutParams(lp);
            builder.setView(mUserName);

            builder.setPositiveButton("DONE", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mUsernameText  = mUserName.getText().toString();
                    mCurrentUser = new Profile(mUsernameText, profileDirectory.getAbsolutePath());
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    profileDirectory.delete();
                    dialog.dismiss();
                }
            });


            builder.show();
        }

        File profileDirectory = new File(getExternalFilesDir(null),"UserGenerated/");

        // Jose Martinez
        // Code for listView in the main menu

        // Options in the main menu
        final String [] values  = {"Testing", "User Profile", "Create Questions"};
        // Creates object and links it with an adapter in order to display
        ListView listView = findViewById(R.id.listView);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.main_menu_list_view, R.id.menu_item, values);

        listView.setAdapter(adapter);

        // Code for the different options in the list
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        switchActivity(TestingSectionActivity.class);
                        break;

                    case 1:
                        switchActivity(ProfileActivity.class);
                        break;

                    case 2:
                        switchActivity(QuestionCreator.class);
                        break;

                }
            }
        });

    }
    // Jose Martinez
    // Simple method that changes to the activity that is in the destination parameter
    private void switchActivity(Class destination){
        Intent intent = new Intent(MainActivity.this, destination);
        startActivity(intent);
    }

    // Jose Martinez
    // Checks if a file exists on the app's designated external storage folder
    boolean hasExternalStoragePrivateFile() {
        File file = new File(MainActivity.this.getExternalFilesDir(null), mDirectoryName);
        return file.exists();
    }
}
