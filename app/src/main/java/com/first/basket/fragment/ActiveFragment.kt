package com.first.basket.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.first.basket.R
import kotlinx.android.synthetic.main.fragment_active.*

/**
 * Created by hanshaobo on 30/08/2017.
 */
class ActiveFragment : BaseFragment() {
    private var mDatas =  ArrayList<Boolean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_active, container, false)!!
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var settings = webview.settings
        settings.javaScriptEnabled = true
        settings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
        webview.webViewClient = MyWebViewClient()
        webview.loadUrl("http://m.jiangsutoutiao.com/activity/luckys/page/luckys/luckys.html?version=2.1.2")
        initView()
    }


    private fun initView() {

    }

    class MyWebViewClient : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
            view!!.loadUrl(url)
            return true
        }
    }
}