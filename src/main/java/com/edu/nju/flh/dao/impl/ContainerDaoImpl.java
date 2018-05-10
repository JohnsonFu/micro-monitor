package com.edu.nju.flh.dao.impl;

import com.edu.nju.flh.dao.ContainerDao;
import com.edu.nju.flh.entity.container;
import com.edu.nju.flh.entity.monitorData;
import com.edu.nju.flh.entity.monitorDataListWithCName;
import org.apache.commons.collections.CollectionUtils;
import org.influxdb.InfluxDB;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by fulinhua on 2018/4/22.
 */
@Repository
public class ContainerDaoImpl implements ContainerDao {
    @Autowired
    private InfluxDB influxDb;
    @Value("${cadvisor.database}")
    private String database;
    @Value("${cadvisor.listAllContainerSQL}")
    private String listAllContainerSQL;

    @Value("${cadvisor.listAllMeasSQL}")
    private String listAllMeasSQL;

    public void query(String command) {
        QueryResult queryResult = influxDb.query(new Query(command, database));
        List<QueryResult.Result> results = queryResult.getResults();
        if (CollectionUtils.isNotEmpty(results)) {
            for (QueryResult.Result result : results) {
                System.out.println(result.toString());
            }
        }
    }

    @Override
    public List<container> listAllContainers() {
        List<container> list=new ArrayList<>();
        QueryResult queryResult = influxDb.query(new Query(listAllContainerSQL, database));
        List<QueryResult.Result> results = queryResult.getResults();
        if (CollectionUtils.isNotEmpty(results)) {
            for (QueryResult.Result result : results) {
               List<QueryResult.Series>seriesList=result.getSeries();
                if(seriesList.size()==0){
                    return list;
                }
                List<List<Object>> objectList=seriesList.get(0).getValues();
                for(List<Object> l:objectList){
                    for(Object object:l){
                        container cont = transferToContainer(object.toString());
                        list.add(cont);
                    }
                }
            }
        }
    return list;
}

    @Override
    public List<String> listAllMeasurements() {
        List<String> list=new ArrayList<>();
        QueryResult queryResult = influxDb.query(new Query(listAllMeasSQL, database));
        List<QueryResult.Result> results = queryResult.getResults();
        if (CollectionUtils.isNotEmpty(results)) {
            for (QueryResult.Result result : results) {
                //System.out.println(result);
                List<QueryResult.Series>seriesList=result.getSeries();
                if(seriesList.size()==0){
                    return list;
                }
                List<List<Object>> objectList=seriesList.get(0).getValues();
                for(List<Object> l:objectList){
                    for(Object object:l){
                        list.add(object.toString());
                    }
                }
            }
        }
        return list;
    }

    @Override
    public List<monitorData> queryDataByCNameAndFeature(String contName, String feature,int count,int groupbyTimeSecond) {
        LinkedList<monitorData> monitorDatas=new LinkedList<>();
        String sql="select mean(value) from "+feature+" where time > now() - 1d and container_name = '"+contName+"' group by time("+groupbyTimeSecond+"s) order by time desc limit "+count;
       // String sql="select value from "+feature+" where time > now() - 1d and container_name = '"+contName+"' order by time  limit "+count;
        System.out.println(sql);
       // String sql="select * from "+feature+" where container_name='"+contName+"' limit "+count;
        QueryResult queryResult = influxDb.query(new Query(sql, database));
        List<QueryResult.Result> results = queryResult.getResults();
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
                            String[] strings=l.toString().replace("[","").replace("]","").replace(" ","").split(",");
                            Date date=sdf.parse(strings[0].toString());
                            date.setHours(date.getHours()+8);
                            if(!Objects.equals(strings[1],"null")) {
                                Double value = Double.parseDouble(strings[1].toString());
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
    public List<List<monitorData>> queryPer_cpu(String contName, int count, int groupbyTimeSecond) {
        List<List<monitorData>> monitorDatas=new ArrayList<>();
        String sql="select mean(value) from cpu_usage_per_cpu "+" where time > now() - 1d and container_name = '"+contName+"' group by time("+groupbyTimeSecond+"s),instance order by time desc limit "+count;
        // String sql="select value from "+feature+" where time > now() - 1d and container_name = '"+contName+"' order by time  limit "+count;
        System.out.println(sql);
        // String sql="select * from "+feature+" where container_name='"+contName+"' limit "+count;
        QueryResult queryResult = influxDb.query(new Query(sql, database));
        List<QueryResult.Result> results = queryResult.getResults();
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
                        String[] strings=l.toString().replace("[","").replace("]","").replace(" ","").split(",");
                        Date date=sdf.parse(strings[0].toString());
                        date.setHours(date.getHours()+8);
                        if(!Objects.equals(strings[1],"null")) {
                            Double value = Double.parseDouble(strings[1].toString());
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
                        String[] strings=l.toString().replace("[","").replace("]","").replace(" ","").split(",");
                        Date date=sdf.parse(strings[0].toString());
                        date.setHours(date.getHours()+8);
                        if(!Objects.equals(strings[1],"null")) {
                            Double value = Double.parseDouble(strings[1].toString());
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
        return monitorDatas;    }

    @Override
    public List<monitorDataListWithCName> queryAllDataByFeature(String feature, int count, int groupbyTimeSecond) {
        List<monitorDataListWithCName> monitorDatas=new ArrayList<>();
        String sql="select mean(value) from "+feature+" where time > now() - 1d group by time("+groupbyTimeSecond+"s),container_name order by time desc limit "+count;
        // String sql="select value from "+feature+" where time > now() - 1d and container_name = '"+contName+"' order by time  limit "+count;
        System.out.println(sql);
        // String sql="select * from "+feature+" where container_name='"+contName+"' limit "+count;
        QueryResult queryResult = influxDb.query(new Query(sql, database));
        List<QueryResult.Result> results = queryResult.getResults();
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
                        String[] strings=l.toString().replace("[","").replace("]","").replace(" ","").split(",");
                        Date date=sdf.parse(strings[0].toString());
                        date.setHours(date.getHours()+8);
                        if(!Objects.equals(strings[1],"null")) {
                            Double value = Double.parseDouble(strings[1].toString());
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
        return monitorDatas;    }

    private container transferToContainer(String s) {
        String[] stringbuffer=s.split(",");
        container cont=new container();
        String contName=stringbuffer[1].split("=")[1];
        String machName=stringbuffer[2].split("=")[1];
        cont.setName(contName);
        cont.setMachine(machName);
        return cont;
    }
}
