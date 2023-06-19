package com.example.utils;
import lombok.Data;

@Data
public class MyScore {

    private String courseName;
    private Integer score;



    public MyScore() {}

    public MyScore(String courseName, Integer score) {
        this.courseName = courseName;
        this.score = score;
    }

}
