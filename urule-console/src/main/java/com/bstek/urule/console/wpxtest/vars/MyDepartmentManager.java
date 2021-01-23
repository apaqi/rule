package com.bstek.urule.console.wpxtest.vars;

import com.bstek.urule.model.Label;
/**
 * @author wpx
 * @since 2021/1/19
 */
public class MyDepartmentManager {
    @Label("名称")
    private String name;
    @Label("年龄")
    private int age;
    @Label("手机号")
    private String mobile;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
