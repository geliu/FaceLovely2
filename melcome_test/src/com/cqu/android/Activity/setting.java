package com.cqu.android.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGestureListener;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.cqu.android.allservice.setup.Contact;
import com.cqu.android.allservice.setup.Left;
import com.cqu.android.allservice.setup.Parameter;
import com.cqu.android.bean.onGestureListener;
import com.cqu.android.db.DatabaseAdapter;


public class setting extends Activity implements OnTouchListener, OnGestureListener{
	//����GestureDetector����
	GestureDetector mGestureDetector;
	
	private View View_quota;
	private View View_left;
	private View View_clear;
	private View View_date;
	private EditText dt;
	private EditText dt1;
	private EditText dt2;
	private DatabaseAdapter dbAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setup);
		setTitle("ϵͳ����");
		dbAdapter = new DatabaseAdapter(this);
        dbAdapter.open();
        
        mGestureDetector = new GestureDetector(this,new onGestureListener(setting.this));

        TableLayout viewSnsLayout = (TableLayout)findViewById(R.id.test);  
	    viewSnsLayout.setOnTouchListener(this);  
	    viewSnsLayout.setLongClickable(true);
	}
	public void onResume() {
		super.onResume();
		setContentView(R.layout.setup);
		ToggleButton isCount;
		ToggleButton isWarn;

		isCount = (ToggleButton) findViewById(R.id.ToggleButton1);
		isWarn = (ToggleButton) findViewById(R.id.ToggleButton2);
		Parameter par1 = new Parameter(this);
		String mCount = par1.getParameter("mCount");
		final TextView is_statist = (TextView)findViewById(R.id.is_statist);
		
		//��ʼ��is_statist��ֵ
		if(mCount.equals("1"))
		{
			is_statist.setText("����ͳ�ƹ����ѿ���");
		}
		else
		{
			is_statist.setText("����ͳ�ƹ����ѹر�");
		}
		
		// ��������ͳ�ƹ����Ƿ���
		isCount.setChecked(this.BoxState(mCount));
		isCount
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						Contact contact;
						// TODO Auto-generated method stub
						if (isChecked) {
							contact = new Contact(setting.this, "mCount");
							contact.saveParameter("1");
							is_statist.setText("����ͳ�ƹ����ѿ���");
						} else {
							contact = new Contact(setting.this, "mCount");
							contact.saveParameter("0");
							is_statist.setText("����ͳ�ƹ����ѹر�");
						}
					}
				});

		
		// ����������ʾ�����Ƿ���
		final TextView is_warn = (TextView)findViewById(R.id.is_warn);
		Parameter par2 = new Parameter(this);
		String mWarn = par2.getParameter("mWarn");
		//��ʼ��is_warn��ֵ
		if(mWarn.equals("1"))
		{
			is_warn.setText("������ʾ�����ѿ���");
		}
		else
		{
			is_warn.setText("������ʾ�����ѹر�");
		}
		
		isWarn.setChecked(this.BoxState(mWarn));
		isWarn
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						Contact contact;
						// TODO Auto-generated method stub
						if (isChecked) {
							contact = new Contact(setting.this, "mWarn");
							contact.saveParameter("1");
							is_warn.setText("������ʾ�����ѿ���");
						} else {
							contact = new Contact(setting.this, "mWarn");
							contact.saveParameter("0");
							is_warn.setText("������ʾ�����ѹر�");
						}
					}
				});

		ImageView imgView1 = (ImageView) this.findViewById(R.id.imageButton_quota);
		ImageView imgView2 = (ImageView) this.findViewById(R.id.imageButton_date);
		ImageView imgView3 = (ImageView) this.findViewById(R.id.imageButton_period);
		ImageView imgView4 = (ImageView) this.findViewById(R.id.imageButton_left);
		ImageView imgView5 = (ImageView) this.findViewById(R.id.imageButton_clear);
		final TextView limit_flow = (TextView)findViewById(R.id.limit_flow);
		final TextView flow_remind = (TextView)findViewById(R.id.flow_remind);
		final TextView count_date = (TextView)findViewById(R.id.count_date);
		final TextView refresh = (TextView)findViewById(R.id.refresh);
		final TextView clear_data = (TextView)findViewById(R.id.clear_data);
		
		//��ʼ��limit_flow��ֵ
		Parameter par3 = new Parameter(this);
		final String mLimit = par3.getParameter("mLimit");
		limit_flow.setText("ÿ�������޶�Ϊ"+mLimit+"MB");
        // ����ÿ�������޶�
		imgView1.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub

				LayoutInflater factory = LayoutInflater.from(setting.this);
				View_quota = factory.inflate(R.layout.setup_quota, null);

				new AlertDialog.Builder(setting.this).setTitle("ÿ�������޶�").setIcon(
						android.R.drawable.ic_dialog_info).setView(View_quota)
						.setPositiveButton("ȷ��",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										Contact contact = new Contact(
												setting.this, "mLimit");
										dt = (EditText) View_quota
												.findViewById(R.id.quota12);
										String limit = dt.getText().toString();
										contact.saveParameter(limit);
										limit_flow.setText("ÿ�������޶�Ϊ"+limit+"MB");
									}

								}).setNegativeButton("ȡ��", null).show();
			}
		});

		
		//��ʼ��count_date��ֵ
		Parameter par4 = new Parameter(this);
		String mCountDate = par3.getParameter("mdate");
		count_date.setText("�½�����Ϊ����"+mCountDate+"��");
		//�����½�����
		imgView2.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub

				LayoutInflater factory = LayoutInflater.from(setting.this);
				View_date = factory.inflate(R.layout.setup_date, null);

				new AlertDialog.Builder(setting.this).setTitle("�½�����").setIcon(
						android.R.drawable.ic_dialog_info).setView(View_date)
						.setPositiveButton("ȷ��",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										Contact contact = new Contact(
												setting.this, "mdate");
										dt2 = (EditText) View_date
												.findViewById(R.id.setup_date);
										String date = dt2.getText().toString();
										if(Integer.valueOf(date)<1||Integer.valueOf(date)>31)
										{
											Toast.makeText(setting.this, "������1~31�����֣�", Toast.LENGTH_SHORT).show();
										}
										else{
																				
										contact.saveParameter(date);
										count_date.setText("�½�����Ϊ����"+date+"��");
										}
									}

								}).setNegativeButton("ȡ��", null).show();
			}
		});

		//��ʼ��ˢ��Ƶ��
		Parameter par5 = new Parameter(this);
		String mRefresh = par3.getParameter("myRound");
		refresh.setText("����ˢ��Ƶ��Ϊ"+mRefresh+"һ��");
		//����ˢ��Ƶ��
		imgView3.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				LayoutInflater factory = LayoutInflater.from(setting.this);
				final View View_quota = factory.inflate(R.layout.setup_period,
						null);

				final RadioGroup rGroup = (RadioGroup) View_quota
						.findViewById(R.id.date_DatePicker);
				Parameter par = new Parameter(setting.this);
				final String str = par.getParameter("myRound");

				final RadioButton rb1 = (RadioButton) View_quota.findViewById(R.id.RadioButton1);
				final RadioButton rb2 = (RadioButton) View_quota.findViewById(R.id.RadioButton2);
				final RadioButton rb3 = (RadioButton) View_quota.findViewById(R.id.RadioButton3);
				final RadioButton rb4 = (RadioButton) View_quota.findViewById(R.id.RadioButton4);
				
				final StringBuilder sBuilder = new StringBuilder();;
				if (str.equals("30��")) {
					rb1.setChecked(true);
				} else if (str.equals("50��") || str.equals("")) {
					rb2.setChecked(true);
				} else if (str.equals("100��")) {
					rb3.setChecked(true);
				} else if (str.equals("150��")) {
					rb4.setChecked(true);
				}
				rGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
					
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						// TODO Auto-generated method stub
					
						if (checkedId == rb1.getId()) {
							sBuilder.append(rb1
									.getText()
									.toString());
						} else if (checkedId == rb2
								.getId()) {
							sBuilder.append(rb2
									.getText()
									.toString());
						} else if (checkedId == rb3
								.getId()) {
							sBuilder.append(rb3
									.getText()
									.toString());
						} else if (checkedId == rb4
								.getId()) {
							sBuilder.append(rb4
									.getText()
									.toString());
		
						}
					}
					
				});
				new AlertDialog.Builder(setting.this).setTitle("��������").setView(
						View_quota).setPositiveButton("ȷ��",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								Log.v("click","click");
								Contact contact = new Contact(
										setting.this, "myRound");
								contact.saveParameter(sBuilder.toString());
								refresh.setText("����ˢ��Ƶ��Ϊ"+sBuilder.toString()+"һ��");
						}

						}).setNegativeButton("ȡ��", null).show();
			}
		});
		
		//��ʼ��ÿ��ʣ����������
		Parameter par6 = new Parameter(this);
		String mLeft = par3.getParameter("mLeft");
		flow_remind.setText("ÿ��ʣ������Ϊ"+mLeft+"MBʱ������");
		//����ÿ��ʣ����������
		imgView4.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				LayoutInflater factory = LayoutInflater.from(setting.this);
				View_left = factory.inflate(R.layout.setup_left, null);

				new AlertDialog.Builder(setting.this).setTitle("ÿ��ʣ����������").setIcon(
						android.R.drawable.ic_dialog_info).setView(View_left)
						.setPositiveButton("ȷ��",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										
										// TODO Auto-generated method stub
										Left left = new Left(
												setting.this, "mLeft");
										dt1 = (EditText) View_left
												.findViewById(R.id.setup_left);
										String strLeft = dt1.getText().toString();
										
										//ʣ�����������ò���С�������޶�
										if(Integer.valueOf(strLeft) > Integer.valueOf(mLimit))
										{
											Toast.makeText(setting.this, "ʣ�����������ò���С�������޶�", Toast.LENGTH_SHORT).show();
											
										//	finish();
										}
										else{
										left.saveParameter(strLeft);
										flow_remind.setText("ÿ��ʣ������Ϊ"+strLeft+"MBʱ������");
										}
									}

								}).setNegativeButton("ȡ��", null).show();
			}
		});
		
		
		//�������ͳ������
		imgView5.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				LayoutInflater factory = LayoutInflater.from(setting.this);
				View_clear = factory.inflate(R.layout.setup_clear, null);

				new AlertDialog.Builder(setting.this).setTitle("��ȷ��Ҫ�������ͳ�Ƽ�¼��").setIcon(
						android.R.drawable.ic_dialog_info).setView(View_clear)
						.setPositiveButton("ȷ��",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										
										// TODO Auto-generated method stub
										dbAdapter.clear();
										clear_data.setText("���������");
									}

								}).setNegativeButton("ȡ��", null).show();
			}
		});
		
		
		
		



	}

	public boolean BoxState(String s) {
		if (s.equals("")) {
			return false;
		} else if (Integer.parseInt(s) == 1) {
			return true;
		} else {
			return false;
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {

		// ���¼����Ϸ��ذ�ť

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent1 = new Intent();
			intent1.setClass(setting.this, mainPage.class);
			startActivity(intent1);
			setting.this.finish();
		}
		return true;
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

}

