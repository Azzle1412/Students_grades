package com.example.controllers;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.pojos.Score;
import com.example.pojos.Student;
import com.example.services.ScoreService;
import com.example.services.StudentService;
import com.example.utils.MyInfo;
import com.example.utils.MyScore;
import com.example.vos.MonitorScoreVo;
import com.example.vos.SingleScoreVo;
import com.example.vos.result.Result;
import com.example.vos.result.ResultEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/system/student-score")
public class StudentScoreController {
    @Autowired
    private ScoreService scoreService;

    @Autowired
    private StudentService studentService;

    @GetMapping("/single/{studentId}")
    public Result singleScoreList(@RequestParam(defaultValue = "1") int pageIndex
            , @RequestParam(defaultValue = "4") int pageSize
            ,@PathVariable BigInteger studentId){

        QueryWrapper query = new QueryWrapper();
        query.eq("student_id",studentId);
        Page<Score> page = new Page<>(pageIndex,pageSize);
        IPage<Score> pageResult = scoreService.page(page,query);

        if(pageResult.getRecords()==null){
            return Result.fail(ResultEnum.ERROR_FOUND);
        }

        List scoreVoList = pageResult.getRecords().stream().map(score->{
            SingleScoreVo singleScoreVo = new SingleScoreVo();
            BeanUtils.copyProperties(score, singleScoreVo);
            return singleScoreVo;
        }).collect(Collectors.toList());

        pageResult.setRecords(scoreVoList);

        return Result.success(ResultEnum.SUCCESS_FOUND, pageResult);
    }

    // 分页前端写死
    @GetMapping("/monitor/{className}")
    public Result monitorScoreList(@PathVariable String className){

        QueryWrapper query = new QueryWrapper();
        query.eq("class_name",className);
        List<Score> scoreList = scoreService.list(query);

        if(scoreList.isEmpty()){
            return Result.fail(ResultEnum.ERROR_FOUND);
        }

        HashMap<MyInfo,List<MyScore>> hashMap = new HashMap<>();

        for (Score score : scoreList) {

            MyInfo myInfo = new MyInfo(score.getStudentId(),score.getStudentName());
            MyScore myScore = new MyScore( score.getCourseName(), score.getScore());

            if (hashMap.isEmpty() || !hashMap.containsKey(myInfo)) {
                List<MyScore> myScoreList = new ArrayList<>(Arrays.asList(myScore));
                hashMap.put(myInfo, myScoreList);
            } else {
                hashMap.get(myInfo).add(myScore);
            }
        }

        List<MonitorScoreVo> monitorScoreVoList = new ArrayList<>();

        for (Map.Entry<MyInfo, List<MyScore>> element : hashMap.entrySet()) {
            MyInfo myInfo = element.getKey();
            List<MyScore> myScore = element.getValue();

            BigInteger studentId = myInfo.getStudentId();
            Student student = studentService.getById(studentId);
            String gpa ="0";

            if(student != null){
                gpa = student.getGpa();
            }

            MonitorScoreVo monitorScoreVo
                    = new MonitorScoreVo(myInfo.getStudentId(),myInfo.getStudentName(),gpa,myScore);

            monitorScoreVoList.add(monitorScoreVo);
        }

        Comparator<MonitorScoreVo> comparator = new Comparator<MonitorScoreVo>() {
            @Override
            public int compare(MonitorScoreVo left, MonitorScoreVo right) {
                float leftGpa = Float.parseFloat(left.getGpa());
                float rightGpa = Float.parseFloat(right.getGpa());
                if (leftGpa < rightGpa) return 1;
                else if (leftGpa > rightGpa) return -1;
                else return 0;
            }
        };

        Collections.sort(monitorScoreVoList, comparator);

        return Result.success(ResultEnum.SUCCESS_FOUND, monitorScoreVoList);
    }

}
