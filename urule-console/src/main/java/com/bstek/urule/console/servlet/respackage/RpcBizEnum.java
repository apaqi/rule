package com.bstek.urule.console.servlet.respackage;


public enum RpcBizEnum {
    /**各个条件处理类**/
    MKT_RULE("mkt_rule", "营销规则中心","0","mktRuleBiz"),
    DB_PINS("db_pins", "人群标签命中","0","dataCenterPinsBiz"),
    DB_TAGS("db_tags", "人群标签属性","0","dataCenterTagBiz"),
    SD_SMS("sd_sms", "鹰眼短信","2","smsBiz"),
    ZN_SD_SMS("zn_sd_sms", "智能短信控件", "2","znSmsBiz"),
    TAG_VAR("tag_var", "变量标签","0","varTagBiz"),
    SD_JPUSH("sd_jpush", "鹰眼push","2","jpushBiz"),
    ZN_PUSH("zn_push", "智能push", "2","znPushBiz"),
    SD_RECOM("recom", "推荐控件", "2","recomBiz"),
    GROUP_ADD("add_group", "加入群组","3","addGroupBiz"),
    GROUP_DEL("del_group", "移除群组","3","delGroupBiz"),
    GROUP_HIT("hit_group", "命中群组","0","hitGroupBiz"),
    MKT_PRICE("mkt_price", "营销发券","1","mktPriceBiz"),
    WAIT("wait", "等待","0","waitBiz"),
    END("end", "结束","0","endBiz"),
    HUB("hub", "承接","1","hubBiz"),
    CUSTOM_RULE("custom_rule","自定义规则","0","customRuleBiz"),
    CUSTOMER("customer","客服呼叫","2","kfCallResultBiz"),
    BURIED_POINT("buried_point","埋点控件","3","buriedPointBiz"),
    START("start", "开始","0","startBiz"),
    ABTEST("abtest","AB测试","0","abtestBiz"),
    RUN_VARIABLE("run_variable","变量控件","0","runVariableBiz"),
    INSURANCE_TYPE("insurance_type","保险种类","0","insuranceTypeBiz"),
    MALL_TRADE("mall_trade","商城交易","0","mallTradeBiz"),
    MODEL_TAG("model_tag","模型标签","0","modelTagBiz"),
    TELE_MARKETING("tele_marketing","电销","1","teleMarketingBiz"),
    MOCK("mock","模拟执行","0","mockBiz"),
    /**这个没有对应的biz处理类 **/
    MOCK_TOUCH("mock_touch","模拟触达","0",null),
    WEALTH_TAGS("wealth_tags","财富标签","0","wealthTagsBiz"),
    ACT_RULE("act_rule","活动规则","0","actRuleBiz"),
    DUP_RULE("dupRule","防重标签","0","dupRuleBiz"),
    FEATURE_ENHANCE("feature_enhance","离线特征","0","featureEnhanceBiz"),
    AISTAT_TAG("aistat_tags","AI客服外呼挂机意图","0","aiExportStatBiz"),
    JT_SUCCESS_ORDER_COUNT("jt_success_order_count","金条成单量","0","jtOrderSuccessCountBiz"),
    BAITIAO_ORDER("btOrderCount","白条订单","0","baiTiaoBiz"),
    WECHAT("weChat","微信控件","3","weChatBiz"),
    RTA("rta","外投广告控件","3","rtaBiz"),
    JT_ACTIVE_TAG("jt_active_tag","金条激活","3","jtActiveTagBiz"),
    JT_LOAN_TAG("jt_loan_tag","金条首贷","3","jtLoanTagBiz"),
    BAITIAO_INSTALMENTS_COUPON("bt_instalments_coupon","是否有白条分期优惠券","3","baiTiaoInstalmentsCoupon"),
    BAITIAO_OUTSTANDING_BILL_AMOUNT("bt_outstanding_bill_amount","白条未出账订单总金额","3","baiTiaoOutstandingBillAmount"),
    BAITIAO_OUTSTANDING_BILL_COUNT("bt_outstanding_bill_count","白条未出账订单量","3","baiTiaoOutstandingBillCount"),
    BAITIAO_NEW_ACCESS_USER("bt_new_access_user", "是否白条拉新准入用户", "3","baiTiaoNewAccessUser"),
    JR_MEMBER_LEVEL("jr_member_level","APP会员等级","3","jRMemberLevel"),
    JR_MEMBER_PREROGATIVE("jr_member_prerogative","APP会员权益","0","prerogativeBiz"),
    HOLD_COUPON("hold_coupon", "用户是否持有优惠券", "3","userCouponQueryBiz"),
    SMS_MALL("sms_mall", "商城短信","2","smsMallBiz"),
    EXTEND_WARRANTY("extend_warranty", " 是否可补购延保服务", "3","extendWarrantyBiz"),
    SD_JMAIL("sd_jmail", "鹰眼站内信","2","mailBiz"),
    DATA_INTERACTION("data_interaction","数据交互","2","dataInteractionBiz"),
    MATERIAL_PUSH("material_push","资源位推荐","2","materialPush"),
    EXPIRING_COUPON_CHECK("expiring_coupon_check","用户即将到期的券列表内是否含有目标利益之一","2","expiringCouponCheckBiz"),
    IF_USER_BIZ("ifUser","条件控件","2","ifUserBiz"),
    ;

    private String code;
    private String desc;
    private String testKey;
    private String handleBeanId;

    RpcBizEnum(String code, String desc , String testKey, String handleBeanId) {
        this.code = code;
        this.desc = desc;
        this.testKey = testKey;
        this.handleBeanId = handleBeanId;
    }
    /*========================================根据code获取相应枚举项========================================*/
    public static RpcBizEnum getByCode(String code) {
        if (code == null) {
            return null;
        }
        for (RpcBizEnum cur : RpcBizEnum.values()) {
            if (cur.getCode().equals(code)) {
                return cur;
            }
        }
        return null;
    }
    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public String getTestKey() {
        return testKey;
    }

    public String getHandleBeanId() {
        return handleBeanId;
    }
}
