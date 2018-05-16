package com.edu.nju.flh.entity.po;

import lombok.Data;

import java.util.List;

/**
 * Created by fulinhua on 2018/5/10.
 */
@Data
public class monitorDataListWithCName {
    private List<monitorData> data;
    private String name;
}
