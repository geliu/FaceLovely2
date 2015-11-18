package com.cqu.android.Activity;


import com.cqu.android.Activity.R;
import com.cqu.android.allservice.monitoring.MonitoringService;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

public class liuliangjiankong extends Activity {
    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start);

        Intent intent = new Intent() ;
        intent.setClass(liuliangjiankong.this, MonitoringService.class);
        this.startService(intent);
        
        new Handler().postDelayed(new Runnable() {

			public void run() {
				// TODO Auto-generated method stub
				Intent intent = new  Intent(liuliangjiankong.this,mainPage.class);
				liuliangjiankong.this.startActivity(intent);
				liuliangjiankong.this.finish();
			}
			
			
		}, 3000);
        
    }
}