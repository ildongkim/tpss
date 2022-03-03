package tpss.com.cmm.ses.web;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.poi.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import egovframework.com.cmm.ComDefaultVO;
import egovframework.com.cmm.service.EgovProperties;
import egovframework.com.utl.fcc.service.EgovStringUtil;
import egovframework.rte.psl.dataaccess.util.EgovMap;
import tpss.com.cmm.ses.service.ComSampleService;
import tpss.com.cmm.ses.service.SampleVO;

@Controller
public class ComSampleController {

	/** log */
	private static final Logger LOGGER = LoggerFactory.getLogger(ComSampleController.class);

	@Resource(name = "comSampleService")
	private ComSampleService comSampleService;
	
	@PostMapping(value="/cmm/ses/downloadSample.do")
	public ModelAndView downloadSample(@RequestBody SampleVO sampleVO) throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("jsonView");
		SampleVO resultVO = comSampleService.downloadSample(sampleVO);
		if (resultVO == null || "".equals(EgovStringUtil.isNullToString(resultVO.getsFileName()))) {
			modelAndView.addObject("result", false);
		} else {
			String synchrnServerPath = EgovProperties.getProperty("Globals.fileStorePath");
			File outFile = new File(synchrnServerPath+resultVO.getsFileName());
			FileOutputStream outputStream = new FileOutputStream(outFile);
			outputStream.write(resultVO.getsFile());
			outputStream.close();			
			modelAndView.addObject("result", true);
		}
		return modelAndView;		
	}
	
	@PostMapping(value="/cmm/ses/selectSample.do")
	public ModelAndView selectSample(@RequestParam Map<String, Object> commandMap) throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("jsonView");
		
		ComDefaultVO searchVO = new ComDefaultVO();
		EgovMap contents = new EgovMap();
		contents.put("contents", comSampleService.selectSample(searchVO));
		modelAndView.addObject("data", contents);
		modelAndView.addObject("result", true);
		
		return modelAndView;
	}
	
	@RequestMapping("/cmm/ses/insertSample.do")
	public ModelAndView insertSample(
			MultipartHttpServletRequest multipartRequest,
			@RequestPart(value="egovMap") List<EgovMap> egovMapList) throws Exception {
    	ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("jsonView");
		
		MultipartFile multipartFile;
		InputStream inputstream;
		byte[] file;
		for (EgovMap egovMap : egovMapList) {
			multipartFile = multipartRequest.getFile("file_"+egovMap.get("rowKey"));
			if (multipartFile != null) {
				inputstream = multipartFile.getInputStream();
				file = IOUtils.toByteArray(inputstream);
				egovMap.put("sFile", file);
				egovMap.put("sFileName", multipartFile.getOriginalFilename());
				egovMap.put("sFileSize", multipartFile.getSize());
				egovMap.put("sFileType", multipartFile.getContentType());
			}
			comSampleService.insertSample(egovMap);
		}
		return modelAndView;
	}
	
	@PostMapping(value="/cmm/ses/deleteSample.do")
	public ModelAndView deleteSample(@RequestBody List<SampleVO> sampleVOList) throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("jsonView");
		comSampleService.deleteSample(sampleVOList);
		return modelAndView;		
	}	
}