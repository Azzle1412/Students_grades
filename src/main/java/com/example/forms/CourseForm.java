package com.example.forms;
import lombok.Data;

import java.math.BigInteger;

@Data
public class CourseForm {

    private BigInteger teacherId;
    private String teacherName;
    private String className; // 班级
    private String courseName;// 课程

}
