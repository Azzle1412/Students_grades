package com.example.vos.result;


public enum ResultEnum {

    SUCCESS_LOGIN(1,"登录成功"),
    SUCCESS_SAVE(2,"信息添加成功"),
    SUCCESS_DELETE(3,"用户成功删除"),
    SUCCESS_EDIT(4,"用户成功修改"),
    SUCCESS_FOUND(5,"用户成功找到"),
    SUCCESS_LIST(6,"成功分页查询"),

    ERROR_LOGIN(-1,"用户登录失败"),
    ERROR_SAVE(-2,"信息添加失败"),
    ERROR_DELETE(-3,"用户删除失败"),
    ERROR_EDIT(-4,"用户修改失败"),
    ERROR_FOUND(-5,"用户未找到");



    private int code;
    private String msg;



    ResultEnum(int code, String msg){
        this.code=code;
        this.msg=msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
