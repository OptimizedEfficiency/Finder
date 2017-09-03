package thosekids.com.finder_sohacks4;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

public class FriendActivity extends AppCompatActivity implements View.OnClickListener {

    private ListView friendsList;
    private Button addFriendBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);

        friendsList = (ListView) findViewById(R.id.friendsList);
        addFriendBtn = (Button) findViewById(R.id.addFriendBtn);

        friendsList.setOnItemClickListener(new ItemList());

        addFriendBtn.setOnClickListener(this);

    }

    class ItemList implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            System.out.println(position);





        }
    }

    @Override
    public void onClick(View view) {
        if(view == addFriendBtn) {
            startActivity(new Intent(this, AddFriendActivity.class));
        }
    }
}
