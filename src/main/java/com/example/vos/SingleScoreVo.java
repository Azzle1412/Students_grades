package com.example.vos;
import lombok.Data;

@Data
public class SingleScoreVo {

    private String teacherName;
    private String term;
    private String courseName;
    private Integer credit;
    private Integer normalScore;
    private Integer normalPercent;
    private Integer examScore;
    private Integer examPercent;
    private Integer score;

}
