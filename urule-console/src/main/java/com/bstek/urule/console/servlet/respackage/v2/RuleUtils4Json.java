package com.bstek.urule.console.servlet.respackage.v2;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bstek.urule.BizUtils;
import com.bstek.urule.model.library.Datatype;
import com.bstek.urule.model.rule.*;
import com.bstek.urule.model.rule.lhs.*;
import com.bstek.urule.script.ScriptType;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * @author wpx
 * @since 2021/1/7
 */
public class RuleUtils4Json {
    private final static Set<String> NEDD_SCRIPT_HANDLE = new HashSet();
    private final static Map<String, String> MALL_TRADE = new HashMap();
    private final static Map<String, Op> OP_MAP = new HashMap();

    static {
        OP_MAP.put("==", Op.Equals);
        OP_MAP.put("equal", Op.Equals);
        OP_MAP.put(">", Op.GreaterThen);
        OP_MAP.put("&gt;", Op.GreaterThen);
        OP_MAP.put(">=", Op.GreaterThenEquals);
        OP_MAP.put("&gt;=", Op.GreaterThenEquals);
        OP_MAP.put("<", Op.LessThen);
        OP_MAP.put("&lt;", Op.LessThen);
        OP_MAP.put("<=", Op.LessThenEquals);
        OP_MAP.put("&lt;=", Op.LessThenEquals);
        OP_MAP.put("!=", Op.NotEquals);
        OP_MAP.put("contains", Op.Contain);
        OP_MAP.put("!contains", Op.NotContain);
        OP_MAP.put("<contains", Op.In);
        OP_MAP.put("&lt;contains", Op.In);
        OP_MAP.put("<!contains", Op.NotIn);
        OP_MAP.put("&lt;!contains", Op.NotIn);

        //商城交易
        MALL_TRADE.put("PROFIT_SKU_ID", "mallTradeProfitSkuIdBiz");
        MALL_TRADE.put("PROFIT_ORDER_ID", "mallTradeProfitOrderIdBiz");
        MALL_TRADE.put("BOUGHT_CID2", "mallTradeBoughtCid2Biz");
        MALL_TRADE.put("BOUGHT_CID3", "mallTradeBoughtCid3Biz");
        MALL_TRADE.put("BOUGHT_SKU_ID", "mallTradeBoughtSkuIdBiz");
        MALL_TRADE.put("BAITIAO_AMOUNT", "mallTradeBaitiaoAmountBiz");
        MALL_TRADE.put("REDUCE_CASH", "mallTradeReduceCashBiz");
        MALL_TRADE.put("FREE_INTEREST", "mallTradeFreeInterestBiz");

        NEDD_SCRIPT_HANDLE.add("varTagBiz");
        NEDD_SCRIPT_HANDLE.add("actRuleBiz");
        NEDD_SCRIPT_HANDLE.add("aiExportStatBiz");
        NEDD_SCRIPT_HANDLE.add("baiTiaoBiz");
        NEDD_SCRIPT_HANDLE.add("baiTiaoInstalmentsCoupon");
        NEDD_SCRIPT_HANDLE.add("baiTiaoNewAccessUser");
        NEDD_SCRIPT_HANDLE.add("baiTiaoOutstandingBillAmount");
        NEDD_SCRIPT_HANDLE.add("baiTiaoOutstandingBillCount");
        NEDD_SCRIPT_HANDLE.add("expiringCouponCheckBiz");
        NEDD_SCRIPT_HANDLE.add("userCouponQueryBiz");
        NEDD_SCRIPT_HANDLE.add("jRMemberLevel");
        NEDD_SCRIPT_HANDLE.add("prerogativeBiz");
        NEDD_SCRIPT_HANDLE.add("customRuleBiz");
        NEDD_SCRIPT_HANDLE.add("extendWarrantyBiz");
        NEDD_SCRIPT_HANDLE.add("jtOrderSuccessCountBiz");
        NEDD_SCRIPT_HANDLE.add("modelTagBiz");
        NEDD_SCRIPT_HANDLE.add("mallTradeFreeInterestBiz");
        NEDD_SCRIPT_HANDLE.add("mallTradeReduceCashBiz");
        NEDD_SCRIPT_HANDLE.add("insuranceTypeBiz");
        NEDD_SCRIPT_HANDLE.add("jtActiveTagBiz");
        NEDD_SCRIPT_HANDLE.add("jtLoanTagBiz");
        NEDD_SCRIPT_HANDLE.add("wealthTagsBiz");
        NEDD_SCRIPT_HANDLE.add("mktRuleBiz");
        NEDD_SCRIPT_HANDLE.add("dupRuleBiz");
        NEDD_SCRIPT_HANDLE.add("dataCenterPinsBiz");
        NEDD_SCRIPT_HANDLE.add("dataCenterTagBiz");
        NEDD_SCRIPT_HANDLE.add("featureEnhanceBiz");
    }

    /**
     * 根据类型获取Junction实例
     *
     * @param type
     * @return
     */


    public static Criterion getJunctionByType(String type) {
        if (StringUtils.equals("and", type)) {
            return And.instance();
        } else {
            return Or.instance();
        }
    }


    /**
     * 根据类型获取Junction实例
     *
     * @param param
     * @return
     */


    public static Criterion getRootJunctionByType(Map<String, Object> param) {
        //1、判断type如果为空设置默认值and 单个参数默认为and 多个参数可能是and或者or
        String type = MapUtils.getString(param, "type", "and");
        //2、获取参数列表
        List<JSONObject> list = (List<JSONObject>) param.get("rule");
        //解决根节点默认有个and 条件问题，测试任务11725
        if (!isHasParent(param)) {
            type = MapUtils.getString(list.get(0), "type");
        }
        return getJunctionByType(type);
    }

    /**
     * 是否有父节点
     *
     * @param param
     * @return
     */


    public static boolean isHasParent(Map<String, Object> param) {
        List<JSONObject> list = (List<JSONObject>) param.get("rule");
        if (CollectionUtils.isNotEmpty(list) && list.size() == 1) {
            return false;
        }
        return true;
    }


    /**
     * 解析规则
     *
     * @param param
     * @param isRoot
     * @param parentJunction
     * @param junctions
     * @param extentionParam
     * @param responseParam
     * @return
     */


    public static List<Criterion> parseCriterions(Map<String, Object> param, boolean isRoot, Criterion parentJunction, List<Criterion> junctions,
                                                  Map<String, Object> extentionParam, Map<String, Object> responseParam) {
        //1、判断type如果为空设置默认值and 单个参数默认为and 多个参数可能是and或者or
        String type = MapUtils.getString(param, "type", "and");
        //2、获取参数列表
        List<JSONObject> list = (List<JSONObject>) param.get("rule");
        //解决根节点默认有个and 条件问题，测试任务11725
        Criterion junction = RuleUtils4Json.getJunctionByType(type);
        //3、便利参数列表
        List<Criterion> criterias = Lists.newArrayList();
        if (isRoot && CollectionUtils.isNotEmpty(list) && list.size() == 1) {
            JSONObject object = list.get(0);
            type = MapUtils.getString(object, "type");
            junction = RuleUtils4Json.getJunctionByType(type);
            junctions = getCriteria(junctions, extentionParam, responseParam, (List<JSONObject>) object.get("rule"), junction, criterias);
        } else {
            junctions = getCriteria(junctions, extentionParam, responseParam, list, junction, criterias);
        }
        if (CollectionUtils.isNotEmpty(criterias)) {
            if (junction instanceof And) {
                And and = ((And) junction).setCriterions(isRoot, criterias);
                if (null == parentJunction) {
                    if (null == junctions) {
                        junctions = Lists.newArrayList();
                    }
                    junctions.add(and);
                } else {
                    if (parentJunction instanceof Or) {
                        ((Or) parentJunction).setCriterions(isRoot, criterias);
                        if (null == junctions) {
                            junctions = Lists.newArrayList();
                        }
                        junctions.add(and);
                    } else {
                        ((And) parentJunction).setCriterions(isRoot, criterias);
                        if (null == junctions) {
                            junctions = Lists.newArrayList();
                        }
                        junctions.add(and);
                    }
                }
            } else if (junction instanceof Or) {
                Or or = ((Or) junction).setCriterions(isRoot, criterias);
                if (null == parentJunction) {
                    if (null == junctions) {
                        junctions = Lists.newArrayList();
                    }
                    junctions.add(or);
                } else {
                    if (parentJunction instanceof Or) {
                        ((Or) parentJunction).setCriterions(isRoot, criterias);
                        if (null == junctions) {
                            junctions = Lists.newArrayList();
                        }
                        junctions.add(or);

                    } else {
                        ((And) parentJunction).setCriterions(isRoot, criterias);
                        if (null == junctions) {
                            junctions = Lists.newArrayList();
                        }
                        junctions.add(or);
                    }
                }
            }
        }
        return junctions;
    }

    private static List<Criterion> getCriteria(List<Criterion> junctions, Map<String, Object> extentionParam, Map<String, Object> responseParam, List<JSONObject> list, Criterion junction, List<Criterion> criterias) {
        for (JSONObject para : list) {
            if (para.containsKey("rule")) {
                junctions = parseCriterions(para, false, junction, junctions, extentionParam, responseParam);
            } else {
                String expectedValue = MapUtils.getString(para, "right");
                String leftSource = MapUtils.getString(para, "left");
                Map<String, Object> leftSourceMap = JSON.parseObject(leftSource, Map.class);
                //todo 需要从玲珑塔匹配
                String protocol = MapUtils.getString(leftSourceMap, "protocol");
                String beanId = MapUtils.getString(leftSourceMap, "");//todo 2.0 默认写死？？
                String methodName = MapUtils.getString(leftSourceMap, "url"); ///todo 2.0 默认写死？？
                String result = MapUtils.getString(leftSourceMap, "result");
                String operator = MapUtils.getString(leftSourceMap, "operator","==");
                Parameter[] params = buildParameters(leftSourceMap);
                Map<String, Object> resultMap = JSON.parseObject(result, Map.class);
                //后期针对控件依赖的资源做资源化管理
                if (MapUtils.isNotEmpty(resultMap) && isNeedScript(resultMap)) {
                    ScriptMethodLeftPart scriptMethodLeftPart = BizUtils.buildScriptMethodLeftPart(beanId, methodName, ScriptType.JSONPATH, "$." + expectedValue, params);
                    Op op = RuleUtils4Json.convert2RuleOperator(operator);
                    if (isNeedConvert2InOp(op, expectedValue)) {
                        op = Op.In;
                    }
                    Criteria criteria = Criteria.instance()
                            .setLeft(Left.instance(scriptMethodLeftPart))
                            .setOp(op)
                            .setValue(buildValue(op, expectedValue));
                    criterias.add(criteria);
                } else {
                    MethodLeftPart leftPart = BizUtils.buildMethodLeftPart(beanId, methodName, params);
                    Op op = RuleUtils4Json.convert2RuleOperator(operator);
                    Criteria criteria = Criteria.instance()
                            .setLeft(Left.instance(leftPart))
                            .setOp(op)
                            .setValue(buildValue(op, expectedValue));
                    criterias.add(criteria);
                }
            }
        }
        return junctions;
    }

    private static Parameter[] buildParameters(Map<String, Object> sourceMap) {
        JSONArray leftParam = (JSONArray)MapUtils.getObject(sourceMap, "param");
        int len = leftParam.size();

        Parameter[] parameters = new Parameter[len];
        for (int i = 0; i < len; i++) {
            JSONObject o = (JSONObject) leftParam.get(i);
            String dataType = o.getString("dataType");
            String paramName = o.getString("code");
            String value = o.getString("value");
            parameters[i] = BizUtils.buildSimpleParameter(paramName, Datatype.getByName(dataType), value);
        }
        return parameters;
    }

    /**
     * @param type
     * @return com.bstek.urule.model.rule.Op
     * @Description 将molo操作符转换为规则组件操作符
     * @Author wpx
     * @Date 2021/1/7 18:05
     */


    public static Op convert2RuleOperator(String type) {
        if (StringUtils.isBlank(type)) {
            return Op.Equals;
        }
        return OP_MAP.get(type.trim());
    }

    /**
     * @param op
     * @param mvelValue
     * @return com.bstek.urule.model.rule.Value
     * @Description 构造Value对象
     * @Author wpx
     * @Date 2021/1/7 18:05
     */


    private static Value buildValue(Op op, String mvelValue) {
        if (mvelValue instanceof String) {
            String valueStr = (String) mvelValue;
            if (StringUtils.isBlank((String) mvelValue)) {
                valueStr = "true";
                return SimpleValue.instance(valueStr);
            }
            if (Op.isCollectType(op)) {
                String[] valueArr = valueStr.split(",");
                List<Object> vals = new ArrayList<>(valueArr.length);
                for (int i = 0, len = valueArr.length; i < len; i++) {
                    vals.add(valueArr[i]);
                }
                return ComplexObjectValue.instance().setContent(vals);
            } else {
                return SimpleValue.instance(valueStr);
            }
        } else {
            /*if (Op.In == op && mvelValue instanceof JSONArray) {
                Object[] valueArr = ((JSONArray) mvelValue).toArray();
                List<Object> vals = new ArrayList<>(valueArr.length);
                for (int i = 0, len = valueArr.length; i < len; i++) {
                    vals.add(valueArr[i]);
                }
                return ComplexObjectValue.instance().setContent(vals);
            } else {
                return SimpleValue.instance(JSON.toJSONString(mvelValue));
            }*/
            return SimpleValue.instance(mvelValue);
        }
    }

    /**
     * {"expression":"","expressionType":"groovy、jsonpath、no、spel"}
     *
     * @param resultMap 结果处理配置
     * @return true ：需要结果脚本处理 false 不需要结果脚本处理
     */
    private static boolean isNeedScript(Map<String, Object> resultMap) {
        String expressionType = MapUtils.getString(resultMap, "scriptType", "none");
        return !StringUtils.equals(expressionType, "none");
    }


    /**
     * 特殊场景需要转换运算符，比如 "条件控件--用户基础信息--性别" 选择多值场景
     *
     * @param op
     * @param expectedValue
     * @return
     */


    private static boolean isNeedConvert2InOp(Op op, Object expectedValue) {
        if (Op.Equals == op && expectedValue instanceof JSONArray) {
            return true;
        }
        return false;
    }

    /**
     * 构造Lhs
     *
     * @param paramJson
     * @return
     */
    private static Lhs buildLhsByJson(String paramJson, Map<String, Object> extention, Map<String, Object> responseParam) {
        Map<String, Object> map = JSON.parseObject(paramJson, Map.class);
        Junction parentJunction = null;
        List<Criterion> conditions = RuleUtils4Json.parseCriterions(map, true, parentJunction, null, extention, responseParam);
        Criterion rootJunctionType = RuleUtils4Json.getRootJunctionByType(map);
        Lhs lhs = null;
        if (!RuleUtils4Json.isHasParent(map)) {
            lhs = Lhs.instance().setCriterion(conditions.get(0));
        } else {
            if (rootJunctionType instanceof And) {
                lhs = Lhs.instance().setCriterion(And.instance().setCriterions(true, conditions));
            } else {
                lhs = Lhs.instance().setCriterion(Or.instance().setCriterions(true, conditions));
            }
        }
        return lhs;
    }

    public static void main(String[] args) {
        //4.引擎2.0规则文件模板（Final）
        String s = "{\"type\":\"and\",\"rule\":[{\"type\":\"and\",\"rule\":[{\"left\":{\"param\":[{\"code\":\"skuId\",\"dataType\":\"Long\",\"value\":10002},{\"code\":\"prize\",\"dataType\":\"BigDecimal\",\"value\":13.24}],\"url\":\"isHit\",\"protocol\":\"JSF\"},\"operator\":\"contains\",\"right\":{\"param\":[{\"code\":\"result\",\"dataType\":\"boolean\",\"value\":10002}],\"url\":\"\",\"protocol\":\"FIXED\"}},{\"left\":{\"param\":[{\"code\":\"skuId\",\"dataType\":\"Long\",\"value\":10002},{\"code\":\"prize\",\"dataType\":\"BigDecimal\",\"value\":13.24}],\"url\":\"接口#method\",\"protocol\":\"JSF\"},\"operator\":\"contains\",\"right\":{\"param\":[{\"code\":\"skuId\",\"dataType\":\"Long\",\"value\":10002},{\"code\":\"prize\",\"dataType\":\"BigDecimal\",\"value\":13.24}],\"url\":\"C54GFDG56FDS54GDS\",\"protocol\":\"JSF\"}}]},{\"type\":\"and\",\"rule\":[{\"left\":{\"param\":[{\"code\":\"skuId\",\"dataType\":\"Long\",\"value\":10002},{\"code\":\"prize\",\"dataType\":\"BigDecimal\",\"value\":13.24}],\"url\":\"C54GFDG56FDS54GDS\",\"protocol\":\"HTTP\"},\"operator\":\"contains\",\"right\":{\"param\":[{\"code\":\"result\",\"dataType\":\"boolean\",\"value\":10002}],\"url\":\"\",\"protocol\":\"FIXED\"}}]}]}";
        Map<String, Object> paramMap = new HashMap<>();
        Lhs lhs = buildLhsByJson(s, null, null);
        System.out.println();
    }
}
