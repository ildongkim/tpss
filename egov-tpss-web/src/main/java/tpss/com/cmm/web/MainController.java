package tpss.com.cmm.web;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import egovframework.com.cmm.ComDefaultCodeVO;
import egovframework.com.cmm.ComDefaultVO;
import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.cmm.LoginVO;
import egovframework.com.cmm.service.CmmnDetailCode;
import egovframework.com.cmm.service.EgovCmmUseService;
import egovframework.com.cmm.service.EgovProperties;
import egovframework.com.cmm.service.Globals;
import egovframework.com.cmm.util.EgovUserDetailsHelper;
import egovframework.com.uat.uia.service.EgovLoginService;
import egovframework.com.utl.fcc.service.EgovStringUtil;
import egovframework.rte.fdl.property.EgovPropertyService;
import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;

@Controller
public class MainController implements ApplicationContextAware, InitializingBean {

	private ApplicationContext applicationContext;

	private static final Logger LOGGER = LoggerFactory.getLogger(MainController.class);

	public void afterPropertiesSet() throws Exception {}

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
		LOGGER.info("MainController setApplicationContext method has called!");
	}

	@Resource(name = "egovMessageSource")
	EgovMessageSource egovMessageSource;
	
	@Resource(name = "loginService")
	private EgovLoginService loginService;
	
	@RequestMapping("/cmm/maintop.do")
	public String top() {
		return "tpss/com/main_top";
	}

	@RequestMapping("/cmm/mainbottom.do")
	public String bottom() {
		return "tpss/com/main_bottom";
	}
	
	@RequestMapping("/cmm/maincontent.do")
	public String setMainContent(ModelMap model) throws Exception {
		return "tpss/com/main_content";
	}

	/**
	 * ????????? ??? ?????????????????? ??????
	 * @param
	 * @return ????????? ?????????
	 * @exception Exception
	 */
	@RequestMapping(value = "/cmm/actionMain.do")
	public String actionMain(ModelMap model) throws Exception {

		// 1. Spring Security ??????????????? ??????
		Boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();
		if (!isAuthenticated) {
			model.addAttribute("loginMessage", egovMessageSource.getMessage("fail.common.login"));
			return "tpss/com/init/LoginUsr";
		}
		
		LoginVO user = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		LOGGER.debug("User Id : {}", user == null ? "" : EgovStringUtil.isNullToString(user.getId()));

		// 2. ?????? ????????? ??????
		String main_page = Globals.MAIN_PAGE;
		LOGGER.debug("Globals.MAIN_PAGE > " + Globals.MAIN_PAGE);
		LOGGER.debug("main_page > {}", main_page);

		if (main_page.startsWith("/")) {
			return "forward:" + main_page;
		} else {
			return main_page;
		}
	}
	
	/**
	 * ???????????? ???????????? ????????? ????????????.
	 * Cookie??? egovLatestServerTime, egovExpireSessionTime ??????????????? ??????.
	 * @return result - String
	 * @exception Exception
	 */
	@RequestMapping(value="/uat/uia/noticeExpirePwd.do")
	public String noticeExpirePwd(@RequestParam Map<String, Object> commandMap, ModelMap model) throws Exception {
		
		// ????????? ???????????? ??????????????? ????????????. ex) 180?????? ???????????? ????????? ???????????? ????????? 180??? 
		String propertyExpirePwdDay = EgovProperties.getProperty("Globals.ExpirePwdDay");
		int expirePwdDay = 0 ;
		try {
			expirePwdDay =  Integer.parseInt(propertyExpirePwdDay);
		} catch (NumberFormatException e) {
			LOGGER.debug("convert expirePwdDay Err : "+e.getMessage());
		} catch (Exception e) {
			LOGGER.debug("convert expirePwdDay Err : "+e.getMessage());
		}
		
		model.addAttribute("expirePwdDay", expirePwdDay);

		// ???????????? ?????????????????? ????????? ???????????? ????????????. ex) 3?????? ???????????? ????????? 3??? ??????
		LoginVO loginVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		model.addAttribute("loginVO", loginVO);
		int passedDayChangePWD = 0;
		if ( loginVO != null ) {
			LOGGER.debug("===>>> loginVO.getId() = "+loginVO.getId());
			LOGGER.debug("===>>> loginVO.getUniqId() = "+loginVO.getUniqId());
			LOGGER.debug("===>>> loginVO.getUserSe() = "+loginVO.getUserSe());
			// ???????????? ????????? ????????? ??????
			passedDayChangePWD = loginService.selectPassedDayChangePWD(loginVO);
			LOGGER.debug("===>>> passedDayChangePWD = "+passedDayChangePWD);
			model.addAttribute("passedDay", passedDayChangePWD);
		}
		
		// ????????????????????? ????????? ?????? => ex)1?????? ??????????????? 1??? ??????
		model.addAttribute("elapsedTimeExpiration", passedDayChangePWD - expirePwdDay);
		
		return "egovframework/com/uat/uia/EgovExpirePwd";
	}
}
