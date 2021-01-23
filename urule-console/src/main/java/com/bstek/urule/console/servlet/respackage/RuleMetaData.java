package com.bstek.urule.console.servlet.respackage;

/**
 * @author wpx
 * @since 2021/1/23
 */

public class RuleMetaData {
    /**
     * 请求类型（dubbo,native）
     */
    private String requestType;
    /**
     * 操作符
     * @see com.bstek.urule.model.rule.Op
     */
    private String operator;
    /**
     * 期望值（比较值）
     */
    private String expectedValue;
    /**
     * 规则条件索引号，用来做规则跟踪
     */
    private Integer conditionIndex;
    /**
     * 数据类型
     */
    private String dataType;
    /**
     * 规则依赖资源信息（Json数据结构）
     * {"beanId":"interface","methodName":"","params":[{"paramName":"","dataType":"","value":""},{"paramName":"","dataType":"","value":""}],"result":{"expressionType":"groovy、jsonpath、no、spel","expression":""}}
     */
    private String source;



    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getExpectedValue() {
        return expectedValue;
    }

    public void setExpectedValue(String expectedValue) {
        this.expectedValue = expectedValue;
    }

    public Integer getConditionIndex() {
        return conditionIndex;
    }

    public void setConditionIndex(Integer conditionIndex) {
        this.conditionIndex = conditionIndex;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
