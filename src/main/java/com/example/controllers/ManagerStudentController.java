package com.example.controllers;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.forms.StudentForm;
import com.example.pojos.Score;
import com.example.pojos.Student;
import com.example.services.ScoreService;
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
/*管理员管理学生信息的模块*/
public class ManagerStudentController {

    @Autowired
    private StudentService studentService;
    @Autowired
    private ScoreService scoreService;
    private IPage<Student> pageResult;



    // 绩点的计算由管理员提供
    @PostMapping("/add")
    public Result studentAdd(@RequestBody StudentForm studentForm){

        Student student = new Student();
        BeanUtils.copyProperties(studentForm,student);

        BigInteger studentId = student.getStudentId();

        Student studentQuery = studentService.getById(studentId);

        if(studentQuery == null){
            boolean flag = studentService.save(student);
            if(flag){
                StudentVo studentVo = new StudentVo();
                BeanUtils.copyProperties(student,studentVo);
                return Result.success(ResultEnum.SUCCESS_SAVE,studentVo);
            }
            else{
                return Result.fail(ResultEnum.ERROR_DELETE,"student_table插入失败");
            }
        }

        return Result.fail(ResultEnum.ERROR_SAVE,"该学生信息已经存在");
    }

    @DeleteMapping("/delete/{studentId}")
    public Result studentDelete(@PathVariable BigInteger studentId){

        Student student = studentService.getById(studentId);

        if(student != null){
            boolean flag = studentService.removeById(studentId);
            if(flag){
                StudentVo studentVo = new StudentVo();
                BeanUtils.copyProperties(student,studentVo);
                return Result.success(ResultEnum.SUCCESS_DELETE,studentVo);
            }
            else {
                return Result.fail(ResultEnum.ERROR_DELETE,"student_table删除失败");
            }
        }

        return Result.fail(ResultEnum.ERROR_DELETE,"该学生信息不存在");
    }

    @PutMapping("/edit")
    public Result studentEdit(@RequestBody StudentForm studentForm){

        Student student = new Student();
        BeanUtils.copyProperties(studentForm,student);

        boolean flag = studentService.updateById(student);
        if(flag){
            StudentVo studentVo = new StudentVo();
            BeanUtils.copyProperties(student,studentVo);
            return Result.success(ResultEnum.SUCCESS_EDIT,studentVo);
        }
        else{
            return Result.fail(ResultEnum.ERROR_EDIT,"student_table更新失败");
        }
    }

    @GetMapping("/detail/{studentId}")
    public Result studentDetailById(@PathVariable BigInteger studentId){

        Student student = studentService.getById(studentId);

        if(student == null){
            return Result.fail(ResultEnum.ERROR_FOUND,"该学生信息不存在");
        }

        StudentVo studentVo = new StudentVo();
        BeanUtils.copyProperties(student,studentVo);

        return Result.success(ResultEnum.SUCCESS_FOUND,studentVo);
    }

    @GetMapping("/list")
    public Result  studentList(@RequestParam(defaultValue = "1") int pageIndex,@RequestParam(defaultValue = "4") int pageSize
            ,@RequestParam(defaultValue = "-1") BigInteger studentId,@RequestParam(defaultValue = "*") String studentName){

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
            return Result.fail(ResultEnum.ERROR_FOUND,"学生信息列表为空");
        }

        List studentVoList = pageResult.getRecords().stream().map(student->{
            StudentVo studentVo = new StudentVo();
            BeanUtils.copyProperties(student,studentVo);
            return studentVo;
        }).collect(Collectors.toList());

        pageResult.setRecords(studentVoList);

        return Result.success(ResultEnum.SUCCESS_LIST,pageResult);
    }

    // 最好以按钮触发
    @GetMapping("/cal-gpa/{studentId}")
    public Result calGPA(@PathVariable BigInteger studentId){
        QueryWrapper query = new QueryWrapper();
        query.eq("student_id",studentId);
        List<Score> scoreList = scoreService.list(query);

        if(scoreList == null){
            return Result.fail(ResultEnum.ERROR_FOUND,"0");
        }

        Integer sumCredits = Integer.valueOf(0);
        Float sumScore = Float.valueOf(0);

        for (Score score : scoreList) {
            sumCredits += score.getCredit();
            sumScore += (toGPA(score.getScore())) * (score.getCredit().floatValue());
        }
        String gpa = Float.toString(sumCredits.floatValue()/sumScore.floatValue());
        return Result.success(ResultEnum.SUCCESS_GPA,gpa);
    }

    private Float toGPA(Integer score){
        if(score>=90){
            return 4F;
        }
        else if(score>=80) {
            return 3F;
        }
        else if(score>=70){
            return 2F;
        }
        else if(score>=60){
            return 1F;
        }
        return 0F;
    }
}
