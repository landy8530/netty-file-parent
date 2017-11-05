/**
 * 版权所有：蚂蚁与咖啡的故事
 *====================================================
 * 文件名称: FileHandlerFactory.java
 * 修订记录：
 * No    日期				作者(操作:具体内容)
 * 1.    2017-08-29			liuyuanxian(创建:创建文件)
 *====================================================
 * 类描述：(说明未实现或其它不应生成javadoc的内容)
 * 
 */
package org.lyx.file.server.handler.factory;

import org.apache.commons.lang3.StringUtils;
import org.lyx.file.Account;
import org.lyx.file.Result;
import org.lyx.file.server.FileServerContainer;
import org.lyx.file.server.handler.CreateThumbPictureServerHandler;
import org.lyx.file.server.handler.DeleteFileServerHandler;
import org.lyx.file.server.handler.ReplaceFileServerHandler;
import org.lyx.file.server.handler.UploadFileServerHandler;
import org.lyx.file.server.handler.processor.FileServerProcessor;
import org.lyx.file.server.parse.RequestParam;
import org.lyx.file.server.utils.common.JSONUtil;
import org.lyx.file.server.utils.enumobj.EnumFileAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileServerHandlerFactory {
	private static final Logger LOGGER = LoggerFactory.getLogger(FileServerHandlerFactory.class);

	/**
	 * 根据请求的不同参数，进行相应的文件处理操作
	 * @param requestParams
	 * @return
	 * @author:landyChris
	 */
	public static String process(RequestParam requestParams) {
		Account account = FileServerContainer.getInstance().getAccount(
				requestParams.getUserName());
		EnumFileAction action = EnumFileAction.converByValue(requestParams
				.getAction());
		Result result = null;
		if (account.auth(requestParams.getPwd())) {
			FileServerProcessor handler = null;
			if (EnumFileAction.UPLOAD_FILE == action) {//上传文件
				if (requestParams.getFileUpload() != null) {
					LOGGER.info("进行文件上传操作....");
					handler = new UploadFileServerHandler(account);
				}
			} else if (EnumFileAction.DELETE_FILE == action) {//删除文件
				if (StringUtils.isNotBlank(requestParams.getFilePath())) {
					LOGGER.info("进行文件删除操作....");
					handler = new DeleteFileServerHandler(account);
				}
			} else if (EnumFileAction.REPLACE_FILE == action) {//替换文件
				if ((requestParams.getFileUpload() != null)
						&& (StringUtils.isNotBlank(requestParams.getFilePath()))) {
					LOGGER.info("进行文件替换操作....");
					handler = new ReplaceFileServerHandler(account);
				}
			} else if ((EnumFileAction.CREATE_THUMB_PICTURE == action)
					&& (StringUtils.isNotBlank(requestParams.getFilePath()))) {//生成缩略图
				LOGGER.info("进行生成缩略图操作....");
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
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("执行结果:" + json);
		}
		return json;
	}
}