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
                url: "queryFinanceInDiff",
                type:"post",
                data: {
                    searchText:$("#searchText").val(),
                    dbName:getQueryString("dbName"),
                    fromId:getQueryString("fromId")
                },
                dataType:"json",
                success: function(data){
                    var tableContext="<tr><td>财务id</td> <td>财务类型</td> <td>财务内容</td></tr>";
                    if(data!=undefined && data!=null){
                        var finances=data.finances;
                        var length=finances.length;
                        if(length!=0){
                            for(i=0;i<length;i++){
                                var type="";
                                if(finances[i].type==1 || finances[i].type=='1'){
                                    type="资产";
                                }else if(finances[i].type==2 || finances[i].type=='2'){
                                    type="负债";
                                }else if(finances[i].type==3 || finances[i].type=='3'){
                                    type="股东权益";
                                }else if(finances[i].type==4 || finances[i].type=='4'){
                                    type="营业收入";
                                }else{
                                    type="未知类型";
                                }
                                tableContext+="<tr><td>"+finances[i].id+"</td><td>"+type+"</td><td>"+finances[i].content+"</td></tr>";
                            }
                        }else{
                            tableContext+='<tr align="center"><td colspan="3">无财务信息</td></tr>';
                        }
                    }else{
                        tableContext+='<tr align="center"><td colspan="3">无财务信息</td></tr>';
                    }
                    $("#table").html(tableContext);
                }

            });
        });
    });
</script>

</body>
</html>
