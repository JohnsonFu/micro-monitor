package com.edu.nju.flh.service;

import com.edu.nju.flh.entity.po.monitorData;
import com.edu.nju.flh.entity.po.monitorDataListWithCName;
import com.edu.nju.flh.entity.vo.AllMeasVO;
import com.edu.nju.flh.entity.vo.SearchResult;

import java.util.List;

/**
 * Created by fulinhua on 2018/5/13.
 */
public interface MonitorDataService {
    public List<monitorData> getDataByCNameAndFeature(String contName, String feature, int count, int groupbyTimeSecond);
    public List<List<monitorData>> getPer_cpu(String contName, int i, int i1);
    public List<monitorDataListWithCName> getAllDataByFeature(String feature, int i, int i1);
    public List<SearchResult> getSearchResultFromMonitorData(List<monitorDataListWithCName> list);
    public  List<AllMeasVO> getMeasVOFromSearchResult(List<SearchResult> searchResultList);
}
