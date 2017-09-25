<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Iterator" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<html>
<head>
    <title>查找用户</title>
    <script src="script/jquery-3.2.1.min.js"></script>
</head>
<body>
<form action="<%=path %>/search" method="post">
    <table>
        <tr>
            <td>请输入查询信息：</td>
            <td><input name="searchText" id="searchText"><input type="button" id="queryBtn" value="查找"></td>
        </tr>
        <tr align="center">
            <td colspan="2"></td>
        </tr>
    </table>
    <table id="table" border="1">

    </table>
</form>
<script type="text/javascript">

    function getQueryString(name)
    {
        var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
        var r = window.location.search.substr(1).match(reg);
        if(r!=null)return  unescape(r[2]); return null;
    }
    $(document).ready(function(){
        $("#queryBtn").click(function(){
            $.ajax({
                url: "queryUser",
                type:"post",
                data: {
                    searchText:$("#searchText").val(),
                    dbName:getQueryString("dbName"),
                    tenantId:getQueryString("tenantId")
                },
                dataType:"json",
                success: function(data){
                    var tableContext="<tr><td>用户id</td> <td>用户名</td> </tr>";
                    if(data!=undefined && data!=null){
                        var users=data.users;
                        var length=users.length;
                        if(length!=0){
                            for(i=0;i<length;i++){
                                tableContext+="<tr><td>"+users[i].id+"</td> <td>"+users[i].name+"</td> </tr>";
                            }
                        }else{
                            tableContext+='<tr align="center"><td colspan="2">无用户信息</td></tr>';
                        }
                    }else{
                        tableContext+='<tr align="center"><td colspan="2">无用户信息</td></tr>';
                    }
                    $("#table").html(tableContext);
                }

            });
        });
    });
</script>

</body>
</html>
