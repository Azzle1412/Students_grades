package com.example.controllers;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.forms.TeacherForm;
import com.example.pojos.Teacher;
import com.example.services.TeacherService;
import com.example.vos.result.Result;
import com.example.vos.result.ResultEnum;
import com.example.vos.TeacherVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/system/admin-teacher")
/*管理员管理教师信息的模块*/
public class ManagerTeacherController {

    @Autowired
    private TeacherService teacherService;
    private IPage<Teacher> pageResult;



    @PostMapping("/add")
    public Result teacherAdd(@RequestBody TeacherForm teacherForm){

        Teacher teacher = new Teacher();
        BeanUtils.copyProperties(teacherForm,teacher);

        BigInteger teacherId = teacher.getTeacherId();

        Teacher teacherQuery = teacherService.getById(teacherId);

        if(teacherQuery == null){
            boolean flag = teacherService.save(teacher);
            if(flag){
                TeacherVo teacherVo = new TeacherVo();
                BeanUtils.copyProperties(teacher,teacherVo);
                return Result.success(ResultEnum.SUCCESS_SAVE,teacherVo);
            }
            else {
                return Result.fail(ResultEnum.ERROR_SAVE,"teacher_table插入失败");
            }
        }

        return Result.fail(ResultEnum.ERROR_SAVE,"该教师已经存在");
    }

    @DeleteMapping("/delete/{teacherId}")
    public Result teacherDelete(@PathVariable BigInteger teacherId){

        Teacher teacher = teacherService.getById(teacherId);

        if(teacher != null){
            boolean flag = teacherService.removeById(teacherId);
            if(flag){
                TeacherVo teacherVo = new TeacherVo();
                BeanUtils.copyProperties(teacher,teacherVo);
                return Result.success(ResultEnum.SUCCESS_DELETE,teacherVo);
            }
            else{
                return Result.fail(ResultEnum.ERROR_DELETE,"teacher_table删除失败");
            }
        }

        return Result.fail(ResultEnum.ERROR_DELETE,"该教师信息不存在");
    }

    @PutMapping("/edit")
    public Result teacherEdit(@RequestBody TeacherForm teacherForm){

        Teacher teacher = new Teacher();
        BeanUtils.copyProperties(teacherForm,teacher);

        boolean flag = teacherService.updateById(teacher);
        if(flag){
            TeacherVo teacherVo = new TeacherVo();
            BeanUtils.copyProperties(teacher,teacherVo);
            return Result.success(ResultEnum.SUCCESS_EDIT,teacher);
        }
        else{
            return Result.fail(ResultEnum.ERROR_EDIT,"teacher_table修改失败");
        }

    }

    @GetMapping("/detail/{teacherId}")
    public Result teacherDetailById(@PathVariable BigInteger teacherId){

        Teacher teacher = teacherService.getById(teacherId);

        if(teacher == null){
            return Result.fail(ResultEnum.ERROR_FOUND,"该教师信息不存在");
        }

        TeacherVo teacherVo = new TeacherVo();
        BeanUtils.copyProperties(teacher,teacherVo);

        return Result.success(ResultEnum.SUCCESS_FOUND,teacherVo);
    }

    @GetMapping("/list")
    public Result  teacherList(@RequestParam(defaultValue = "1") int pageIndex, @RequestParam(defaultValue = "4") int pageSize
            , @RequestParam(defaultValue = "-1") BigInteger teacherId,@RequestParam(defaultValue = "*") String teacherName){


        Page<Teacher> page = new Page<>(pageIndex,pageSize);

        QueryWrapper query = new QueryWrapper();
        if(!teacherId.equals(BigInteger.valueOf(-1))) query.eq("teacher_id",teacherId);
        if(!teacherName.equals("*")) query.eq("teacher_name",teacherName);


        if(!teacherId.equals(BigInteger.valueOf(-1)) || !teacherName.equals("*")){
            pageResult = teacherService.page(page,query);
        }
        else{
            pageResult=teacherService.page(page);
        }

        if(pageResult.getRecords()==null){
            return Result.fail(ResultEnum.ERROR_FOUND,"教师信息列表为空");
        }

        List teacherVoList = pageResult.getRecords().stream().map(teacher->{
            TeacherVo teacherVo = new TeacherVo();
            BeanUtils.copyProperties(teacher,teacherVo);
            return teacherVo;
        }).collect(Collectors.toList());

        pageResult.setRecords(teacherVoList);

        return Result.success(ResultEnum.SUCCESS_LIST,pageResult);
    }

}
