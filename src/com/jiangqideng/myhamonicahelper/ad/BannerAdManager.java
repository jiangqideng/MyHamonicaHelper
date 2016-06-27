package com.jiangqideng.myhamonicahelper.ad;

import com.jiangqideng.myhamonicahelper.ui.MainActivity;
import com.qq.e.ads.AdRequest;
import com.qq.e.ads.AdSize;
import com.qq.e.ads.AdView;

/**
 * @author jiangqideng@163.com
 * @date 2016-6-27 下午1:39:57
 * @description 广告条的显示
 */
public class BannerAdManager {
	private AdView bannerAD;
	
	public void showBannerAD(MainActivity activity) {
		bannerAD = new AdView(activity, AdSize.BANNER, "1101983001",
				"9079537216591129292");
		AdRequest adRequest = new AdRequest();
		adRequest.setTestAd(false);
		adRequest.setRefresh(31);
		adRequest.setShowCloseBtn(false);
		activity.extraLayout.removeAllViews();
		activity.extraLayout.addView(bannerAD);
		bannerAD.fetchAd(new AdRequest());
	}
}
