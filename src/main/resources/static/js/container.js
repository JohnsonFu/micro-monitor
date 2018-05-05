/**
 * Created by fulinhua on 2018/4/29.
 */
function show(i){
    var data ={
        contName:i,
    }
    $.ajax({
        type: "POST",
        url:"showContainer",
        data:data,
        success:function(data){
            if(data.result){
                $("#false").show();
            }
            else{
                $("#true").show();
            }
        },
        error:function(XMLHttRequest,textStatus, errorThrown){
            alert(XMLHttRequest.responseText);
//        	alert(XMLHttpRequest.status);
//        	alert(XMLHttpRequest.readyState);
//        	alert(textStatus);
        },
    });
}

function check_exam_name(){
    var exam_name = $("#exam_name").val();
    var data ={
        examName: exam_name,
    }
    $("#true").hide();
    $("#false").hide();
    $.ajax({
        type: "POST",
        url:"exam/checkExamName",
        data:data,
        success:function(data){
            if(data.result){
                $("#false").show();
            }
            else{
                $("#true").show();
            }
        },
        error:function(XMLHttRequest,textStatus, errorThrown){
            alert(XMLHttRequest.responseText);
//        	alert(XMLHttpRequest.status);
//        	alert(XMLHttpRequest.readyState);
//        	alert(textStatus);
        },
    });
}

function login(){
    var email = $("#email").val();
    var password = $("#password").val();
    var data ={
        email: email,
        password: password
    }
    $.ajax({
        type: "POST",
        url:"/login",
//        data: $.param({"email":$("#email").val(),"password":$("#password").val()}),
//        dataType:json,
        data:data,
        async : true,
        success:function(data){
            var path = data.path;
            window.location.href =path
        },
        error:function(XMLHttRequest,textStatus, errorThrown){
            alert(XMLHttRequest.responseText);
//        	alert(XMLHttpRequest.status);
//        	alert(XMLHttpRequest.readyState);
//        	alert(textStatus);
        },
    });
}


function showIndex(contName,meas){
    var arrList = meas.valueOf().replace('[','').replace(']','').split(',');
    var tbody=document.getElementById("cont_body");
    var title=document.getElementById("title");
    var thead=document.getElementById("cont_head");
    title.innerHTML=contName+"              <button class='btn btn-success' onclick=\"back()\" style='margin-left:50px'>返回</button>";
    tbody.innerHTML=""
    thead.innerHTML="<tr><th>指标名称</th><th>查看详情</th></tr>"
    var mea;
    tbody.innerHTML+="<tbody id='cont_body'>"
    for(mea in arrList){
        tbody.innerHTML+="<tr><td><label>"+arrList[mea]+"</label></td><td><button class='btn btn-info' onclick=\"showMonitorData('"+contName+"','"+arrList[mea]+"')\">查看</button></td></tr>"
    }
    tbody.innerHTML+="</tbody>"
}

function back() {
    location.reload();
}

function showMonitorData(contName,feature){
    var data ={
        cName:contName,
        feat:feature,
    }
    $.ajax({
        type: "POST",
        url:"showMonitorData",
        data:data,
        success:function(data){
           var path = data.path;
            //alert(path)
            window.location.href =path
        },
        error:function(XMLHttRequest,textStatus, errorThrown){
          //  alert(XMLHttRequest.responseText);
        },
    });
}

function showTotal(){

    var multiple_answer = getCheckboxSelected("multiple-choice");
    var modalbody = document.getElementById("modal-body");
    modalbody.innerHTML=""
    modalbody.innerHTML+="<ol>";
    for(var i=0; i<multiple_answer.length; i++) {
        if(multiple_answer[i]==''){
            modalbody.innerHTML+="<li><a href='javascript:void(0);' onclick='hideModal("+i+");event.returnValue=false;'>第"+(i+1)+"题&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp</a>未选</li>"
        }
        else{
            modalbody.innerHTML+="<li><a href='javascript:void(0);' onclick='hideModal("+i+");event.returnValue=false;'>第"+(i+1)+"题&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp</a>"+multiple_answer[i]+"</li>"
        }
    }
    modalbody.innerHTML+="</ol>";
}



function  initData() {
    // 基于准备好的dom，初始化echarts实例
    var myChart = echarts.init(document.getElementById('main'));

    // 指定图表的配置项和数据
    var option = {
        title: {
            text: 'ECharts 入门示例'
        },
        tooltip: {},
        legend: {
            data: ['销量']
        },
        xAxis: {
            data: []
        },
        yAxis: {},
        series: [{
            name: '销量',
            type: 'line',
            data: []
        }]
    };

    // 使用刚指定的配置项和数据显示图表。
    //myChart.setOption(option);
    alert("rest")
    $.ajax({
        type: "POST",
        url: "showData",
        success: function (data) {
            alert(data.dataList)
            var dataList=data.dataList;
            for(var i=0;i<dataList.length;i++){
                option.xAxis.data[i]=dataList[i].time;
                option.series.data[i]=dataList[i].value;
            }
            alert(option.xAxis.data[0])
            myChart.setOption(option)

        },
        error: function (XMLHttRequest, textStatus, errorThrown) {
            //  alert(XMLHttRequest.responseText);
        },
    });
//    var ajax = function() {
//        alert("test")
//             $.ajax({
//                 type: "POST",
//                     url : 'showData',
//                         success: function(responseText) {
//                             alert(responseText);
//                         //请求成功时处理
////                         var responseText = eval('(' + responseText + ')');
////                         lineOption.legend.data=responseText.legend;
////                         lineOption.xAxis[0].data = responseText.xAxis;
////                         var serieslist = responseText.series;
////                         //alert(serieslist);
////                         for(var i=0;i<serieslist.length;i++) {
////                                 lineOption.series[i] = serieslist[i];
////                             }
////                         //alert(lineOption.series);
////                         myChart.setOption(lineOption,true);
//                     },
//                complete: function() {
//                         //请求完成的处理
//                     },
//                 error: function() {
//                        //请求出错处理
//                         alert("加载失败");
//                     }
//             })
//         }
}

function  initData2() {
    // 基于准备好的dom，初始化echarts实例
    var myChart = echarts.init(document.getElementById('main'));

    // 指定图表的配置项和数据
    var option = {
        tooltip: {
            trigger: 'axis'
        },
        toolbox: {
            feature: {
                dataView: {show: true, readOnly: false},
                magicType: {show: true, type: ['line', 'bar']},
                restore: {show: true},
                saveAsImage: {show: true}
            }
        },
        legend: {
            data:['蒸发量','降水量','平均温度']
        },
        xAxis: [
            {
                type: 'category',
                data: ['1月','2月','3月','4月','5月','6月','7月','8月','9月','10月','11月','12月']
            }
        ],
        yAxis: [
            {
                type: 'value',
                name: '数值',
                // min: 0,
                // max: 250,
                // interval: 50,
                axisLabel: {
                    formatter: '{value} yr'
                }

            }
        ],
        series: [
            {
                name:'数据',
                type:'line',
                data:[]
            }
        ]
    };


    // 使用刚指定的配置项和数据显示图表。
    myChart.setOption(option);
   // alert("rest")
    $.ajax({
        type: "POST",
        url: "showData",
        success: function (data) {
          //  alert(data.xData)
            var dataList=data.dataList;
            myChart.setOption({
                xAxis: [
                    {
                        data:data.xData
                    }
                ],
                yAxis: [
                    {
                     min: data.minVal*0.98
                    }
                    ],
                dataZoom: [{
                    type: 'slider',
                    show: true,
                    xAxisIndex: [0],
                    left: '9%',
                    bottom: -5,
                    start: 20,
                    end: 100 //初始化滚动条
                }],
                series: [
                    {
                        name:'数据',
                        data:data.yData
                     }
                    // {
                    //     name:'降水量',
                    //     data:data.c_rain
                    // },
                    // {
                    //     name:'平均温度',
                    //     data:data.c_avgt
                    // }
                ]
            });
            myChart.hideLoading();

        },
        error: function (XMLHttRequest, textStatus, errorThrown) {
            //  alert(XMLHttRequest.responseText);
        },
    });
//    var ajax = function() {
//        alert("test")
//             $.ajax({
//                 type: "POST",
//                     url : 'showData',
//                         success: function(responseText) {
//                             alert(responseText);
//                         //请求成功时处理
////                         var responseText = eval('(' + responseText + ')');
////                         lineOption.legend.data=responseText.legend;
////                         lineOption.xAxis[0].data = responseText.xAxis;
////                         var serieslist = responseText.series;
////                         //alert(serieslist);
////                         for(var i=0;i<serieslist.length;i++) {
////                                 lineOption.series[i] = serieslist[i];
////                             }
////                         //alert(lineOption.series);
////                         myChart.setOption(lineOption,true);
//                     },
//                complete: function() {
//                         //请求完成的处理
//                     },
//                 error: function() {
//                        //请求出错处理
//                         alert("加载失败");
//                     }
//             })
//         }
}