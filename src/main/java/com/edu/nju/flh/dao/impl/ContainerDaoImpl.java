package com.edu.nju.flh.dao.impl;

import com.edu.nju.flh.dao.ContainerDao;
import org.apache.commons.collections.CollectionUtils;
import org.influxdb.InfluxDB;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by fulinhua on 2018/4/22.
 */
@Repository
public class ContainerDaoImpl implements ContainerDao {
    @Autowired
    private InfluxDB influxDb;
    @Value("${cadvisor.database}")
    private String database;

    public void query(String command){
      QueryResult queryResult = influxDb.query(new Query(command, database));
        List<QueryResult.Result> results = queryResult.getResults();
        if (CollectionUtils.isNotEmpty(results)) {
            for (QueryResult.Result result : results) {
                System.out.println(result.toString());
            }
        }
    }

}
