package com.example.forms;
import lombok.Data;

import java.math.BigInteger;

@Data
public class ScoreForm {

    private BigInteger studentId;
    /*private BigInteger teacherId;*/
    private String studentName;
    /*private String teacherName;*/
    private String term;
    private String courseName;
    private String className;
    private Integer credit;
    private Integer normalScore;
    private Integer normalPercent;
    private Integer examScore;
    private Integer examPercent;
    private Integer score;

}
