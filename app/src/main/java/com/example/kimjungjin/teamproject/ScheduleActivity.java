package com.example.kimjungjin.teamproject;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import DB.DBHelper;
import DB.Exercise;
import DB.Schedule;

public class ScheduleActivity extends Activity {
	private DBHelper dbHelper;
	AlertDialog.Builder alertDialogBuilder;
	int sch_idx = 0 ; // 리스트 뷰 클릭 후 다이얼로그에서 어떤 리스트를 클릭했는 지(어떤 운동인지)를 식별하기 위해
	ListviewAdapter adapter;
	ListView achieve_list;
	String clickedDate; // 리스트 뷰 클릭 후 다이얼로그에서 어떤 리스트를 클릭했는 지를 식별하기 위해
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_schedule);

		dbHelper = new DBHelper( ScheduleActivity.this, "capstone", null, 1);

//		dbHelper.addScheduleData("데드리프트", "목", "2018-12-06", 10);
//		dbHelper.addScheduleData("스쿼트", "목", "2018-12-06", 10);
//		dbHelper.addScheduleData("데드리프트", "금", "2018-12-07", 10);
//		dbHelper.addScheduleData("데드리프트", "토", "2018-12-08", 10);

		HashSet<Date> events = new HashSet<>();
		events.add(new Date());

		CalendarView cv = ((CalendarView)findViewById(R.id.calendar_view));
		cv.updateCalendar(events);
		alertDialogBuilder = new AlertDialog.Builder(ScheduleActivity.this);
		// 제목셋팅
		alertDialogBuilder.setTitle("성공여부 확인");

		// AlertDialog 셋팅
		alertDialogBuilder.setMessage("이 운동을 수행하셨습니까?")
						  .setCancelable(false)
						  .setPositiveButton("네",
			new DialogInterface.OnClickListener() {
				public void onClick(
						DialogInterface dialog, int id) {
					// 프로그램을 종료한다
//					ScheduleActivity.this.finish();
					/*
					TODO : 스케쥴에서 isSuccess를 "1"로 체크, listview update
					 */
					dbHelper.setScheduleIsSuccess(sch_idx, 1);
					reloadAllData(clickedDate);
				}
			})
			.setNegativeButton("아니요",
					new DialogInterface.OnClickListener() {
						public void onClick(
								DialogInterface dialog, int id) {
							// 다이얼로그를 취소한다
//							dialog.cancel();
							/*
							TODO : 스케쥴에서 isSuccess를 "0"으로 체크, listview update
							 */
							dbHelper.setScheduleIsSuccess(sch_idx, 0);
							reloadAllData(clickedDate);
						}
					});


		cv.grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Date date = (Date)parent.getAdapter().getItem(position);
				int year = date.getYear() + 1900;
				int month = date.getMonth() + 1;
				int day = date.getDate();
				String yyyymmdd = year + "-" + month + "-" + day;

				((TextView)findViewById(R.id.date)).setText(yyyymmdd);

				List schData = dbHelper.getAllScheduleDataByDate(yyyymmdd);
				int listCnt = schData.size();
				int achieve_cnt = 0 ; // 달성률

				clickedDate = yyyymmdd;

				for(int i = 0 ; i < listCnt ; i++){
					if(((Schedule)((List)schData.get(i)).get(0)).getIsSuccess() == 1){
						achieve_cnt++;
					}
				}

				TextView achieve = (TextView)findViewById(R.id.achieve);
				achieve.setText((int)(((double)achieve_cnt/(double)listCnt)*100) + "%");
				achieve_list = (ListView)findViewById(R.id.achieve_list);
				adapter = new ListviewAdapter(schData);
				achieve_list.setAdapter(adapter);
			}
		});

		((ListView)findViewById(R.id.achieve_list)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				AlertDialog alertDialog = alertDialogBuilder.create();
				Schedule schedule = (Schedule)((List)parent.getItemAtPosition(position)).get(0);
				sch_idx = schedule.getSch_idx();
 				alertDialog.show();
			}
		});

		// assign event handler
		cv.setEventHandler(new CalendarView.EventHandler()
		{
			@Override
			public void onDayLongPress(Date date)
			{
				// show returned day
				DateFormat df = SimpleDateFormat.getDateInstance();
				Toast.makeText(getApplicationContext(), df.format(date), Toast.LENGTH_SHORT).show();
			}
		});
	}

	public void reloadAllData(String yyyymmdd) {
		List<List> data = dbHelper.getAllScheduleDataByDate(yyyymmdd);
		int listCnt = data.size();
		int achieve_cnt = 0 ; // 달성률
		for(int i = 0 ; i < listCnt ; i++){
			if(((Schedule)((List)data.get(i)).get(0)).getIsSuccess() == 1){
				achieve_cnt++;
			}
		}

		TextView achieve = (TextView)findViewById(R.id.achieve);
		achieve.setText((int)(((double)achieve_cnt/(double)listCnt)*100) + "%");
		adapter = new ListviewAdapter(data);
		achieve_list.setAdapter(adapter);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings)
		{
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	private class ListviewAdapter extends BaseAdapter {
		private List<List> data;
		ListviewAdapter(List<List> data){
			this.data=data;
		}
		@Override
		public int getCount() {
			return data.size();
		}

		@Override
		public Object getItem(int position) {
			return data.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Context context = parent.getContext();
			if(convertView==null){
				LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.day_item, parent, false);
			}

			TextView day_item = (TextView)convertView.findViewById(R.id.day_item);
			TextView day_count = (TextView)convertView.findViewById(R.id.day_count);
			TextView day_isSuccess = (TextView)convertView.findViewById(R.id.day_isSuccess);

			List dataSet = data.get(position);
			day_item.setText(((Exercise)dataSet.get(1)).getExe_name());
			if(day_item.getText().equals("걷기") || day_item.getText().equals("달리기") ||
					day_item.getText().equals("자전거") || day_item.getText().equals("줄넘기")
					|| day_item.getText().equals("수영")){
				day_count.setText(Integer.toString(((Schedule)dataSet.get(0)).gettime())+ "분");
			} else {
				day_count.setText(Integer.toString(((Schedule)dataSet.get(0)).gettime())+ "x3세트");
			}
			if(((Schedule)dataSet.get(0)).getIsSuccess() == 1){
				day_isSuccess.setText("성공");
			} else {
				day_isSuccess.setText("미성공");
			}

			return convertView;
		}
	}
}
