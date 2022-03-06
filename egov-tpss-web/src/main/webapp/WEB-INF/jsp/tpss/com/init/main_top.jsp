<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<link href="<c:url value="/css/egovframework/com/com.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<c:url value='/js/egovframework/com/cmm/jquery.js'/>" ></script>
<script type="text/javascript" src="<c:url value='/js/egovframework/com/cmm/jqueryui.js'/>" ></script>
<script type="text/javascript" src="<c:url value='/js/tpss/com/com.js'/>" ></script>
<script type="text/javaScript" language="javascript">
var loginUrl = "<c:url value='/cmm/init/actionLogin.do'/>";
var logoutUrl = "<c:url value='/cmm/init/actionLogout.do'/>";
var regiUrl = "<c:url value='/cmm/init/EgovStplatCnfirmMber.do'/>";
var findIdUrl = "<c:url value='/cmm/init/searchIdPassword.do'/>";
var niceUrl = "<c:url value='/cmm/init/NiceCnfirm.do'/>";
/* ********************************************************
* document.ready 처리 함수
******************************************************** */
$(document).ready(function() 
{
	leftTimeInit();
	//$("label[for='message']").text('아이디가 잘못 되었거나 비밀번호가 잘못 입력되었습니다.');
});
</script>
<div id=top>
	<form name="loginForm" id="loginForm" method="post">
	<div class="login_input">
		<ul>
		<li>
			<a id="home" class="btn02" href="/tpss">HOME</a>
			<c:if test="${loginVO == null}">
				<input type="text" name="id" id="id" maxlength="20">
				<input type="password" name="password" id="password" maxlength="20" value="rhdxhd12">
				<input name="userSe" type="hidden" value="GNR"/>
				<a id="login" class="btn02" href="#" onclick="actionLogin();return false;">로그인</a>
				<a id="regiUsr" class="btn02" href="#" onclick="goRegiUsr();return false;">회원가입</a>
				<a id="findId" class="btn02" href="#" onclick="goFindId();return false;">ID/PW찾기</a>
				<a id="niceId" class="btn02" href="#" onclick="goNiceId();return false;">본인인증</a>
				<label for="message"/>
			</c:if>
			<c:if test="${loginVO != null}">
				[${loginVO.name}님] 세션만료 남은시간 - <span id="leftTimeInfo">00:00:00</span>
				<a id="clickInfo" class="btn02" href="#" onclick="reqTimeAjax();return false;">시간연장</a>
				<a id="logout" class="btn02" href="#" onclick="logout();return false;">로그아웃</a>
			</c:if>
		</li>
		</ul>
	</div>
	</form>
</div>