package com.bstek.urule.console.wpxtest.vars;

import com.bstek.urule.model.Label;


//com.bstek.library.vars.Dept

public class MyDept {
    @Label("部门名称")
    private String deptName;
    @Label("部门经理")
    private String deptManager;
    @Label("部门人员数")
    private Integer deptNum;

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getDeptManager() {
        return deptManager;
    }

    public void setDeptManager(String deptManager) {
        this.deptManager = deptManager;
    }

    public Integer getDeptNum() {
        return deptNum;
    }

    public void setDeptNum(Integer deptNum) {
        this.deptNum = deptNum;
    }
}
