package com.edu.nju.flh.service;

import com.edu.nju.flh.entity.container;

import java.util.List;

/**
 * Created by fulinhua on 2018/5/13.
 */
public interface ContainerService {
    public List<container> listAllContainers();
    public List<String> listAllMeasurements();
}
