/**
 * 版权所有：福建邮科电信业务部厦门研发中心 
 *====================================================
 * 文件名称: DeleteFileHandler.java
 * 修订记录：
 * No    日期				作者(操作:具体内容)
 * 1.    2013-4-15			liuyuanxian(创建:创建文件)
 *====================================================
 * 类描述：(说明未实现或其它不应生成javadoc的内容)
 * 
 */
package org.lyx.file.server.handler;

import java.io.File;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.lyx.file.Account;
import org.lyx.file.Constants;
import org.lyx.file.Result;
import org.lyx.file.server.parse.RequestParam;
import org.lyx.file.server.utils.enumobj.EnumFileAction;

public class DeleteFileServerHandler extends AbstractFileServerHandler implements FileServerHandler {
	private static final Log log = LogFactory.getLog(DeleteFileServerHandler.class);

	public DeleteFileServerHandler(Account account) {
		super(account);
	}

	public Result process(RequestParam reqParams) {
		Result result = new Result();
		result.setCode(false);
		result.setAction(EnumFileAction.DELETE_FILE.getValue());

		if (StringUtils.isNotBlank(reqParams.getFilePath())) {
			String realPath = getRealPath(reqParams.getFilePath());
			if (log.isDebugEnabled()) {
				log.debug("进行删除文件：" + realPath);
			}
			File file = new File(realPath);

			boolean bool = false;
			if ((file.exists()) && (file.isFile())) {
				bool = file.delete();
			}

			int position = realPath.lastIndexOf(".");
			String suffix = realPath.substring(position);
			String thumbPath = realPath.substring(0, position) + Constants.THUMB_SUFFIX
					+ suffix;

			File thumbFile = new File(thumbPath);

			if ((thumbFile.exists()) && (thumbFile.isFile()) && (bool)) {
				thumbFile.delete();
			}
			result.setCode(bool);
			result.setMsg("文件删除成功");
		}
		if (!result.isCode()) {
			result.setMsg("文件删除失败");
		}
		return result;
	}
}