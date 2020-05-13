package com.example.fa_re;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.util.Arrays;

/**
 * FileBrowser.java and its activity.xml file were written by Jose Martinez
 *
 * File Browser works as an in-app
 */
public class FileBrowser extends AppCompatActivity {

    private static final String TAG = "FileBrowser";

    private String [] mFileList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_browser);
        setTitle("File Browser");

        // File object with the directory for the user created txt files.
        File dir = new File(getExternalFilesDir(null), "UserGenerated/");
        if (!dir.canRead()) {
            setTitle(getTitle() + " (inaccessible)");
        }

        mFileList = dir.list();
        assert mFileList != null;
        Arrays.sort(mFileList);
        Log.d(TAG, Arrays.toString(mFileList));

        // Put the data into the list
        ListView listView = findViewById(R.id.list);
        ArrayAdapter adapter = new ArrayAdapter<>(this, R.layout.main_menu_list_view, R.id.menu_item, mFileList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "position is" + position);
                Log.d(TAG, "file is " + mFileList[position]);
                Intent intent = QuestionActivity.newIntent(FileBrowser.this, mFileList[position]);
                startActivity(intent);
            }
        });
    }
}
