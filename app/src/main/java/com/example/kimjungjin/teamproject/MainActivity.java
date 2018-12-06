package com.example.kimjungjin.teamproject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;



public class MainActivity extends AppCompatActivity {

    private SharedPreferences userInfo;
    private Button btn1;
    private Button btn2;
    private Button btn3;
    private Button btn4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userInfo = getSharedPreferences("userInfo", Activity.MODE_PRIVATE);
        btn1 = (Button) findViewById(R.id.btn1);
        btn2 = (Button) findViewById(R.id.btn2);
        btn3 = (Button) findViewById(R.id.btn3);
        btn4 = (Button) findViewById(R.id.btn4);

        if(userInfo.getString("name", null) == null){
            btn2.setEnabled(false);
            btn3.setEnabled(false);
            btn4.setEnabled(false);
            Toast.makeText(MainActivity.this, "개인정보를 입력해주세요", Toast.LENGTH_SHORT).show();
        }
        else{
            btn1.setText("개인정보 수정");
            btn2.setEnabled(true);
            btn3.setEnabled(true);
            btn4.setEnabled(true);
        }
//        btnCreateDB.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final EditText etDBName = new EditText(MainActivity.this);
//                etDBName.setHint("DB명을 입력하세요.");
//
//                AlertDialog.Builder dialog =new AlertDialog.Builder(MainActivity.this);
//                dialog.setTitle("DB 이름을 입력하세요.")
//                        .setMessage("DB 이름을 입력하세요")
//                        .setView(etDBName)
//                        .setPositiveButton("생성", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                if( etDBName.getText().toString().length() > 0 ) {
//                                    dbHelper = new DBHelper( MainActivity.this,
//                                            etDBName.getText().toString(),
//                                            null, 1);
//                                    dbHelper.testDB();
//                                }
//
//                            }
//                        })
//                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//
//                            }
//                        })
//                        .create()
//                        .show();
//            }
//        });
//
//
//        btnInsertDB.setOnClickListener(new View.OnClickListener() {
//                                           @Override
//                                           public void onClick(View v) {
//                                               LinearLayout layout = new LinearLayout(MainActivity.this);
//                                               layout.setOrientation(LinearLayout.VERTICAL);
//
//                                               final EditText etName = new EditText(MainActivity.this);
//                                               etName.setHint("이름을 입력하세요.");
//
//                                               final EditText etAge = new EditText(MainActivity.this);
//                                               etAge.setHint("나이를 입력하세요.");
//
//                                               final EditText etPhone = new EditText(MainActivity.this);
//                                               etPhone.setHint("전화번호를 입력하세요.");
//
//                                               layout.addView(etName);
//                                               layout.addView(etAge);
//                                               layout.addView(etPhone);
//
//                                               AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
//                                               dialog.setTitle("정보를 입력하세요")
//                                                       .setView(layout)
//                                                       .setPositiveButton("등록", new DialogInterface.OnClickListener() {
//                                                           @Override public void onClick(DialogInterface dialog, int which) {
//                                                               String name = etName.getText().toString();
//                                                               String age = etAge.getText().toString();
//                                                               String phone = etPhone.getText().toString();
//
//                                                               if( dbHelper == null ) {
//                                                                   dbHelper = new DBHelper(MainActivity.this, "TEST", null , 1);
//                                                               }
//
//                                                               Person person = new Person();
//                                                               person.setName(name);
//                                                               person.setAge(age);
//                                                               person.setPhone(phone);
//                                                               dbHelper.addPerson(person);
//                                                           }
//                                                       })
//                                                       .setNeutralButton("취소", new DialogInterface.OnClickListener() {
//                                                           @Override public void onClick(DialogInterface dialog, int which) {
//
//                                                           }
//                                                       })
//                                                       .create()
//                                                       .show();
//                                           }
//                                       }
//        );
//
//        btnSelectDB.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        // ListView를 보여준다.
//                        lvPeople.setVisibility(View.VISIBLE);
//
//                        // DB Helper가 Null이면 초기화 시켜준다.
//                        if( dbHelper == null ) {
//                            dbHelper = new DBHelper(MainActivity.this,"TEST", null , 1);
//                        }
//
//                        // 1. Person 데이터를 모두 가져온다.
//                        List people = dbHelper.getAllPersonData();
//
//                        // 2. ListView에 Person 데이터를 모두 보여준다.
//                        lvPeople.setAdapter(new PersonListAdapter(people, MainActivity.this));
//                    }
//        });
//
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ExerciseManagement.class);
                startActivity(intent);
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Exerciseregit.class);
                startActivity(intent);
            }
        });
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ScheduleActivity.class);
                startActivity(intent);
            }
        });
    }
}
