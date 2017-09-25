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
<h2 align=center>新增财务信息</h2>
<center>
    <form action="<%=path %>/login" method="post">
        <table border="1">
            <tr>
                <td>财务类型：</td>
                <td><select id="type" name="type" >
                   <option value="1">资产</option>
                   <option value="2">负债</option>
                   <option value="3">股东权益</option>
                   <option value="4">营业收入</option>
                        </select>
                </td>
            </tr>
            <tr>
                <td>财务内容：</td>
                <td><textarea id="content" name="content" ></textarea></td>
            </tr>
            <tr align="center">
                <td colspan="2"><input type="button" id="saveBtn" value="保存"></td>
            </tr>
            <tr align="center">
                <td colspan="2"><input type="button" id="queryBtn" value="跳转查询财务界面"></td>
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
        $("#queryBtn").click(function(){
            window.location.href="displayFinancePage?dbName="+getQueryString("dbName")+"&fromId="+getQueryString("fromId");
        });
            $("#saveBtn").click(function(){
                $.ajax({
                    url: "addFinanceInDiff",
                    type:"POST",
                    data: {
                        content:$("#content").val(),
                        type:$("#type").val(),
                        dbName:getQueryString("dbName"),
                        fromId:getQueryString("fromId"),
                        status:0
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