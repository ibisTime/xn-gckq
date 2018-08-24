package util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtil {

    public static final String DB_DATE_FORMAT_STRING = "yyyyMMdd";

    public static final String FRONT_DATE_FORMAT_STRING = "yyyy-MM-dd";

    public static final String DATA_TIME_PATTERN_1 = "yyyy-MM-dd HH:mm:ss";

    public static final String DATA_TIME_PATTERN_2 = "yyyy-MM-dd HH:mm";

    public static final String DATA_TIME_PATTERN_3 = "yyyyMMDDhhmmss";

    public static final String DATA_TIME_PATTERN_4 = "yyyyMMDDhhmmss";

    public static final String DATA_TIME_PATTERN_5 = "yyyyMMddHHmmssSSS";

    public static final String DATA_TIME_PATTERN_6 = "yyyy年MM月dd日";

    public static final String DATA_TIME_PATTERN_7 = "HH:mm";

    public static final String DATA_TIME_PATTERN_8 = "yyyy-MM-dd%HH:mm:ss";

    public static final String TIME_BEGIN = " 00:00:00";

    public static final String TIME_MIDDLE = " 12:00:00";

    public static final String TIME_END = " 23:59:59";

    public static Date getStartDatetime(String startDate) {
        Date repayDatetime = DateUtil.strToDate(startDate + DateUtil.TIME_BEGIN,
            DateUtil.DATA_TIME_PATTERN_1);
        return repayDatetime;
    }

    public static Date getEndDatetime(String endDate) {
        Date repayDatetime = DateUtil.strToDate(endDate + DateUtil.TIME_END,
            DateUtil.DATA_TIME_PATTERN_1);
        return repayDatetime;
    }

    public static Date getRelativeDateOfSecond(Date startDate, int second) {
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(startDate);
            calendar.add(Calendar.SECOND, second);
            return calendar.getTime();
        } catch (Exception e) {
            return startDate;
        }
    }

    public static Date getRelativeDateOfMinute(Date startDate, int minute) {
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(startDate);
            calendar.add(Calendar.MINUTE, minute);
            return calendar.getTime();
        } catch (Exception e) {
            return startDate;
        }
    }

    public static Date getRelativeDateOfDays(Date startDate, int days) {
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(startDate);
            calendar.add(Calendar.SECOND, days * 3600 * 24);
            return calendar.getTime();
        } catch (Exception e) {
            return startDate;
        }
    }

    /** 
     * Date按格式pattern转String
     * @param date
     * @param pattern
     * @return 
     * @create: 2015-4-18 下午11:02:34 miyb
     * @history: 
     */
    public static String dateToStr(Date date, String pattern) {
        String str = null;
        SimpleDateFormat formater = new SimpleDateFormat(pattern);
        try {
            str = formater.format(date);
        } catch (Exception e) {
        }
        return str;
    }

    /** 
     * 获取当天开始时间
     * @return 
     * @create: 2014-10-14 下午4:24:57 miyb
     * @history: 
     */
    public static Date getTodayStart() {
        Calendar currentDate = new GregorianCalendar();
        currentDate.set(Calendar.HOUR_OF_DAY, 0);
        currentDate.set(Calendar.MINUTE, 0);
        currentDate.set(Calendar.SECOND, 0);
        return (Date) currentDate.getTime().clone();
    }

    /** 
     * 获取当天结束时间
     * @return 
     * @create: 2014-10-14 下午4:24:57 miyb
     * @history: 
     */
    public static Date getTodayEnd() {
        Calendar currentDate = new GregorianCalendar();
        currentDate.set(Calendar.HOUR_OF_DAY, 23);
        currentDate.set(Calendar.MINUTE, 59);
        currentDate.set(Calendar.SECOND, 59);
        return (Date) currentDate.getTime().clone();
    }

    /**
     * 相对参数today的明日起始时刻。比如今天是11日23点，明日起始时刻为12日0点0分0秒
     * @param today
     * @return 
     * @create: 2015年11月16日 上午11:49:51 myb858
     * @history:
     */
    public static Date getTomorrowStart(Date today) {
        String str = dateToStr(today, FRONT_DATE_FORMAT_STRING);
        Date tommrow = getRelativeDateOfSecond(
            strToDate(str, FRONT_DATE_FORMAT_STRING), 24 * 3600);
        return tommrow;
    }

    /** 
     * String 按格式pattern转Date
     * @param str
     * @param pattern
     * @return 
     * @create: 2015-4-18 下午11:02:34 miyb
     * @history: 
     */
    public static Date strToDate(String str, String pattern) {
        Date date = null;
        SimpleDateFormat formater = new SimpleDateFormat(pattern);
        try {
            date = formater.parse(str);
        } catch (Exception e) {
        }
        return date;
    }

    /**
     * 删除—
     * @param pattern
     * @return 
     * @create: 2015年10月27日 下午7:59:41 myb858
     * @history:
     */
    public static String remove_(String strDate) {
        String string = null;
        try {
            string = strDate.replace("-", "");
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return string;
    }

    /**
     * 按格式获取当前时间
     * @param pattern
     * @return 
     * @create: 2015-5-7 上午11:22:04 miyb
     * @history:
     */
    public static String getToday(String pattern) {
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        return df.format(new Date());
    }

    /**
     * 
     * @param date
     * @param addOneDay 是否加1天
     * @return 
     * @create: 2015-5-7 上午11:25:23 miyb
     * @history:
     */
    public static Date getFrontDate(String date, boolean addOneDay) {
        Date returnDate = null;
        try {
            returnDate = new SimpleDateFormat(FRONT_DATE_FORMAT_STRING)
                .parse(date);
            if (addOneDay) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(returnDate);
                calendar.add(calendar.DATE, 1);// 把日期往后增加一天.整数往后推,负数往前移动
                calendar.add(calendar.SECOND, -1);// 变成23：59：59
                returnDate = calendar.getTime(); // 这个时间就是日期往后推一天的结果
            }
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return returnDate;
    }

    /**
     * 统计两个时间差，返回的是天数(即24小时算一天，少于24小时就为0，用这个的时候最好把小时、分钟等去掉)
     * @param beginStr 开始时间
     * @param endStr 结束时间
     * @param format 时间格式
     * @return
     */
    public static int daysBetween(String beginStr, String endStr,
            String format) {
        Date end = strToDate(endStr, format);
        Date begin = strToDate(beginStr, format);
        long times = end.getTime() - begin.getTime();
        return (int) (times / 60 / 60 / 1000 / 24);
    }

    /**
     * 统计两个时间差，返回的是天数(即24小时算一天，少于24小时就为0，用这个的时候最好把小时、分钟等去掉)
     * @param beginDate
     * @param endDate
     * @return 
     * @create: 2015年11月16日 上午11:20:51 myb858
     * @history:
     */
    public static int daysBetween(Date beginDate, Date endDate) {
        long times = endDate.getTime() - beginDate.getTime();
        return (int) (times / 60 / 60 / 1000 / 24);
    }

    /**
     * 提供格式化时间，统计两个时间差之间返回的天数(即24小时算一天，少于24小时就为0，用这个的时候最好把小时、分钟等去掉)
     * @param beginDate
     * @param endDate
     * @param format
     * @return 
     * @create: 2017年1月8日 下午5:07:50 xieyj
     * @history:
     */
    public static int daysBetweenDate(Date beginDate, Date endDate) {
        String beginStr = DateUtil.dateToStr(beginDate,
            DateUtil.FRONT_DATE_FORMAT_STRING);
        String endStr = DateUtil.dateToStr(endDate,
            DateUtil.FRONT_DATE_FORMAT_STRING);
        Date end = strToDate(endStr, DateUtil.FRONT_DATE_FORMAT_STRING);
        Date begin = strToDate(beginStr, DateUtil.FRONT_DATE_FORMAT_STRING);
        long times = end.getTime() - begin.getTime();
        return (int) (times / 60 / 60 / 1000 / 24);
    }

    public static Date getCurrentMonthFirstDay() {
        Calendar currentDate = new GregorianCalendar();
        currentDate.set(Calendar.DATE, 1);
        currentDate.set(Calendar.HOUR_OF_DAY, 0);
        currentDate.set(Calendar.MINUTE, 0);
        currentDate.set(Calendar.SECOND, 0);
        return (Date) currentDate.getTime().clone();
    }

    public static Date getCurrentMonthLastDay() {
        Date date = new Date();
        Calendar currentDate = Calendar.getInstance();
        currentDate.setTime(date);
        do {
            currentDate.add(Calendar.DATE, 1);
        } while (currentDate.get(Calendar.DATE) != 1);
        currentDate.add(Calendar.DATE, -1);
        currentDate.set(Calendar.HOUR_OF_DAY, 23);
        currentDate.set(Calendar.MINUTE, 59);
        currentDate.set(Calendar.SECOND, 59);
        return currentDate.getTime();
    }

    /**
     * 获取当指定月份第一天
     * @param month
     * @return 
     * @create: 2018年5月11日 下午1:58:15 nyc
     * @history:
     */
    public static Date getFristDay(int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    /**
     * 获取指定月份最后一天
     * @param month
     * @return 
     * @create: 2018年5月11日 下午1:58:15 nyc
     * @history:
     */
    public static Date getLastDay(int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH,
            calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return calendar.getTime();
    }

    /**
     * 获取当指定年份和月份第一天
     * @param year
     * @param month
     * @return 
     * @create: 2018年5月11日 下午1:58:15 nyc
     * @history:
     */
    public static Date getFristDay(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    /**
     * 获取指定年份和月份最后一天
     * @param year
     * @param month
     * @return 
     * @create: 2018年5月11日 下午1:58:15 nyc
     * @history:
     */
    public static Date getLastDay(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH,
            calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return calendar.getTime();
    }

    /**
     * 获取当前月份最后一天
     * @param month
     * @return 
     * @create: 2018年5月11日 下午1:58:15 nyc
     * @history:
     */
    public static Date getLastDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH,
            calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return calendar.getTime();
    }

    /**
     * 当前时间距离下一个月的天数
     * @param month
     * @return 
     * @create: 2018年5月11日 下午1:58:15 nyc
     * @history:
     */
    public static int getRemainDays() {
        Date now = new Date();
        Date lastDay = DateUtil.getLastDay();
        long days = (lastDay.getTime() - now.getTime()) / 60 / 60 / 1000 / 24;
        return (int) days;
    }

    // 获取当前HH：mm
    public static String getNow() {
        Calendar calendar = Calendar.getInstance();
        int time = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        return time + ":" + minute;
    }

    // 获取当前月份
    public static int getMonth() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.MONTH) + 1;
    }

    /**
     * 比较两个时间是否相等
     *  -1  开始小于结束
     *  0 等于
     *  1 大于
     * @param args 
     * @create: 2018年5月5日 下午2:26:44 nyc
     * @history:
     */

    public static boolean compare(String start, String end) {
        boolean flag = false;
        if (start != null) {
            Date startDate = DateUtil.strToDate(start, DATA_TIME_PATTERN_7);
            Date endDate = DateUtil.strToDate(end,
                DateUtil.DATA_TIME_PATTERN_7);
            if (-1 == startDate.compareTo(endDate)) {
                flag = true;
            }
        }
        return flag;
    }

    /**
     * 当前时间是否在指定时间范围内
     * @param startDatetime
     * @param endDatetime
     * @return 
     * @create: 2018年5月8日 下午2:14:27 nyc
     * @history:
     */
    public static boolean isIn(Date startDatetime, Date endDatetime) {
        Date now = new Date();
        if (startDatetime.before(now) && endDatetime.after(now)) {
            return true;
        }
        return false;
    }

    /**
     * 某个时间是否在指定时间端内
     * @param startDatetime
     * @param endDatetime
     * @return 
     * @create: 2018年5月8日 下午2:14:27 nyc
     * @history:
     */
    public static boolean isIn(Date startDatetime, Date checkDatetime,
            Date endDatetime) {
        if (startDatetime.before(checkDatetime)
                && endDatetime.after(endDatetime)) {
            return true;
        }
        return false;
    }

    /**
     * 计算请假时间
     * 不大于工作时间一半视为半天
     * @param start
     * @param end
     * @return 
     * @create: 2018年5月5日 下午5:30:39 nyc
     * @history:
     */
    public static double getDays(String startDatetime, String endDatetime,
            Date leavingStartDate, Date leavingEndDate) {
        double days = 1.0;
        Date start = strToDate(startDatetime, DateUtil.DATA_TIME_PATTERN_7);
        Date end = strToDate(endDatetime, DateUtil.DATA_TIME_PATTERN_7);
        int times = (int) ((end.getTime() - start.getTime()) / 60 / 60 / 1000);
        int leavingDays = (int) ((leavingEndDate.getTime()
                - leavingStartDate.getTime()) / 60 / 60 / 1000);
        if (leavingDays <= times / 2) {
            days = 0.5;
        }

        return days;
    }

    /**
     * 计算迟到、早退小时数
     * 不足一小时算作一小时
     * @param startDatetime
     * @param endDatetime
     * @param leavingStartDate
     * @param leavingEndDate
     * @return 
     * @create: 2018年5月8日 下午8:49:03 nyc
     * @history:
     */
    public static int getHours(Date start, String end) {
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(start);
        Date startDatetime = startCalendar.getTime();
        int year = startCalendar.get(Calendar.YEAR);
        int month = startCalendar.get(Calendar.MONTH);
        int day = startCalendar.get(Calendar.DAY_OF_MONTH);
        int second = startCalendar.get(Calendar.SECOND);

        Calendar endCalendar = Calendar.getInstance();
        String[] time = end.split(":");
        endCalendar.set(year, month, day, StringValidater.toInteger(time[0]),
            StringValidater.toInteger(time[1]), second);

        int minute = (int) Math
            .abs(((endCalendar.getTime().getTime() - startDatetime.getTime())
                    / (59 * 1000)));
        int hours = 0;
        if (minute % 60 == 0) {
            hours = minute / 60;
        } else if (minute % 60 != 0) {
            hours = minute / 60 + 1;
        }
        return hours;
    }

    // 判断时间是否在今天之内
    public static boolean isToday(String date) {
        boolean flag = false;

        Date todayStart = DateUtil.getTodayStart();
        Date todayEnd = DateUtil.getTodayEnd();

        Date checkDate = DateUtil.strToDate(date, DateUtil.DATA_TIME_PATTERN_1);
        if (todayStart.before(checkDate) && todayEnd.after(checkDate)) {
            flag = true;
        }
        return flag;
    }

    /**
      * 获取当月当前剩余天数
      * @param month
      * @return 
      * @create: 2018年6月5日 上午10:04:44 nyc
      * @history:
      */
    public static int getMonthDays() {
        int allDays = DateUtil.getMonthDays(Calendar.MONTH);
        int passDays = Calendar.DAY_OF_MONTH;
        return allDays - passDays;
    }

    /**
     * 获取指定月份天数
     * @param month
     * @return 
     * @create: 2018年6月5日 上午10:04:44 nyc
     * @history:
     */
    public static int getMonthDays(int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.roll(Calendar.DAY_OF_MONTH, -1);
        return calendar.get(Calendar.DATE);
    }

    public static void main(String[] args) throws ParseException {
    }

}
