/**
 * 版权所有：蚂蚁与咖啡的故事
 *====================================================
 * 文件名称: CreateThumbPictureClientHandler.java
 * 修订记录：
 * No    日期				作者(操作:具体内容)
 * 1.    2017-08-29			liuyuanxian(创建:创建文件)
 *====================================================
 * 类描述：(说明未实现或其它不应生成javadoc的内容)
 * 
 */
package org.lyx.file.client.handler;

import java.net.URI;

import org.jboss.netty.handler.codec.http.multipart.HttpDataFactory;
import org.jboss.netty.handler.codec.http.multipart.HttpPostRequestEncoder;
import org.lyx.file.Constants;
import org.lyx.file.client.utils.enumobj.EnumUploadAction;
/**
 * 
 *<pre><b><font color="blue">CreateThumbPictureClientHandler</font></b></pre>
 *
 *<pre><b>客户端生成缩略图处理器</b></pre>
 * <pre></pre>
 * <pre>
 * <b>--样例--</b>
 *   CreateThumbPictureClientHandler obj = new CreateThumbPictureClientHandler();
 *   obj.method();
 * </pre>
 * @author  <b>landyChris</b>
 */
public class CreateThumbPictureClientHandler extends WrapFileClientHandler {
	private String filePath;

	public CreateThumbPictureClientHandler(String host, URI uri,
			String userName, String pwd, String filePath) {
		super(host, uri, userName, pwd);
		this.filePath = filePath;
	}

	public HttpPostRequestEncoder wrapRequestData(HttpDataFactory factory) {
		HttpPostRequestEncoder bodyRequestEncoder = null;
		try {
			bodyRequestEncoder = new HttpPostRequestEncoder(factory,
					getRequest(), false);
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (HttpPostRequestEncoder.ErrorDataEncoderException e) {
			e.printStackTrace();
		}
		try {
			//设置请求方式
			bodyRequestEncoder.addBodyAttribute("getform", "POST");
			//设置文件操作类型
			bodyRequestEncoder.addBodyAttribute(Constants.ACTION_KEY,
					EnumUploadAction.CREATE_THUMB_PICTURE.getValue());
			//设置文件路径
			bodyRequestEncoder.addBodyAttribute(Constants.FILE_PATH_KEY, this.filePath);
			//设置鉴权
			bodyRequestEncoder
					.addBodyAttribute(Constants.USER_NAME_KEY, super.getUserName());
			bodyRequestEncoder.addBodyAttribute(Constants.PWD_KEY, super.getPwd());
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