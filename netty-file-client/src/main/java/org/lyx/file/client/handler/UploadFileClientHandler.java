/**
 * 版权所有：蚂蚁与咖啡的故事
 *====================================================
 * 文件名称: UploadFileClientHandler.java
 * 修订记录：
 * No    日期				作者(操作:具体内容)
 * 1.    2017-08-29			liuyuanxian(创建:创建文件)
 *====================================================
 * 类描述：(说明未实现或其它不应生成javadoc的内容)
 * 
 */
package org.lyx.file.client.handler;

import java.io.File;
import java.net.URI;

import org.jboss.netty.handler.codec.http.multipart.HttpDataFactory;
import org.jboss.netty.handler.codec.http.multipart.HttpPostRequestEncoder;
import org.lyx.file.Constants;
import org.lyx.file.client.utils.enumobj.EnumUploadAction;
/**
 * 
 *<pre><b><font color="blue">UploadFileClientHandler</font></b></pre>
 *
 *<pre><b>客户端文件上传处理器</b></pre>
 * <pre></pre>
 * <pre>
 * <b>--样例--</b>
 *   UploadFileClientHandler obj = new UploadFileClientHandler();
 *   obj.method();
 * </pre>
 * @author  <b>landyChris</b>
 */
public class UploadFileClientHandler extends WrapFileClientHandler {
	private File file;
	private String thumbMark = Constants.THUMB_MARK_NO;
	private String fileName;

	public UploadFileClientHandler(String host, URI uri, File file,
			String fileName, String thumbMark, String userName, String pwd) {
		super(host, uri, userName, pwd);
		this.file = file;
		this.thumbMark = thumbMark;
		this.fileName = fileName;
	}

	public HttpPostRequestEncoder wrapRequestData(HttpDataFactory factory) {
		HttpPostRequestEncoder bodyRequestEncoder = null;
		try {
			bodyRequestEncoder = new HttpPostRequestEncoder(factory,
					getRequest(), true);
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (HttpPostRequestEncoder.ErrorDataEncoderException e) {
			e.printStackTrace();
		}
		try {
			//设置请求方式post
			bodyRequestEncoder.addBodyAttribute("getform", "POST");
			//设置文件操作类型为文件上传
			bodyRequestEncoder.addBodyAttribute(Constants.ACTION_KEY,
					EnumUploadAction.UPLOAD_FILE.getValue());
			//设置是否需要缩略图
			bodyRequestEncoder.addBodyAttribute(Constants.THUMB_MARK_KEY, this.thumbMark);
			//设置账户鉴权
			bodyRequestEncoder
					.addBodyAttribute(Constants.USER_NAME_KEY, super.getUserName());
			bodyRequestEncoder.addBodyAttribute(Constants.PWD_KEY, super.getPwd());
			//设置文件名称
			bodyRequestEncoder.addBodyAttribute(Constants.FILE_NAME_KEY, this.fileName);
			//设置文件内容
			bodyRequestEncoder.addBodyFileUpload("myfile", this.file,
					"application/x-zip-compressed", false);
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (HttpPostRequestEncoder.ErrorDataEncoderException e) {
			e.printStackTrace();
		}
		try {
			bodyRequestEncoder.finalizeRequest();
		} catch (HttpPostRequestEncoder.ErrorDataEncoderException e) {
			e.printStackTrace();
		}
		return bodyRequestEncoder;
	}
}