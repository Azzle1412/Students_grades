package com.example.controllers;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.utils.MyPair;
import com.example.forms.LoginForm;
import com.example.pojos.Manager;
import com.example.pojos.Student;
import com.example.pojos.Teacher;
import com.example.services.ManagerService;
import com.example.services.StudentService;
import com.example.services.TeacherService;
import com.example.vos.result.Result;
import com.example.vos.result.ResultEnum;
import com.example.vos.StudentVo;
import com.example.vos.TeacherVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.math.BigInteger;


@Slf4j
@RestController
@RequestMapping("/system")
public class LoginController {

    @Autowired
    private ManagerService managerService;
    @Autowired
    private TeacherService teacherService;
    @Autowired
    private StudentService studentService;



    @PostMapping("/login")
    public Result login(@RequestBody LoginForm loginForm){

        BigInteger id = loginForm.getId();
        String password = loginForm.getPassword();
        Integer type = loginForm.getType();

        if(type == 1){
            MyPair<Manager> pair = checkManager(id,password);
            if(pair.getFlag() == true) {
                return Result.success(ResultEnum.SUCCESS_LOGIN,null);
            }
            return Result.fail(ResultEnum.ERROR_LOGIN);
        }
        else if(type == 2){
            MyPair<Teacher> pair = checkTeacher(id,password);
            if(pair.getFlag() == true) {
                Teacher teacher = pair.getObj();
                TeacherVo teacherVo =new TeacherVo();
                BeanUtils.copyProperties(teacher,teacherVo);
                return Result.success(ResultEnum.SUCCESS_LOGIN,teacherVo);
            }
            return Result.fail(ResultEnum.ERROR_LOGIN);
        }else if(type == 3){
            MyPair<Student> pair = checkStudent(id,password);
            if(pair.getFlag() == true) {
                Student student = pair.getObj();
                StudentVo studentVo =new StudentVo();
                BeanUtils.copyProperties(student,studentVo);
                return Result.success(ResultEnum.SUCCESS_LOGIN,studentVo);
            }
            return Result.fail(ResultEnum.ERROR_LOGIN);
        }

        return Result.fail(ResultEnum.ERROR_LOGIN);
    }

    private MyPair checkManager(BigInteger managerId,String password){

        Manager manager = managerService.getById(managerId);

        MyPair<Manager> pair = new MyPair<>(false,null);
        if(manager != null && password.equals(manager.getPassword())){
            pair.setFlag(true);
            pair.setObj(manager);
            return pair;
        }

        return pair;

    }

    private MyPair checkTeacher(BigInteger teacherId,String password){

        Teacher teacher = teacherService.getById(teacherId);

        MyPair<Teacher> pair = new MyPair<>(false,null);
        if(teacher != null && password.equals(teacher.getPassword())){
            pair.setFlag(true);
            pair.setObj(teacher);
            return pair;
        }

        return pair;

    }

    private MyPair checkStudent(BigInteger studentId,String password) {

        Student student = studentService.getById(studentId);

        MyPair<Student> pair = new MyPair<>(false,null);
        if(student != null && password.equals(student.getPassword())){
            pair.setFlag(true);
            pair.setObj(student);
            return pair;
        }

        return pair;

    }

}
