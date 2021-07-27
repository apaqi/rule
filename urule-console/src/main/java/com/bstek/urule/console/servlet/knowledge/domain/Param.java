package com.bstek.urule.console.servlet.knowledge.domain;

import com.bstek.urule.model.library.Datatype;
import org.apache.commons.collections.MapUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author wpx
 * @version 1.0.0
 * @Description TODO
 * @since 2021/07/09
 */
public class Param implements Serializable {

    private String code;
    private Object value;
    private Map<String, Object> param;


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Map<String, Object> getParam() {
        return param;
    }

    public void setParam(Map<String, Object> param) {
        this.param = param;
    }


    public List<RuleParam> convert2RuleParams() {
        if (MapUtils.isNotEmpty(param)) {
            List<RuleParam> ruleParams = new ArrayList<>(param.size());
            RuleParam ruleParam = new RuleParam();
            for (Map.Entry<String, Object> entry : param.entrySet()) {
                Object value = entry.getValue();
                String datatype = Datatype.String.name();
                if (value instanceof String) {
                    datatype = Datatype.String.name();
                } else if (value instanceof Integer) {
                    datatype = Datatype.Integer.name();
                } else if (value instanceof Character) {
                    datatype = Datatype.Char.name();
                } else if (value instanceof Double) {
                    datatype = Datatype.Double.name();
                } else if (value instanceof Long) {
                    datatype = Datatype.Long.name();
                } else if (value instanceof Float) {
                    datatype = Datatype.Float.name();
                } else if (value instanceof BigDecimal) {
                    datatype = Datatype.BigDecimal.name();
                } else if (value instanceof Boolean) {
                    datatype = Datatype.Boolean.name();
                } else if (value instanceof Date) {
                    datatype = Datatype.Date.name();
                } else if (value instanceof List) {
                    datatype = Datatype.List.name();
                } else if (value instanceof Set) {
                    datatype = Datatype.Set.name();
                } else if (value instanceof Map) {
                    datatype = Datatype.Map.name();
                } else if (value instanceof Enum) {
                    datatype = Datatype.Enum.name();
                }
                ruleParams.add(ruleParam.setCode(entry.getKey()).setValue(entry.getValue()).setDataType(datatype));
            }
            return ruleParams;
        } else {
            return Collections.emptyList();
        }
    }
}
