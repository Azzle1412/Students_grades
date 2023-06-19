package com.example.forms;
import lombok.Data;

import java.math.BigInteger;

@Data
public class LoginForm {

    public BigInteger id;
    private String password;
    private Integer type;

}
