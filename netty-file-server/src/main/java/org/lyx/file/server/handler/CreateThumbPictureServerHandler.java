/**
 * 版权所有：福建邮科电信业务部厦门研发中心 
 *====================================================
 * 文件名称: CreateThumbPictureHandler.java
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

public class CreateThumbPictureServerHandler extends AbstractFileServerHandler implements FileServerHandler{
	private static final Log log = LogFactory
			.getLog(CreateThumbPictureServerHandler.class);

	public CreateThumbPictureServerHandler(Account account) {
		super(account);
	}

	public Result process(RequestParam reqParams) {
		Result result = new Result();
		result.setCode(false);
		result.setAction(EnumFileAction.CREATE_THUMB_PICTURE.getValue());

		if (StringUtils.isNotBlank(reqParams.getFilePath())) {
			String realPath = getRealPath(reqParams.getFilePath());
			
			if (log.isDebugEnabled()) {
				log.debug("生成缩略图：" + realPath);
			}
			File file = new File(realPath);

			boolean bool = false;
			if (file.exists()) {
				if (log.isDebugEnabled()) {
					log.debug("生成缩略图");
				}
				String thumbFilePath = ThumbUtil
						.getThumbImagePath(realPath);
				File thumbFile = new File(thumbFilePath);
				if (!thumbFile.exists()) {
					new ThumbUtil(new File(realPath), thumbFile, this.account.getThumbWidth(),
							this.account.getThumbHeight()).createThumbImage();
					/*ThumbUtil.createThumbImage(new File(realPath),
							thumbFile, this.account.getThumbWidth(),
							this.account.getThumbHeight());*/
					result.setCode(bool);
					result.setMsg("缩略图创建成功");
				} else {
					result.setCode(bool);
					result.setMsg("缩略图已存在，无法创建；缩略图路径=" + thumbFilePath);
				}
			}
		}

		return result;
	}
}