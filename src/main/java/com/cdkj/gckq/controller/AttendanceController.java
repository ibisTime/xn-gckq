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

import util.StringValidater;

@Controller
public class AttendanceController {

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
            result = BizConnecter.getBizData(str);

            // 打开闸机
            WgMjController controller = new WgMjController();
            controller.setControllerSN(200023568);
            controller.setIP("192.168.1.224");
            controller.setPORT(60000);
            controller.RemoteOpenDoorIP(1);

        }
        // true 成功 false 失败
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
