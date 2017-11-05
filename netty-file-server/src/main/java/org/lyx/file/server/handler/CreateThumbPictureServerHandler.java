/**
 * 版权所有：蚂蚁与咖啡的故事
 *====================================================
 * 文件名称: CreateThumbPictureHandler.java
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
 *<pre><b><font color="blue">CreateThumbPictureServerHandler</font></b></pre>
 *
 *<pre><b>生成缩略图操作</b></pre>
 * <pre></pre>
 * <pre>
 * <b>--样例--</b>
 *   CreateThumbPictureServerHandler obj = new CreateThumbPictureServerHandler();
 *   obj.method();
 * </pre>
 * @author  <b>landyChris</b>
 */
public class CreateThumbPictureServerHandler extends AbstractFileServerHandler implements FileServerProcessor{
	private static final Logger LOGGER = LoggerFactory.getLogger(CreateThumbPictureServerHandler.class);

	public CreateThumbPictureServerHandler(Account account) {
		super(account);
	}

	public Result process(RequestParam reqParams) {
		Result result = new Result();
		result.setCode(false);
		result.setAction(EnumFileAction.CREATE_THUMB_PICTURE.getValue());

		if (StringUtils.isNotBlank(reqParams.getFilePath())) {
			String realPath = getRealPath(reqParams.getFilePath());
			
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("生成缩略图：" + realPath);
			}
			File file = new File(realPath);

			boolean bool = false;
			if (file.exists()) {
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("生成缩略图");
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