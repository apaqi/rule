package com.bstek.urule.console.servlet.respackage;


import java.util.Map;



public class BizReq {

    /**
     * 条件业务类型 规则中心/标签中心/自定义等
     */
    private String bizType;

    /**
     * CAMEL表达式中需要用到的属性 request.headers.${mvelKey}
     */
    private String mvelKey;

    /**
     * CAMEL表达式中需要用到表达式 ==,><等
     */
    private String mvelOper;

    /**
     * CAMEL 表达式右边的值
     */
    private Object mvelValue;

    /**
     * 控件输入类型
     */
    private int valueType;

    /**
     * 日期格式
     */
    private String dateFormat;

    /**
     * 时间范围 0 开始时间 1 结束时间
     */
    private String[] timeRange;

    /**
     * 其他参数
     */
    private Map<String,Object> bizParam;

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    public String getMvelKey() {
        return mvelKey;
    }

    public void setMvelKey(String mvelKey) {
        this.mvelKey = mvelKey;
    }

    public String getMvelOper() {
        return mvelOper;
    }

    public void setMvelOper(String mvelOper) {
        this.mvelOper = mvelOper;
    }

    public Object getMvelValue() {
        return mvelValue;
    }

    public void setMvelValue(Object mvelValue) {
        this.mvelValue = mvelValue;
    }

    public int getValueType() {
        return valueType;
    }

    public void setValueType(int valueType) {
        this.valueType = valueType;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public String[] getTimeRange() {
        return timeRange;
    }

    public void setTimeRange(String[] timeRange) {
        this.timeRange = timeRange;
    }

    public Map<String, Object> getBizParam() {
        return bizParam;
    }

    public void setBizParam(Map<String, Object> bizParam) {
        this.bizParam = bizParam;
    }
}
