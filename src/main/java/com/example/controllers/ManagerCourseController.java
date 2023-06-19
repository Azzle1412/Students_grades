package com.example.controllers;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.forms.CourseForm;
import com.example.pojos.Course;
import com.example.services.CourseService;
import com.example.vos.CourseVo;
import com.example.vos.result.Result;
import com.example.vos.result.ResultEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@RestController
@RequestMapping("/system/admin-course")
public class ManagerCourseController {

    @Autowired
    private CourseService courseService;
    private IPage<Course> pageResult;



    @PostMapping("/add")
    public Result courseAdd(@RequestBody CourseForm courseForm){

        Course course = new Course();
        BeanUtils.copyProperties(courseForm,course);

        BigInteger teacherId = course.getTeacherId();
        String teacherName = course.getTeacherName();
        String courseName = course.getCourseName();
        String className = course.getClassName();

        QueryWrapper query = new QueryWrapper();
        query.eq("teacher_id",teacherId);
        query.eq("teacher_name",teacherName);
        query.eq("course_name",courseName);
        query.eq("class_name",className);
        Course courseQuery = courseService.getOne(query);

        if(courseQuery == null){
            courseService.save(course);
            CourseVo courseVo = new CourseVo();
            BeanUtils.copyProperties(course,courseVo);
            return Result.success(ResultEnum.SUCCESS_SAVE,courseVo);
        }

        return Result.fail(ResultEnum.ERROR_SAVE);
    }

    @DeleteMapping("/delete")
    public Result courseDelete(@RequestBody CourseForm courseForm){

        BigInteger teacherId = courseForm.getTeacherId();
        String teacherName = courseForm.getTeacherName();
        String courseName = courseForm.getCourseName();
        String className = courseForm.getClassName();

        QueryWrapper query = new QueryWrapper();
        query.eq("teacher_id",teacherId);
        query.eq("teacher_name",teacherName);
        query.eq("course_name",courseName);
        query.eq("class_name",className);
        Course courseQuery = courseService.getOne(query);


        if(courseQuery != null){
            courseService.remove(query);
            CourseVo courseVo = new CourseVo();
            BeanUtils.copyProperties(courseQuery,courseVo);
            return Result.success(ResultEnum.SUCCESS_DELETE,courseVo);
        }

        return Result.fail(ResultEnum.ERROR_DELETE);
    }

    // 不能更改教师的工号和姓名
    @PutMapping("/edit")
    public Result courseEdit(@RequestBody CourseForm courseForm
            ,@RequestParam String oldClassName
            ,@RequestParam String oldCourseName){

        Course course = new Course();
        BeanUtils.copyProperties(courseForm,course);

        BigInteger teacherId = course.getTeacherId();
        String teacherName = course.getTeacherName();

        QueryWrapper query = new QueryWrapper();
        query.eq("teacher_id",teacherId);
        query.eq("teacher_name",teacherName);
        query.eq("course_name",oldCourseName);
        query.eq("class_name",oldClassName);
        boolean flag = courseService.update(course,query);
        if(flag){
            return Result.success(ResultEnum.SUCCESS_EDIT,course);
        }
        else {
            return Result.fail(ResultEnum.ERROR_EDIT);
        }
    }

    @GetMapping("/list")
    public Result courseListById(@RequestParam(defaultValue = "1") int pageIndex
            ,@RequestParam(defaultValue = "4") int pageSize
            ,@RequestParam(defaultValue = "-1") BigInteger id){

        Page<Course> page = new Page<>(pageIndex,pageSize);
        QueryWrapper query =new QueryWrapper();
        if(!id.equals(BigInteger.valueOf(-1))){
            query.eq("teacher_id",id);
            pageResult = courseService.page(page,query);
        }
        else{
            pageResult = courseService.page(page);
        }

        if(pageResult.getRecords() == null){
            return Result.fail(ResultEnum.ERROR_FOUND);
        }

        List courseList = pageResult.getRecords().stream().map(course->{
            CourseVo courseVo = new CourseVo();
            BeanUtils.copyProperties(course,courseVo);
            return courseVo;
        }).collect(Collectors.toList());

        pageResult.setRecords(courseList);

        return Result.success(ResultEnum.SUCCESS_LIST,pageResult);
    }

}
