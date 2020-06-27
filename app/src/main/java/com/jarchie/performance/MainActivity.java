package com.jarchie.performance;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.asynclayoutinflater.view.AsyncLayoutInflater;
import androidx.core.view.LayoutInflaterCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Choreographer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.jarchie.performance.adapter.FeedAdapter;
import com.jarchie.performance.adapter.OnFeedShowCallBack;
import com.jarchie.performance.adapter.OnItemClickListener;
import com.jarchie.performance.bean.FeedBean;
import com.jarchie.performance.constants.Constant;
import com.jarchie.performance.net.RetrofitManager;
import com.jarchie.performance.utils.GsonUtils;
import com.jarchie.performance.utils.LaunchTime;
import com.jarchie.performance.utils.LogUtils;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.zhangyue.we.x2c.X2C;
import com.zhangyue.we.x2c.ano.Xml;

import java.io.IOException;
import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import top.zibin.luban.Luban;

/**
 * 作者: 乔布奇
 * 日期: 2020-05-17 10:00
 * 邮箱: jarchie520@gmail.com
 * 描述: Demo主页
 */
//@Xml(layouts = "activity_main")
public class MainActivity extends AppCompatActivity implements OnFeedShowCallBack {
    private RecyclerView mRecycler;
    private FeedAdapter mAdapter;
    private List<FeedBean.DataBean.DatasBean> mList;
    //演示获取FPS
    private long mStartFrameTime = 0;
    private int mFrameCount = 0;
    private static final long MONITOR_INTERVAL = 160L; //单次计算FPS使用160毫秒
    private static final long MONITOR_INTERVAL_NANOS = MONITOR_INTERVAL * 1000L * 1000L;
    private static final long MAX_INTERVAL = 1000L; //设置计算fps的单位时间间隔1000ms,即fps/s;


    @SuppressLint({"CheckResult", "InlinedApi"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        new Thread(){
//            @Override
//            public void run() {
//                super.run();
//                synchronized (MainActivity.this){
//                    try {
//                        Thread.sleep(20000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }.start();

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                Log.i("FirstShow","MSG");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        new AsyncLayoutInflater(this).inflate(R.layout.activity_main, null, new AsyncLayoutInflater.OnInflateFinishedListener() {
            @Override
            public void onInflateFinished(@NonNull View view, int resid, @Nullable ViewGroup parent) {
                setContentView(view);
                mRecycler = findViewById(R.id.mRecycler);
                mRecycler.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                mRecycler.addItemDecoration(new DividerItemDecoration(MainActivity.this, DividerItemDecoration.VERTICAL));
                mRecycler.setAdapter(mAdapter);
                mAdapter.setOnFeedShowCallBack(MainActivity.this);
            }
        });
        super.onCreate(savedInstanceState);
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.requestEach(android.Manifest.permission.CAMERA,
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_WIFI_STATE,
                android.Manifest.permission.ACCESS_NETWORK_STATE,
                android.Manifest.permission.CHANGE_WIFI_STATE,
                android.Manifest.permission.READ_PHONE_STATE,
                android.Manifest.permission.SYSTEM_ALERT_WINDOW)
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {
                        if (permission.granted) {
                            // 用户已经同意该权限
                            //Log.d(TAG, permission.name + " is granted.");
                        } else if (permission.shouldShowRequestPermissionRationale) {
                            // 用户拒绝了该权限，没有选中『不再询问』（Never ask again）,那么下次再次启动时，还会提示请求权限的对话框
                            // Log.d(TAG, permission.name + " is denied. More info should be provided.");
                        } else {
                            // 用户拒绝了该权限，并且选中『不再询问』
                            // Log.d(TAG, permission.name + " is denied.");
                        }

                    }
                });
//        X2C.setContentView(this,R.layout.activity_main);
        mAdapter = new FeedAdapter(this, mList);
        initData();
        mAdapter.setOnItem1ClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Luban.with(MainActivity.this)
                        .load(Environment.getExternalStorageDirectory().getPath()+"/Android/luban/yingbao.png")
                        .setTargetDir(Environment.getExternalStorageDirectory().getPath()+"/Android/luban")
                        .launch();
            }
        });
//        getFPS();
//        synchronized (MainActivity.this){
//            Toast.makeText(this,"锁冲突问题",Toast.LENGTH_SHORT).show();
//        }
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void getFPS() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            return;
        }
        Choreographer.getInstance().postFrameCallback(new Choreographer.FrameCallback() {
            @Override
            public void doFrame(long frameTimeNanos) {
                if (mStartFrameTime == 0) {
                    mStartFrameTime = frameTimeNanos;
                }
                long interval = frameTimeNanos - mStartFrameTime;
                if (interval > MONITOR_INTERVAL_NANOS) {
                    double fps = (((double) (mFrameCount * 1000L * 1000L)) / interval) * MAX_INTERVAL;
                    Log.i("FPS","--->"+fps);
                    mFrameCount = 0;
                    mStartFrameTime = 0;
                } else {
                    ++mFrameCount;
                }

                Choreographer.getInstance().postFrameCallback(this);
            }
        });
    }

    //        LayoutInflaterCompat.setFactory2(getLayoutInflater(), new LayoutInflater.Factory2() {
//            @Override
//            public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
//                long time = System.currentTimeMillis();
//                View view = getDelegate().createView(parent, name, context, attrs);
//                Log.i(name,"控件耗时：" + (System.currentTimeMillis() - time));
//                return view;
//            }
//
//            @Override
//            public View onCreateView(String name, Context context, AttributeSet attrs) {
//                return null;
//            }
//        });

    //初始化数据
    @SuppressLint("NewApi")
    private void initData() {
        RetrofitManager.getApiService().getArticleList()
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            if (response.body()!=null){
                                String json = response.body().string();
                                FeedBean bean = GsonUtils.fromJson(json,FeedBean.class);
                                if (bean.getData()!=null){
                                    if (bean.getData().getDatas()!=null && bean.getData().getDatas().size()>0){
                                        mList = bean.getData().getDatas();
                                        mAdapter.setList(mList);
                                    }
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onFeedShow() {
        //模拟执行了两个Task，TaskA和TaskB
//        new DispatchRunnable(new DelayInitTaskA()).run();
//        new DispatchRunnable(new DelayInitTaskB()).run();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
//        LaunchTime.endRecord("onWindowFocusChanged");
    }

}
