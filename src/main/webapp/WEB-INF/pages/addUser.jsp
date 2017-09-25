<%@page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<html>
<head>
    <title>新增用户页面</title>
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta name="keywords" content="云平台">
    <meta name="description" content="a simple demo to Web APP">
    <meta name="content-type" content="text/html; charset=UTF-8">
    <script src="script/jquery-3.2.1.min.js"></script>
</head>

<body>
<h2 align=center>新增用户</h2>
<center>
    <form action="<%=path %>/login" method="post">
        <table border="1">
            <tr>
                <td>用户名：</td>
                <td><input id="userName" name="userName"></td>
            </tr>
            <tr>
                <td>密码：</td>
                <td><input id="password" name="password" type="password"></td>
            </tr>
            <tr align="center">
                <td colspan="2"><input type="button" id="saveBtn" value="保存"></td>
            </tr>
            <tr align="center">
                <td colspan="2"><input type="button" id="queryUserPageBtn" value="跳转查询用户界面"></td>
            </tr>
        </table>

    </form>
    <%--还没注册？<a href="<%=path%>/index.jsp">点击注册吧</a>--%>
</center>
<script type="text/javascript">
    function getQueryString(name)
    {
        var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
        var r = decodeURI(window.location.search.substr(1)).match(reg);
        if(r!=null)return  unescape(r[2]); return null;
    }

    $(document).ready(function(){
        $("#queryUserPageBtn").click(function(){
            window.location.href="displayPage?dbName="+getQueryString("dbName")+"&tenantId="+getQueryString("tenantId");
        });
            $("#saveBtn").click(function(){
                $.ajax({
                    url: "addUser",
                    type:"POST",
                    data: {
                        userName:$("#userName").val(),
                        password:$("#password").val(),
                        dbName:getQueryString("dbName"),
                        companyName:getQueryString("companyName"),
                        tenantId:getQueryString("tenantId")
                    },
                    dataType:"json",
                    success: function(data){
                        if(data!=undefined && data!=null){
                                alert(data.msg);
                        }
                    }
                });
            });

    });

</script>
</body>
</html>