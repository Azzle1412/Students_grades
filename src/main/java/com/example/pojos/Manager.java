package com.example.pojos;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigInteger;

@Data
@TableName("manager_table")
public class Manager {

    @TableId
    public BigInteger managerId;
    private String password;

}
