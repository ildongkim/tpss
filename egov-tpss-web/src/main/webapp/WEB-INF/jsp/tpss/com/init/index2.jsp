<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8">
<link href="<c:url value='/modules/tui-grid/dist/tui-grid.min.css' />" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<c:url value='/js/egovframework/com/cmm/jquery.js'/>" ></script>
<script type="text/javascript" src="<c:url value='/modules/tui-grid/dist/tui-grid.js'/>" ></script>
<script type="text/javascript" src="<c:url value='/js/tpss/com/com.tui.js'/>" ></script>
<script type="text/javaScript" language="javascript">
/* ********************************************************
* document.ready 처리 함수
******************************************************** */
$(document).ready(function() 
{
	gridSample = new tui.Grid({
		el: document.getElementById('gridSample'),
		bodyHeight: 200, scrollX: false,
		rowHeaders: ['rowNum', 'checkbox'],
		//data: setReadData("<c:url value='/cmm/cmmnCodeDtlList.do'/>"),
		columns: 
		[
			{header:'cntry', name:'cntry', align:'center', editor: { type: CustomAutoComplete }},
			{header:'name',  name:'name',  align:'center', formatter: CustomUploader },
			{header:'birth', name:'birth', align:'center', editor: 'text', defaultValue: '1900.01.01-05'},
			{header:'phone', name:'phone', align:'center', editor: 'text' }
		]
	});
	
	$("#test").autocomplete({
		source: dataList,
		fucus:function(event, ui) {
			return false;
		},
		mimLength:1,
		delay:100
	});
});

var dataList = [
    "종로2가사거리",
    "창경궁.서울대학교병원",
    "명륜3가.성대입구",
    "종로2가.삼일교",
    "혜화동로터리.여운형활동터",
    "서대문역사거리",
    "서울역사박물관.경희궁앞",
    "서울역사박물관.경희궁앞",
    "광화문",
    "광화문",
    "종로1가",
    "종로1가",
    "종로2가",
    "종로2가",
    "종로3가.탑골공원",
    "종로3가.탑골공원",
    "종로4가.종묘"
    ];
    
function addGridRow() {
	var rowData = [{cntry: "", name: "", phone: ""}]
	gridSample.appendRow(rowData,1)
}

function viewGridData() {
	console.log($(".autocomplete")[0].name);
	console.log(gridSample.getRow(0));
}

function autoComplete(e) {
	console.log(e);
}
</script>
</head>
<jsp:include page='main_top.jsp'><jsp:param value="${loginVO}" name="loginVO"/></jsp:include>
<br><br>
<a id="login" class="btn02" href="#" onclick="addGridRow();return false;">추가</a>
<a id="login" class="btn02" href="#" onclick="viewGridData();return false;">데이터보기</a>
<br><br>
AA:<input type="text" id="test" name="test">
<br><br>
	<table>
	<colgroup>
		<col style="" />
	</colgroup>
	<tr>
		<td style="vertical-align:top">
			<div id="gridSample"></div>
		</td>
	</tr>
	</table>
</html>