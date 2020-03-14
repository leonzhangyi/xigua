package com.water.melon.ui.home.h5.utils;

import android.util.Log;
import android.webkit.WebView;

import com.just.agentweb.WebChromeClient;
import com.water.melon.utils.LogUtil;

/**
 * @author cenxiaozhong
 * @date 2019/2/19
 * @since 1.0.0
 */
public class CommonWebChromeClient extends WebChromeClient {
	@Override
	public void onProgressChanged(WebView view, int newProgress) {
		  super.onProgressChanged(view, newProgress);
		Log.i("CommonWebChromeClient", "onProgressChanged:" + newProgress + "  view:" + view);
	}
	@Override
	public void onReceivedTitle(WebView view, String title) {
		LogUtil.e("webview","title = "+title);
		super.onReceivedTitle(view, title);
		if (title != null) {
//			titleView.setCenterText(title);
		}
	}
}
