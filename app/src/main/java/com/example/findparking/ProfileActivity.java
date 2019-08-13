package com.example.findparking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth firebaseAuth;
    private TextView textViewUserEmail;
    MenuInflater inflater;
    private ProgressBar pkLoadingIndicator2;
    private DatabaseReference databaseReference;
    private EditText editTextName, editTextAddress;
    private Button buttonSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        firebaseAuth = firebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(this, LoginActivity.class));

        }
        // TODO USER CAN BE UPDATE HIS PERSONAL INFORMATIONS
        databaseReference = FirebaseDatabase.getInstance().getReference("persons"); // reference to specific branch of JSON tree
        editTextAddress = (EditText) findViewById(R.id.editTextAddress);
        editTextName = (EditText) findViewById(R.id.editTextName);
        buttonSave = (Button) findViewById(R.id.buttonSave);
        pkLoadingIndicator2 = (ProgressBar) findViewById(R.id.pb_loading_indicator2);
        FirebaseUser user = firebaseAuth.getCurrentUser();

        textViewUserEmail = (TextView) findViewById(R.id.textViewUserEmail);
        textViewUserEmail.setText("Welcome\n" +user.getEmail()+ "\nplease apply the form to make you a profile");
        // adding listener to button
        buttonSave.setOnClickListener(this);
    }

    private void saveUserInformation(){
            String name = editTextName.getText().toString().trim();
            String address = editTextAddress.getText().toString().trim();
            UserInformation userInfo = new UserInformation(name,address);

            FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            Log.i("name: " , name);
            databaseReference.child(user.getUid()).setValue(userInfo);
            Log.i("userinfo: " , userInfo.toString());
        }
        pkLoadingIndicator2.setVisibility(View.INVISIBLE);
        Toast.makeText(this,"Information saved..", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View view) {
        if(view == buttonSave){
            pkLoadingIndicator2.setVisibility(View.VISIBLE);
            saveUserInformation();
        }
    }


    // Initiating Menu XML file (main1.xml)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        inflater = getMenuInflater();
        inflater.inflate(R.menu.main_user, menu);
        return true;
    }
    /**
     * Event Handling for Individual menu item selected
     * Identify single menu item by it's id
     * */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.option0: {
                finish();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;
            }
            case R.id.option1: {
                firebaseAuth.signOut();
                finish();
                startActivity(new Intent(this, LoginActivity.class));
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
