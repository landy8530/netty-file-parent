/**
 * 版权所有：蚂蚁与咖啡的故事
 *====================================================
 * 文件名称: ReplaceFileHandler.java
 * 修订记录：
 * No    日期				作者(操作:具体内容)
 * 1.    2017-08-29			liuyuanxian(创建:创建文件)
 *====================================================
 * 类描述：(说明未实现或其它不应生成javadoc的内容)
 * 
 */
package org.lyx.file.server.handler;

import java.io.File;

import org.apache.commons.lang3.StringUtils;
import org.lyx.file.Account;
import org.lyx.file.Result;
import org.lyx.file.server.handler.processor.AbstractFileServerHandler;
import org.lyx.file.server.handler.processor.FileServerProcessor;
import org.lyx.file.server.parse.RequestParam;
import org.lyx.file.server.utils.common.ThumbUtil;
import org.lyx.file.server.utils.enumobj.EnumFileAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 
 *<pre><b><font color="blue">ReplaceFileServerHandler</font></b></pre>
 *
 *<pre><b>替换文件操作</b></pre>
 * <pre></pre>
 * <pre>
 * <b>--样例--</b>
 *   ReplaceFileServerHandler obj = new ReplaceFileServerHandler();
 *   obj.method();
 * </pre>
 * @author  <b>landyChris</b>
 */
public class ReplaceFileServerHandler extends AbstractFileServerHandler implements FileServerProcessor {
	private static final Logger LOGGER = LoggerFactory.getLogger(ReplaceFileServerHandler.class);

	public ReplaceFileServerHandler(Account account) {
		super(account);
	}

	public Result process(RequestParam reqParams) {
		Result result = new Result();
		result.setCode(false);
		result.setAction(EnumFileAction.REPLACE_FILE.getValue());

		if ((StringUtils.isNotBlank(reqParams.getFilePath()))
				&& (reqParams.getFileUpload() != null)) {
			String realPath = getRealPath(reqParams.getFilePath());
			
			
			LOGGER.info("进行替换文件：" + realPath);
			File oldFile = new File(realPath);
			if ((oldFile.exists()) && (oldFile.isFile())) {
				oldFile.delete();
			} else {
				result.setMsg("替换的文件不存在");
				LOGGER.info("替换的文件不存在：" + realPath);
				return result;
			}

			String thumbFilePath = ThumbUtil.getThumbImagePath(realPath);
			File thumbFile = new File(thumbFilePath);

			boolean thumbBool = false;
			if ((thumbFile.exists()) && (thumbFile.isFile())) {
				thumbFile.delete();
				thumbBool = true;
			}
			try {
				boolean bool = reqParams.getFileUpload().renameTo(oldFile);
				result.setCode(bool);
				result.setMsg("文件替换上传成功");
				LOGGER.info("文件替换上传成功");
				result.setFilePath(reqParams.getFilePath());
				if ((bool) && (thumbBool)) {
					LOGGER.info("生成缩略图");
					
					new ThumbUtil(oldFile, thumbFile, this.account.getThumbWidth(),
							this.account.getThumbHeight()).createThumbImage();
					/*ThumbUtil.createThumbImage(oldFile, thumbFile,
							this.account.getThumbWidth(),
							this.account.getThumbHeight());*/
				}
			} catch (Exception e) {
				e.printStackTrace();
				result.setCode(false);
				result.setMsg("文件替换报错" + e + ",acount:" + this.account);
				LOGGER.error("文件替换报错" + e + ",acount:" + this.account);
			}
		}
		return result;
	}
}