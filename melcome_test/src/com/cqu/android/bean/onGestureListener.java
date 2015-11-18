package com.cqu.android.bean;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.cqu.android.Activity.*;
public class onGestureListener extends GestureDetector.SimpleOnGestureListener {
	      enum Current {c_mainPage,c_netList,c_statist,c_keepInTime,c_setting,c_aboutus}; 
	      private Activity currentActivity; 
	      private Intent leftIntent=new Intent();
	       private Intent rightIntent=new Intent();
	       Current current;//=Current.c_aboutAs;
	       public onGestureListener() {
				// TODO Auto-generated constructor stub
			}

	       public onGestureListener(Activity currentActivity){
	    	   this.currentActivity=currentActivity;
	    	   //获取当前Activity的name
	    	   String currentActivityName=this.currentActivity.getLocalClassName().toString();
	    	
	    	 //System.out.println("logA:"+ this.currentActivity.getLocalClassName().toString());
	    	//  System.out.println(this.currentActivity.getLocalClassName().toString().equals("mainPage"));
	    	 
	    	  if(currentActivityName.equals("mainPage")){
	    		   this.current=Current.c_mainPage;
	    		 //  System.out.println("logA1:　"+this.current.toString());
	    	   }
	    	   else if(currentActivityName.equals("netList")
	    			   ){
	    		   this.current=Current.c_netList;
	    		   System.out.println("log 0:　"+currentActivityName);
	    	   }else if(currentActivityName.equals("setting")){
	    		   this.current=Current.c_setting;		   
	    	   }   else if(currentActivityName.equals("keepInTime")){
	    		   this.current=Current.c_keepInTime;
	    	   }else if(currentActivityName.equals("aboutus")){
	    		   this.current=Current.c_aboutus;		   
	    	   }else if(currentActivityName.equals("statist")){
	    		   this.current=Current.c_statist;
	    	   }
	    	   setIntent();
	       }
	    	  
	    
			public void setIntent(){
	        	switch(current){
	        	case c_mainPage:
	        		leftIntent.setClass(currentActivity, mainPage.class);
	        		rightIntent.setClass(currentActivity, keepInTime.class);
	        		break;
	        	case c_keepInTime:
	        		leftIntent.setClass(currentActivity, mainPage.class);
	        		rightIntent.setClass(currentActivity, netList.class);
	        		break;
	        	case c_netList:
	        		
	        		leftIntent.setClass(currentActivity, mainPage.class);
	        		 System.out.println("log 1:　"+"leftIntent");
	        		rightIntent.setClass(currentActivity, statist.class);
	        		System.out.println("log 2:　"+"rightIntent");
	        		break;
	        		
	        	case c_statist:
	        		leftIntent.setClass(currentActivity, mainPage.class);
	        		rightIntent.setClass(currentActivity, setting.class);
	        		break;
	        	case c_setting:
	        		leftIntent.setClass(currentActivity, mainPage.class);
	        		rightIntent.setClass(currentActivity, aboutus.class);
	        		break;
	        	case c_aboutus:
	        		leftIntent.setClass(currentActivity, mainPage.class);
	        		rightIntent.setClass(currentActivity, aboutus.class);
	        		break;
	        	}
	        }

			 public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
						float velocityY) {
					if (e1.getX()-e2.getX() > SnsConstant.getFlingMinDistance()
							&& Math.abs(velocityX) >SnsConstant.getFlingMinVelocity()) {
					System.out.println("log 0:　"+" qi dong right");
						currentActivity.startActivity(rightIntent);
						 Log.d("DEBUG","zuo zuo shoushi");
					
					} else if (e2.getX()-e1.getX() > SnsConstant.getFlingMinDistance()
							&& Math.abs(velocityX) > SnsConstant.getFlingMinVelocity()) {
						//Intent intent=setIntent(startActivity,avtivityI);
						currentActivity.startActivity(leftIntent);
					}
					
					return false;  
				}
				// TODO Auto-generated method stub
			 public static class SnsConstant {
				    private static final int FLING_MIN_DISTANCE = 50;  
				    private static final int FLING_MIN_VELOCITY = 0;
				    
					public static int getFlingMinDistance() {
						return FLING_MIN_DISTANCE;
					}
					public static int getFlingMinVelocity() {
						return FLING_MIN_VELOCITY;
					}
				}
			 
		
 }


