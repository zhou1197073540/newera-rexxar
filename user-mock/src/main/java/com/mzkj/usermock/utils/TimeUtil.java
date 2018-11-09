package com.mzkj.usermock.utils;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

import static java.time.ZoneId.SHORT_IDS;

/**
 * Created by daniel.luo on 2017/5/25.
 */
public class TimeUtil {
    public static String convertTime(String oriTime) {
        LocalDate fmtTime = null;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy", Locale.ENGLISH);
            fmtTime = LocalDate.parse(oriTime, formatter);
            System.out.printf("Successfully parsed String %s, date is %s%n", oriTime, fmtTime);
        } catch (DateTimeParseException ex) {
            System.out.printf("%s is not parsable!%n", oriTime);
            ex.printStackTrace();
        }
        if (fmtTime == null) return "";
        else return fmtTime.toString();
    }

    public static LocalDate convertDateTime(String oriTime) {
        LocalDate fmtTime = null;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy", Locale.ENGLISH);
            fmtTime = LocalDate.parse(oriTime, formatter);
            System.out.printf("Successfully parsed String %s, date is %s%n", oriTime, fmtTime);
        } catch (DateTimeParseException ex) {
            System.out.printf("%s is not parsable!%n", oriTime);
            ex.printStackTrace();
        }
        if (fmtTime == null) return null;
        else return fmtTime;
    }

    /**
     * 把一个字符串类型转换成下一天的date
     *
     * @param oriDate 如："2017-07-24"
     * @return 如"2017-07-25"
     */
    public static String nextDate(String oriDate) {
        LocalDate fmtTime = null;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            fmtTime = LocalDate.parse(oriDate, formatter);
        } catch (DateTimeParseException ex) {
            System.out.printf("%s is not parsable!%n", oriDate);
            ex.printStackTrace();
        }
        return fmtTime.plusDays(1).toString();
    }

    /**
     * @param delta （因为美股收盘的时候是凌晨四点，我们当时已经是第二天）所以添加天的偏移量delta，-1代表前一天，0代表当天，1代表后一天
     * @return dateStr
     */
    public static String getDate(int delta) {
        LocalDate today = LocalDate.now();
        if (delta < 0) return today.minusDays(Math.abs(delta)).toString();
        else if (delta > 0) return today.plusDays(delta).toString();
        else return today.toString();
    }

    public static String getDateByTimeZone(String timeZone) {
        LocalDate date = LocalDate.now(ZoneId.of(SHORT_IDS.get(timeZone)));
        return date.toString();
    }

    /**
     * 当前日期返回 yyyy-MM-dd HH:mm:ss 格式
     *
     * @return 例如：2017-06-15 15:50:43
     */
    public static String getDateTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime time = LocalDateTime.now();
        return time.format(formatter);
    }
    /**
     * 当前日期返回 yyyy-MM-dd HH:mm:ss 格式
     *
     * @return 例如：2017-06-15 15:50:43
     */
    public static String getDateTimes() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        LocalDateTime time = LocalDateTime.now();
        return time.format(formatter);
    }

    /**
     * 当前日期返回 yyyy-MM-dd 格式
     *
     * @return 例如：2017-06-15
     */
    public static String getCurDateTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime time = LocalDateTime.now();
        return time.format(formatter);
    }
    public static String getCurDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDateTime time = LocalDateTime.now();
        return time.format(formatter);
    }

    /**
     * 获取当前的时间戳，精确到毫秒如：1497926312513
     *
     * @return
     */
    public static String getTimeStamp() {
        Timestamp ts = Timestamp.valueOf(LocalDateTime.now());
        return String.valueOf(ts.getTime());
    }

    public static long getTimeStampAsLong() {
        return Timestamp.valueOf(LocalDateTime.now()).getTime();
    }

    public static void sleep(long miliseconds) {
        try {
            Thread.sleep(miliseconds);
        } catch (Exception e) {

        }
    }

    /**
     *
     * @return
     */
    public static String addSubDate(int num){
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        if(num<0) return formatter.format(today.minusDays(Math.abs(num)));
        else return formatter.format(today.plusDays(num));
    }

    /**
     * 返回当前时刻的时分秒
     * @return
     */
    public static String getHourMinSec() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalDateTime time = LocalDateTime.now();
        return time.format(formatter);
    }

    public static String isNine(){
        String nine="09:00:00";
        String nowTime=getHourMinSec();
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        if(nowTime.compareTo(nine)<0){
            LocalDate date=today.minusDays(Math.abs(-1));
            return formatter.format(date);
        }else{
            return formatter.format(today);
        }
    }

    /**
     * datetime日期 yyyy-MM-dd 格式
     */
    public static boolean isNextDay(String datetime) {
        if(!StringUtil.isEmpty(datetime)&&datetime.length()>=10){
            String settlement_time=datetime.substring(0,10);
            return TimeUtil.getCurDateTime().compareTo(settlement_time) > 0;
        }
        return false;
    }

    public static void main(String[] args) {
        System.out.println(addSubDate(0));
    }
}
