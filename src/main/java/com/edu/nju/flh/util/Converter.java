package com.edu.nju.flh.util;

import com.edu.nju.flh.entity.SearchResult;
import com.edu.nju.flh.entity.monitorData;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by fulinhua on 2018/5/5.
 */
public class Converter {
    public static SearchResult convertToSearchResult(List<monitorData> dataList){
        SearchResult searchResult=new SearchResult();
        List<String> xData = dataList.stream().map(data -> data.getTime()).collect(Collectors.toList());
        List<String> yData = dataList.stream().map(monitorData -> {
            double d = monitorData.getValue();
            if (Math.abs(d + 1) < 0.01) {
                return "";
            } else {
                d = d / 31536000;
            }
            return d + "";
        }).collect(Collectors.toList());
        dataList = dataList.stream().filter(monitorData -> Math.abs(monitorData.getValue() + 1) > 0.01).collect(Collectors.toList());
        double min = Collections.min(dataList.stream().map(monitorData -> monitorData.getValue()).collect(Collectors.toList())) / 31536000;
        searchResult.setXData(xData);
        searchResult.setYData(yData);
        searchResult.setMin(min);
        return searchResult;
    }
}