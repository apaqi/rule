package com.bstek.urule.console.servlet.knowledge.domain;

import java.io.Serializable;

/**
 * @version 1.0.0
 * @Description TODO
 * @since 2021/07/09
 */
public class LeftVO implements Serializable {
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

    public LeftVO setParam(Param[] param) {
        this.param = param;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public LeftVO setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getProtocol() {
        return protocol;
    }

    public LeftVO setProtocol(String protocol) {
        this.protocol = protocol;
        return this;
    }
}
