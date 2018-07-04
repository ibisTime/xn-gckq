package com.cdkj.gckq.http;

import java.util.Properties;

import com.cdkj.gckq.exception.BizException;

import util.PropertiesUtil;

public class BizConnecter {

    public static final String YES = "0";

    public static final String XN_GCHF_URL = PropertiesUtil.Config.XN_GCHF_URL;

    public static String getBizData(String json) {
        try {
            Properties formProperties = new Properties();
            formProperties.put("json", json);
            return PostSimulater.requestPostForm(XN_GCHF_URL, formProperties);
        } catch (Exception e) {
            throw new BizException("Biz000", "链接请求超时，请联系管理员");
        }
    }

}
