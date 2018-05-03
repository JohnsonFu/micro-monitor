package com.edu.nju.flh.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by fulinhua on 2018/5/3.
 */
public class test {
    public static void main(String[] args){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        String date = "2013-01-16T01:24:32Z";
        try {
            Date date1=sdf.parse(date);
            Calendar calendar=Calendar.getInstance();
            calendar.setTime(date1);
            System.out.println(calendar.get(Calendar.YEAR)+"_"+calendar.get(Calendar.MONTH)+"_"+calendar.get(Calendar.DAY_OF_MONTH)+"_"+calendar.get(Calendar.HOUR));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
