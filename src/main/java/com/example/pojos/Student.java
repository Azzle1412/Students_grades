package com.example.pojos;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigInteger;

@Data
@TableName("student_table")
public class Student {

    @TableId
    public BigInteger studentId;
    private String studentName;
    private String password;
    private String sex;
    private String subject; // 专业
    private String className; // 班级
    private String gpa;

}
