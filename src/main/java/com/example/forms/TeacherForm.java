package com.example.forms;
import lombok.Data;
import java.math.BigInteger;

@Data
public class TeacherForm {

    private BigInteger teacherId;
    private String teacherName;
    private String password;
    private String sex;
    private String phone;

}
