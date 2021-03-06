package com.first.basket.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.first.basket.R
import com.first.basket.activity.OrderDetailActivity
import com.first.basket.activity.PayChooseActivity
import com.first.basket.base.BaseRecyclerAdapter
import com.first.basket.base.HttpResult
import com.first.basket.bean.OrderListBean
import com.first.basket.common.CommonMethod
import com.first.basket.common.StaticValue
import com.first.basket.http.HttpMethods
import com.first.basket.http.HttpResultSubscriber
import com.first.basket.http.TransformUtils
import com.first.basket.utils.ImageUtils
import com.first.basket.utils.LogUtils
import com.first.basket.utils.SPUtil
import com.first.basket.utils.ToastUtil
import kotlinx.android.synthetic.main.fragment_order.*
import kotlinx.android.synthetic.main.item_recycler_order.view.*
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.sdk25.coroutines.onClick

class OrderFragment : BaseFragment() {
    private var mPosition = "0"
    //3待支付；4待发货；5待收货；6已完成；7订单超时未支付
    private var mDatas = ArrayList<OrderListBean.DataBean>()
    private lateinit var mAdapter: BaseRecyclerAdapter<OrderListBean.DataBean, BaseRecyclerAdapter.ViewHolder<OrderListBean.DataBean>>

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_order, container, false)!!
        var bundle = arguments
        if (bundle != null) {
            mPosition = bundle.getString("position")
            LogUtils.d("mPosition:" + mPosition)
        }
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        initData()
    }

    private fun initData() {
        mAdapter = BaseRecyclerAdapter(R.layout.item_recycler_order, mDatas) { view: View, item: OrderListBean.DataBean ->
            view.tvNO.text = resources.getString(R.string.order_number, item.strorderid)
            view.tvCost.text = resources.getString(R.string.order_price, item.qty, item.pay)
            when (item.statusid) {
                "3" -> {
                    view.btStatus.text = "立即支付"
                    view.btStatus.backgroundColor = activity.resources.getColor(R.color.colorMain)
                    view.btStatus.onClick {
                        var intent = Intent(activity, PayChooseActivity::class.java)
                        intent.putExtra("strorderid", item.strorderid)
                        intent.putExtra("price", java.lang.Float.valueOf(item.pay))
                        startActivity(intent)
                    }
                }
                "4" -> {
                    view.btStatus.text = "已支付"
                    view.btStatus.backgroundColor = activity.resources.getColor(R.color.gray99)
                    view.btStatus.onClick { }
                }
            }
            if (item.orderdetail != null) {
                view.llImg.removeAllViews()
                for (i in 0 until item.orderdetail.size) {
                    if (i < 4) {
                        var iv = ImageView(activity)
                        var params = ViewGroup.MarginLayoutParams(CommonMethod.dip2px(activity, 60f), CommonMethod.dip2px(activity, 60f))
                        params.setMargins(8, 8, 8, 8)
                        iv.layoutParams = params
                        ImageUtils.showImg(activity, item.orderdetail[i].img, iv)
                        view.llImg.addView(iv)
                    }
                }
            }
            view.llRoot.onClick {
//                var intent = Intent(activity, OrderDetailActivity::class.java)
//                intent.putExtra("orderBean",item)
//                startActivity(intent)
            }
        }
        recyclerView.adapter = mAdapter

        getOrderList()
    }


    private fun getOrderList() {
        HttpMethods.createService()
                .getOrderList("get_orderlist", SPUtil.getString(StaticValue.USER_ID, ""))
                .compose(TransformUtils.defaultSchedulers())
                .subscribe(object : HttpResultSubscriber<HttpResult<OrderListBean>>() {
                    override fun onNext(t: HttpResult<OrderListBean>) {
                        super.onNext(t)
                        if (t.status == 0) {
                            setData(t.result.data)
                        } else {
                            ToastUtil.showToast(t.info)
                        }
                    }
                })
    }


    private fun setData(data: List<OrderListBean.DataBean>) {
        var finalData = ArrayList<OrderListBean.DataBean>()
        when (mPosition) {
            "0" ->
                finalData.addAll(data)
            "1" ->
                data.filter { it.statusid == "3" }.forEach {
                    finalData.add(it)
                }
            "2" ->
                data.filter { it.statusid == "4" }.forEach {
                    finalData.add(it)
                }
            "3" ->
                data.filter { it.statusid == "5" }.forEach {
                    finalData.add(it)
                }
        }
        mDatas.clear()
        mDatas.addAll(finalData)
        mAdapter.notifyDataSetChanged()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (Activity.RESULT_OK == resultCode) {
            //刷新
            getOrderList()
        }
    }
}