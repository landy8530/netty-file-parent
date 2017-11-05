/**
 * 版权所有：蚂蚁与咖啡的故事
 *====================================================
 * 文件名称: UploadParamParser.java
 * 修订记录：
 * No    日期				作者(操作:具体内容)
 * 1.    2017-08-29			liuyuanxian(创建:创建文件)
 *====================================================
 * 类描述：(说明未实现或其它不应生成javadoc的内容)
 * 
 */
package org.lyx.file.server.parse;

import java.util.List;

import org.jboss.netty.handler.codec.http.multipart.Attribute;
import org.jboss.netty.handler.codec.http.multipart.FileUpload;
import org.jboss.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import org.jboss.netty.handler.codec.http.multipart.InterfaceHttpData;
import org.lyx.file.Constants;
/**
 * 
 *<pre><b><font color="blue">RequestParamParser</font></b></pre>
 *
 *<pre><b>请求参数解析工具类</b></pre>
 * <pre></pre>
 * <pre>
 * <b>--样例--</b>
 *   RequestParamParser obj = new RequestParamParser();
 *   obj.method();
 * </pre>
 * @author  <b>landyChris</b>
 */
public class RequestParamParser {
	public static void parseParams(HttpPostRequestDecoder decoder,
			RequestParam requestParams) {
		if (decoder == null) {
			return;
		}
		if (requestParams == null)
			requestParams = new RequestParam();
		try {
			List<InterfaceHttpData> datas = decoder.getBodyHttpDatas();
			if (datas != null) {
				for (InterfaceHttpData data : datas)
					if (data.getHttpDataType() == InterfaceHttpData.HttpDataType.Attribute) {
						//接收到的是普通参数
						Attribute attribute = (Attribute) data;
						String value = attribute.getValue();
						String name = attribute.getName();

						if (Constants.PWD_KEY.equals(name))
							requestParams.setPwd(value);
						else if (Constants.USER_NAME_KEY.equals(name))
							requestParams.setUserName(value);
						else if (Constants.THUMB_MARK_KEY.equals(name))
							requestParams.setThumbMark(value);
						else if (Constants.ACTION_KEY.equals(name))
							requestParams.setAction(value);
						else if (Constants.FILE_PATH_KEY.equals(name))
							requestParams.setFilePath(value);
						else if (Constants.FILE_NAME_KEY.equals(name))
							requestParams.setFileName(value);
						else
							requestParams.putOtherParam(name, value);
					} else if (data.getHttpDataType() == InterfaceHttpData.HttpDataType.FileUpload) {
						//接收到的是文件
						FileUpload fileUpload = (FileUpload) data;
						if (fileUpload.isCompleted()) {
							requestParams.setFileUpload(fileUpload);
							requestParams.setFileContentType(fileUpload.getContentType());
						}
					}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}