package com.jarchie.performance.adapter;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jarchie.performance.R;
import com.jarchie.performance.bean.FeedBean;
import com.jarchie.performance.utils.LaunchTime;

import java.util.List;

/**
 * 作者: 乔布奇
 * 日期: 2020-05-17 10:25
 * 邮箱: jarchie520@gmail.com
 * 描述: 列表适配器
 */
public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.FeedHolder> {
    private Context mContext;
    private List<FeedBean.DataBean.DatasBean> mList;
    private boolean mHasRecorded;
    private OnFeedShowCallBack mCallBack;

    public FeedAdapter(Context context, List<FeedBean.DataBean.DatasBean> list) {
        this.mContext = context;
        this.mList = list;
    }

    //点击事件的回调
    private OnItemClickListener onItemClickListener;

    //设置回调监听
    public void setOnItem1ClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public void setList(List<FeedBean.DataBean.DatasBean> items) {
        this.mList = items;
        notifyDataSetChanged();
    }

    public void setOnFeedShowCallBack(OnFeedShowCallBack callBack){
        this.mCallBack = callBack;
    }

    @NonNull
    @Override
    public FeedHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FeedHolder(LayoutInflater.from(mContext).inflate(R.layout.item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final FeedHolder holder, int position) {
        if (position ==0 && !mHasRecorded){
            mHasRecorded = true;
            holder.mAllLayout.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    holder.mAllLayout.getViewTreeObserver().removeOnPreDrawListener(this);
                    LaunchTime.endRecord("FirstShow");
                    if (mCallBack!=null){
                        mCallBack.onFeedShow();
                    }
                    return true;
                }
            });
        }

        final FeedBean.DataBean.DatasBean bean = mList.get(position);
        holder.mTitle.setText(Html.fromHtml(TextUtils.isEmpty(bean.getTitle()) ? "暂无" : bean.getTitle())); //标题
        holder.mSource.setText(TextUtils.isEmpty(
                bean.getSuperChapterName()) ? "暂无" : bean.getSuperChapterName()); //来源
        holder.mLink.setText(TextUtils.isEmpty(bean.getLink()) ? "地址：暂无" : "地址：" + bean.getLink());
        holder.itemView.setOnClickListener(new View.OnClickListener() { //删除
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    int pos = holder.getLayoutPosition();
                    onItemClickListener.onItemClick(holder.itemView, pos);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    class FeedHolder extends RecyclerView.ViewHolder {

        final TextView mTitle;
        final TextView mSource;
        final TextView mLink;
        final LinearLayout mAllLayout;

        FeedHolder(@NonNull View itemView) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.mTitle);
            mSource = itemView.findViewById(R.id.mSource);
            mLink = itemView.findViewById(R.id.mLink);
            mAllLayout = itemView.findViewById(R.id.mAllLayout);
        }
    }

}
