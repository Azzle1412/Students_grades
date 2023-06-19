package com.example.pojos;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.context.annotation.Primary;

import java.math.BigInteger;
import java.security.Principal;

@Data
@TableName("course_table")
public class Course {

    @TableId
    private BigInteger teacherId;
    private String teacherName;
    private String className;
    private String courseName;

}
