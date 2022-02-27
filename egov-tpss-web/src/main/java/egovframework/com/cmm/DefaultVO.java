package egovframework.com.cmm;

import java.io.Serializable;

/**
 * @Class Name : DefaultVO.java
 * @Description : DefaultVO class
 * @Modification Information
 * @
 * @  수정일           수정자               수정내용
 * @ -------     --------    ---------------------------
 * @ 2022.02.03  김일동              최초 생성
 *
 *  @author Harry
 *  @since 2022.02.03
 *  @version 1.0
 *  @see 
 *  
 */
@SuppressWarnings("serial")
public class DefaultVO implements Serializable {

	/** 등록자 아이디 */
	private String registerId = "";
	
	/** 사용여부 */
	private String useAt = "";

	public String getRegisterId() {
		return registerId;
	}

	public void setRegisterId(String registerId) {
		this.registerId = registerId;
	}

	public String getUseAt() {
		return useAt;
	}

	public void setUseAt(String useAt) {
		this.useAt = useAt;
	}
	
}