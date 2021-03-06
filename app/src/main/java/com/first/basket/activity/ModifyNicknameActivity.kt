package com.first.basket.activity

import android.app.Activity
import android.os.Bundle
import com.first.basket.R
import com.first.basket.base.BaseActivity
import com.first.basket.base.HttpResult
import com.first.basket.bean.LoginBean
import com.first.basket.common.CommonMethod
import com.first.basket.common.StaticValue
import com.first.basket.http.HttpMethods
import com.first.basket.http.HttpResultSubscriber
import com.first.basket.http.TransformUtils
import com.first.basket.utils.SPUtil
import com.first.basket.utils.ToastUtil
import kotlinx.android.synthetic.main.activity_modify_nickname.*
import org.jetbrains.anko.sdk25.coroutines.onClick

/**
 * Created by hanshaobo on 15/10/2017.
 */
class ModifyNicknameActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modify_nickname)

        btModify.onClick {
            var nickname = etNickname.text.toString()

            HttpMethods.createService().doModifyUsername("do_modifyusername", SPUtil.getString(StaticValue.USER_ID, ""), nickname)
                    .compose(TransformUtils.defaultSchedulers())
                    .subscribe(object : HttpResultSubscriber<HttpResult<LoginBean>>() {
                        override fun onNext(t: HttpResult<LoginBean>) {
                            super.onNext(t)
                            SPUtil.setString(StaticValue.SP_LOGIN_USERNAME, t.result.data.username)
                            ToastUtil.showToast("修改成功")
                        }

                        override fun onError(e: Throwable) {
                            super.onError(e)
                            ToastUtil.showToast("修改失败：" + e.message)
                        }

                        override fun onCompleted() {
                            super.onCompleted()
                            CommonMethod.hideKeyboard(etNickname)
                            setResult(Activity.RESULT_OK)
                            myFinish()
                        }
                    })
        }
    }
}