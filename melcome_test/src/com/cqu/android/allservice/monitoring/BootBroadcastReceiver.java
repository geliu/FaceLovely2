package com.cqu.android.allservice.monitoring;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.cqu.android.allservice.setup.Parameter;

public class BootBroadcastReceiver extends BroadcastReceiver {
	static final String ACTION = "android.intent.action.BOOT_COMPLETED";

	@Override
	public void onReceive(Context context, Intent intent) {
		Parameter pa = new Parameter(context);
		if ((intent.getAction().equals(ACTION))
				|| this.isStart(pa.getParameter("mCount"))) {
			Intent sayHelloIntent = new Intent(context, MonitoringService.class);
			context.startService(sayHelloIntent);
		}
	}

	public boolean isStart(String s) {
		if (Integer.parseInt(s) == 1) {
			return true;
		} else {
			return false;
		}
	}

}
