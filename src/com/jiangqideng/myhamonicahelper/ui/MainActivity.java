package com.jiangqideng.myhamonicahelper.ui;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.myhamonicahelper.R;
import com.jiangqideng.myhamonicahelper.ad.AppWallManager;
import com.jiangqideng.myhamonicahelper.ad.BannerAdManager;
import com.jiangqideng.myhamonicahelper.model.NumberClickModel;
import com.jiangqideng.myhamonicahelper.model.deleteModel;
import com.jiangqideng.myhamonicahelper.uinote.MusicNote;
import com.jiangqideng.myhamonicahelper.uinote.MusicNoteLayout;
import com.jiangqideng.myhamonicahelper.utils.SizeSwitch;
import com.tencent.stat.StatService;

/**
 * @author jiangqideng@163.com
 * @date 2016-6-27 下午1:46:45
 * @description 主界面
 */
public class MainActivity extends Activity {
	private int pitch = 0; // 音高
	private int offset = 0; // 升讲
	private int num = 1;
	private String[] bigMusicStrings = new String[] { "C", "C#", "D", "Eb", "E", "F", "F#", "G", "Ab", "A", "Bb", "B" };
	private MusicNoteLayout musicNoteLayout;
	private TextView buttonToSpinner1;
	private TextView buttonToSpinner2;
	private TextView textView7;
	private ToggleButton toggleButton1;
	private RelativeLayout inputLayout;
	private RelativeLayout outputLayout;
	public RelativeLayout extraLayout;
	private RelativeLayout buttonLayout;
	private Button button_firstStart1;
	private Button button_firstStart2;
	private Button button_firstStart3;
	private Button button_firstStart4;
	private Spinner spinner1;
	private Spinner spinner2;
	private int inputBigMusicIndex = 0;
	private int outputBigMusicIndex = 0;
	private float defaultHight;
	private SeekBar seekBar;
	int[] menu_image_array = { R.drawable.ic_menu_delete,
			R.drawable.ic_menu_save, R.drawable.ic_menu_preferences,
			R.drawable.ic_menu_help, R.drawable.ic_menu_info_details,
			R.drawable.ic_menu_favorite };
	//菜单文字 
	String[] menu_name_array = { "清空", "保存", "设置", "帮助", "详细", "更多" };
	AlertDialog menuDialog;// menu菜单Dialog
	AlertDialog promptDialog; // 提示对话框
	AlertDialog helpDialog;
	GridView menuGrid;
	View menuView;

	BannerAdManager bannerAdManager; //banner广告的管理
	AppWallManager appWallManager; //appWall广告的管理

	private int[] numcode = { 0, 0, 0, 0, 0, 0 };
	private SharedPreferences settings;
	private boolean hideAD = true;
	private boolean firstStart = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		StatService.trackCustomEvent(this, "onCreate", "");
		initResources();
		initView();
	}


	@Override
	public boolean onMenuOpened(int featureId, Menu menu) {
		if (menuDialog == null) {
			menuDialog = new AlertDialog.Builder(this).setView(menuView).show();
		} else {
			menuDialog.show();
		}
		return false;// 返回为true 则显示系统menu
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add("menu");// 必须创建一项
		return super.onCreateOptionsMenu(menu);
	}

	private SimpleAdapter getMenuAdapter(String[] menuNameArray,
			int[] imageResourceArray) {
		ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < menuNameArray.length; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("itemImage", imageResourceArray[i]);
			map.put("itemText", menuNameArray[i]);
			data.add(map);
		}
		SimpleAdapter simperAdapter = new SimpleAdapter(this, data,
				R.layout.my_menu_items,
				new String[] { "itemImage", "itemText" }, new int[] {
						R.id.item_image, R.id.item_text });
		return simperAdapter;
	}

	// 两次返回退出
	private long exitTime = 0;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				Toast.makeText(getApplicationContext(), "再次点击“返回”退出",
						Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
			} else {
				finish();
				System.exit(0);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	OnCheckedChangeListener onCheckedChangeListener = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			switch (checkedId) {
			case R.id.radio0:
				pitch = 1;
				break;
			case R.id.radio1:
				pitch = 0;
				break;
			case R.id.radio2:
				pitch = -1;
				break;
			case R.id.radio3:
				offset = 1;
				break;
			case R.id.radio4:
				offset = 0;
				break;
			case R.id.radio5:
				offset = -1;
				break;
			default:
				Toast.makeText(getApplicationContext(), "pitch或offset错误", 0)
						.show();
				break;
			}
		}
	};

	public void numClick(View view) {
		switch (Integer.parseInt((String) view.getTag())) {
		case 0:
			num = 0;
			break;
		case 1:
			num = 1;
			break;
		case 2:
			num = 2;
			break;
		case 3:
			num = 3;
			break;
		case 4:
			num = 4;
			break;
		case 5:
			num = 5;
			break;
		case 6:
			num = 6;
			break;
		case 7:
			num = 7;
			break;
		default:
			Toast.makeText(getApplicationContext(), "num错误", 0).show();
			break;
		}

		int row = (int) ((inputLayout.getHeight() - defaultHight) / (31 * musicNoteLayout
				.getSize()));
		if (musicNoteLayout.getStandardNums().size() >= musicNoteLayout
				.getLine() * row)
		{
			// Toast.makeText(MainActivity.this, "窗口已满！",
			// Toast.LENGTH_SHORT).show();
			// System.out.println("line"+musicNoteLayout.getInputLayout().getHeight());
		} else {
			MusicNote musicNote = new MusicNote(inputBigMusicIndex, num,
					offset, pitch);
			musicNoteLayout.drawMusicNoteOutput(musicNote, outputBigMusicIndex);// 输出的调
			musicNoteLayout.drawMusicNoteInput(musicNote, inputBigMusicIndex);// 输入的调

			if (musicNoteLayout.getStandardNums().size() > 1) {
				new NumberClickModel().setMusicNoteLayout(musicNoteLayout, outputLayout, inputLayout);
			}
			numcode[0] = numcode[1];
			numcode[1] = numcode[2];
			numcode[2] = numcode[3];
			numcode[3] = numcode[4];
			numcode[4] = numcode[5];
			numcode[5] = num;
			int[] password = { 4, 2, 5, 4, 2, 5 };
			if (numcode[0] == 4 && numcode[1] == 2 && numcode[2] == 5
					&& numcode[3] == 4 && numcode[4] == 2 && numcode[5] == 5
					&& firstStart == false) {

				SharedPreferences.Editor prefEditor = settings.edit();
				hideAD = !settings.getBoolean("hideAD", false);
				prefEditor.putBoolean("hideAD", hideAD);
				prefEditor.apply();
				musicNoteLayout.cleanAll();
				if (!hideAD) {
					Toast.makeText(MainActivity.this, "添加广告成功！",
							Toast.LENGTH_SHORT).show();
					bannerAdManager.showBannerAD(MainActivity.this);
				} else {
					Toast.makeText(MainActivity.this, "解除广告成功！",
							Toast.LENGTH_SHORT).show();
					extraLayout.removeAllViews();
				}

			}
		}

	}

	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		int[] position = new int[2];
		inputLayout.getLocationInWindow(position);
		float x = event.getX() - position[0];
		float y = event.getY() - position[1];
		int tempLine = -1;
		int tempRow = -1;
		int standardNumsSize = musicNoteLayout.getStandardNums().size();
		int line = musicNoteLayout.getLine();
		float size = musicNoteLayout.getSize();
		if (y <= position[1] + inputLayout.getHeight()) {

			tempRow = (int) ((y - defaultHight) / (31 * size));
			tempLine = (int) ((x + 13 * size) / (26 * size));

			if ((tempLine + tempRow * line) > standardNumsSize) {
				tempRow = standardNumsSize / line;
				tempLine = standardNumsSize - tempRow * line;
			}
			if (tempLine >= 0 && tempRow >= 0) {
				musicNoteLayout.setIndexCursorLine(tempLine);
				musicNoteLayout.setIndexCursorRow(tempRow);
				musicNoteLayout.drawCursor();
				musicNoteLayout.setX(tempLine * 26 * size
						+ SizeSwitch.dip2px(MainActivity.this, 1));
				musicNoteLayout.setY(tempRow * 31 * size + defaultHight);
			}
		}

		return super.onTouchEvent(event);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (!hideAD) {
			bannerAdManager.showBannerAD(MainActivity.this);
		}
	}

	public void onFirstStart() {
		hideAD = true;
		button_firstStart1.setVisibility(View.VISIBLE);
		toggleButton1.setAlpha(0.15f);
		seekBar.setAlpha(0.15f);
		textView7.setAlpha(0.15f);
		buttonLayout.setAlpha(0.15f);
	}

	private class NewItemSelectedListener implements OnItemSelectedListener {
		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1,
				int position, long arg3) {
			buttonToSpinner1.setText(bigMusicStrings[position] + "调");
			inputBigMusicIndex = position;
			musicNoteLayout.setInputBigMusicIndex(position);
			ArrayList<Integer> standardNums = musicNoteLayout.getStandardNums();
			if (standardNums != null) {
				for (int i = 0; i < standardNums.size(); i++) {

					MusicNote musicNote = new MusicNote(standardNums.get(i));
					TextView offsetTextView = (TextView) inputLayout
							.findViewById(i * 4 + 1);
					TextView bigMusciStringTextView = (TextView) inputLayout
							.findViewById(i * 4 + 2);
					TextView pitchUpTextView = (TextView) inputLayout
							.findViewById(i * 4 + 3);
					TextView pitchDownTextView = (TextView) inputLayout
							.findViewById(i * 4 + 4);

					offsetTextView.setText(musicNote
							.getoffsetString(inputBigMusicIndex));
					bigMusciStringTextView.setText(musicNote
							.getMusicString(inputBigMusicIndex));
					pitchUpTextView.setText(musicNote
							.getPitchUp(inputBigMusicIndex));
					pitchDownTextView.setText(musicNote
							.getPitchDown(inputBigMusicIndex));
				}
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
		}
	}

	private class NewItemSelectedListener2 implements OnItemSelectedListener {
		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1,
				int position, long arg3) {
			buttonToSpinner2.setText(bigMusicStrings[position] + "调");
			outputBigMusicIndex = position;
			musicNoteLayout.setOutputBigMusicIndex(position);
			ArrayList<Integer> standardNums = musicNoteLayout.getStandardNums();
			if (standardNums != null) {
				for (int i = 0; i < standardNums.size(); i++) {
					MusicNote musicNote = new MusicNote(standardNums.get(i));
					TextView offsetTextView = (TextView) outputLayout
							.findViewById(i * 4 + 1);
					TextView bigMusciStringTextView = (TextView) outputLayout
							.findViewById(i * 4 + 2);
					TextView pitchUpTextView = (TextView) outputLayout
							.findViewById(i * 4 + 3);
					TextView pitchDownTextView = (TextView) outputLayout
							.findViewById(i * 4 + 4);

					offsetTextView.setText(musicNote
							.getoffsetString(outputBigMusicIndex));
					bigMusciStringTextView.setText(musicNote
							.getMusicString(outputBigMusicIndex));
					pitchUpTextView.setText(musicNote
							.getPitchUp(outputBigMusicIndex));
					pitchDownTextView.setText(musicNote
							.getPitchDown(outputBigMusicIndex));
				}
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
		}
	}

	private void initView() {
		bannerAdManager = new BannerAdManager();
		appWallManager = new AppWallManager();
		if (firstStart) {
			onFirstStart();
		}

		if (!hideAD) {
			bannerAdManager.showBannerAD(MainActivity.this);
		}

		appWallManager.creatAppWall(MainActivity.this);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, bigMusicStrings);
		adapter.setDropDownViewResource(R.layout.myspinner_dropdown);
		spinner1.setAdapter(adapter);
		spinner2.setAdapter(adapter);
		spinner1.setOnItemSelectedListener(new NewItemSelectedListener());
		spinner2.setOnItemSelectedListener(new NewItemSelectedListener2());
		spinner1.setAlpha(0.0f);
		spinner2.setAlpha(0.0f);

		RadioGroup radioGroup1 = (RadioGroup) findViewById(R.id.radioGroup1);
		radioGroup1.setOnCheckedChangeListener(onCheckedChangeListener);
		RadioGroup radioGroup2 = (RadioGroup) findViewById(R.id.radioGroup2);
		radioGroup2.setOnCheckedChangeListener(onCheckedChangeListener);

		Button backSpacebButton = (Button) findViewById(R.id.buttonBackSpace);
		backSpacebButton.setOnClickListener(new backSpaceOnClickListener());

		menuView = View.inflate(this, R.layout.my_menu, null);
		// 创建AlertDialog
		menuDialog = new AlertDialog.Builder(this).create();
		// 设置透明度
		Window window = menuDialog.getWindow();
		WindowManager.LayoutParams lp = window.getAttributes();
		lp.alpha = 0.7f;
		window.setAttributes(lp);
		menuDialog.setView(menuView);
		menuDialog.setOnKeyListener(new NewKeyListener());
		menuGrid = (GridView) menuView.findViewById(R.id.gridview);
		menuGrid.setAdapter(getMenuAdapter(menu_name_array, menu_image_array));
		/** 监听menu选项 **/
		menuGrid.setOnItemClickListener(new NewItemClickListener());

		// 下面是提示信息的对话框
		AlertDialog.Builder builder = new Builder(this);
		builder.setTitle("提示信息！");
		builder.setMessage("该操作将会清空当前数据，继续？");
		builder.setNegativeButton("继续", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				musicNoteLayout.cleanAll();
			}
		});
		builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		promptDialog = builder.create();
		Window promptDialogWindow = promptDialog.getWindow();
		WindowManager.LayoutParams lp_promptDialog = promptDialogWindow.getAttributes();
		lp_promptDialog.alpha = 0.8f;
		promptDialogWindow.setAttributes(lp);
		// 下面是帮助对话框
		AlertDialog.Builder builder_help = new Builder(this);
		builder_help.setTitle("帮助信息！");
		builder_help.setMessage("1. 在左上角选择输入简谱的调号\n2. 使用软键盘输入音符\n3. 选择不同的调号即可完成转调\n");
		builder_help.setPositiveButton("确定",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
					}
				});
		helpDialog = builder_help.create();
		seekBar.setOnSeekBarChangeListener(new NewSeekBarChangeListener());
		toggleButton1.setOnCheckedChangeListener(new NewOnCheckedChangeListener());
		button_firstStart1.setOnClickListener(new firstOnClick1());
		button_firstStart2.setOnClickListener(new firstOnClick2());
		button_firstStart3.setOnClickListener(new firstOnClick3());
		button_firstStart4.setOnClickListener(new firstOnClick4());
	}
	
	private void initResources() {
		settings = getSharedPreferences("test", MODE_PRIVATE);
		firstStart = settings.getBoolean("firstStart", true);
		SharedPreferences.Editor prefEditor = settings.edit();
		prefEditor.putBoolean("firstStart", false);
		prefEditor.apply();
		hideAD = settings.getBoolean("hideAD", false);

		button_firstStart1 = (Button) findViewById(R.id.button_firstStart1);
		button_firstStart2 = (Button) findViewById(R.id.button_firstStart2);
		button_firstStart3 = (Button) findViewById(R.id.button_firstStart3);
		button_firstStart4 = (Button) findViewById(R.id.button_firstStart4);

		defaultHight = SizeSwitch.sp2px(MainActivity.this, 50);
		musicNoteLayout = new MusicNoteLayout((50 + 20) / 2, this);// 尺寸每个音符的宽度（基准尺寸为50）

		inputLayout = (RelativeLayout) findViewById(R.id.inputLayout);
		outputLayout = (RelativeLayout) findViewById(R.id.outputLayout);
		extraLayout = (RelativeLayout) findViewById(R.id.extraLayout);
		buttonLayout = (RelativeLayout) findViewById(R.id.buttonLayout);
		buttonToSpinner1 = (TextView) findViewById(R.id.buttonToSpinner1);
		buttonToSpinner2 = (TextView) findViewById(R.id.buttonToSpinner2);
		textView7 = (TextView) findViewById(R.id.textView7);
		toggleButton1 = (ToggleButton) findViewById(R.id.toggleButton1);
		spinner1 = (Spinner) findViewById(R.id.spinner1);
		spinner2 = (Spinner) findViewById(R.id.spinner2);
		seekBar = (SeekBar) findViewById(R.id.seekBar1);
	}

	private class backSpaceOnClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			new deleteModel().setMusicNoteLayout(musicNoteLayout, outputLayout, inputLayout, MainActivity.this);
		}
	}

	private class NewKeyListener implements DialogInterface.OnKeyListener {
		public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
			if (keyCode == KeyEvent.KEYCODE_MENU)// 监听按键
				dialog.dismiss();
			return false;
		}
	}

	private class NewItemClickListener implements
			AdapterView.OnItemClickListener {
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			menuDialog.hide();
			switch (arg2) {
			case 0:// 清空
				promptDialog.show();
				// musicNoteLayout.cleanAll();
				break;
			case 1:// 保存
				Toast.makeText(MainActivity.this, "抱歉，暂不支持保存功能！",
						Toast.LENGTH_SHORT).show();
				break;
			case 2:// 设置
					// musicNoteLayout.update(40,4);
				Toast.makeText(MainActivity.this, "您可以在主界面上设置音符尺寸与模式",
						Toast.LENGTH_SHORT).show();
				break;
			case 3:// 帮助
				helpDialog.show();
				break;
			case 4:// 详细
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, DetailActivity.class);
				MainActivity.this.startActivity(intent);
				break;
			case 5:// 更多
				appWallManager.show();
				break;
			}

		}
	}

	private class NewSeekBarChangeListener implements OnSeekBarChangeListener {
		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			musicNoteLayout.update((seekBar.getProgress() + 20) / 2,
					outputBigMusicIndex);
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
		}

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			textView7.setText(seekBar.getProgress() + "%");
		}
	}

	private class NewOnCheckedChangeListener implements
			CompoundButton.OnCheckedChangeListener {
		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			if (!isChecked) {
				inputLayout.setLayoutParams(new LinearLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, 0, 14.0f));
				outputLayout.setLayoutParams(new LinearLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, 0, 0.0f));
				buttonLayout.setLayoutParams(new LinearLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, 0, 7.0f));
				// extraLayout.setLayoutParams(new
				// LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
				// 0, 1.0f));
			} else {
				inputLayout.setLayoutParams(new LinearLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, 0, 7.0f));
				outputLayout.setLayoutParams(new LinearLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, 0, 7.0f));
				buttonLayout.setLayoutParams(new LinearLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, 0, 7.0f));
			}

		}
	}

	private class firstOnClick1 implements OnClickListener{
		@Override
		public void onClick(View v) {
			button_firstStart1.setVisibility(View.INVISIBLE);
			button_firstStart2.setVisibility(View.VISIBLE);
			buttonLayout.setAlpha(1f);
			buttonToSpinner1.setAlpha(0.15f);
		}
	}
	
	private class firstOnClick2 implements OnClickListener{
		@Override
		public void onClick(View v) {
			button_firstStart2.setVisibility(View.INVISIBLE);
			button_firstStart3.setVisibility(View.VISIBLE);
			buttonLayout.setAlpha(0.15f);
			buttonToSpinner1.setAlpha(1f);
		}
	}
	
	private class firstOnClick3 implements OnClickListener{
		@Override
		public void onClick(View v) {
			button_firstStart3.setVisibility(View.INVISIBLE);
			button_firstStart4.setVisibility(View.VISIBLE);
			buttonToSpinner1.setAlpha(0.15f);
			toggleButton1.setAlpha(1f);
			seekBar.setAlpha(1f);
			textView7.setAlpha(1f);
		}
	}
	
	private class firstOnClick4 implements OnClickListener{
		@Override
		public void onClick(View v) {
			button_firstStart4.setVisibility(View.INVISIBLE);
			buttonToSpinner1.setAlpha(1f);
			outputLayout.setAlpha(1f);
			buttonLayout.setAlpha(1f);
		}
	}
}
