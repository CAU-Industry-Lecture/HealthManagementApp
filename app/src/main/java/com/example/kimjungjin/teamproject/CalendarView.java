package com.example.kimjungjin.teamproject;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import DB.DBHelper;
import DB.Exercise;
import DB.Schedule;

/**
 * Created by a7med on 28/06/2015.
 */
public class CalendarView extends LinearLayout
{
	// for logging
	private static final String LOGTAG = "Calendar View";

	// how many days to show, defaults to six weeks, 42 days
	private static final int DAYS_COUNT = 42;

	// default date format
	private static final String DATE_FORMAT = "MMM yyyy";

	// date format
	private String dateFormat;

	// current displayed month
	private Calendar currentDate = Calendar.getInstance();

	//event handling
	private EventHandler eventHandler = null;

	// internal components
	private LinearLayout header;
	private ImageView btnPrev;
	private ImageView btnNext;
	private TextView txtDate;
	public GridView grid;
	private DBHelper dbHelper;

    ArrayList<String> data;
    //ListviewAdapter adapter;
    ArrayAdapter adapter;
    Context context;

	// seasons' rainbow
	int[] rainbow = new int[] {
			R.color.summer,
			R.color.fall,
			R.color.winter,
			R.color.spring
	};

	// month-season association (northern hemisphere, sorry australia :)
	int[] monthSeason = new int[] {2, 2, 3, 3, 3, 0, 0, 0, 1, 1, 1, 2};

	public CalendarView(Context context)
	{
		super(context);
		this.context = context;
	}

	public CalendarView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		initControl(context, attrs);
		this.context = context;
	}

	public CalendarView(Context context, AttributeSet attrs, int defStyleAttr)
	{
		super(context, attrs, defStyleAttr);
		initControl(context, attrs);
		this.context = context;
	}

	/**
	 * Load control xml layout
	 */
	private void initControl(Context context, AttributeSet attrs)
	{
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.control_calendar, this);

		loadDateFormat(attrs);
		assignUiElements();
		assignClickHandlers();

		updateCalendar();
	}

	private void loadDateFormat(AttributeSet attrs)
	{
		TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.CalendarView);

		try
		{
			// try to load provided date format, and fallback to default otherwise
			dateFormat = ta.getString(R.styleable.CalendarView_dateFormat);
			if (dateFormat == null)
				dateFormat = DATE_FORMAT;
		}
		finally
		{
			ta.recycle();
		}
	}
	private void assignUiElements()
	{
		// layout is inflated, assign local variables to components
		header = (LinearLayout)findViewById(R.id.calendar_header);
		btnPrev = (ImageView)findViewById(R.id.calendar_prev_button);
		btnNext = (ImageView)findViewById(R.id.calendar_next_button);
		txtDate = (TextView)findViewById(R.id.calendar_date_display);
		grid = (GridView)findViewById(R.id.calendar_grid);
	}

	private void assignClickHandlers()
	{
		// add one month and refresh UI
		btnNext.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				currentDate.add(Calendar.MONTH, 1);
				HashSet<Date> events = new HashSet<>();
				events.add(new Date());
				updateCalendar(events);
			}
		});

		// subtract one month and refresh UI
		btnPrev.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				currentDate.add(Calendar.MONTH, -1);
				HashSet<Date> events = new HashSet<>();
				events.add(new Date());
				updateCalendar(events);
			}
		});

		grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//            	LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            	View outerView = inflater.inflate(R.layout.activity_schedule, null);
//				List hidden_itmes = new ArrayList<Schedule>();
//				//((LinearLayout)view.findViewById(R.id.item_layout)).setBackgroundResource(R.drawable.grid_border);
//				ListView hidden_list = (ListView)view.findViewById(R.id.hidden_list);
//				int listCnt = hidden_list.getAdapter().getCount();
//				for(int i = 0 ; i < listCnt ; i++){
//					hidden_itmes.add(hidden_list.getAdapter().getItem(i));
//				}
//				ListView achieve_list = (ListView)outerView.findViewById(R.id.achieve_list);
//				adapter = new ArrayAdapter(context, android.R.layout.simple_list_item_1, hidden_itmes);
//				achieve_list.setAdapter(adapter);
                Toast.makeText(getContext(), parent.getAdapter().getItem(position).toString(), Toast.LENGTH_SHORT).show();
            }
        });
		// long-pressing a day
		grid.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
		{
			@Override
			public boolean onItemLongClick(AdapterView<?> view, View cell, int position, long id)
			{
				// handle long-press
				if (eventHandler == null)
					return false;

				eventHandler.onDayLongPress((Date)view.getItemAtPosition(position));
				return true;
			}

		});
	}

	/**
	 * Display dates correctly in grid
	 */
	public void updateCalendar()
	{
		updateCalendar(null);
	}

	/**
	 * Display dates correctly in grid
	 */
	public void updateCalendar(HashSet<Date> events)
	{
		ArrayList<Date> cells = new ArrayList<>();
		Calendar calendar = (Calendar)currentDate.clone();

		// determine the cell for current month's beginning
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		int monthBeginningCell = calendar.get(Calendar.DAY_OF_WEEK) - 1;

		// move calendar backwards to the beginning of the week
		calendar.add(Calendar.DAY_OF_MONTH, -monthBeginningCell);

		// fill cells
		while (cells.size() < DAYS_COUNT)
		{
			cells.add(calendar.getTime());
			calendar.add(Calendar.DAY_OF_MONTH, 1);
		}

		// update grid
		grid.setAdapter(new CalendarAdapter(getContext(), cells, events));

		// update title
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		txtDate.setText(sdf.format(currentDate.getTime()));

		// set header color according to current season
		int month = currentDate.get(Calendar.MONTH);
		int season = monthSeason[month];
		int color = rainbow[season];

		header.setBackgroundColor(getResources().getColor(color));
	}

	private class CalendarAdapter extends ArrayAdapter<Date>
	{

		private DBHelper dbHelper = new DBHelper( context, "capstone", null, 1);

		// days with events
		private HashSet<Date> eventDays;

		// for view inflation
		private LayoutInflater inflater;
//		List scheduleData = dbHelper.getAllScheduleData();
		Exercise testEx = null;

		int []isSuccess = new int[4]; // 성공여부

		public CalendarAdapter(Context context, ArrayList<Date> days, HashSet<Date> eventDays)
        {
			super(context, R.layout.item, days);
			this.eventDays = eventDays;
			inflater = LayoutInflater.from(context);
		}

		@Override
		public View getView(int position, View view, ViewGroup parent)
		{
			// day in question
			Date date = getItem(position);
			int day = date.getDate();
			int month = date.getMonth();
			int year = date.getYear();
			int cnt = 0 ; // 하루에 등록된 운동이 몇개인지
			// today
			Date today = new Date();
			List thisSchedule = new ArrayList<>();

			// inflate item if it does not exist yet
			if (view == null)
				view = inflater.inflate(R.layout.item, parent, false);

			// if this day has an event, specify event image
			view.setBackgroundResource(0);
			if (eventDays != null)
			{
				for (Date eventDate : eventDays)
				{
					if (eventDate.getDate() == day &&
							eventDate.getMonth() == month &&
							eventDate.getYear() == year)
					{
						// mark this day for event
						view.setBackgroundResource(R.drawable.reminder);
						break;
					}
				}

				List allScheduleData = dbHelper.getAllScheduleData();
				for (Object datas : allScheduleData) { //dbHelper.getAllScheduleData()
					List schData = (List)(datas);
					String dates = ((Schedule)schData.get(0)).getDate();
					String []scheduleDate = dates.split("-"); // 0 : year, 1 : month, 2 : day
					if(year == Integer.parseInt(scheduleDate[0])-1900 && month == Integer.parseInt(scheduleDate[1])-1 && day == Integer.parseInt(scheduleDate[2])) {
						cnt++;
						if(cnt == 1){
							((ImageView)view.findViewById(R.id.image_red)).setImageResource(R.drawable.circle_red);
						} else if(cnt == 2){
							((ImageView)view.findViewById(R.id.image_orange)).setImageResource(R.drawable.circle_orange);
						} else if(cnt == 3){
							((ImageView)view.findViewById(R.id.image_blue)).setImageResource(R.drawable.circle_blue);
						} else{
							((ImageView)view.findViewById(R.id.image_plus)).setImageResource(R.drawable.circle_plus);
						}
						thisSchedule.add(schData);
					}
				}
			}

			// clear styling
			((TextView)view.findViewById(R.id.textView1)).setTypeface(null, Typeface.NORMAL);
			((TextView)view.findViewById(R.id.textView1)).setTextColor(Color.BLACK);

			if (month != today.getMonth() || year != today.getYear())
			{
				// if this day is outside current month, grey it out
				((TextView)view.findViewById(R.id.textView1)).setTextColor(getResources().getColor(R.color.greyed_out));
			}
			else if (day == today.getDate())
			{
				// if it is today, set it to blue/bold
				((TextView)view.findViewById(R.id.textView1)).setTypeface(null, Typeface.BOLD);
				((TextView)view.findViewById(R.id.textView1)).setTextColor(getResources().getColor(R.color.today));
			}

			// set text
			((TextView)view.findViewById(R.id.textView1)).setText(String.valueOf(date.getDate()));


						  /*
	    	listview
	    	 */

            adapter = new ArrayAdapter(view.getContext(), android.R.layout.simple_list_item_1, thisSchedule);
            ((ListView)view.findViewById(R.id.hidden_list)).setAdapter(adapter);
//            adapter = new ListviewAdapter(view.getContext(), R.layout.day_item, thisSchedule);
//            ((ListView)view.findViewById(R.id.listView)).setAdapter(adapter);

			return view;
		}
	}

	/**
	 * Assign event handler to be passed needed events
	 */
	public void setEventHandler(EventHandler eventHandler)
	{
		this.eventHandler = eventHandler;
	}

	/**
	 * This interface defines what events to be reported to
	 * the outside world
	 */
	public interface EventHandler
	{
		void onDayLongPress(Date date);
	}
}
