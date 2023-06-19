package com.example.pojos;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigInteger;

@Data
@TableName("score_table")
public class Score {

    private BigInteger studentId;
    private BigInteger teacherId;
    private String studentName;
    private String teacherName;
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
