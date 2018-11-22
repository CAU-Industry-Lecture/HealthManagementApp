package com.example.kimjungjin.teamproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.widget.AdapterView.OnItemClickListener;
import android.os.Bundle;
import android.view.View;
import java.util.HashMap;
import java.util.Map;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import DB.DBHelper;
import DB.Exercise;

public class Exerciseregit extends AppCompatActivity
{

    private ListView listView;
    private DBHelper dbHelper;
    private Button btnregit;
    private Button btnregitcancel;
    Map<String, Integer> exerciseType = new HashMap<String, Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exerciseregit);

        listView = (ListView)findViewById(R.id.listView);
        btnregit = (Button)findViewById(R.id.btnRegit);
        btnregitcancel = (Button)findViewById(R.id.btnRegitCancel);

        dbHelper = new DBHelper( Exerciseregit.this, "capstone", null, 1);
        ArrayList<String> exercise = dbHelper.getAllExerciseName();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
            getApplicationContext(),
            android.R.layout.simple_list_item_multiple_choice,
            exercise
        );
        listView.setAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listView.setOnItemClickListener(itemClickListenerOfLanguageList);

        btnregit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 데이터베이스에 interest를  YES로 업데이트
                dbHelper.updateInterest(exerciseType);
                Intent intent = new Intent(Exerciseregit.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        btnregitcancel.setOnClickListener(new View.OnClickListener() {
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

    private OnItemClickListener itemClickListenerOfLanguageList = new OnItemClickListener()
    {
        public void onItemClick(AdapterView<?> adapterView, View clickedView, int pos, long id)
        {
            String data = ((TextView)clickedView).getText().toString();
            if(exerciseType.containsKey(data)){
                exerciseType.put(data ,exerciseType.get(data)+1);
            }
            else{
                exerciseType.put(data, 1);
            }

            String toastMessage = ((TextView)clickedView).getText().toString() + " is selected.";
            Toast.makeText(
                    getApplicationContext(),
                    toastMessage,
                    Toast.LENGTH_SHORT
            ).show();
        }
    };
}
