package com.amogomsau.vdiary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class home extends AppCompatActivity {

    private GoogleSignInClient mGoogleSignInClient;
    RecyclerView recyclerView;
    DatabaseHelper myDB;
    ArrayList<String> entry_title, entry_description, entry_location, entry_date, entry_image;
    CustomAdaptor customAdaptor;

    void storeDataInArrays(){
        Cursor cursor = myDB.readAllData();
        if(cursor.getCount() == 0){
            //empty_imageview.setVisibility(View.VISIBLE);
            //no_data.setVisibility(View.VISIBLE);
        }else{
            while (cursor.moveToNext()){
                entry_title.add(cursor.getString(3));
                entry_description.add(cursor.getString(4));
                entry_image.add(cursor.getString(5));
                entry_location.add(cursor.getString(6));
                entry_date.add(cursor.getString(2));
            }
            //empty_imageview.setVisibility(View.GONE);
            //no_data.setVisibility(View.GONE);
        }
    }

    @Override
    public void onRestart()
    {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        recyclerView = findViewById(R.id.recyclerView);

        myDB = new DatabaseHelper(home.this);
        entry_title = new ArrayList<>();
        entry_description = new ArrayList<>();
        entry_location = new ArrayList<>();
        entry_date = new ArrayList<>();
        entry_image = new ArrayList<>();

        storeDataInArrays();

        customAdaptor = new CustomAdaptor(home.this, entry_title, entry_description, entry_location, entry_date, entry_image);
        recyclerView.setAdapter(customAdaptor);
        recyclerView.setLayoutManager(new LinearLayoutManager(home.this));

        FloatingActionButton add_button = findViewById(R.id.btnAddEntry);
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(home.this, AddEntry.class);
                startActivity(intent);
            }
        });


        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        ImageView avatarImage = findViewById(R.id.avatarImage);
        TextView username = findViewById(R.id.usernameText);
        TextView email = findViewById(R.id.emailText);
        TextView googleId = findViewById(R.id.googleIdText);

        Button signOut = findViewById(R.id.btnSignOut);
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.btnSignOut) {
                    signOut();
                }
            }
        });

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);

        if (acct != null) {
            String personName = acct.getDisplayName();
            username.setText(personName);
            String personEmail = acct.getEmail();
            email.setText(personEmail);
            String personId = acct.getId();
            googleId.setText(personId);
            Uri personPhoto = acct.getPhotoUrl();
            Glide.with(this).load(String.valueOf(personPhoto)).into(avatarImage);
        }
    }

    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(home.this, "Signed Out!", Toast.LENGTH_LONG).show();
                        finish();
                    }
                });
    }
}
