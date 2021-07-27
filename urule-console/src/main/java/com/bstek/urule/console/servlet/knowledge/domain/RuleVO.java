package com.bstek.urule.console.servlet.knowledge.domain;

import java.io.Serializable;

/**
 * @author wpx
 * @version 1.0.0
 * @Description TODO
 * @since 2021/07/09
 */
public class RuleVO implements Serializable {
    /**
     * 类型（and 、 or）
     */
    private String type;

    /**
     * 规则
     */
    private RuleVO[] rule;

   // private List<CalData> calData;
    private LeftVO left;
    private String operator;
    private RightVO right;

    public String getType() {
        return type;
    }

    public RuleVO setType(String type) {
        this.type = type;
        return this;
    }

    public RuleVO[] getRule() {
        return rule;
    }

    public RuleVO setRule(RuleVO[] rule) {
        this.rule = rule;
        return this;
    }

    public LeftVO getLeft() {
        return left;
    }

    public void setLeft(LeftVO left) {
        this.left = left;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public RightVO getRight() {
        return right;
    }

    public void setRight(RightVO right) {
        this.right = right;
    }
}
