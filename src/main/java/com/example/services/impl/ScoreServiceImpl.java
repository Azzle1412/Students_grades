package com.example.services.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mappers.ScoreMapper;
import com.example.pojos.Score;
import com.example.services.ScoreService;
import org.springframework.stereotype.Service;

@Service
public class ScoreServiceImpl extends ServiceImpl<ScoreMapper, Score> implements ScoreService {
}
