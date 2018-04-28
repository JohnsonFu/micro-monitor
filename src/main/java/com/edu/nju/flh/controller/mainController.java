package com.edu.nju.flh.controller;

/**
 * Created by fulinhua on 2018/3/24.
 */

import com.edu.nju.flh.dao.ContainerDao;
import com.edu.nju.flh.entity.container;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
    public class mainController {
        @Autowired
        private ContainerDao containerDao;

        @RequestMapping("/")
        public String home(Model model) {
           //containerDao.query("select * from \"memory_usage\" limit 10");
           List<container> containerList=containerDao.listAllContainers();
            model.addAttribute("containers",containerList);
            return "table";
        }


    }
