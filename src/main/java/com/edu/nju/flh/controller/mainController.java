package com.edu.nju.flh.controller;

/**
 * Created by fulinhua on 2018/3/24.
 */

import com.edu.nju.flh.dao.ContainerDao;
import com.edu.nju.flh.entity.SearchResult;
import com.edu.nju.flh.entity.container;
import com.edu.nju.flh.entity.monitorData;
import com.edu.nju.flh.util.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
        if(!Objects.equals(feature,"cpu_usage_per_cpu")) {
            List<monitorData> dataList = containerDao.queryDataByCNameAndFeature(contName, feature, 2000, 5);
            map.put("path", "showChart");
            session.setAttribute("dataList", dataList);
        }else{
            List<List<monitorData>> dataList2 = containerDao.queryPer_cpu(contName, 2000,5);
            map.put("path", "showChart");
            session.setAttribute("dataList2", dataList2);
        }
        return map;
    }

    @RequestMapping("showChart")
    public String showChart() {
        return "chart2";
    }

    @RequestMapping(value = "/showData", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> showData(HttpSession session) {
        Map<String, Object> map = new HashMap<>();
        String feature = (String) session.getAttribute("feat");
        String contName = (String) session.getAttribute("cName");
        map.put("title",contName+"容器中"+feature+"数据");
        if(!Objects.equals(feature.replace(" ",""),"cpu_usage_per_cpu")) {
            List<monitorData> dataList = (List<monitorData>) session.getAttribute("dataList");
            if(!CollectionUtils.isEmpty(dataList)) {
                SearchResult searchResult = Converter.convertToSearchResult(dataList);
                map.put("minVal", searchResult.getMin());
                map.put("xData", searchResult.getXData());
                map.put("yData", searchResult.getYData());
                map.put("instance", 1);
            }
            return map;
        }else{
            List<List<monitorData>> dataList = (List<List<monitorData>>) session.getAttribute("dataList2");
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




    }
