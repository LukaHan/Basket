package com.first.basket.base

import android.app.ProgressDialog
import android.content.Intent
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import com.first.basket.rxjava.RxjavaUtil
import com.first.basket.rxjava.UITask

/**
 * Created by hanshaobo on 30/08/2017.
 */
open class BaseActivity1 : AppCompatActivity() {


    private lateinit var mProgressDialog: ProgressDialog

    fun replaceContent(fragment: Fragment, fragmentResId: Int) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        transaction.replace(fragmentResId, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    fun switchContent(from: Fragment, to: Fragment, id: Int) {
        if (from !== to) {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            if (!to.isAdded) {
                transaction.hide(from).add(id, to).commitAllowingStateLoss()
            } else {
                transaction.hide(from).show(to).commitAllowingStateLoss()
            }
        }
    }

//    fun myStartActivity(className: Class) {
//        var intent = Intent(this, className)
//        startActivity(intent)
//    }


    fun showProgressDialog(message: String) {
        RxjavaUtil.doInUIThread(object : UITask<Any>() {
            override fun doInUIThread() {
                if (mProgressDialog == null) {
                    mProgressDialog = ProgressDialog(this@BaseActivity1)
                    mProgressDialog.setCanceledOnTouchOutside(false)
                }
                mProgressDialog.setMessage(message)
                mProgressDialog.show()
            }
        })
    }


    fun hideProgress() {
        RxjavaUtil.doInUIThread(object : UITask<Any>() {
            override fun doInUIThread() {
                if (mProgressDialog != null && mProgressDialog.isShowing) {
                    mProgressDialog.hide()
                }
            }
        })
    }

    fun myStartActivity(cls: Class<*>, needFinish: Boolean) {
        val intent = Intent(this, cls)
        startActivity(intent)
        if (needFinish) this.finish()
    }
}