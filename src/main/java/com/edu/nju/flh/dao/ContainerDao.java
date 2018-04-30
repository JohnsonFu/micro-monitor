package com.edu.nju.flh.dao;

import com.edu.nju.flh.entity.container;
import com.edu.nju.flh.entity.monitorData;

import java.util.List;
/**
 * Created by fulinhua on 2018/4/22.
 */
public interface ContainerDao {
    public void query(String command);
    public List<container> listAllContainers();
    public List<String> listAllMeasurements();
    public List<monitorData> queryDataByCNameAndFeature(String contName,String feature,int count);
}
