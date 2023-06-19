package com.example.services.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mappers.StudentMapper;
import com.example.pojos.Student;
import com.example.services.StudentService;
import org.springframework.stereotype.Service;

@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService {
}
