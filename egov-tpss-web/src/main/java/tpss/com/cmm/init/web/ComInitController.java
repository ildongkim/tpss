package tpss.com.cmm.init.web;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import egovframework.com.cmm.ComDefaultCodeVO;
import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.cmm.EgovWebUtil;
import egovframework.com.cmm.LoginVO;
import egovframework.com.cmm.config.EgovLoginConfig;
import egovframework.com.cmm.service.EgovCmmUseService;
import egovframework.com.cmm.service.EgovProperties;
import egovframework.com.cmm.util.EgovUserDetailsHelper;
import egovframework.com.uat.uia.service.EgovLoginService;
import egovframework.com.uss.umt.service.EgovMberManageService;
import egovframework.com.uss.umt.service.MberManageVO;
import egovframework.com.uss.umt.service.UserDefaultVO;

/**
 * 비로그인 서비스를 처리하는 컨트롤러 클래스
 * 로그인, 회원가입, 약관동의, 아이디중복체크, initValidator
 * @author Harry
 * @since 2022.01.27
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *  수정일                    수정자                수정내용
 *  ----------   --------   ---------------------------
 *  2022.01.27   김일동                최초생성
 *  
 *  </pre>
 */
@Controller
public class ComInitController {

	/** EgovLoginService */
	@Resource(name = "loginService")
	private EgovLoginService loginService;

	/** EgovCmmUseService */
	@Resource(name = "EgovCmmUseService")
	private EgovCmmUseService cmmUseService;

	/** EgovMessageSource */
	@Resource(name = "egovMessageSource")
	EgovMessageSource egovMessageSource;

	@Resource(name = "egovLoginConfig")
	EgovLoginConfig egovLoginConfig;

	/** mberManageService */
	@Resource(name = "mberManageService")
	private EgovMberManageService mberManageService;
	
	@Resource(name = "egovPageLinkWhitelist")
    protected List<String> egovWhitelist;

	@Resource(name = "egovNextUrlWhitelist")
    protected List<String> nextUrlWhitelist;
	
	/** log */
	private static final Logger LOGGER = LoggerFactory.getLogger(ComInitController.class);

	@RequestMapping("/cmm/init/index.do")
	public String index(ModelMap model) throws Exception {
		
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
		
		return "tpss/com/init/index";
	}

	/**
	 * 로그아웃한다.
	 * @return String
	 * @exception Exception
	 */
	@RequestMapping(value = "/cmm/init/actionLogout.do")
	public String actionLogout(HttpServletRequest request, ModelMap model) throws Exception {
		request.getSession().setAttribute("loginVO", null);
		request.getSession().setAttribute("accessUser", null);
		return "redirect:/j_spring_security_logout";
	}

	/**
	 * 아이디/비밀번호 찾기 화면으로 들어간다
	 * @param
	 * @return 아이디/비밀번호 찾기 페이지
	 * @exception Exception
	 */
	@RequestMapping(value = "/cmm/init/searchIdPassword.do")
	public String idPasswordSearchView(ModelMap model) throws Exception {

		// 1. 비밀번호 힌트 공통코드 조회
		ComDefaultCodeVO vo = new ComDefaultCodeVO();
		vo.setCodeId("COM022");
		List<?> code = cmmUseService.selectCmmCodeDetail(vo);
		model.addAttribute("pwhtCdList", code);

		return "tpss/com/init/SearchIdPassword";
	}

	/**
	 * 아이디를 찾는다.
	 * @param vo - 이름, 이메일주소, 사용자구분이 담긴 LoginVO
	 * @return result - 아이디
	 * @exception Exception
	 */
	@RequestMapping(value = "/cmm/init/searchId.do")
	public String searchId(@ModelAttribute("loginVO") LoginVO loginVO, ModelMap model) throws Exception {

		if (loginVO == null || loginVO.getName() == null || loginVO.getName().equals("") && loginVO.getEmail() == null || loginVO.getEmail().equals("")
				&& loginVO.getUserSe() == null || loginVO.getUserSe().equals("")) {
			return "tpss/com/init/error/error";
		}

		// 1. 아이디 찾기
		loginVO.setName(loginVO.getName().replaceAll(" ", ""));
		LoginVO resultVO = loginService.searchId(loginVO);

		if (resultVO != null && resultVO.getId() != null && !resultVO.getId().equals("")) {

			model.addAttribute("resultInfo", "아이디는 " + resultVO.getId() + " 입니다.");
			return "tpss/com/init/SearchResultIdPassword";
		} else {
			model.addAttribute("resultInfo", egovMessageSource.getMessage("fail.common.idsearch"));
			return "tpss/com/init/SearchResultIdPassword";
		}
	}

	/**
	 * 비밀번호를 찾는다.
	 * @param vo - 아이디, 이름, 이메일주소, 비밀번호 힌트, 비밀번호 정답, 사용자구분이 담긴 LoginVO
	 * @return result - 임시비밀번호전송결과
	 * @exception Exception
	 */
	@RequestMapping(value = "/cmm/init/searchPassword.do")
	public String searchPassword(@ModelAttribute("loginVO") LoginVO loginVO, ModelMap model) throws Exception {

		//KISA 보안약점 조치 (2018-10-29, 윤창원)
		if (loginVO == null || loginVO.getId() == null || loginVO.getId().equals("") && loginVO.getName() == null || "".equals(loginVO.getName()) && loginVO.getEmail() == null
				|| loginVO.getEmail().equals("") && loginVO.getPasswordHint() == null || "".equals(loginVO.getPasswordHint()) && loginVO.getPasswordCnsr() == null
				|| "".equals(loginVO.getPasswordCnsr()) && loginVO.getUserSe() == null || "".equals(loginVO.getUserSe())) {
			return "tpss/com/init/error/error";
		}

		// 1. 비밀번호 찾기
		boolean result = loginService.searchPassword(loginVO);

		// 2. 결과 리턴
		if (result) {
			model.addAttribute("resultInfo", "임시 비밀번호를 발송하였습니다.");
			return "tpss/com/init/SearchResultIdPassword";
		} else {
			model.addAttribute("resultInfo", egovMessageSource.getMessage("fail.common.pwsearch"));
			return "tpss/com/init/SearchResultIdPassword";
		}
	}

	/**
	 * 일반회원 약관확인
	 * @param model 화면모델
	 * @return EgovStplatCnfirm
	 * @throws Exception
	 */
	@RequestMapping("/cmm/init/EgovStplatCnfirmMber.do")
	public String sbscrbCnfirmMber(ModelMap model) throws Exception {

		//일반회원용 약관 아이디 설정
		String stplatId = "STPLAT_0000000000001";
		//회원가입유형 설정-일반회원
		String sbscrbTy = "USR01";
		//약관정보 조회
		List<?> stplatList = mberManageService.selectStplat(stplatId);
		model.addAttribute("stplatList", stplatList); //약관정보 포함
		model.addAttribute("sbscrbTy", sbscrbTy); //회원가입유형 포함

		return "tpss/com/init/EgovStplatCnfirm";
	}
	
	/**
	 * 실명인증확인화면 호출(주민번호)
	 * @param model 모델
	 * @return EgovStplatCnfirm
	 * @exception Exception
	 */
	@RequestMapping("/cmm/init/EgovRlnmCnfirm.do")
	public String rlnmCnfirm(ModelMap model, @RequestParam Map<String, Object> commandMap) throws Exception {

		model.addAttribute("ihidnum", (String) commandMap.get("ihidnum"));			//주민번호
		model.addAttribute("realname", (String) commandMap.get("realname"));		//사용자이름
		model.addAttribute("sbscrbTy", (String) commandMap.get("sbscrbTy"));		//사용자유형
		model.addAttribute("nextUrlName", (String) commandMap.get("nextUrlName"));	//다음단계버튼명(이동할 URL에 따른)
		String nextUrl = (String) commandMap.get("nextUrl");
		if ( nextUrl == null ) nextUrl = "";
		model.addAttribute("nextUrl", nextUrl);										//다음단계로 이동할 URL
		String result = "";

//		if ("".equals((String) commandMap.get("ihidnum"))) {
//			result = "info.user.rlnmCnfirm";
//			model.addAttribute("result", result); 	//실명확인 결과
//			return "egovframework/com/sec/rnc/EgovRlnmCnfirm";
//		}
//
//		try {
//			result = rlnmManageService.rlnmCnfirm((String) commandMap.get("ihidnum"), (String) commandMap.get("realname"), (String) commandMap.get("sbscrbTy"));
//		} finally {
//			model.addAttribute("result_tmp", result + "__" + result.substring(0, 2));
//			if (result.substring(0, 2).equals("00")) {
//				result = "success.user.rlnmCnfirm";
//			} else if (result.substring(0, 2).equals("01")) {
//				result = "fail.user.rlnmCnfirm";
//			} else {
//				result = "fail.user.connectFail";
//			}
//			model.addAttribute("result", result);		//실명확인 결과
//
//		}
		
		// 화이트 리스트 처리
		// whitelist목록에 있는 경우 결과가 true, 결과가 false인경우 FAIL처리
		if (nextUrlWhitelist.contains(nextUrl) == false) {
			LOGGER.debug("nextUrl WhiteList Error! Please check whitelist!");
			nextUrl="tpss/com/init/error/error";
		}
		
		// 안전한 경로 문자열로 조치
		nextUrl = EgovWebUtil.filePathBlackList(nextUrl);

		
//		return "egovframework/com/sec/rnc/EgovRlnmCnfirm";
		// 실명인증기능 미탑재로 바로 회원가입 페이지로 이동.
		return "forward:" + nextUrl;
	}
	
	/**
	 * 일반회원가입신청 등록화면으로 이동한다.
	 * @param userSearchVO 검색조건
	 * @param mberManageVO 일반회원가입신청정보
	 * @param commandMap 파라메터전달용 commandMap
	 * @param model 화면모델
	 * @return EgovMberSbscrb
	 * @throws Exception
	 */
	@RequestMapping("/cmm/init/EgovMberSbscrbView.do")
	public String sbscrbMberView(@ModelAttribute("userSearchVO") UserDefaultVO userSearchVO, @ModelAttribute("mberManageVO") MberManageVO mberManageVO,
			@RequestParam Map<String, Object> commandMap, ModelMap model) throws Exception {

		ComDefaultCodeVO vo = new ComDefaultCodeVO();

		//패스워드힌트목록을 코드정보로부터 조회
		vo.setCodeId("COM022");
		List<?> passwordHint_result = cmmUseService.selectCmmCodeDetail(vo);
		//성별구분코드를 코드정보로부터 조회
		vo.setCodeId("COM014");
		List<?> sexdstnCode_result = cmmUseService.selectCmmCodeDetail(vo);

		model.addAttribute("passwordHint_result", passwordHint_result); //패스워트힌트목록
		model.addAttribute("sexdstnCode_result", sexdstnCode_result); //성별구분코드목록
		if (!"".equals(commandMap.get("realname"))) {
			model.addAttribute("mberNm", commandMap.get("realname")); //실명인증된 이름 - 주민번호 인증
			model.addAttribute("ihidnum", commandMap.get("ihidnum")); //실명인증된 주민등록번호 - 주민번호 인증
		}
		if (!"".equals(commandMap.get("realName"))) {
			model.addAttribute("mberNm", commandMap.get("realName")); //실명인증된 이름 - ipin인증
		}

		mberManageVO.setMberSttus("DEFAULT");

		return "tpss/com/init/EgovMberSbscrb";
	}

	/**
	 * 일반회원가입신청등록처리후로그인화면으로 이동한다.
	 * @param mberManageVO 일반회원가입신청정보
	 * @return loginUsr.do
	 * @throws Exception
	 */
	@RequestMapping("/cmm/init/EgovMberSbscrb.do")
	public String sbscrbMber(@ModelAttribute("mberManageVO") MberManageVO mberManageVO) throws Exception {

		//가입상태 초기화
		mberManageVO.setMberSttus("A");
		//그룹정보 초기화
		//mberManageVO.setGroupId("1");
		//일반회원가입신청 등록시 일반회원등록기능을 사용하여 등록한다.
		mberManageService.insertMber(mberManageVO);
		return "tpss/com/init/LoginUsr";
	}
	
	/**
	 * 입력한 사용자아이디의 중복확인화면 이동
	 * @param model 화면모델
	 * @return uss/umt/EgovIdDplctCnfirm
	 * @throws Exception
	 */
	@RequestMapping(value = "/cmm/init/EgovIdDplctCnfirmView.do")
	public String checkIdDplct(ModelMap model) throws Exception {

		// 미인증 사용자에 대한 보안처리
		Boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();
		if (!isAuthenticated) {
			return "tpss/com/init/index";
		}

		model.addAttribute("checkId", "");
		model.addAttribute("usedCnt", "-1");
		return "tpss/com/init/EgovIdDplctCnfirm";
	}
	
	/**
	 * 입력한 사용자아이디의 중복여부를 체크하여 사용가능여부를 확인
	 * @param commandMap 파라메터전달용 commandMap
	 * @param model 화면모델
	 * @return uss/umt/EgovIdDplctCnfirm
	 * @throws Exception
	 */
	@RequestMapping(value = "/cmm/init/EgovIdDplctCnfirmAjax.do")
	public ModelAndView checkIdDplctAjax(@RequestParam Map<String, Object> commandMap) throws Exception {

    	ModelAndView modelAndView = new ModelAndView();
    	modelAndView.setViewName("jsonView");

		String checkId = (String) commandMap.get("checkId");
		//checkId = new String(checkId.getBytes("ISO-8859-1"), "UTF-8");

		int usedCnt = mberManageService.checkIdDplct(checkId);
		modelAndView.addObject("usedCnt", usedCnt);
		modelAndView.addObject("checkId", checkId);

		return modelAndView;
	}
	
    /**
	 * 권한제한 화면 이동
	 * @return String
	 * @exception Exception
	 */
    @RequestMapping("/cmm/init/accessDenied.do")
    public String accessDenied()
            throws Exception {
        return "tpss/com/init/error/accessDenied";
    }
    
    /**
	 * 모달조회
	 * @return String
	 * @exception Exception
	 */
    @RequestMapping(value="/cmm/init/EgovModal.do")
    public String selectUtlJsonInquire()  throws Exception {
        return "tpss/com/init/EgovModal";
    }
    
    /**
	 * validato rule dynamic Javascript
	 */
	@RequestMapping("/cmm/init/validator.do")
	public String validate(){
		return "tpss/com/init/validator";
	}
	
    /**
	 * JSP 호출작업만 처리하는 공통 함수
	 */
	@RequestMapping(value="/cmm/init/EgovPageLink.do")
	public String moveToPage(@RequestParam("link") String linkPage){
		String link = linkPage;
		link = link.replace(";", "");
		link = link.replace(".", "");
		
		// service 사용하여 리턴할 결과값 처리하는 부분은 생략하고 단순 페이지 링크만 처리함
		if (linkPage==null || linkPage.equals("")){
			link="tpss/com/init/error/error";
		}
		
		// 화이트 리스트 처리
		// whitelist목록에 있는 경우 결과가 true, 결과가 false인경우 FAIL처리
		if (egovWhitelist.contains(linkPage) == false) {
			LOGGER.debug("Page Link WhiteList Error! Please check whitelist!");
			link="tpss/com/init/error/erro";
		}
		
		// 안전한 경로 문자열로 조치
		link = EgovWebUtil.filePathBlackList(link);
		
		return link;
	}
}