package com.example.utils;
import lombok.Data;
import java.math.BigInteger;

@Data
public class MyInfo {

    private BigInteger studentId;
    private String studentName;



    public MyInfo() {}

    public MyInfo(BigInteger studentId, String studentName) {
        this.studentId = studentId;
        this.studentName = studentName;
    }

}
