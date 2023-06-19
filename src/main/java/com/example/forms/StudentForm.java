package com.example.forms;
import lombok.Data;
import java.math.BigInteger;

@Data
public class StudentForm {

    public BigInteger studentId;
    private String studentName;
    private String password;
    private String sex;
    private String subject; // 专业
    private String className; // 班级
    private String gpa;

}
