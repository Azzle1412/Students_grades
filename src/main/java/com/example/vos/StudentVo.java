package com.example.vos;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigInteger;

@Data
public class StudentVo {

    public BigInteger studentId;
    private String studentName;
    /*private String password;*/
    private String sex;
    private String subject; // 专业
    private String className; // 班级
    private String gpa;
    private Integer monitor; // 是否是班长 0:不是,1:是

}
