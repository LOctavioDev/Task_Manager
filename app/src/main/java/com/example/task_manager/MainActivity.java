package com.example.task_manager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class MainActivity extends AppCompatActivity {

    private Button btnSave;
    private EditText txtFilename;
    private EditText txtFileContent;
    private ListView lvFiles;
    private ArrayAdapter adapter;
    private String filename;
    private String filecontent;
    private FileOutputStream fileOutputStream;
    private FileInputStream fileInputStream;
    private InputStreamReader inputStreamReader;
    private BufferedReader bufferedReader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnSave = findViewById(R.id.btn_save);
        lvFiles = findViewById(R.id.lv_files);
        txtFilename = findViewById(R.id.txt_filename);
        txtFileContent = findViewById(R.id.txt_filecontent);
        updateList();
        lvFiles.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                filename = (String)lvFiles.getItemAtPosition(i);
                txtFilename.setText(filename);
                try {
                    fileInputStream = MainActivity.this.openFileInput(filename);
                    inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);
                    bufferedReader = new BufferedReader(inputStreamReader);
                    String line = bufferedReader.readLine();
                    filecontent="";
                    while (line != null){
                        filecontent += line + "\n";
                        line = bufferedReader.readLine();
                    }
                    txtFileContent.setText(filecontent);
                } catch (FileNotFoundException e) {
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filename = txtFilename.getText().toString().trim();
                filecontent = txtFileContent.getText().toString().trim();
                if(filename != "" && filecontent != ""){
                    try {
                        fileOutputStream = MainActivity.this.openFileOutput(filename, Context.MODE_PRIVATE);
                        fileOutputStream.write(filecontent.getBytes());
                        Toast.makeText(MainActivity.this, "Tarea Almacenada", Toast.LENGTH_SHORT).show();
                        clearFields();
                        updateList();
                    } catch (FileNotFoundException e) {
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(MainActivity.this, "Falta informacion.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    protected void clearFields(){
        txtFilename.setText("");
        txtFileContent.setText("");
        txtFilename.requestFocus();
    }
    protected void updateList(){
        String[] fileame = this.fileList();
        ArrayAdapter adapter = new ArrayAdapter<String>(this, com.google.android.material.R.layout.support_simple_spinner_dropdown_item,fileame);
        lvFiles.setAdapter(adapter);
    }
}