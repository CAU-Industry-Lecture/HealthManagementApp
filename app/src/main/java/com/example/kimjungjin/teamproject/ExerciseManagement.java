package com.example.kimjungjin.teamproject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import DB.DBHelper;
import DB.Exercise;

public class ExerciseManagement extends AppCompatActivity {
    Button btnConfirm;
    Button btncancel;
    EditText Editkillo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_management);
        Editkillo = (EditText)findViewById(R.id.Editkillo);
        btnConfirm = (Button)findViewById(R.id.btnConfirm);
        btncancel = (Button)findViewById(R.id.btnCancel);

        // Spinner purpose
        final Spinner purposeSpinner = (Spinner)findViewById(R.id.spinner_purpose);
        ArrayAdapter purposeAdapter = ArrayAdapter.createFromResource(this,
                R.array.purpose, android.R.layout.simple_spinner_item);
        purposeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        purposeSpinner.setAdapter(purposeAdapter);

        // Spinner time
        final Spinner timeSpinner = (Spinner)findViewById(R.id.spinner_time);
        ArrayAdapter timeAdapter = ArrayAdapter.createFromResource(this,
                R.array.time, android.R.layout.simple_spinner_item);
        timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeSpinner.setAdapter(timeAdapter);

        String str_purpose = purposeSpinner.getSelectedItem().toString();
        String str_time = timeSpinner.getSelectedItem().toString();
        String str_killo = Editkillo.getText().toString();

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Editkillo.getText().toString().length() == 0) {
                    Toast.makeText(ExerciseManagement.this, "목표 몸무게를 입력해주세요", Toast.LENGTH_SHORT).show();
                } else {
                    String str_purpose = purposeSpinner.getSelectedItem().toString();
                    String str_time = timeSpinner.getSelectedItem().toString();
                    String str_killo = Editkillo.getText().toString();
                    Intent intent = new Intent(ExerciseManagement.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    Toast.makeText(ExerciseManagement.this, "스케줄이 생성되었습니다.", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
//                    finish();
                }
            }
            });

        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });

    }
    @Override
    public  void onBackPressed(){
        super.onBackPressed();
    }
}
