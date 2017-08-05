package thosekids.com.finder_sohacks4;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth firebaseAuth;
    private Button signOutBtn;

    @OnClick(R.id.StartNav)
    public void toSelectionActivity(){
        setContentView(R.layout.activity_selection);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        System.out.println("Running Main Activity");

        firebaseAuth = FirebaseAuth.getInstance();

        //System.out.println(firebaseAuth.getCurrentUser().getEmail());

        if(firebaseAuth.getCurrentUser() == null) {
            System.out.println("No user logged in. Switching to Login Activity.");
            startActivity(new Intent(this, LoginActivity.class));
        }

        signOutBtn = (Button) findViewById(R.id.signOutBtn);

        signOutBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if(view == signOutBtn) {
            System.out.println("Logging out");
            firebaseAuth.signOut();
            startActivity(new Intent(this, LoginActivity.class));
        }
    }

}
