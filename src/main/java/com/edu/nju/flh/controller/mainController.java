package com.edu.nju.flh.controller;

/**
 * Created by fulinhua on 2018/3/24.
 */

import com.edu.nju.flh.entity.*;
import com.edu.nju.flh.service.ContainerService;
import com.edu.nju.flh.service.MonitorDataService;
import com.edu.nju.flh.util.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.stream.Collectors;

@Controller
    public class mainController {

    @Value("${container_query_size}")
    private int container_query_size;

    @Value("${measure_query_size}")
    private int measure_query_size;

    @Value("${mean_time_interval}")
    private int mean_time_interval;

    @Autowired
    private ContainerService containerService;

    @Autowired
    private MonitorDataService monitorDataService;

        @RequestMapping("/")
        public String home(Model model) {
           List<container> containerList=containerService.listAllContainers();
            List<String> measurements=containerService.listAllMeasurements();
            model.addAttribute("containers",containerList);
            model.addAttribute("meas",measurements);
            return "table";
        }

        @RequestMapping("/showAllMeas")
        public String showAllMeas(Model model) {
            List<String> measurements=containerService.listAllMeasurements();
            model.addAttribute("meas",measurements);
            return "meas";
        }

    @RequestMapping(value = "/showAllMonitorData", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> showAllMonitorData(HttpServletRequest request, HttpSession session) {
        Map<String, Object> map = new HashMap<>();
        String feature = request.getParameter("feat");
        session.setAttribute("feat",feature);
        map.put("path", "showAllChart");
        return map;
    }

    @RequestMapping(value = "/showContainer", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> showMonitor(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        String contName = request.getParameter("contName");
        map.put("contName", contName);
        return map;
    }
    @RequestMapping(value = "/showMonitorData", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> showMonitorData(HttpServletRequest request, HttpSession session) {
        Map<String, Object> map = new HashMap<>();
        String contName = request.getParameter("cName");
        String feature = request.getParameter("feat");
        session.setAttribute("feat",feature);
        session.setAttribute("cName",contName);
        map.put("path", "showChart");
        return map;
    }

    @RequestMapping("showChart")
    public String showChart() {
        return "chart2";
    }

    @RequestMapping("showAllChart")
    public String showAllChart() {
        return "chart3";
    }


    @RequestMapping(value = "/showData", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> showData(HttpSession session) {
        Map<String, Object> map = new HashMap<>();
        String contName = (String)session.getAttribute("cName");
        String feature = (String)session.getAttribute("feat");
        map.put("title",contName+"容器中"+feature+"数据");
        if(!Objects.equals(feature,"cpu_usage_per_cpu")) {
            List<monitorData> dataList = monitorDataService.getDataByCNameAndFeature(contName, feature, container_query_size, mean_time_interval);
            if(!CollectionUtils.isEmpty(dataList)) {
                SearchResult searchResult = Converter.convertToSearchResult(dataList);
                map.put("minVal", searchResult.getMin());
                map.put("xData", searchResult.getXData());
                map.put("yData", searchResult.getYData());
                map.put("instance", 1);
            }
            return map;
        }else{
            List<List<monitorData>> dataList = monitorDataService.getPer_cpu(contName, container_query_size,mean_time_interval);
            if(!CollectionUtils.isEmpty(dataList)) {
                List<monitorData> dataList0 = dataList.get(0);
                List<monitorData> dataList1 = dataList.get(1);
                SearchResult searchResult0 = Converter.convertToSearchResult(dataList0);
                SearchResult searchResult1 = Converter.convertToSearchResult(dataList1);
                double min = Math.min(searchResult0.getMin(), searchResult1.getMin());
                map.put("minVal", min);
                map.put("xData", searchResult0.getXData());
                map.put("yData0", searchResult0.getYData());
                map.put("yData1", searchResult1.getYData());
                map.put("instance", 2);
            }
            return map;
        }
    }

    @RequestMapping(value = "/showAllData", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> showAllData(HttpSession session) {
        Map<String, Object> map = new HashMap<>();
        String feature = (String)session.getAttribute("feat");
        map.put("title","所有容器中"+feature+"数据");
            List<monitorDataListWithCName> dataList = monitorDataService.getAllDataByFeature(feature, measure_query_size, mean_time_interval);
            if(!CollectionUtils.isEmpty(dataList)) {
                List<SearchResult> searchResultList = dataList.stream().map(list-> Converter.convertToSearchResult(list)).collect(Collectors.toList());
                double min= Collections.min(searchResultList.stream().map(monitorData -> monitorData.getMin()).collect(Collectors.toList()));
                List<AllMeasVO> listlist=searchResultList.stream().map(searchResult -> {
                    AllMeasVO vo=new AllMeasVO();
                    vo.setName(searchResult.getName());
                    vo.setData(searchResult.getYData());
                    return vo;
                }).collect(Collectors.toList());
                map.put("minVal", min);
                map.put("xData", searchResultList.get(0).getXData());
                map.put("dataList", listlist);
            }
            return map;
    }
    }
