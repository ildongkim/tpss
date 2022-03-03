package tpss.com.cmm.ses.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.com.cmm.ComDefaultVO;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import egovframework.rte.psl.dataaccess.util.EgovMap;
import tpss.com.cmm.ses.service.ComSampleService;
import tpss.com.cmm.ses.service.SampleVO;

@Service("comSampleService")
public class ComSampleServiceImpl extends EgovAbstractServiceImpl implements ComSampleService {

	@Resource(name="comSampleDAO")
	private ComSampleDAO comSampleDAO;

	@Override
	public void insertSample(EgovMap egovMap) throws Exception  {
		comSampleDAO.insertSample(egovMap);
	}
	
	@Override
	public List<?> selectSample(ComDefaultVO vo) throws Exception {
   		return comSampleDAO.selectSample(vo);
	}
	
	@Override
	public SampleVO downloadSample(SampleVO sampleVO) throws Exception {
   		return comSampleDAO.downloadSample(sampleVO);
	}
	
	@Override
	public void deleteSample(List<SampleVO> sampleVOList) throws Exception {
   		comSampleDAO.deleteSample(sampleVOList);
	}
}