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
	int threadNum; // �߳���
	static int count = 12;
	NetworkInfo nwi;
	
	public IBinder onBind(Intent intent) {
		return null;
	}
	//ArrayList<AppInfo> appList = new ArrayList<AppInfo>(); 

	public void onCreate() {
	
		old_mobileRx = TrafficStats.getMobileRxBytes();
		old_mobileTx = TrafficStats.getMobileTxBytes();

		// ��ȡȫ��������ա�������������
		totalRx = TrafficStats.getTotalRxBytes();
		totalTx = TrafficStats.getTotalTxBytes();
		
		// ����WiFi������ա�������������
		old_wifiRx = totalRx - old_mobileRx;
		old_wifiTx = totalTx - old_mobileTx;	
		
		handler.post(thread);//���Ӧ���ǿ�������̵߳�Ŷ��
		
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
				
			// ������������
			// ��ȡ�ƶ�������ա�����������������λΪbyte������ͬ��
			mobileRx = TrafficStats.getMobileRxBytes();
			mobileTx = TrafficStats.getMobileTxBytes();

			// ��ȡȫ��������ա�������������
			totalRx = TrafficStats.getTotalRxBytes();
			totalTx = TrafficStats.getTotalTxBytes();
			
			// ����WiFi������ա�������������
			wifiRx = totalRx - mobileRx;
			wifiTx = totalTx - mobileTx;
			
			if (mobileRx == -1 && mobileTx == -1) {
				in.putExtra("mobileRx", "No");
				in.putExtra("mobileTx", "No");
			}
			 else {
					mrx = (mobileRx - old_mobileRx); // �õ�˲ʱGPRS����
					old_mobileRx = mobileRx;
					mtx = (mobileTx - old_mobileTx) ; // �õ�˲ʱGPRS����
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
				wrx = (wifiRx - old_wifiRx); // �õ�˲ʱwifi����
				old_wifiRx = wifiRx;
				wtx = (wifiTx - old_wifiTx); // �õ�˲ʱwifi����
				old_wifiTx = wifiTx;
				wrx = (long) ((float) (Math.round(wrx * 100.0)) / 100);// ������λС��
				wtx = (long) ((float) (Math.round(wtx * 100.0)) / 100);
				in.putExtra("wifiRx", wrx / 1024 + "KB");
				in.putExtra("wifiTx", wtx + "KB");
			}
			Date date = new Date() ;
			mobileRx_all += mrx; // ��ͬһ�������֮��
			mobileTx_all += mtx; // ��ͬһ�������֮��
			wifiTx_all += wtx; // ��ͬһ�������֮��
			wifiRx_all += wrx; // ��ͬһ�������֮��
			if(count==12){
				
			//	try{
				//������ڸ���GPRS�����ļ�¼����±�����¼
				if(mobileTx_all!=0||mobileRx_all!=0){
					Cursor checkMobile = dbAdapter.check(1, date);//1 Ϊ GPRS��������
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
				//������ݿ���û�н����GPRS���������һ����Ϣ
//				}catch(Exception e){
//					e.printStackTrace();
//				}finally{
//					checkMobile.close();
//				}
				
//				Cursor checkWifi = dbAdapter.check(0, date);//0Ϊ wifi��������
//				long up = dbAdapter.getProFlowUp(0, date);
//				long dw = dbAdapter.getProFlowDw(0, date);
//				long all = up+ dw ;
				//try{
				
				if(wifiTx_all!=0 ||wifiRx_all!=0){
					Cursor checkWifi = dbAdapter.check(0, date);//0Ϊ wifi��������
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




