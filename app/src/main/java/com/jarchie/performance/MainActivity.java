package com.jarchie.performance;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.jarchie.performance.adapter.FeedAdapter;
import com.jarchie.performance.bean.FeedBean;
import com.jarchie.performance.constants.Constant;
import com.jarchie.performance.utils.LaunchTime;
import com.rxjava.rxlife.RxLife;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import rxhttp.wrapper.param.RxHttp;
/**
 * 作者: 乔布奇
 * 日期: 2020-05-17 10:00
 * 邮箱: jarchie520@gmail.com
 * 描述: Demo主页
 */
public class MainActivity extends AppCompatActivity {
    private RecyclerView mRecycler;
    private FeedAdapter mAdapter;
    private List<FeedBean.DataBean.DatasBean> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecycler = findViewById(R.id.mRecycler);
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mRecycler.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mAdapter = new FeedAdapter(this, mList);
        mRecycler.setAdapter(mAdapter);
        initData();
    }

    //初始化数据
    @SuppressLint("NewApi")
    private void initData() {
        RxHttp.get(Constant.GET_ARTICAL_LIST)
                .asObject(FeedBean.class)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> Log.e("请求", "后台请求。。。"))
                .as(RxLife.as(this))
                .subscribe(feedBean -> {
                    if (feedBean.getData().getDatas().size() > 0) {
                        mList = feedBean.getData().getDatas();
                        mAdapter.setList(mList);
                    }
                }, throwable -> Toast.makeText(MainActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show());
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        LaunchTime.endRecord("onWindowFocusChanged");
    }

}
