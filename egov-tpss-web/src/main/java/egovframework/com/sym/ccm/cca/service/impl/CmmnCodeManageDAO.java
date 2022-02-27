package egovframework.com.sym.ccm.cca.service.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import egovframework.com.cmm.ComDefaultVO;
import egovframework.com.cmm.service.impl.EgovComAbstractDAO;
import egovframework.com.sym.ccm.cca.service.CmmnCode;

/**
*
* 공통코드에 대한 데이터 접근 클래스를 정의한다
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

@Repository("CmmnCodeManageDAO")
public class CmmnCodeManageDAO extends EgovComAbstractDAO {

   /**
	 * 공통코드 목록을 조회한다.
     * @param searchVO
     * @return List(공통코드 목록)
     * @throws Exception
     */
	public List<?> selectCmmnCodeList(ComDefaultVO searchVO) throws Exception{
		 return list("CmmnCodeManage.selectCmmnCodeList", searchVO);
	}

	/**
	 * 공통코드를 등록한다.
	 * @param cmmnCode
	 * @throws Exception
	 */
	public void insertCmmnCode(CmmnCode cmmnCode) throws Exception{
		insert("CmmnCodeManage.insertCmmnCode", cmmnCode);
	}
	
	/**
	 * 공통코드를 삭제한다.
	 * @param cmmnCode
	 * @throws Exception
	 */
	public void deleteCmmnCode(CmmnCode cmmnCode) {
		delete("CmmnCodeManage.deleteCmmnCode", cmmnCode);
	}

}
