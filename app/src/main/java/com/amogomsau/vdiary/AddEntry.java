package com.amogomsau.vdiary;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddEntry extends AppCompatActivity {

    EditText title, description;
    Button add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_entry);

        title = findViewById(R.id.title);
        description = findViewById(R.id.description);
        add = findViewById(R.id.btnAddEntry);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseHelper mango = new DatabaseHelper(AddEntry.this);
                mango.addEntry(1, title.getText().toString().trim(), description.getText().toString().trim(), title.getText().toString().trim(), title.getText().toString().trim(), title.getText().toString().trim());
            }
        });
    }
}