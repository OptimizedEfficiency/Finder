package thosekids.com.finder_sohacks4;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    EditText editTextName;
    EditText editTextUsername;
    EditText editTextEmail;
    EditText editTextPassword;
    EditText editTextReenterPassword;
    Button register;
    TextView loginNow;

    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editTextName = (EditText) findViewById(R.id.name);
        editTextUsername = (EditText) findViewById(R.id.username);
        editTextEmail = (EditText) findViewById(R.id.email);
        editTextPassword = (EditText) findViewById(R.id.password);
        editTextReenterPassword = (EditText) findViewById(R.id.confirmPassword);
        register = (Button) findViewById(R.id.register);
        loginNow = (TextView) findViewById(R.id.loginNow);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        progressDialog = new ProgressDialog(this);

        if(firebaseAuth.getCurrentUser() != null) {
            finish();
            getParent().finish();
            startActivity(new Intent(this, MainActivity.class));
        }

        register.setOnClickListener(this);
        loginNow.setOnClickListener(this);
    }

    public void registerClicked() {
        final String name = editTextName.getText().toString().trim();
        final String username = editTextUsername.getText().toString().trim();
        final String email = editTextEmail.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();
        final String password2 = editTextReenterPassword.getText().toString().trim();


        System.out.println("About to define event listener");

        databaseReference.child("UserInformation").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                System.out.println("Inside Event Listener");

                if(TextUtils.isEmpty(name)) {
                    Toast.makeText(RegisterActivity.this, "Please enter name", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(email)) {
                    Toast.makeText(RegisterActivity.this, "Please enter email", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(password)) {
                    Toast.makeText(RegisterActivity.this, "Please enter password", Toast.LENGTH_SHORT).show();
                    return;
                }

                for(DataSnapshot userInfo : dataSnapshot.getChildren()) {
                    if(username.equals(userInfo.child("username").getValue())) {
                        Toast.makeText(RegisterActivity.this, "Username already exists", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                if(usernameIsInUse(username)) {
                    Toast.makeText(RegisterActivity.this, "Username is already in already in use", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!password.equals(password2)) {
                    Toast.makeText(RegisterActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    return;
                }


                progressDialog.setMessage("Registering User...");
                progressDialog.show();

                firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                progressDialog.dismiss();

                                if(task.isSuccessful()) {
                                    UserInformation userInformation = new UserInformation(name, username);
                                    databaseReference.child("UserInformation").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(userInformation);
                                    finish();
                                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                } else {
                                    progressDialog.hide();
                                    Toast.makeText(RegisterActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println(databaseError.toString());
                System.out.println("Error");
            }
        });

        System.out.println("Outside EventListener");
    }

    public void loginNowClicked() {
        finish();
        startActivity(new Intent(this, LoginActivity.class));
    }

    public boolean usernameIsInUse(final String username) {

        final boolean[] inUse = {false};

        System.out.println("Adding Listener");
        databaseReference.child("UserInformation").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println("Running Listener");
                for(DataSnapshot userInfo : dataSnapshot.getChildren()) {
                    if(username.equals(userInfo.child("username").getValue())) {
                        inUse[0] = true;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        System.out.println("Added Listener. Returning result.");
        return inUse[0];
    }

    @Override
    public void onClick(View view) {
        if(view == register) {
            registerClicked();
        } else if(view == loginNow) {
            loginNowClicked();
        }
    }
}