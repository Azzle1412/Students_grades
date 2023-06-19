package com.example.pojos;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigInteger;

@Data
@TableName("teacher_table")
public class Teacher {

    @TableId
    private BigInteger teacherId;
    private String teacherName;
    private String password;
    private String sex;
    private String phone;

}
