package com.example.todoapp;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //a numeric code to identify the edit activity
    public final static int EDIT_REQUEST_CODE = 20;
    //keys used for passing data between activities
    public final static String ITEM_TEXT = "itemText";
    public final static String ITEM_POSITION = "itemPosition";

    ArrayList<String>items;
    ArrayAdapter<String>itemAdapter;
    ListView lvItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        readItem();
        itemAdapter = new ArrayAdapter<String>(this, R.layout.list_item_layout,items);
        lvItems = findViewById(R.id.lvItems);
        lvItems.setAdapter(itemAdapter);

        final EditText etItem = findViewById(R.id.etNewItem);
        Button btnAdd = findViewById(R.id.btnAddItem);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String item = etItem.getText().toString();
                if(!item.equals("")){
                    itemAdapter.add(item);
                    etItem.setText("");
                    writeItems();
                    Toast.makeText(MainActivity.this, "Task added", Toast.LENGTH_SHORT).show();
                }
            }
        });

        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                items.remove(position);
                itemAdapter.notifyDataSetChanged();
                writeItems();
                Toast.makeText(MainActivity.this, "Task "+(position+1)+" removed", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(MainActivity.this, EditItemActivity.class);
                i.putExtra(ITEM_TEXT, items.get(position));
                i.putExtra(ITEM_POSITION,position);
                startActivityForResult(i,EDIT_REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && requestCode == EDIT_REQUEST_CODE){
            String updatedItem = data.getExtras().getString(ITEM_TEXT);
            int position = data.getExtras().getInt(ITEM_POSITION);
            items.set(position,updatedItem);
            itemAdapter.notifyDataSetChanged();
            writeItems();
            Toast.makeText(this, "Item updated successfully", Toast.LENGTH_SHORT).show();
        }
    }

    private File getDataFile(){
        return new File(getFilesDir(),"todo.txt");
    }

    private void readItem(){
        try{
            items = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        }catch (IOException e){
            Toast.makeText(this, "Error reading file", Toast.LENGTH_SHORT).show();
            items = new ArrayList<>();
        }
    }

    private  void writeItems(){
        try {
            FileUtils.writeLines(getDataFile(), items);
        }catch (IOException e){
            Toast.makeText(this, "Error writing in file", Toast.LENGTH_SHORT).show();
        }
    }
}
