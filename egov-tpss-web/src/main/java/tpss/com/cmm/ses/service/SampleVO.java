package tpss.com.cmm.ses.service;

public class SampleVO  {
	private static final long serialVersionUID = 1L;
	private int rowKey;
	private String cntry;
	private String name;
	private String sFileName;
	private String sFileType;
	private Long sFileSize;
	private byte[] sFile;
	
	public int getRowKey() {
		return rowKey;
	}
	public void setRowKey(int rowKey) {
		this.rowKey = rowKey;
	}
	public String getCntry() {
		return cntry;
	}
	public void setCntry(String cntry) {
		this.cntry = cntry;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getsFileName() {
		return sFileName;
	}
	public void setsFileName(String sFileName) {
		this.sFileName = sFileName;
	}
	public String getsFileType() {
		return sFileType;
	}
	public void setsFileType(String sFileType) {
		this.sFileType = sFileType;
	}
	public Long getsFileSize() {
		return sFileSize;
	}
	public void setsFileSize(Long sFileSize) {
		this.sFileSize = sFileSize;
	}
	public byte[] getsFile() {
		return sFile;
	}
	public void setsFile(byte[] sFile) {
		this.sFile = sFile;
	}
}
