//Thomas Haines and Jose Martinez
//Select which quiz category to start from this activity

package com.example.fa_re;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

public class TestingSectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing_section);

        //Links the ListView and displays it.
        final String [] values  = {"Regular Languages Quiz", "Context Free Languages Quiz", "Non-Context-Free Languages Quiz", "User Created Files"};
        ListView listView = findViewById(R.id.listView);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.main_menu_list_view, R.id.menu_item, values);
        listView.setAdapter(adapter);

        //Thomas Haines and Jose Martinez
        //Transition to the selected activity when it is selected in the view on the screen
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        Intent intentRL = QuestionActivity.newIntent(TestingSectionActivity.this, values[0]);
                        startActivity(intentRL);
                        break;

                    case 1:
                        Intent intentCF = QuestionActivity.newIntent(TestingSectionActivity.this, values[1]);
                        startActivity(intentCF);
                        break;

                    case 2:
                        Intent intentNCF = QuestionActivity.newIntent(TestingSectionActivity.this, values[2]);
                        startActivity(intentNCF);
                        break;

                    case 3:
                        switchActivity(FileBrowser.class);
                        break;

                }
            }
        });
    }

    private void switchActivity(Class destination){
        Intent intent = new Intent(TestingSectionActivity.this, destination);
        startActivity(intent);
    }
}
