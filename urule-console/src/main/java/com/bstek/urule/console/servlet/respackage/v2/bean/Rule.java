package com.bstek.urule.console.servlet.respackage.v2.bean;

import java.io.Serializable;

/**
 * @author wpx
 * @version 1.0.0
 * @Description TODO
 * @since 2021/07/09
 */
public class Rule implements Serializable {
    /**
     * 类型（and 、 or）
     */
    private String type;

    /**
     * 规则
     */
    private Rule[] rule;

   // private List<CalData> calData;
    private Left left;
    private String operator;
    private Right right;

    public String getType() {
        return type;
    }

    public Rule setType(String type) {
        this.type = type;
        return this;
    }

    public Rule[] getRule() {
        return rule;
    }

    public Rule setRule(Rule[] rule) {
        this.rule = rule;
        return this;
    }

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
