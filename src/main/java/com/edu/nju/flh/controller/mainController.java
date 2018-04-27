package com.edu.nju.flh.controller;

/**
 * Created by fulinhua on 2018/3/24.
 */

import com.edu.nju.flh.dao.ContainerDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

    @Controller
    public class mainController {
        @Autowired
        private ContainerDao containerDao;

        @RequestMapping("/")
        String home() {
           //containerDao.query("select * from \"memory_usage\" limit 10");
            containerDao.listAllContainers();
            return "chart";
        }


    }
