package tpss.com.cmm.ses.service;

import java.util.List;

import egovframework.com.cmm.ComDefaultVO;
import egovframework.rte.psl.dataaccess.util.EgovMap;

public interface ComSampleService {
	public void insertSample(EgovMap egovMap) throws Exception;
	List<?> selectSample(ComDefaultVO vo) throws Exception;
	public SampleVO downloadSample(SampleVO sampleVO) throws Exception;
	public void deleteSample(List<SampleVO> sampleVOList) throws Exception;
}