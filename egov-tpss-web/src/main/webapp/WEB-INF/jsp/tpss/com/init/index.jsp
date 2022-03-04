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
	var dlistItems = [
        { text: '대한민국', value: '대한민국', code: 'KOR' },
        { text: '아루바', value: '아루바', code: 'ARB'  },
        { text: '올란드', value: '올란드', code: 'ORD'  }
    ]
    
	//1.트리메뉴목록
	mainGrid = new tui.Grid({
		el: document.getElementById('mainGrid'),
		bodyHeight: 200, scrollX: false,
		rowHeaders: ['checkbox','rowNum'],
		data: setReadData("<c:url value='/cmm/ses/selectSample.do'/>"),
		columns: 
		[
			{name:'cntry',      align:'center', validation:{required:true}, editor:{type:'radio', options:{listItems:dlistItems}}, header:'국적'},
			{name:'name',       align:'center', validation:{required:true}, editor:{type:CustomInputText, options:{maxLength:10} }, header:'성명'},
			{name:'rank',       align:'center', editor:'text', editor:{type:CustomInputText, options:{maxLength:6} }, header:'직책'},
			{name:'birth',      align:'center', editor:'text', editor:{type:CustomInputText, options:{maxLength:8} }, header:'생년월일'},
			{name:'phone',      align:'center', validation:{dataType:'number'}, editor:'text', header:'휴대번호'},
			{name:'sFileName',  align:'center', header:'파일명'},
			{name:'sFileSize',  align:'center', header:'파일용량'},
			{name:'sFileType',  align:'center', header:'파일형식'},
			{name:'downloader', align:'center', renderer:{type:CustomButton}, header:'다운로드'},
			{name:'uploader',   align:'center', formatter:CustomUploader, defaultValue:'mainGrid', header:'업로드'}
		]
	});
	searchGrid();
});

function gridButtonClick(data) {
	if(confirm("다운로드하시겠습니까?")){
		fileDownloadOpen("<c:url value='/cmm/ses/downloadSample.do'/>", data);
	}
}

function insertSample() {
	if(confirm("저장하시겠습니까?")){
		if(mainGrid.validate().length>0) {
			confirm("입력정보에 오류가 있습니다.");
			return;
		}
		var formData = new FormData();
		formData.append('egovMap', new Blob([ JSON.stringify(mainGrid.getData()) ], {type : "application/json"}));
		var fileInput = $('.gridUploader');
		for (var i = 0; i < fileInput.length; i++) {
			if (fileInput[i].files.length > 0) {
				for (var j = 0; j < fileInput[i].files.length; j++) {
					formData.append(fileInput[i].id, $('.gridUploader')[i].files[j]);
				}
			}
		}
		$.ajax({
			url : "<c:url value='/cmm/ses/insertSample.do'/>",
			method :"POST",
			data : formData,
			processData: false,
			contentType: false,
			enctype: 'multipart/form-data',
			success : function(result) {
				confirm("정상적으로 저장되었습니다.");
				searchGrid();
			},
			error : function(xhr, status) {
				confirm("저장이 실패하였습니다.");
			}
		});
	}
}

function deleteSample() {
	if (mainGrid.getCheckedRows().length == 0) {
		confirm("삭제할 데이터를 선택해주세요.")
		return;
	}
	if(confirm("삭제하시겠습니까?")){
		$.ajax({
			url : "<c:url value='/cmm/ses/deleteSample.do'/>",
			method :"POST",
			data : JSON.stringify(mainGrid.getCheckedRows()),
			dataType : "JSON",
			contentType : "application/json",
			success : function(result) {
				mainGrid.getCheckedRows().forEach(function(data, idx) {
					mainGrid.removeRow(data['rowKey']);
				});
			},
			error : function(xhr, status) {
				confirm("삭제가 실패하였습니다.");
			}
		});
	}
}

function gridValidate() {
	$('#validCn').val(gridInputValidation(mainGrid));
}

function searchGrid() {
	mainGrid.readData(1);
}

function addRow() {
	var rowData = [{cntry: "", name: "", rank: "",  birth: "", phone: "", sFileName: "", sFileSize: "", sFileType: ""}];
	mainGrid.appendRow(rowData);
}

function delRow() {
	console.log('delRow');
	mainGrid.removeRow(1);
}
</script>
<style>
.gridFileButton {
	padding: 6px 25px;
	background-color:#FF6600;
	border-radius: 4px;
	color: white;
	cursor: pointer;
}
</style>
</head>
<jsp:include page='main_top.jsp'><jsp:param value="${loginVO}" name="loginVO"/></jsp:include>
<br>
<br>
<!-- Page content-->
<table>
<colgroup>
	<col style="" />
</colgroup>
<tr>
	<td style="vertical-align:top;text-align:right">
		<a id="insertSample" class="btn02" href="#" onclick="insertSample();return false;">출입허가신청</a><br><br>
	</td>
</tr>
<tr>
	<td style="vertical-align:top;text-align:right">
		<a id="vew" class="btn02" href="#" onclick="gridValidate();return false;">입력값검증</a>
		<a id="sel" class="btn02" href="#" onclick="searchGrid();return false;">조회</a>
		<a id="add" class="btn02" href="#" onclick="addRow();return false;">추가</a>
		<a id="del" class="btn02" href="#" onclick="deleteSample();return false;">삭제</a><br><br>
	</td>
</tr>
<tr>
	<td style="vertical-align:top">
		<div id="mainGrid"></div>
	</td>
</tr>
<tr>
	<td style="vertical-align:top">
		<br><br>
		<textarea id="validCn" name="validCn" cols="150" rows="15"></textarea>
	</td>
</tr>
<!-- 
<tr>
	<td style="vertical-align:top">
		<input type="image" name="img"/>
	</td>
</tr>
<tr>
	<td style="vertical-align:top">
		<input type="file" class="upload" name="file1" accept="image/jpeg,.pdf" multiple/>
		<input type="file" class="upload" name="file2" accept="image/jpeg,.pdf" multiple/>
	</td>
</tr>
 -->
</table>

</html>