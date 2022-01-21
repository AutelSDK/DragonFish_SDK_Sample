package com.autel.sdksample.dragonfish.rxrunnable;

/**
 * Created by A16343 on 2016/8/9.
 * 任务
 */
public interface RequestConfig {

    boolean isDiskCache();

    RequestConfig setDiskCache(boolean diskCache);

    void sendRequest();
}
