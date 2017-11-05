/**
 * 版权所有：福建邮科电信业务部厦门研发中心 
 *====================================================
 * 文件名称: UploadParam.java
 * 修订记录：
 * No    日期				作者(操作:具体内容)
 * 1.    2013-4-15			liuyuanxian(创建:创建文件)
 *====================================================
 * 类描述：(说明未实现或其它不应生成javadoc的内容)
 * 
 */
package org.lyx.file.server.parse;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.jboss.netty.handler.codec.http.multipart.FileUpload;
import org.lyx.file.Constants;
@SuppressWarnings("all")
public class RequestParam {
	private String action;
	private String userName;
	private String pwd;
	private Map<String, String> otherParams = new HashMap<String, String>();

	private String thumbMark = Constants.THUMB_MARK_NO;
	private FileUpload fileUpload;
	private String filePath;
	private String fileName;
	private String fileContentType;

	public String getAction() {
		return this.action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPwd() {
		return this.pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public Map<String, String> getOtherParams() {
		return this.otherParams;
	}

	public FileUpload getFileUpload() {
		return this.fileUpload;
	}

	public void setFileUpload(FileUpload fileUpload) {
		this.fileUpload = fileUpload;
	}

	public String getThumbMark() {
		return this.thumbMark;
	}

	public void setThumbMark(String thumbMark) {
		this.thumbMark = thumbMark;
	}

	public void putOtherParam(String key, String value) {
		this.otherParams.put(key, value);
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("NETTY WEB Server\r\n");
		sb.append("===================================\r\n");
		sb.append("\r\n\r\n");
		if (StringUtils.isNotBlank(getUserName())) {
			sb.append("UserName=" + getUserName() + "\r\n");
		}
		if (StringUtils.isNotBlank(getPwd())) {
			sb.append("pwd=" + getPwd() + "\r\n");
		}
		if (StringUtils.isNotBlank(getAction())) {
			sb.append("action=" + getAction() + "\r\n");
		}
		if (StringUtils.isNotBlank(this.fileName)) {
			sb.append("fileName=" + getFileName() + "\r\n");
		}
		if (StringUtils.isNotBlank(this.fileContentType)) {
			sb.append("fileContentType=" + getFileContentType() + "\r\n");
		}
		if (this.otherParams != null) {
			for (Map.Entry item : this.otherParams.entrySet()) {
				sb.append((String) item.getKey() + "="
						+ (String) item.getValue() + "\r\n");
			}
		}
		return sb.toString();
	}

	public String getFileName() {
		return this.fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileContentType() {
		return this.fileContentType;
	}

	public void setFileContentType(String fileContentType) {
		this.fileContentType = fileContentType;
	}

	public String getFilePath() {
		return this.filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
}