package com.jarchie.performance.net;


import android.content.Context;

import com.alibaba.sdk.android.httpdns.HttpDns;
import com.alibaba.sdk.android.httpdns.HttpDnsService;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;

import okhttp3.Dns;

public class OkHttpDNS implements Dns {
    //阿里云提供的HttpDns解析服务
    private HttpDnsService dnsService;

    //单例模式
    private static OkHttpDNS instance = null;

    private OkHttpDNS(Context context) {
        dnsService = HttpDns.getService(context, ""); //用户id这里演示直接写的""
    }

    public static OkHttpDNS getIns(Context context) {
        if (instance == null) {
            synchronized (OkHttpDNS.class) {
                if (instance == null) {
                    instance = new OkHttpDNS(context);
                }
            }
        }
        return instance;
    }

    @Override
    public List<InetAddress> lookup(String hostname) throws UnknownHostException {
        //优先使用阿里云dns解析服务返回的ip地址，如果为空再走系统的DNS解析服务
        String ip = dnsService.getIpByHostAsync(hostname);
        if(ip != null){ //如果不为空直接使用这个ip进行网络请求
            List<InetAddress> inetAddresses = Arrays.asList(InetAddress.getAllByName(ip));
            return inetAddresses;
        }
        return Dns.SYSTEM.lookup(hostname); //如果为空走系统的解析服务
    }
}
