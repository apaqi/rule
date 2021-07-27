package com.bstek.urule.console.servlet.knowledge.domain;

/**
 * @author wpx
 * @version 1.0.0
 * @Description TODO
 * @since 2021/07/09
 */
public class RuleParam {
    /**
     * 参数code
     */
    private String code;
    /**
     * 参数类型,参照 ： Datatype
     */
    private String dataType;
    /**
     * 参数值
     */
    private Object value;

    public String getCode() {
        return code;
    }

    public RuleParam setCode(String code) {
        this.code = code;
        return this;
    }

    public String getDataType() {
        return dataType;
    }

    public RuleParam setDataType(String dataType) {
        this.dataType = dataType;
        return this;
    }

    public Object getValue() {
        return value;
    }

    public RuleParam setValue(Object value) {
        this.value = value;
        return this;
    }
}
