package com.offcn.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AppDateUtils {

    public static String getFormatTime(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(new Date());
    }
    //根据指定格式化方式，获取日期时间格式化字符串
    public static String getFormatTime(String pattern){
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        return dateFormat.format(new Date());
    }

    //根据指定格式化方式，指定时间，获取日期格式化字符串
    public static String getFormatTime(String pattern,Date date){
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        return dateFormat.format(date);
    }
}
