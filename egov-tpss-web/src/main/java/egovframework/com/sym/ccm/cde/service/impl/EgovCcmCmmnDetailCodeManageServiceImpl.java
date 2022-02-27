package egovframework.com.sym.ccm.cde.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.com.cmm.service.CmmnDetailCode;
import egovframework.com.sym.ccm.cde.service.CmmnDetailCodeVO;
import egovframework.com.sym.ccm.cde.service.EgovCcmCmmnDetailCodeManageService;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

/**
*
* 공통상세코드에 대한 서비스 구현클래스를 정의한다
* @author 공통서비스 개발팀 이중호
* @since 2009.04.01
* @version 1.0
* @see
*
* <pre>
* << 개정이력(Modification Information) >>
*
*   수정일      수정자           수정내용
*  -------    --------    ---------------------------
*   2009.04.01  이중호          최초 생성
*
* </pre>
*/
@Service("CmmnDetailCodeManageService")
public class EgovCcmCmmnDetailCodeManageServiceImpl extends EgovAbstractServiceImpl implements EgovCcmCmmnDetailCodeManageService{
	
    @Resource(name="CmmnDetailCodeManageDAO")
    private CmmnDetailCodeManageDAO cmmnDetailCodeManageDAO;
    
	/**
	 * 공통상세코드 목록을 조회한다.
	 */
	@Override
	public List<?> selectCmmnDetailCodeList(CmmnDetailCodeVO cmmnDetailCodeVO) throws Exception {
        return cmmnDetailCodeManageDAO.selectCmmnDetailCodeList(cmmnDetailCodeVO);
	}

	/**
	 * 공통상세코드를 등록한다.
	 * @throws Exception 
	 */
	@Override
	public void insertCmmnDetailCode(CmmnDetailCode cmmnDetailCode) throws Exception {
		cmmnDetailCodeManageDAO.insertCmmnDetailCode(cmmnDetailCode);
		
	}
	
	/**
	 * 공통상세코드를 삭제한다.
	 * @throws Exception 
	 */
	@Override
	public void deleteCmmnDetailCode(CmmnDetailCode cmmnDetailCode) throws Exception {
		cmmnDetailCodeManageDAO.deleteCmmnDetailCode(cmmnDetailCode);
		
	}
	
}
