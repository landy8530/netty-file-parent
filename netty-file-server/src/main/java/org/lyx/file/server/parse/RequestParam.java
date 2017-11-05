/**
 * 版权所有：蚂蚁与咖啡的故事
 *====================================================
 * 文件名称: UploadParam.java
 * 修订记录：
 * No    日期				作者(操作:具体内容)
 * 1.    2017-08-29			liuyuanxian(创建:创建文件)
 *====================================================
 * 类描述：(说明未实现或其它不应生成javadoc的内容)
 * 
 */
package org.lyx.file.server.parse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.jboss.netty.handler.codec.http.multipart.FileUpload;
import org.lyx.file.Constants;
/**
 *
 *<pre><b><font color="blue">RequestParam</font></b></pre>
 *
 *<pre><b>请求参数</b></pre>
 * <pre></pre>
 * <pre>
 * <b>--样例--</b>
 *   RequestParam obj = new RequestParam();
 *   obj.method();
 * </pre>
 * @author  <b>landyChris</b>
 */
public class RequestParam {
	//文件操作类型
	private String action;
	//鉴权信息
	private String userName;
	private String pwd;
	private Map<String, String> otherParams = new HashMap<String, String>();
	//是否需要转为缩略图
	private String thumbMark = Constants.THUMB_MARK_NO;
	//上传的文件对象
	private FileUpload fileUpload;
	//上传的文件路径
	private String filePath;
	//上传的文件名称
	private String fileName;
	//上传的文件contentType
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
		sb.append("\r\nNETTY WEB Server\r\n");
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
		if (fileUpload != null) {
			try {
				sb.append("fileSize=" + fileUpload.getFile().length()/1024 + " KB \r\n");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (this.otherParams != null) {
			for (Map.Entry<String,String> item : this.otherParams.entrySet()) {
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