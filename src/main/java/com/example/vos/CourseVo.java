package com.example.vos;
import lombok.Data;
import java.math.BigInteger;
import java.security.Principal;

@Data
public class CourseVo {

    private BigInteger teacherId;
    private String teacherName;
    private String className;
    private String courseName;

}
