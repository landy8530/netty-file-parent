/**
 * 版权所有：福建邮科电信业务部厦门研发中心 
 *====================================================
 * 文件名称: FileHandlerFactory.java
 * 修订记录：
 * No    日期				作者(操作:具体内容)
 * 1.    2013-4-15			liuyuanxian(创建:创建文件)
 *====================================================
 * 类描述：(说明未实现或其它不应生成javadoc的内容)
 * 
 */
package org.lyx.file.server.handler;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.lyx.file.Account;
import org.lyx.file.Result;
import org.lyx.file.server.FileServerContainer;
import org.lyx.file.server.parse.RequestParam;
import org.lyx.file.server.utils.common.JSONUtil;
import org.lyx.file.server.utils.enumobj.EnumFileAction;

public class FileServerHandlerFactory {
	private static Log log = LogFactory.getLog(FileServerHandlerFactory.class);

	public static String process(RequestParam requestParams) {
		Account account = FileServerContainer.getInstance().getAccount(
				requestParams.getUserName());
		EnumFileAction action = EnumFileAction.converByValue(requestParams
				.getAction());
		Result result = null;
		if (account.auth(requestParams.getPwd())) {
			FileServerHandler handler = null;
			if (EnumFileAction.UPLOAD_FILE == action) {//上传文件
				if (requestParams.getFileUpload() != null) {
					handler = new UploadFileServerHandler(account);
				}
			} else if (EnumFileAction.DELETE_FILE == action) {//删除文件
				if (StringUtils.isNotBlank(requestParams.getFilePath())) {
					handler = new DeleteFileServerHandler(account);
				}
			} else if (EnumFileAction.REPLACE_FILE == action) {//替换文件
				if ((requestParams.getFileUpload() != null)
						&& (StringUtils.isNotBlank(requestParams.getFilePath()))) {
					handler = new ReplaceFileServerHandler(account);
				}
			} else if ((EnumFileAction.CREATE_THUMB_PICTURE == action)
					&& (StringUtils.isNotBlank(requestParams.getFilePath()))) {//生成缩略图
				handler = new CreateThumbPictureServerHandler(account);
			}
			if(handler != null) {
				result = handler.process(requestParams);
			}
		} else {
			result = new Result();
			result.setAction(EnumFileAction.NULL.getValue());
			result.setCode(false);
			result.setMsg("密码错误");
		}
		if (result == null) {
			result = new Result();
			result.setAction(EnumFileAction.NULL.getValue());
			result.setCode(false);
			result.setMsg("无效动作");
		}

		String json = JSONUtil.toJSONString(result);
		if (log.isDebugEnabled()) {
			log.debug("执行结果:" + json);
		}
		return json;
	}
}