/**
 * 版权所有：蚂蚁与咖啡的故事
 *====================================================
 * 文件名称: DeleteFileClientHandler.java
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
 *<pre><b><font color="blue">DeleteFileClientHandler</font></b></pre>
 *
 *<pre><b>客户端删除文件处理器</b></pre>
 * <pre></pre>
 * <pre>
 * <b>--样例--</b>
 *   DeleteFileClientHandler obj = new DeleteFileClientHandler();
 *   obj.method();
 * </pre>
 * @author  <b>landyChris</b>
 */
public class DeleteFileClientHandler extends WrapFileClientHandler {
	private String filePath;

	public DeleteFileClientHandler(String host, URI uri, String filePath,
			String userName, String pwd) {
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
					EnumUploadAction.DELETE_FILE.getValue());
			//设置文件路径
			bodyRequestEncoder.addBodyAttribute(Constants.FILE_PATH_KEY, this.filePath);
			//鉴权
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