package com.edu.nju.flh.service.impl;

import com.edu.nju.flh.dao.BaseDao;
import com.edu.nju.flh.entity.po.monitorData;
import com.edu.nju.flh.entity.po.monitorDataListWithCName;
import com.edu.nju.flh.entity.vo.AllMeasVO;
import com.edu.nju.flh.entity.vo.SearchResult;
import com.edu.nju.flh.service.MonitorDataService;
import com.edu.nju.flh.util.Converter;
import org.apache.commons.collections.CollectionUtils;
import org.influxdb.dto.QueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by fulinhua on 2018/5/13.
 */
@Service
public class MonitorDataServiceImpl implements MonitorDataService {
    @Autowired
    private BaseDao baseDao;
    @Override
    public List<monitorData> getDataByCNameAndFeature(String contName, String feature, int count, int groupbyTimeSecond) {
        LinkedList<monitorData> monitorDatas=new LinkedList<>();
        String sql="select mean(value) from "+feature+" where time > now() - 1d and container_name = '"+contName+"' group by time("+groupbyTimeSecond+"s) order by time desc limit "+count;
        System.out.println(sql);
        List<QueryResult.Result> results = baseDao.query(sql);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        if (CollectionUtils.isNotEmpty(results)) {
            for (QueryResult.Result result : results) {
                List<QueryResult.Series>seriesList=result.getSeries();
                if(Objects.isNull(seriesList)||seriesList.size()==0){
                    return monitorDatas;
                }
                List<List<Object>> objectList=seriesList.get(0).getValues();
                for(List<Object> l:objectList){
                    monitorData monitorData=new monitorData();
                    try {
                     //   String[] strings=l.toString().replace("[","").replace("]","").replace(" ","").split(",");
                        Date date=sdf.parse(l.get(0).toString());
                        date.setHours(date.getHours()+8);
                        if(!Objects.isNull(l.get(1))) {
                            Double value = Double.parseDouble(l.get(1).toString());
                            monitorData.setValue(value);
                        }else{
                            monitorData.setValue(-1);
                        }
                        Calendar calendar=Calendar.getInstance();
                        calendar.setTime(date);
                        String time=calendar.get(Calendar.YEAR)+"-"+(calendar.get(Calendar.MONTH)+1)+"-"+calendar.get(Calendar.DAY_OF_MONTH)+" "+calendar.get(Calendar.HOUR_OF_DAY)+":"+calendar.get(Calendar.MINUTE)+":"+calendar.get(Calendar.SECOND);
                        monitorData.setTime(time);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    monitorDatas.addFirst(monitorData);
                }
            }

        }
        return monitorDatas;
    }

    @Override
    public List<List<monitorData>> getPer_cpu(String contName, int count, int groupbyTimeSecond) {
        List<List<monitorData>> monitorDatas=new ArrayList<>();
        String sql="select mean(value) from cpu_usage_per_cpu "+" where time > now() - 1d and container_name = '"+contName+"' group by time("+groupbyTimeSecond+"s),instance order by time desc limit "+count;
        System.out.println(sql);
        List<QueryResult.Result> results = baseDao.query(sql);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        if (CollectionUtils.isNotEmpty(results)) {
            for (QueryResult.Result result : results) {
                List<QueryResult.Series>seriesList=result.getSeries();
                if(Objects.isNull(seriesList)||seriesList.size()==0){
                    return monitorDatas;
                }
                List<List<Object>> objectList1=seriesList.get(0).getValues();
                LinkedList<monitorData> monitorDatas1=new LinkedList<>();
                for(List<Object> l:objectList1){
                    monitorData monitorData=new monitorData();
                    try {
                      //  String[] strings=l.toString().replace("[","").replace("]","").replace(" ","").split(",");
                        Date date=sdf.parse(l.get(0).toString());
                        date.setHours(date.getHours()+8);
                        if(!Objects.isNull(l.get(1))) {
                            Double value = Double.parseDouble(l.get(1).toString());
                            monitorData.setValue(value);
                        }else{
                            monitorData.setValue(-1);
                        }
                        Calendar calendar=Calendar.getInstance();
                        calendar.setTime(date);
                        String time=calendar.get(Calendar.YEAR)+"-"+(calendar.get(Calendar.MONTH)+1)+"-"+calendar.get(Calendar.DAY_OF_MONTH)+" "+calendar.get(Calendar.HOUR_OF_DAY)+":"+calendar.get(Calendar.MINUTE)+":"+calendar.get(Calendar.SECOND);
                        monitorData.setTime(time);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    monitorDatas1.addFirst(monitorData);
                }

                List<List<Object>> objectList2=seriesList.get(1).getValues();
                LinkedList<monitorData> monitorDatas2=new LinkedList<>();
                for(List<Object> l:objectList2){
                    monitorData monitorData=new monitorData();
                    try {
                     //   String[] strings=l.toString().replace("[","").replace("]","").replace(" ","").split(",");
                        Date date=sdf.parse(l.get(0).toString());
                        date.setHours(date.getHours()+8);
                        if(!Objects.isNull(l.get(1))) {
                            Double value = Double.parseDouble(l.get(1).toString());
                            monitorData.setValue(value);
                        }else{
                            monitorData.setValue(-1);
                        }
                        Calendar calendar=Calendar.getInstance();
                        calendar.setTime(date);
                        String time=calendar.get(Calendar.YEAR)+"-"+(calendar.get(Calendar.MONTH)+1)+"-"+calendar.get(Calendar.DAY_OF_MONTH)+" "+calendar.get(Calendar.HOUR_OF_DAY)+":"+calendar.get(Calendar.MINUTE)+":"+calendar.get(Calendar.SECOND);
                        monitorData.setTime(time);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    monitorDatas2.addFirst(monitorData);
                }
                monitorDatas.add(monitorDatas2);
                monitorDatas.add(monitorDatas1);
            }

        }
        return monitorDatas;
    }

    @Override
    public List<monitorDataListWithCName> getAllDataByFeature(String feature, int count, int groupbyTimeSecond){
        List<monitorDataListWithCName> monitorDatas=new ArrayList<>();
        String sql="select mean(value) from "+feature+" where time > now() - 1d group by time("+groupbyTimeSecond+"s),container_name order by time desc limit "+count;
        System.out.println(sql);
        List<QueryResult.Result> results = baseDao.query(sql);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        if (CollectionUtils.isNotEmpty(results)) {
            for (QueryResult.Result result : results) {
                List<QueryResult.Series>seriesList=result.getSeries();
                if(Objects.isNull(seriesList)||seriesList.size()==0){
                    return monitorDatas;
                }
                for(int i=0;i<seriesList.size();i++){
                    List<List<Object>> objectList1=seriesList.get(i).getValues();
                    monitorDataListWithCName dataListWithCName=new monitorDataListWithCName();
                    String name=seriesList.get(i).getTags().get("container_name");
                    dataListWithCName.setName(name);
                    LinkedList<monitorData> monitorDatas1=new LinkedList<>();
                    for(List<Object> l:objectList1){
                        monitorData monitorData=new monitorData();
                        try {
                         //   String[] strings=l.toString().replace("[","").replace("]","").replace(" ","").split(",");
                            Date date=sdf.parse(l.get(0).toString());
                            date.setHours(date.getHours()+8);
                            if(!Objects.isNull(l.get(1))) {
                                Double value = Double.parseDouble(l.get(1).toString());
                                monitorData.setValue(value);
                            }else{
                                monitorData.setValue(-1);
                            }
                            Calendar calendar=Calendar.getInstance();
                            calendar.setTime(date);
                            String time=calendar.get(Calendar.YEAR)+"-"+(calendar.get(Calendar.MONTH)+1)+"-"+calendar.get(Calendar.DAY_OF_MONTH)+" "+calendar.get(Calendar.HOUR_OF_DAY)+":"+calendar.get(Calendar.MINUTE)+":"+calendar.get(Calendar.SECOND);
                            monitorData.setTime(time);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        monitorDatas1.addFirst(monitorData);
                    }
                    dataListWithCName.setData(monitorDatas1);
                    monitorDatas.add(dataListWithCName);
                }

            }
        }
        return monitorDatas;
    }

    @Override
    public List<SearchResult> getSearchResultFromMonitorData(List<monitorDataListWithCName> dataList) {
      return dataList.stream().map(list-> Converter.convertToSearchResult(list)).collect(Collectors.toList());
    }

    @Override
    public List<AllMeasVO> getMeasVOFromSearchResult(List<SearchResult> searchResultList) {
        return searchResultList.stream().map(searchResult -> {
            AllMeasVO vo=new AllMeasVO();
            vo.setName(searchResult.getName());
            vo.setData(searchResult.getYData());
            return vo;
        }).collect(Collectors.toList());
    }
}
