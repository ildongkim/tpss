<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8">
<script type="text/javascript" src="<c:url value='/js/egovframework/com/cmm/jquery.js'/>" ></script>
<script type="text/javaScript" language="javascript">
/* ********************************************************
* document.ready 처리 함수
******************************************************** */
$(document).ready(function() 
{
	
});
</script>
</head>
<jsp:include page='main_top.jsp'><jsp:param value="${loginVO}" name="loginVO"/></jsp:include>
</html>