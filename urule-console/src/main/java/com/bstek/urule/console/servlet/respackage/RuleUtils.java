package com.bstek.urule.console.servlet.respackage;

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
public class RuleUtils {
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
        Criterion junction = RuleUtils.getJunctionByType(type);
        //3、便利参数列表
        List<Criterion> criterias = Lists.newArrayList();
        if (isRoot && CollectionUtils.isNotEmpty(list) && list.size() == 1) {
            JSONObject object = list.get(0);
            type = MapUtils.getString(object, "type");
            junction = RuleUtils.getJunctionByType(type);
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
            //4、解析多重条件
            if (para.containsKey("rule")) {
                junctions = parseCriterions(para, false, junction, junctions, extentionParam, responseParam);
            } else {
                RuleMetaData ruleMetaData = JSONObject.parseObject(JSON.toJSONString(para), RuleMetaData.class);
                BizReq bizReqObj = JSONObject.parseObject(JSON.toJSONString(para), BizReq.class);
                List<BizReq> bizReqsParam = new ArrayList<>();
                bizReqsParam.add(bizReqObj);
                String source = MapUtils.getString(para, "source");

                RpcBizEnum rpcBizEnum = RpcBizEnum.getByCode(MapUtils.getString(para, "bizType"));
                Parameter bizReqs = BizUtils.buildComplexObjectValueParameter("bizReqs", Datatype.List, bizReqsParam);
                Parameter extention = BizUtils.buildComplexObjectValueParameter("extention", Datatype.Map, extentionParam);
                Parameter response = BizUtils.buildComplexObjectValueParameter("response", Datatype.Map, responseParam);
                //后期针对控件依赖的资源做资源化管理
                if (isNeedScript(getHandleBeanId(rpcBizEnum, bizReqObj.getBizParam()))) {
                    ScriptMethodLeftPart scriptMethodLeftPart = BizUtils.buildScriptMethodLeftPart(rpcBizEnum.getHandleBeanId(), "bizData", ScriptType.JSONPATH, "$." + bizReqObj.getMvelKey(), bizReqs, extention, response);
                    Op op = RuleUtils.convert2RuleOperator(bizReqObj.getMvelOper());
                    if (isNeedConvert2InOp(op, bizReqObj)) {
                        op = Op.In;
                    }
                    Criteria criteria = Criteria.instance()
                            .setLeft(Left.instance(scriptMethodLeftPart))
                            .setOp(op)
                            .setValue(buildValue(op, bizReqObj));
                    criterias.add(criteria);
                } else {
                    MethodLeftPart leftPart = BizUtils.buildMethodLeftPart(rpcBizEnum.getHandleBeanId(), "bizData", bizReqs, extention, response);
                    Op op = RuleUtils.convert2RuleOperator(bizReqObj.getMvelOper());
                    Criteria criteria = Criteria.instance()
                            .setLeft(Left.instance(leftPart))
                            .setOp(op)
                            .setValue(buildValue(op, bizReqObj));
                    criterias.add(criteria);
                }
            }
        }
        return junctions;
    }

    /**
     * @param type
     * @return com.bstek.urule.model.rule.Op
     * @Description 将molo操作符转换为规则组件操作符
     * @Author wangpeixuaninfo@jd.com
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
     * @param bizReq
     * @return com.bstek.urule.model.rule.Value
     * @Description 构造Value对象
     * @Author wangpeixuaninfo@jd.com
     * @Date 2021/1/7 18:05
     */


    private static Value buildValue(Op op, BizReq bizReq) {
        Object mvelValue = bizReq.getMvelValue();
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
            if (Op.In == op && mvelValue instanceof JSONArray) {
                Object[] valueArr = ((JSONArray) mvelValue).toArray();
                List<Object> vals = new ArrayList<>(valueArr.length);
                for (int i = 0, len = valueArr.length; i < len; i++) {
                    vals.add(valueArr[i]);
                }
                return ComplexObjectValue.instance().setContent(vals);
            } else {
                return SimpleValue.instance(JSON.toJSONString(mvelValue));
            }
        }
    }

    private static boolean isNeedScript(String beanId) {
        return NEDD_SCRIPT_HANDLE.contains(beanId);
    }

    private static String getHandleBeanId(RpcBizEnum rpcBizEnum, Map<String, Object> bizParam) {
        if (StringUtils.equals(rpcBizEnum.getCode(), RpcBizEnum.MALL_TRADE.getCode())) {
            String labelKey = MapUtils.getString(bizParam, "labelKey");
            return MALL_TRADE.get(labelKey);
        } else {
            return rpcBizEnum.getHandleBeanId();
        }
    }

    /**
     * 特殊场景需要转换运算符，比如 "条件控件--用户基础信息--性别" 选择多值场景
     *
     * @param op
     * @param bizReq
     * @return
     */


    private static boolean isNeedConvert2InOp(Op op, BizReq bizReq) {
        Object mvelValue = bizReq.getMvelValue();
        if (Op.Equals == op && mvelValue instanceof JSONArray) {
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
        List<Criterion> conditions = RuleUtils.parseCriterions(map, true, parentJunction, null, extention, responseParam);
        Criterion rootJunctionType = RuleUtils.getRootJunctionByType(map);
        Lhs lhs = null;
        if (!RuleUtils.isHasParent(map)) {
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
        String s = "{\"type\":\"and\",\"ruleName\":\"根组\",\"rule\":[{\"type\":\"and\",\"ruleName\":\"一组\",\"rule\":[{\"requestType\":\"native\",\"operator\":\"==\",\"expectedValue\":\"7-618002,das,das,dsa\",\"conditionIndex\":\"0\",\"dataType\":\"enum\",\"source\":{\"beanId\":\"interface\",\"methodName\":\"\",\"params\":[{\"paramName\":\"\",\"dataType\":\"\",\"value\":\"\"},{\"paramName\":\"\",\"dataType\":\"\",\"value\":\"\"}],\"result\":{\"expressionType\":\"groovy、jsonpath、no、spel\",\"expression\":\"\"}}},{\"requestType\":\"native\",\"operator\":\"==\",\"expectedValue\":\"7-618002,das,das,dsa\",\"conditionIndex\":\"1\",\"dataType\":\"enum\",\"source\":{\"beanId\":\"interface\",\"methodName\":\"\",\"params\":[{\"paramName\":\"\",\"dataType\":\"\",\"value\":\"\"},{\"paramName\":\"\",\"dataType\":\"\",\"value\":\"\"}],\"result\":{\"expressionType\":\"groovy、jsonpath、no、spel\",\"expression\":\"\"}}}]},{\"type\":\"and\",\"ruleName\":\"二组\",\"rule\":[{\"requestType\":\"jsf/native/https\",\"operator\":\"==\",\"expectedValue\":\"7-618002,das,das,dsa\",\"conditionIndex\":\"2\",\"dataType\":\"enum\",\"source\":{\"beanId\":\"interface\",\"methodName\":\"\",\"params\":[{\"paramName\":\"\",\"dataType\":\"\",\"value\":\"\"},{\"paramName\":\"\",\"dataType\":\"\",\"value\":\"\"}],\"result\":{\"expressionType\":\"groovy、jsonpath、no、spel\",\"expression\":\"\"}}}]}]}";
        buildLhsByJson(s, null, null);
    }
}
