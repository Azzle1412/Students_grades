package com.example.controllers;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.forms.ScoreForm;
import com.example.pojos.Course;
import com.example.pojos.Score;
import com.example.pojos.Student;
import com.example.pojos.Teacher;
import com.example.services.CourseService;
import com.example.services.ScoreService;
import com.example.services.StudentService;
import com.example.services.TeacherService;
import com.example.utils.MyPair;
import com.example.vos.ScoreVo;
import com.example.vos.TeacherVo;
import com.example.vos.result.Result;
import com.example.vos.result.ResultEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/system/teacher-score")
/*教师提供成绩的模块*/
public class TeacherScoreController {

    @Autowired
    private StudentService studentService;
    @Autowired
    private TeacherService teacherService;
    @Autowired
    private CourseService courseService;
    @Autowired
    private ScoreService scoreService;
    private IPage<Score> pageResult;



    // 单科加权分数的计算由前端提供
    @PostMapping("/add")
    public Result teacherScoreAdd(@RequestBody ScoreForm scoreForm
            , @RequestParam BigInteger teacherId){

        /*判断student_table是否存在*/
        QueryWrapper query = new QueryWrapper();
        query.eq("student_id",scoreForm.getStudentId());
        Student student = studentService.getOne(query);
        // 不存在
        if(student==null){
            return Result.fail(ResultEnum.ERROR_FOUND,"该学生信息不存在");
        }

        /*判断是否在course_table授予的教师的权限中*/
        List<MyPair<String,String>> class_course = new ArrayList<>();
        query.clear();
        query.eq("teacher_id",teacherId);
        List<Course> teacherList = courseService.list(query);
        for (Course course : teacherList) {
            MyPair<String,String> pair = new MyPair<>();
            pair.setObj1(course.getClassName());
            pair.setObj2(course.getCourseName());
            if(class_course.isEmpty() || !class_course.contains(pair)){
                class_course.add(pair);
            }
        }
        Score score =new Score();
        BeanUtils.copyProperties(scoreForm, score);
        MyPair<String,String> pair =new MyPair<>(score.getClassName(),score.getCourseName());
        // 没有权限
        if(!class_course.contains(pair)){
            return Result.fail(ResultEnum.ERROR_ACCESS,"没有该班级或该课程权限");
        }
        // 有权限
        score.setTeacherId(teacherId);
        query.clear();
        query.eq("teacher_id",teacherId);
        Teacher teacher = teacherService.getOne(query);
        score.setTeacherName(teacher.getTeacherName());

        /*判断在score_table中已经存在*/
        query.clear();
        query.eq("student_id",score.getStudentId());
        query.eq("teacher_id",score.getTeacherId());
        query.eq("course_name",score.getCourseName());
        Score scoreQuery = scoreService.getOne(query);
        // 存在
        if(scoreQuery != null){
            return Result.fail(ResultEnum.ERROR_SAVE,"该得分信息已经存在");
        }
        // 不存在
        boolean flag = scoreService.save(score);
        if(flag){
            ScoreVo scoreVo = new ScoreVo();
            BeanUtils.copyProperties(score,scoreVo);
            return Result.success(ResultEnum.SUCCESS_SAVE,scoreVo);
        }
        else {
            return Result.fail(ResultEnum.ERROR_SAVE,"score_table插入错误");
        }
    }

    @DeleteMapping("/delete")
    public Result teacherScoreDelete(@RequestParam BigInteger teacherId
            , @RequestParam BigInteger studentId
            , @RequestParam String courseName){

        log.info("teacherId={},studentId={},courseName={}",teacherId,studentId,courseName);
        // 判断score_table中是否存在
        QueryWrapper query = new QueryWrapper();
        query.eq("teacher_id",teacherId);
        query.eq("student_id",studentId);
        query.eq("course_name",courseName);
        Score score = scoreService.getOne(query);
        if(score == null){
            return Result.fail(ResultEnum.ERROR_FOUND,"该得分信息不存在");
        }

        boolean flag = scoreService.remove(query);
        if(flag){
            ScoreVo scoreVo = new ScoreVo();
            BeanUtils.copyProperties(score,scoreVo);
            return Result.success(ResultEnum.SUCCESS_DELETE,scoreVo);
        }
        else{
            return Result.fail(ResultEnum.ERROR_DELETE,"score_table删除失败");
        }
    }

    @PutMapping("/edit")
    public Result teacherScoreEdit(@RequestBody ScoreForm scoreForm
            ,@RequestParam BigInteger teacherId){

        Score score = new Score();
        BeanUtils.copyProperties(scoreForm,score);
        score.setTeacherId(teacherId);

        // 判断teacher_table和student_table中是否存在
        QueryWrapper query = new QueryWrapper();
        query.eq("teacher_id",teacherId);
        Teacher teacher = teacherService.getOne(query);

        query.clear();
        query.eq("student_id",score.getStudentId());
        Student student = studentService.getOne(query);

        if(teacher == null || student == null){
            return Result.fail(ResultEnum.ERROR_FOUND,"教师或者学生不存在");
        }
        score.setTeacherName(teacher.getTeacherName());


        query.clear();
        query.eq("teacher_id",teacherId);
        query.eq("student_id",score.getStudentId());
        query.eq("course_name",score.getCourseName());
        Score scoreQuery = scoreService.getOne(query);
        if(scoreQuery == null){
            return Result.fail(ResultEnum.ERROR_FOUND,"该得分信息不存在");
        }

        boolean flag = scoreService.update(score,query);
        if(flag){
            ScoreVo scoreVo = new ScoreVo();
            BeanUtils.copyProperties(score,scoreVo);
            return Result.success(ResultEnum.SUCCESS_EDIT,scoreVo);
        }
        else{
            return Result.fail(ResultEnum.ERROR_EDIT,"score_table修改失败");
        }
    }

    @GetMapping("/list/{teacherId}")
    public Result teacherScoreList(@RequestParam(defaultValue = "1") int pageIndex, @RequestParam(defaultValue = "4") int pageSize
            ,@PathVariable BigInteger teacherId,@RequestParam(defaultValue = "*") String className){

        Page<Score> page = new Page<>(pageIndex,pageSize);
        QueryWrapper query = new QueryWrapper();
        query.eq("teacher_id",teacherId);
        if(!className.equals("*")) query.eq("class_name", className);
        pageResult = scoreService.page(page,query);

        if(pageResult.getRecords() == null){
            return Result.fail(ResultEnum.ERROR_FOUND,"所教的学生成绩列表为空");
        }

        List scoreVoList = pageResult.getRecords().stream().map(score->{
            ScoreVo scoreVo = new ScoreVo();
            BeanUtils.copyProperties(score,scoreVo);
            return scoreVo;
        }).collect(Collectors.toList());

        pageResult.setRecords(scoreVoList);

        return Result.success(ResultEnum.SUCCESS_LIST,pageResult);
    }
}
