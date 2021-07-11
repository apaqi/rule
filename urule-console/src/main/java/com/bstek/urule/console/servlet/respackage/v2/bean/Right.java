package com.bstek.urule.console.servlet.respackage.v2.bean;

import java.io.Serializable;

/**
 * @author wpx
 * @version 1.0.0
 * @Description TODO
 * @since 2021/07/09
 */
public class Right implements Serializable {
    /**
     * 参数
     */
    private Param[] param;
    /**
     * 请求URL
     */
    private String url;
    /**
     * 协议
     */
    private String protocol;

    public Param[] getParam() {
        return param;
    }

    public Right setParam(Param[] param) {
        this.param = param;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public Right setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getProtocol() {
        return protocol;
    }

    public Right setProtocol(String protocol) {
        this.protocol = protocol;
        return this;
    }
}
