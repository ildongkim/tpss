/*********************************************************
* Definition of variables.
******************************************************** */
var objLeftTime;
var objClickInfo;
var latestTime;
var expireTime;
var timeInterval = 1000; // 1초 간격 호출
var firstLocalTime = 0;
var elapsedLocalTime = 0;
var stateExpiredTime = false;
var timer;

/*********************************************************
* Get Cookie
******************************************************** */
function getCookie(cname) {
	var name = cname + "=";
	var decodedCookie = decodeURIComponent(document.cookie);
	var ca = decodedCookie.split(';');
	for(var i = 0; i <ca.length; i++) {
		var c = ca[i];
		while (c.charAt(0) == ' ') { c = c.substring(1); }
		if (c.indexOf(name) == 0) { return c.substring(name.length, c.length); }
	}
	return "";
}

/*********************************************************
* LeftTime Initialization
******************************************************** */
function leftTimeInit() {
	objLeftTime = document.getElementById("leftTimeInfo");
	if (objLeftTime != null) {
		objClickInfo = document.getElementById("clickInfo");
		latestTime = getCookie("egovLatestServerTime");
		expireTime = getCookie("egovExpireSessionTime");
		elapsedTime = 0;
		firstLocalTime = (new Date()).getTime();
		showRemaining();
		clearInterval(timer);
		timer = setInterval(showRemaining, timeInterval); // 1초 간격 호출
	}
}

/*********************************************************
* Show Remaining
******************************************************** */
function showRemaining() {
	var elapsedLocalTime = (new Date()).getTime() - firstLocalTime;
	var timeRemaining = expireTime - latestTime - elapsedLocalTime;
	if ( timeRemaining < timeInterval ) {
		clearInterval(timer);
		objLeftTime.innerHTML = "00:00:00";
		objClickInfo.text = '시간만료'; //시간만료
		stateExpiredTime = true;
		alert('로그인 세션시간이 만료 되었습니다.');//로그인 세션시간이 만료 되었습니다.
		$("#sessionInfo").hide();
		parent.location.href = logoutUrl;
	} else {
		var timeHour = Math.floor(timeRemaining/1000/60 / 60); 
		var timeMin = Math.floor((timeRemaining/1000/60) % 60);
		var timeSec = Math.floor((timeRemaining/1000) % 60);
		objLeftTime.innerHTML = pad(timeHour,2) +":"+ pad(timeMin,2) +":"+ pad(timeSec,2);
	}
}

/*********************************************************
* Request Time
******************************************************** */
function reqTimeAjax() {

	if (stateExpiredTime==true) {
		alert('시간을 연장할수 없습니다.');
		return;
	}
	
	$.ajax({
		url:'cmm/refreshSessionTimeout.do', //request 보낼 서버의 경로
		type:'get', // 메소드(get, post, put 등)
		data:{}, //보낼 데이터
		success: function(data) {
			latestTime = getCookie("egovLatestServerTime");
			expireTime = getCookie("egovExpireSessionTime");
			leftTimeInit();
		},
		error: function(err) {
			alert("err : "+err);
		}
	});
	return false;
}

/*********************************************************
* Login
******************************************************** */
function actionLogin() {
	if ($('#id').val() =="") {
		alert("아이디를 입력하세요");
	} else if ($('#password').val() =="") {
		alert("비밀번호를 입력하세요");
	} else {
		document.loginForm.action=loginUrl;
		document.loginForm.submit();
	}
}

/*********************************************************
* Logout
******************************************************** */
function logout() {
	$("#sessionInfo").hide();
	parent.location.href = logoutUrl;
}

/*********************************************************
* Register User
******************************************************** */
function goRegiUsr() {
	document.loginForm.action=regiUrl;
	document.loginForm.submit();
}

/*********************************************************
* Find ID/PW
******************************************************** */
function goFindId() {
	const options = {
		pagetitle : 'ID/PW 찾기', width: 550, height: 650,
		pageUrl : findIdUrl
	};
	settingDialog(options);
}

/*********************************************************
* 본인인증
******************************************************** */
function goNiceId() {
	document.loginForm.action=niceUrl;
	document.loginForm.submit();
}

/*********************************************************
* Open Dialog Modal
******************************************************** */
function settingDialog(options) {
	$dialog = $('<div></div>')
	.html('<iframe style="border: 0px; " src="' + options['pageUrl'] + '" width="100%" height="100%"></iframe>')
	.dialog({autoOpen: false, modal: true, width: options['width'], height: options['height'], title: options['pagetitle']});
	$dialog.dialog('open');
	$('.ui-dialog').css('z-index', '120');
} 

/*********************************************************
* Utils
******************************************************** */
function isNullToString(obj) {
	return (obj == null) ? "" : obj;
}

function pad(n, width) {
	n = n + '';
	return n.length >= width ? n : new Array(width - n.length + 1).join('0') + n;
}