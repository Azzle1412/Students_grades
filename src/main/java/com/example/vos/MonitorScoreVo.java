package com.example.vos;
import com.example.utils.MyScore;
import lombok.Data;
import java.math.BigInteger;
import java.util.List;

@Data
public class MonitorScoreVo {

    private BigInteger studentId;
    private String studentName;
    private String gpa;
    private List<MyScore> scoreList;



    public MonitorScoreVo() {}

    public MonitorScoreVo(BigInteger studentId, String studentName, String gpa, List<MyScore> scoreList) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.gpa = gpa;
        this.scoreList = scoreList;
    }
}
