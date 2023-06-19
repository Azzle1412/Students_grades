package com.example.utils;

public class MyPair<T> {

    private boolean flag;
    private T obj;



    public MyPair() {}

    public MyPair(boolean flag, T obj) {
        this.flag = flag;
        this.obj = obj;
    }

    public boolean getFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public T getObj() {
        return obj;
    }

    public void setObj(T obj) {
        this.obj = obj;
    }
}
