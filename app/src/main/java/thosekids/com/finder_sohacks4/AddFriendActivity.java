package thosekids.com.finder_sohacks4;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AddFriendActivity extends AppCompatActivity {

    private EditText searchBar;
    private ListView matchesList;

    private UserListAdapter currentMatchesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);

        searchBar = (EditText) findViewById(R.id.searchBar);
        matchesList = (ListView) findViewById(R.id.matchesList);

        searchBar.addTextChangedListener(new searchBarChangeListener(this));
    }

    public void updateResults(ArrayList<UserInformation> matches) {
        currentMatchesAdapter = new UserListAdapter(this, matches);
        matchesList.setAdapter(currentMatchesAdapter);
        matchesList.setOnItemClickListener(new AddFriendListListener(matches));
    }

    class AddFriendListListener implements AdapterView.OnItemClickListener {

        private ArrayList<UserInformation> matches;

        public AddFriendListListener(ArrayList<UserInformation> matches) {
            this.matches = matches;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            UserInformation SelectedUser = matches.get(position);
            // !!! // Send friend request here
        }
    }

}



class searchBarChangeListener implements TextWatcher {

    AddFriendActivity addFriendActivity;

    public searchBarChangeListener(AddFriendActivity addFriendActivity) {
        this.addFriendActivity = addFriendActivity;
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String searchString = s.toString();
        updateMatchingUIDs(searchString, 15);
    }

    private void updateMatchingUIDs(final String searchString, final int numberOfResults) {
        String myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

//        FirebaseDatabase.getInstance().getReference().child("UserFriends").child(myUid)
//                .addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        UserInformation[] friends; // = dataSnapshot.getValue();
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//                        databaseError.toException().printStackTrace();
//                    }
//        });

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("UserInformation").orderByChild("name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<UserInformation> matches = new ArrayList<UserInformation>();
                for(DataSnapshot userInformation: dataSnapshot.getChildren()) {
                    String uid = (String)userInformation.getKey();
                    String name = (String)userInformation.child("name").getValue();
                    String username = (String)userInformation.child("username").getValue();
                    int len = searchString.length();
                    String nameMatch = name.substring(0,(len <= name.length())? len : 0).toLowerCase();
                    String usernameMatch = username.substring(0, (len <= username.length())? len : 0).toLowerCase();
                    //System.out.printf("{%s,%s,%s,%s}%n", name, username, nameMatch, usernameMatch);
                    if(nameMatch.equals(searchString.toLowerCase()) || usernameMatch.equals(searchString.toLowerCase())) {
                        matches.add(new UserInformation(name, username, uid));
                    }
                    if(matches.size() >= numberOfResults) {
                        break;
                    }
                }
                addFriendActivity.updateResults(matches);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                databaseError.toException().printStackTrace();
            }
        });
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}



    @Override
    public void afterTextChanged(Editable s) {}
}
