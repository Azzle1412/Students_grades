package com.example.vos.result;


public enum ResultEnum {

    SUCCESS_GPA(0,"GPA获取成功"),
    SUCCESS_LOGIN(1,"登录成功"),
    SUCCESS_SAVE(2,"保存成功"),
    SUCCESS_DELETE(3,"删除成功"),
    SUCCESS_EDIT(4,"修改成功"),
    SUCCESS_FOUND(5,"查找成功"),
    SUCCESS_LIST(6,"分页查询成功"),

    ERROR_LOGIN(-1,"登录失败"),
    ERROR_SAVE(-2,"保存失败"),
    ERROR_DELETE(-3,"删除失败"),
    ERROR_EDIT(-4,"修改失败"),
    ERROR_FOUND(-5,"查找失败"),
    ERROR_ACCESS(-6,"权限不够");



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
