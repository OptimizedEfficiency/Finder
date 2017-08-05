package thosekids.com.finder_sohacks4;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class SelectionActivity extends AppCompatActivity {

    private ListView userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);

        userList = (ListView) findViewById(R.id.userList);


        String[] users = {"U 1", "U 2", "U 3"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(userList.getContext(), android.R.layout.simple_list_item_1, users);
        userList.setAdapter(adapter);


    }
}
