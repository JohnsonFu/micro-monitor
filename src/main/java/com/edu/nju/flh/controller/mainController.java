package com.edu.nju.flh.controller;

/**
 * Created by fulinhua on 2018/3/24.
 */

import com.edu.nju.flh.dao.ContainerDao;
import com.edu.nju.flh.entity.container;
import com.edu.nju.flh.entity.monitorData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
    public class mainController {
        @Autowired
        private ContainerDao containerDao;

        @RequestMapping("/")
        public String home(Model model) {
           List<container> containerList=containerDao.listAllContainers();
            List<String> measurements=containerDao.listAllMeasurements();
            model.addAttribute("containers",containerList);
            model.addAttribute("meas",measurements);
            return "table";
        }
    @RequestMapping(value = "/showContainer", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> showMonitor(HttpServletRequest request, Model model) {
        Map<String, Object> map = new HashMap<>();
        String contName = request.getParameter("contName");
        boolean result=false;
        map.put("contName", contName);
        return map;
    }
    @RequestMapping(value = "/showMonitorData", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> showMonitorData(HttpServletRequest request, Model model) {
        Map<String, Object> map = new HashMap<>();
        String contName = request.getParameter("cName");
        String feature = request.getParameter("feat");
        List<monitorData> dataList=containerDao.queryDataByCNameAndFeature(contName,feature,1000,2);
        map.put("cont", contName+feature);
        return map;
    }


    }
