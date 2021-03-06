package com.first.basket.activity

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import butterknife.ButterKnife
import com.first.basket.R
import com.first.basket.app.SampleApplicationLike
import com.first.basket.base.BaseActivity
import com.first.basket.base.HttpResult
import com.first.basket.bean.GoodsDetailBean
import com.first.basket.common.CommonMethod
import com.first.basket.common.CommonMethod1
import com.first.basket.common.StaticValue
import com.first.basket.constants.Constants
import com.first.basket.http.HttpMethods
import com.first.basket.http.HttpResultSubscriber
import com.first.basket.http.TransformUtils
import com.first.basket.utils.ImageUtils
import com.first.basket.utils.SPUtil
import com.first.basket.utils.ToastUtil
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.layout_loading.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import q.rorbin.badgeview.QBadgeView
import java.util.*

/**
 * Created by hanshaobo on 02/09/2017.
 */
class GoodsDetailActivity : BaseActivity() {

    private var data: GoodsDetailBean.DataBean? = null
    private var images = ArrayList<GoodsDetailBean.DataBean.ImagesBean>()
    private lateinit var badgeView: QBadgeView
    private var mCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ButterKnife.bind(this)
        setContentView(R.layout.activity_detail)
        initView()
        initData()
        initListener()
    }

    private fun initView() {
        badgeView = QBadgeView(this@GoodsDetailActivity)
    }


    private fun initData() {
        mCount = SampleApplicationLike.getInstance().productsCount
        badgeView.bindTarget(tvCount).badgeNumber = mCount
        var id = intent.extras.getString("id")
        if (TextUtils.isEmpty(id)) {
            getProductDetail("", intent.extras.getString("ocr"))
        } else {
            getProductDetail(id, "")
        }
    }

    private fun initListener() {
        btAdd.onClick {
            if (data != null) {
                var product = data!!.product
                if (CommonMethod.isTrue(product.promboolean)) {
                    if ("荤" == product.promdata.promproducttype && !SPUtil.getBoolean(StaticValue.PROM_HUN, false)) {
                        CommonMethod1.addGoodToCar(ivGoods, rlRoot, ivCar, object : CommonMethod1.Companion.OnAddListener {
                            override fun onAdd() {
                                mCount++
                                tvCount.text = mCount.toString()
                                badgeView.bindTarget(tvCount).badgeNumber = mCount

                                SampleApplicationLike.getInstance().addProduct(product)
                                MainActivity.getInstance1().setCountAdd()
                                SPUtil.setBoolean(StaticValue.PROM_HUN, true)
                                SPUtil.setString(StaticValue.GET_TIME, CommonMethod.getTime(false))
                            }
                        })
                    } else if ("素" == product.promdata.promproducttype && !SPUtil.getBoolean(StaticValue.PROM_SU, false)) {
                        CommonMethod1.addGoodToCar(ivGoods, rlRoot, ivCar, object : CommonMethod1.Companion.OnAddListener {
                            override fun onAdd() {
                                mCount++
                                tvCount.text = mCount.toString()
                                badgeView.bindTarget(tvCount).badgeNumber = mCount

                                SampleApplicationLike.getInstance().addProduct(product)
                                MainActivity.getInstance1().setCountAdd()
                                SPUtil.setBoolean(StaticValue.PROM_SU, true)
                                SPUtil.setString(StaticValue.GET_TIME, CommonMethod.getTime(false))
                            }
                        })
                    } else {
                        ToastUtil.showToast(getString(R.string.one_oneday))
                    }
                } else {
                    CommonMethod1.addGoodToCar(ivGoods, rlRoot, ivCar, object : CommonMethod1.Companion.OnAddListener {
                        override fun onAdd() {
                            mCount++
                            tvCount.text = mCount.toString()
                            badgeView.bindTarget(tvCount).badgeNumber = mCount
                            SampleApplicationLike.getInstance().addProduct(data!!.product)
                            MainActivity.getInstance1().setCountAdd()
                        }
                    })
                }
            }
        }
        ivCar.onClick {
            MainActivity.getInstance1().setCurrentPage(3)
            myFinish()
        }
    }

    private fun getProductDetail(id: String?, ocr: String?) {
        showLoading(loadingView)
        HttpMethods.createService().getDetail("get_productdetailpage", id, ocr)
                .compose(TransformUtils.defaultSchedulers())
                .subscribe(object : HttpResultSubscriber<HttpResult<GoodsDetailBean>>() {
                    override fun onNext(t: HttpResult<GoodsDetailBean>) {
                        super.onNext(t)
                        if (t.status == 0) {
                            setData(t.result.data)
                        }
                    }

                    override fun onCompleted() {
                        super.onCompleted()
                        hideLoading(loadingView)
                    }
                })
    }


    private fun setData(data: GoodsDetailBean.DataBean?) {
        if (data != null) {
            this.data = data
            tvName.text = data.title
            tvDes.text = data.product.weight + "/" + data.product.unit
            tvPrice.text = getString(R.string.price, CommonMethod1.showPrice(data.product))
            tvDetail.text = data.productdetail
            //设置banner
            images.addAll(data.images)
            var imgs = ArrayList<String>()
            for (i in 0 until images.size) {
                imgs.add(Constants.BASE_IMG_URL + images[i].image)
            }

            ImageUtils.showImg(this@GoodsDetailActivity, data.images[0].image, ivGoods)
            if (CommonMethod.isTrue(data.product.promboolean)) {
                tvProm.visibility = View.VISIBLE
            }
        }
    }
}