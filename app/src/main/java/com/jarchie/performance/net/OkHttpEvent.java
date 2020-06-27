package com.jarchie.performance.net;

public class OkHttpEvent {
    public long dnsStartTime; //dns开始时间
    public long dnsEndTime; //dns结束时间
    public long responseBodySize; //网络请求返回值大小
    public boolean apiSuccess; //网络请求是否成功
    public String errorReason; //请求失败的具体原因
}
