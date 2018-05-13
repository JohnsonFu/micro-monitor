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
        dataType : "json",
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
        dataType : "json",
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



function showMeas(measure) {
    var data ={
        feat:measure,
    }
    $.ajax({
        type: "POST",
        url:"showAllMonitorData",
        data:data,
        dataType : "json",
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

function  initData2() {
    // 基于准备好的dom，初始化echarts实例
    var myChart = echarts.init(document.getElementById('main'));
    myChart.showLoading({
        text: "数据加载中..."
    });
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
            },
            {
                name:'数据2',
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
        dataType : "json",
        success: function (data) {
            if(data.instance==1) {
                myChart.setOption({
                    title : {
                        text: data.title,
                        x: 'center',
                        align: 'right'
                    },
                    xAxis: [
                        {
                            data: data.xData
                        }
                    ],
                    yAxis: [
                        {
                            min: data.minVal
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
                            name: '数据',
                            data: data.yData
                        }
                    ]
                });
            }else{
                myChart.setOption({
                    title : {
                        text: data.title,
                        x: 'center',
                        align: 'right'
                    },
                    xAxis: [
                        {
                            data: data.xData
                        }
                    ],
                    yAxis: [
                        {
                            min: data.minVal
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
                            name: 'instance0',
                            data: data.yData0
                        },
                        {
                            name:'instance1',
                            data:data.yData1
                        }
                    ]
                });
            }
            myChart.hideLoading();

        },
        error: function (XMLHttRequest, textStatus, errorThrown) {
              alert(XMLHttRequest.responseText);
        },
    });
}

function  initAllData() {
    // 基于准备好的dom，初始化echarts实例
    var myChart = echarts.init(document.getElementById('main'));
    myChart.showLoading({
        text: "数据加载中..."
    });
    // 指定图表的配置项和数据
    var option = {
        title : {
            text: ''
        },
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
            data:[]
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
                type:'line',
                data:[]
            },
            {
                name:'数据2',
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
        url: "showAllData",
        dataType : "json",
        success: function (data) {
            option.xAxis[0].data=data.xData;
            option.yAxis[0].min=data.minVal;
            option.title.text=data.title;
            var legends = [];// 准备存放图例数据
            var Series = []; // 准备存放图表数据
            var json = data.dataList;// 后台返回的json
            var Item = function(){
                return {
                    name:'',
                    type:'line',
                    data:[]
                }
            };
            for(var i=0;i < json.length;i++){
                var it = new Item();
                it.name = json[i].name;// 先将每一项填充数据
                legends.push(json[i].name);// 将每一项的图例名称也放到图例的数组中
                it.data = json[i].data;
                Series.push(it);// 将item放在series中
            }
            option.legend.data = legends;// 设置图例
            option.series = Series; // 设置图表
            myChart.setOption(option)
            myChart.hideLoading();

        },
        error: function (XMLHttRequest, textStatus, errorThrown) {
              alert(XMLHttRequest.responseText);
        },
    });
}