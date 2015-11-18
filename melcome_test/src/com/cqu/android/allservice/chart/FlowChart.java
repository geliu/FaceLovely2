package com.cqu.android.allservice.chart;

import java.math.BigDecimal;
import java.util.Calendar;

import org.achartengine.ChartFactory;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.model.RangeCategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;

import com.cqu.android.db.DatabaseAdapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint.Align;

public class FlowChart extends AbstractDemoChart {


	public String getName() {
		// TODO Auto-generated method stub
		return "ÿ������ͳ��";
	}

	
	public String getDesc() {
		// TODO Auto-generated method stub
		return "ͳ��ÿ�յ���������������״ͼ��ʾ";
	}

	
	public Intent execute(Context context) {
		// TODO Auto-generated method stub
		double[] minValues = new double[] { 0, 0, 0, 0, 0, 0, 0};
	    //double[] maxValues = new double[] { 20, 10, 25, 13, 21, 19, 15};
		DatabaseAdapter db = new DatabaseAdapter(context);
		db.open();
		Calendar calendar = Calendar.getInstance();
		int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
	    double[] maxValues = new double[7];
	    int day =  calendar.get(Calendar.DATE);
	    int Sunday = day - weekDay + 1;
	    int month = calendar.get(Calendar.MONTH)+1;
	    int year = calendar.get(Calendar.YEAR);
	    
	    for(int i = 0; i<7;i++){
	    	Long temp = db.calculate(year, month, ++Sunday, 1);
	    	maxValues[i] = new BigDecimal(temp).divide(new BigDecimal(1000000),1,1).doubleValue();
	    }
	    db.close();
	   
	    XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
	    RangeCategorySeries series = new RangeCategorySeries("����");
	    int length = minValues.length;
	    for (int k = 0; k < length; k++) {
	      series.add(minValues[k], maxValues[k]);
	    }
	   
	    dataset.addSeries(series.toXYSeries());

	    int[] colors = new int[] { Color.RED };
	    XYMultipleSeriesRenderer renderer = buildBarRenderer(colors);
	    setChartSettings(renderer, "ÿ������ͳ��", "���ڣ��죩", "����(MB)", 0.5, 7.5,
	        0, 30, Color.BLACK, Color.WHITE);
	    renderer.setBackgroundColor(Color.rgb(47, 132, 187));
	    renderer.setApplyBackgroundColor(true);
	    renderer.setMarginsColor(Color.rgb(47, 132, 187));
	    renderer.setBarSpacing(0.5);
	    
	    renderer.setXLabelsAlign(Align.RIGHT);
	    renderer.setYLabelsAlign(Align.RIGHT);
	    
	    renderer.setXLabels(1);
	    renderer.setYLabels(10);
	    renderer.addTextLabel(1, "һ");
	    renderer.addTextLabel(2, "��");
	    renderer.addTextLabel(3, "��");
	    renderer.addTextLabel(4, "��");
	    renderer.addTextLabel(5, "��");
	    renderer.addTextLabel(6, "��");
	    renderer.addTextLabel(7, "��");
	    renderer.setAxisTitleTextSize(12);
	    renderer.setDisplayChartValues(true);
	    renderer.setChartValuesTextSize(12);
        renderer.setLabelsTextSize(12);
	    return ChartFactory.getRangeBarChartIntent(context, dataset, renderer, Type.DEFAULT,
	        "ÿ������ͳ��");
	  }
	}


