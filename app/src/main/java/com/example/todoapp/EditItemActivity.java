package com.example.todoapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import static com.example.todoapp.MainActivity.ITEM_POSITION;
import static com.example.todoapp.MainActivity.ITEM_TEXT;

public class EditItemActivity extends AppCompatActivity {

    EditText editUpdateItem;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        editUpdateItem = findViewById(R.id.editTextUpdate);
        editUpdateItem.setText(getIntent().getStringExtra(ITEM_TEXT));
        position = getIntent().getIntExtra(ITEM_POSITION,0);
    }

    public void onSaveItem(View view){
        Intent i = new Intent();
        i.putExtra(ITEM_TEXT,editUpdateItem.getText().toString());
        i.putExtra(ITEM_POSITION,position);
        setResult(RESULT_OK,i);
        finish();
    }
}
