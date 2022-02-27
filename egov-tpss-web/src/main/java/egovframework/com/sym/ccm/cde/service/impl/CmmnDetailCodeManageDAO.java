package egovframework.com.sym.ccm.cde.service.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import egovframework.com.cmm.service.CmmnDetailCode;
import egovframework.com.cmm.service.impl.EgovComAbstractDAO;
import egovframework.com.sym.ccm.cde.service.CmmnDetailCodeVO;

/**
*
* 공통상세코드에 대한 데이터 접근 클래스를 정의한다
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

@Repository("CmmnDetailCodeManageDAO")
public class CmmnDetailCodeManageDAO extends EgovComAbstractDAO {

    /**
	 * 공통상세코드 목록을 조회한다.
     * @param cmmnDetailCodeVO
     * @return List(공통상세코드 목록)
     * @throws Exception
     */
    public List<?> selectCmmnDetailCodeList(CmmnDetailCodeVO cmmnDetailCodeVO) throws Exception {
        return list("CmmnDetailCodeManage.selectCmmnDetailCodeList", cmmnDetailCodeVO);
    }
	
	/**
	 * 공통상세코드를 등록한다.
	 * @param cmmnDetailCode
	 * @throws Exception
	 */
	public void insertCmmnDetailCode(CmmnDetailCode cmmnDetailCode) throws Exception{
		insert("CmmnDetailCodeManage.insertCmmnDetailCode", cmmnDetailCode);
		
	}
	
	/**
	 * 공통상세코드를 삭제한다.
	 * @param cmmnDetailCode
	 * @throws Exception
	 */
	public void deleteCmmnDetailCode(CmmnDetailCode cmmnDetailCode) throws Exception{
		delete("CmmnDetailCodeManage.deleteCmmnDetailCode", cmmnDetailCode);
		
	}
    
}