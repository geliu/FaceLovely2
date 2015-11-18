package com.cqu.android.allservice.chart;

import java.util.ArrayList;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.renderer.DefaultRenderer;

import com.cqu.android.db.DatabaseAdapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

public class WeekChart extends AbstractDemoChart {
	DatabaseAdapter db;

	public String getName() {
		// TODO Auto-generated method stub
		return "每周流量统计";
	}


	public String getDesc() {
		// TODO Auto-generated method stub
		return "统计每周的总流量，并以环状图表示";
	}

	public Intent execute(Context context) {
		// TODO Auto-generated method stub
		
		db = new DatabaseAdapter(context);
		db.open();

	    List<double[]> values = new ArrayList<double[]>();
	    values.add(new double[] { 12, 14, 11, 10});
	   // values.add(new double[] { 10, 9, 14, 20, 11 });
	    List<String[]> titles = new ArrayList<String[]>();
	    titles.add(new String[] { "第一周", "第二周", "第三周", "第四周"});
	  //  titles.add(new String[] { "Project1", "Project2", "Project3", "Project4", "Project5" });
	    int[] colors = new int[] { Color.CYAN, Color.GREEN, Color.MAGENTA, Color.YELLOW };
	    DefaultRenderer renderer = buildCategoryRenderer(colors);
	    renderer.setApplyBackgroundColor(true);
	    renderer.setBackgroundColor(Color.rgb(47, 132, 187));
	    renderer.setLabelsColor(Color.WHITE);
	    return ChartFactory.getDoughnutChartIntent(context, buildMultipleCategoryDataset(
	        "周流量", titles, values), renderer, "每周流量统计");
	}
}