package com.example.services.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mappers.StudentMapper;
import com.example.mappers.TeacherMapper;
import com.example.pojos.Student;
import com.example.pojos.Teacher;
import com.example.services.TeacherService;
import org.springframework.stereotype.Service;

@Service
public class TeacherServiceImpl extends ServiceImpl<TeacherMapper, Teacher> implements TeacherService {
}
