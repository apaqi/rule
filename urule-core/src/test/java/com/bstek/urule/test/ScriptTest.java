package com.bstek.urule.test;


import com.alibaba.fastjson.JSON;
import com.bstek.urule.script.GroovyAction;
import com.bstek.urule.script.JsonPathAction;
import com.google.common.collect.Maps;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

public class ScriptTest {
    @Test
    public void testJsonPath(){
        JsonPathAction jsonPathAction = new JsonPathAction();
        Map<String,String> stringMap = new HashMap<>();
        stringMap.put("d","bs,dsa,grtsd");

        jsonPathAction.setExpression("$.d");
        Object invoke = jsonPathAction.actionInvoker().invoke(JSON.toJSON(stringMap));
        System.out.println(invoke);
    }

    /**https://blog.csdn.net/chengbinbbs/article/details/86743205*/
    @Test
    public void testJsonPathGroovy(){
        String script = "def testMethodName(map){\n" +
                "    def paramMap = map['prizeInstanceEntity']\n" +
                "    def prizeCategory = paramMap['prizeCategory']\n" +
                "    def prizeSubCategory = paramMap['prizeSubCategory']\n" +
                "    if(prizeCategory == 'COUPON'\n" +
                "        || prizeCategory == 'SUBSIDY_PRIZE'\n" +
                "        ||prizeCategory == 'ORDER_COUPON'\n" +
                "        ||(prizeCategory == 'INTER_PRIZE' && prizeSubCategory == 'L')){\n" +
                "             def operateTypeEnum = map['operateTypeEnum']\n" +
                "             if(operateTypeEnum=='TAKEN' || operateTypeEnum =='USE_SUCCESS'\n" +
                "                || operateTypeEnum =='USE_FAIL'\n" +
                "                || operateTypeEnum =='EXPIRE') {\n" +
                "                return false;\n" +
                "             }else{\n" +
                "                 return true;\n" +
                "             }\n" +
                "    }\n" +
                "    return true;\n" +
                "}";

        GroovyAction groovyAction = new GroovyAction();
        groovyAction.setExpression(script);
        groovyAction.setScriptMethodName("testMethodName");

        Map<String ,Object> param = new HashMap<>();
        param.put("operateTypeEnum","EXPIRE");
        Map<String ,String> innerParam = new HashMap<>();
        innerParam.put("prizeCategory","INTER_PRIZE");
        innerParam.put("prizeSubCategory","L");
        innerParam.put("operateTypeEnum","xUSE_SUCCESS");
        param.put("prizeInstanceEntity", innerParam);

        Object invoke = groovyAction.actionInvoker().invoke(param);
        System.out.println(invoke);
    }
}
