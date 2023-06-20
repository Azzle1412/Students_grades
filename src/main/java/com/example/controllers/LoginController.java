package com.example.controllers;
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
/*登录模块*/
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


        if(type == 1){ // 管理员
            MyPair<Boolean,Manager> pair = checkManager(id,password);
            if(pair.getObj1() == true) {
                return Result.success(ResultEnum.SUCCESS_LOGIN,"这是管理员");
            }
            return Result.fail(ResultEnum.ERROR_LOGIN,"账号或者密码错误");
        }
        else if(type == 2){ // 教师
            MyPair<Boolean,Teacher> pair = checkTeacher(id,password);
            if(pair.getObj1() == true) {
                Teacher teacher = pair.getObj2();
                TeacherVo teacherVo =new TeacherVo();
                BeanUtils.copyProperties(teacher,teacherVo);
                return Result.success(ResultEnum.SUCCESS_LOGIN,teacherVo);
            }
            return Result.fail(ResultEnum.ERROR_LOGIN,"工号或者密码错误");
        }
        else if(type == 3){ // 学生
            MyPair<Boolean,Student> pair = checkStudent(id,password);
            if(pair.getObj1() == true) {
                Student student = pair.getObj2();
                StudentVo studentVo =new StudentVo();
                BeanUtils.copyProperties(student,studentVo);
                return Result.success(ResultEnum.SUCCESS_LOGIN,studentVo);
            }
            return Result.fail(ResultEnum.ERROR_LOGIN,"学号或者密码错误");
        }

        return Result.fail(ResultEnum.ERROR_LOGIN,"未知的登录失败");
    }

    private MyPair checkManager(BigInteger managerId,String password){

        Manager manager = managerService.getById(managerId);

        MyPair<Boolean,Manager> pair = new MyPair<>(false,null);
        if(manager != null && password.equals(manager.getPassword())){
            pair.setObj1(true);
            pair.setObj2(manager);
        }

        return pair;
    }

    private MyPair checkTeacher(BigInteger teacherId,String password){

        Teacher teacher = teacherService.getById(teacherId);

        MyPair<Boolean,Teacher> pair = new MyPair<>(false,null);
        if(teacher != null && password.equals(teacher.getPassword())){
            pair.setObj1(true);
            pair.setObj2(teacher);
        }

        return pair;

    }

    private MyPair checkStudent(BigInteger studentId,String password) {

        Student student = studentService.getById(studentId);

        MyPair<Boolean,Student> pair = new MyPair<>(false,null);
        if(student != null && password.equals(student.getPassword())){
            pair.setObj1(true);
            pair.setObj2(student);
        }

        return pair;

    }

}
