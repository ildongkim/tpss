/*********************************************************
 * TUI.GRID Theme
 ******************************************************** */
tui.Grid.applyTheme('default', {
	selection: {
        background: "#00A9ff",
        border: '#00a9ff'
    },
	cell: {
		normal: {
            background: "#F5F5F5",
            border: "#D0D0D0",
            text: "#222",
            showVerticalBorder: !0,
            showHorizontalBorder: !0
        },
		header: {
            background: "#EDEDED",
            border: "#D0D0D0",
            text: "#222",
            showVerticalBorder: !0,
            showHorizontalBorder: !0
        },
        rowHeader: {
            background: "#EDEDED",
            border: "#D0D0D0",
            showVerticalBorder: !0,
            showHorizontalBorder: !0
        },
        selectedHeader: {
            background: "#EDEDED",
            border: "#D0D0D0"
        },
        selectedRowHeader: {
            background: "none"
        },
        focused: {
            border: "none"
        },
        focusedInactive: {
            border: "none"
        }, 
        currentRow: {
        	background: "#FAE8DF"
        }
	},
    row: {
    	hover: {
        	background: "#FAE8DF"
    	},
    	odd : {
    		background: "#ffffff"
    	},
    	even : {
    		background: "#F5F5F5"
    	}
    }
});

/*********************************************************
 * TUI.GRID Event
 ******************************************************** */
function setGridEvent(grid) {
	var selectedRowKey = null;
	grid.on('focusChange', function (ev) {
		if (selectedRowKey != null) {
			grid.removeRowClassName(selectedRowKey, 'currentRow');
		}
		selectedRowKey = ev.rowKey;
		grid.addRowClassName(selectedRowKey, 'currentRow');
	});
	
	grid.on('dblclick', function (ev) {
		return;
	});
	
	//성공, 실패와 관계 없이 응답을 받았을 경우 실행
	grid.on('response', function(ev) {
		//console.log('성공, 실패와 관계 없이 응답을 받았을 경우 실행');
	});
	
	grid.on('beforeRequest', function(ev) {
		//console.log('요청을 보내기 전 실행');
	});
			
	grid.on('successResponse', function(ev) {
		//console.log('결과가 true인 경우');
	});
	
	grid.on('failResponse', function(ev) {
		try {
			var responseObj = JSON.parse(ev.xhr.response);
			if (responseObj.message) { confirm(responseObj.message); }
		} catch (error) {
			confirm("요청처리를 실패하였습니다.");
		}
	});
	
	grid.on('errorResponse', function(ev) {
		confirm("요청처리를 실패하였습니다.");
	});
}

/*********************************************************
 * TUI.GRID datasource
 ******************************************************** */
function setReadData(url) {
	var datasource = {
		contentType: 'application/x-www-form-urlencoded',
		api: { 
			readData: { url: url, method: 'POST' }
		},
		hideLoadingBar: true,
		initialRequest: false 
	};
	return datasource;
}

/*********************************************************
* Program File Name Search Modal
******************************************************** */
function settingDialog(options) {
	$dialog = $('<div></div>')
	.html('<iframe style="border: 0px; " src="' + options['pageUrl'] + '" width="100%" height="100%"></iframe>')
	.dialog({autoOpen: false, modal: true, width: options['width'], height: options['height'], title: options['pagetitle']});
	$dialog.dialog('open');
	$('.ui-dialog').css('z-index', '120');
} 

/*********************************************************
* Program File Name Search Modal
******************************************************** */
function isNullToString(obj) {
	return (obj == null) ? "" : obj;
}

/*********************************************************
* Hierarchy MenuList delete empty folder
******************************************************** */
function getHierarchyMenuList(array) {
	//console.log('HierarchyMenuList');
	array.forEach(function(data, idx) {
		if (data['_children'].length > 0) {
			getHierarchyMenuList(data['_children']);
		} else {
			delete data['_children'];			
		}
	});
}

/* ********************************************************
 * Grid Checkbox Header Renderer
 ******************************************************** */
const CustomHeaderCheckBox = class {
	constructor(props) {
		const { grid, rowKey } = props;
		const el = document.createElement('input');
		el.id = String(rowKey);
		el.type = 'checkbox';
		el.checked = grid.getRow(rowKey).useAt == "Y" ? true : false;
		el.onchange = function (e) { 
			grid.setValue(rowKey, "useAt", this.checked ? "Y" : "N"); 
		};
		this.el = el;
		this.render(props);
	}
	getElement() { return this.el; }
	render(props) {
		const { grid, rowKey } = props;
		this.el.checked = grid.getRow(rowKey).useAt == "Y" ? true : false;
	}
}
	
/* ********************************************************
 * Grid Checkbox Renderer
 ******************************************************** */
class CustomCheckBox {
	constructor(props) {
		const el = document.createElement('input');
		const { grid, rowKey, columnInfo } = props;
		el.type = 'checkbox';
		el.checked = props.value == "Y" ? true : false;
		el.onchange = function (e) { grid.setValue(rowKey, columnInfo.name, this.checked ? "Y" : "N"); };
		this.el = el;
	}
	
	getElement() {
        return this.el;
    }
}

/* ********************************************************
 * Grid Selected Row Find Sample
 ******************************************************** */
//function setSendData(jarr) {
//	for(idx in jarr) {
//		jarr[idx].uniqId = grid.getRow(grid.getFocusedCell()['rowKey'])['uniqId'];
//	}
//	return jarr;
//}

/*********************************************************
 * TUI.UPLOADER Set
 ******************************************************** */
var $uploadedCount;
var $itemTotalSize;
var $checkedItemCount;
var $checkedItemSize;
var $removeButton;

function setFileUploader() {
	$uploadedCount = $('#uploadedCount');
	$itemTotalSize = $('#itemTotalSize');
	$checkedItemCount = $('#checkedItemCount');
	$checkedItemSize = $('#checkedItemSize');
	$removeButton = $('.tui-btn-cancel');
	
	//파일체크박스클릭이벤트
    uploader.on('check', function(evt) {
        var checkedItems = uploader.getCheckedList();
        var checkedItemSize = uploader.getCheckedSize(checkedItems);
        var checkedItemCount = checkedItems.length;
        var removeButtonState = (checkedItemCount === 0);
        disableRemoveButton(removeButtonState);
        updateCheckedInfo(checkedItemSize, checkedItemCount);
    });
	
	//파일체크박스 전체클릭이벤트
    uploader.on('checkAll', function(evt) {
        var checkedItems = uploader.getCheckedList();
        var checkedItemSize = uploader.getCheckedSize(checkedItems);
        var checkedItemCount = checkedItems.length;
        var removeButtonState = (checkedItemCount === 0);
        disableRemoveButton(removeButtonState);
        updateCheckedInfo(checkedItemSize, checkedItemCount);
    });
    
	//업로드 파일 체크
    uploader.on('update', function(evt) { // This event is only fired when using batch transfer
    	setTotalCountInfo();
    });
	
	//파일첨부오류
    uploader.on('error', function(evt) {
    	switch (evt['message']) {
	    	case 'EXT': confirm("지원되는 파일유형이 아닙니다."); break;
	    	case 'CNT': confirm("첨부 가능 개수을 초과했습니다."); break;
	    	case 'SIZE': confirm("첨부 가능 용량을 초과했습니다."); break;    		
    	}
    });
    
    //파일삭제
    uploader.on('remove', function(evt) {
        var checkedItems = uploader.getCheckedList();
        var removeButtonState = (checkedItems.length === 0);
        disableRemoveButton(removeButtonState);
        setUploadedCountInfo(0);
        resetInfo();
        setTotalCountInfo();
    });
    
    //파일첨부성공
    uploader.on('success', function(evt) {
        var successCount = evt.success;
        var removeButtonState = (successCount > 0);
        $uploadedCount.html(successCount);
        disableRemoveButton(removeButtonState);
        setUploadedCountInfo(successCount);
        resetInfo();
    });
    
    //삭제버튼클릭
    $removeButton.on('click', function() {
        var checkedItems = uploader.getCheckedList();
        uploader.removeList(checkedItems);
        setTotalCountInfo();
    });    	
}

/*********************************************************
 * TUI.UPLOADER disable remove button
 ******************************************************** */
function disableRemoveButton(state) {
    var className = 'tui-is-disabled';
    if (state) {
        $removeButton.addClass(className);
    } else {
        $removeButton.removeClass(className);
    }
    $removeButton.prop('disabled', false);
}

/*********************************************************
 * TUI.UPLOADER update checked info
 ******************************************************** */
function updateCheckedInfo(size, count) {
    $checkedItemSize.html(size);
    $checkedItemCount.html(count);
}

/*********************************************************
 * TUI.UPLOADER set uploaded count info
 ******************************************************** */
function setUploadedCountInfo(count) {
    $uploadedCount.html(count);
}

/*********************************************************
 * TUI.UPLOADER set total count info
 ******************************************************** */
function setTotalCountInfo() {
    $itemTotalSize.html(uploader.getTotalSize());
}

/*********************************************************
 * TUI.UPLOADER reset info
 ******************************************************** */
function resetInfo() {
    $itemTotalSize.html('0 KB');
    $checkedItemSize.html('0 KB');
    $checkedItemCount.html('0');
}