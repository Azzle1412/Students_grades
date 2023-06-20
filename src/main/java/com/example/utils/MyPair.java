package com.example.utils;
import lombok.Data;

@Data
public class MyPair<T,K> {

    private T obj1;
    private K obj2;

    public MyPair() {}

    public MyPair(T obj1, K obj2) {
        this.obj1 = obj1;
        this.obj2 = obj2;
    }

}
