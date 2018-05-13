package com.edu.nju.flh.dao;

import org.influxdb.dto.QueryResult;

import java.util.List;
/**
 * Created by fulinhua on 2018/4/22.
 */
public interface BaseDao {
    public List<QueryResult.Result> query(String command);
}
