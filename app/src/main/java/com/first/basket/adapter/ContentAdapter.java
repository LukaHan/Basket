package com.first.basket.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.first.basket.R;
import com.first.basket.bean.ClassifyContentBean;
import com.first.basket.utils.ImageUtils;
import com.first.basket.view.AmountView;

import java.util.List;

/**
 * Created by hanshaobo on 24/09/2017.
 */

public class ContentAdapter extends RecyclerView.Adapter<ContentAdapter.MyViewHolder> {
    private Context context;
    private List<ClassifyContentBean.ResultBean.DataBean> data;
    private OnRecyclerViewItemClickListener mOnItemClickListener;
    private OnAmountChangeListener mOnAmountChangeListener;
    private MyViewHolder holder;
    private int layoutPosition;

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, ClassifyContentBean.ResultBean.DataBean data, int position);
    }

    public interface OnAmountChangeListener {
        void onAmountChange(ImageView imageView, int position);
    }

    public ContentAdapter(Context context, List<ClassifyContentBean.ResultBean.DataBean> data) {
        this.context = context;
        this.data = data;
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public void setOnAmountChangeListener(OnAmountChangeListener listener) {
        this.mOnAmountChangeListener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = View.inflate(context, R.layout.item_recycler_content, null);
        holder = new MyViewHolder(itemView);
        return holder;
    }

    @Override

    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.tvName.setText(data.get(position).getProductname());
        holder.tvUnit.setText(data.get(position).getUnit());
        holder.tvPrice.setText(data.get(position).getPrice());
        holder.amountView.setGoods_storage(10);
        ImageUtils.showImg(context, data.get(position).getImg(), holder.ivGoods);

        holder.itemView.setTag(data.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //获取当前点击的位置
                layoutPosition = holder.getLayoutPosition();
                notifyDataSetChanged();
                mOnItemClickListener.onItemClick(holder.itemView, (ClassifyContentBean.ResultBean.DataBean) holder.itemView.getTag(), layoutPosition);
            }
        });

//        holder.amountView.setOnAmountChangeListener(new AmountView.OnAmountChangeListener() {
//            @Override
//            public void onAmountChange(View view, int amount) {
//
//                mOnAmountChangeListener.onAmountChange(view, amount);
//            }
//        });

        //更改状态
//        if (position == layoutPosition) {
//            Drawable drawable = context.getResources().getDrawable(R.drawable.category_line);
//            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
//            holder.tvCategory.setCompoundDrawables(drawable, null, null, null);
//            holder.tvCategory.setBackgroundColor(context.getResources().getColor(R.color.text_bg));
//        } else {
//            Drawable drawable = context.getResources().getDrawable(R.drawable.category_line);
//            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
//            holder.tvCategory.setCompoundDrawables(null, null, null, null);
//            holder.tvCategory.setBackgroundColor(context.getResources().getColor(R.color.white));
//        }

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private final ImageView ivGoods;
        private final TextView tvName;
        private final TextView tvUnit;
        private final TextView tvPrice;
        private final AmountView amountView;

        MyViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvUnit = itemView.findViewById(R.id.tvUnit);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            amountView = itemView.findViewById(R.id.amoutView);
            ivGoods = itemView.findViewById(R.id.ivGoods);
        }
    }
}
