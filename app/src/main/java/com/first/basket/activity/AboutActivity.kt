package com.first.basket.activity

import android.os.Bundle
import com.first.basket.R
import com.first.basket.base.BaseActivity
import com.first.basket.common.CommonMethod
import kotlinx.android.synthetic.main.activity_about.*

/**
 * Created by hanshaobo on 15/10/2017.
 */
class AboutActivity :BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        tvVersion.text = "v"+CommonMethod.getVersionName()
    }
}