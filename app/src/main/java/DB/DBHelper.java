package DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DBHelper extends SQLiteOpenHelper {
    private Context context;

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
    }
        /*
         Database가 존재하지 않을 때, 딱 한번 실행된다.
         DB를 만드는 역할을 한다.
         @param db
        */

    @Override
    public void onCreate(SQLiteDatabase db) {

        if (!db.isReadOnly()) {
            // Enable foreign key constraints
            db.execSQL("PRAGMA foreign_keys='ON';");
        }

        // EXERCISE 테이블 생성
        StringBuffer sb = new StringBuffer();
        sb.append(" CREATE TABLE EXERCISE ( ");
        sb.append(" exe_idx INTEGER PRIMARY KEY AUTOINCREMENT, ");
        sb.append(" exe_name TEXT, ");
        sb.append(" exe_cate TEXT, ");
        sb.append(" exe_cal TEXT, ");
        sb.append(" interest TEXT) ");
        try{
            db.execSQL(sb.toString());
            Log.d("t","생성완료1");
        } catch (Exception e){
            Log.d("t","오류1");
        }

        // SCHEDULE 테이블 생성
        sb = new StringBuffer();
        sb.append(" CREATE TABLE SCHEDULE ( ");
        sb.append(" sch_idx INTEGER PRIMARY KEY AUTOINCREMENT, ");
        sb.append(" exe_idx_fk INTEGER,");
        sb.append(" day TEXT, ");
        sb.append(" date TEXT, ");
        sb.append(" isSuccess TINYINT(1),");
        sb.append(" time INTEGER,");
        sb.append(" FOREIGN KEY (exe_idx_fk) REFERENCES EXERCISE(exe_idx)) ");


        try{
            db.execSQL(sb.toString());
            Log.d("t","생성완료2");
        } catch (Exception e){
            Log.d("t","오류2");
        }
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Toast.makeText(context, "버전이 올라갔습니다.", Toast.LENGTH_SHORT).show();
    }

    public void testDB() {
        SQLiteDatabase db = getReadableDatabase();
    }

    public void addExerciseData() {

        String[] exename = {"걷기", "달리기", "자전거", "줄넘기", "수영", "벤치프레스", "싯업", "스쿼트", "데드리프트", "레그프레스"};
        String[] execate = {"유산소", "유산소", "유산소", "유산소", "유산소", "근육운동", "근육운동", "근육운동", "근육운동", "근육운동"};
        String[] execal = {"200", "500", "400", "700", "500", "12", "12", "12", "12", "12"};
        StringBuffer sb;

        // 1. 쓸 수 있는 DB 객체를 가져온다.
        SQLiteDatabase instance = getWritableDatabase();

        // 2. 운동 Data를 Insert한다.
        for(int i = 0 ; i < 10 ; i++){
            sb = new StringBuffer();
            sb.append(" INSERT INTO EXERCISE ( ");
            sb.append(" exe_name, exe_cate, exe_cal, interest) ");
            sb.append(" VALUES ( ?, ?, ?, ? ) ");
            try{
                instance.execSQL(sb.toString(), new Object[]{exename[i], execate[i], execal[i], "NO"});
                Log.d("t","생성완료3");
            } catch (Exception e){
                Log.d("t","오류3");
            }
        }
        instance.close();
    }

    public void setScheduleIsSuccess(int sch_idx, int isSuccess) {
        StringBuffer sb;

        SQLiteDatabase db = getWritableDatabase();

        sb = new StringBuffer();
        sb.append(" UPDATE SCHEDULE SET isSuccess = ? WHERE sch_idx = ? ");

        try{
            db.execSQL(sb.toString(), new Object[]{isSuccess, sch_idx});
            Log.d("t","생성완료");
        } catch (Exception e){
            Log.d("t","오류");
        }

        db.close();
    }

    public void addScheduleData(String exe_name, String day, String date, int time){
        StringBuffer sb;

        SQLiteDatabase db = getWritableDatabase();

        sb = new StringBuffer();
        sb.append(" SELECT exe_idx FROM EXERCISE WHERE exe_name = '" + exe_name + "'");

        Cursor cursor = db.rawQuery(sb.toString(), null);
        cursor.moveToFirst();
        int exe_idx_fk = cursor.getInt(cursor.getColumnIndex("exe_idx"));

        sb = new StringBuffer();
        sb.append(" INSERT INTO SCHEDULE ( ");
        sb.append(" exe_idx_fk, day, date, isSuccess, time) ");
        sb.append(" VALUES (?, ?, ?, ?, ?) ");
        try{
            db.execSQL(sb.toString(), new Object[]{exe_idx_fk, day, date, 0, time});
            Log.d("t","생성완료");
        } catch (Exception e){
            Log.d("t","오류");
        }
        db.close();
    }

    public List<List> getAllScheduleData() {
        StringBuffer sb = new StringBuffer();
        sb.append(" SELECT * FROM EXERCISE JOIN SCHEDULE ON(EXERCISE.exe_idx = SCHEDULE.exe_idx_fk)");
//        sb.append(" SELECT * FROM EXERCISE NATURAL JOIN SCHEDULE");
        // 읽기 전용 DB 객체를 만든다.
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(sb.toString(), null);
        List Info = new ArrayList();
        Schedule schedule = null;
        Exercise exercise = null;
        // moveToNext 다음에 데이터가 있으면 true 없으면 false
        while (cursor.moveToNext()) {
            schedule = new Schedule();
            exercise = new Exercise();
            schedule.setSch_idx(cursor.getInt(cursor.getColumnIndex("sch_idx")));
            schedule.setExe_idx_fk(cursor.getInt(cursor.getColumnIndex("exe_idx_fk")));
            schedule.setDay(cursor.getString(cursor.getColumnIndex("day")));
            schedule.setDate(cursor.getString(cursor.getColumnIndex("date")));
            schedule.setIsSuccess(cursor.getInt(cursor.getColumnIndex("isSuccess")));
            schedule.settime(cursor.getInt(cursor.getColumnIndex("time")));
            exercise.setExe_name(cursor.getString(cursor.getColumnIndex("exe_name")));
            List pack = new ArrayList<>();
            pack.add(schedule);
            pack.add(exercise);
            Info.add(pack);
        }

        return Info;
    }

    public List<Schedule> getAllScheduleDataByDate(String date) {
        StringBuffer sb = new StringBuffer();
        sb.append(" SELECT * FROM EXERCISE JOIN SCHEDULE ON(EXERCISE.exe_idx = SCHEDULE.exe_idx_fk) WHERE date = '" + date + "';");

        // 읽기 전용 DB 객체를 만든다.
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(sb.toString(), null);
        List Info = new ArrayList();
        Schedule schedule = null;

        // moveToNext 다음에 데이터가 있으면 true 없으면 false
        while (cursor.moveToNext()) {
            schedule = new Schedule();
            schedule.setExe_idx_fk(cursor.getInt(0));
            schedule.setDay(cursor.getString(1));
            schedule.setDate(cursor.getString(2));
            schedule.setIsSuccess(cursor.getInt(3));
            schedule.settime(cursor.getInt(4));

            Info.add(schedule);
        }

        return Info;
    }

    public List getAllExerciseData() {
        StringBuffer sb = new StringBuffer();
        sb.append(" SELECT exe_name, exe_cate, exe_cal, interest FROM EXERCISE ");

        // 읽기 전용 DB 객체를 만든다.
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(sb.toString(), null);
        List Info = new ArrayList();
        Exercise exercise = null;

        // moveToNext 다음에 데이터가 있으면 true 없으면 false
        while (cursor.moveToNext()) {
            exercise = new Exercise();
            exercise.setExe_name(cursor.getString(0));
            exercise.setExe_cate(cursor.getString(1));
            exercise.setExe_cal(cursor.getString(2));
            exercise.setInterest(cursor.getString(3));

            Info.add(exercise);
        }

        return Info;
    }

    public ArrayList<String> getAllExerciseName() {
        StringBuffer sb = new StringBuffer();
        sb.append(" SELECT exe_name FROM EXERCISE WHERE interest =" + "'" + "NO" + "'");

        // 읽기 전용 DB 객체를 만든다.
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(sb.toString(), null);
        ArrayList<String> Info = new ArrayList<String>();
        Exercise exercise = null;

        // moveToNext 다음에 데이터가 있으면 true 없으면 false
        while (cursor.moveToNext()) {
            exercise = new Exercise();
            exercise.setExe_name(cursor.getString(0));
            Info.add(exercise.getExe_name());
        }
        return Info;
    }


    public ArrayList<String> getInterestExerciseName() {
        StringBuffer sb = new StringBuffer();
        sb.append(" SELECT exe_name FROM EXERCISE WHERE interest =" + "'" + "YES" + "'");

        // 읽기 전용 DB 객체를 만든다.
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(sb.toString(), null);
        ArrayList<String> Info = new ArrayList<String>();
        Exercise exercise = null;

        // moveToNext 다음에 데이터가 있으면 true 없으면 false
        while (cursor.moveToNext()) {
            exercise = new Exercise();
            exercise.setExe_name(cursor.getString(0));
            Info.add(exercise.getExe_name());
        }
        return Info;
    }

    public void updateInterest( String exe_name , String interest){
        // 1이면 YES로 업데이트
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("interest", interest);
        db.update("EXERCISE",values,"exe_name=?",new String[]{exe_name});
    }
}