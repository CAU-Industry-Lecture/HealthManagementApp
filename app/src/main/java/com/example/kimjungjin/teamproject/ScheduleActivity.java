package com.example.kimjungjin.teamproject;


import android.app.Activity;
import android.content.Context;
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
import DB.Schedule;


public class ScheduleActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_schedule);

		HashSet<Date> events = new HashSet<>();
		events.add(new Date());

		CalendarView cv = ((CalendarView)findViewById(R.id.calendar_view));
		cv.updateCalendar(events);

		cv.grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Date date = (Date)parent.getAdapter().getItem(position);
				((TextView)findViewById(R.id.date)).setText(date.getYear()+1900 + " " + (date.getMonth()+1) + " " + date.getDate());

				ArrayList hidden_itmes = new ArrayList<Schedule>();
				//((LinearLayout)view.findViewById(R.id.item_layout)).setBackgroundResource(R.drawable.grid_border);
				ListView hidden_list = (ListView)view.findViewById(R.id.hidden_list);
				int listCnt = hidden_list.getAdapter().getCount();
				int achieve_cnt = 0 ; // 달성률

				for(int i = 0 ; i < listCnt ; i++){
					Schedule tmpSch = (Schedule)hidden_list.getAdapter().getItem(i);
					if(tmpSch.getIsSuccess().equals("1")){
						achieve_cnt++;
					}
					hidden_itmes.add(tmpSch);
				}

				TextView achieve = (TextView)findViewById(R.id.achieve);
				achieve.setText((int)(((double)achieve_cnt/(double)listCnt)*100) + "%");
				ListView achieve_list = (ListView)findViewById(R.id.achieve_list);
				ListviewAdapter adapter = new ListviewAdapter(hidden_itmes);
				achieve_list.setAdapter(adapter);
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
		private ArrayList<Schedule> data = new ArrayList<>();

		ListviewAdapter(ArrayList<Schedule> data){
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

			TextView day_item =(TextView)convertView.findViewById(R.id.day_item);
			Schedule dataSet = data.get(position);
			day_item.setText(dataSet.getIsSuccess());
			return convertView;
		}
	}
}
