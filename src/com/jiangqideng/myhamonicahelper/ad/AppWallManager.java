package com.jiangqideng.myhamonicahelper.ad;

import com.jiangqideng.myhamonicahelper.ui.MainActivity;
import com.qq.e.appwall.GdtAppwall;

/**
 * @author jiangqideng@163.com
 * @date 2016-6-27 下午1:37:53
 * @description 广告墙的显示
 */
public class AppWallManager {
	public GdtAppwall appwall;

	public void creatAppWall(MainActivity activity) {
		appwall = new GdtAppwall(activity, "1101983001", "9007479622553201356",
				false);
	}

	public void show() {
		appwall.doShowAppWall();
	}
}
