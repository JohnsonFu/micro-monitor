package com.edu.nju.flh.entity.vo;

import lombok.Data;

import java.util.List;

/**
 * Created by fulinhua on 2018/5/5.
 */
@Data
public class SearchResult {
    private List<String> xData;
    private List<String> yData;
    private String name;
    private double min;
}
