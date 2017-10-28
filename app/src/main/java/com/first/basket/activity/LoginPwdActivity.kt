package com.first.basket.activity

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.view.View
import com.first.basket.R
import com.first.basket.base.BaseActivity
import com.first.basket.base.HttpResult
import com.first.basket.bean.CodeBean
import com.first.basket.bean.LoginBean
import com.first.basket.common.CommonMethod
import com.first.basket.common.CommonMethod1
import com.first.basket.common.StaticValue
import com.first.basket.http.HttpMethods
import com.first.basket.http.HttpResultSubscriber
import com.first.basket.http.TransformUtils
import com.first.basket.utils.*
import com.first.basket.view.TitleView
import com.github.ybq.android.spinkit.style.DoubleBounce
import kotlinx.android.synthetic.main.activity_login_pwd.*
import kotlinx.android.synthetic.main.layout_loading.*
import org.jetbrains.anko.sdk25.coroutines.onClick

/**
 * Created by hanshaobo on 15/10/2017.
 */
class LoginPwdActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_pwd)
        initData()
        initListener()
    }

    private fun initListener() {
        btLogin.onClick {
            CommonMethod.hideKeyboard(etPassword)
            CommonMethod1.showLoading(loadingView)
            Handler().postDelayed({
                doLogin(etPhone.text.toString(), "", Md5Util.getMd5Value(etPassword.text.toString()))
            }, 2000)
        }
    }

    private fun initData() {

    }


    private fun doLogin(phonenumber: String, code: String, password: String) {
        HttpMethods.createService()
                .doLogin("do_login", phonenumber, code, password)
                .compose(TransformUtils.defaultSchedulers())
                .subscribe(object : HttpResultSubscriber<HttpResult<LoginBean>>() {
                    override fun onNext(t: HttpResult<LoginBean>) {
                        super.onNext(t)
                        if (t.status == 0) {
                            LogUtils.d(t.result.data.phone)
                            ToastUtil.showToast(this@LoginPwdActivity, "登录成功")
                            SPUtil.setBoolean(StaticValue.SP_LOGIN_STATUS, true)

                            SPUtil.setString(StaticValue.SP_LOGIN_PHONE, t.result.data.phone)
                            SPUtil.setString(StaticValue.USER_ID, t.result.data.userid)
                            setResult(Activity.RESULT_OK)
                            CommonMethod.hideKeyboard(etPassword)
                        } else {
                            ToastUtil.showToast(this@LoginPwdActivity, t.info)
                        }
                    }

                    override fun onCompleted() {
                        super.onCompleted()
                        CommonMethod1.showLoading(loadingView)
                    }
                })
    }
}