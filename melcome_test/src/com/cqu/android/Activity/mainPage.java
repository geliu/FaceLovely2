package com.cqu.android.Activity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.gesture.GestureOverlayView;
import android.graphics.Color;
import android.net.TrafficStats;
import android.os.Bundle;
import android.os.Handler;
import android.view.GestureDetector;
import android.gesture.GestureOverlayView.OnGestureListener;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.ViewFlipper;
import android.widget.AdapterView.OnItemClickListener;

import com.cqu.android.allservice.monitoring.TrafficMonitoring;
import com.cqu.android.allservice.setup.Parameter;
import com.cqu.android.bean.onGestureListener;
import com.cqu.android.db.DatabaseAdapter;



public class mainPage extends Activity implements OnTouchListener, OnGestureListener {

	//申明GestureDetector对象
	GestureDetector mGestureDetector;
	
	private PopupWindow popup;
	private ListView listView;
	private GridView toolbarGrid, mGridView, mTitleGridView;
	private LinearLayout mLayout;
	private TextView title1, title2, title3;
	private int titleIndex;
	private ViewFlipper mViewFlipper;
	private TextView up,upRate,down,downRate;
	
	Calendar currentCa;
	DatabaseAdapter dbAdapter;

	private final int TOOLBAR_ITEM_KEEPINTIME = 0;
	private final int TOOLBAR_ITEM_NETLIST = 1;
	private final int TOOLBAR_ITEM_STATIST = 2;
	private final int TOOLBAR_ITEM_SETTING = 3;
	private final int TOOLBAR_ITEM_ABOUT = 4;
	long old_totalRx=0;
	long old_totalTx=0;
	long totalRx=0;
	long totalTx=0;
	

	/** 底部菜单图片 **/
	int[] menu_toolbar_image_array = { R.drawable.shishi2,R.drawable.menu_debug,
			R.drawable.tongji,
			R.drawable.shezhi, R.drawable.women,
			};
	/** 底部菜单文字 **/
	String[] menu_toolbar_name_array = { "实时监控","联网监控" ,"流量统计", "系统设置", "关于我们",};
    
	  private Handler handler = new Handler();
	    private Runnable runnable = new Runnable() {
	        public void run() {
	            this.update();
	            handler.postDelayed(this, 1000 * 3);// 间隔120秒   
	            old_totalRx = TrafficStats.getTotalRxBytes();
	            old_totalTx = TrafficStats.getTotalTxBytes();
	        }
	        void update() {
	            //刷新msg的内容
	        	currentCa =  Calendar.getInstance();
	    		int year = currentCa.get(Calendar.YEAR);
	    		int month = currentCa.get(Calendar.MONTH)+1;
	    		int day = currentCa.get(Calendar.DATE);
	    		
	    		TextView today3G = (TextView) findViewById(R.id.Today_3G);
	    		TextView todayWifi = (TextView) findViewById(R.id.Today_WIFI);
	    		TextView month3G = (TextView) findViewById(R.id.Month_3G);
	    		TextView monthWifi = (TextView) findViewById(R.id.Month_WIFI);
	    		TextView remain3G = (TextView) findViewById(R.id.Remain);
	    		TextView limit = (TextView) findViewById(R.id.limit);
	    		String month3GTraffic;
	    		String day3GTraffic;
	    		String dayWIFITraffic;
	    		String monthWIFITraffic;

	    		
	    		
	    	
	    		Date date = new Date();
	    		// 显示本月已用3G流量
	    		month3GTraffic = TrafficMonitoring.convertTraffic(dbAdapter.calculateForMonth(year, month, 1));
	    		month3G.setText(month3GTraffic);

	    		// 本日已用3G流量;从数据库中获取
	    		day3GTraffic = TrafficMonitoring.convertTraffic(dbAdapter.calculate(year, month, day, 1));
	    		today3G.setText(day3GTraffic);
	    		

	    		// 本日已用WIFI流量;从数据库中获取
	    		dayWIFITraffic = TrafficMonitoring.convertTraffic(dbAdapter.calculate(year, month, day, 0));
	    		todayWifi.setText(dayWIFITraffic);
	    		
	    		// 本月已用WIFI流量；从数据库中获取
	    		monthWIFITraffic =TrafficMonitoring.convertTraffic(dbAdapter.calculateForMonth(year, month, 0));
	    		monthWifi.setText(monthWIFITraffic);
	    		
	    		// 显示流量限额
	    		try{
	    		Parameter par = new Parameter(this);
	    		String parLimit = par.getParameter("mLimit");
	    		if(parLimit.equals("")){
	    			parLimit ="30";
	    			limit.setText("30MB");
	    		}else{
	    			limit.setText(parLimit+"MB");
	    		}

	    		// 剩余3G流量
	    		Parameter isWarn = new Parameter(this);
	    		String warn = isWarn.getParameter("mWarn");
	    		
	    		
	    			String tempString[];// 临时存储3G流量
	    			double iLimit;
	    			if(parLimit.equals("")){
	    				iLimit = 30.0;
	    			}else{
	    				iLimit = Double.valueOf(parLimit);
	    			}
	    			double remain;
	    			if (month3GTraffic.contains("KB")) {
	    				tempString = month3GTraffic.split("KB");
	    				double temp = Double.valueOf(tempString[0]);
	    				remain = new BigDecimal(iLimit * 1000 - temp).divide(new BigDecimal(1000),2,1).doubleValue();
	    			} else if (month3GTraffic.contains("MB")) {
	    				tempString = month3GTraffic.split("MB");
	    				double temp = Double.valueOf(tempString[0]);
	    				remain = iLimit - temp;
	    			} else {
	    				tempString = month3GTraffic.split("GB");
	    				double temp = Double.valueOf(tempString[0]);
	    				remain = new BigDecimal(iLimit * 1000 - temp).doubleValue();
	    			}
	    			
	    			double percent = new BigDecimal(remain).divide(new BigDecimal(iLimit),2,1).doubleValue();
	    			if(warn.equals("")||Integer.parseInt(warn) == 0){
	    				
	    				remain3G.setTextColor(Color.WHITE);
	    		
	    			}else  if(Integer.parseInt(warn) == 1&& percent < 0.2) {	
	    				remain3G.setTextColor(Color.RED);
	    			}
	    			remain3G.setText(remain + "MB");
	    		} catch (Exception ex) {
	    			System.out.println(" ");
	    		}
	    		totalRx = TrafficStats.getTotalRxBytes();
				totalTx = TrafficStats.getTotalTxBytes();
	    		long mrx = totalRx - old_totalRx;
	    		old_totalRx=totalRx;
	    		long mtx = totalTx - old_totalTx;
	    		old_totalTx=totalTx;
	    		upRate.setText(TrafficMonitoring.convertTraffic(mrx));
	    		downRate.setText(TrafficMonitoring.convertTraffic(mtx));
	        }
	        
	    }; 

	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		setTitle("手机流量监控系统");
		dbAdapter = new DatabaseAdapter(this);
        dbAdapter.open();
        
        mGestureDetector = new GestureDetector(this,new onGestureListener(mainPage.this));

	    LinearLayout viewSnsLayout = (LinearLayout)findViewById(R.id.mainmel);  
	    viewSnsLayout.setOnTouchListener(this);  
	    viewSnsLayout.setLongClickable(true);
        
        toolbarGrid = (GridView)findViewById(R.id.GridView_toolbar);
		toolbarGrid.setSelector(R.drawable.toolbar_menu_item);
		toolbarGrid.setBackgroundResource(R.drawable.menu_bg2);// 设置背景
		toolbarGrid.setNumColumns(5);// 设置每行列数
		toolbarGrid.setGravity(Gravity.CENTER);// 位置居中
		toolbarGrid.setVerticalSpacing(20);// 垂直间隔
		toolbarGrid.setHorizontalSpacing(7);// 水平间隔
		toolbarGrid.setAdapter(getMenuAdapter(menu_toolbar_name_array,
				menu_toolbar_image_array));// 设置菜单Adapter
		toolbarGrid.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				switch (arg2) {
				case TOOLBAR_ITEM_KEEPINTIME:
					Intent intent1 = new Intent();
					intent1.setClass(mainPage.this, keepInTime.class);
					mainPage.this.startActivity(intent1);

					break;
				case TOOLBAR_ITEM_NETLIST:
					Intent intent5 = new Intent();
					intent5.setClass(mainPage.this, netList.class);
					mainPage.this.startActivity(intent5);

					break;
				case TOOLBAR_ITEM_STATIST:
					Intent intent2 = new Intent();
					intent2.setClass(mainPage.this, statist.class);
					mainPage.this.startActivity(intent2);

					break;
				case TOOLBAR_ITEM_SETTING:
					Intent intent3 = new Intent();
					intent3.setClass(mainPage.this, setting.class);
					mainPage.this.startActivity(intent3);

					break;
				case TOOLBAR_ITEM_ABOUT:
					Intent intent4 = new Intent();
					intent4.setClass(mainPage.this, aboutus.class);
					mainPage.this.startActivity(intent4);

					break;

			
				}
			}
		});
		handler.postDelayed(runnable, 1000 * 6);
    }
    public void onResume(){
    	super.onResume();
		currentCa =  Calendar.getInstance();
		int year = currentCa.get(Calendar.YEAR);
		int month = currentCa.get(Calendar.MONTH)+1;
		int day = currentCa.get(Calendar.DATE);
		
		TextView today3G = (TextView) findViewById(R.id.Today_3G);
		TextView todayWifi = (TextView) findViewById(R.id.Today_WIFI);
		TextView month3G = (TextView) findViewById(R.id.Month_3G);
		TextView monthWifi = (TextView) findViewById(R.id.Month_WIFI);
		TextView remain3G = (TextView) findViewById(R.id.Remain);
		TextView limit = (TextView) findViewById(R.id.limit);
		up=(TextView)findViewById(R.id.up);
		down=(TextView)findViewById(R.id.down);
		upRate=(TextView)findViewById(R.id.upRate);
		downRate=(TextView)findViewById(R.id.downRate);
		String month3GTraffic;
		String day3GTraffic;
		String dayWIFITraffic;
		String monthWIFITraffic;

		// 显示流量限额
		
		Parameter par = new Parameter(this);
		String parLimit = par.getParameter("mLimit");
		if(parLimit.equals("")){
			parLimit ="30";
			limit.setText("30MB");
		}else{
			limit.setText(parLimit+"MB");
		}
	
		Date date = new Date();
		// 显示本月已用3G流量
		month3GTraffic = TrafficMonitoring.convertTraffic(dbAdapter.calculateForMonth(year, month, 1));
		month3G.setText(month3GTraffic);

		// 本日已用3G流量;从数据库中获取
		day3GTraffic = TrafficMonitoring.convertTraffic(dbAdapter.calculate(year, month, day, 1));
		today3G.setText(day3GTraffic);
		

		// 本日已用WIFI流量;从数据库中获取
		dayWIFITraffic = TrafficMonitoring.convertTraffic(dbAdapter.calculate(year, month, day, 0));
		todayWifi.setText(dayWIFITraffic);
		
		// 本月已用WIFI流量；从数据库中获取
		monthWIFITraffic =TrafficMonitoring.convertTraffic(dbAdapter.calculateForMonth(year, month, 0));
		monthWifi.setText(monthWIFITraffic);
		
		
		// 剩余3G流量
		Parameter isWarn = new Parameter(this);
		String warn = isWarn.getParameter("mWarn");
		
		try {
			String tempString[];// 临时存储3G流量
			double iLimit;
			if(parLimit.equals("")){
				iLimit = 30.0;
			}else{
				iLimit = Double.valueOf(parLimit);
			}
			double remain;
			if (month3GTraffic.contains("KB")) {
				tempString = month3GTraffic.split("KB");
				double temp = Double.valueOf(tempString[0]);
				remain = new BigDecimal(iLimit * 1000 - temp).divide(new BigDecimal(1000),2,1).doubleValue();
			} else if (month3GTraffic.contains("MB")) {
				tempString = month3GTraffic.split("MB");
				double temp = Double.valueOf(tempString[0]);
				remain = iLimit - temp;
			} else {
				tempString = month3GTraffic.split("GB");
				double temp = Double.valueOf(tempString[0]);
				remain = new BigDecimal(iLimit * 1000 - temp).doubleValue();
			}
			
			double percent = new BigDecimal(remain).divide(new BigDecimal(iLimit),2,1).doubleValue();
			if(warn.equals("")||Integer.parseInt(warn) == 0){
				
				remain3G.setTextColor(Color.WHITE);
		
			}else  if(Integer.parseInt(warn) == 1&& percent < 0.2) {	
				remain3G.setTextColor(Color.RED);
			}
			remain3G.setText(remain + "MB");
		} catch (Exception ex) {
			System.out.println(" ");
		}

    }


	
	 private SimpleAdapter getMenuAdapter(String[] menuNameArray,
				int[] imageResourceArray) {
			ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
			for (int i = 0; i < menuNameArray.length; i++) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("itemImage", imageResourceArray[i]);
				map.put("itemText", menuNameArray[i]);
				data.add(map);
			}
			SimpleAdapter simperAdapter = new SimpleAdapter(this, data,
					R.layout.item_menu, new String[] { "itemImage", "itemText" },
					new int[] { R.id.item_image, R.id.item_text });
			return simperAdapter;
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
