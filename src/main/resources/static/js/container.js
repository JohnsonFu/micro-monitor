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
    // alert(meas.valueOf());
    // var arr=new Array();
    // arr.push(1);
    // arr.push(2);
    // alert(arr);
    var arrList = meas.valueOf().replace('[','').replace(']','').split(',');
    var tbody=document.getElementById("cont_body");
    var title=document.getElementById("title");
    var thead=document.getElementById("cont_head");
    title.innerHTML=contName;
    tbody.innerHTML=""
    thead.innerHTML="<tr><th>指标名称</th><th>查看详情</th></tr>"
    var mea;
    var test='abc'
    tbody.innerHTML+="<tbody id='cont_body'>"
    for(mea in arrList){
        tbody.innerHTML+="<tr><td><label>"+arrList[mea]+"</label></td><td><button class='btn btn-info' onclick=\"showMonitorData('"+contName+"','"+arrList[mea]+"')\">查看</button></td></tr>"
    }
    tbody.innerHTML+="</tbody>"
}

function showMonitorData(contName,feature){
    alert(contName)
    alert(feature)
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