package com.cqu.android.allservice.setup;

import com.cqu.android.Activity.R;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;
import java.io.InputStream;
import java.io.OutputStream;
import android.widget.EditText;

public class Round extends Activity implements Button.OnClickListener {
	private TextView textview;
	private InputStream is;/* �ļ�����������ȡ�ļ��� */
	private byte[] b;/* �ֽ����飬������ȡ�ļ����� */
	private TextView showmyText;/* TextView����ʾ��ȡ�ļ����� */
	private String Text_of_output;/* �ַ��������ļ��ж�ȡ�����ַ��� */
	private String Text_of_input;/* �ַ������û�������ַ��� */
	private EditText inputArt;/* �༭�������û��ַ��� */
	private OutputStream os1,os2,oss;/* �ļ�������������ļ��� */	
	private Button saveTxt, escTxt;/* ��ť�����ļ� */
	private static final String[] m_Countries = { "һ��", "����", "����", "����", "һ��" };

	private TextView round_textView;
	private Spinner m_spinner;
	private ArrayAdapter<String> adapter;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.round);
		UIinit();
		
		//����ѡ������ArrayAdapter����
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, m_Countries);

		// ���������б���
		adapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		//��Adapter��ӵ�m_Countries
		m_spinner.setAdapter(adapter);

		//���Spinner�¼�����
		m_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
//				m_textView.setText("���ļ������ڣ�" + m_Countries[arg2]);
				Text_of_input=m_Countries[arg2];
				//������ʾ��ǰѡ�����
				arg0.setVisibility(View.VISIBLE);
			}

			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
			}
		});
		Logic();
	}

	private void UIinit() {
		/* ��ʼ��UI */
		round_textView = (TextView) findViewById(R.id.TextView_round);
		m_spinner = (Spinner) findViewById(R.id.Spinner01);
		saveTxt = (Button) findViewById(R.id.Button_save_round);
		escTxt = (Button) findViewById(R.id.Button_cancle_round);
	       Parameter par = new Parameter(this);
	       String myRound = par.getParameter("myRound");
	       if(myRound==null||myRound.equals("")){
	    	   round_textView.setText("���õ������ǣ���");
	       }else
	    	   round_textView.setText("���õ������ǣ�"+myRound);
	}

	private void Logic() {
		saveTxt.setOnClickListener(this);
		escTxt.setOnClickListener(this);
	}

	public void onClick(View v) {
		/* ����ID�жϰ�ť�¼� */
		switch (v.getId()) {
		case R.id.Button_save_round: {
			/* ��ʾ */
			os1 = null;
			// TODO Auto-generated method stub

			try {
				/* ���ļ������������myRound���Բ�����ģʽ�� */
				os1 = this.openFileOutput("myRound", MODE_PRIVATE);
				/* ���ַ���ת�����ֽ����飬д���ļ��� */
				os1.write(Text_of_input.getBytes());
			} catch (FileNotFoundException e) {

			} catch (IOException e) {

			} finally {
					try {
						os1.flush();
						/* �ر��ļ������ */
						os1.close();
						NoteDebug("���óɹ�");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						NoteDebug("�ļ��ر�ʧ��" + e);
					}
					finish();
			}
		}
			break;

		case R.id.Button_cancle_round: {
			finish();
			NoteDebug("Byebye");
			break;
		}
		default:
			break;
		}
	}

	private void NoteDebug(String showString) {
		/*��ʾToast��ʾ*/
		Toast.makeText(this, showString, Toast.LENGTH_SHORT).show();
	}
}