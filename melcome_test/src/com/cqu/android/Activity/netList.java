package com.cqu.android.Activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGestureListener;
import android.net.TrafficStats;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cqu.android.allservice.setup.Contact;
import com.cqu.android.bean.FireWall;
import com.cqu.android.bean.Programme;
import com.cqu.android.bean.onGestureListener;


public class netList extends Activity implements OnTouchListener, OnGestureListener{

	//申明GestureDetector对象
	GestureDetector mGestureDetector;
    //Use ArrayList to store the installed non-system apps
    ArrayList<Programme> appList = new ArrayList<Programme>(); 
    //ListView app_listView;
	/** Called when the activity is first created. */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.netlist);
        
		mGestureDetector = new GestureDetector(this,new onGestureListener(netList.this));

	    ListView viewSnsLayout = (ListView)findViewById(R.id.listview);  
	    viewSnsLayout.setOnTouchListener(this);  
	    viewSnsLayout.setLongClickable(true);
        
         getList();
        ListView app_listView=(ListView)findViewById(R.id.listview);
        AppAdapter appAdapter=new AppAdapter(netList.this,appList);
        app_listView.setAdapter(appAdapter);
        app_listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
			public void onItemClick(AdapterView<?> arg0, View arg1,  int arg2,
					long arg3) {
				// TODO Auto-generated method stu
				 final List<Integer> list=new ArrayList<Integer>();
				int uid=appList.get(arg2+1).getUid();
				list.add(uid);
				
				new AlertDialog.Builder(netList.this).setTitle("联网终止")
				.setMessage("是否终止该程序联网?").setIcon(
						android.R.drawable.ic_dialog_info)
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which ) {
										// TODO Auto-generated method stub
									
										String result="";
										if(FireWall.hasRootAccess(netList.this, false)){
											result=FireWall.applyIptablesRulesImpl(netList.this, list,false);
									//DisplayToast("---------> "+Integer.toString(arg2+1)+"项");
										
									}

								}
						}).setNegativeButton("取消", null).show();		
			
			}
        });
  
        	
	}
	public  void getList(){
    	//获取已安装的所有应用程序包
        List<ApplicationInfo> applications = getPackageManager().getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES); 
          //获取应用程序信息
          for(int i=0;i<applications.size();i++) { 
          ApplicationInfo application = applications.get(i); 
       
          ////////
          int uid = application.uid;    
       // 过滤系统的应用和电话应用，当然你也可以把它注释掉。
          if (application.processName.equals("system")
  				|| application.processName.equals("com.android.phone")) {
  		     continue;
       		}
          if(monitoringEachApplicationSend( uid)==0&&monitoringEachApplicationReceive(uid)==0){
        	  continue;
          }
          Programme Programme = new Programme(); 
        //  appInfo.appName = application.loadLabel(getPackageManager()).toString(); 
          //应用程序名字
          Programme.setName( application.packageName); 
          Programme.setUid(uid);
          //获取应用程序图标
          Programme.setIcon(application.loadIcon(getPackageManager()));
       // 查询某个Uid的下行值
         
          /////////////////////////////
          Programme.setReceive(monitoringEachApplicationReceive(uid));
          Programme.setSend(monitoringEachApplicationSend( uid)) ;
          appList.add(Programme);
         }
        
          
    }
    public class AppAdapter extends BaseAdapter {
    	
    	Context context;
    	ArrayList<Programme> dataList=new ArrayList<Programme>();
    	public AppAdapter(Context context,ArrayList<Programme> inputDataList)
    	{
    		this.context=context;
    		dataList.clear();
    		for(int i=0;i<inputDataList.size();i++)
    		{
    			dataList.add(inputDataList.get(i));
    		}
    	}
	
		public int getCount() {
			// TODO Auto-generated method stub
			return dataList.size();
		}

		
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return dataList.get(position);
		}

	
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}


		public View getView(int position, View convertView, ViewGroup parent) {
			
			View v=convertView;
			final Programme appUnit=dataList.get(position);
			if(v==null)
			{
        		LayoutInflater vi=(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v=vi.inflate(R.layout.app_row, null);
			}
			
        	TextView appName=(TextView)v.findViewById(R.id.appName);
        	TextView appSend=(TextView)v.findViewById(R.id.app_send);
        	TextView appRecieve=(TextView)v.findViewById(R.id.app_recieve);
        	TextView appTotal=(TextView)v.findViewById(R.id.app_taoal);
        	ImageView appIcon=(ImageView)v.findViewById(R.id.icon);
        	if(appName!=null)
        		appName.setText(appUnit.getName());
        	if(appIcon!=null)
        		appIcon.setImageDrawable(appUnit.getIcon());
        	if(appSend!=null)
        		appSend.setText(String.valueOf(appUnit.getSend()));
        	if(appRecieve!=null)
        		appRecieve.setText(String.valueOf(appUnit.getReceive()));
        	if(appTotal!=null)
        		appTotal.setText(String.valueOf(appUnit.getReceive()+appUnit.getSend()));
			return v;
		}
    }
	public static long monitoringEachApplicationReceive(int uid) {
		long   receive=TrafficStats.getUidRxBytes(uid);
		if(receive==-1)receive=0;
	  return receive;
}

    public static long monitoringEachApplicationSend(int uid) {
	long   send=TrafficStats.getUidTxBytes(uid);
		if(send==-1)send=0;
	  return send;
}
  public void DisplayToast(String str){
	  Toast.makeText(this,str,Toast.LENGTH_SHORT).show();
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
		intent_return.setClass(netList.this, mainPage.class);
		startActivity(intent_return);
		netList.this.finish();
	}
	return true;
}
	
}




