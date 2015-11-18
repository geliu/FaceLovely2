package com.cqu.android.allservice.chart;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.chart.PointStyle;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import com.cqu.android.db.DatabaseAdapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint.Align;

public class MonthChart extends AbstractDemoChart {


	public String getName() {
		// TODO Auto-generated method stub
		return "每月流量统计";
	}


	public String getDesc() {
		// TODO Auto-generated method stub
		return "统计每月的总流量，并以折线图显示";
	}

	public Intent execute(Context context) {
		// TODO Auto-generated method stub
		String[] titles = new String[] { "月流量" };
	    List<double[]> x = new ArrayList<double[]>();
	    for (int i = 0; i < titles.length; i++) {
	      x.add(new double[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 });
	    }
	    List<double[]> values = new ArrayList<double[]>();
	   // values.add(new double[] { 12.3, 12.5, 13.8, 16.8, 20.4, 24.4, 26.4, 26.1, 23.6, 20.3, 17.2, 13.9 });
	    DatabaseAdapter dbAdapter = new DatabaseAdapter(context);
	    dbAdapter.open();
	    Calendar calendar = Calendar.getInstance();
	    double[] month = new double[12];
	    for(int i = 0; i<=11;i++){
	    	long temp=dbAdapter.calculateForMonth(calendar.get(Calendar.YEAR),i+1, 1);
	    	if(temp!=0){
	    		month[i] = new BigDecimal(temp).divide(new BigDecimal(1000000),1,1).doubleValue();
	    	}else{
	    		month[i]=0;
	    	}
	    }
	    values.add(month);
	    dbAdapter.close();
	    int[] colors = new int[] {Color.RED};
	    PointStyle[] styles = new PointStyle[] { PointStyle.TRIANGLE};
	    XYMultipleSeriesRenderer renderer = buildRenderer(colors, styles);
	    int length = renderer.getSeriesRendererCount();
	    for (int i = 0; i < length; i++) {
	      ((XYSeriesRenderer) renderer.getSeriesRendererAt(i)).setFillPoints(true);
	    }
	    setChartSettings(renderer, "每月流量统计", "月份（月）", "流量（MB）", 0.5, 12.5, 0, 100,
	        Color.WHITE, Color.WHITE);
	    renderer.setBackgroundColor(Color.rgb(47, 132, 187));
	    renderer.setApplyBackgroundColor(true);
	    renderer.setMarginsColor(Color.rgb(47, 132, 187));
	    renderer.setXLabels(10);
	    renderer.setYLabels(10);
	    renderer.setXLabelsAlign(Align.RIGHT);
	    renderer.setYLabelsAlign(Align.RIGHT);
	    renderer.setPanLimits(new double[] { -10, 20, -10, 40 });
	    renderer.setZoomLimits(new double[] { -10, 20, -10, 40 });
	    Intent intent = ChartFactory.getLineChartIntent(context, buildDataset(titles, x, values),
	        renderer, "每月流量统计");
	    return intent;
	}

}
