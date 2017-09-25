<%@page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<html>
<head>
    <title>登陆页面</title>
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta name="keywords" content="云平台">
    <meta name="description" content="a simple demo to Web APP">
    <meta name="content-type" content="text/html; charset=UTF-8">
    <script src="script/jquery-3.2.1.min.js"></script>
</head>

<body>
<h2 align=center>欢迎您</h2>
<center>
    <form action="<%=path %>/login" method="post">
        <table border="1">
            <tr>
                <td>所属公司：</td>
                <td>
                    <select id="dbName">
                    </select>
                </td>
            </tr>
            <tr>
                <td>用户名：</td>
                <td><input id="userName" name="userName"></td>
            </tr>
            <tr>
                <td>密码：</td>
                <td><input id="password" name="password" type="password"></td>
            </tr>
            <tr align="center">
                <td colspan="2"><input type="button" id="loginBtn" value="点击登陆"></td>
            </tr>
        </table>
    </form>
    <%--还没注册？<a href="<%=path%>/index.jsp">点击注册吧</a>--%>
</center>
<script type="text/javascript">
    $(document).ready(function(){
        loadCompanyInfo();
            $("#loginBtn").click(function(){
                $.ajax({
                    url: "loginOfUser",
                    type:"post",
                    data: {
                        userName:$("#userName").val(),
                        password:$("#password").val(),
                        dbName:$("#dbName").val()
                    },
                    dataType:"json",
                    success: function(data){
                        if(data!=undefined && data!=null){
                            if(data.islogin){
                                window.location.href="addFinancePage?dbName="+data.user.dbName+"&fromId="+data.user.id;
                            }else{
                                alert(data.msg);
                            }

                        }
                    }
                });
            });

    });
    function loadCompanyInfo(){
        $.ajax({
            url: "queryAllTenant",
            type:"post",
            data: {

            },
            dataType:"json",
            success: function(data){
                var html="";
                if(data!=undefined && data!=null){
                    var tenantInfos=data.tenantInfo;
                    var length=tenantInfos.length;
                    if(length!=0){
                        for(i=0;i<length;i++){
                            html+="<option value='"+tenantInfos[i].dbName+"'>"+tenantInfos[i].companyName+"</option>";
                        }
                    }else{
                        html+='<option>查无租户信息</option>';
                    }
                    $("#dbName").html(html);
                }
            }
        });
    }
</script>
</body>
</html>