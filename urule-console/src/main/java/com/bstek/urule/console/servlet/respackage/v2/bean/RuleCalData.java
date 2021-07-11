package com.bstek.urule.console.servlet.respackage.v2.bean;

/**
 * 叶子节点规则计算数据
 * @author wpx
 * @version 1.0.0
 * @Description TODO
 * @since 2021/07/11
 */
public class RuleCalData {
    private Left left;
    private String operator;
    private Right right;

    public Left getLeft() {
        return left;
    }

    public void setLeft(Left left) {
        this.left = left;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Right getRight() {
        return right;
    }

    public void setRight(Right right) {
        this.right = right;
    }
}
