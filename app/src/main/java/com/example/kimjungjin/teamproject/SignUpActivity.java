
package com.example.kimjungjin.teamproject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import DB.DBHelper;

public class SignUpActivity extends AppCompatActivity {

    private SharedPreferences userInfo;
    private SharedPreferences.Editor editor;
    private RadioGroup radioGroup;
    private RadioButton rb;
    private EditText EditName;
    private EditText EditAge;
    private EditText EditWeight;
    private EditText EditHeight;
    private Button btnCancel;
    private Button btnConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        radioGroup = (RadioGroup)findViewById(R.id.radioGroup);
        EditName = (EditText)findViewById(R.id.EditName);
        EditAge = (EditText)findViewById(R.id.EditAge);
        EditWeight = (EditText)findViewById(R.id.EditWeight);
        EditHeight = (EditText)findViewById(R.id.EditHeight);
        btnCancel = (Button)findViewById(R.id.btnCancel);
        btnConfirm = (Button)findViewById(R.id.btnConfirm);
        userInfo = getSharedPreferences("userInfo", Activity.MODE_PRIVATE);
        editor = userInfo.edit();

        if(userInfo.getString("name", null) != null){
            EditName.setText(userInfo.getString("name", null));
        }
        if(userInfo.getString("age", null) != null){
            EditAge.setText(userInfo.getString("age", null));
        }
        if(userInfo.getString("sex", null) != null){
            if(userInfo.getString("sex", null).equals("남자")){
                radioGroup.check(R.id.radioman);
            }
            else{
                radioGroup.check(R.id.radiowoman);
            }
        }
        if(userInfo.getString("weight", null) != null){
            EditWeight.setText(userInfo.getString("weight", null));
        }
        if(userInfo.getString("height", null) != null){
            EditHeight.setText(userInfo.getString("height", null));
        }


        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                rb = (RadioButton)findViewById(radioGroup.getCheckedRadioButtonId());
                if(EditName.getText().toString().length() == 0){
                    Toast.makeText(SignUpActivity.this, "이름을 입력해주세요", Toast.LENGTH_SHORT).show();
                }
                else if(EditAge.getText().toString().length() == 0){
                    Toast.makeText(SignUpActivity.this, "나이를 입력해주세요", Toast.LENGTH_SHORT).show();
                }
                else if(EditWeight.getText().toString().length() == 0){
                    Toast.makeText(SignUpActivity.this, "몸무게를 입력해주세요", Toast.LENGTH_SHORT).show();
                }
                else if(EditHeight.getText().toString().length() == 0){
                    Toast.makeText(SignUpActivity.this, "키를 입력해주세요", Toast.LENGTH_SHORT).show();
                }
                else{
                    if(userInfo.getString("state", null) == null){
                        DBHelper dbHelper = dbHelper = new DBHelper( SignUpActivity.this, "capstone", null, 1);;
                        dbHelper.addExerciseData();
                    }

                    editor.putString("name", EditName.getText().toString());
                    editor.putString("age", EditAge.getText().toString());
                    editor.putString("sex", rb.getText().toString());
                    editor.putString("weight", EditWeight.getText().toString());
                    editor.putString("height", EditHeight.getText().toString());
                    editor.putString("state", "YES");
                    editor.commit();
                    Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
//                    finish();
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
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
