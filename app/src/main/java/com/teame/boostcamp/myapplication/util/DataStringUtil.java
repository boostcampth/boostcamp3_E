package com.teame.boostcamp.myapplication.util;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DataStringUtil {

    public final static int _SEC = 60;
    public final static int _MIN = 60;
    public final static int _HOUR = 24;
    public final static int _DAY = 7;
    public final static int _MONTH = 12;


    private DataStringUtil(){

    }
    public static String makeStringComma(String str) {
        if (str.length() == 0)
            return null;
        try{
            long value = Long.parseLong(str);
            DecimalFormat format = new DecimalFormat("###,###");
            return format.format(value);
        }catch (Exception e){
            return null;
        }
    }

    public static String DataFormat(String dataString) {
        String resultString = null;

        resultString = dataString.substring(0, 4).toString() + "년";
        resultString += dataString.substring(4, 6).toString() + "월";
        resultString += dataString.substring(6, 8).toString() + "일";

        return resultString;
    }

    public static String DataCommaFormat(String dataString) {
        String resultString = null;

        resultString = dataString.substring(0, 4)+ ".";
        resultString += dataString.substring(4, 6)+ ".";
        resultString += dataString.substring(6, 8);

        return resultString;
    }

    public static String DataHypenFormat(String dataString) {
        String resultString = null;

        resultString = dataString.substring(0, 4) + "-";
        resultString += dataString.substring(4, 6) + "-";
        resultString += dataString.substring(6, 8);

        return resultString;
    }

    public static String CreateDataWithCheck(Date date) {
        long curTime = System.currentTimeMillis();
        long regTime = date.getTime();
        long diffTime = (curTime - regTime) / 1000;

        String msg = null;
        if (diffTime < _SEC) {
            // sec
            msg = "방금 전";
        } else if ((diffTime /= _SEC) < _MIN) {
            // min
            msg = diffTime + "분 전";
        } else if ((diffTime /= _MIN) < _HOUR) {
            // hour
            msg = (diffTime) + "시간 전";
        } else if ((diffTime /= _HOUR) < _DAY) {
            // day
            msg = (diffTime) + "일 전";
        } else if((diffTime /= _MONTH) < _DAY){
            // month
            msg = (diffTime) + "달 전";
        }
            else {
            SimpleDateFormat aformat = new SimpleDateFormat("yyyy-MM-dd");
            msg = aformat.format(date);
        }
        return msg;
    }
}