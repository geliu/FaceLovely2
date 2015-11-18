package com.cqu.android.Activity;

import android.app.Activity;
import android.content.Intent;
import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGestureListener;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.cqu.android.allservice.chart.FlowChart;
import com.cqu.android.allservice.chart.MonthChart;
import com.cqu.android.allservice.chart.WeekChart;
import com.cqu.android.bean.onGestureListener;

public class statist extends Activity implements OnTouchListener, OnGestureListener{
	
	//申明GestureDetector对象
	GestureDetector mGestureDetector;
		
	private FlowChart flowchart = new FlowChart();
	private WeekChart weekchart = new WeekChart();
	private MonthChart monthchart = new MonthChart();
	private ImageView dayChartButton = null;
	private ImageView weekChartButton = null;
	private ImageView monthChartButton = null;
				
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.statistchart);
		this.setTitle("流量统计");
	

		mGestureDetector = new GestureDetector(this,new onGestureListener(statist.this));
	    LinearLayout viewSnsLayout = (LinearLayout)findViewById(R.id.layout);  
	    viewSnsLayout.setOnTouchListener(this);  
	    viewSnsLayout.setLongClickable(true);
	    
		dayChartButton = (ImageView)findViewById(R.id.dayChart);
		weekChartButton = (ImageView)findViewById(R.id.weekChart);
		monthChartButton = (ImageView)findViewById(R.id.monthChart);
		dayChartButton.setOnClickListener(new DayChartListener());
		weekChartButton.setOnClickListener(new WeekChartListener());
		monthChartButton.setOnClickListener(new MonthChartListener());
		
	}
	
	class DayChartListener implements OnClickListener
	{

		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = null;
			intent = flowchart.execute(statist.this);

			startActivity(intent);
		}
		
	}
	class WeekChartListener implements OnClickListener{

		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = null;
			intent = weekchart.execute(statist.this);

			startActivity(intent);
		}
		
	}
	
	class MonthChartListener implements OnClickListener{

		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = null;
			intent = monthchart.execute(statist.this);

			startActivity(intent);
		}
		
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		// 按下键盘上返回按钮

		if (keyCode == KeyEvent.KEYCODE_BACK) {

			Intent intent1 = new Intent();
			intent1.setClass(statist.this, mainPage.class);
			startActivity(intent1);
			statist.this.finish();
		}
		return true;

	}
	public boolean onTouch(View v, MotionEvent event) {
		return mGestureDetector.onTouchEvent(event);
	}
public void onGesture(GestureOverlayView overlay, MotionEvent event) {
	// TODO Auto-generated method stub
	
}
public void onGestureCancelled(GestureOverlayView overlay, MotionEvent event) {
	// TODO Auto-generated method stub
	
}
public void onGestureEnded(GestureOverlayView overlay, MotionEvent event) {
	// TODO Auto-generated method stub
	
}
public void onGestureStarted(GestureOverlayView overlay, MotionEvent event) {
	// TODO Auto-generated method stub
	
}


}
