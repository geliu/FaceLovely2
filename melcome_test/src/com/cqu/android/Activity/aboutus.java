package com.cqu.android.Activity;

import android.app.Activity;
import android.content.Intent;
import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGestureListener;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;

import com.cqu.android.bean.onGestureListener;

public class aboutus extends Activity implements OnTouchListener, OnGestureListener{

	//申明GestureDetector对象
	GestureDetector mGestureDetector;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about_us);
		setTitle("关于我们");
		
		mGestureDetector = new GestureDetector(this,new onGestureListener(aboutus.this));

	    LinearLayout viewSnsLayout = (LinearLayout)findViewById(R.id.aboutus);  
	    viewSnsLayout.setOnTouchListener(this);  
	    viewSnsLayout.setLongClickable(true);
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		menu.add(0, 1, 1, "详细");
		menu.add(0, 2, 2, "返回");
		return super.onCreateOptionsMenu(menu);
	}
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if(item.getItemId()==2){
		  Intent intent = new Intent(aboutus.this, mainPage.class);
		  aboutus.this.startActivity(intent);
		}
		return super.onOptionsItemSelected(item);
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
		intent_return.setClass(aboutus.this, mainPage.class);
		startActivity(intent_return);
		aboutus.this.finish();
	}
	return true;
}

	
	

}
