package com.cdkj.gckq.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URLDecoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.cdkj.gckq.exception.BizException;
import com.cdkj.gckq.http.BizConnecter;
import com.xinai.core.WgMjController;

import util.PropertiesUtil;
import util.StringValidater;

@Controller
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

    // 离线考勤文件路劲
    private static String path = null;
    static {
        try {
            path = Thread.currentThread().getContextClassLoader()
                .getResource("").toURI().getPath();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 离线考勤文件名称
    private static final String attendanceFile = "attendance.txt";

    @RequestMapping(value = "/receive-attend", method = RequestMethod.GET)
    public void doClockIn(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String result = "{\"result\":false}";
        try {
            String param = URLDecoder.decode(request.getQueryString(), "UTF-8");
            JSONObject json = JSONObject.parseObject(param);
            String sim = json.getString("sim");
            String id = json.getString("id");

            logger.info("=========员工" + id + "人脸相似度：" + sim + "============");
            if (StringValidater.toDouble(sim) > StringValidater
                .toDouble(Score)) {
                logger.info(" ==========人脸识别成功，打开闸机==============");
                openDoor();

                logger.info("  =========上传考勤记录==============");
                result = BizConnecter.getBizData(param);
            }

            // 将离线考勤记录上传到服务器
            File file = new File(path + "/" + attendanceFile);
            if (file.exists()) {
                uploadOfflineAttendance();
            }
        } catch (Exception e) {
            if (e instanceof JSONException) {
                logger.info("===========参数错误!==============");
            }
            if (e instanceof BizException) {
                logger.info("===========链接请求超时，生成离线考勤记录==============");
                createAttendanceFile(request);
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

    // 将离线考勤记录上传到服务器
    private void uploadOfflineAttendance()
            throws FileNotFoundException, IOException {
        logger.info("===========开始上传离线考勤记录==============");
        File file = new File(path + "/" + attendanceFile);
        InputStreamReader reader = new InputStreamReader(
            new FileInputStream(file));
        BufferedReader bufferedReader = new BufferedReader(reader);
        String param = null;
        param = bufferedReader.readLine();
        while (param != null) {
            BizConnecter.getBizData(param);
            param = bufferedReader.readLine();
        }
        bufferedReader.close();
        file.delete();
        logger.info("===========离线考勤记录上传完成==============");
    }

    // 开闸机
    private void openDoor() throws Exception {
        WgMjController controller = new WgMjController();
        controller.setControllerSN(StringValidater.toInteger(SN));
        controller.setIP(IP);
        controller.setPORT(StringValidater.toInteger(PORT));
        controller.RemoteOpenDoorIP(StringValidater.toInteger(DoorIP));
    }

    // 创建离线考勤文件
    private void createAttendanceFile(HttpServletRequest request) {
        try {
            File attendance = new File(path, attendanceFile);
            attendance.createNewFile();

            byte attendanceBt[] = new byte[1024];
            attendanceBt = (URLDecoder.decode(request.getQueryString(), "UTF-8")
                    + "\r\n").getBytes();
            FileOutputStream attendanceIn = new FileOutputStream(attendance,
                true);
            attendanceIn.write(attendanceBt, 0, attendanceBt.length);
            attendanceIn.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
