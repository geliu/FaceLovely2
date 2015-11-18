package com.cqu.android.allservice.setup;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.DatePicker;
import android.app.DatePickerDialog;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.TimePicker.OnTimeChangedListener;

import com.cqu.android.Activity.R;

public class SetTime extends Activity implements Button.OnClickListener{
    /** Called when the activity is first created. */
	
	 private TextView textview1,textview2;
	 private InputStream is;/* 文件输入流，读取文件流 */
	 private byte[] b;/* 字节数组，用来读取文件内容 */	 
	 private String Text_of_output;/* 字符串，从文件中读取到得字符串 */	 
	 private String Date_input1,Date_input2;/* 字符串，用户输入的字符串 */	 
	 private OutputStream os1,os2,oss;/* 文件输出流，保存文件流 */	 
	 private Button saveTxt, escTxt;/* 按钮，打开文件 */	
	 private DatePicker dpicker1;
	 private DatePicker dpicker2;
	 private Calendar c;	
	 
    @Override
   
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.time);
		UIinit();/* 初始化UI元素方法 */
		Logic();/* 添加事件逻辑方法 */	
    }
    
   	private int getYdate(String s1){
   		String Ydate=s1.substring(0, 3);  //取前四位字符代表年份
   		int yearInt=Integer.parseInt(Ydate); 
   		return yearInt;
   	}
   	private int getMdate(String s2){
   		String Ydate=s2.substring(0, 3);  //取中间二位字符代表月份
   		int monthInt=Integer.parseInt(Ydate); 
   		return monthInt;
   	}
   	private int getDdate(String s3){
   		String Ydate=s3.substring(0, 3);  //取后二位字符代表日子
   		int dateInt=Integer.parseInt(Ydate); 
   		return dateInt;
   	}
	private void UIinit() {
		/* 初始化UI */
	       textview1 =(TextView)findViewById(R.id.TextView03);
	       textview2 =(TextView)findViewById(R.id.TextView04);	       
	       saveTxt = (Button) findViewById(R.id.Button_save);
	       escTxt = (Button) findViewById(R.id.Button_cancle);
	       dpicker1 = (DatePicker) this.findViewById(R.id.DatePicker01);
	       dpicker2 = (DatePicker) this.findViewById(R.id.DatePicker02);
	       
	       Parameter par = new Parameter(this);
	       String myRound1 = par.getParameter("myDate1");   
	       String myRound2 = par.getParameter("myDate2");   
		      
	       if(myRound1==null||myRound1.equals("")){
	    	   textview1.setText("StartTime是：");
	       }else
	    	   textview1.setText("StartTime是："+myRound1);
	       if(myRound2==null||myRound2.equals("")){
	    	   textview2.setText("StopTime是：");
	       }else
	    	   textview2.setText("StopTime是："+myRound2);
	}
    
	private void Logic() {
		saveTxt.setOnClickListener(this);
		escTxt.setOnClickListener(this);	
	}   
    
	public void onClick(View v) {
		/* 根据ID判断按钮事件 */
		switch (v.getId()) {
		case R.id.Button_save: {
			/* 提示 */
			os1=null;
			os2=null;
			// TODO Auto-generated method stub
			try {
				/* 打开文件输出流，名称myPage，以不覆盖模式打开 */
				os1 = this.openFileOutput("myDate1", MODE_PRIVATE);		
				os2 = this.openFileOutput("myDate2", MODE_PRIVATE);					

				/* 获得用户输入的字符串 */
				Date_input1=format(dpicker1.getYear())+format(dpicker1.getMonth()+1)+format(dpicker1.getDayOfMonth());
				Date_input2=format(dpicker2.getYear())+format(dpicker2.getMonth()+1)+format(dpicker2.getDayOfMonth());

				/* 把字符串转换成字节数组，写入文件中 */
				os1.write(Date_input1.getBytes());
				os2.write(Date_input2.getBytes());
			} catch (FileNotFoundException e) {

			} catch (IOException e) {

			} finally {
				if( Integer.parseInt(Date_input1)<Integer.parseInt(Date_input2)){
				try {
					os1.flush();
					/* 关闭文件输出流 */
					os1.close();
					NoteDebug("设置成功");
				} catch (IOException e) {

				}

				finish();
			} else
				NoteDebug("请重新输入");
			}
		}
			break;
		case R.id.Button_cancle: {
			finish();
			NoteDebug("Byebye");
			break;
		}
		default:
			break;}
		}
	
	/* 格式化字符串(7:3->07:03) */
	private String format(int x)
	{
		String s = "" + x;
		if (s.length() == 1)
			s = "0" + s;
		return s;
	}    
   
	private void NoteDebug(String showString) {
		/*显示Toast提示*/
		Toast.makeText(this, showString, Toast.LENGTH_SHORT).show();
	}    
}