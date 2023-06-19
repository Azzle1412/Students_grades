package com.example.services.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mappers.ManagerMapper;
import com.example.pojos.Manager;
import com.example.services.ManagerService;
import org.springframework.stereotype.Service;

@Service
public class ManagerServiceImpl extends ServiceImpl<ManagerMapper, Manager> implements ManagerService {
}
