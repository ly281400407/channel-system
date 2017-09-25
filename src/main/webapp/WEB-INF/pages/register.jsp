<%@page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<html>
<head>
    <title>注册页面</title>
    <meta name="keywords" content="云平台">
    <meta name="description" content="a simple demo to Web APP">
    <meta name="content-type" content="text/html; charset=UTF-8">
    <script src="script/jquery-3.2.1.min.js"></script>
</head>

<body>

<h2 align=center>注册页面</h2>
<center>

    <form action="<%=path %>/register" method="post">
        <table border="1">
            <tr>
                <td>用户名：</td>
                <td><input id="userName" name="userName"></td>
            </tr>
            <tr>
                <td>密码：</td>
                <td><input  id="password"  name="password" type="password"></td>
            </tr>
            <%--<tr>--%>
                <%--<td>重复密码：</td>--%>
                <%--<td><input name="conPassword" type="password"></td>--%>
            <%--</tr>--%>
            <tr>
                <td>手机号：</td>
                <td><input id="phone" name="phone"></td>
            </tr>
            <tr>
                <td>公司名：</td>
                <td><input id="companyName" name="company"></td>
            </tr>
            <tr align="center">
                <td colspan="2"><input type="button" id="registerBtn" value="提交注册"></td>
            </tr>
        </table>
    </form>

    <%--您有账号？<a href="<%=path %>/jsp/front/Login.jsp"><front color="GREEN" >点击直接登录</front></a>--%>
</center>
<script type="text/javascript">

    function getQueryString(name)
    {
        var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
        var r = window.location.search.substr(1).match(reg);
        if(r!=null)return  unescape(r[2]); return null;
    }
    $(document).ready(function(){
        $("#registerBtn").click(function(){
            $.ajax({
                url: "register",
                type:"post",
                data: {
                    userName:$("#userName").val(),
                    password:$("#password").val(),
                    companyName:$("#companyName").val(),
                    phone:$("#phone").val()
                },
                dataType:"json",
                success: function(data){
                    if(data!=undefined && data!=null){
                          alert(data.msg);
                    }else{
                        alert("系统发生未知错误，请重新申请");
                    }
                }

            });
        });
    });
</script>

</body>
</html>