package com.first.basket.adapter

import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.first.basket.R
import com.first.basket.activity.MainActivity
import com.first.basket.app.SampleApplicationLike
import com.first.basket.bean.AddressBean
import com.first.basket.bean.ProductBean
import com.first.basket.common.CommonMethod
import com.first.basket.common.CommonMethod1
import com.first.basket.common.StaticValue
import com.first.basket.utils.ImageUtils
import com.first.basket.utils.SPUtil
import com.first.basket.utils.ToastUtil
import com.first.basket.view.AmountView
import com.google.gson.GsonBuilder
import com.yanzhenjie.recyclerview.swipe.SwipeMenuAdapter
import org.jetbrains.anko.sdk25.coroutines.onClick


/**
 * Created by hanshaobo on 14/09/2017.
 */
class ShopAdapter(context: MainActivity, list: ArrayList<ProductBean>, listener: OnItemClickListener, cbListener: OnItemCheckedListener, amountListener: OnItemAmountClickListener) : SwipeMenuAdapter<ShopAdapter.ViewHolder>() {
    private var context = context
    private var mDatas: ArrayList<ProductBean> = list
    private var listener = listener
    private var cbListener = cbListener
    private var amountListener = amountListener
    var isModifyMode = false
    private var mFooterView: View? = null
    private val TYPE_NORMAL = 0
    private val TYPE_FOOTER = 2

    fun addFooterView(footer: View?) {
        mFooterView = footer
        notifyItemInserted(itemCount - 1)
    }

    override fun getItemCount(): Int {
        if (mFooterView == null) {
            return mDatas.size
        } else {
            return mDatas.size + 1
        }
    }

    override fun onCreateContentView(parent: ViewGroup, viewType: Int): View? {
        return LayoutInflater.from(parent.context).inflate(R.layout.item_recycler_shop, parent, false);
    }

    override fun onCompatCreateViewHolder(realContentView: View, viewType: Int): ViewHolder? {
        return ViewHolder(realContentView)
    }

    override fun onBindViewHolder(holder: ShopAdapter.ViewHolder, position: Int) {
        if (getItemViewType(position) == TYPE_NORMAL) {

        } else if (getItemViewType(position) == TYPE_FOOTER) {

        }

        holder.itemView.tag = mDatas[position]

        val product = mDatas[position]

        holder.tvName1.text = product.productname

        holder.tvUnit1.text = product.weight + "/" + product.unit
        holder.tvPrice1.text = CommonMethod1.showPrice(product)
        holder.amoutView.amount = product.amount
        ImageUtils.showImg(SampleApplicationLike.getInstance().application, product.img, holder.ivGoods)
        holder.cbSelect.isChecked = product.isCheck
        holder.cbSelect.setOnCheckedChangeListener { compoundButton, b ->
            product.isCheck = b
            cbListener.onItemCheck(compoundButton, b, position)
        }
        holder.amoutView.setOnAmountClickListener(object : AmountView.OnAmountClickListener {
            override fun onAmountAddClick(view: View, amount: Int) {
                if(!CommonMethod.isTrue(product.promboolean)){
                    amountListener.OnItemAmountAddClick(holder.amoutView, amount, position)
                }else{
                    ToastUtil.showToast(context.getString(R.string.one_oneday))
                }
            }

            override fun onAmountSubClick(view: View, amount: Int) {
                amountListener.OnItemAmountSubClick(holder.amoutView, amount, position)
            }

        })
        holder.itemView.onClick { holder.cbSelect.performClick() }

        if (position == 0) {
            holder.tvTitle.visibility = View.VISIBLE
            setTitle(holder.tvTitle, mDatas[0].channelid)
        } else {
            if (mDatas.get(position - 1).channelid == mDatas[position].channelid) {
                //相同，不展示
                holder.tvTitle.visibility = View.GONE
            } else {
                holder.tvTitle.visibility = View.VISIBLE
                setTitle(holder.tvTitle, mDatas[position].channelid)
            }
        }
        if (isModifyMode) {
            holder.llShadow.visibility = View.GONE
        } else {
            checkStatus(holder, position)
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    private fun checkStatus(holder: ViewHolder, position: Int) {
        //判断菜市是否可选
        val str = SPUtil.getString(StaticValue.DEFAULT_ADDRESS, "")
        if (!TextUtils.isEmpty(str)) {
            val gson = GsonBuilder().create()
            val addressInfo = gson.fromJson(str, AddressBean::class.java)

            when {
                mDatas[position].channelid == "1" -> holder.llShadow.visibility = if (CommonMethod.isTrue(addressInfo.issqcs)) (View.GONE) else (View.VISIBLE)
                mDatas[position].channelid == "2" -> holder.llShadow.visibility = if (CommonMethod.isTrue(addressInfo.isshcs)) (View.GONE) else (View.VISIBLE)
                mDatas[position].channelid == "3" -> holder.llShadow.visibility = if (CommonMethod.isTrue(addressInfo.isqgcs)) (View.GONE) else (View.VISIBLE)
            }
        }

    }

    fun setTitle(tv: TextView, str: String) {
        when (str) {
            "1" -> tv.text = "社区菜市"
            "2" -> tv.text = "上海菜市"
            "3" -> tv.text = "全国菜市"
        }
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.onClick { listener.onItemClick(itemView) }
        }


        var tvTitle = itemView.findViewById<TextView>(R.id.tvTitle)
        var tvName1 = itemView.findViewById<TextView>(R.id.tvName1)
        var tvUnit1 = itemView.findViewById<TextView>(R.id.tvUnit1)
        var tvPrice1 = itemView.findViewById<TextView>(R.id.tvPrice1)
        var amoutView = itemView.findViewById<AmountView>(R.id.amoutView)!!
        var ivGoods = itemView.findViewById<ImageView>(R.id.ivGoods)
        var cbSelect = itemView.findViewById<CheckBox>(R.id.cbSelect)
        var llShadow = itemView.findViewById<LinearLayout>(R.id.llShadow)


    }

    interface OnItemClickListener {
        fun onItemClick(view: View)
    }

    interface OnItemCheckedListener {
        fun onItemCheck(view: View, b: Boolean, index: Int)
    }

    interface OnItemAmountClickListener {
        fun OnItemAmountAddClick(view: View, amount: Int, position: Int)
        fun OnItemAmountSubClick(view: View, amount: Int, position: Int)
    }

    fun setIsModifyMode(b: Boolean) {
        isModifyMode = b
        notifyDataSetChanged()
    }
}