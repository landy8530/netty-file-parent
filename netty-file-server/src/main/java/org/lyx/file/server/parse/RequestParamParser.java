/**
 * 版权所有：福建邮科电信业务部厦门研发中心 
 *====================================================
 * 文件名称: UploadParamParser.java
 * 修订记录：
 * No    日期				作者(操作:具体内容)
 * 1.    2013-4-15			liuyuanxian(创建:创建文件)
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
						FileUpload fileUpload = (FileUpload) data;
						if (fileUpload.isCompleted()) {
							requestParams.setFileUpload(fileUpload);

							requestParams.setFileContentType(fileUpload
									.getContentType());
						}
					}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}