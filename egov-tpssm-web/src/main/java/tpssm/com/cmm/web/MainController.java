package tpssm.com.cmm.web;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.cmm.LoginVO;
import egovframework.com.cmm.service.EgovProperties;
import egovframework.com.cmm.service.Globals;
import egovframework.com.cmm.util.EgovUserDetailsHelper;
import egovframework.com.sym.mnu.mpm.service.EgovMenuManageService;
import egovframework.com.sym.mnu.mpm.service.MenuManageVO;
import egovframework.com.uat.uia.service.EgovLoginService;
import egovframework.com.utl.fcc.service.EgovStringUtil;

@Controller
public class MainController implements ApplicationContextAware, InitializingBean {

	private ApplicationContext applicationContext;

	private static final Logger LOGGER = LoggerFactory.getLogger(MainController.class);

	public void afterPropertiesSet() throws Exception {}

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
		
		LOGGER.info("MainController setApplicationContext method has called!");
	}

	/** EgovMessageSource */
	@Resource(name = "egovMessageSource")
	EgovMessageSource egovMessageSource;
	
	/** EgovLoginService */
	@Resource(name = "loginService")
	private EgovLoginService loginService;
	
    /** EgovMenuManageService */
	@Resource(name = "meunManageService")
    private EgovMenuManageService menuManageService;
	
	@RequestMapping("/cmm/main.do")
	public String main(ModelMap model) throws Exception {

		// 설정된 비밀번호 유효기간을 가져온다. ex) 180이면 비밀번호 변경후 만료일이 앞으로 180일 
		String propertyExpirePwdDay = EgovProperties.getProperty("Globals.ExpirePwdDay");
		int expirePwdDay = 0 ;
		try {
			expirePwdDay =  Integer.parseInt(propertyExpirePwdDay);
		} catch (Exception e) {
			LOGGER.debug("convert expirePwdDay Err : "+e.getMessage());
		}
		
		model.addAttribute("expirePwdDay", expirePwdDay);

		// 비밀번호 설정일로부터 몇일이 지났는지 확인한다. ex) 3이면 비빌번호 설정후 3일 경과
		LoginVO loginVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		model.addAttribute("loginVO", loginVO);
		int passedDayChangePWD = 0;
		if ( loginVO != null ) {
			LOGGER.debug("===>>> loginVO.getId() = "+loginVO.getId());
			LOGGER.debug("===>>> loginVO.getUniqId() = "+loginVO.getUniqId());
			LOGGER.debug("===>>> loginVO.getUserSe() = "+loginVO.getUserSe());
			// 비밀번호 변경후 경과한 일수
			passedDayChangePWD = loginService.selectPassedDayChangePWD(loginVO);
			LOGGER.debug("===>>> passedDayChangePWD = "+passedDayChangePWD);
			model.addAttribute("passedDay", passedDayChangePWD);
		}
		
		// 만료일자로부터 경과한 일수 => ex)1이면 만료일에서 1일 경과
		model.addAttribute("elapsedTimeExpiration", passedDayChangePWD - expirePwdDay);
		
		return "tpssm/com/main";
	}
	
	@RequestMapping("/cmm/maintop.do")
	public String top() {
		return "tpssm/com/main_top";
	}

	@RequestMapping("/cmm/mainbottom.do")
	public String bottom() {
		return "tpssm/com/main_bottom";
	}
	
	@RequestMapping("/cmm/mainleft.do")
	public String left(ModelMap model) throws Exception {
    	LoginVO user =
			(LoginVO)EgovUserDetailsHelper.getAuthenticatedUser();
    	
		// 2. 메뉴조회
		MenuManageVO menuManageVO = new MenuManageVO();
    	menuManageVO.setTmpId(user == null ? "" : EgovStringUtil.isNullToString(user.getId()));
    	menuManageVO.setTmpPassword(user == null ? "" : EgovStringUtil.isNullToString(user.getPassword()));
    	menuManageVO.setTmpUserSe(user == null ? "" : EgovStringUtil.isNullToString(user.getUserSe()));
    	menuManageVO.setTmpName(user == null ? "" : EgovStringUtil.isNullToString(user.getName()));
    	menuManageVO.setTmpEmail(user == null ? "" : EgovStringUtil.isNullToString(user.getEmail()));
    	menuManageVO.setTmpOrgnztId(user == null ? "" : EgovStringUtil.isNullToString(user.getOrgnztId()));
    	menuManageVO.setTmpUniqId(user == null ? "" : EgovStringUtil.isNullToString(user.getUniqId()));
    	
    	List<?> list_menulist = menuManageService.selectMainMenuLeft(menuManageVO);
		model.addAttribute("list_menulist", list_menulist);
		
		return "tpssm/com/main_left";
	}
	
	@RequestMapping("/cmm/maincontent.do")
	public String setMainContent(ModelMap model) throws Exception {
		return "tpssm/com/main_content";
	}

	/**
	 * 로그인 후 메인화면으로 들어간다
	 * @param
	 * @return 로그인 페이지
	 * @exception Exception
	 */
	@RequestMapping(value = "/cmm/actionMain.do")
	public String actionMain(ModelMap model) throws Exception {

		// 1. Spring Security 사용자권한 처리
		Boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();
		if (!isAuthenticated) {
			model.addAttribute("loginMessage", egovMessageSource.getMessage("fail.common.login"));
			return "tpssm/com/init/EgovLoginUsr";
		}
		LoginVO user = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		
		LOGGER.debug("User Id : {}", user == null ? "" : EgovStringUtil.isNullToString(user.getId()));

		// 3. 메인 페이지 이동
		String main_page = Globals.MAIN_PAGE;

		LOGGER.debug("Globals.MAIN_PAGE > " + Globals.MAIN_PAGE);
		LOGGER.debug("main_page > {}", main_page);

		if (main_page.startsWith("/")) {
			return "forward:" + main_page;
		} else {
			return main_page;
		}

		/*
		if (main_page != null && !main_page.equals("")) {

			// 3-1. 설정된 메인화면이 있는 경우
			return main_page;

		} else {

			// 3-2. 설정된 메인화면이 없는 경우
			if (user.getUserSe().equals("USR")) {
				return "egovframework/com/EgovMainView";
			} else {
				return "egovframework/com/EgovMainViewG";
			}
		}
		*/
	}
	
	/**
	 * 세션타임아웃 시간을 연장한다.
	 * Cookie에 egovLatestServerTime, egovExpireSessionTime 기록하도록 한다.
	 * @return result - String
	 * @exception Exception
	 */
	@RequestMapping(value="/cmm/refreshSessionTimeout.do")
	public ModelAndView refreshSessionTimeout(@RequestParam Map<String, Object> commandMap) throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("jsonView");

		modelAndView.addObject("result", "ok");

		return modelAndView;
	}
	
	/**
	 * 비밀번호 유효기간 팝업을 출력한다.
	 * Cookie에 egovLatestServerTime, egovExpireSessionTime 기록하도록 한다.
	 * @return result - String
	 * @exception Exception
	 */
	@RequestMapping(value="/uat/uia/noticeExpirePwd.do")
	public String noticeExpirePwd(@RequestParam Map<String, Object> commandMap, ModelMap model) throws Exception {
		
		// 설정된 비밀번호 유효기간을 가져온다. ex) 180이면 비밀번호 변경후 만료일이 앞으로 180일 
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

		// 비밀번호 설정일로부터 몇일이 지났는지 확인한다. ex) 3이면 비빌번호 설정후 3일 경과
		LoginVO loginVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		model.addAttribute("loginVO", loginVO);
		int passedDayChangePWD = 0;
		if ( loginVO != null ) {
			LOGGER.debug("===>>> loginVO.getId() = "+loginVO.getId());
			LOGGER.debug("===>>> loginVO.getUniqId() = "+loginVO.getUniqId());
			LOGGER.debug("===>>> loginVO.getUserSe() = "+loginVO.getUserSe());
			// 비밀번호 변경후 경과한 일수
			passedDayChangePWD = loginService.selectPassedDayChangePWD(loginVO);
			LOGGER.debug("===>>> passedDayChangePWD = "+passedDayChangePWD);
			model.addAttribute("passedDay", passedDayChangePWD);
		}
		
		// 만료일자로부터 경과한 일수 => ex)1이면 만료일에서 1일 경과
		model.addAttribute("elapsedTimeExpiration", passedDayChangePWD - expirePwdDay);
		
		return "egovframework/com/uat/uia/EgovExpirePwd";
	}
	
	// context-security.xml 설정
	// csrf="true"인 경우 csrf Token이 없는경우 이동하는 페이지
	// csrfAccessDeniedUrl="/egovCSRFAccessDenied.do"
	//@RequestMapping("/egovCSRFAccessDenied.do")
	//public String egovCSRFAccessDenied() {
	//	return "egovframework/com/cmm/error/csrfAccessDenied";
	//}
}
