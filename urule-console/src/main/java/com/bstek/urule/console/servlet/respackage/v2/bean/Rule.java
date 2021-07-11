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

    private RuleCalData[] ruleCalDatas;

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

    public RuleCalData[] getRuleCalDatas() {
        return ruleCalDatas;
    }

    public Rule setRuleCalDatas(RuleCalData[] ruleCalDatas) {
        this.ruleCalDatas = ruleCalDatas;
        return this;
    }
}
