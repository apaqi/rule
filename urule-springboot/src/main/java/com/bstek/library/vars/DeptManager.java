package com.bstek.library.vars;

import com.bstek.urule.model.Label;
import lombok.Data;

import java.util.Date;

@Data
public class DeptManager {
    @Label("名称")
    private String name;
    @Label("年龄")
    private int age;

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
}
