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
        sb.append(" exe_idx_fk INTEGER");
        sb.append(" day TEXT, ");
        sb.append(" date TEXT, ");
        sb.append(" isSuccess TINYINT(1)");
        sb.append(" count_all INTEGER");
        sb.append(" count_now INTEGER");
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

        String[] exename = {"데드리프트", "스쿼트"};
        String[] execate = {"하체", "하체"};
        String[] execal = {"350", "360"};
        StringBuffer sb;

        // 1. 쓸 수 있는 DB 객체를 가져온다.
        SQLiteDatabase instance = getWritableDatabase();

        // 2. 운동 Data를 Insert한다.
        for(int i = 0 ; i < 2 ; i++){
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