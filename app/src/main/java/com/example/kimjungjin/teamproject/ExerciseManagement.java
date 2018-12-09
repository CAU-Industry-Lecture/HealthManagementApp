package com.example.kimjungjin.teamproject;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import DB.DBHelper;
import DB.Exercise;

public class ExerciseManagement extends AppCompatActivity {
    private static HashMap<String, Integer> calorieHash = new HashMap<>();
             static {
                  calorieHash.put("걷기", Integer.parseInt("200"));
                  calorieHash.put("달리기", Integer.parseInt("500"));
                  calorieHash.put("자전거", Integer.parseInt("400"));
                  calorieHash.put("줄넘기", Integer.parseInt("700"));
                  calorieHash.put("수영", Integer.parseInt("500"));
              }

    Button btnConfirm;
    Button btncancel;
    EditText Editkillo;
    RadioButton mon, tue, wed, thu, fri, sat, sun;
    long mNow;
    Date mDate;
    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd");
    String str_purpose;
    String str_time;
    String str_killo;
    String day;
    String date;
    SharedPreferences preferences;
    String age;
    String sex;
    String height;
    String weight;
    int cal;
    DatePicker datePicker;

    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_management);
        Editkillo = (EditText)findViewById(R.id.Editkillo);
        btnConfirm = (Button)findViewById(R.id.btnConfirm);
        btncancel = (Button)findViewById(R.id.btnCancel);
        // radio group for day
        mon = (RadioButton)findViewById(R.id.radio0);
        tue = (RadioButton)findViewById(R.id.radio1);
        wed = (RadioButton)findViewById(R.id.radio2);
        thu = (RadioButton)findViewById(R.id.radio3);
        fri = (RadioButton)findViewById(R.id.radio4);
        sat = (RadioButton)findViewById(R.id.radio5);
        sun = (RadioButton)findViewById(R.id.radio6);
        datePicker = (DatePicker)findViewById(R.id.datePicker);

        dbHelper = new DBHelper( ExerciseManagement.this, "capstone", null, 1);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        mon.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                boolean checked = ((RadioButton) v).isChecked();
                // Check which radiobutton was pressed
                if (checked){
                    mon.setChecked(false);
                }
                else{
                    mon.setChecked(true);
                }
            }
            });

        tue.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                boolean checked = ((RadioButton) v).isChecked();
                // Check which radiobutton was pressed
                if (checked){
                    tue.setChecked(false);
                }
                else{
                    tue.setChecked(true);
                }
            }
        });

        wed.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                boolean checked = ((RadioButton) v).isChecked();
                // Check which radiobutton was pressed
                if (checked){
                    wed.setChecked(false);
                }
                else{
                    wed.setChecked(true);
                }
            }
        });

        thu.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                boolean checked = ((RadioButton) v).isChecked();
                // Check which radiobutton was pressed
                if (checked){
                    thu.setChecked(false);
                }
                else{
                    thu.setChecked(true);
                }
            }
        });

        fri.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                boolean checked = ((RadioButton) v).isChecked();
                // Check which radiobutton was pressed
                if (checked){
                    fri.setChecked(false);
                }
                else{
                    fri.setChecked(true);
                }
            }
        });

        sat.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                boolean checked = ((RadioButton) v).isChecked();
                // Check which radiobutton was pressed
                if (checked){
                    sat.setChecked(false);
                }
                else{
                    sat.setChecked(true);
                }
            }
        });

        sun.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                boolean checked = ((RadioButton) v).isChecked();
                // Check which radiobutton was pressed
                if (checked){
                    sun.setChecked(false);
                }
                else{
                    sun.setChecked(true);
                }
            }
        });

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

        /// 필요한 data들
        day = String.valueOf(doDayOfWeek());
        date = getTime();
        preferences = getSharedPreferences("userInfo", 0);
        age = preferences.getString("age", null);
        sex = preferences.getString("sex", null);
        height = preferences.getString("height", null);
        weight = preferences.getString("weight", null);


        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Editkillo.getText().toString().length() == 0) {
                    Toast.makeText(ExerciseManagement.this, "목표 몸무게를 입력해주세요", Toast.LENGTH_SHORT).show();
                } else {

                    ArrayList<String> date = new ArrayList<String>(); // String형의 ArrayList 인스턴스를 만듬

                    if (mon.isChecked() == true)
                        date.add("월");
                    if (tue.isChecked() == true)
                        date.add("화");
                    if (wed.isChecked() == true)
                        date.add("수");
                    if (thu.isChecked() == true)
                        date.add("목");
                    if (fri.isChecked() == true)
                        date.add("금");
                    if (sat.isChecked() == true)
                        date.add("토");
                    if (sun.isChecked() == true)
                        date.add("일");
                    if (date.isEmpty()) {
                        Toast.makeText(ExerciseManagement.this, "하루 이상 선택해주세요", Toast.LENGTH_SHORT).show();
                    } else {
                        str_purpose = purposeSpinner.getSelectedItem().toString();
                        str_time = timeSpinner.getSelectedItem().toString();
                        str_killo = Editkillo.getText().toString(); // 기존에 있던것과 비교해서 높으면(다이어트니까) 재설정 하라는 토스트 띄우기


                        // datepicker 날짜 검증 토스트 띄우기
                        switch (str_purpose){
                            case "다이어트" :
                                ArrayList<String> exercise2 = dbHelper.getInterestExerciseName2();
                                cal = getLossCaloriesPerDay(sex, age, height, weight, str_killo);
                                if( cal < 100 )
                                {
                                    Toast.makeText(ExerciseManagement.this, "목표수치가 너무 낮습니다. 다시 설정해주세요", Toast.LENGTH_SHORT).show();
                                } else if( Integer.parseInt(weight) < Integer.parseInt(str_killo) ){
                                    Toast.makeText(ExerciseManagement.this, "다이어트는 기존 몸무게보다 낮게 설정하셔야 합니다.", Toast.LENGTH_SHORT).show();
                                } else if( exercise2.size() == 0){
                                    Toast.makeText(ExerciseManagement.this, "관심운동을 등록해주세요.", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    List<List<Map.Entry<String, Integer>>> test = getExercisePerformCounts(str_time, date.size(), cal, exercise2);
                                    // date 정보(요일정보), datepicker의 시작정보를 가지고 스케쥴 생성하는 함수 하나 만들어서 넣기
                                    setSchedule(date, test, datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
                                    Toast.makeText(ExerciseManagement.this, Integer.toString(cal), Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(ExerciseManagement.this, MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    Toast.makeText(ExerciseManagement.this, date.toString(), Toast.LENGTH_SHORT).show();
                                    Toast.makeText(ExerciseManagement.this, "스케줄이 생성되었습니다.", Toast.LENGTH_SHORT).show();
                                    startActivity(intent);
                                    //finish();
                                }
                                break;
                            case "근육운동" :
                                ArrayList<String> exercise = dbHelper.getInterestExerciseName();
                                cal = getLossCaloriesPerDay(sex, age, height, weight, str_killo);
                                setSchedule2(date, exercise, str_time, datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
                                Toast.makeText(ExerciseManagement.this, Integer.toString(cal), Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(ExerciseManagement.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                Toast.makeText(ExerciseManagement.this, date.toString(), Toast.LENGTH_SHORT).show();
                                Toast.makeText(ExerciseManagement.this, "스케줄이 생성되었습니다.", Toast.LENGTH_SHORT).show();
                                startActivity(intent);
                                break;
                        }
                    }
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

    public void setSchedule2(ArrayList<String> date, ArrayList<String> exercise, String weekString, int year, int month, int day){
        Calendar calendar = new GregorianCalendar(Locale.KOREA);
        calendar.set(year, month, day);
        calendar.set(Calendar.MILLISECOND,0);

        int totalDays = Integer.parseInt(weekString.substring(0, 1)) * date.size();

        String schDay = "";
        switch(calendar.get(Calendar.DAY_OF_WEEK)){
            case 1 :
                schDay = "일";
                break;
            case 2 :
                schDay = "월";
                break;
            case 3 :
                schDay = "화";
                break;
            case 4 :
                schDay = "수";
                break;
            case 5 :
                schDay = "목";
                break;
            case 6 :
                schDay = "금";
                break;
            case 7 :
                schDay = "토";
                break;
        }

        int cnt = 0;
        while(true){
            if(date.contains(schDay)){
                if(schDay.equals("월") && exercise.contains("벤치프레스")){
                    dbHelper.addScheduleData("벤치프레스", schDay, (calendar.getTime().getYear() + 1900)
                                    + "-" + (calendar.getTime().getMonth() + 1)
                                    + "-" + calendar.getTime().getDate(),
                            12);
                } else if(schDay.equals("화") && exercise.contains("싯업")){
                    dbHelper.addScheduleData("싯업", schDay, (calendar.getTime().getYear() + 1900)
                                    + "-" + (calendar.getTime().getMonth() + 1)
                                    + "-" + calendar.getTime().getDate(),
                            12);
                } else if(schDay.equals("수") && exercise.contains("스쿼트")){
                    dbHelper.addScheduleData("스쿼트", schDay, (calendar.getTime().getYear() + 1900)
                                    + "-" + (calendar.getTime().getMonth() + 1)
                                    + "-" + calendar.getTime().getDate(),
                            12);
                } else if(schDay.equals("목") && exercise.contains("데드리프트")){
                    dbHelper.addScheduleData("데드리프트", schDay, (calendar.getTime().getYear() + 1900)
                                    + "-" + (calendar.getTime().getMonth() + 1)
                                    + "-" + calendar.getTime().getDate(),
                            12);
                } else if(schDay.equals("금") && exercise.contains("싯업")){
                    dbHelper.addScheduleData("싯업", schDay, (calendar.getTime().getYear() + 1900)
                                    + "-" + (calendar.getTime().getMonth() + 1)
                                    + "-" + calendar.getTime().getDate(),
                            12);
                } else if(schDay.equals("토") && exercise.contains("스쿼트")){
                    dbHelper.addScheduleData("스쿼트", schDay, (calendar.getTime().getYear() + 1900)
                                    + "-" + (calendar.getTime().getMonth() + 1)
                                    + "-" + calendar.getTime().getDate(),
                            12);
                }
                cnt++;
            }
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            switch(calendar.get(Calendar.DAY_OF_WEEK)){
                case 1 :
                    schDay = "일";
                    break;
                case 2 :
                    schDay = "월";
                    break;
                case 3 :
                    schDay = "화";
                    break;
                case 4 :
                    schDay = "수";
                    break;
                case 5 :
                    schDay = "목";
                    break;
                case 6 :
                    schDay = "금";
                    break;
                case 7 :
                    schDay = "토";
                    break;
            }

            if(totalDays == cnt)
                break;
        }
    }

    public void setSchedule(ArrayList<String> date, List<List<Map.Entry<String, Integer>>> schedule, int year, int month, int day){
        Calendar calendar = new GregorianCalendar(Locale.KOREA);
        Date date1 = new Date();
        date1.setYear(year);
        date1.setMonth(month);
        date1.setDate(day);
        calendar.set(year, month, day);
        calendar.set(Calendar.MILLISECOND,0);

        String schDay = "";
        switch(calendar.get(Calendar.DAY_OF_WEEK)){
            case 1 :
                schDay = "일";
                break;
            case 2 :
                schDay = "월";
                break;
            case 3 :
                schDay = "화";
                break;
            case 4 :
                schDay = "수";
                break;
            case 5 :
                schDay = "목";
                break;
            case 6 :
                schDay = "금";
                break;
            case 7 :
                schDay = "토";
                break;
        }
        SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd");

        int cnt = 0;
        while(true){
            if(date.contains(schDay)){
                for(int i = 0 ; i < schedule.get(cnt).size() ; i++){
                    dbHelper.addScheduleData(schedule.get(cnt).get(i).getKey(), schDay, (calendar.getTime().getYear() + 1900)
                                    + "-" + (calendar.getTime().getMonth() + 1)
                                    + "-" + calendar.getTime().getDate(),
                            schedule.get(cnt).get(i).getValue());
                }
                cnt++;
            }
                calendar.add(Calendar.DAY_OF_YEAR, 1);
                switch(calendar.get(Calendar.DAY_OF_WEEK)){
                    case 1 :
                        schDay = "일";
                        break;
                    case 2 :
                        schDay = "월";
                        break;
                    case 3 :
                        schDay = "화";
                        break;
                    case 4 :
                        schDay = "수";
                        break;
                    case 5 :
                        schDay = "목";
                        break;
                    case 6 :
                        schDay = "금";
                        break;
                    case 7 :
                        schDay = "토";
                        break;
                }

            if(schedule.size() == cnt)
                break;
        }

        for(String dayOfWeek : date) {

            switch(dayOfWeek){
                case "월" :
                    break;
                case "화" :
                    break;
                case "수" :
                    break;
                case "목" :
                    break;
                case "금" :
                    break;
                case "토" :
                    break;
                case "일" :
                    break;

            }
        }
    }

    @Override
    public  void onBackPressed(){
        super.onBackPressed();
    }

    public String doDayOfWeek() {
        Calendar cal = Calendar.getInstance();
        String strWeek = null;

        int nWeek = cal.get(Calendar.DAY_OF_WEEK);
        if (nWeek == 1){
            strWeek = "일";
        } else if (nWeek == 2){
            strWeek = "월";
        } else if (nWeek == 3){
            strWeek = "화";
        } else if (nWeek == 4){
            strWeek = "수";
        } else if (nWeek == 5){
            strWeek = "목";
        } else if (nWeek == 6){
            strWeek = "금";
        } else if (nWeek == 7){
            strWeek = "토";
        }
        return strWeek;
    }

    public String getTime(){
        mNow = System.currentTimeMillis();
        mDate = new Date(mNow);
        return mFormat.format(mDate);
    }
    public static int getLossCaloriesPerDay(String gender, String age, String height, String currentWeight, String targetWeight) {
          int convertedAge = Integer.parseInt(age);
          double convertedHeight = Double.parseDouble(height);
          double convertedCurrentWeight = Double.parseDouble(currentWeight);
          double convertedTargetWeight = Double.parseDouble(targetWeight);
          double lossCalories;
          if (gender.equals("남자")) {
              lossCalories = 662 - (9.53 * convertedAge) + 1.185 + (15.91 * convertedCurrentWeight) + (539.6 * convertedHeight * 0.0328) * 2.54 / 100;
              } else {
              lossCalories = 354 - (6.91 * convertedAge) + 1.185 + (9.36 * convertedCurrentWeight) + (726 * convertedHeight * 0.0328) * 2.54 / 100;
              }

              double weightDifference = convertedCurrentWeight - convertedTargetWeight;


                  return (int) Math.floor((0.85 * lossCalories) / (weightDifference * 3500) * (weightDifference * 3500));
              }
    public static List<List<Map.Entry<String, Integer>>> getExercisePerformCounts(String weekString, int howMuchInWeek, int totalCalories, List<String> exerciseNames) {
                  int firstExerciseIndex = 0;
                  int secondExerciseIndex = 1;
                  int totalDays = Integer.parseInt(weekString.substring(0, 1)) * howMuchInWeek;


                  List<List<Map.Entry<String, Integer>>> exercisePerformCounts = new ArrayList<>();
                  List<Integer> minutesLossCalories = new ArrayList<>();


                  for (String exerciseName : exerciseNames) {
                          minutesLossCalories.add(calorieHash.get(exerciseName) / 60);
                      }


                  for (int elapsedDay = 0; elapsedDay < totalDays; ++elapsedDay) {
                          exercisePerformCounts.add(new ArrayList<Map.Entry<String, Integer>>());

                          Map.Entry<Integer, Integer> exercisePerformCount
                                  = solveIndeterminateEquation(minutesLossCalories.get(firstExerciseIndex), minutesLossCalories.get(secondExerciseIndex), totalCalories);


                          exercisePerformCounts.get(elapsedDay).add(new AbstractMap.SimpleEntry<>(exerciseNames.get(firstExerciseIndex), exercisePerformCount.getKey()));


                          if (exerciseNames.size() > 1) {
                                  exercisePerformCounts.get(elapsedDay).add(new AbstractMap.SimpleEntry<>(exerciseNames.get(secondExerciseIndex), exercisePerformCount.getValue()));
                              }


                          firstExerciseIndex = (firstExerciseIndex + 1) % exerciseNames.size();
                          secondExerciseIndex = (secondExerciseIndex + 1) % exerciseNames.size();
                      }


                  return exercisePerformCounts;
              }
              /**
 48       * Solve an equation "ax + by = c".
 49       * @param a coefficient of x.
 50       * @param b coefficient of y.
 51       * @param c equation constant.
 52       * @return a solution that has the smallest difference between x and y.
 53       */
             public static Map.Entry<Integer, Integer> solveIndeterminateEquation(final int a, final int b, final int c) {
                  double maxX = Math.ceil((double) c / a);


                  List<Map.Entry<Integer, Integer>> solutions = new ArrayList<>();


                  for (int x = 0; x < maxX; ++x) {
                          int y = (int) Math.ceil((c - a * x) / (double) b);


                          if (y < 0) {
                                  break;
                              }


                          solutions.add(new AbstractMap.SimpleEntry<>(x, y));
                      }


                  Collections.sort(solutions, new Comparator<Map.Entry<Integer, Integer>>() {
              @Override
              public int compare(Map.Entry<Integer, Integer> left, Map.Entry<Integer, Integer> right) {
                                  int differenceCompareValue = Integer.valueOf(Math.abs(left.getKey() - left.getValue()))
                                          .compareTo(Math.abs(right.getKey() - right.getValue()));

                                  if (differenceCompareValue != 0) {
                                          return differenceCompareValue;
                                      }

                                  return Integer.valueOf(Math.abs(c - (a * left.getKey() + b * left.getValue())))
                                          .compareTo(Math.abs(c - (a * right.getKey() + b * right.getValue())));
                              }
          });
                  return solutions.get(0);
              }
}
