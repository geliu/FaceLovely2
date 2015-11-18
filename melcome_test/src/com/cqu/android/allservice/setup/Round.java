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
	private InputStream is;/* 文件输入流，读取文件流 */
	private byte[] b;/* 字节数组，用来读取文件内容 */
	private TextView showmyText;/* TextView，显示读取文件内容 */
	private String Text_of_output;/* 字符串，从文件中读取到得字符串 */
	private String Text_of_input;/* 字符串，用户输入的字符串 */
	private EditText inputArt;/* 编辑框，输入用户字符串 */
	private OutputStream os1,os2,oss;/* 文件输出流，保存文件流 */	
	private Button saveTxt, escTxt;/* 按钮，打开文件 */
	private static final String[] m_Countries = { "一天", "两天", "三天", "五天", "一周" };

	private TextView round_textView;
	private Spinner m_spinner;
	private ArrayAdapter<String> adapter;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.round);
		UIinit();
		
		//将可选内容与ArrayAdapter连接
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, m_Countries);

		// 设置下拉列表风格
		adapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		//将Adapter添加到m_Countries
		m_spinner.setAdapter(adapter);

		//添加Spinner事件监听
		m_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
//				m_textView.setText("您的计算周期：" + m_Countries[arg2]);
				Text_of_input=m_Countries[arg2];
				//设置显示当前选择的项
				arg0.setVisibility(View.VISIBLE);
			}

			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
			}
		});
		Logic();
	}

	private void UIinit() {
		/* 初始化UI */
		round_textView = (TextView) findViewById(R.id.TextView_round);
		m_spinner = (Spinner) findViewById(R.id.Spinner01);
		saveTxt = (Button) findViewById(R.id.Button_save_round);
		escTxt = (Button) findViewById(R.id.Button_cancle_round);
	       Parameter par = new Parameter(this);
	       String myRound = par.getParameter("myRound");
	       if(myRound==null||myRound.equals("")){
	    	   round_textView.setText("设置的周期是：空");
	       }else
	    	   round_textView.setText("设置的周期是："+myRound);
	}

	private void Logic() {
		saveTxt.setOnClickListener(this);
		escTxt.setOnClickListener(this);
	}

	public void onClick(View v) {
		/* 根据ID判断按钮事件 */
		switch (v.getId()) {
		case R.id.Button_save_round: {
			/* 提示 */
			os1 = null;
			// TODO Auto-generated method stub

			try {
				/* 打开文件输出流，名称myRound，以不覆盖模式打开 */
				os1 = this.openFileOutput("myRound", MODE_PRIVATE);
				/* 把字符串转换成字节数组，写入文件中 */
				os1.write(Text_of_input.getBytes());
			} catch (FileNotFoundException e) {

			} catch (IOException e) {

			} finally {
					try {
						os1.flush();
						/* 关闭文件输出流 */
						os1.close();
						NoteDebug("设置成功");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						NoteDebug("文件关闭失败" + e);
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
		/*显示Toast提示*/
		Toast.makeText(this, showString, Toast.LENGTH_SHORT).show();
	}
}