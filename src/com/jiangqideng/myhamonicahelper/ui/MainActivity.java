package com.jiangqideng.myhamonicahelper.ui;

import com.jiangqideng.myhamonicahelper.view.*;
import java.util.ArrayList;
import java.util.HashMap;
import com.example.myhamonicahelper.R;
import com.qq.e.ads.AdRequest;
import com.qq.e.ads.AdSize;
import com.qq.e.ads.AdView;
import com.qq.e.appwall.GdtAppwall;
import com.tencent.stat.StatService;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
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

public class MainActivity extends Activity {

	private int pitch = 0;
	private int offset = 0;
	private int num = 1;
	private String bigMusicStrings[] = new String[] { "C", "C#", "D", "Eb",
			"E", "F", "F#", "G", "Ab", "A", "Bb", "B" };
	private MusicNoteLayout musicNoteLayout;
	private TextView buttonToSpinner1;
	private TextView buttonToSpinner2;
	private TextView textView7;
	private ToggleButton toggleButton1;
	private RelativeLayout inputLayout;
	private RelativeLayout outputLayout;
	private RelativeLayout extraLayout;
	private RelativeLayout buttonLayout;
	private Button button_firstStart1;
	private Button button_firstStart2;
	private Button button_firstStart3;
	private Button button_firstStart4;
	private int inputBigMusicIndex = 0;
	private int outputBigMusicIndex = 0;
	private float defaultHight;
	private SeekBar seekBar;
	int[] menu_image_array = { R.drawable.ic_menu_delete,
			R.drawable.ic_menu_save, R.drawable.ic_menu_preferences,
			R.drawable.ic_menu_help, R.drawable.ic_menu_info_details,
			R.drawable.ic_menu_favorite };
	/** �˵����� **/
	String[] menu_name_array = { "���", "����", "����", "����", "��ϸ", "����" };
	AlertDialog menuDialog;// menu�˵�Dialog
	AlertDialog promptDialog; // ��ʾ�Ի���
	AlertDialog helpDialog;
	GridView menuGrid;
	View menuView;

	private AdView bannerAD;
	private int[] numcode = { 0, 0, 0, 0, 0, 0 };
	private SharedPreferences settings;
	private boolean hideAD = true;
	private boolean firstStart = true;

	// private Spinner spinner2;

	/**
	 * �����ֻ��ķֱ��ʴ� dp �ĵ�λ ת��Ϊ px(����)
	 */
	public static float dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (dpValue * scale + 0.5f);
	}

	/**
	 * ��spֵת��Ϊpxֵ����֤���ִ�С����
	 * 
	 * @param spValue
	 * @param fontScale
	 *            ��DisplayMetrics��������scaledDensity��
	 * @return
	 */
	public static float sp2px(Context context, float spValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (spValue * fontScale + 0.5f);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		StatService.trackCustomEvent(this, "onCreate", "");

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

		defaultHight = sp2px(MainActivity.this, 50);
		musicNoteLayout = new MusicNoteLayout((50 + 20) / 2, this);// �ߴ�ÿ�������Ŀ��ȣ���׼�ߴ�Ϊ50��

		inputLayout = (RelativeLayout) findViewById(R.id.inputLayout);
		outputLayout = (RelativeLayout) findViewById(R.id.outputLayout);
		extraLayout = (RelativeLayout) findViewById(R.id.extraLayout);
		buttonLayout = (RelativeLayout) findViewById(R.id.buttonLayout);
		buttonToSpinner1 = (TextView) findViewById(R.id.buttonToSpinner1);
		buttonToSpinner2 = (TextView) findViewById(R.id.buttonToSpinner2);
		textView7 = (TextView) findViewById(R.id.textView7);
		toggleButton1 = (ToggleButton) findViewById(R.id.toggleButton1);
		Spinner spinner1 = (Spinner) findViewById(R.id.spinner1);
		Spinner spinner2 = (Spinner) findViewById(R.id.spinner2);
		seekBar = (SeekBar) findViewById(R.id.seekBar1);

		if (firstStart) {
			hideAD = true;
			button_firstStart1.setVisibility(View.VISIBLE);
			toggleButton1.setAlpha(0.15f);
			seekBar.setAlpha(0.15f);
			textView7.setAlpha(0.15f);
			buttonLayout.setAlpha(0.15f);
		}

		if (!hideAD) {
			showBannerAD();
		}
		final GdtAppwall appwall = new GdtAppwall(this, "1101983001",
				"9007479622553201356", false);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, bigMusicStrings);
		adapter.setDropDownViewResource(R.layout.myspinner_dropdown);
		spinner1.setAdapter(adapter);
		spinner2.setAdapter(adapter);
		spinner1.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				buttonToSpinner1.setText(bigMusicStrings[position] + "��");
				inputBigMusicIndex = position;
				musicNoteLayout.setInputBigMusicIndex(position);
				ArrayList<Integer> standardNums = musicNoteLayout
						.getStandardNums();
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
				// TODO Auto-generated method stub

			}
		});

		spinner2.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				buttonToSpinner2.setText(bigMusicStrings[position] + "��");
				outputBigMusicIndex = position;
				musicNoteLayout.setOutputBigMusicIndex(position);
				ArrayList<Integer> standardNums = musicNoteLayout
						.getStandardNums();
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
					// ��������ܴ�������ѭ���ⲿ�֣�������������и�����
					// musicNoteLayout.update((seekBar.getProgress()+20)/2,
					// outputBigMusicIndex);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
			}
		});

		spinner1.setAlpha(0.0f);
		spinner2.setAlpha(0.0f);

		RadioGroup radioGroup1 = (RadioGroup) findViewById(R.id.radioGroup1);
		radioGroup1.setOnCheckedChangeListener(onCheckedChangeListener);
		RadioGroup radioGroup2 = (RadioGroup) findViewById(R.id.radioGroup2);
		radioGroup2.setOnCheckedChangeListener(onCheckedChangeListener);

		Button backSpacebButton = (Button) findViewById(R.id.buttonBackSpace);
		backSpacebButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int indexCursorLine = musicNoteLayout.getIndexCursorLine();
				int indexCursorRow = musicNoteLayout.getIndexCursorRow();
				int line = musicNoteLayout.getLine();
				float size = musicNoteLayout.getSize();

				if (!(indexCursorLine == 0 && indexCursorRow == 0)) {

					ArrayList<Integer> standardNums = musicNoteLayout
							.getStandardNums();

					standardNums.remove(line * indexCursorRow + indexCursorLine
							- 1);
					musicNoteLayout.setStandardNums(standardNums);
					if (indexCursorLine == 0) {
						musicNoteLayout.setIndexCursorLine(line - 1);
						musicNoteLayout.setIndexCursorRow(indexCursorRow - 1);
						musicNoteLayout.setX((line - 1) * 26 * size
								+ dip2px(MainActivity.this, 1));
						musicNoteLayout.setY(musicNoteLayout.getY() - 31 * size);
					} else {
						musicNoteLayout.setIndexCursorLine(indexCursorLine - 1);
						musicNoteLayout.setX(musicNoteLayout.getX() - 26 * size);
					}
					musicNoteLayout.drawCursor();

					inputLayout.removeView(inputLayout.findViewById((line
							* indexCursorRow + indexCursorLine - 1) * 4 + 1));
					inputLayout.removeView(inputLayout.findViewById((line
							* indexCursorRow + indexCursorLine - 1) * 4 + 2));
					inputLayout.removeView(inputLayout.findViewById((line
							* indexCursorRow + indexCursorLine - 1) * 4 + 3));
					inputLayout.removeView(inputLayout.findViewById((line
							* indexCursorRow + indexCursorLine - 1) * 4 + 4));

					outputLayout.removeView(outputLayout.findViewById((line
							* indexCursorRow + indexCursorLine - 1) * 4 + 1));
					outputLayout.removeView(outputLayout.findViewById((line
							* indexCursorRow + indexCursorLine - 1) * 4 + 2));
					outputLayout.removeView(outputLayout.findViewById((line
							* indexCursorRow + indexCursorLine - 1) * 4 + 3));
					outputLayout.removeView(outputLayout.findViewById((line
							* indexCursorRow + indexCursorLine - 1) * 4 + 4));

					TextView textView;
					for (int i = line * indexCursorRow + indexCursorLine - 1; i < standardNums
							.size(); i++) {
						for (int j = 1; j < 5; j++) {
							textView = (TextView) inputLayout
									.findViewById((i + 1) * 4 + j);
							if (textView.getX() <= 26 * size) {
								textView.setX((line - 1) * 26 * size
										+ textView.getX());
								textView.setY(textView.getY() - 31 * size);
							} else {
								textView.setX(textView.getX() - 26 * size);
							}
							textView.setId(i * 4 + j);
						}
					}
					for (int i = line * indexCursorRow + indexCursorLine - 1; i < standardNums
							.size(); i++) {
						for (int j = 1; j < 5; j++) {
							textView = (TextView) outputLayout
									.findViewById((i + 1) * 4 + j);
							if (textView.getX() <= 26 * size) {
								textView.setX((line - 1) * 26 * size
										+ textView.getX());
								textView.setY(textView.getY() - 31 * size);
							} else {
								textView.setX(textView.getX() - 26 * size);
							}
							textView.setId(i * 4 + j);
						}
					}
				}
			}
		});

		menuView = View.inflate(this, R.layout.my_menu, null);
		// ����AlertDialog
		menuDialog = new AlertDialog.Builder(this).create();
		// ����͸����
		Window window = menuDialog.getWindow();
		WindowManager.LayoutParams lp = window.getAttributes();
		lp.alpha = 0.7f;
		window.setAttributes(lp);

		menuDialog.setView(menuView);
		menuDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_MENU)// ��������
					dialog.dismiss();
				return false;
			}
		});

		menuGrid = (GridView) menuView.findViewById(R.id.gridview);
		menuGrid.setAdapter(getMenuAdapter(menu_name_array, menu_image_array));
		/** ����menuѡ�� **/
		menuGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				menuDialog.hide();
				switch (arg2) {
				case 0:// ���
					promptDialog.show();
					// musicNoteLayout.cleanAll();
					break;
				case 1:// ����
					Toast.makeText(MainActivity.this, "��Ǹ���ݲ�֧�ֱ��湦�ܣ�",
							Toast.LENGTH_SHORT).show();
					break;
				case 2:// ����
					// musicNoteLayout.update(40,4);
					Toast.makeText(MainActivity.this, "�������������������������ߴ���ģʽ",
							Toast.LENGTH_SHORT).show();
					break;
				case 3:// ����
					helpDialog.show();
					break;
				case 4:// ��ϸ
					Intent intent = new Intent();
					intent.setClass(MainActivity.this, DetailActivity.class);
					MainActivity.this.startActivity(intent);
					break;
				case 5:// ����
					appwall.doShowAppWall();
					break;
				}

			}
		});

		// ��������ʾ��Ϣ�ĶԻ���
		AlertDialog.Builder builder = new Builder(this);

		builder.setTitle("��ʾ��Ϣ��");
		builder.setMessage("�ò���������յ�ǰ���ݣ�������");
		builder.setNegativeButton("����", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				musicNoteLayout.cleanAll();
			}
		});

		builder.setPositiveButton("ȡ��", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		promptDialog = builder.create();
		Window promptDialogWindow = promptDialog.getWindow();
		WindowManager.LayoutParams lp_promptDialog = promptDialogWindow
				.getAttributes();
		lp_promptDialog.alpha = 0.8f;
		promptDialogWindow.setAttributes(lp);
		// �����ǰ����Ի���
		AlertDialog.Builder builder_help = new Builder(this);
		builder_help.setTitle("������Ϣ��");
		builder_help
				.setMessage("1. �����Ͻ�ѡ��������׵ĵ���\n2. ʹ����������������\n3. ѡ��ͬ�ĵ��ż������ת��\n");

		builder_help.setPositiveButton("ȷ��",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
					}
				});
		helpDialog = builder_help.create();

		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

				musicNoteLayout.update((seekBar.getProgress() + 20) / 2,
						outputBigMusicIndex);
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				textView7.setText(seekBar.getProgress() + "%");
			}
		});

		toggleButton1
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (!isChecked) {
							inputLayout
									.setLayoutParams(new LinearLayout.LayoutParams(
											LayoutParams.MATCH_PARENT, 0, 14.0f));
							outputLayout
									.setLayoutParams(new LinearLayout.LayoutParams(
											LayoutParams.MATCH_PARENT, 0, 0.0f));
							buttonLayout
									.setLayoutParams(new LinearLayout.LayoutParams(
											LayoutParams.MATCH_PARENT, 0, 7.0f));
							// extraLayout.setLayoutParams(new
							// LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
							// 0, 1.0f));
						} else {
							inputLayout
									.setLayoutParams(new LinearLayout.LayoutParams(
											LayoutParams.MATCH_PARENT, 0, 7.0f));
							outputLayout
									.setLayoutParams(new LinearLayout.LayoutParams(
											LayoutParams.MATCH_PARENT, 0, 7.0f));
							buttonLayout
									.setLayoutParams(new LinearLayout.LayoutParams(
											LayoutParams.MATCH_PARENT, 0, 7.0f));
							// extraLayout.setLayoutParams(new
							// LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
							// 0, 1.0f));
						}

					}
				});

		button_firstStart1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				button_firstStart1.setVisibility(View.INVISIBLE);
				button_firstStart2.setVisibility(View.VISIBLE);
				buttonLayout.setAlpha(1f);
				buttonToSpinner1.setAlpha(0.15f);
			}
		});
		button_firstStart2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				button_firstStart2.setVisibility(View.INVISIBLE);
				button_firstStart3.setVisibility(View.VISIBLE);
				buttonLayout.setAlpha(0.15f);
				buttonToSpinner1.setAlpha(1f);
			}
		});
		button_firstStart3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				button_firstStart3.setVisibility(View.INVISIBLE);
				button_firstStart4.setVisibility(View.VISIBLE);
				buttonToSpinner1.setAlpha(0.15f);
				toggleButton1.setAlpha(1f);
				seekBar.setAlpha(1f);
				textView7.setAlpha(1f);

			}
		});
		button_firstStart4.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				button_firstStart4.setVisibility(View.INVISIBLE);
				buttonToSpinner1.setAlpha(1f);
				outputLayout.setAlpha(1f);
				buttonLayout.setAlpha(1f);
			}
		});
	}

	@Override
	public boolean onMenuOpened(int featureId, Menu menu) {
		if (menuDialog == null) {
			menuDialog = new AlertDialog.Builder(this).setView(menuView).show();
		} else {
			menuDialog.show();
		}
		return false;// ����Ϊtrue ����ʾϵͳmenu
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add("menu");// ���봴��һ��
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

	// ���η����˳�
	private long exitTime = 0;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				Toast.makeText(getApplicationContext(), "�ٴε�������ء��˳�",
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
				Toast.makeText(getApplicationContext(), "pitch��offset����", 0)
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
			Toast.makeText(getApplicationContext(), "num����", 0).show();
			break;
		}
		// if (num==7 && pitch==1 && offset==1) {
		// Toast.makeText(MainActivity.this, "����ȷ����...", 0).show();
		// }else if (num==1 && pitch==-1 && offset==-1) {
		//
		// Toast.makeText(MainActivity.this, "����ȷ����...", 0).show();
		// }else
		int row = (int) ((inputLayout.getHeight() - defaultHight) / (31 * musicNoteLayout
				.getSize()));
		if (musicNoteLayout.getStandardNums().size() >= musicNoteLayout
				.getLine() * row)
		// if( musicNoteLayout.getX()>=inputLayout.getWidth()-2*26*size
		// && musicNoteLayout.getY() >= inputLayout.getHeight()-2*31*size )
		{
			// Toast.makeText(MainActivity.this, "����������",
			// Toast.LENGTH_SHORT).show();
			// System.out.println("line"+musicNoteLayout.getInputLayout().getHeight());
		} else {
			// MusicNote musicNote = new MusicNote(2, 3, 0, 0);//����ĵ������֡��������ߵ���
			// spinner2.setY(musicNoteLayout.getWindowHeight()/3);
			MusicNote musicNote = new MusicNote(inputBigMusicIndex, num,
					offset, pitch);
			musicNoteLayout.drawMusicNoteOutput(musicNote, outputBigMusicIndex);// ����ĵ�
			musicNoteLayout.drawMusicNoteInput(musicNote, inputBigMusicIndex);// ����ĵ�

			if (musicNoteLayout.getStandardNums().size() > 1) {

				int indexCursorLine = musicNoteLayout.getIndexCursorLine();
				int indexCursorRow = musicNoteLayout.getIndexCursorRow();
				int line = musicNoteLayout.getLine();
				float size = musicNoteLayout.getSize();

				TextView textView;
				for (int i = musicNoteLayout.getStandardNums().size() - 1; i >= line
						* indexCursorRow + indexCursorLine; i--) {
					for (int j = 1; j < 5; j++) {
						textView = (TextView) inputLayout.findViewById((i - 1)
								* 4 + j);
						if (textView.getX() >= 26 * size * (line - 1)) {
							textView.setX(textView.getX() - 26 * size
									* (line - 1));
							textView.setY(textView.getY() + 31 * size);
						} else {
							textView.setX(textView.getX() + 26 * size);
						}
						textView.setId(i * 4 + j);
					}
				}
				for (int i = musicNoteLayout.getStandardNums().size() - 1; i >= line
						* indexCursorRow + indexCursorLine; i--) {
					for (int j = 1; j < 5; j++) {
						textView = (TextView) outputLayout.findViewById((i - 1)
								* 4 + j);
						if (textView.getX() >= 26 * size * (line - 1)) {
							textView.setX(textView.getX() - 26 * size
									* (line - 1));
							textView.setY(textView.getY() + 31 * size);
						} else {
							textView.setX(textView.getX() + 26 * size);
						}
						textView.setId(i * 4 + j);
					}
				}
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
					Toast.makeText(MainActivity.this, "���ӹ��ɹ���",
							Toast.LENGTH_SHORT).show();
					showBannerAD();
				} else {
					Toast.makeText(MainActivity.this, "������ɹ���",
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
						+ dip2px(MainActivity.this, 1));
				musicNoteLayout.setY(tempRow * 31 * size + defaultHight);
			}
		}

		return super.onTouchEvent(event);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (!hideAD) {
			showBannerAD();
		}
	}

	private void showBannerAD() {
		bannerAD = new AdView(this, AdSize.BANNER, "1101983001",
				"9079537216591129292");
		AdRequest adRequest = new AdRequest();
		adRequest.setTestAd(false);
		adRequest.setRefresh(31);
		adRequest.setShowCloseBtn(false);
		extraLayout.removeAllViews();
		extraLayout.addView(bannerAD);
		bannerAD.fetchAd(new AdRequest());
	}
}