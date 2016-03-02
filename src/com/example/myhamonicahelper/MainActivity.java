package com.example.myhamonicahelper;

import java.util.ArrayList;
import java.util.HashMap;

import com.qq.e.ads.AdRequest;
import com.qq.e.ads.AdSize;
import com.qq.e.ads.AdView;
import com.qq.e.appwall.GdtAppwall;
import com.tencent.stat.StatService;

import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
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
import android.widget.ImageButton;
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
	private int num=1;
	private String bigMusicStrings[]	= new String[] {
		"C","C#","D","Eb","E","F","F#","G","Ab","A","Bb","B"
		};
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
	private float size=50;
	private SeekBar seekBar;
    int[] menu_image_array = { R.drawable.ic_menu_delete,
            R.drawable.ic_menu_save,
            R.drawable.ic_menu_preferences,
            R.drawable.ic_menu_help,
            R.drawable.ic_menu_info_details,
            R.drawable.ic_menu_favorite
            };
    /** 菜单文字 **/
    String[] menu_name_array = { "清空", "保存", "设置", "帮助", "详细","更多" };
    AlertDialog menuDialog;// menu菜单Dialog
    AlertDialog promptDialog; //提示对话框
    AlertDialog helpDialog;
    GridView menuGrid;
    View menuView;
    
    
    private AdView bannerAD;
    private int [] numcode = {0, 0, 0, 0, 0, 0};
    private SharedPreferences settings;
    private boolean hideAD=true;
    private boolean firstStart=true;
	
//	private Spinner spinner2;
    
    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static float dip2px(Context context, float dpValue) {
    	final float scale = context.getResources().getDisplayMetrics().density;
    	return  (dpValue * scale + 0.5f);
    }
    /**
    * 将sp值转换为px值，保证文字大小不变 
    *  
    * @param spValue 
    * @param fontScale 
    *            （DisplayMetrics类中属性scaledDensity） 
    * @return 
    */  
   public static float sp2px(Context context, float spValue) {  
       final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;  
       return  (spValue * fontScale + 0.5f);  
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



		defaultHight = sp2px(MainActivity.this,50);
		musicNoteLayout = new MusicNoteLayout((50+20)/2, this);//尺寸每个音符的宽度（基准尺寸为50）
		
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
		final GdtAppwall appwall = new GdtAppwall(this, "1101983001", "9007479622553201356", false);
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,bigMusicStrings);
		adapter.setDropDownViewResource(R.layout.myspinner_dropdown);
		spinner1.setAdapter(adapter);
		spinner2.setAdapter(adapter);
		spinner1.setOnItemSelectedListener(new OnItemSelectedListener() {

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
						TextView offsetTextView = (TextView) inputLayout.findViewById(i*4+1);
						TextView bigMusciStringTextView = (TextView) inputLayout.findViewById(i*4+2);
						TextView pitchUpTextView = (TextView) inputLayout.findViewById(i*4+3);
						TextView pitchDownTextView = (TextView) inputLayout.findViewById(i*4+4);
						
						offsetTextView.setText(musicNote.getoffsetString(inputBigMusicIndex));
						bigMusciStringTextView.setText(musicNote.getMusicString(inputBigMusicIndex));
						pitchUpTextView.setText(musicNote.getPitchUp(inputBigMusicIndex));
						pitchDownTextView.setText(musicNote.getPitchDown(inputBigMusicIndex));
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
				buttonToSpinner2.setText(bigMusicStrings[position] + "调");
				outputBigMusicIndex = position;
				musicNoteLayout.setOutputBigMusicIndex(position);
				ArrayList<Integer> standardNums = musicNoteLayout.getStandardNums();
				if (standardNums != null) {
					for (int i = 0; i < standardNums.size(); i++) {
						MusicNote musicNote = new MusicNote(standardNums.get(i));
						TextView offsetTextView = (TextView) outputLayout.findViewById(i*4+1);
						TextView bigMusciStringTextView = (TextView) outputLayout.findViewById(i*4+2);
						TextView pitchUpTextView = (TextView) outputLayout.findViewById(i*4+3);
						TextView pitchDownTextView = (TextView) outputLayout.findViewById(i*4+4);
						
						offsetTextView.setText(musicNote.getoffsetString(outputBigMusicIndex));
						bigMusciStringTextView.setText(musicNote.getMusicString(outputBigMusicIndex));
						pitchUpTextView.setText(musicNote.getPitchUp(outputBigMusicIndex));
						pitchDownTextView.setText(musicNote.getPitchDown(outputBigMusicIndex));
					}
//下面这句能代替上面循环这部分，但上面这个运行更流畅					
//					musicNoteLayout.update((seekBar.getProgress()+20)/2, outputBigMusicIndex);
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
		
		Button backSpacebButton =(Button) findViewById(R.id.buttonBackSpace);
		backSpacebButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int indexCursorLine = musicNoteLayout.getIndexCursorLine();
				int indexCursorRow = musicNoteLayout.getIndexCursorRow();
				int line = musicNoteLayout.getLine();
				float size = musicNoteLayout.getSize();
				
				if (!(indexCursorLine==0 && indexCursorRow==0)) {

					ArrayList<Integer> standardNums = musicNoteLayout.getStandardNums();
					
					standardNums.remove(line*indexCursorRow+indexCursorLine-1);
					musicNoteLayout.setStandardNums(standardNums);
					if (indexCursorLine==0) {
						musicNoteLayout.setIndexCursorLine(line-1);
						musicNoteLayout.setIndexCursorRow(indexCursorRow-1);
						musicNoteLayout.setX((line-1)*26*size+dip2px(MainActivity.this, 1));
						musicNoteLayout.setY(musicNoteLayout.getY()-31*size);
					}else {
						musicNoteLayout.setIndexCursorLine(indexCursorLine-1);
						musicNoteLayout.setX(musicNoteLayout.getX()-26*size);
					}
					musicNoteLayout.drawCursor();
					
					inputLayout.removeView(inputLayout.findViewById((line*indexCursorRow+indexCursorLine-1)*4+1));
					inputLayout.removeView(inputLayout.findViewById((line*indexCursorRow+indexCursorLine-1)*4+2));
					inputLayout.removeView(inputLayout.findViewById((line*indexCursorRow+indexCursorLine-1)*4+3));
					inputLayout.removeView(inputLayout.findViewById((line*indexCursorRow+indexCursorLine-1)*4+4));

					outputLayout.removeView(outputLayout.findViewById((line*indexCursorRow+indexCursorLine-1)*4+1));
					outputLayout.removeView(outputLayout.findViewById((line*indexCursorRow+indexCursorLine-1)*4+2));
					outputLayout.removeView(outputLayout.findViewById((line*indexCursorRow+indexCursorLine-1)*4+3));
					outputLayout.removeView(outputLayout.findViewById((line*indexCursorRow+indexCursorLine-1)*4+4));
					
					TextView textView;
					for (int i = line*indexCursorRow+indexCursorLine-1; i < standardNums.size(); i++) {
						for (int j = 1; j < 5; j++) {
							textView = (TextView) inputLayout.findViewById((i+1)*4+j);
							if (textView.getX()<=26*size) {
								textView.setX((line-1)*26*size+textView.getX());
								textView.setY(textView.getY()-31*size);
							}else {
								textView.setX(textView.getX()-26*size);
							}
							textView.setId(i*4+j);
						}
					}
					for (int i = line*indexCursorRow+indexCursorLine-1; i < standardNums.size(); i++) {
						for (int j = 1; j < 5; j++) {
							textView = (TextView) outputLayout.findViewById((i+1)*4+j);
							if (textView.getX()<=26*size) {
								textView.setX((line-1)*26*size+textView.getX());
								textView.setY(textView.getY()-31*size);
							}else {
								textView.setX(textView.getX()-26*size);
							}
							textView.setId(i*4+j);
						}
					}
			}
			}
		});
		
		
		menuView = View.inflate(this, R.layout.my_menu, null);
        // 创建AlertDialog
        menuDialog = new AlertDialog.Builder(this).create();
        //设置透明度
        Window window = menuDialog.getWindow();  
        WindowManager.LayoutParams lp = window.getAttributes();  
        lp.alpha = 0.7f;  
        window.setAttributes(lp);
        
        menuDialog.setView(menuView);
        menuDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            public boolean onKey(DialogInterface dialog, int keyCode,
                    KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_MENU)// 监听按键
                    dialog.dismiss();
                return false;
            }
        });

        menuGrid = (GridView) menuView.findViewById(R.id.gridview);
        menuGrid.setAdapter(getMenuAdapter(menu_name_array, menu_image_array));
        /** 监听menu选项 **/
        menuGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                    long arg3) {
            	menuDialog.hide();
                switch (arg2) {
                case 0:// 清空
                	promptDialog.show();
 //               	musicNoteLayout.cleanAll();
                    break;
                case 1:// 保存
                	Toast.makeText(MainActivity.this, "抱歉，暂不支持保存功能！", Toast.LENGTH_SHORT).show();
                    break;
                case 2:// 设置
//                	musicNoteLayout.update(40,4);
                	Toast.makeText(MainActivity.this, "您可以在主界面上设置音符尺寸与模式", Toast.LENGTH_SHORT).show();
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
                	appwall.doShowAppWall();
                	break;
                }
                
                
            }
        });
		
        //下面是提示信息的对话框
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
        //下面是帮助对话框
        AlertDialog.Builder builder_help = new Builder(this);
        builder_help.setTitle("帮助信息！");
		builder_help.setMessage(
				"1. 在左上角选择输入简谱的调号\n2. 使用软键盘输入音符\n3. 选择不同的调号即可完成转调\n");
		
		builder_help.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
			}
		});
        helpDialog = builder_help.create();

        

        seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

				musicNoteLayout.update((seekBar.getProgress()+20)/2, outputBigMusicIndex);
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				textView7.setText(seekBar.getProgress()+"%");
			}
		});
        
        
        toggleButton1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (!isChecked) {
					inputLayout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 0, 14.0f));
                	outputLayout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 0, 0.0f));
                	buttonLayout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 0, 7.0f));
//                	extraLayout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 0, 1.0f));
				}else {
					inputLayout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 0, 7.0f));
                	outputLayout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 0, 7.0f));
                	buttonLayout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 0, 7.0f));
//                	extraLayout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 0, 1.0f));
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
                R.layout.my_menu_items, new String[] { "itemImage", "itemText" },
                new int[] { R.id.item_image, R.id.item_text });
        return simperAdapter;
    }
	//两次返回退出
	private long exitTime = 0;
	@Override 
	public boolean onKeyDown(int keyCode, KeyEvent event) { 
	if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){ 
	if((System.currentTimeMillis()-exitTime) > 2000){ 
	Toast.makeText(getApplicationContext(), "再次点击“返回”退出", Toast.LENGTH_SHORT).show(); 
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
				Toast.makeText(getApplicationContext(), "pitch或offset错误", 0).show();
				break;
			}
		}
	};
	
	public void numClick (View view) {
		switch (Integer.parseInt((String)view.getTag())) {
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
//		if (num==7 && pitch==1 && offset==1) {
//			Toast.makeText(MainActivity.this, "请正确输入...", 0).show();
//		}else if (num==1 && pitch==-1 && offset==-1) {
//			
//			Toast.makeText(MainActivity.this, "请正确输入...", 0).show();
//		}else 
		int row = (int) ((inputLayout.getHeight()-defaultHight)/(31*musicNoteLayout.getSize()));
		if( musicNoteLayout.getStandardNums().size()>= musicNoteLayout.getLine()*row)
//		if( musicNoteLayout.getX()>=inputLayout.getWidth()-2*26*size 
//				&& musicNoteLayout.getY() >= inputLayout.getHeight()-2*31*size )	
		{
//			Toast.makeText(MainActivity.this, "窗口已满！", Toast.LENGTH_SHORT).show();
//			System.out.println("line"+musicNoteLayout.getInputLayout().getHeight());
		}else
		{
//			MusicNote musicNote = new MusicNote(2, 3, 0, 0);//输入的调、数字、升降、高低音 
//			spinner2.setY(musicNoteLayout.getWindowHeight()/3);
			
			MusicNote musicNote = new MusicNote(inputBigMusicIndex, num, offset, pitch);
			musicNoteLayout.drawMusicNoteOutput(musicNote, outputBigMusicIndex);//输出的调
			musicNoteLayout.drawMusicNoteInput(musicNote, inputBigMusicIndex);//输入的调
			
			if (musicNoteLayout.getStandardNums().size()>1) {
				
			
				int indexCursorLine = musicNoteLayout.getIndexCursorLine();
				int indexCursorRow = musicNoteLayout.getIndexCursorRow();
				int line = musicNoteLayout.getLine();
				float size = musicNoteLayout.getSize();
					
					
					TextView textView;
					for (int i = musicNoteLayout.getStandardNums().size()-1; i >=line*indexCursorRow+indexCursorLine; i--) {
						for (int j = 1; j < 5; j++) {
							textView = (TextView) inputLayout.findViewById((i-1)*4+j);
							if (textView.getX()>=26*size*(line-1)) {
								textView.setX(textView.getX()-26*size*(line-1));
								textView.setY(textView.getY()+31*size);
							}else {
								textView.setX(textView.getX()+26*size);
							}
							textView.setId(i*4+j);
						}
					}
					for (int i = musicNoteLayout.getStandardNums().size()-1; i >=line*indexCursorRow+indexCursorLine; i--) {
						for (int j = 1; j < 5; j++) {
							textView = (TextView) outputLayout.findViewById((i-1)*4+j);
							if (textView.getX()>=26*size*(line-1)) {
								textView.setX(textView.getX()-26*size*(line-1));
								textView.setY(textView.getY()+31*size);
							}else {
								textView.setX(textView.getX()+26*size);
							}
							textView.setId(i*4+j);
						}
					}	
			}
			numcode[0]=numcode[1];numcode[1]=numcode[2];numcode[2]=numcode[3];
			numcode[3]=numcode[4];numcode[4]=numcode[5];numcode[5]=num;
			int [] password = {4,2,5,4,2,5};
			if (numcode[0]==4 && numcode[1]==2 &&numcode[2]==5 &&numcode[3]==4 &&numcode[4]==2 &&numcode[5]==5 && firstStart == false){
					
				
					SharedPreferences.Editor prefEditor = settings.edit();
					hideAD = !settings.getBoolean("hideAD", false);
					prefEditor.putBoolean("hideAD", hideAD);			
					prefEditor.apply();
					musicNoteLayout.cleanAll();
					if (!hideAD) {
						Toast.makeText(MainActivity.this, "添加广告成功！", Toast.LENGTH_SHORT).show();
						showBannerAD();
					}else {
						Toast.makeText(MainActivity.this, "解除广告成功！", Toast.LENGTH_SHORT).show();
						extraLayout.removeAllViews();
					}
				
			}
		}

	}


	/* (non-Javadoc)
	 * @see android.app.Activity#onTouchEvent(android.view.MotionEvent)
	 */
	
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		
		int [] position = new int[2];
		inputLayout.getLocationInWindow(position);
		float x = event.getX()-position[0];
		float y = event.getY()-position[1];
//		System.out.println("x:"+x+"y:"+y);
		int tempLine = -1;
		int tempRow = -1;
		int standardNumsSize = musicNoteLayout.getStandardNums().size();
		int line = musicNoteLayout.getLine();
		float size = musicNoteLayout.getSize();
		if (y<=position[1]+inputLayout.getHeight()) {

//			for (int i = 1; i <= 2*line+3; i+=2) {
//				if (x<=i*13*size) {
//					tempLine=i/2;
//					if (i==(2*line+3)) {
//						tempLine=line;
//					}
//					break;
//				}
//			}
//			for (int i = 0; i < 10; i++) {
//				if (y>=(defaultHight+i*51*size) && y<=(i+1)*51*size+defaultHight) {
//					tempRow=i;
//					break;
//				}
//			}
			
			tempRow = (int) ((y-defaultHight)/(31*size));
			tempLine = (int) ((x+13*size)/(26*size));
			
			if ((tempLine+tempRow*line) > standardNumsSize) {	
				tempRow=standardNumsSize/line;
				tempLine=standardNumsSize-tempRow*line;	
			}
			if (tempLine>=0 && tempRow>=0 ) {
				musicNoteLayout.setIndexCursorLine(tempLine);
				musicNoteLayout.setIndexCursorRow(tempRow);
				musicNoteLayout.drawCursor();
				musicNoteLayout.setX(tempLine*26*size+dip2px(MainActivity.this, 1));
				musicNoteLayout.setY(tempRow*31*size+defaultHight);
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
		bannerAD = new AdView(this, AdSize.BANNER, "1101983001", "9079537216591129292");
		
		AdRequest adRequest = new AdRequest();
		adRequest.setTestAd(false);
		adRequest.setRefresh(31);
		adRequest.setShowCloseBtn(false);
		
		extraLayout.removeAllViews();
		extraLayout.addView(bannerAD);
		bannerAD.fetchAd(new AdRequest());
	}
	
}


class MusicNote {
	private int standardNum;
	private boolean isSpace=false;
							//    1 2 3 4 5 6 7 
	private int [] bigMusicNum = {0,2,4,5,7,9,11};//全全半全全全半  这里数字以半音为一个单位,数组里是相差
	private String bigMusicStrings[]	= new String[] {
			"1","1","2","3","3","4","4","5","6","6","7","7"
			};
	private String offsetStrings[]	= new String[] {
			" ","#"," ","b"," "," ","#"," ","b"," ","b"," "
			};
	public MusicNote(int inputBigMusicIndex, int num, int offset, int pitch) {
	//inputBigMusicIndex为该大调在数组中的位置(0-11) 	num为简谱的数字(1-7)
	//offset为-1 0 1 代表升降调       pitch为-1 0 1 代表低音 中音 高音
		if (num == 0) {
			isSpace = true;
			standardNum=-100;
		}else {
			this.standardNum=inputBigMusicIndex + bigMusicNum[num-1] + offset + 12*pitch;
		}
	//得出的标准数的范围为：-13  ---    35
	}//2+4   0+6
	
	public MusicNote(int standardNum) {
		this.standardNum=standardNum;
		if (standardNum == -100) {
			isSpace = true;
		}
	}

	String getPitchUp(int outputBigMusicIndex){
		if (isSpace) {
			return " ";
		}else {
			return standardNum-outputBigMusicIndex<0 ? " " :standardNum-outputBigMusicIndex<12 ? " " : "●";
		}
	}
	String getPitchDown(int outputBigMusicIndex){
		if (isSpace) {
			return " ";
		}else {
			return standardNum-outputBigMusicIndex<0 ? "●" :standardNum-outputBigMusicIndex<12 ? " " : " ";
		}
	}
	int getPitch(int outputBigMusicIndex){
		if (isSpace) {
			return 0;
		}else {
			return standardNum-outputBigMusicIndex<0 ? -1 :standardNum-outputBigMusicIndex<12 ? 0 : 1;
		}
	}
	
	String getMusicString(int outputBigMusicIndex){
		if (isSpace) {
			return " ";
		}else {
			int num = standardNum-outputBigMusicIndex-12*this.getPitch(outputBigMusicIndex);
			if (num<0 || num>11) {
				return "?";
			}else {
				return bigMusicStrings[num];
			}
		}
	}	
	String getoffsetString(int outputBigMusicIndex){
		if (isSpace) {
			return " ";
		}else {
			int num = standardNum-outputBigMusicIndex-12*this.getPitch(outputBigMusicIndex);
			if (num<0 || num>11) {
				return " ";
			}else {
				return offsetStrings[num];
			}
		}
	}	
	
	int getStandardNum(){
			return standardNum;
	}
}

class MusicNoteLayout{
	private int indexCursorLine=0;
	private int indexCursorRow=0;
	private int row;
	private int line;
	private float size;
	private Activity activity;
	private int windowHeight;
	private int windowWidth;
	private float x;
	private float y;
	private boolean isCursorExisted=false;
	private int inputBigMusicIndex = 0;
	private int outputBigMusicIndex = 0;
	private float defaultHight;
	private ArrayList<Integer> standardNums = new ArrayList<Integer>();
	public MusicNoteLayout(float size, Activity activity) {

		this.size = dip2px(activity, size/50);//得到的size是相对于50px的倍数
		this.activity = activity;
		
		Display display = this.activity.getWindowManager().getDefaultDisplay();
		Point phoneSize = new Point();
		display.getSize(phoneSize);
		windowHeight = phoneSize.y;
		windowWidth = phoneSize.x;
		x=dip2px(activity, 1);
		defaultHight = sp2px(activity, 50);
		y=defaultHight;
		
		line = (int) ((windowWidth)/(26*this.size));
	}
	
	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static float dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return  (dpValue * scale + 0.5f);
	}
    /** 将sp值转换为px值，保证文字大小不变 
    *  
    * @param spValue 
    * @param fontScale 
    *            （DisplayMetrics类中属性scaledDensity） 
    * @return 
    */  
	
   public static float sp2px(Context context, float spValue) {  
       final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;  
       return  (spValue * fontScale + 0.5f);  
   } 
   
   /** 将px值转换为sp值，保证文字大小不变 
   *  
   * @param pxValue 
   * @param fontScale 
   *            （DisplayMetrics类中属性scaledDensity） 
   * @return 
   */  
  public static int px2sp(Context context, float pxValue) {  
      final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;  
      return (int) (pxValue / fontScale + 0.5f);  
  } 
	ArrayList<Integer> getStandardNums(){
		return standardNums;
	}
	void setStandardNums(ArrayList<Integer> standardNums){
		this.standardNums=standardNums;
	}
	int getIndexCursorLine(){
		return indexCursorLine;
	}
	void setIndexCursorLine(int indexCursorLine){
		this.indexCursorLine = indexCursorLine;
	}
	int getIndexCursorRow(){
		return indexCursorRow;
	}
	void setIndexCursorRow(int indexCursorRow){
		this.indexCursorRow = indexCursorRow;
	}
	float getSize(){
		return size;
	}
	int getWindowHeight(){
		return windowHeight;
	}
	int getWindowWidth(){
		return windowWidth;
	}
	void setSize(int size){
		this.size=size;
	}
	void setX(float x){
		this.x=x;
	}
	int getLine(){
		return line;
	}
	int getRow(){
		return row;
	}
	void setLine(int line){
		this.line=line;
	}
	void setRow(int row){
		this.row=row;
	}
	void setY(float y){
		this.y=y;
	}
	float getX(){
		return x;
	}
	float getY(){
		return y;
	}
	
	public int getInputBigMusicIndex() {
		return inputBigMusicIndex;
	}

	public void setInputBigMusicIndex(int inputBigMusicIndex) {
		this.inputBigMusicIndex = inputBigMusicIndex;
	}

	public int getOutputBigMusicIndex() {
		return outputBigMusicIndex;
	}

	public void setOutputBigMusicIndex(int outputBigMusicIndex) {
		this.outputBigMusicIndex = outputBigMusicIndex;
	}
	RelativeLayout getInputLayout(){
		return (RelativeLayout) activity.findViewById(R.id.inputLayout);
	}
	RelativeLayout getOutputLayout(){
		return (RelativeLayout) activity.findViewById(R.id.outputLayout);
	}
	
	void drawMusicNoteInput(MusicNote musicNote, int outputBigMusicIndex){
		
		int inputColor = 0x0000ff00;
//		int inputColor = 0x77ffffff;
		int numColor = 0xff001111;
		RelativeLayout layout = (RelativeLayout) activity.findViewById(R.id.inputLayout);
		
		TextView offsetTextView = new TextView(activity);
		offsetTextView.setId((indexCursorLine+indexCursorRow*line)*4+1);
		offsetTextView.setX(x);
		offsetTextView.setY(y);
		offsetTextView.setWidth((int)(10*size));
		offsetTextView.setHeight((int)(30*size));
		offsetTextView.setTextSize(px2sp(activity, 8*size));
		if (musicNote.getoffsetString(outputBigMusicIndex) == "#") {
			offsetTextView.setTextSize((int)(px2sp(activity, 8*size)));
		}
		offsetTextView.setText(musicNote.getoffsetString(outputBigMusicIndex));
		offsetTextView.setBackgroundColor(inputColor);
		offsetTextView.setTextColor(Color.BLUE);
		offsetTextView.setGravity(Gravity.CENTER);
		layout.addView(offsetTextView);
		
		
		TextView pitchUpTextView = new TextView(activity);
		pitchUpTextView.setId((indexCursorLine+indexCursorRow*line)*4+3);
		pitchUpTextView.setX(x+10*size);
		pitchUpTextView.setY(y);
		pitchUpTextView.setWidth((int)(15*size));
		pitchUpTextView.setHeight((int)(10*size));
		pitchUpTextView.setTextSize(px2sp(activity, 4*size));
		pitchUpTextView.setBackgroundColor(inputColor);
		pitchUpTextView.setGravity(Gravity.CENTER);
		pitchUpTextView.setText(musicNote.getPitchUp(outputBigMusicIndex));
		layout.addView(pitchUpTextView);
		
		TextView pitchDownTextView = new TextView(activity);
		pitchDownTextView.setId((indexCursorLine+indexCursorRow*line)*4+4);
		pitchDownTextView.setX(x+10*size);
		pitchDownTextView.setY(y+20*size);
		pitchDownTextView.setWidth((int)(15*size));
		pitchDownTextView.setHeight((int)(10*size));
		pitchDownTextView.setTextSize(px2sp(activity, 4*size));
		pitchDownTextView.setBackgroundColor(inputColor);
		pitchDownTextView.setGravity(Gravity.CENTER);
		pitchDownTextView.setText(musicNote.getPitchDown(outputBigMusicIndex));
		layout.addView(pitchDownTextView);
		
		TextView bigMusciStringTextView = new TextView(activity);
		bigMusciStringTextView.setId((indexCursorLine+indexCursorRow*line)*4+2);
		bigMusciStringTextView.setX(x+10*size);
		bigMusciStringTextView.setY(y);
		bigMusciStringTextView.setWidth((int)(15*size));
		bigMusciStringTextView.setHeight((int)(30*size));
		bigMusciStringTextView.setTextSize(px2sp(activity, 16*size));
		bigMusciStringTextView.setText(musicNote.getMusicString(outputBigMusicIndex));
		bigMusciStringTextView.setBackgroundColor(inputColor);
		bigMusciStringTextView.setGravity(Gravity.CENTER);
		bigMusciStringTextView.setTextColor(numColor);
		layout.addView(bigMusciStringTextView);
		
		standardNums.add(line*indexCursorRow+indexCursorLine, musicNote.getStandardNum());
		indexCursorLine = indexCursorLine + 1;
		x = x + (25+1)*size;
		if (indexCursorLine%line==0) {
			x=dip2px(activity, 1);
			y=y+(30+1)*size;
			indexCursorLine = 0;
			indexCursorRow= indexCursorRow + 1;
		}	
		drawCursor();
	}
	
	void drawMusicNoteOutput(MusicNote musicNote, int outputBigMusicIndex){
		
//		y=y+windowHeight/3;
		int outputColor = 0x0000ff00;
		int numColor = 0xff191970;
		RelativeLayout layout = (RelativeLayout) activity.findViewById(R.id.outputLayout);
		TextView offsetTextView = new TextView(activity);
		offsetTextView.setId((indexCursorLine+indexCursorRow*line)*4+1);
		offsetTextView.setX(x);
		offsetTextView.setY(y);
		offsetTextView.setWidth((int)(10*size));
		offsetTextView.setHeight((int)(30*size));
		offsetTextView.setTextSize(px2sp(activity, 8*size));
		if (musicNote.getoffsetString(outputBigMusicIndex) == "#") {
			offsetTextView.setTextSize((int)(px2sp(activity, 8*size)));
		}
		offsetTextView.setText(musicNote.getoffsetString(outputBigMusicIndex));
		offsetTextView.setBackgroundColor(outputColor);
		offsetTextView.setTextColor(Color.BLUE);
		offsetTextView.setGravity(Gravity.CENTER);
		layout.addView(offsetTextView);
		
		
		TextView pitchUpTextView = new TextView(activity);
		pitchUpTextView.setId((indexCursorLine+indexCursorRow*line)*4+3);
		pitchUpTextView.setX(x+10*size);
		pitchUpTextView.setY(y);
		pitchUpTextView.setWidth((int)(15*size));
		pitchUpTextView.setHeight((int)(10*size));
		pitchUpTextView.setTextSize(px2sp(activity, 4*size));
		pitchUpTextView.setBackgroundColor(outputColor);
		pitchUpTextView.setGravity(Gravity.CENTER);
		pitchUpTextView.setText(musicNote.getPitchUp(outputBigMusicIndex));
		layout.addView(pitchUpTextView);
		
		TextView pitchDownTextView = new TextView(activity);
		pitchDownTextView.setId((indexCursorLine+indexCursorRow*line)*4+4);
		pitchDownTextView.setX(x+10*size);
		pitchDownTextView.setY(y+20*size);
		pitchDownTextView.setWidth((int)(15*size));
		pitchDownTextView.setHeight((int)(10*size));
		pitchDownTextView.setTextSize(px2sp(activity, 4*size));
		pitchDownTextView.setBackgroundColor(outputColor);
		pitchDownTextView.setGravity(Gravity.CENTER);
		pitchDownTextView.setText(musicNote.getPitchDown(outputBigMusicIndex));
		layout.addView(pitchDownTextView);
		
		TextView bigMusciStringTextView = new TextView(activity);
		bigMusciStringTextView.setId((indexCursorLine+indexCursorRow*line)*4+2);
		bigMusciStringTextView.setX(x+10*size);
		bigMusciStringTextView.setY(y);
		bigMusciStringTextView.setWidth((int)(15*size));
		bigMusciStringTextView.setHeight((int)(30*size));
		bigMusciStringTextView.setTextSize(px2sp(activity, 16*size));
		bigMusciStringTextView.setText(musicNote.getMusicString(outputBigMusicIndex));
		bigMusciStringTextView.setBackgroundColor(outputColor);
		bigMusciStringTextView.setGravity(Gravity.CENTER);
		bigMusciStringTextView.setTextColor(numColor);
		layout.addView(bigMusciStringTextView);
		
//		y=y-windowHeight/3;
	}
	
	void drawCursor(){
		RelativeLayout layout = (RelativeLayout) activity.findViewById(R.id.inputLayout);
		if (isCursorExisted==true) {
			TextView textView = (TextView) layout.findViewById(123456);
			layout.removeView(textView);
		}
		TextView cursorTextView = new TextView(activity);
		cursorTextView.setId(123456);
		cursorTextView.setX(indexCursorLine*26*size);
		cursorTextView.setY(indexCursorRow*31*size+defaultHight);
		cursorTextView.setWidth((int)(2*size));
		cursorTextView.setHeight((int)(30*size));
		cursorTextView.setBackgroundColor(Color.BLUE);
//		cursorTextView.setText("●●●");
//		cursorTextView.setTextSize(2*size);
		cursorTextView.setTextColor(Color.BLUE);
		layout.addView(cursorTextView);
		isCursorExisted=true;
	}
	
	void cleanAll(){//其实也不是完全clean了all
		cleanView();
		indexCursorLine=0;
		indexCursorRow=0;
		x=dip2px(activity, 1);
		y=defaultHight;
		standardNums = new ArrayList<Integer>();
		drawCursor();
	}
	void cleanView(){
		RelativeLayout outputLayout = (RelativeLayout) activity.findViewById(R.id.outputLayout);
		RelativeLayout inputLayout = (RelativeLayout) activity.findViewById(R.id.inputLayout);
		View view1 = activity.findViewById(R.id.spinner1);
		View view2 = activity.findViewById(R.id.buttonToSpinner1);
		View view3 = activity.findViewById(R.id.textView3);
		View view4 = activity.findViewById(R.id.spinner2);
		View view5 = activity.findViewById(R.id.buttonToSpinner2);
		View view6 = activity.findViewById(R.id.textView5);
		View view7 = activity.findViewById(R.id.seekBar1);
		View view8 = activity.findViewById(R.id.textView7);
		View view9 = activity.findViewById(R.id.toggleButton1);
		
		
		inputLayout.removeAllViews();
		inputLayout.addView(view1);
		inputLayout.addView(view3);
		inputLayout.addView(view2);
		inputLayout.addView(view9);
		inputLayout.addView(view8);
		inputLayout.addView(view7);

		outputLayout.removeAllViews();
		outputLayout.addView(view4);
		outputLayout.addView(view6);
		outputLayout.addView(view5);
		
	}
	void update(float size, int outputBigMusicIndex){
		ArrayList<Integer> tempArrayList = standardNums;
		cleanAll();
		line = (int) ((windowWidth)/(26*dip2px(activity, size/50)));
		this.size = dip2px(activity, size/50);//更新size
		this.outputBigMusicIndex = outputBigMusicIndex;
		for (int i = 0; i < tempArrayList.size(); i++) {
			MusicNote musicNote = new MusicNote(tempArrayList.get(i));
			drawMusicNoteOutput(musicNote, outputBigMusicIndex);//输出的调
			drawMusicNoteInput(musicNote, inputBigMusicIndex);//输入的调
		}
		
	}
}


