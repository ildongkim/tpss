package tpss.com.cmm.web;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import egovframework.com.cmm.annotation.IncludedInfo;
import egovframework.com.sym.log.lgm.service.EgovSysLogService;
import egovframework.com.sym.log.lgm.service.SysLog;
import egovframework.com.sym.log.plg.service.EgovPrivacyLogService;
import egovframework.com.sym.log.plg.service.PrivacyLog;
import egovframework.com.sym.log.ulg.service.EgovUserLogService;
import egovframework.com.sym.log.ulg.service.UserLog;
import egovframework.com.sym.log.wlg.service.EgovWebLogService;
import egovframework.com.sym.log.wlg.service.WebLog;
import egovframework.rte.fdl.property.EgovPropertyService;
import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;

/**
 * 로그를 처리하는 컨트롤러 클래스
 * @author Harry
 * @since 2022.02.07
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *  수정일                    수정자                수정내용
 *  ----------   --------   ---------------------------
 *  2022.02.07   김일동                최초생성
 *  
 *  </pre>
 */
@Controller
public class LogController {

	/** log */
	private static final Logger LOGGER = LoggerFactory.getLogger(LogController.class);
	
	@Resource(name="EgovUserLogService")
	private EgovUserLogService userLogService;

	@Resource(name="EgovWebLogService")
	private EgovWebLogService webLogService;

	@Resource(name="propertiesService")
	protected EgovPropertyService propertyService;

	@Resource(name="egovPrivacyLogService")
	private EgovPrivacyLogService privacyLogService;

	@Resource(name="EgovSysLogService")
	private EgovSysLogService sysLogService;
	
	/**
	 * 사용자 로그 목록 조회
	 *
	 * @param UserLog
	 * @return sym/log/ulg/EgovUserLogList
	 * @throws Exception
	 */
	@IncludedInfo(name="사용로그관리", listUrl= "/sym/log/ulg/SelectUserLogList.do", order = 1040 ,gid = 60)
	@RequestMapping(value="/sym/log/ulg/SelectUserLogList.do")
	public String selectUserLogInf(@ModelAttribute("searchVO") UserLog userLog,
			ModelMap model) throws Exception{

		/** EgovPropertyService.sample */
		userLog.setPageUnit(propertyService.getInt("pageUnit"));
		userLog.setPageSize(propertyService.getInt("pageSize"));

		/** pageing */
		PaginationInfo paginationInfo = new PaginationInfo();
		paginationInfo.setCurrentPageNo(userLog.getPageIndex());
		paginationInfo.setRecordCountPerPage(userLog.getPageUnit());
		paginationInfo.setPageSize(userLog.getPageSize());

		userLog.setFirstIndex(paginationInfo.getFirstRecordIndex());
		userLog.setLastIndex(paginationInfo.getLastRecordIndex());
		userLog.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());

		HashMap<?, ?> _map = (HashMap<?, ?>)userLogService.selectUserLogInf(userLog);
		int totCnt = Integer.parseInt((String)_map.get("resultCnt"));

		model.addAttribute("resultList", _map.get("resultList"));
		model.addAttribute("resultCnt", _map.get("resultCnt"));

		paginationInfo.setTotalRecordCount(totCnt);
		model.addAttribute("paginationInfo", paginationInfo);

		return "egovframework/com/sym/log/ulg/EgovUserLogList";
	}

	/**
	 * 사용자 로그 상세 조회
	 *
	 * @param userLog
	 * @param model
	 * @return sym/log/ulg/EgovUserLogInqire
	 * @throws Exception
	 */
	@RequestMapping(value="/sym/log/ulg/SelectUserLogDetail.do")
	public String selectUserLog(@ModelAttribute("searchVO") UserLog userLog,
			@RequestParam("occrrncDe") String occrrncDe,
			@RequestParam("rqesterId") String rqesterId,
			@RequestParam("srvcNm") String srvcNm,
			@RequestParam("methodNm") String methodNm,
			ModelMap model) throws Exception{

		userLog.setOccrrncDe(occrrncDe.trim());
		userLog.setRqesterId(rqesterId.trim());
		userLog.setSrvcNm(srvcNm.trim());
		userLog.setMethodNm(methodNm.trim());

		UserLog vo = userLogService.selectUserLog(userLog);
		model.addAttribute("result", vo);
		return "egovframework/com/sym/log/ulg/EgovUserLogDetail";
	}
	
	/**
	 * 개인정보조회 로그 목록 조회
	 *
	 * @param privacyLog
	 * @return sym/log/plg/EgovPrivacyLogList
	 * @throws Exception
	 */
	@IncludedInfo(name="개인정보조회로그관리", listUrl="/sym/log/plg/SelectPrivacyLogList.do", order = 1085 ,gid = 60)
	@RequestMapping(value="/sym/log/plg/SelectPrivacyLogList.do")
	public String selectPrivacyLogList(@ModelAttribute("searchVO") PrivacyLog privacyLog,
			ModelMap model) throws Exception{

		privacyLog.setPageUnit(propertyService.getInt("pageUnit"));
		privacyLog.setPageSize(propertyService.getInt("pageSize"));

		PaginationInfo paginationInfo = new PaginationInfo();
		paginationInfo.setCurrentPageNo(privacyLog.getPageIndex());
		paginationInfo.setRecordCountPerPage(privacyLog.getPageUnit());
		paginationInfo.setPageSize(privacyLog.getPageSize());

		privacyLog.setFirstIndex(paginationInfo.getFirstRecordIndex());
		privacyLog.setLastIndex(paginationInfo.getLastRecordIndex());
		privacyLog.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());

		Map<String, Object> map = privacyLogService.selectPrivacyLogList(privacyLog);
		int totalCount = Integer.parseInt((String)map.get("resultCnt"));

		model.addAttribute("resultList", map.get("resultList"));
		model.addAttribute("resultCnt", map.get("resultCnt"));

		paginationInfo.setTotalRecordCount(totalCount);
		model.addAttribute("paginationInfo", paginationInfo);

		return "egovframework/com/sym/log/plg/EgovPrivacyLogList";
	}

	/**
	 * 개인정보조회 로그 상세 조회
	 *
	 * @param privacyLog
	 * @param model
	 * @return sym/log/plg/EgovPrivacyLogInqire
	 * @throws Exception
	 */
	@RequestMapping(value="/sym/log/plg/SelectPrivacyLogDetail.do")
	public String selectWebLog(@ModelAttribute("searchVO") PrivacyLog privacyLog,
			ModelMap model) throws Exception{

		model.addAttribute("result", privacyLogService.selectPrivacyLog(privacyLog));

		return "egovframework/com/sym/log/plg/EgovPrivacyLogDetail";
	}
	
	/**
	 * 웹 로그 목록 조회
	 *
	 * @param webLog
	 * @return sym/log/wlg/EgovWebLogList
	 * @throws Exception
	 */
	@IncludedInfo(name="웹로그관리", listUrl="/sym/log/wlg/SelectWebLogList.do", order = 1070 ,gid = 60)
	@RequestMapping(value="/sym/log/wlg/SelectWebLogList.do")
	public String selectWebLogInf(@ModelAttribute("searchVO") WebLog webLog,
			ModelMap model) throws Exception{

		webLog.setPageUnit(propertyService.getInt("pageUnit"));
		webLog.setPageSize(propertyService.getInt("pageSize"));

		PaginationInfo paginationInfo = new PaginationInfo();
		paginationInfo.setCurrentPageNo(webLog.getPageIndex());
		paginationInfo.setRecordCountPerPage(webLog.getPageUnit());
		paginationInfo.setPageSize(webLog.getPageSize());

		webLog.setFirstIndex(paginationInfo.getFirstRecordIndex());
		webLog.setLastIndex(paginationInfo.getLastRecordIndex());
		webLog.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());

		HashMap<?, ?> _map = (HashMap<?, ?>)webLogService.selectWebLogInf(webLog);
		int totCnt = Integer.parseInt((String)_map.get("resultCnt"));

		model.addAttribute("resultList", _map.get("resultList"));
		model.addAttribute("resultCnt", _map.get("resultCnt"));

		paginationInfo.setTotalRecordCount(totCnt);
		model.addAttribute("paginationInfo", paginationInfo);

		return "egovframework/com/sym/log/wlg/EgovWebLogList";
	}

	/**
	 * 웹 로그 상세 조회
	 *
	 * @param webLog
	 * @param model
	 * @return sym/log/wlg/EgovWebLogInqire
	 * @throws Exception
	 */
	@RequestMapping(value="/sym/log/wlg/SelectWebLogDetail.do")
	public String selectWebLog(@ModelAttribute("searchVO") WebLog webLog,
			@RequestParam("requstId") String requstId,
			ModelMap model) throws Exception{

		webLog.setRequstId(requstId.trim());

		WebLog vo = webLogService.selectWebLog(webLog);
		model.addAttribute("result", vo);
		return "egovframework/com/sym/log/wlg/EgovWebLogDetail";
	}

	/**
	 * 시스템 로그 목록 조회
	 *
	 * @param sysLog
	 * @return sym/log/lgm/EgovSysLogList
	 * @throws Exception
	 */
	@IncludedInfo(name="로그관리", listUrl="/sym/log/lgm/SelectSysLogList.do", order = 1030 ,gid = 60)
	@RequestMapping(value="/sym/log/lgm/SelectSysLogList.do")
	public String selectSysLogInf(@ModelAttribute("searchVO") SysLog sysLog,
			ModelMap model) throws Exception{
		
    	/** EgovPropertyService.sample */
		sysLog.setPageUnit(propertyService.getInt("pageUnit"));
		sysLog.setPageSize(propertyService.getInt("pageSize"));

    	/** pageing */
    	PaginationInfo paginationInfo = new PaginationInfo();
		paginationInfo.setCurrentPageNo(sysLog.getPageIndex());
		paginationInfo.setRecordCountPerPage(sysLog.getPageUnit());
		paginationInfo.setPageSize(sysLog.getPageSize());

		sysLog.setFirstIndex(paginationInfo.getFirstRecordIndex());
		sysLog.setLastIndex(paginationInfo.getLastRecordIndex());
		sysLog.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());
		
		HashMap<?, ?> _map = (HashMap<?, ?>)sysLogService.selectSysLogInf(sysLog);
		int totCnt = Integer.parseInt((String)_map.get("resultCnt"));

		model.addAttribute("resultList", _map.get("resultList"));
		model.addAttribute("resultCnt", _map.get("resultCnt"));
		model.addAttribute("frm", sysLog);

		paginationInfo.setTotalRecordCount(totCnt);
		model.addAttribute("paginationInfo", paginationInfo);

		return "egovframework/com/sym/log/lgm/EgovSysLogList";
	}
	
	/**
	 * 시스템 로그 상세 조회
	 *
	 * @param sysLog
	 * @param model
	 * @return sym/log/lgm/EgovSysLogInqire
	 * @throws Exception
	 */
	@RequestMapping(value="/sym/log/lgm/SelectSysLogDetail.do")
	public String selectSysLog(@ModelAttribute("searchVO") SysLog sysLog,
			@RequestParam("requstId") String requstId,
			ModelMap model) throws Exception{

		sysLog.setRequstId(requstId.trim());

		SysLog vo = sysLogService.selectSysLog(sysLog);
		model.addAttribute("result", vo);
		return "egovframework/com/sym/log/lgm/EgovSysLogDetail";
	}
}