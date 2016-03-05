package com.jiangqideng.myhamonicahelper.listener;

import java.util.List;

import android.content.Context;


public interface Button1Listener {

	abstract Context getContext();
	
	abstract void handleWifiTick(long progress, long max);

}
