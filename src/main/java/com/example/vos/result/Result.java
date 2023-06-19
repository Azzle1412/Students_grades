package com.example.vos.result;
import lombok.Data;
import org.springframework.lang.Nullable;


@Data
public class Result<T> {

    private int code;
    private String msg;
    @Nullable
    private T data;



    public Result(int code, String msg, @Nullable T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static <T> Result success(ResultEnum resultEnum, @Nullable T data){

        Result result = new Result(resultEnum.getCode()
                , resultEnum.getMsg()
                , data);

        return result;

    }

    public static <T> Result fail(ResultEnum resultEnum){

        Result result = new Result(resultEnum.getCode()
                ,resultEnum.getMsg()
                , null);

        return result;

    }

}