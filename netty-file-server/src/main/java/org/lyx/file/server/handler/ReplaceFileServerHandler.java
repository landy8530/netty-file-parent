/**
 * 版权所有：福建邮科电信业务部厦门研发中心 
 *====================================================
 * 文件名称: ReplaceFileHandler.java
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
import org.lyx.file.Result;
import org.lyx.file.server.parse.RequestParam;
import org.lyx.file.server.utils.common.ThumbUtil;
import org.lyx.file.server.utils.enumobj.EnumFileAction;

public class ReplaceFileServerHandler extends AbstractFileServerHandler implements FileServerHandler {
	private static final Log log = LogFactory.getLog(ReplaceFileServerHandler.class);

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
			
			
			log.info("进行替换文件：" + realPath);
			File oldFile = new File(realPath);
			if ((oldFile.exists()) && (oldFile.isFile())) {
				oldFile.delete();
			} else {
				result.setMsg("替换的文件不存在");
				log.info("替换的文件不存在：" + realPath);
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
				log.info("文件替换上传成功");
				result.setFilePath(reqParams.getFilePath());
				if ((bool) && (thumbBool)) {
					log.info("生成缩略图");
					
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
				log.error("文件替换报错" + e + ",acount:" + this.account);
			}
		}
		return result;
	}
}