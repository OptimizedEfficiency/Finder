package thosekids.com.finder_sohacks4;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SelectionActivity extends AppCompatActivity {

    private ListView userList;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);

        userList = (ListView) findViewById(R.id.userList);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();


        databaseReference.child("UserInformation").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                showUsers(snapshot);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void showUsers(DataSnapshot dataSnapshot) {
        System.out.println("Show Users called");
        ArrayList<String> users = new ArrayList<String>();
        for(DataSnapshot ds : dataSnapshot.getChildren()) {
            System.out.println("Inside for loop");
            System.out.println(ds.child("name").getValue());
            users.add(ds.child("name").getValue().toString());
        }
        displayUsernames(users);
    }

    public void displayUsernames(ArrayList<String> users) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, users);
        userList.setAdapter(adapter);

    }


}
