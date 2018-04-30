package com.edu.nju.flh.entity;

import lombok.Data;

/**
 * Created by fulinhua on 2018/4/30.
 */
@Data
public class monitorData {
    private String time;
    private String cName;
    private int instance;
    private String machine;
    private long value;
}
