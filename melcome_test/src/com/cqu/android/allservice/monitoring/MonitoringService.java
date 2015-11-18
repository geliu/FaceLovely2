package com.cqu.android.allservice.monitoring;



import java.util.Date;


import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.net.NetworkInfo;
import android.net.TrafficStats;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.cqu.android.Activity.mainPage;
import com.cqu.android.db.DatabaseAdapter;

public class MonitoringService extends Service {

	private DatabaseAdapter dbAdapter;
	private Handler handler = new Handler() ;
	private long mobileRx = 0 , mobileTx = 0 ,totalRx = 0 , totalTx = 0 ,wifiRx = 0 ,wifiTx = 0;
	private long old_mobileRx = 0 ,old_mobileTx = 0  ,old_wifiRx = 0, old_wifiTx= 0 ;
	private long mrx = 0,mtx = 0 , wrx = 0 ,wtx = 0 ;
	private long mobileRx_all= 0 ,mobileTx_all= 0 ,wifiRx_all = 0,wifiTx_all = 0 ;
	private Intent in = new Intent("Runnable");
	int threadNum; // 线程数
	static int count = 12;
	NetworkInfo nwi;
	
	public IBinder onBind(Intent intent) {
		return null;
	}
	//ArrayList<AppInfo> appList = new ArrayList<AppInfo>(); 

	public void onCreate() {
	
		old_mobileRx = TrafficStats.getMobileRxBytes();
		old_mobileTx = TrafficStats.getMobileTxBytes();

		// 获取全部网络接收、发送数据总量
		totalRx = TrafficStats.getTotalRxBytes();
		totalTx = TrafficStats.getTotalTxBytes();
		
		// 计算WiFi网络接收、发送数据总量
		old_wifiRx = totalRx - old_mobileRx;
		old_wifiTx = totalTx - old_mobileTx;	
		
		handler.post(thread);//这个应该是开启这个线程的哦？
		
		super.onCreate();
	        
	}
	
	
	
    Runnable thread = new Runnable(){

		public void run() {
			// TODO Auto-generated method stub
			
			dbAdapter = new DatabaseAdapter(MonitoringService.this);
			dbAdapter.open();
			
//			 try {
//				Thread.sleep(5000);
//			} catch (InterruptedException e) {}
				
			// 截至启动机器
			// 获取移动网络接收、发送数据总量，单位为byte，以下同上
			mobileRx = TrafficStats.getMobileRxBytes();
			mobileTx = TrafficStats.getMobileTxBytes();

			// 获取全部网络接收、发送数据总量
			totalRx = TrafficStats.getTotalRxBytes();
			totalTx = TrafficStats.getTotalTxBytes();
			
			// 计算WiFi网络接收、发送数据总量
			wifiRx = totalRx - mobileRx;
			wifiTx = totalTx - mobileTx;
			
			if (mobileRx == -1 && mobileTx == -1) {
				in.putExtra("mobileRx", "No");
				in.putExtra("mobileTx", "No");
			}
			 else {
					mrx = (mobileRx - old_mobileRx); // 得到瞬时GPRS流量
					old_mobileRx = mobileRx;
					mtx = (mobileTx - old_mobileTx) ; // 得到瞬时GPRS流量
					old_mobileTx = mobileTx;

					mrx = (long) ((float) (Math.round(mrx * 100.0)) / 100);
					mtx = (long) ((float) (Math.round(mtx * 100.0)) / 100);

					in.putExtra("mobileRx", mrx / 1024 + "KB");
					in.putExtra("mobileTx", mtx/ 1024 + "KB");
				}
			if (wifiRx == -1 && wifiTx == -1) {
				in.putExtra("wifiRx", "No");
				in.putExtra("wifiTx", "No");
			} else {
				wrx = (wifiRx - old_wifiRx); // 得到瞬时wifi流量
				old_wifiRx = wifiRx;
				wtx = (wifiTx - old_wifiTx); // 得到瞬时wifi流量
				old_wifiTx = wifiTx;
				wrx = (long) ((float) (Math.round(wrx * 100.0)) / 100);// 保留两位小数
				wtx = (long) ((float) (Math.round(wtx * 100.0)) / 100);
				in.putExtra("wifiRx", wrx / 1024 + "KB");
				in.putExtra("wifiTx", wtx + "KB");
			}
			Date date = new Date() ;
			mobileRx_all += mrx; // 求同一天的数据之和
			mobileTx_all += mtx; // 求同一天的数据之和
			wifiTx_all += wtx; // 求同一天的数据之和
			wifiRx_all += wrx; // 求同一天的数据之和
			if(count==12){
				
			//	try{
				//如果存在该天GPRS流量的记录则跟新本条记录
				if(mobileTx_all!=0||mobileRx_all!=0){
					Cursor checkMobile = dbAdapter.check(1, date);//1 为 GPRS流量类型
				  if(checkMobile.moveToNext()){
					long up = dbAdapter.getProFlowUp(1, date);
					long dw = dbAdapter.getProFlowDw(1, date);
					mobileTx_all += up ;
					mobileRx_all += dw ;
					dbAdapter.updateData(mobileTx_all, mobileRx_all, 1, date);
					System.out.println("geng xin le GPRS liu liang sahngxing"+mobileTx_all+"xia xing"+mobileRx_all);
					mobileTx_all=0;
					mobileRx_all=0;
					
					}
				  if(!checkMobile.moveToNext()){
						
						dbAdapter.insertData(mobileTx_all, mobileRx_all, 1, date);	
						System.out.println("cun chu le GPRS liu liang sahngxing"+mobileTx_all+"xia xing"+mobileRx_all);
						
					}
				  
				}
				//如果数据库中没有今天的GPRS流量则插入一条信息
//				}catch(Exception e){
//					e.printStackTrace();
//				}finally{
//					checkMobile.close();
//				}
				
//				Cursor checkWifi = dbAdapter.check(0, date);//0为 wifi流量类型
//				long up = dbAdapter.getProFlowUp(0, date);
//				long dw = dbAdapter.getProFlowDw(0, date);
//				long all = up+ dw ;
				//try{
				
				if(wifiTx_all!=0 ||wifiRx_all!=0){
					Cursor checkWifi = dbAdapter.check(0, date);//0为 wifi流量类型
					long up = dbAdapter.getProFlowUp(0, date);
					long dw = dbAdapter.getProFlowDw(0, date);
					if(checkWifi.moveToNext()){
					wifiTx_all += up ;
					wifiRx_all += dw ;
					dbAdapter.updateData(wifiTx_all, wifiRx_all, 0, date);
					System.out.println("geng xin le WIFI liu liang sahngxing"+wifiTx_all+"xia xing"+wifiRx_all);
					wifiTx_all = 0 ;
					wifiRx_all = 0 ;
					
					}
					else{
						
						dbAdapter.insertData(wifiTx_all, wifiRx_all, 0, date);
						System.out.println("cun chu le WIFI liu liang sahngxing"+wifiTx_all+"xia xing"+wifiRx_all);
						
					}
				}
				
//				}catch(Exception e){
//					e.printStackTrace();
//				}finally{
//					checkWifi.close();
//				}
				count = 1 ;
			}
			count++;
			dbAdapter.close();
			handler.postDelayed(thread, 500);
//			Intent intent = new Intent();
//			intent.setClass(MonitoringService.this, mainPage.class);
//			MonitoringService.this.startActivity(intent);
		}
    	
    };
    
    public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		handler.post(thread);
		return super.onStartCommand(intent, flags, startId);
	}
	public static long monitoringEachApplicationReceive(int uid) {
		long   receive=TrafficStats.getUidRxBytes(uid);
		if(receive==-1)receive=0;
	  return receive;
}

    public static long monitoringEachApplicationSend(int uid) {
	long   send=TrafficStats.getUidRxBytes(uid);
		if(send==-1)send=0;
	  return send;
}
    public int getNetType() {
		if(nwi != null){
			String net = nwi.getTypeName();
			if(net.equals("WIFI")){
				return 0;
			}else {
				return 1;
			}
		}else {
			return -1;
		}
	}

	public void onDestroy() {
		super.onDestroy();
		handler.removeCallbacks(thread);
		Log.v("CountService", "on destroy");
	}
}




