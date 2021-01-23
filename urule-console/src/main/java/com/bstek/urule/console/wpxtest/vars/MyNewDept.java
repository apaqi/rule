package com.bstek.urule.console.wpxtest.vars;

import com.bstek.urule.model.Label;

public class MyNewDept {

    @Label("部门名称")
    private String deptName;
    @Label("部门经理")
    private MyDepartmentManager myDepartmentManager;
    @Label("部门人员数")
    private Integer deptNum;

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public MyDepartmentManager getMyDepartmentManager() {
        return myDepartmentManager;
    }

    public void setMyDepartmentManager(MyDepartmentManager myDepartmentManager) {
        this.myDepartmentManager = myDepartmentManager;
    }

    public Integer getDeptNum() {
        return deptNum;
    }

    public void setDeptNum(Integer deptNum) {
        this.deptNum = deptNum;
    }
}
