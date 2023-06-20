package com.example.controllers;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.forms.CourseForm;
import com.example.pojos.Course;
import com.example.pojos.Teacher;
import com.example.services.CourseService;
import com.example.services.TeacherService;
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
/*管理员管理教师课程的模块*/
public class ManagerCourseController {

    @Autowired
    private TeacherService teacherService;
    @Autowired
    private CourseService courseService;
    private IPage<Course> pageResult;



    @PostMapping("/add")
    public Result courseAdd(@RequestBody CourseForm courseForm){

        Course course = new Course();
        BeanUtils.copyProperties(courseForm,course);

        BigInteger teacherId = course.getTeacherId();
        String courseName = course.getCourseName();
        String className = course.getClassName();

        // 检查teacher_table是否有该老师
        QueryWrapper query = new QueryWrapper();
        query.eq("teacher_id",teacherId);
        Teacher teacher = teacherService.getOne(query);
        if(teacher == null){
            return Result.fail(ResultEnum.ERROR_FOUND,"该教师信息不存在");
        }
        query.clear();


        // 检查course_table是否有该信息
        query.eq("teacher_id",teacherId);
        query.eq("course_name",courseName);
        query.eq("class_name",className);
        Course courseQuery = courseService.getOne(query);

        if(courseQuery == null){
            boolean flag = courseService.save(course);
            if(flag){
                CourseVo courseVo = new CourseVo();
                BeanUtils.copyProperties(course,courseVo);
                return Result.success(ResultEnum.SUCCESS_SAVE,courseVo);
            }
            else {
                return Result.fail(ResultEnum.ERROR_SAVE,"course_table插入失败");
            }
        }

        return Result.fail(ResultEnum.ERROR_SAVE,"该课程信息已经存在");
    }

    @DeleteMapping("/delete")
    public Result courseDelete(@RequestBody CourseForm courseForm){

        BigInteger teacherId = courseForm.getTeacherId();
        String courseName = courseForm.getCourseName();
        String className = courseForm.getClassName();

        QueryWrapper query = new QueryWrapper();
        query.eq("teacher_id",teacherId);
        query.eq("course_name",courseName);
        query.eq("class_name",className);
        Course courseQuery = courseService.getOne(query);

        if(courseQuery != null){
            boolean flag = courseService.remove(query);
            if(flag){
                CourseVo courseVo = new CourseVo();
                BeanUtils.copyProperties(courseQuery,courseVo);
                return Result.success(ResultEnum.SUCCESS_DELETE,courseVo);
            }
            else {
                return Result.fail(ResultEnum.ERROR_DELETE,"course_table删除失败");
            }
        }

        return Result.fail(ResultEnum.ERROR_DELETE,"该课程信息不存在");
    }

    @PutMapping("/edit")
    public Result courseEdit(@RequestBody CourseForm courseForm
            ,@RequestParam String oldTeacherId,@RequestParam String oldClassName, @RequestParam String oldCourseName){

        Course course = new Course();
        BeanUtils.copyProperties(courseForm,course);

        BigInteger teacherId = course.getTeacherId();


        QueryWrapper query = new QueryWrapper();
        query.eq("teacher_id",oldTeacherId);
        query.eq("course_name",oldCourseName);
        query.eq("class_name",oldClassName);
        Course courseQuery = courseService.getOne(query);
        if(courseQuery != null){
            return Result.fail(ResultEnum.ERROR_EDIT,"该课程信息已经存在");
        }

        boolean flag = courseService.update(course,query);
        if(flag){
            CourseVo courseVo = new CourseVo();
            BeanUtils.copyProperties(course,courseVo);
            return Result.success(ResultEnum.SUCCESS_EDIT,courseVo);
        }
        else {
            return Result.fail(ResultEnum.ERROR_EDIT,"course_table更新失败");
        }
    }

    @GetMapping("/list")
    public Result courseListById(@RequestParam(defaultValue = "1") int pageIndex,@RequestParam(defaultValue = "4") int pageSize
            ,@RequestParam(defaultValue = "-1") BigInteger teacherId){

        Page<Course> page = new Page<>(pageIndex,pageSize);
        QueryWrapper query =new QueryWrapper();
        if(!teacherId.equals(BigInteger.valueOf(-1))){
            query.eq("teacher_id",teacherId);
            pageResult = courseService.page(page,query);
        }
        else{
            pageResult = courseService.page(page);
        }

        if(pageResult.getRecords() == null){
            return Result.fail(ResultEnum.ERROR_FOUND,"课程信息列表为空");
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
