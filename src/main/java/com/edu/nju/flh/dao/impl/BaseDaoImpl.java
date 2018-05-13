package com.edu.nju.flh.dao.impl;

import com.edu.nju.flh.dao.BaseDao;
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
public class BaseDaoImpl implements BaseDao {
    @Autowired
    private InfluxDB influxDb;
    @Value("${cadvisor.database}")
    private String database;
    public List<QueryResult.Result> query(String sql) {
        QueryResult queryResult = influxDb.query(new Query(sql, database));
        List<QueryResult.Result> results = queryResult.getResults();
       return results;
    }
}
