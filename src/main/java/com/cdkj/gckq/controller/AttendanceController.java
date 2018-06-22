package com.cdkj.gckq.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSONObject;
import com.cdkj.gckq.http.BizConnecter;
import com.xinai.core.WgMjController;

import util.PropertiesUtil;
import util.StringValidater;

@Controller
public class AttendanceController {

    // POST 考勤信息URL
    public static final String XN_GCHF_URL = PropertiesUtil.Config.XN_GCHF_URL;

    // SN号
    public static final String SN = PropertiesUtil.Config.SN;

    // IP，配在同一局域网
    public static final String IP = PropertiesUtil.Config.IP;

    // 端口号
    public static final String PORT = PropertiesUtil.Config.PORT;

    // 闸机门编号
    public static final String DoorIP = PropertiesUtil.Config.DoorIP;

    @RequestMapping(value = "/receive-attend", method = RequestMethod.GET)
    public void doClockIn(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        System.out.println("lalla");
        String str = URLDecoder.decode(request.getQueryString(), "UTF-8");
        JSONObject json = JSONObject.parseObject(str);
        String sim = json.getString("sim");
        System.out.println(json.getString("sim"));
        String result = null;
        // 可配
        if (StringValidater.toDouble(sim) > 70.0) {
            // 上传考勤记录
            result = BizConnecter.getBizData(str);
            JSONObject resultJson = JSONObject.parseObject(result);
            // true 成功 false 失败
            if ("true".equals(resultJson.getString("result"))) {
                // 打开闸机
                WgMjController controller = new WgMjController();
                controller.setControllerSN(StringValidater.toInteger(SN));
                controller.setIP(SN);
                controller.setPORT(StringValidater.toInteger(PORT));
                controller.RemoteOpenDoorIP(StringValidater.toInteger(DoorIP));
            }

        }

        PrintWriter writer;
        try {
            writer = response.getWriter();
            writer.append(result);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
