package com.autel.sdksample.utils;

public abstract class Singleton<T> {
    private volatile T mInstance;
    public Singleton(){}
    protected abstract T create();
    public final T get(){
        if(this.mInstance == null){
            synchronized (this){
                if(this.mInstance == null){
                    this.mInstance = this.create();
                }
            }
        }
        return this.mInstance;
    }
}
