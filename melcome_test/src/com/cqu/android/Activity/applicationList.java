package com.cqu.android.Activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.cqu.android.Activity.R;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class applicationList extends Activity{
	private static final String tag="Screen1";
	private ListView listview;
	private Context mContext;
	private List<ResolveInfo> list;
	private AppAdapter adapter;
	private PackageManager pm;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//给Activity注册界面进度条功能
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.alist);
		mContext = this;
		
		list=new ArrayList<ResolveInfo>();
		pm = mContext.getPackageManager();
		listview = (ListView)findViewById(R.id.listview);
		
		adapter = new AppAdapter(mContext);
		
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
//				// TODO Auto-generated method stub
			}
		});
		new MyTask().execute();
		
	}


	class AppAdapter extends BaseAdapter{
		Context context;
		AppAdapter(Context context){
			this.context=context;
		}
		public int getCount() {
			return list.size();
		}

		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return list.get(position);
		}

		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			if(convertView==null){
				LayoutInflater inflater=getLayoutInflater().from(context);
				convertView=inflater.inflate(R.layout.simple_item_2, null);
			}
			ImageView iv=(ImageView)convertView.findViewById(R.id.icon);
			TextView tv=(TextView)convertView.findViewById(R.id.p_title);
			TextView send=(TextView)convertView.findViewById(R.id.p_send);
			ResolveInfo info=list.get(position);
			iv.setBackgroundDrawable(info.activityInfo.loadIcon(pm));
			tv.setText(info.activityInfo.loadLabel(pm));
			send.setText("5M");
			return convertView;
		}
		
	}
	
	class MyTask extends AsyncTask<String, Integer, String>{

		@Override
		protected void onPreExecute() {
			setProgressBarIndeterminateVisibility(true);
			showProgress();
		}

		@Override
		protected void onPostExecute(String param) {
			setProgressBarIndeterminateVisibility(false);
			adapter.notifyDataSetChanged();
			closeProgress();
		}

		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			super.onCancelled();
		}

		@Override
		protected void onProgressUpdate(Integer...values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			// 获取已经安装程序列表
			Intent intent = new Intent(Intent.ACTION_MAIN, null);
			intent.addCategory(Intent.CATEGORY_LAUNCHER);
			list = pm.queryIntentActivities(intent, 0);
	        Collections.sort(list, new ResolveInfo.DisplayNameComparator(pm));
			return null;
		}
		
	}
	
	private Dialog 		dialog;
    protected void showProgress() {
		if(dialog == null) {
			dialog =  new Dialog(this, R.layout.dialog0);
//			dialog.setContentView(R.layout.progress_dialog);
			dialog.setContentView(new ProgressBar(this));
			dialog.setCancelable(true);
			dialog.show();
		}
	}
    //
    protected void closeProgress() {
		
		if(dialog != null) {
			dialog.cancel();
			dialog = null;
		}
	}
    public boolean isShowing(){
    	if(dialog != null) {
    		return dialog.isShowing();
    	}
    	return false;
    }
    
}