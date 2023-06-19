package com.example.controllers;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.forms.StudentForm;
import com.example.pojos.Student;
import com.example.services.StudentService;
import com.example.vos.result.Result;
import com.example.vos.result.ResultEnum;
import com.example.vos.StudentVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/system/admin-student")
public class ManagerStudentController {

    @Autowired
    private StudentService studentService;
    private IPage<Student> pageResult;



    @PostMapping("/add")
    public Result studentAdd(@RequestBody StudentForm studentForm){

        Student student = new Student();
        BeanUtils.copyProperties(studentForm,student);
        BigInteger studentId = student.getStudentId();

        Student studentQuery = studentService.getById(studentId);

        if(studentQuery == null){
            studentService.save(student);
            return Result.success(ResultEnum.SUCCESS_SAVE,student);
        }

        return Result.fail(ResultEnum.ERROR_SAVE);
    }

    @DeleteMapping("/delete/{studentId}")
    public Result studentDelete(@PathVariable BigInteger studentId){

        Student student = studentService.getById(studentId);

        if(student != null){
            studentService.removeById(studentId);
            return Result.success(ResultEnum.SUCCESS_DELETE,student);
        }

        return Result.fail(ResultEnum.ERROR_DELETE);
    }

    @PutMapping("/edit")
    public Result studentEdit(@RequestBody StudentForm studentForm){

        Student student = new Student();
        BeanUtils.copyProperties(studentForm,student);

        boolean flag = studentService.updateById(student);
        if(flag){
            return Result.success(ResultEnum.SUCCESS_EDIT,student);
        }
        else{
            return Result.fail(ResultEnum.ERROR_EDIT);
        }
    }

    @GetMapping("/detail/{studentId}")
    public Result studentDetailById(@PathVariable BigInteger studentId){

        Student student = studentService.getById(studentId);

        if(student == null){
            return Result.fail(ResultEnum.ERROR_FOUND);
        }

        StudentVo studentVo = new StudentVo();
        BeanUtils.copyProperties(student,studentVo);

        return Result.success(ResultEnum.SUCCESS_FOUND,studentVo);
    }

    @GetMapping("/list")
    public Result  studentList(@RequestParam(defaultValue = "1") int pageIndex
            ,@RequestParam(defaultValue = "4") int pageSize
            ,@RequestParam(defaultValue = "-1") BigInteger studentId
            ,@RequestParam(defaultValue = "*") String studentName){

        Page<Student> page = new Page<>(pageIndex,pageSize);
        QueryWrapper query = new QueryWrapper();

        if(!studentId.equals(BigInteger.valueOf(-1))) {
            query.eq("student_id",studentId);
        }

        if(!studentName.equals("*")) {
            query.eq("student_name",studentName);
        }

        if(!studentId.equals(BigInteger.valueOf(-1)) || !studentName.equals("*")){
            pageResult = studentService.page(page,query);
        }
        else{
            pageResult=studentService.page(page);
        }
        if(pageResult.getRecords()==null){
            return Result.fail(ResultEnum.ERROR_FOUND);
        }

        List studentVoList = pageResult.getRecords().stream().map(student->{
            StudentVo studentVo = new StudentVo();
            BeanUtils.copyProperties(student,studentVo);
            return studentVo;
        }).collect(Collectors.toList());

        pageResult.setRecords(studentVoList);

        return Result.success(ResultEnum.SUCCESS_LIST,pageResult);
    }

}
