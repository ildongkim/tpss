package tpss.com.cmm.ses.service.impl;

import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Repository;

import egovframework.com.cmm.ComDefaultVO;
import egovframework.com.cmm.service.impl.EgovComAbstractDAO;
import egovframework.rte.psl.dataaccess.util.EgovMap;
import tpss.com.cmm.ses.service.SampleVO;

@Repository("comSampleDAO")
public class ComSampleDAO extends EgovComAbstractDAO {

    public void insertSample(EgovMap egovMap) {
        insert("ComSampleDAO.insertSample", egovMap);
    }
    
	public List<EgovMap> selectSample(ComDefaultVO vo) throws Exception{
		return selectList("ComSampleDAO.selectSample", vo);
	}
	
	public SampleVO downloadSample(SampleVO sampleVO) throws Exception{
		return selectOne("ComSampleDAO.downloadSample", sampleVO);
	}
	
    public void deleteSample(List<SampleVO> sampleVOList) {
		Iterator<?> iter = sampleVOList.iterator();
		SampleVO sampleVO;
		while (iter.hasNext()) {
			sampleVO = (SampleVO) iter.next();
			delete("ComSampleDAO.deleteSample", sampleVO);
		}
    }	
}