package com.edu.nju.flh.service;

import com.edu.nju.flh.entity.monitorData;
import com.edu.nju.flh.entity.monitorDataListWithCName;

import java.util.List;

/**
 * Created by fulinhua on 2018/5/13.
 */
public interface MonitorDataService {
    public List<monitorData> getDataByCNameAndFeature(String contName, String feature, int count, int groupbyTimeSecond);
    public List<List<monitorData>> getPer_cpu(String contName, int i, int i1);
    public List<monitorDataListWithCName> getAllDataByFeature(String feature, int i, int i1);
}
