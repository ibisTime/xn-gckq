package com.cdkj.gckq.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSONObject;
import com.cdkj.gckq.http.BizConnecter;
import com.xinai.core.WgMjController;

import util.DateUtil;
import util.PropertiesUtil;
import util.StringValidater;

@Controller
@Component("attendanceController")
public class AttendanceController {
    private static final Log logger = LogFactory
        .getLog(AttendanceController.class);

    // SN号
    private static final String SN = PropertiesUtil.Config.SN;

    // IP，配在同一局域网
    private static final String IP = PropertiesUtil.Config.IP;

    // 端口号
    private static final String PORT = PropertiesUtil.Config.PORT;

    // 闸机门编号
    private static final String DoorIP = PropertiesUtil.Config.DoorIP;

    // 识别分值
    private static final String Score = PropertiesUtil.Config.Score;

    // 人脸识别软件路径
    private static final String AttendProcesserPath = PropertiesUtil.Config.AttendProcesserPath;

    private WgMjController controller = new WgMjController();

    @RequestMapping(value = "/receive-attend", method = RequestMethod.GET)
    public void doClockIn(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String result = "{\"result\":false}";

        try {
            String param = URLDecoder.decode(request.getQueryString(), "UTF-8");

            JSONObject json = JSONObject.parseObject(param);
            String sim = json.getString("sim");

            if (StringValidater.toDouble(sim) > StringValidater
                .toDouble(Score)) {
                logger.info(" ==========人脸识别成功，打开闸机==============");
                openDoor();

                logger.info("  =========上传考勤记录==============");
                result = BizConnecter.getBizData(param);
            }
        } catch (Exception e) {
            e.printStackTrace();
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

    // 将离线考勤记录上传到服务器
    @SuppressWarnings("unused")
    private void uploadAttendanceRecord()
            throws FileNotFoundException, IOException {
        logger.info("===========开始上传离线考勤记录==============");
        File file = new File(AttendProcesserPath);
        File[] files = file.listFiles();
        if (files == null) {
            return;
        }

        String fileDate = DateUtil.dateToStr(new Date(),
            DateUtil.FRONT_DATE_FORMAT_STRING);
        for (File attendanceFile : files) {
            if (attendanceFile.isFile()
                    && attendanceFile.getName().contains(fileDate)) {
                InputStreamReader reader = new InputStreamReader(
                    new FileInputStream(attendanceFile), "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(reader);
                String param = bufferedReader.readLine();
                while (null != param) {
                    BizConnecter.getBizData(param);
                    param = bufferedReader.readLine();
                }
                bufferedReader.close();
                attendanceFile.delete();
            }
        }
        logger.info("===========离线考勤记录上传完成==============");
    }

    // 开闸机
    private void openDoor() throws Exception {
        // WgMjController controller = new WgMjController();
        controller.setControllerSN(StringValidater.toInteger(SN));
        controller.setIP(IP);
        controller.setPORT(StringValidater.toInteger(PORT));
        controller.RemoteOpenDoorIP(StringValidater.toInteger(DoorIP));
    }
}
