package com.example.services.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mappers.CourseMapper;
import com.example.pojos.Course;
import com.example.services.CourseService;
import org.springframework.stereotype.Service;

@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements CourseService {
}
