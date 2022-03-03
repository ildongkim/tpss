package tpss.com.cmm.ses.web;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.ModelAndViewDefiningException;

import egovframework.com.cmm.ComDefaultVO;
import egovframework.com.cmm.EgovBrowserUtil;
import egovframework.com.cmm.util.EgovResourceCloseHelper;
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
	public void downloadSample(@RequestParam Map<String, Object> commandMap,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		SampleVO sampleVO = new SampleVO();
		sampleVO.setCntry(EgovStringUtil.isNullToString(commandMap.get("cntry")));
		sampleVO.setName(EgovStringUtil.isNullToString(commandMap.get("name")));
		sampleVO.setsFileName(EgovStringUtil.isNullToString(commandMap.get("sFileName")));
		sampleVO.setsFileType(EgovStringUtil.isNullToString(commandMap.get("sFileType")));
		SampleVO resultVO = comSampleService.downloadSample(sampleVO);
		if (resultVO == null || "".equals(EgovStringUtil.isNullToString(resultVO.getsFileName()))) {
			ModelAndView modelAndView = new ModelAndView("redirect:/cmm/init/error.do");
			throw new ModelAndViewDefiningException(modelAndView);
		} else {
			String mimetype = "application/x-msdownload";
			String userAgent = request.getHeader("User-Agent");
			HashMap<String,String> browser = EgovBrowserUtil.getBrowser(userAgent);
			if ( !EgovBrowserUtil.MSIE.equals(browser.get(EgovBrowserUtil.TYPEKEY)) ) {
				mimetype = "application/x-stuff";
			}
			String contentDisposition = EgovBrowserUtil.getDisposition(resultVO.getsFileName(),userAgent,"UTF-8");
			response.setContentType(mimetype);
			response.setHeader("Content-Disposition", contentDisposition);
			BufferedInputStream in = null;
			BufferedOutputStream out = null;
			try {
				InputStream is = new ByteArrayInputStream(resultVO.getsFile());
				in = new BufferedInputStream(is);
				out = new BufferedOutputStream(response.getOutputStream());
				FileCopyUtils.copy(in, out);
				out.flush();
			} catch (FileNotFoundException ex) {
				ModelAndView modelAndView = new ModelAndView("redirect:/cmm/init/error.do");
				throw new ModelAndViewDefiningException(modelAndView);
			} catch (IOException ex) {
				ModelAndView modelAndView = new ModelAndView("redirect:/cmm/init/error.do");
				throw new ModelAndViewDefiningException(modelAndView);
			} finally {
				EgovResourceCloseHelper.close(out);
			}
		}
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
	
	@PostMapping("/cmm/ses/insertSample.do")
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