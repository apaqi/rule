package com.bstek.urule.console.wpxtest.actions;

import com.alibaba.fastjson.JSON;
import com.bstek.urule.action.ActionId;
import com.bstek.urule.console.wpxtest.vars.MyDepartmentManager;
import com.bstek.urule.console.wpxtest.vars.MyDept;
import com.bstek.urule.console.wpxtest.vars.MyNewDept;
import com.bstek.urule.model.ExposeAction;

import java.util.Map;

public class MyDeptActionTest {
    @ActionId("deptAction")
    public String deptAction() {
        return "deptAction";
    }

    @ExposeAction(value = "是否有部门经理", parameters = {"经理名"})
    public boolean hasDeptManage(String deptManager) {
        if (deptManager == null || deptManager.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    @ExposeAction(value = "打印部门信息", parameters = {"部门信息"})
    public void printDept(MyDept m) {
        System.out.println("Dept：" + m.toString());
    }

    @ExposeAction(value = "打印新部门信息", parameters = {"新部门信息"})
    public void printDepts(MyNewDept m) {
        System.out.println("Dept：" + JSON.toJSONString(m));
    }

    /**
     * 打印部门经理信息
     *
     * @param manager 经理信息
     * @param company 公司名称
     * @param extMap  扩展信息
     * @return 是否成功
     */
    @ExposeAction(value = "打印部门经理信息", parameters = {"部门经理信息"})
    public boolean printDepartmentManager(MyDepartmentManager manager, String company, Map<String, Object> extMap) {
        System.out.println("manager：" + JSON.toJSONString(manager) + "##company=" + company + "##map=" + JSON.toJSONString(extMap));
        return false;
    }
}
