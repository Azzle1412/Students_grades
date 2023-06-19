package com.example.vos;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigInteger;

@Data
public class TeacherVo {

    private BigInteger teacherId;
    private String teacherName;
    private String sex;
    private String phone;

}
