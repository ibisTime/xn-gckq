package com.cdkj.gckq.http;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.cdkj.gckq.exception.BizException;

import util.PropertiesUtil;
import util.RegexUtils;

public class BizConnecter {

    private static Logger logger = Logger.getLogger(BizConnecter.class);

    public static final String YES = "0";

    public static final String XN_GCHF_URL = PropertiesUtil.Config.XN_GCHF_URL;

    public static String getBizData(String json) {
        String data = null;
        String resJson = null;
        try {
            Properties formProperties = new Properties();
            formProperties.put("json", json);
            resJson = PostSimulater.requestPostForm(XN_GCHF_URL,
                formProperties);
            logger.info("request:code<" + "" + ">  json<" + json + ">\n");
        } catch (Exception e) {
            throw new BizException("Biz000", "链接请求超时，请联系管理员");
        }
        // 开始解析响应json
        String errorCode = RegexUtils.find(resJson, "errorCode\":\"(.+?)\"", 1);
        String errorInfo = RegexUtils.find(resJson, "errorInfo\":\"(.+?)\"", 1);
        logger.info(
            "request:code<" + "" + ">  json<" + json + ">\nresponse:errorCode<"
                    + errorCode + ">  errorInfo<" + errorInfo + ">");
        if (YES.equalsIgnoreCase(errorCode)) {
            data = RegexUtils.find(resJson, "data\":(.*)\\}", 1);
        } else {
            throw new BizException(errorCode, errorInfo);
        }
        return data;
    }

}
