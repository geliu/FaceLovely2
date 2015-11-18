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
	 private InputStream is;/* �ļ�����������ȡ�ļ��� */
	 private byte[] b;/* �ֽ����飬������ȡ�ļ����� */	 
	 private String Text_of_output;/* �ַ��������ļ��ж�ȡ�����ַ��� */	 
	 private String Date_input1,Date_input2;/* �ַ������û�������ַ��� */	 
	 private OutputStream os1,os2,oss;/* �ļ�������������ļ��� */	 
	 private Button saveTxt, escTxt;/* ��ť�����ļ� */	
	 private DatePicker dpicker1;
	 private DatePicker dpicker2;
	 private Calendar c;	
	 
    @Override
   
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.time);
		UIinit();/* ��ʼ��UIԪ�ط��� */
		Logic();/* ����¼��߼����� */	
    }
    
   	private int getYdate(String s1){
   		String Ydate=s1.substring(0, 3);  //ȡǰ��λ�ַ��������
   		int yearInt=Integer.parseInt(Ydate); 
   		return yearInt;
   	}
   	private int getMdate(String s2){
   		String Ydate=s2.substring(0, 3);  //ȡ�м��λ�ַ������·�
   		int monthInt=Integer.parseInt(Ydate); 
   		return monthInt;
   	}
   	private int getDdate(String s3){
   		String Ydate=s3.substring(0, 3);  //ȡ���λ�ַ���������
   		int dateInt=Integer.parseInt(Ydate); 
   		return dateInt;
   	}
	private void UIinit() {
		/* ��ʼ��UI */
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
	    	   textview1.setText("StartTime�ǣ�");
	       }else
	    	   textview1.setText("StartTime�ǣ�"+myRound1);
	       if(myRound2==null||myRound2.equals("")){
	    	   textview2.setText("StopTime�ǣ�");
	       }else
	    	   textview2.setText("StopTime�ǣ�"+myRound2);
	}
    
	private void Logic() {
		saveTxt.setOnClickListener(this);
		escTxt.setOnClickListener(this);	
	}   
    
	public void onClick(View v) {
		/* ����ID�жϰ�ť�¼� */
		switch (v.getId()) {
		case R.id.Button_save: {
			/* ��ʾ */
			os1=null;
			os2=null;
			// TODO Auto-generated method stub
			try {
				/* ���ļ������������myPage���Բ�����ģʽ�� */
				os1 = this.openFileOutput("myDate1", MODE_PRIVATE);		
				os2 = this.openFileOutput("myDate2", MODE_PRIVATE);					

				/* ����û�������ַ��� */
				Date_input1=format(dpicker1.getYear())+format(dpicker1.getMonth()+1)+format(dpicker1.getDayOfMonth());
				Date_input2=format(dpicker2.getYear())+format(dpicker2.getMonth()+1)+format(dpicker2.getDayOfMonth());

				/* ���ַ���ת�����ֽ����飬д���ļ��� */
				os1.write(Date_input1.getBytes());
				os2.write(Date_input2.getBytes());
			} catch (FileNotFoundException e) {

			} catch (IOException e) {

			} finally {
				if( Integer.parseInt(Date_input1)<Integer.parseInt(Date_input2)){
				try {
					os1.flush();
					/* �ر��ļ������ */
					os1.close();
					NoteDebug("���óɹ�");
				} catch (IOException e) {

				}

				finish();
			} else
				NoteDebug("����������");
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
	
	/* ��ʽ���ַ���(7:3->07:03) */
	private String format(int x)
	{
		String s = "" + x;
		if (s.length() == 1)
			s = "0" + s;
		return s;
	}    
   
	private void NoteDebug(String showString) {
		/*��ʾToast��ʾ*/
		Toast.makeText(this, showString, Toast.LENGTH_SHORT).show();
	}    
}