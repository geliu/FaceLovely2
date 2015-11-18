package com.cqu.android.Activity;

import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGestureListener;
import android.os.Bundle;
import android.os.Handler;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cqu.android.allservice.monitoring.TrafficMonitoring;
import com.cqu.android.bean.onGestureListener;
import com.cqu.android.db.DatabaseAdapter;

public class keepInTime extends Activity implements OnTouchListener, OnGestureListener{

	//申明GestureDetector对象
	GestureDetector mGestureDetector;
	private TextView textview = null ;
	private TextView GPRS = null ;
	private TextView WIFI = null ; 
	
	private TextView jintians_3g = null ;
	private TextView jintianx_3g = null ;
	private TextView jintianz_3g =null ;
	private TextView benzhous_3g = null ;
	private TextView benzhoux_3g = null ;
	private TextView benzhouz_3g = null ;
	private TextView benyues_3g = null ;
	private TextView benyuex_3g = null ;
	private TextView benyuez_3g = null ;
	
	private TextView jintians_wifi = null ;
	private TextView jintianx_wifi = null ;
	private TextView jintianz_wifi =null ;
	private TextView benzhous_wifi = null ;
	private TextView benzhoux_wifi = null ;
	private TextView benzhouz_wifi = null ;
	private TextView benyues_wifi = null ;
	private TextView benyuex_wifi = null ;
	private TextView benyuez_wifi = null ;
	
	DatabaseAdapter dbAdapter;
	Calendar currentCa ;
	 private Handler handler = new Handler();
	    private Runnable runnable = new Runnable() {
	        public void run() {
	            this.update();
	            handler.postDelayed(this, 1000 * 3);// 间隔120秒   

	        }
	        void update() {
	        	jintians_3g =(TextView)findViewById(R.id.Gjts);
	   		 jintianx_3g =(TextView)findViewById(R.id.Gjtx);
	   		 jintianz_3g =(TextView)findViewById(R.id.Gjtz);
	   		 benzhous_3g =(TextView)findViewById(R.id.Gbzs);
	   		 benzhoux_3g =(TextView)findViewById(R.id.Gbzx);
	   		 benzhouz_3g =(TextView)findViewById(R.id.Gbzz);
	   		 benyues_3g =(TextView)findViewById(R.id.Gbys);
	   		 benyuex_3g =(TextView)findViewById(R.id.Gbyx);
	   		 benyuez_3g =(TextView)findViewById(R.id.Gbyz);
	   		 
	   		 jintians_wifi =(TextView)findViewById(R.id.wifi_jts);
	   		 jintianx_wifi =(TextView)findViewById(R.id.wifi_jtx);
	   		 jintianz_wifi =(TextView)findViewById(R.id.wifi_jtz);
	   		 benzhous_wifi =(TextView)findViewById(R.id.wifi_bzs);
	   		 benzhoux_wifi =(TextView)findViewById(R.id.wifi_bzx);
	   		 benzhouz_wifi =(TextView)findViewById(R.id.wifi_bzz);
	   		 benyues_wifi =(TextView)findViewById(R.id.wifi_bys);
	   		 benyuex_wifi =(TextView)findViewById(R.id.wifi_byx);
	   		 benyuez_wifi =(TextView)findViewById(R.id.wifi_byz);
	   		 String gjts = "123" ;
	   		 currentCa =  Calendar.getInstance();
	   			int year = currentCa.get(Calendar.YEAR);
	   			int month = currentCa.get(Calendar.MONTH)+1;
	   			int day = currentCa.get(Calendar.DATE);
	   		 Date date = new Date() ;	
	   		 
	   			long dup = dbAdapter.calculateUp(year, month, day, 1);
	   			long ddw = dbAdapter.calculateDw(year, month, day, 1);
	   		 gjts =TrafficMonitoring.convertTraffic(dup);
	   		 jintians_3g.setText(gjts);
	   		 String gjtx = TrafficMonitoring.convertTraffic(ddw);
	   		 jintianx_3g.setText(gjtx);
	   		 String gjtz = TrafficMonitoring.convertTraffic(dup+ddw);
	   		 jintianz_3g.setText(gjtz);
	   		 
	   		 long wup = dbAdapter.weekUpFloew(1);
	   		 long wdw = dbAdapter.weekDownFloew(1);
	   		 
	   		 String gnzs = TrafficMonitoring.convertTraffic(wup);
	   		 benzhous_3g.setText(gnzs);
	   		 String  gbzx = TrafficMonitoring.convertTraffic(wdw);
	   		 benzhoux_3g.setText(gbzx);
	   		 String gbzz =TrafficMonitoring.convertTraffic(wup+wdw);
	   		 benzhouz_3g.setText(gbzz);
	   		 String gbys =TrafficMonitoring.convertTraffic(dbAdapter.calculateUpForMonth(year, month, 1));
	   		 benyues_3g.setText(gbys);
	   		 String gbyx =TrafficMonitoring.convertTraffic(dbAdapter.calculateDnForMonth(year, month, 1));
	   		 benyuex_3g.setText(gbyx);
	   		 String gbyz =TrafficMonitoring.convertTraffic(dbAdapter.calculateForMonth(year, month, 1));
	   		 benyuez_3g.setText(gbyz);
	   		 
	   		 long dup_wifi = dbAdapter.calculateUp(year, month, day, 0);
	   		 long ddw_wifi = dbAdapter.calculateDw(year, month, day, 0);
	   		 
	   		 String wjts =TrafficMonitoring.convertTraffic(dup_wifi);
	   		 jintians_wifi.setText(wjts);
	   		 String wjtx = TrafficMonitoring.convertTraffic(ddw_wifi);
	   		 jintianx_wifi.setText(wjtx);
	   		 String wjtz = TrafficMonitoring.convertTraffic(dup_wifi+ddw_wifi);
	   		 jintianz_wifi.setText(wjtz);
	   		 
	   		 long wup_wifi = dbAdapter.weekUpFloew(0);
	   		 long wdw_wifi = dbAdapter.weekDownFloew(0);
	   		 
	   		 String wnzs = TrafficMonitoring.convertTraffic(wup_wifi);
	   		 benzhous_wifi.setText(wnzs);
	   		 String  wbzx = TrafficMonitoring.convertTraffic(wdw_wifi);
	   		 benzhoux_wifi.setText(wbzx);
	   		 String wbzz =TrafficMonitoring.convertTraffic(wup_wifi+wdw_wifi);
	   		 benzhouz_wifi.setText(wbzz);
	   		 
	   		 String wbys =TrafficMonitoring.convertTraffic(dbAdapter.calculateUpForMonth(year, month, 0));
	   		 benyues_wifi.setText(wbys);
	   		 String wbyx =TrafficMonitoring.convertTraffic(dbAdapter.calculateDnForMonth(year, month, 0));
	   		 benyuex_wifi.setText(wbyx);
	   		 String wbyz =TrafficMonitoring.convertTraffic(dbAdapter.calculateForMonth(year, month, 0));
	   		 benyuez_wifi.setText(wbyz);
	        }};
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.intime);
		
		mGestureDetector = new GestureDetector(this,new onGestureListener(keepInTime.this));

	    LinearLayout viewSnsLayout = (LinearLayout)findViewById(R.id.intime);  
	    viewSnsLayout.setOnTouchListener(this);  
	    viewSnsLayout.setLongClickable(true);
	    
		dbAdapter = new DatabaseAdapter(this);
        dbAdapter.open();
		 textview = (TextView)findViewById(R.id.total);
		 GPRS = (TextView)findViewById(R.id.gprs);
		 WIFI = (TextView)findViewById(R.id.wifi);
		 textview.setText("流量实时监控");
		 GPRS.setText(R.string.gprs);
		 WIFI.setText(R.string.wifi);

		 handler.postDelayed(runnable, 1000 * 6);
	}
	 public void onResume(){
		 super.onResume();
		 jintians_3g =(TextView)findViewById(R.id.Gjts);
		 jintianx_3g =(TextView)findViewById(R.id.Gjtx);
		 jintianz_3g =(TextView)findViewById(R.id.Gjtz);
		 benzhous_3g =(TextView)findViewById(R.id.Gbzs);
		 benzhoux_3g =(TextView)findViewById(R.id.Gbzx);
		 benzhouz_3g =(TextView)findViewById(R.id.Gbzz);
		 benyues_3g =(TextView)findViewById(R.id.Gbys);
		 benyuex_3g =(TextView)findViewById(R.id.Gbyx);
		 benyuez_3g =(TextView)findViewById(R.id.Gbyz);
		 
		 jintians_wifi =(TextView)findViewById(R.id.wifi_jts);
		 jintianx_wifi =(TextView)findViewById(R.id.wifi_jtx);
		 jintianz_wifi =(TextView)findViewById(R.id.wifi_jtz);
		 benzhous_wifi =(TextView)findViewById(R.id.wifi_bzs);
		 benzhoux_wifi =(TextView)findViewById(R.id.wifi_bzx);
		 benzhouz_wifi =(TextView)findViewById(R.id.wifi_bzz);
		 benyues_wifi =(TextView)findViewById(R.id.wifi_bys);
		 benyuex_wifi =(TextView)findViewById(R.id.wifi_byx);
		 benyuez_wifi =(TextView)findViewById(R.id.wifi_byz);
		 String gjts = "123" ;
		 currentCa =  Calendar.getInstance();
			int year = currentCa.get(Calendar.YEAR);
			int month = currentCa.get(Calendar.MONTH)+1;
			int day = currentCa.get(Calendar.DATE);
		 Date date = new Date() ;	
		 
			long dup = dbAdapter.calculateUp(year, month, day, 1);
			long ddw = dbAdapter.calculateDw(year, month, day, 1);
		 gjts =TrafficMonitoring.convertTraffic(dup);
		 jintians_3g.setText(gjts);
		 String gjtx = TrafficMonitoring.convertTraffic(ddw);
		 jintianx_3g.setText(gjtx);
		 String gjtz = TrafficMonitoring.convertTraffic(dup+ddw);
		 jintianz_3g.setText(gjtz);
		 
		 long wup = dbAdapter.weekUpFloew(1);
		 long wdw = dbAdapter.weekDownFloew(1);
		 
		 String gnzs = TrafficMonitoring.convertTraffic(wup);
		 benzhous_3g.setText(gnzs);
		 String  gbzx = TrafficMonitoring.convertTraffic(wdw);
		 benzhoux_3g.setText(gbzx);
		 String gbzz =TrafficMonitoring.convertTraffic(wup+wdw);
		 benzhouz_3g.setText(gbzz);
		 String gbys =TrafficMonitoring.convertTraffic(dbAdapter.calculateUpForMonth(year, month, 1));
		 benyues_3g.setText(gbys);
		 String gbyx =TrafficMonitoring.convertTraffic(dbAdapter.calculateDnForMonth(year, month, 1));
		 benyuex_3g.setText(gbyx);
		 String gbyz =TrafficMonitoring.convertTraffic(dbAdapter.calculateForMonth(year, month, 1));
		 benyuez_3g.setText(gbyz);
		 
		 long dup_wifi = dbAdapter.calculateUp(year, month, day, 0);
		 long ddw_wifi = dbAdapter.calculateDw(year, month, day, 0);
		 
		 String wjts =TrafficMonitoring.convertTraffic(dup_wifi);
		 jintians_wifi.setText(wjts);
		 String wjtx = TrafficMonitoring.convertTraffic(ddw_wifi);
		 jintianx_wifi.setText(wjtx);
		 String wjtz = TrafficMonitoring.convertTraffic(dup_wifi+ddw_wifi);
		 jintianz_wifi.setText(wjtz);
		 
		 long wup_wifi = dbAdapter.weekUpFloew(0);
		 long wdw_wifi = dbAdapter.weekDownFloew(0);
		 
		 String wnzs = TrafficMonitoring.convertTraffic(wup_wifi);
		 benzhous_wifi.setText(wnzs);
		 String  wbzx = TrafficMonitoring.convertTraffic(wdw_wifi);
		 benzhoux_wifi.setText(wbzx);
		 String wbzz =TrafficMonitoring.convertTraffic(wup_wifi+wdw_wifi);
		 benzhouz_wifi.setText(wbzz);
		 
		 String wbys =TrafficMonitoring.convertTraffic(dbAdapter.calculateUpForMonth(year, month, 0));
		 benyues_wifi.setText(wbys);
		 String wbyx =TrafficMonitoring.convertTraffic(dbAdapter.calculateDnForMonth(year, month, 0));
		 benyuex_wifi.setText(wbyx);
		 String wbyz =TrafficMonitoring.convertTraffic(dbAdapter.calculateForMonth(year, month, 0));
		 benyuez_wifi.setText(wbyz);
		 
		 
		 
		 
		
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
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		// 按下键盘上返回按钮

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent_return = new Intent();
			intent_return.setClass(keepInTime.this, mainPage.class);
			startActivity(intent_return);
			keepInTime.this.finish();
		}
		return true;
	}
 
		

}
