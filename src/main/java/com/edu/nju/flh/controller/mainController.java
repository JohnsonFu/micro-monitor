package com.edu.nju.flh.controller;

/**
 * Created by fulinhua on 2018/3/24.
 */

import com.alibaba.fastjson.JSONObject;
import com.edu.nju.flh.entity.po.container;
import com.edu.nju.flh.entity.po.monitorData;
import com.edu.nju.flh.entity.po.monitorDataListWithCName;
import com.edu.nju.flh.entity.vo.AllMeasVO;
import com.edu.nju.flh.entity.vo.SearchResult;
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
    public String showAllMonitorData(HttpServletRequest request, HttpSession session) {
        JSONObject json=new JSONObject();
        String feature = request.getParameter("feat");
        session.setAttribute("feat",feature);
        json.put("path", "showAllChart");
        return json.toJSONString();
    }
    @RequestMapping(value = "/showMonitorData", method = RequestMethod.POST)
    @ResponseBody
    public String showMonitorData(HttpServletRequest request, HttpSession session) {
        JSONObject json=new JSONObject();
        String contName = request.getParameter("cName");
        String feature = request.getParameter("feat");
        session.setAttribute("feat",feature);
        session.setAttribute("cName",contName);
        json.put("path", "showChart");
        return json.toJSONString();
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
    public String showData(HttpSession session) {
        JSONObject json=new JSONObject();
        String contName = (String)session.getAttribute("cName");
        String feature = (String)session.getAttribute("feat");
        json.put("title",contName+"容器中"+feature+"数据");
        if(!Objects.equals(feature,"cpu_usage_per_cpu")) {
            List<monitorData> dataList = monitorDataService.getDataByCNameAndFeature(contName, feature, container_query_size, mean_time_interval);
            if(!CollectionUtils.isEmpty(dataList)) {
                SearchResult searchResult = Converter.convertToSearchResult(dataList);
                json.put("minVal", searchResult.getMin());
                json.put("xData", searchResult.getXData());
                json.put("yData", searchResult.getYData());
                json.put("instance", 1);
            }
            return json.toJSONString();
        }else{
            List<List<monitorData>> dataList = monitorDataService.getPer_cpu(contName, container_query_size,mean_time_interval);
            if(!CollectionUtils.isEmpty(dataList)) {
                List<monitorData> dataList0 = dataList.get(0);
                List<monitorData> dataList1 = dataList.get(1);
                SearchResult searchResult0 = Converter.convertToSearchResult(dataList0);
                SearchResult searchResult1 = Converter.convertToSearchResult(dataList1);
                double min = Math.min(searchResult0.getMin(), searchResult1.getMin());
                json.put("minVal", min);
                json.put("xData", searchResult0.getXData());
                json.put("yData0", searchResult0.getYData());
                json.put("yData1", searchResult1.getYData());
                json.put("instance", 2);
            }
            return json.toJSONString();
        }
    }

    @RequestMapping(value = "/showAllData", method = RequestMethod.POST)
    @ResponseBody
    public String showAllData(HttpSession session) {
        JSONObject json=new JSONObject();
        String feature = (String)session.getAttribute("feat");
        json.put("title","所有容器中"+feature+"数据");
            List<monitorDataListWithCName> dataList = monitorDataService.getAllDataByFeature(feature, measure_query_size, mean_time_interval);
            if(!CollectionUtils.isEmpty(dataList)) {
                List<SearchResult> searchResultList = monitorDataService.getSearchResultFromMonitorData(dataList);
                double min= Collections.min(searchResultList.stream().map(monitorData -> monitorData.getMin()).collect(Collectors.toList()));
                List<AllMeasVO> listlist=monitorDataService.getMeasVOFromSearchResult(searchResultList);
                json.put("minVal", min);
                json.put("xData", searchResultList.get(0).getXData());
                json.put("dataList", listlist);
            }

            return json.toJSONString();
    }
    }
