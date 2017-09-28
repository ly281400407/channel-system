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

    <form action="uploadPic" method="post" enctype="multipart/form-data">
        <table border="1">
            <tr>
                <td colspan="2"><input id="picture" type="file" name="picture" ></td>
            </tr>
            <tr align="center">
                <td colspan="2"><input type="submit" id="uploadPic" value="上传图片"></td>
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
//        var formData = new FormData();
//        formData.append("picture",$("#picture").get(0).files[0]);
//        $("#uploadPic").click(function(){
//            $.ajax({
//                url: "uploadPic",
//                type:"post",
//                // 告诉jQuery不要去处理发送的数据
//                processData : false,
//                // 告诉jQuery不要去设置Content-Type请求头
//                contentType : false,
//                data: formData,
//                success: function(data){
//                    if(data!=undefined && data!=null){
//                          alert(data.url);
//                    }else{
//                        alert("系统发生未知错误，请重新申请");
//                    }
//                }
//
//            });
//        });
    });
</script>

</body>
</html>