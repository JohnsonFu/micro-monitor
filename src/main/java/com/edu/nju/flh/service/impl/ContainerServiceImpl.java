package com.edu.nju.flh.service.impl;

import com.edu.nju.flh.dao.impl.BaseDaoImpl;
import com.edu.nju.flh.entity.po.container;
import com.edu.nju.flh.service.ContainerService;
import com.edu.nju.flh.util.Converter;
import org.apache.commons.collections.CollectionUtils;
import org.influxdb.dto.QueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fulinhua on 2018/5/13.
 */
@Service
public class ContainerServiceImpl implements ContainerService {
    @Autowired
    private BaseDaoImpl containerDao;

    @Value("${cadvisor.listAllContainerSQL}")
    private String listAllContainerSQL;

    @Value("${cadvisor.listAllMeasSQL}")
    private String listAllMeasSQL;

    @Override
    public List<container> listAllContainers() {
        List<container> list=new ArrayList<>();
        List<QueryResult.Result> results = containerDao.query(listAllContainerSQL);
        if (CollectionUtils.isNotEmpty(results)) {
            for (QueryResult.Result result : results) {
                List<QueryResult.Series>seriesList=result.getSeries();
                if(seriesList.size()==0){
                    return list;
                }
                List<List<Object>> objectList=seriesList.get(0).getValues();
                for(List<Object> l:objectList){
                    for(Object object:l){
                        container cont = Converter.transferToContainer(object.toString());
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
        List<QueryResult.Result> results = containerDao.query(listAllMeasSQL);
        if (CollectionUtils.isNotEmpty(results)) {
            for (QueryResult.Result result : results) {
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
}
