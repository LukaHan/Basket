package com.first.basket.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.first.basket.R
import com.first.basket.app.BaseApplication
import com.first.basket.bean.ProductBean
import com.first.basket.utils.ImageUtils
import com.first.basket.view.AmountView
import com.yanzhenjie.recyclerview.swipe.SwipeMenuAdapter
import org.jetbrains.anko.sdk25.coroutines.onClick

/**
 * Created by hanshaobo on 14/09/2017.
 */
class MenuAdapter(list: ArrayList<ProductBean>, listener: OnItemClickListener, cbListener: CompoundButton.OnCheckedChangeListener) : SwipeMenuAdapter<MenuAdapter.ViewHolder>() {
    private var mDatas: ArrayList<ProductBean> = list
    private var listener = listener
    private var cbListener = cbListener

    override fun getItemCount(): Int {
        return mDatas.size
    }

    override fun onCreateContentView(parent: ViewGroup, viewType: Int): View? {
        return LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_shop, parent, false);
    }

    override fun onCompatCreateViewHolder(realContentView: View, viewType: Int): ViewHolder? {
        return ViewHolder(realContentView)
    }

    override fun onBindViewHolder(holder: MenuAdapter.ViewHolder, position: Int) {
        holder.itemView.tag = mDatas[position]

        val product = mDatas[position]

        holder.tvName1.text = product.productname

        holder.tvUnit1.text = product.weight + "/" + product.unit
        holder.tvPrice1.text = product.price
        holder.amoutView.amount = product.amount
        ImageUtils.showImg(BaseApplication.getInstance(), product.img, holder.ivGoods)
        holder.cbSelect.isChecked = product.isCheck
        holder.cbSelect.setOnCheckedChangeListener { compoundButton, b ->
            product.isCheck = b
            cbListener.onCheckedChanged(compoundButton, b)
        }

    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvName1 = itemView.findViewById<TextView>(R.id.tvName1)
        var tvUnit1 = itemView.findViewById<TextView>(R.id.tvUnit1)
        var tvPrice1 = itemView.findViewById<TextView>(R.id.tvPrice1)
        var amoutView = itemView.findViewById<AmountView>(R.id.amoutView)
        var ivGoods = itemView.findViewById<ImageView>(R.id.ivGoods)
        var cbSelect = itemView.findViewById<CheckBox>(R.id.cbSelect)

        init {
            itemView.onClick { listener.onItemClick(itemView) }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(view: View)
    }
}