/**
* 版权所有：蚂蚁与咖啡的故事
*====================================================
* 文件名称: AbstractFileServerHandler.java
* 修订记录：
* No    日期				作者(操作:具体内容)
* 1.    2017-08-28			liuyuanxian(创建:创建文件)
*====================================================
* 类描述：(说明未实现或其它不应生成javadoc的内容)
* 
*/
package org.lyx.file.server.handler.processor;

import org.lyx.file.Account;

public abstract class AbstractFileServerHandler {

	protected Account account;

	public AbstractFileServerHandler(Account account) {
		this.account = account;
	}
	
	public AbstractFileServerHandler() {
		super();
	}
	/**
	 * 服务器真实路径
	 * @param savePath 数据库保存的路径
	 * @return
	 * @author liuyuanxian
	 */
	protected String getRealPath(String savePath) {
		return this.account.getRootPath() + savePath;
	}
	
}
