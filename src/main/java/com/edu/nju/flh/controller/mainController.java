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
import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @RequestMapping("testChart")
    public String testChart(Model model) {
        return "echart";
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
    public Map<String,Object> showMonitorData(HttpServletRequest request, HttpSession session) {
        Map<String, Object> map = new HashMap<>();
        String contName = request.getParameter("cName");
        String feature = request.getParameter("feat");
        List<monitorData> dataList=containerDao.queryDataByCNameAndFeature(contName,feature,2000,5);
        map.put("path","showChart");
        session.setAttribute("dataList",dataList);
      //  System.out.println(dataList);
        return map;
    }

    @RequestMapping("showChart")
    public String showChart(Model model,HttpSession session) {

        return "chart2";
    }

    @RequestMapping(value = "/showData", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> showData(HttpServletRequest request, HttpSession session) {
        Map<String, Object> map = new HashMap<>();
        List<monitorData> dataList= (List<monitorData>) session.getAttribute("dataList");
     // dataList=dataList.stream().filter(monitorData -> monitorData.getValue()!=0).collect(Collectors.toList());
        List<String> xData= dataList.stream().map(data->data.getTime()).collect(Collectors.toList());
        List<String> yData= dataList.stream().map(monitorData -> {
            double d=monitorData.getValue();
            if(Math.abs(d+1)<0.01){
                return "";
            }else {
                d = d / 31536000;
            }
            return d+"";}).collect(Collectors.toList());

        dataList=dataList.stream().filter(monitorData -> Math.abs(monitorData.getValue()+1)>0.01).collect(Collectors.toList());
        double min= Collections.min(dataList.stream().map(monitorData -> monitorData.getValue()).collect(Collectors.toList()))/31536000;
        System.out.println(min);
        map.put("minVal",min);
        map.put("xData",xData);
        map.put("yData",yData);
       // System.out.println(yData);
        return map;
    }


    }
